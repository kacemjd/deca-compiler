/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.ensimag.deca.codegen;

/**
 *
 * @author ensimag
 */
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import java.util.*;
public class InitManager {
    protected Stack<GPRegister> regDispo = new Stack<GPRegister>();
    protected LinkedList<GPRegister> regNonDispo = new LinkedList<GPRegister>();
    protected Stack<GPRegister> regEcrase = new Stack<GPRegister>();
    public InitManager(int j){
        for(int i = 2; i<j+1; i++){
            regDispo.push(Register.getR(j+2-i));
        }
    }
    public GPRegister getDispo(){
        return this.regDispo.pop();
    }
    public GPRegister getNonDispo(){
        GPRegister r = regNonDispo.poll();
        regNonDispo.add(r);
        regEcrase.push(r);
        return r;
    }
    public void setRegDispo(GPRegister r){
        regDispo.add(r);
    }
    public void setRegNonDispo(GPRegister r){
        regNonDispo.add(r);
    }

}
