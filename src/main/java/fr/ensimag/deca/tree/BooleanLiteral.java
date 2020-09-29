package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.deca.codegen.RegisterManager;

/**
 *
 * @author gl53
 * @date 01/01/2020
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        if (!compiler.getSymbols().checkSymbol("boolean")){
            throw new ContextualError("Type \"boolean\" n'est pas un "
                    + "type prédéfini (règle 0.2)", this.getLocation());
        }

       Type returnType = new BooleanType(compiler.getSymbols().getSymbol("boolean"));
       this.setType(returnType);
       return this.getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }
    @Override
    protected GPRegister codeGenLoad(DecacCompiler compiler){
        boolean val = this.getValue();
        GPRegister r1 = compiler.getRegisterManager().allocReg(compiler);
        if(val){
            compiler.addInstruction(new LOAD(0, r1));
        }
        else{
            compiler.addInstruction(new LOAD(1, r1));
        }
        return r1;

    }
}
