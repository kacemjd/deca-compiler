package fr.ensimag.deca.tools;

import java.util.HashMap;
import java.util.Map;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tree.Location;
/**
 * Manage unique symbols.
 *
 * A Symbol contains the same information as a String, but the SymbolTable
 * ensures the uniqueness of a Symbol for a given String value. Therefore,
 * Symbol comparison can be done by comparing references, and the hashCode()
 * method of Symbols can be used to define efficient HashMap (no string
 * comparison or hashing required).
 *
 * @author gl53
 * @date 01/01/2020
 */
public class SymbolTable {
    private Map<String, Symbol> map = new HashMap<String, Symbol>();

    /**
     * Create or reuse a symbol.
     *
     * If a symbol already exists with the same name in this table, then return
     * this Symbol. Otherwise, create a new Symbol and add it to the table.
     */
    public Symbol create(String name) {
        // throw new UnsupportedOperationException("Symbol creation");
        if(!map.containsKey(name)){
          map.put(name, new Symbol(name));
        }
        return map.get(name);
    }
    

    public Symbol getSymbol(String name)
    {
        if (this.map.containsKey(name))
        {
            return this.map.get(name);
        }
        return null;
        // exception
    }
    public boolean checkSymbol(String name)
    {
        if (this.map.containsKey(name))
        {
            return true;
        }
        return false;
    }
    public static class Symbol {
        // Constructor is private, so that Symbol instances can only be created
        // through SymbolTable.create factory (which thus ensures uniqueness
        // of symbols).
        private Symbol(String name) {
            super();
            this.name = name;
        }

        public String getName() {
            return name;
        }
        @Override
        public String toString() {
            return name;
        }
        @Override
        public int hashCode(){
            return name.hashCode();
        }
        @Override
        public boolean equals(Object o){
            if( ! (o instanceof Symbol)) {
                return false ;
            }
            Symbol other = (Symbol)o;
            return other.getName().equals(this.getName());
        }
        private String name;
        public Type getType()
        {

            if (this.getName().equals("int"))
            {
                return new IntType(this);
            }
            if (this.getName().equals("String"))
            {
                return new StringType(this);
            }
            if (this.getName().equals("float"))
            {
                return new FloatType(this);
            }
            if (this.getName().equals("boolean"))
            {
                return new BooleanType(this);
            }
            if (this.getName().equals("void"))
            {
                return new VoidType(this);
            }
            /*
            if (this.getName().equals("class"))
            {
                return new ClassType(this, Location.BUILTIN, null);
            }*/
            if (this.getName().equals("null"))
            {
                return new NullType(this);
            }

            return null;
        }
    }
}
