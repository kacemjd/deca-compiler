package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Full if/else if/else statement.
 *
 * @author gl53
 * @date 01/01/2020
 */
public class IfThenElse extends AbstractInst {

    private final AbstractExpr condition;
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public AbstractExpr getCondition()
    {
        return this.condition;
    }
    public ListInst getThen()
    {
        return this.thenBranch;
    }

    public ListInst getElse()
    {
        return this.elseBranch;
    }
    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    public void setElseBranch(ListInst elsebranch){
        this.elseBranch = elsebranch;
    }
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.getCondition().verifyCondition(compiler, localEnv, currentClass);
        this.getThen().verifyListInst(compiler, localEnv, currentClass, returnType);
        this.getElse().verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        GPRegister R1 = Register.R1;
        //GPRegister r1 = Register.getR(Register.getCpt());
        GPRegister r1 = condition.codeGenLoad(compiler);
        compiler.addInstruction(new LOAD(r1, Register.R1));
        Label labelElse = new Label("IfThenElse_else_" + condition.getLocation().toStringLabel());
        compiler.addInstruction(new BNE(labelElse));
        thenBranch.codeGenListInst(compiler);
        Label labelFin = new Label("IfThenElse_fin_" + condition.getLocation().toStringLabel());
        compiler.addInstruction(new BRA(labelFin));
        compiler.addLabel(labelElse);
        elseBranch.codeGenListInst(compiler);
        compiler.addLabel(labelFin);
         compiler.getRegisterManager().used.add(r1);
        compiler.getRegisterManager().freeReg(compiler, r1);
    }

    @Override
    public void decompile(IndentPrintStream s) {
      s.print("if (");
      this.getCondition().decompile(s);
      s.println(")");
      s.println("{");
      s.indent();
      this.getThen().decompile(s);
      //s.println();
      s.unindent();
      s.println("}");
      s.println("else");
      s.println("{");
      s.indent();
      this.getElse().decompile(s);
      // s.println();
      s.unindent();
      s.print("}");    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
