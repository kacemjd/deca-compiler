package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.deca.codegen.RegisterManager;

/**
 * Arithmetic binary operations (+, -, /, ...)
 *
 * @author gl53
 * @date 01/01/2020
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        this.setType(this.typeArith(compiler, localEnv, currentClass, t1, t2));
        return getType();

    }
    public Type typeArith(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type t1, Type t2) throws ContextualError
    {

      if (t1.sameType(t2) & t1.isTypeBinary())
      {
          return t1;
      }
      else if (t2.isFloat() & t1.isInt())
      {
          this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
          this.getLeftOperand().setType(t2);
          return this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
          
      }
      else if(t1.isFloat() & t2.isInt())
      {
          this.setRightOperand(new ConvFloat(this.getRightOperand()));
          this.getRightOperand().setType(t1);
          return this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
      }
      else
        {
            throw new ContextualError("Opération arithmétique " + decompile() 
                    + " : (" +
                t1.toString() + " " + getOperatorName() +  " " + t2.toString() + 
                ") : non autorisée (règle 3.33)"
                , this.getLocation());
        }
      
    }
    @Override
    public void codeGenOp(DecacCompiler compiler){
        throw new UnsupportedOperationException("not yet implemented");
    }
    @Override
    protected GPRegister codeGenLoad(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
