package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.*;
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
public class DeclParam extends AbstractDeclParam
{
    final private AbstractIdentifier type;
    final private AbstractIdentifier param;


    private static final Logger LOG = Logger.getLogger(DeclParam.class);
    public DeclParam(AbstractIdentifier type, AbstractIdentifier param) {
        Validate.notNull(type);
        Validate.notNull(param);
        this.type = type;
        this.param = param;
    }

    public AbstractIdentifier getNameType()
    {
        return this.type;
    }
    public AbstractIdentifier getParam()   {
        return this.param;
    }

    @Override
    protected Type verifyDeclParam(DecacCompiler compiler) throws ContextualError {
              Type nameType = this.getNameType().verifyType(compiler);
              if (nameType.isVoid())
              {
                  throw new ContextualError("Type du paramètre \""+
                          this.getParam().getName().toString() +
                          "\" doit être différent de void (règle 2.9)", this.getLocation());
              }
              this.getNameType().setType(nameType);


              return nameType;

    }
    @Override
    protected void codeGenParam(DecacCompiler compiler){
            param.codeGenIdentparam(compiler);

    }
    @Override
    protected void verifyParam(DecacCompiler compiler,
        EnvironmentExp paramEnv) throws ContextualError{
          Type nameType = this.getNameType().getType();
          Symbol symbol = getParam().getName();
          ParamDefinition def = new ParamDefinition(nameType, param.getLocation());
          try
          {
              paramEnv.declare(symbol, def);
          }
          catch (DoubleDefException e)
          {
              throw new ContextualError("Le paramètre \"" +
              symbol.toString() + "\" est déja déclaré à " +
              paramEnv.get(symbol).getLocation() + " (règle 3.12)", param.getLocation());
          }
          this.getParam().verifyExpr(compiler, paramEnv, null);
        }
    @Override
    public void decompile(IndentPrintStream s) {
      this.type.decompile(s);
      s.print(" ");
      this.param.decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        param.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        param.prettyPrint(s, prefix, false);
    }
}
