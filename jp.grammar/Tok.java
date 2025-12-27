 
import decaf.lexer.*;
import decaf.node.*;
import java.io.*;

class Tok 

{

Object t;
Lexer lexer;

Tok (Lexer lxr)
{   lexer = lxr; }

void getToken()
{

       try { 
        t =  lexer.next(); 
        while (t instanceof TSpace ||
            t instanceof TComment1 ||
            t instanceof TComment2)
        t =  lexer.next(); 
       }
        
    catch (LexerException le)
        {   System.out.println ("Lexer exception: " + le); }
    catch (IOException ioe)
        {   System.out.println ("IO Exception: " + ioe); }
}

void peek()
{
       try {    t = lexer.peek();
        while (t instanceof TSpace ||
            t instanceof TComment1 ||
            t instanceof TComment2)
          { t = lexer.next();
            t =  lexer.peek(); 
          }
       }
    catch (LexerException le)
        {   System.out.println ("Lexer exception: " + le); }
    catch (IOException ioe)
        {   System.out.println ("IO Exception: " + ioe); }
}

public static final int
    EOF =       0,
    IDENT=              257,
    INT =               258,
    FLOAT =             259,
    FOR =               260,
    WHILE =             261,
    IF =                262,
    ELSE =              263,
    COMPARE =           264,
    NUM =               265,
    LPar =              266,
    RPar =              267,
    LBrace =            268,
    RBrace =            269,
    LBracket =          270,
    RBracket =          271,
    SEMI =              272,
    PLUS =              273,
    MINUS =             274,
    MUL =               275,
    DIV =               276,
    ASSIGN =            278,
    VOID =              279,
    MAIN =              280,
    PUBLIC =            281,
    STATIC =            282,
    STRING =            283,
    COMMA =         284,
    CLASS =         285,
    DO = 286,
    MISC =       287;

public int get_class()
{   
    if (t instanceof TIdentifier)    return IDENT;
    if (t instanceof TInt)      return INT;
    if (t instanceof TFloat)    return FLOAT;
    if (t instanceof TFor)      return FOR;
    if (t instanceof TWhile)    return WHILE;
    if (t instanceof TIf)       return IF;
    if (t instanceof TElse)     return ELSE;
    if (t instanceof TCompare)  return COMPARE;
    if (t instanceof TNumber)    return NUM;
    if (t instanceof TLPar)     return LPar;
    if (t instanceof TRPar)     return RPar;
    if (t instanceof TLBrace)   return LBrace;
    if (t instanceof TRBrace)   return RBrace;
    if (t instanceof TLBracket) return LBracket;
    if (t instanceof TRBracket) return RBracket;
    if (t instanceof TSemi)     return SEMI;
    if (t instanceof TComma)    return COMMA;
    if (t instanceof TPlus)     return PLUS;
    if (t instanceof TMinus)    return MINUS;
    if (t instanceof TMult)      return MUL;
    if (t instanceof TDiv)      return DIV;
    if (t instanceof TAssign)   return ASSIGN;
    if (t instanceof TVoid)     return VOID;
    if (t instanceof TMain)     return MAIN;
    if (t instanceof TPublic)   return PUBLIC;
    if (t  instanceof TStatic)  return STATIC;
    if (t  instanceof TString)  return STRING;
    if (t instanceof  TClas)    return CLASS;
    if (t instanceof TDo) return DO; //added do statement
   //if (t instanceof  TEof)     return EOF;
   if (t instanceof TMisc)  return MISC;


   return 0;
}   

public String getValue()
{  if (! (t instanceof TCompare))
    return t.toString(); 
   if (((Token)t).getText().equals("==")) return "1";
   if (((Token)t).getText().equals("<"))  return "2";
   if (((Token)t).getText().equals(">"))  return "3";
   if (((Token)t).getText().equals("<=")) return "4";
   if (((Token)t).getText().equals(">=")) return "5";
   if (((Token)t).getText().equals("!=")) return "6";

return "";

}

public int getLine()
{  return ((Token)t).getLine() ;  }

public int getPos()
{  return ((Token)t).getPos() ;  }

}