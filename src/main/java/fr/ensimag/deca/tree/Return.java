
package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;


import org.apache.commons.lang.Validate;
/**
 *
 * @author gl53
 * @date 01/01/2020
 */
public class Return extends AbstractInst {

    private AbstractExpr rvalue;

    public AbstractExpr getRvalue()
    {
        return this.rvalue;
    }


    public Return(AbstractExpr rvalue) {
        Validate.notNull(rvalue);
        this.rvalue = rvalue;

    }
    public void setRvalue(AbstractExpr rvalue){
        this.rvalue = rvalue;
    }
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {

        if (returnType.isVoid())
        {
            throw new ContextualError("Il s'agit d'une méthode de type \"void\", " +
                                      "le \"return\" pose un problème (règle 3.24)", this.getLocation());
        }
        this.rvalue = this.getRvalue().verifyRValue(compiler, localEnv, currentClass, returnType);
        

    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        GPRegister r = rvalue.codeGenLoad(compiler);
        compiler.addInstruction(new LOAD(r, Register.R0));
        compiler.getRegisterManager().used.add(r);
        compiler.getRegisterManager().freeReg(compiler, r);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        this.getRvalue().decompile(s);
        s.print(";");


    }

    @Override
    protected void iterChildren(TreeFunction f) {
        rvalue.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        rvalue.prettyPrint(s, prefix, false);

    }
}
