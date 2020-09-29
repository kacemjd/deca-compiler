package fr.ensimag.deca.tree;
import java.util.*;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import org.apache.commons.lang.Validate;
import java.io.PrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.codegen.RegisterManager;
/**
 * Integer literal
 *
 * @author gl53
 * @date 01/01/2020
 */
public class IntLiteral extends AbstractExpr {
    public int getValue() {
        return value;
    }

    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        if (!compiler.getSymbols().checkSymbol("int")){
            throw new ContextualError("Type \"int\" n'est pas un "
                    + "type prédéfini (règle 0.2)", this.getLocation());
        }

       Type returnType = new IntType(compiler.getSymbols().getSymbol("int"));
       this.setType(returnType);
       return this.getType();
    }


    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
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
    protected void codeGenPrint(DecacCompiler compiler, boolean hex) {
        GPRegister r = Register.getR(1);
        compiler.addInstruction(new LOAD(value, r));
        super.codeGenPrint(compiler, hex);
    }
    @Override
    protected GPRegister codeGenLoad(DecacCompiler compiler){
        int val = this.getValue();
        GPRegister r1 = compiler.getRegisterManager().allocReg(compiler);
        compiler.addInstruction(new LOAD(val, r1));
        return r1;
    }

}
