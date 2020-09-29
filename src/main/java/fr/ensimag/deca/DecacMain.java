package fr.ensimag.deca;

import fr.ensimag.deca.tree.AbstractProgram;
import java.io.File;
import java.io.PrintStream;
import org.apache.log4j.Logger;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tree.LocationException;
import java.util.concurrent.*;
import java.util.concurrent.Executors;
import java.lang.Runtime.*;
import java.util.ArrayList;
import java.io.*;
import java.lang.System;


/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl53
 * @date 01/01/2020
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);

    public static void main(String[] args){
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            //System.out.println("hi");
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            //options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            //throw new UnsupportedOperationException("decac -b not yet implemented");
            System.out.println("Compilateur DECA. Equipe GL53."); System.exit(0);
        }
        if (options.getSourceFiles().isEmpty() && !options.getPrintBanner()) {
            System.out.println(
                     " Standard usage of our compiler DECA :\n"
                    + " decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w]"
                    + " <fichier deca>...] | [-b]\n"
                    + " -b       (banner)       : affiche une bannière"
                    + " indiquant le nom de l’équipe.\n"
                    + " -p       (parse)        : arrête decac après"
                    + " l’étape de construction del’arbre, et affiche"
                    + " la décompilation de ce dernier.\n"
                    + " -v       (verification) : arrête decac après"
                    + " l’étape de vérifications(ne produit aucune "
                    + " sortie en l’absence d’erreur).\n"
                    + " -n       (no check)     : supprime les tests"
                    + " de débordement à l’exécution- débordement"
                    + " arithmétique- débordement mémoire- déréférencement"
                    + " de null.\n"
                    + " -r X     (registers)    : limite les registres"
                    + " banalisés disponibles àR0 ... R{X-1}, avec"
                    + " 4 <= X <= 16.\n"
                    + " -d       (debug)        : active les traces"
                    + " de debug. Répéter l’option plusieurs fois pour*"
                    + " avoir plus detraces..\n"
                    + " -P       (parallel)     : s’il y a plusieurs"
                    + " fichiers sources,lance la compilation des fichiers"
                    + " en parallèle (pour accélérer la compilation)");
            System.exit(0);
        }
        if (options.getParallel()) {
            ExecutorService e = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            ArrayList<Future<Boolean>> l = new ArrayList<Future<Boolean>>();
            for (File source : options.getSourceFiles()) {
                Future<Boolean> future
                        = e.submit(new Callable<Boolean>() {
                          public Boolean call() {
                              DecacCompiler compiler = 
                                        new DecacCompiler(options, source);
                              return compiler.compile();
                          }});
                l.add(future);
            }
            for (Future<Boolean> future : l){
                try{
                    if(future.get()){
                        error = true;
                    }
                }
                catch(ExecutionException er){
                    er.printStackTrace();
                }
                catch( InterruptedException er2){
                    er2.printStackTrace();
                }
            }
        }
        else if ( !options.getParallel()){
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
