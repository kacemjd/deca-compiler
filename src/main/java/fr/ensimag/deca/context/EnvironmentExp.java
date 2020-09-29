package fr.ensimag.deca.context;
import java.util.*;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.util.Map;
import fr.ensimag.deca.context.*;
/**
 * Dictionary associating identifier's ExpDefinition to their names.
 *
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 *
 * The dictionary at the head of this list thus corresponds to the "current"
 * block (eg class).
 *
 * Searching a definition (through method get) is done in the "current"
 * dictionary and in the parentEnvironment if it fails.
 *
 * Insertion (through method declare) is always done in the "current" dictionary.
 *
 * @author gl53
 * @date 01/01/2020
 */
public class EnvironmentExp {

    EnvironmentExp parentEnvironment;
    protected Map<Symbol, ExpDefinition> dictionary;

    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        dictionary = new HashMap<Symbol, ExpDefinition>();
    }
    public void setParent(EnvironmentExp parentEnvironment)
    {
      this.parentEnvironment = parentEnvironment;
    }
    public EnvironmentExp getParent()
    {
      return this.parentEnvironment;
    }

    public Map<Symbol, ExpDefinition> getMapMethod(){
        Set<Map.Entry<Symbol, ExpDefinition>> couples = dictionary.entrySet();
        Iterator<Map.Entry<Symbol, ExpDefinition>> itCouples = couples.iterator();
        HashMap<Symbol, ExpDefinition> dic = new HashMap<Symbol, ExpDefinition>();
        while (itCouples.hasNext()) {
            Map.Entry<Symbol, ExpDefinition> couple = itCouples.next();
            ExpDefinition def = couple.getValue();
            if(def.isMethod()){
                dic.put(couple.getKey(), def);
            }
        }
        return dic;
    }
    public Map<Symbol, ExpDefinition> getMapField(){
        Set<Map.Entry<Symbol, ExpDefinition>> couples = dictionary.entrySet();
        Iterator<Map.Entry<Symbol, ExpDefinition>> itCouples = couples.iterator();
        HashMap<Symbol, ExpDefinition> dic = new HashMap<Symbol, ExpDefinition>();
        while (itCouples.hasNext()) {
            Map.Entry<Symbol, ExpDefinition> couple = itCouples.next();
            ExpDefinition def = couple.getValue();
            if(def.isField()){
                dic.put(couple.getKey(), def);
            }
        }
        return dic;
    }
    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public ExpDefinition get(Symbol key) {
        Symbol s = this.stringIsIn(key);
        if (s != null)
        {
            return dictionary.get(s);


        }
        else
        {
            if (this.parentEnvironment != null)
            {
                return this.parentEnvironment.get(key);
            }
            else
            {
                return null;

            }
        }


    }
    /** Compatibilté pour l'affectation */
    public boolean assignCompatible(Type t1, Type t2)
    {
        if ((t1.isFloat() & t2.isInt())||(this.subType(t2, t1)) )
        {
            return true;
        }
        return false;
    }
    public boolean castCompatible(Type t1, Type t2)
    {
        if (t1.isVoid())
        {
            return false;
        }
        if (this.assignCompatible(t1, t2) || this.assignCompatible(t2, t1))
        {
            return true;
        }
        return false;
    }
    /** Relation de sous-typage */
    public boolean subType(Type t1, Type t2)
    {
        if (!t2.isClass() && t1.sameType(t2))
        {
            return true;
        }
        else if (t1.isClass() && t2.isClass() && ((ClassType)t1).isSubClassOf((ClassType)t2))
        {
          return true;
        }
        else if (t1.isNull() && t2.isClass())
        {
          return true;
        }
        return false;

    }
    public Set<Symbol> stringIsIn()
    {
        return this.dictionary.keySet();
    }
    public Symbol stringIsIn(Symbol key)
    {
        Set<Symbol> sym = this.stringIsIn();
        for (Symbol s:sym)
        {
            if(s.getName().equals(key.toString()))
            {
                return s;
            }
        }
          return null;

    }
    public boolean isIn(Symbol key)
    {
        if (this.get(key) != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     *
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary
     * - or, hides the previous declaration otherwise.
     *
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        //throw new UnsupportedOperationException("not yet implemented");
        if (this.stringIsIn(name) != null)
        {
            throw new DoubleDefException();
        }
        else
        {
            dictionary.put(name, def);
        }
    }

}
