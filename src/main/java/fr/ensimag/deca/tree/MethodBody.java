package fr.ensimag.deca.tree;
import java.util.*;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.Label;

/**
 * @author gl53
 * @date 01/01/2020
 */
public class MethodBody extends Tree {
    private static final Logger LOG = Logger.getLogger(MethodBody.class);

    private ListDeclVar declVariables;
    private ListInst insts;
    public MethodBody(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }


    protected void verifyBody(DecacCompiler compiler, EnvironmentExp localEnv,
              EnvironmentExp paramEnv, ClassDefinition currentClass,
              Type rType) throws ContextualError
      {
        declVariables.verifyListDeclVariable(compiler, paramEnv, currentClass);
        insts.verifyListInst(compiler, paramEnv, currentClass, rType);
      }
    public ListDeclVar getDeclVariables(){
        return this.declVariables;

    }
/**
    protected void codeGenBody(DecacCompiler compiler) {
        declVariables.codeGenListVar(compiler);
        //loading class attributes
        
        insts.codeGenListInst(compiler);
    }
*/
    protected void codeGenMethodBody(DecacCompiler compiler){
        declVariables.codeGenListVarM(compiler);
        insts.codeGenListInst(compiler);
        
        
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
