/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.ensimag.deca.tree;


import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.NullType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.deca.codegen.RegisterManager;
/**
 *
 * @author ensimag
 */
public class NullLiteral extends AbstractExpr{
    public NullLiteral(){
        super();
    }
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError
    {
        if (!compiler.getSymbols().checkSymbol("null"))
        {
            throw new ContextualError("\"null\" n'est pas "
                    + "défini", this.getLocation());
        }
        Type returnType = new NullType(compiler.getSymbols().getSymbol("null"));
        this.setType(returnType);
        return this.getType();
    }
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("null");
    }
    @Override
    String prettyPrintNode() {
        return "NullLiteral";
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
    }
    public GPRegister codeGenLoad(DecacCompiler compiler){
        return null;
    }
}
