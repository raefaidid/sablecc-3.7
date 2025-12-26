package lexing;
import lexing.lexer.*;
import lexing.node.*;
import java.io.*;

class Lexing
{
    static Lexer lexer;
    static Object token;
    // Needed for pushbackreader and
    // inputstream
    public static void main(String [] args)
    {
        lexer = new Lexer
            (new PushbackReader
                (new InputStreamReader (System.in), 1024));
        token = null;
        try {
            while ( ! (token instanceof EOF))
            {
                token = lexer.next();     // read next token
                if (token instanceof TNumber)
                    System.out.print ("Number:       ");
                else if (token instanceof TIdent)
                    System.out.print ("Identifier:  ");
                else if (token instanceof TArithOp)
                    System.out.print ("Arith Op:    ");
                else if (token instanceof TRelOp)
                    System.out.print ("Relational Op: ");
                else if (token instanceof TParen)
                    System.out.print ("Parentheses  ");
                else if (token instanceof TBlank)  ;
                    // Ignore white space
                else if (token instanceof TUnknown)
                    System.out.print ("Unknown        ");
                if (! (token instanceof TBlank))
                    System.out.println (token);   // print token as a string
            }
        }
        catch (LexerException le)
        {
            System.out.println ("Lexer Exception " + le);
        }
        catch (IOException ioe)
        {
            System.out.println ("IO Exception " + ioe);
        }
    }
}