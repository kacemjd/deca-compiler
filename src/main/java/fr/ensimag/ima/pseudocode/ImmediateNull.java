/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.ensimag.ima.pseudocode;

/**
 *
 * @author ensimag
 */
public class ImmediateNull extends DVal{
    public ImmediateNull() {
        super();
    }

    @Override
    public String toString() {
        return "#" + "null";
    }
}
