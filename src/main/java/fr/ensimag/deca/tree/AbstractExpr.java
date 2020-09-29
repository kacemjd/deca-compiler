package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl53
 * @date 01/01/2020
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     *
     * implements non-terminals "expr" and "lvalue"
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments
     *
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass,
            Type expectedType)
            throws ContextualError {
        Type type2 = this.verifyExpr(compiler, localEnv, currentClass);
        if (!localEnv.assignCompatible(expectedType, type2))
        {
            throw new ContextualError("On ne peut pas affecter une expression de type \"" +
                type2.toString()+ "\" à un identificateur de type \"" +
                expectedType.toString() + "\" (règle 3.28)", this.getLocation());
        }

        if (expectedType.sameType(type2))
        {
            this.setType(type2);
            return this;
        }
        else
        {
          if (type2.isInt())
          {
            ConvFloat expr = new ConvFloat(this);
            Type t = expr.verifyExpr(compiler, localEnv, currentClass);
            expr.setType(t);
            return expr;
          }
          return this;
        }
    }


    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.verifyExpr(compiler, localEnv, currentClass);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
              Type condType = this.verifyExpr(compiler, localEnv, currentClass);
              if (!condType.isBoolean())
              {
                  throw new  ContextualError("Expression \"" + decompile() +
                          "\" est de type \"" + condType.getName().getName() + 
                          "\". Le type doit être un booléen (règle 3.29)",
                                              this.getLocation());
              }
              this.setType(condType);

    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler, boolean hex) {
        //System.out.println(this.getType());
        //throw new UnsupportedOperationException("not yet implemented");
        if(hex){
            if(this.getType().isInt()){
                compiler.addInstruction(new FLOAT(Register.R1, Register.R1));
            }
            compiler.addInstruction(new WFLOATX());
        }
        else{
            if(this.getType().toString().equals("int")){
                compiler.addInstruction(new WINT());
            }
            if(this.getType().toString().equals("float")){
                compiler.addInstruction(new WFLOAT());
            }
        }


    }


    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    protected GPRegister codeGenLoad(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implementedoki");
    }

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }

}
