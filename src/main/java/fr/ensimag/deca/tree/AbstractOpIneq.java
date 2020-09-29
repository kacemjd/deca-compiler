package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;


/**
 *
 * @author gl53
 * @date 01/01/2020
 */
public abstract class AbstractOpIneq extends AbstractOpCmp {

    public AbstractOpIneq(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    
    @Override
    public void codeGenIma(DecacCompiler compiler, Label label){
        System.out.println("ERREUR");
    };

}
