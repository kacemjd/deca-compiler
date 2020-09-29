package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.ima.pseudocode.instructions.BOV;


/**
 * @author gl53
 * @date 01/01/2020
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }
    @Override
    public void codeGenOp(DecacCompiler compiler){
        GPRegister R1 = Register.R1;
        GPRegister r1 = this.getLeftOperand().codeGenLoad(compiler);
        GPRegister r2 = this.getRightOperand().codeGenLoad(compiler);
        compiler.getRegisterManager().used.add(r1);
        compiler.getRegisterManager().used.add(r2); 
        compiler.addInstruction(new SUB(r2, r1));
        if (!compiler.getCompilerOptions().getNoCheck() && this.getType().isFloat()){
            compiler.addInstruction(new BOV(compiler.over_flow));
        }
        compiler.getRegisterManager().freeReg(compiler, r2);
        compiler.addInstruction(new LOAD(r1, R1));
        compiler.getRegisterManager().freeReg(compiler, r1);
    }
    @Override
    protected GPRegister codeGenLoad(DecacCompiler compiler) {
        GPRegister r1 = this.getLeftOperand().codeGenLoad(compiler);
        GPRegister r2 = this.getRightOperand().codeGenLoad(compiler);
        compiler.addInstruction(new SUB(r2, r1));
        if (!compiler.getCompilerOptions().getNoCheck() && this.getType().isFloat()){
            compiler.addInstruction(new BOV(compiler.over_flow));
        }
         compiler.getRegisterManager().used.add(r2);
        compiler.getRegisterManager().freeReg(compiler, r2);
        return r1;
    }
    
}
