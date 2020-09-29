package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Entry point for contextual verifications and code generation from outside the package.
 * 
 * @author gl53
 * @date 01/01/2020
 *
 */
public abstract class AbstractProgram extends Tree {
    public abstract void verifyProgram(DecacCompiler compiler) throws ContextualError;
    public abstract void codeGenProgram(DecacCompiler compiler) ;
    public abstract void decompile(IndentPrintStream s);
    public abstract AbstractMain getMain();

}
