package fr.ensimag.deca.tree;
import java.util.Iterator;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.GPRegister;


/**
 * List of declarations (e.g. int x; float y,z).
 *
 * @author gl53
 * @date 01/01/2020
 */
public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

    @Override
    public void decompile(IndentPrintStream s) {

      for (AbstractDeclMethod method : getList()) {
          method.decompile(s);
          s.println();
      }
    }

    /**
     * Implements non-terminal "list_decl_Method" of [SyntaxeContextuelle] in pass 3
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
    void verifyListDeclMethod(DecacCompiler compiler,
                AbstractIdentifier superIdentifier, AbstractIdentifier classIdentifier) throws ContextualError {
        Iterator<AbstractDeclMethod> declMethods = this.iterator();
        int index = superIdentifier.getClassDefinition().getNumberOfMethods();
        classIdentifier.getClassDefinition().setNumberOfMethods(index);
        while (declMethods.hasNext())
        {
            AbstractDeclMethod declMethod = declMethods.next();
            declMethod.verifyDeclMethod(compiler, superIdentifier, classIdentifier);
        }
    }
    void verifyListDeclMethodBody(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError
        {
          Iterator<AbstractDeclMethod> declMethods = this.iterator();
          while (declMethods.hasNext())
          {
              AbstractDeclMethod declMethod = declMethods.next();
              declMethod.verifyMethodBody(compiler, localEnv, currentClass);
          }
        }
    /*public void codeGenListMethod(DecacCompiler compiler, ListDeclField fields){
        //setting the fields
        int k = 1;
        GPRegister rtmp = compiler.getRegisterManager().allocReg(compiler);
        for (AbstractDeclField field : fields.getList()){
            ExpDefinition def = ((DeclField)field).getNameField().getExpDefinition();
            def.setOperand(new RegisterOffset(k, rtmp)); k++;
        }
        
        int j = 1;
        int n = getList().size();
        for (AbstractDeclMethod i : getList()) {
            i.codeGenMethod(compiler, rtmp);
            j ++;
        }
    }**/
    public void codeGenListMethod(DecacCompiler compiler, ListDeclField fields){
        for (AbstractDeclMethod method : getList()) {
            method.codeGenMethod(compiler);
            AbstractIdentifier meth = ((DeclMethod)(method)).getNameMethod();
            /*compiler.addInstruction(new BRA(new Label("fin."+((Identifier)(meth)).getMethodDefinition().getLabel().toString())));
            compiler.addInstruction(new WSTR("erreur sortie sans return"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
            compiler.addLabel(new Label("fin."+((Identifier)(meth)).getMethodDefinition().getLabel().toString()));
            compiler.addInstruction(new RTS());**/
        }
    
    }


}
