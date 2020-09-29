package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.ima.pseudocode.*;
/**
 * Left-hand side value of an assignment.
 *
 * @author gl53
 * @date 01/01/2020
 */
public abstract class AbstractLValue extends AbstractExpr {
     public ExpDefinition getExpDefinition(){
        return null;
      }
     protected RegisterOffset codeGenField(DecacCompiler compiler){
        return null;
    }
    
    
}
