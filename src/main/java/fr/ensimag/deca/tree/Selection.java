
package fr.ensimag.deca.tree;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.OPP;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 *
 * @author
 */

public class Selection extends AbstractLValue{
    private AbstractExpr expr;
    private AbstractIdentifier id;
    public Selection(AbstractExpr expr, AbstractIdentifier id){
       this.id = id;
       this.expr = expr;
    }
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type class2 = this.expr.verifyExpr(compiler, localEnv, currentClass);
        if (class2 == null || !class2.isClass())
        {
          throw new ContextualError("L'identificateur \"" + expr.decompile() +
          "\" n'est pas une classe (règle 3.65)", expr.getLocation());
        }
        EnvironmentExp exp2 = ((ClassType)class2).getDefinition().getMembers();
        this.id.verifyExpr(compiler, exp2, currentClass);
        Definition field0 = this.id.getDefinition();
        if (field0 == null || !field0.isField())
        {
          throw new ContextualError("L'identificateur \"" + id.decompile() +
          "\" n'est pas un field (règle 3.65)", id.getLocation());
        }
        FieldDefinition field = (FieldDefinition) field0;
        if (field.getVisibility().getValue().equals("PROTECTED"))
        {
          if (currentClass == null)
          {
            throw new ContextualError("Le programme principale n'est pas un sous " +
            "type de la classe \"" + expr.decompile() + "\" (règle 3.66)",this.getLocation());
          }
          else if (!compiler.get_env_types().subType(class2, currentClass.getType()))
          {
            throw new ContextualError("Le type de l'expression \"" +
            expr.decompile() + "\" doit être un sous type de la classe courante \"" +
            currentClass.getType().toString() + "\" : problème de visibilité (règle 3.66)",
            this.getLocation());
          }
          else if (!compiler.get_env_types().subType(currentClass.getType(),
                                                     field.getContainingClass().getType()))
          {
            throw new ContextualError("la classe courante \"" +
            currentClass.getType().toString() + "\" doit être un sous " +
            "type de la classe \"" + field.getContainingClass().getType().toString() +
            "\" où le champ protégé \"" +
            id.decompile() + "\" est déclaré : problème de visibilité (règle 3.66)",this.getLocation());
          }
        }
        this.setType(field.getType());
        this.definition = field;
        return field.getType();
    }
    @Override
    public ExpDefinition getExpDefinition()
    {
      return this.definition;
    }
    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iterChildren(f);
        id.iterChildren(f);

    }
    private ExpDefinition definition;
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
      expr.prettyPrint(s, prefix, true);
        id.prettyPrint(s, prefix, true);

    }
    @Override public void decompile(IndentPrintStream s){
      expr.decompile(s);
      s.print(".");
      id.decompile(s);

    }
     public GPRegister codeGenLoad(DecacCompiler compiler){
        GPRegister r = expr.codeGenLoad(compiler);
        FieldDefinition fld = ((Identifier)(id)).getFieldDefinition();
        compiler.addInstruction(new LOAD(new RegisterOffset(fld.getIndex(), r), r));
       //compiler.getRegisterManager().freeReg(compiler, r);
        return r;
    }
    protected RegisterOffset codeGenField(DecacCompiler compiler){
        if(expr instanceof Identifier){
            GPRegister r = compiler.getRegisterManager().allocReg(compiler);
            compiler.addInstruction(new LOAD(((Identifier)(expr)).getExpDefinition().getOperand(), r));
            return new RegisterOffset(((Identifier)(id)).getFieldDefinition().getIndex(), r);
        }
        else{
            return id.codeGenField(compiler);
        }
    }
    @Override
     protected void codeGenPrint(DecacCompiler compiler, boolean hex) {
        GPRegister r1 = this.codeGenLoad(compiler);
        compiler.addInstruction(new LOAD(r1, Register.R1));
        super.codeGenPrint(compiler, hex);
        compiler.getRegisterManager().freeReg(compiler, r1);

    }
    }
