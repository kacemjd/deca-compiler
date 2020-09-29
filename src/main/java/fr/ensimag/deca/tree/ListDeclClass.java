package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 *
 * @author gl53
 * @date 01/01/2020
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        //LOG.debug("verify listClass: start");

        Iterator<AbstractDeclClass> classes = this.iterator();
        while (classes.hasNext())
        {
            AbstractDeclClass classe = classes.next();
            classe.verifyClass(compiler);
        }
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
      Iterator<AbstractDeclClass> classes = this.iterator();
      while (classes.hasNext())
      {
          AbstractDeclClass classe = classes.next();
          classe.verifyClassMembers(compiler);
      }
        //throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
      Iterator<AbstractDeclClass> classes = this.iterator();
      while (classes.hasNext())
      {
          AbstractDeclClass classe = classes.next();
          classe.verifyClassBody(compiler);
      }
        //throw new UnsupportedOperationException("not yet implemented");
    }
    public void codeGenListClasse(DecacCompiler compiler){
        for (AbstractDeclClass i : getList()) {
            i.codeGenClass(compiler);
        }
    }
    
    public void codeGenListClassPass2(DecacCompiler compiler){
        for (AbstractDeclClass i : getList()) {
            i.codeGenClassPass2(compiler);
        }
    }


}
