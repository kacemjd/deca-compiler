package fr.ensimag.deca.tree;
import java.util.Iterator;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

/**
 * List of declarations (e.g. int x; float y,z).
 *
 * @author gl53
 * @date 01/01/2020
 */
public class ListDeclParam extends TreeList<AbstractDeclParam> {

    @Override
    public void decompile(IndentPrintStream s) {

      Iterator<AbstractDeclParam> params = this.iterator();
      AbstractDeclParam param;
      if (params.hasNext())
      {
          param =  params.next();
          param.decompile(s);
      }
      while (params.hasNext())
      {
          param = params.next();
          s.print(", ");
          param.decompile(s);
      }

    }

    /**
     * Implements non-terminal "list_decl_Param" of [SyntaxeContextuelle] in pass 3
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
    Signature verifyListDeclParam(DecacCompiler compiler) throws ContextualError {
        Iterator<AbstractDeclParam> declParams = this.iterator();
        Signature sig = new Signature();

        while (declParams.hasNext())
        {
            DeclParam declParam = (DeclParam) declParams.next();
            sig.add(declParam.verifyDeclParam(compiler));


        }
        return sig;
    }
    int i = 1;
    void verifyParams(DecacCompiler compiler,
        EnvironmentExp paramEnv)  throws ContextualError

        {
          Iterator<AbstractDeclParam> declParams = this.iterator();
          while (declParams.hasNext())

          {
              DeclParam declParam = (DeclParam) declParams.next();
              declParam.verifyParam(compiler, paramEnv);
              ParamDefinition def = (ParamDefinition)(declParam.getParam().getDefinition());
              def.setIndex(i);
              i ++;
          }
        }
    public void codeGenListParam(DecacCompiler compiler){
        for (AbstractDeclParam i : getList()) {
            i.codeGenParam(compiler);
        }
    }


}
