package fr.ensimag.deca.tree;
import java.util.Iterator;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.context.EnvironmentExp;
/**
 * List of expressions (eg list of parameters).
 *
 * @author gl53
 * @date 01/01/2020
 */
public class ListExpr extends TreeList<AbstractExpr> {


  protected void verifyListExpr(DecacCompiler compiler, EnvironmentExp localEnv,
  ClassDefinition currentClass)
  throws ContextualError {

  Iterator<AbstractExpr> exprs = this.iterator();
      while (exprs.hasNext())
      {
          AbstractExpr expr = exprs.next();
          Type type = expr.verifyExpr(compiler, localEnv, currentClass);
          if (this.checkType(type))
          {
              throw new ContextualError("L'expression \"" +
                      expr.decompile() + "\" est de type \"" + type.getName().getName() +
                      "\". Le type doit être un String, un int ou un float" +
                      " (règle 3.31)",
                      this.getLocation());
          }
      }
  }
  public boolean checkType(Type type)
  {
      if ((type.isString() || type.isFloat() || type.isInt()))
      {
          return false;
      }
      return true;
  }
  @Override
  public void decompile(IndentPrintStream s) {
      Iterator<AbstractExpr> exprs = this.iterator();
      AbstractExpr expr;
      if (exprs.hasNext())
      {
          expr = exprs.next();
          expr.decompile(s);
      }
      while (exprs.hasNext())
      {
          expr = exprs.next();
          s.print(", ");
          // s.println();
          expr.decompile(s);
      }

      /*********************** Or  **************************/
      /*
      int compteur i = 0;
      for (AbstractExpr expr : getList()) {
          if (i == 0)
          {
              expr.decompile(s);
          }
          else
          {
              s.print(",");
              expr.decompile(s);
          }
          i += 1;
      }
*/
    }
}
