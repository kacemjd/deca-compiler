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
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.*;



/**
 *
 * @author ensimag
 */
public class New extends AbstractExpr{
  private AbstractIdentifier idExpr;
  public New(AbstractIdentifier idExpr){
      this.idExpr = idExpr;
  }
  public AbstractIdentifier getIdExpr(){
    return this.idExpr;
  }
  @Override
  public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
          ClassDefinition currentClass) throws ContextualError {
        Type type =  this.getIdExpr().verifyType(compiler);
        if (!type.isClass())
        {
          throw new ContextualError("L'identificateur \"" +
                  idExpr.decompile() + "\" doit être une classe (règle 3.42)",
                  idExpr.getLocation());
        }
        this.setType(type);
        return type;
  }


  @Override
  public void decompile(IndentPrintStream s) {
      s.print("new ");
      idExpr.decompile(s);
      s.print("(");
      s.print(")");


  }

  @Override
  protected void iterChildren(TreeFunction f) {
      // leaf node => nothing to do
      idExpr.iterChildren(f);
  }

  @Override
  protected void prettyPrintChildren(PrintStream s, String prefix) {
      // leaf node => nothing to do
      idExpr.prettyPrint(s, prefix, true);
  }
  @Override
  protected GPRegister codeGenLoad(DecacCompiler compiler){
        //throw new UnsupportedOperationException("not yet implemented hm?");
        compiler.addComment("instruction new");
        GPRegister r = compiler.getRegisterManager().allocReg(compiler);
        ClassDefinition def = ((Identifier)(idExpr)).getClassDefinition();
        compiler.addInstruction(new NEW(def.getNumberOfFields() + 1, r));
        compiler.addInstruction(new LEA(def.getOperand(idExpr.getType()), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, r)));
        compiler.addInstruction(new PUSH(r));
        compiler.addInstruction(new BSR(new LabelOperand(new Label("init."+this.getType()))));
        compiler.addInstruction(new POP(r));
        return r;
    }
}
