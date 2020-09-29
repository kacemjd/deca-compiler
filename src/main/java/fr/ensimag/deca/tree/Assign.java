package fr.ensimag.deca.tree;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl53
 * @date 01/01/2020
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        if (getLeftOperand().getExpDefinition().isMethod())
        {
          throw new ContextualError("On ne peut pas affecter la méthode \"" +
                  getLeftOperand().decompile() + "\"", this.getLocation());
        }
        this.setRightOperand(this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, type));
        Type type2 = this.getRightOperand().getType();
        if (!localEnv.assignCompatible(type, type2))
        {
            throw new ContextualError("L'identificateur " +
                //this.getLeftOperand().getName().toString() +
                " est de type " + type.toString() +
                ", qui n'est pas compatible avec le type " +
                type2.toString() + " affecté (règle 3.32)", this.getLocation());
        }
        this.setType(type2);
        return getType();
    }
    @Override
    public void decompile(IndentPrintStream s) {
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        ExpDefinition def = this.getLeftOperand().getExpDefinition();
        GPRegister r1 = this.getRightOperand().codeGenLoad(compiler);
        if(def != null && !def.isField()){
            compiler.addInstruction(new STORE(r1,this.getLeftOperand().getExpDefinition().getOperand()));
        }
        else{
            RegisterOffset r2 = this.getLeftOperand().codeGenField(compiler);
            compiler.addInstruction(new STORE(r1, r2));
        }
        compiler.getRegisterManager().used.add(r1);
        compiler.getRegisterManager().freeReg(compiler, r1);



}

}
