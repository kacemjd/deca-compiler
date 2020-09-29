package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl53
 * @date 01/01/2020
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
        this.setLocation(this.getOperand().getLocation());
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass){
        this.getOperand().setType(new IntType(compiler.getSymbols().getSymbol("int")));
        return (new FloatType(compiler.getSymbols().getSymbol("float")));
    }
    @Override
    public void decompile(IndentPrintStream s) {
        getOperand().decompile(s);
    }
    @Override
    protected String getOperatorName() {
        return "ConvFloat";
    }
    @Override
    protected GPRegister codeGenLoad(DecacCompiler compiler){
        GPRegister r1 = this.getOperand().codeGenLoad(compiler);
        compiler.addInstruction(new FLOAT(r1, r1));
        return r1;
    
    }

}
