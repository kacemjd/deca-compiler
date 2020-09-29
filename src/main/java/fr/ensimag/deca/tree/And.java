package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.deca.codegen.RegisterManager;


/**
 *
 * @author gl53
 * @date 01/01/2020
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }
    @Override
    public void codeGenOp(DecacCompiler compiler){
        GPRegister R1 = Register.R1;
        GPRegister r3 = this.getLeftOperand().codeGenLoad(compiler);
        compiler.addInstruction(new LOAD(r3, R1));
        compiler.getRegisterManager().used.add(r3);
        compiler.getRegisterManager().freeReg(compiler, r3);
        Label andElse= new Label("And_else_in_"+this.getLeftOperand()
                .getLocation().toStringLabel());
        Label andFin= new Label("And_fin_in_"+this.getLeftOperand()
                .getLocation().toStringLabel());
        compiler.addInstruction(new BNE(andElse));//premier faux -> and faux
        GPRegister r2  = this.getRightOperand().codeGenLoad(compiler);
        compiler.addInstruction(new LOAD(r2, R1));
        compiler.addInstruction(new BNE(andElse));//deuxieme faux -> and faux
        compiler.addInstruction(new LOAD(0, R1));
        compiler.addInstruction(new BRA(andFin));
        compiler.addLabel(andElse);
        compiler.addInstruction(new LOAD(1, R1));
        compiler.getRegisterManager().used.add(r2);
        compiler.getRegisterManager().freeReg(compiler, r2);
        compiler.addLabel(andFin);
    }


}
