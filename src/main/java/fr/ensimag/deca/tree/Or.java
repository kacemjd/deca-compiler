package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.deca.codegen.RegisterManager;


/**
 *
 * @author gl53
 * @date 01/01/2020
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }
    @Override
    public void codeGenOp(DecacCompiler compiler){
        GPRegister R1 = Register.R1;
        GPRegister r3 = this.getLeftOperand().codeGenLoad(compiler);
        compiler.addInstruction(new LOAD(r3, R1));
        r3.freeR();
        Label andElse= new Label("And_else_in_"+this.getLeftOperand()
                .getLocation().toStringLabel());
        Label andFin= new Label("And_fin_in_"+this.getLeftOperand()
                .getLocation().toStringLabel());
        compiler.addInstruction(new BEQ(andElse));//premier vrai -> or vrai
        GPRegister r2 = this.getRightOperand().codeGenLoad(compiler);
        compiler.addInstruction(new LOAD(r2, R1));
        compiler.addInstruction(new BEQ(andElse));
        compiler.addInstruction(new LOAD(1, R1));
        compiler.addInstruction(new BRA(andFin));
        compiler.addLabel(andElse);
        compiler.addInstruction(new LOAD(0, R1));
        compiler.addLabel(andFin);
    }
    

}
