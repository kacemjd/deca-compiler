package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.OPP;
import fr.ensimag.deca.codegen.RegisterManager;

/**
 * @author gl53
 * @date 01/01/2020
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
              Type unary = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
              if (!unary.isInt() & !unary.isFloat())
              {
                  throw new ContextualError("L'opération unaire \"" + decompile()
                          + " : [" +
                " - ( " + unary.toString() +
                " ) ]\" : n'est pas autorisée (règle 3.37)", this.getLocation());
              }

              this.setType(unary);
              return unary;
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }
    @Override
    public void codeGenOp(DecacCompiler compiler){
        GPRegister R1 = Register.R1;
        GPRegister r1 = this.getOperand().codeGenLoad(compiler);
        compiler.addInstruction(new OPP(r1, r1));
        compiler.addInstruction(new LOAD(r1, R1));
        r1.freeR();
    }
    @Override
    protected GPRegister codeGenLoad(DecacCompiler compiler) {
        GPRegister r1 = this.getOperand().codeGenLoad(compiler);
        compiler.addInstruction(new OPP(r1, r1));
        return r1;
    }

}
