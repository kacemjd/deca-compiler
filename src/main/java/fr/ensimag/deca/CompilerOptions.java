package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.*;
import java.util.stream.IntStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import java.lang.System;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl53
 * @date 01/01/2020
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    public boolean getParse(){
        return parse;
    }

    public boolean getVerification(){
        return verification;
    }

    public boolean getNoCheck(){
        return noCheck;
    }

    public int getRegisters(){
        return registers;
    }

    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean parse = false;
    private boolean verification = false;
    private boolean noCheck = false;
    private int registers = 16;
    private List<File> sourceFiles = new ArrayList<File>();


    public void parseArgs(String[] args) throws CLIException {
        // A FAIRE : parcourir args pour positionner les options correctement.
        Logger logger = Logger.getRootLogger();
        if (args.length > 0){
        if (args[0].equals("-b")){
            if (args.length==1){
                printBanner = true;
            }
            else{
                throw new CLIException("Bad arguments for decac");
            }
        }else{
        for (int index=0; index<args.length; index++){
            String arg = args[index];
            if (arg.substring(0,1).equals("-")){
                switch (arg){
                    case "-p":
                        if (verification){
                            throw new CLIException("Bad arguments for decac");}
                        else{parse = true;}
                        break;
                    case "-v":
                        if (parse){throw new CLIException("Bad arguments for decac");}
                        else{verification = true;}
                        break;
                    case "-n": noCheck = true; break;
                    case "-d": debug = Math.min(debug+1, 3); break;
                    case "-P": parallel = true; break;
                    case "-r":
                        try {
                            int x = Integer.parseInt(args[index+1]);index++;
                            assert(x>=4 && x<=16); registers = x;
                        }catch(NumberFormatException | AssertionError e){
                            throw new CLIException("Bad arguments for decac");}
                        break;
                    default:
                        throw new CLIException("Bad arguments for decac hna");}
            }
            else if (arg.length()>=5 &&
                    arg.substring(arg.length()-5, arg.length()).equals(".deca")){
                sourceFiles.add(new File(arg));
            }
            else{
                throw new CLIException("Bad arguments for decac");
            }
        }
        }
        }

        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

        //throw new UnsupportedOperationException("not yet implemented");
    }

    protected void displayUsage() {
        throw new UnsupportedOperationException("not yet implemented555");
    }
}
