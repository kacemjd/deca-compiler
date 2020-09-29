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
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;

/**
 *
 * @author gl53
 * @date 01/01/2020
 */
public class InstanceOf extends AbstractExpr {
  private AbstractIdentifier idExpr;
  private AbstractExpr expr;
  public InstanceOf(AbstractExpr expr, AbstractIdentifier idExpr){
      this.idExpr = idExpr;
      this.expr = expr;
  }
  public AbstractIdentifier getIdExpr(){
    return this.idExpr;
  }
  public AbstractExpr getExpr(){
    return this.expr;
  }
  @Override
  public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
          ClassDefinition currentClass) throws ContextualError {
        return null;
  }


  @Override
  public void decompile(IndentPrintStream s) {
      s.print("(");
      idExpr.decompile(s);
      s.print(")");
      expr.decompile(s);


  }

  @Override
  protected void iterChildren(TreeFunction f) {
      // leaf node => nothing to do
      expr.iterChildren(f);
      idExpr.iterChildren(f);
  }

  @Override
  protected void prettyPrintChildren(PrintStream s, String prefix) {
      // leaf node => nothing to do
      expr.prettyPrint(s, prefix, true);
      idExpr.prettyPrint(s, prefix, true);
  }
  @Override
  protected GPRegister codeGenLoad(DecacCompiler compiler){
        return null;

}
}
