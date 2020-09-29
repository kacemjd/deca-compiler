package fr.ensimag.ima.pseudocode;

/**
 * Immediate operand representing a string.
 * 
 * @author Ensimag
 * @date 01/01/2020
 */
public class ImmediateString extends Operand {
    private String value;

    public ImmediateString(String value) {
        super();
        String tmp = value.substring(1, value.length()-1);//enlever les ""
        this.value = "";
        for (int i = 0; i<tmp.length(); i++){
            if (String.valueOf(tmp.charAt(i)).equals("\\")){
                switch(String.valueOf(tmp.charAt(i+1))){
                    case "\"":break;
                    default: this.value += String.valueOf(tmp.charAt(i));
                }
            }
            else{
                this.value += String.valueOf(tmp.charAt(i));
            }
        }
        
    }
    public String getValue(){
        return this.value;
    }

    @Override
    public String toString() {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
