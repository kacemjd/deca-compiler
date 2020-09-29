package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl53
 * @date 01/01/2020
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
              Type unary = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
              if (!unary.isBoolean())
              {
                  throw new ContextualError("L'opération unaire: \"" + decompile()
                          + ": [" +
                " ! ( " + unary.toString() +
                " ) ]\" :  n'est pas autorisée (règle 3.37)", this.getLocation());
              }
              this.setType(unary);
              return unary;
    }

    @Override
    public void codeGenOp(DecacCompiler compiler){
        GPRegister R1 = Register.R1;
        GPRegister r3 = this.getOperand().codeGenLoad(compiler);
        compiler.addInstruction(new LOAD(r3, R1));
        r3.freeR();
        Label Else= new Label("else_in_"+this.getOperand()
                .getLocation().toStringLabel());
        Label Fin= new Label("fin_in_"+this.getOperand()
                .getLocation().toStringLabel());
        compiler.addInstruction(new BEQ(Else));
        compiler.addInstruction(new LOAD(0, R1));
        compiler.addInstruction(new BRA(Fin));
        compiler.addLabel(Else);
        compiler.addInstruction(new LOAD(1, R1));
        compiler.addLabel(Fin);

    }
    @Override
    protected String getOperatorName() {
        return "!";
    }
}
