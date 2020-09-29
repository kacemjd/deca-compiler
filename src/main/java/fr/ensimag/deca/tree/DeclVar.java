package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
/**
 * @author gl53
 * @date 01/01/2020
 */
public class DeclVar extends AbstractDeclVar {


    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;
    private static final Logger LOG = Logger.getLogger(DeclVar.class);
    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    public AbstractIdentifier getNameType()
    {
        return this.type;
    }
    public AbstractIdentifier getNameVar()
    {
        return this.varName;
    }
    public AbstractInitialization getInitialization()
    {
        return this.initialization;
    }
    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
              Type nameType = this.getNameType().verifyType(compiler);
              if (nameType.isVoid())
              {
                  throw new ContextualError("Type de l'indentificateur \""+
                          this.getNameVar().getName().toString() +
                          "\" doit être différent de void (règle 3.17)", this.getLocation());
              }
              /* declaration of nameVar in the invironment */

              Type t = this.getInitialization().verifyInitialization(compiler, nameType, localEnv, currentClass);
              VariableDefinition def = new VariableDefinition(t, this.getLocation());
              Symbol symbol = this.getNameVar().getName();

              try
              {
                  localEnv.declare(symbol, def);
              }
              catch (DoubleDefException e)
              {
                  throw new ContextualError("Identificateur \"" +
                          symbol.toString() + "\" est déjà déclaré à " +
                          localEnv.get(symbol).getLocation() + " (règle 3.17)",
                          this.getLocation());
              }
              Type nameVar = this.getNameVar().verifyExpr(compiler, localEnv, currentClass);
              if (currentClass != null && currentClass.getMembers().get(symbol) != null &&
                  currentClass.getMembers().get(symbol).isMethod())
              {
                throw new ContextualError("L'identificateur \"" +
                        symbol.toString() + "\" est une méthode", this.getLocation());
              }
              //LOG.debug("End of verifyDeclVar");
    }
    @Override
    protected void codeGenVar(DecacCompiler compiler){
        int j = varName.codeGenIdent(compiler);
        initialization.codeGenInt(compiler, j);

    }
    @Override
    protected void codeGenVarM(DecacCompiler compiler){
        int j = varName.codeGenIdentM(compiler);
        initialization.codeGenIntM(compiler, j);

    }

    @Override
    public void decompile(IndentPrintStream s) {
      this.type.decompile(s);
      s.print(" ");
      this.varName.decompile(s);
      this.initialization.decompile(s);
      s.print(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
