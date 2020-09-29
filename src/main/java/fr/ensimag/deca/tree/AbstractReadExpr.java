package fr.ensimag.deca.tree;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.deca.DecacCompiler;

/**
 * read...() statement.
 *
 * @author gl53
 * @date 01/01/2020
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
    }
    @Override
    protected GPRegister codeGenLoad(DecacCompiler compiler){
        return null;
    }

}
