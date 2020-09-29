package fr.ensimag.deca.tree;
import java.util.Iterator;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.RTS;

/**
 * List of declarations (e.g. int x; float y,z).
 *
 * @author gl53
 * @date 01/01/2020
 */
public class ListDeclField extends TreeList<AbstractDeclField> {

    @Override
    public void decompile(IndentPrintStream s) {

      for (AbstractDeclField field : getList()) {
          field.decompile(s);
          s.println();
      }
    }

    /**
     * Implements non-terminal "list_decl_field" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to
     *      the "env_exp_r" attribute
     * @param currentClass
     *          corresponds to "class" attribute (null in the main bloc).
     */
    void verifyListDeclField(DecacCompiler compiler, AbstractIdentifier superIdentifier,
                             AbstractIdentifier classIdentifier) throws ContextualError {
        Iterator<AbstractDeclField> declFields = this.iterator();
        int index = superIdentifier.getClassDefinition().getNumberOfFields();
        classIdentifier.getClassDefinition().setNumberOfFields(index);
        while (declFields.hasNext())
        {
            AbstractDeclField declField = declFields.next();
            declField.verifyDeclField(compiler, superIdentifier, classIdentifier);
        }

    }

    void verifyListDeclFieldValue(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError
        {
          Iterator<AbstractDeclField> declFields = this.iterator();
          while (declFields.hasNext())
          {
              AbstractDeclField declField = declFields.next();
              declField.verifyFieldValue(compiler, localEnv, currentClass);
          }
        }

    public void codeGenListField(DecacCompiler compiler){
        for (AbstractDeclField i : getList()) {
            i.codeGenField(compiler);
       }
        compiler.addInstruction(new RTS());
    }


}
