package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

import java.util.Iterator;

/**
 * Print statement (print, println, ...).
 *
 * @author gl53
 * @date 01/01/2020
 */
public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();

    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.getArguments().verifyListExpr(compiler, localEnv, currentClass);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Iterator<AbstractExpr> it = getArguments().getList().iterator();
        while (it.hasNext()){
            AbstractExpr expr = it.next();
            expr.codeGenPrint(compiler, this.printHex);
            if (it.hasNext()){
                compiler.addInstruction(new WSTR("   "));//ajout espace entre arguments
            }
        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
      boolean x = this.getPrintHex();
      String ln = new String(this.getSuffix());
      String print = new String("print");
      if (x)
      {
          print = print + "x" + ln;
      }
      else
      {
          print = print + ln;
      }
      s.print(print +"(");
      //s.indent();
      this.getArguments().decompile(s);
      //s.unindent();
      s.print(");");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
