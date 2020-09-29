package fr.ensimag.deca.tree;

/**
 * Visibility of a field.
 *
 * @author gl53
 * @date 01/01/2020
 */
public class Visibility
{
    public enum s
    {
    PUBLIC,
    PROTECTED
    }
    private String visibility;
    public Visibility()
    {
        this.visibility = new String("PUBLIC");
    }
    public Visibility(String type)
    {
        if(!type.equals("protected"))
        {
          throw new UnsupportedOperationException("This is impossible");
        }
        this.visibility = new String("PROTECTED");
    }
    public String getValue() {
        return visibility;
    }

}
