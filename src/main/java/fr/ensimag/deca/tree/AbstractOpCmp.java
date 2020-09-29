package fr.ensimag.deca.tree;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.deca.codegen.RegisterManager;
/**
 *
 * @author gl53
 * @date 01/01/2020
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        this.setType(this.typeBool(compiler, localEnv, currentClass,t1, t2));
        return getType();
    }
    public Type typeBool(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type t1, Type t2) throws ContextualError
    {
        String op = this.getOperatorName();
        Type type = new BooleanType(compiler.getSymbols().getSymbol("boolean"));
        if ( ( (t1.isBoolean() & t2.isBoolean())
                ||(t1.isClassOrNull() & t2.isClassOrNull()))
            &(op.equals("==")||op.equals("!=")))
        {
            return type;
        }
        /***/
        else if ((op.equals("==")||op.equals("!=")||op.equals("<")
            ||op.equals("<=")||op.equals(">")||op.equals(">="))
            & t1.isTypeBinary() & t2.isTypeBinary())
        {

            if (t2.isFloat() & t1.isInt())
            {
                this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
                this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
                //this.getLeftOperand().setType(t1);
                this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
                this.getLeftOperand().setType(t2);
            }
            if(t1.isFloat() & t2.isInt())
            {
                this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
                this.setRightOperand(new ConvFloat(this.getRightOperand()));
                //this.getRightOperand().setType(t2);
                this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
                this.getRightOperand().setType(t1);
            }

            return type;
        }
        /*if ((op.equals("==")||op.equals("!="))
             &t1.isClassOrNull()&t2.isClassOrNull())
        {
            return type;
        }
        */
        else
        {
            throw new ContextualError("Opération de comparaison " + decompile()
                    + " : (" +
                t1.toString() + " " + getOperatorName() +  " " + t2.toString() +
                ") : non autorisée (règle 3.33)"
                , this.getLocation());
        }

    }
    public void codeGenOp(DecacCompiler compiler){
        GPRegister R1 = Register.R1;
        GPRegister r1 = this.getLeftOperand().codeGenLoad(compiler);
        GPRegister r2 = this.getRightOperand().codeGenLoad(compiler);
        compiler.addInstruction(new SUB(r2, r1));
        compiler.addInstruction(new LOAD(r1, R1));
        Label opIf = new Label("OpCmp_if_in_"+this.getLeftOperand()
                .getLocation().toStringLabel());
        Label opFin = new Label("OpCmp_fin_in_"+this.getLeftOperand()
                .getLocation().toStringLabel());
        // appel instruction de la fille
        this.codeGenIma(compiler, opIf);
        compiler.addInstruction(new LOAD(1, R1));
        compiler.addInstruction(new BRA(opFin));
        compiler.addLabel(opIf);
        compiler.addInstruction(new LOAD(0, R1));
        compiler.addLabel(opFin);
        compiler.getRegisterManager().used.add(r1);
        compiler.getRegisterManager().used.add(r2);
        compiler.getRegisterManager().freeReg(compiler, r1);
        compiler.getRegisterManager().freeReg(compiler, r2);
    };

    public abstract void codeGenIma(DecacCompiler compiler, Label label);

    @Override
    protected GPRegister codeGenLoad(DecacCompiler compiler) {
        GPRegister r1 = compiler.getRegisterManager().allocReg(compiler);
        this.codeGenOp(compiler);
        compiler.addInstruction(new LOAD(Register.R1, r1));
        return r1;
    }


}
