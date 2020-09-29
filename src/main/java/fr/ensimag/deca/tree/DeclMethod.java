package fr.ensimag.deca.tree;
import java.util.*;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
/**
 * @author gl53
 * @date 01/01/2020
 */
public class DeclMethod extends AbstractDeclMethod
{
    final private AbstractIdentifier type;
    final private AbstractIdentifier method;
    final private ListDeclParam params;
    private MethodBody methodBody;
    final private MethodAsmBody code;

    private static final Logger LOG = Logger.getLogger(DeclMethod.class);
    public DeclMethod(AbstractIdentifier type, AbstractIdentifier method,
        ListDeclParam params, MethodBody methodBody) {
        Validate.notNull(type);
        Validate.notNull(method);
        Validate.notNull(params);
        Validate.notNull(methodBody);
        this.type = type;
        this.method = method;
        this.params = params;
        this.methodBody = methodBody;
        this.code = null;
    }
    public DeclMethod(AbstractIdentifier type, AbstractIdentifier method,
        ListDeclParam params, MethodAsmBody code) {
        Validate.notNull(type);
        Validate.notNull(method);
        Validate.notNull(params);
        Validate.notNull(code);

        this.type = type;
        this.method = method;
        this.params = params;
        this.code = code;
        this.methodBody = null;
    }
    public MethodBody getBody()
    {
        return this.methodBody;
    }
    public MethodAsmBody getCode()
    {
        return this.code;
    }
    public AbstractIdentifier getNameType()
    {
        return this.type;
    }
    public AbstractIdentifier getNameMethod()   {
        return this.method;
    }
    public ListDeclParam getParams()   {
        return this.params;
    }

    @Override
    protected void verifyDeclMethod(DecacCompiler compiler,
            AbstractIdentifier superIdentifier, AbstractIdentifier classIdentifier) throws ContextualError {
            Type type = this.getNameType().verifyType(compiler);
            Signature sig = this.getParams().verifyListDeclParam(compiler);
            Definition override = superIdentifier.getClassDefinition().getMembers().get(getNameMethod().getName());
            String erreur = new String("La méthode \""
                      + this.method.decompile() + "\" est déja définie "
                      + "dans une super classe ");
            if (override != null && !(override instanceof MethodDefinition))
            {
              erreur += "en tant que \"" + override.getNature() + "\" à "+
                      override.getLocation() + " (règle 2.7)";
              throw new ContextualError(erreur, method.getLocation());
            }
            else if (override != null && !((MethodDefinition) override).getSignature().equals(sig))
            {
              erreur +=  "à " + override.getLocation() +
                        " avec une autre signature (règle 2.7)";
              throw new ContextualError(erreur, method.getLocation());
            }
            else if (override != null && !compiler.get_env_types().subType(type, override.getType()))
            {
              erreur +=  "à " + override.getLocation() +
                      ". Le type de retour \"" + type.toString() +
                      "\" n'est pas un sous type de \"" +
                      override.getType().toString() + "\" (règle 2.7)";
              throw new ContextualError(erreur, method.getLocation());

            }

            ClassDefinition classDef = classIdentifier.getClassDefinition();
            Symbol symbol = this.getNameMethod().getName();
            int index = classDef.getNumberOfMethods();
            if (override == null)
            {
              index ++;
            }
            classDef.setNumberOfMethods(index);
            MethodDefinition def = new MethodDefinition(type,
                    this.getLocation(), sig, index);
            def.setLabel(new Label("code." + classIdentifier.getName().getName() + "." + this.getNameMethod().getName().getName()));
            try
            {
                classDef.getMembers().declare(symbol, def);
            }
            catch (DoubleDefException e)
            {
              throw new ContextualError("La méthode \"" +
                      method.decompile() +
                      "\" est déjà déclarée dans cette classe  à " +
                      classDef.getMembers().get(symbol).getLocation() +
                      " (règle 2.6)", method.getLocation());
            }
            this.getNameMethod().verifyExpr(compiler, classDef.getMembers(), classDef);
    }
    @Override
    protected void verifyMethodBody(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError
        {
          Type rType = this.getNameType().getType();
          EnvironmentExp paramEnv = new EnvironmentExp(localEnv);
          this.getParams().verifyParams(compiler, paramEnv);

          MethodBody mBody = this.getBody();
          if (mBody != null)
          {
            mBody.verifyBody(compiler, localEnv, paramEnv, currentClass, rType);
          }
          else if (getCode() != null)
          {
            getCode().verifyBody(compiler, paramEnv, currentClass);
          }

        }
    @Override
    protected void codeGenMethod(DecacCompiler compiler){
       compiler.addLabel(new Label(getNameMethod().getMethodDefinition().getLabel().toString() ));
       params.codeGenListParam(compiler);
       IMAProgram p = new IMAProgram();
       IMAProgram org = compiler.getProg();
       compiler.setProgram(p);
       if(methodBody != null){
            methodBody.codeGenMethodBody(compiler);
       }
       else{
            code.codeGenBody(compiler);
        }
        HashSet<GPRegister> list = compiler.getRegisterManager().used;
        for(GPRegister reg: list){
            if(reg.getNumber() != 0){
                compiler.incTs();
                p.addFirst(new Line(new PUSH(reg)));
                compiler.addInstruction(new POP(reg));
            }
        }
        org.append(p);
        compiler.setProgram(org);
        compiler.getRegisterManager().used = new HashSet<GPRegister>();
        compiler.addInstruction(new RTS());
    }

    @Override
    public void decompile(IndentPrintStream s) {
      this.type.decompile(s);
      s.print(" ");
      this.method.decompile(s);
      s.print("(");
      this.params.decompile(s);
      s.println(")");

      if (getBody() != null)
      {
        getBody().decompile(s);
      }
      else
      {
        getCode().decompile(s);
      }
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        method.iter(f);
        params.iter(f);
        if (code == null)
        {
            methodBody.iter(f);
        }
        else
        {
          code.iter(f);
        }
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        method.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, true);
        if (code == null)
        {
          methodBody.prettyPrint(s, prefix, true);
        }
        else
        {
          code.prettyPrint(s, prefix, true);
        }
    }
}
