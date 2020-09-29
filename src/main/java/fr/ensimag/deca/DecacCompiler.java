package fr.ensimag.deca;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tools.SymbolTable;
import java.io.File;
import java.util.*;

import fr.ensimag.deca.context.EnvironmentType.DoubleDefException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import fr.ensimag.deca.context.*;

import fr.ensimag.deca.tree.Location;


import java.io.PrintStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.Label;


/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl53
 * @date 01/01/2020
 */
public class DecacCompiler {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");
    private Set<LabelOperand> labels = new HashSet<LabelOperand>();
    private int cptSp = 0;
    private int cptTs = 0;
    public boolean flag = false;
    public void incSp(){
        cptSp ++;
    }
    public void incTs(){
        cptTs ++;
    }
    /************************ Partie B  ***************************/
    private EnvironmentExp env_exp;
    private EnvironmentType env_types;
    private SymbolTable symbols;
    /****************************************************************/
    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        /********************** Partie B  ****************************/
        this.env_exp = new EnvironmentExp(null);
        this.env_types = new EnvironmentType(null);
        this.symbols = new SymbolTable();
        this.initialisation_symbols();


        /***************************************************************/
    }
    public void initialisation_symbols()
    {
        Symbol voidS = this.symbols.create("void");
        Symbol boolS = this.symbols.create("boolean");
        Symbol floatS = this.symbols.create("float");
        Symbol intS = this.symbols.create("int");
        this.symbols.create("String");
        TypeDefinition voidDef = new TypeDefinition(voidS.getType(), Location.BUILTIN);
        TypeDefinition boolDef = new TypeDefinition(boolS.getType(), Location.BUILTIN);
        TypeDefinition floatDef = new TypeDefinition(floatS.getType(), Location.BUILTIN);
        TypeDefinition intDef = new TypeDefinition(intS.getType(), Location.BUILTIN);
        this.symbols.create("null");

        Symbol object = this.symbols.create("Object");
        ClassType objectType = new ClassType(object, Location.BUILTIN, null);
        /*********************************************/
        Signature sig = new Signature();
        sig.add(objectType);
        Symbol equal = (new SymbolTable()).create("equals");
        MethodDefinition def = new MethodDefinition(boolS.getType(),
                Location.BUILTIN, sig, 1);
        def.setLabel(new Label("code.Object.equals"));
        objectType.getDefinition().setNumberOfMethods(1);
        try
        {
            objectType.getDefinition().getMembers().declare(equal, def);
            this.get_env_types().declare(voidS, voidDef);
            this.get_env_types().declare(boolS, boolDef);
            this.get_env_types().declare(floatS, floatDef);
            this.get_env_types().declare(intS, intDef);
            this.get_env_types().declare(object, objectType.getDefinition());
        }
        catch (EnvironmentType.DoubleDefException | EnvironmentExp.DoubleDefException e)
        {
            System.out.println("C'est impossible : C'est la première déclaration");
        }

    }

    public SymbolTable getSymbols()
    {
        return this.symbols;
    }
    public EnvironmentExp get_env_exp()
    {
        return this.env_exp;
    }
    public EnvironmentType get_env_types()
    {
        return this.env_types;
    }
    public void set_env_types(Symbol name, TypeDefinition def) throws ContextualError
    {
        this.env_types = new EnvironmentType(this.get_env_types());
        try
        {
            this.get_env_types().declare(name, def);
        }
        catch (EnvironmentType.DoubleDefException e)
        {
            throw new ContextualError("règle 2.3", def.getLocation());
        }

    }
    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }
    public void addLab(LabelOperand lab){
        labels.add(lab);
    }
    private RegisterManager registerManager;
    public RegisterManager getRegisterManager(){
        return registerManager;
    }


    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }

    private final CompilerOptions compilerOptions;
    private final File source;
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private IMAProgram program = new IMAProgram();
    public IMAProgram getProg(){
        return program;
    }
    public void setProgram(IMAProgram p){
        this.program = p;
    }

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        // A FAIRE: calculer le nom du fichier .ass à partir du nom du
        // A FAIRE: fichier .deca.
        String sourceFile = source.getAbsolutePath();
        String destFile = sourceFile.substring(0, sourceFile.length()-4)+ "ass";
        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        CompilerOptions cond = this.getCompilerOptions();
        registerManager = new RegisterManager(this);
        AbstractProgram prog = doLexingAndParsing(sourceName, err);
        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        if (cond.getParse()){
            IndentPrintStream fstream = new IndentPrintStream(new PrintStream(System.out));
            prog.decompile(fstream);
            return false;
        }
        //assert(prog.checkAllLocations());
       prog.verifyProgram(this);
        //assert(prog.checkAllDecorations());
        if (cond.getVerification()){
            return false;
        }

        //addComment("start main program");
        //prog.getMain().codeGenEntete(this, 13);
        prog.codeGenProgram(this);
        //addComment("end main program");
        if(flag){
            this.addLabel(new Label("code.Object.equals"));
            codeGenEquals();
        }
        this.codeGenErr();
        program.addFirst(new Line (new ADDSP(cptSp)));
        program.addFirst(new Line(new BOV(new Label("pile_pleine"))));
        program.addFirst(new Line(new TSTO(cptTs)));

        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }
    public static Label pilePleine= new Label("pile_pleine");
    public static Label over_flow = new Label("over_flow");
    public static Label i0Error = new Label("i0_error");
    public static Label divisionErr = new Label("divisionErr");
    protected void codeGenErr(){
        this.addLabel(pilePleine);
        this.addInstruction(new WSTR("EError: Stack Overfloww"));
        this.addInstruction(new WNL());
        this.addInstruction(new ERROR());
        this.addLabel(over_flow);
        this.addInstruction(new WSTR("EError: Overflow during arithmetic operationn"));
        this.addInstruction(new WNL());
        this.addInstruction(new ERROR());
        this.addLabel(i0Error);
        this.addInstruction(new WSTR("EError: Input/Output errorr"));
        this.addInstruction(new WNL());
        this.addInstruction(new ERROR());
        this.addLabel(divisionErr);
        this.addInstruction(new WSTR(("EError :Division par 0 ")));
        this.addInstruction(new WNL());
        this.addInstruction(new ERROR());
    }
    protected void codeGenEquals(){
         GPRegister r1 = this.getRegisterManager().allocReg(this);
         GPRegister r2 = this.getRegisterManager().allocReg(this);
         this.addInstruction( new PUSH(r1));
         this.addInstruction(new PUSH(r2));
        this.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), r1));
        this.addInstruction(new LOAD(new RegisterOffset(-3, Register.LB), r2));
        this.addInstruction(new CMP(r1, r2));
        this.addInstruction(new SNE(Register.R0));
        this.cptTs ++; this.cptTs ++;
        this.addInstruction(new POP(r2));
        this.addInstruction(new POP(r1));
        this.getRegisterManager().freeReg(this, r1);
        this.getRegisterManager().freeReg(this, r1);
        this.addInstruction(new RTS());
    }
}
