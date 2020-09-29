package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RINT;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.deca.codegen.RegisterManager;

/**
 *
 * @author gl53
 * @date 01/01/2020
 */
public class ReadInt extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        if (!compiler.getSymbols().checkSymbol("int")){
            throw new ContextualError("Type \"int\" n'est pas un "
                    + "type prédéfini (règle 0.2)", this.getLocation());
        }
       Type returnType = new IntType(compiler.getSymbols().getSymbol("int"));
       this.setType(returnType);
       return this.getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
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
    protected GPRegister codeGenLoad(DecacCompiler compiler){
        //throw new UnsupportedOperationException("not yet implemented5555");
         GPRegister r = compiler.getRegisterManager().allocReg(compiler);
        compiler.addInstruction(new RINT());
        compiler.addInstruction(new BOV(compiler.i0Error));
        compiler.addInstruction(new LOAD(Register.R1, r));
        return r;
    }
    
}
