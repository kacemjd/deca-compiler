package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.REM;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.deca.codegen.RegisterManager;
/**
 *
 * @author gl53
 * @date 01/01/2020
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

        @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        this.setType(this.typeArith(t1, t2));
        return getType();
    }
    public Type typeArith(Type t1, Type t2) throws ContextualError
    {

        if (t1.isInt() & t2.isInt())
        {
            return t1;
        }
        throw new ContextualError("L'opération arithmétique: (" +
                t1.toString() + " % " + t2.toString() + 
                ") n'est pas autorisée (règle 3.33)", this.getLocation());

    }
    @Override
    protected String getOperatorName() {
        return "%";
    }
    @Override
    public void codeGenOp(DecacCompiler compiler){
        GPRegister R1 = Register.R1;
        GPRegister r1 = this.getLeftOperand().codeGenLoad(compiler);
        GPRegister r2 = this.getRightOperand().codeGenLoad(compiler);
        compiler.getRegisterManager().used.add(r1);
        compiler.getRegisterManager().used.add(r2);
        compiler.addInstruction(new REM(r2, r1));
        compiler.addInstruction(new BOV(compiler.divisionErr));
        compiler.getRegisterManager().freeReg(compiler, r2);
        compiler.addInstruction(new LOAD(r1, R1));
        compiler.getRegisterManager().freeReg(compiler, r1);
    }
    @Override
    protected GPRegister codeGenLoad(DecacCompiler compiler) {
        GPRegister r1 = this.getLeftOperand().codeGenLoad(compiler);
        GPRegister r2 = this.getRightOperand().codeGenLoad(compiler);
        compiler.addInstruction(new REM(r2, r1));
        compiler.addInstruction(new BOV(compiler.divisionErr));
        compiler.getRegisterManager().used.add(r2);
        compiler.getRegisterManager().freeReg(compiler, r2);
        return r1;
    }

}
