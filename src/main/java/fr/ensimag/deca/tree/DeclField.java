package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.*;

import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.FloatLiteral;
import java.io.PrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

/**
 * @author gl53
 * @date 01/01/2020
 */
public class DeclField extends AbstractDeclField {

    final private Visibility visib;
    final private AbstractIdentifier type;
    final private AbstractIdentifier fieldName;
    private AbstractInitialization initialization;
    private static final Logger LOG = Logger.getLogger(DeclField.class);
    public DeclField(Visibility visib, AbstractIdentifier type,
                     AbstractIdentifier fieldName,
                     AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(visib);
        Validate.notNull(fieldName);
        Validate.notNull(initialization);
        this.visib = visib;
        this.type = type;
        this.fieldName = fieldName;
        this.initialization = initialization;
    }

    public Visibility getVisibility()
    {
        return this.visib;
    }
    public AbstractIdentifier getNameType()
    {
        return this.type;
    }
    public AbstractIdentifier getNameField()
    {
        return this.fieldName;
    }
    public AbstractInitialization getInitialization()
    {
        return this.initialization;
    }
    @Override
    protected void verifyDeclField(DecacCompiler compiler,
                  AbstractIdentifier superIdentifier, AbstractIdentifier classIdentifier)
            throws ContextualError {

              Type nameType = this.getNameType().verifyType(compiler);
              if (nameType.isVoid())
              {
                  throw new ContextualError("Type du field \""+
                          this.getNameField().getName().toString() +
                          "\" doit être différent de \"void\" (règle 2.5)", this.getLocation());
              }
              Definition override = superIdentifier.getClassDefinition().getMembers().get(getNameField().getName());
              if ((override != null) && !(override instanceof FieldDefinition))
              {
                throw new ContextualError("Le field \""
                + this.getNameField().getName().getName() + "\" est déja défini " +
                "dans une super classe en tant que \"" + override.getNature() + "\" à "+
                override.getLocation() + " (règle 2.5)",
                fieldName.getLocation());
              }
              ClassDefinition classDef = classIdentifier.getClassDefinition();
              int index = classDef.getNumberOfFields(); index ++;
              classDef.setNumberOfFields(index);
              FieldDefinition def = new FieldDefinition(nameType,
                      this.getLocation(), this.getVisibility(),
                      classDef, index);
              Symbol symbol = this.getNameField().getName();
              try
              {
                  classDef.getMembers().declare(symbol, def);
              }
              catch (DoubleDefException e)
              {
                throw new ContextualError("Le field \"" +
                        fieldName.decompile() + "\" est déjà déclaré à " +
                        classDef.getMembers().get(symbol).getLocation() +
                        " (règle 2.4)", fieldName.getLocation());

              }
              this.getNameField().verifyExpr(compiler, classDef.getMembers(), classDef);
    }
    protected  void verifyIdent(EnvironmentExp localEnv) throws ContextualError
    {

    }
    protected void verifyFieldValue(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError
        {
          Type type = this.getNameField().getType();
          this.getInitialization().verifyInitialization(compiler, type, localEnv, currentClass);

        }
    @Override
    protected void codeGenField(DecacCompiler compiler){
        fieldName.codeGenOperand(compiler);
        if (getInitialization() instanceof NoInitialization){
            if (type.getDefinition().getType().toString()=="int"){
                compiler.addInstruction(new LOAD(0, Register.R0));
            }
            else if (type.getDefinition().getType().toString()=="float"){
                FloatLiteral f = new FloatLiteral((float)0);
                GPRegister r = f.codeGenLoad(compiler);
                compiler.addInstruction(new LOAD(r, Register.R0));
                compiler.getRegisterManager().freeReg(compiler,r);
            }
            else{
                compiler.addInstruction(new LOAD(new ImmediateNull(), Register.R0));
            }
        }
        else {
           GPRegister r = ((Initialization) getInitialization()).getExpression().codeGenLoad(compiler);
           compiler.addInstruction(new LOAD(r, Register.R0));
           compiler.getRegisterManager().used.add(r);
           compiler.getRegisterManager().freeReg(compiler, r);
        }
        FieldDefinition fld = ((Identifier)(fieldName)).getFieldDefinition();
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(fld.getIndex(), Register.R1)));
    }
    @Override
    String printNodeLine(PrintStream s, String prefix, boolean last,
            boolean inlist, String nodeName) {
            return super.printNodeLine(s, prefix, last, inlist,
                      "[visibility=" + visib.getValue() + "] " + nodeName);
          }

    @Override
    public void decompile(IndentPrintStream s) {
      this.type.decompile(s);
      s.print(" ");
      this.fieldName.decompile(s);
      s.print(" ");
      this.initialization.decompile(s);
      s.print(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        fieldName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        fieldName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
