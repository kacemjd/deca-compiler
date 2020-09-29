package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Syntax error for an Integer that depasses 2^32.
 *
 * @author gl53
 * @date 01/01/2020
 */
public class InvalidInteger extends DecaRecognitionException {

    private static final long serialVersionUID = 4670163376041273741L;

    private final String name;

    public InvalidInteger(String name, DecaParser recognizer, ParserRuleContext ctx) {
        super(recognizer, ctx);
        this.name=name;
    }

    @Override
    public String getMessage() {
        return name + " is a bad Integer format, it depasses 2^32 bits";
    }
}
