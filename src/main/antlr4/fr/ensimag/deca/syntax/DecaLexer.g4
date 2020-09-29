lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

//Reserved
SEMI : ';';
ASM : 'asm';
INSTANCEOF : 'instanceof';
PRINTLN : 'println';
TRUE : 'true';
CLASS : 'class';
NEW : 'new';
PRINTLNX : 'printlnx';
WHILE : 'while';
EXTENDS : 'extends';
NULL : 'null';
PRINTX : 'printx';
ELSE : 'else';
READINT : 'readInt';
PROTECTED : 'protected';
FALSE : 'false';
READFLOAT : 'readFloat';
RETURN : 'return';
IF : 'if';
PRINT : 'print';
THIS : 'this';


// Mots reserves
//fragment RESERV : ('asm'|'class'|'extends'|'else'|'false'|'if'|'instanceof'|'new'
//      |'null'|'readInt'|'readFloat'|'print'|'println'|'printlnx'
//    |'printx'|'protected'|'return'|'this'|'true'|'while');


//Symbole Speciaux
PLUS : '+' ;
MINUS : '-' ;
TIMES : '*' ;
SLASH : '/';
PERCENT : '%';
EXCLAM : '!';

LEQ : '<=';
GEQ : '>=';
LT : '<';
GT : '>';
EQUALS : '=';
EQEQ : '==';
NEQ : '!=';

DOT : '.';
COMMA : ',';

OPARENT : '(';
CPARENT : ')';
OBRACE : '{';
CBRACE : '}';

OR : '||';
AND : '&&';

fragment EOL : '\n';
fragment TAB : '\t';
//EMPTY :
//Identificateur
fragment LETTER : 'a' .. 'z'|'A' .. 'Z';
fragment DIGIT : '0' .. '9';


fragment SKIPCAR : (' ' |EOL |TAB |'\r');

//Litteraux entiers
fragment POSITIVE_DIGITS : '1' .. '9';
INT : ('0' |POSITIVE_DIGITS)+;


//Litteraux Flottant
fragment HEXA_MAJ : 'A' .. 'F';
fragment HEXA_MIN : 'a' .. 'f';

fragment NUM : DIGIT+;
fragment SIGN : ('+' |'-')?;
fragment EXP : ('E' | 'e') SIGN NUM;
fragment DEC : NUM '.' NUM;
fragment FLOATDEC : (DEC | DEC EXP) (('F' | 'f')?);
fragment DIGITHEX : DIGIT | HEXA_MAJ| HEXA_MIN;
fragment NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM (('F' | 'f')?);
FLOAT : FLOATDEC | FLOATHEX;

//Chaine de caracteres
fragment STRING_CAR : ~('"' | '\\' | '\n');
STRING : '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING : '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';
//
//Commentaires
fragment COMMENT : ('/*' .*? '*/') | ('//' .*? ('\n' | EOF));

//Separateurs
WS : (SKIPCAR | COMMENT) {skip();};

//Inclusion de Fichier
fragment FILENAME : (LETTER | DIGIT | '.' | '-' | '_')+;
//La fonction doInclude est situé dans AbstractDecaLexer.java


INCLUDE : '#include' (' ')* '"' FILENAME '"'{doInclude(getText());};
IDENT : (((LETTER |'$' |'_')(LETTER |DIGIT |'$' |'_')*));
