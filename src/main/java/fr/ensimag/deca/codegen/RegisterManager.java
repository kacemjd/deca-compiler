package fr.ensimag.deca.codegen;
/**
 * 
 * @author gl53
 * @date 01/01/2020
 */

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import java.util.*;
public class RegisterManager{
    private static InitManager initR;
    private static int cptOffset = 1;
    private static int cptOffsetM = 1;
    public HashSet<GPRegister> used = new HashSet<GPRegister>();
    public RegisterManager(DecacCompiler compiler){
        initR = new InitManager(compiler.getCompilerOptions().getRegisters()-1);
    }
    //System.out.println(initR);
    public static GPRegister allocReg(DecacCompiler compiler){
        int cpt = Register.getCpt();
        if(!initR.regDispo.empty()){
            GPRegister r = initR.regDispo.pop();
            initR.regNonDispo.add(r);
            return r;    
        }
        else{
            GPRegister r = initR.getNonDispo();
            compiler.incTs();
            compiler.addInstruction(new BOV(compiler.pilePleine));
            compiler.addInstruction(new PUSH(r));
            return r;
        }
    }
    public static void freeReg(DecacCompiler compiler, GPRegister r){
        if(initR.regEcrase.contains(r)){ 
            compiler.addInstruction(new POP(r));
            initR.regEcrase.pop();
        }
        else if(r.getNumber() != 0 && r.getNumber() != 1){
            r.freeR();
            initR.regNonDispo.remove(r);
            initR.regDispo.push(r);        
        }
    }
    public RegisterOffset getRegOff(){
        RegisterOffset reg = new RegisterOffset(cptOffset, Register.GB);
        cptOffset ++;
        return reg;
    }
    public void reset(){
        
        for(GPRegister r: initR.regDispo){
            initR.regNonDispo.add(r);
        }
        initR.regDispo = new Stack<GPRegister>();
    }  
    public Stack<GPRegister> getNonDispo(){
        return initR.regDispo;
    }
    public RegisterOffset getRegOffM(){
        RegisterOffset reg = new RegisterOffset(cptOffsetM, Register.LB);
        cptOffsetM ++;
        return reg;
    }
}