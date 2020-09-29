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
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.deca.codegen.RegisterManager;
/**
 *
 * @author ensimag
 */
public class ThisLiteral extends AbstractExpr{
    private boolean visibility;
    public ThisLiteral(boolean visibility){
        super();
        this.visibility = visibility;
    }
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError
    {
          if (currentClass == null)
          {
            if (!visibility)
            {
              throw new ContextualError("Le programme principal n'a aucune méthode",
                      this.getLocation());
            }
            throw new ContextualError("\"this\" ne doit pas apparaître " +
                  "dans le programme principale (règle 3.43)",
                    this.getLocation());
          }
          this.setType(currentClass.getType());
          return this.getType();
    }
    @Override
    public void decompile(IndentPrintStream s) {
      if (visibility)
      {
        s.print("this");
      }
      s.print("");
    }
    @Override
    String prettyPrintNode() {
        return "this";
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
        GPRegister r = compiler.getRegisterManager().allocReg(compiler);
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), r));
        return r;
    }


}
