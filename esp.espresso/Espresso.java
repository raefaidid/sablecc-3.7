// Recursive Descent Parser for decaf-named espresso, with Attributes and Atoms Output

 
import decaf.lexer.*;
import java.io.*;       // Needed for pushbackreader, inputstream
import java.util.*;     // Needed for Hashtable

class Espresso
{
static  Lexer lexer;
static  Tok token;
static MutableInt zero;
static int newlabel;
static int available;
static double [] memory;

static Hashtable symbols;       // symbol table (identifiers)
static Hashtable numbers;       // numeric constants

public static void main (String [] args)
{
  Espresso espresso;
  espresso = new Espresso();
  token.getToken();
  espresso.Program();
  
  System.out.println("Variables:"+symbols);
  System.out.println("Numbers:"+numbers);
  
  if (token.get_class()!=Tok.EOF)
    espresso.err ("Extraneous code beyond end of program");
}

Espresso ()
{
  newlabel = 0;
  available = 0;
  zero = new MutableInt (0);
  memory = new double[1024];
  symbols = new Hashtable (100);
  numbers = new Hashtable (100);
  lexer = new Lexer
                (new PushbackReader
                        (new InputStreamReader (System.in), 1024));
  token = new Tok (lexer);

}

void err (String msg)
{   System.out.println (
    "[" + token.getLine() + "," + token.getPos() + "] "
     + msg + ", input is " + token.getValue());
    System.exit(0);
}
 
MutableInt newlab ()
// allocate a new label number
{ return new MutableInt (newlabel++);}

MutableInt alloc ()
// allocate space for a temp
{  return new MutableInt (available++); }

void atom (String cls, MutableInt left, MutableInt right, 
        MutableInt result, MutableInt comp, MutableInt dest)
/*  Put out an atom with parameters:
    cls     atom Class - ADD, SUB, MUL, DIV, MOV, TST, JMP, LBL
    left    left operand, for ADD, SUB, MUL, DIV, MOV, TST
    right   right operand for ADD, SUB, MUL, DIV, TST
    result  result of operation for ADD, SUB, MUL, DIV, MOV
    comp    comparison code 1-6 for TST
    dest    destination for TST, JMP, LBL
    */
{   System.out.println (
            cls + " T" +
        left + " T" +
        right +  " T" +
        result + " C" +
        comp  + " L" +
        dest) ; 
}

void Program ()
{   if (token.get_class()==Tok.CLASS)
    token.getToken();
        else err ("expected 'class'");
    if (token.get_class()==Tok.IDENT)
    token.getToken();
    else err ("expected Identifier");
    if (token.get_class()==Tok.LBrace)
    token.getToken();
    else err ("expected '{'");
    if (token.get_class()==Tok.PUBLIC)
    token.getToken();
    else err ("expected 'public'");
    if (token.get_class()==Tok.STATIC)
    token.getToken();
    else err ("expected 'static'");
    if (token.get_class()==Tok.VOID)
    token.getToken();
    else err ("expected 'void'");
    if (token.get_class()==Tok.MAIN)
    token.getToken();
    else err ("expected 'main'");
    if (token.get_class()==Tok.LPar)
    token.getToken();
    else err ("expected '('");
    if (token.get_class()==Tok.STRING)
    token.getToken();
    else err ("expected 'String'");
    if (token.get_class()==Tok.LBracket)
    token.getToken();
    else err ("expected '['");
    if (token.get_class()==Tok.RBracket)
    token.getToken();
    else err ("expected ']'");
    if (token.get_class()==Tok.IDENT)
    token.getToken();
    else err ("expected identifier");
    if (token.get_class()==Tok.RPar)
    token.getToken();
    else err ("expected ')'");
    if (token.get_class()==Tok.LBrace) CompoundStmt();
    if (token.get_class()==Tok.RBrace) 
        token.getToken();
    else err ("expected '}'");
    int j=alloc().get();
}

void Declaration ()
{   if (token.get_class()==Tok.INT || token.get_class()==Tok.FLOAT)
    token.getToken();
    if (token.get_class()==Tok.IDENT)
      if (symbols.get (token.getValue())!=null)
     err ("error: " + token.getValue() + " already declared");
      else
      { symbols.put (token.getValue(), alloc());
    token.getToken();
      }
    else err ("expected identifier");
    while (token.get_class()==Tok.COMMA)
    { token.getToken();
      if (token.get_class()==Tok.IDENT) 
         {  symbols.put (token.getValue(), alloc());
        token.getToken();
         }
         else err ("expected identifier");
    }
    if (token.get_class()==Tok.SEMI) token.getToken();
    else err ("expected ; in declaration");
    }

void Stmt()
{
    int p;
    if (token.get_class()==Tok.FOR) ForStmt();
    else if (token.get_class()==Tok.WHILE) WhileStmt();
    else if (token.get_class()==Tok.DO) DoWhileStmt(); //do while statement added
    else if (token.get_class()==Tok.IF) IfStmt();
    else if (token.get_class()==Tok.LBrace) CompoundStmt();
    else if (token.get_class()==Tok.IDENT) AssignStmt();
    else if (token.get_class()==Tok.INT || token.get_class()==Tok.FLOAT) Declaration();
    else if (token.get_class()==Tok.SEMI) token.getToken(); // Null stmt
    
    else err ("expected a statememt");
}


void ForStmt()
{
   MutableInt l1,l3,l4;
   MutableInt p,q,l2;
   l1 = new MutableInt(0);
   l2 = new MutableInt(0);
   l3 = new MutableInt(0);
   l4 = new MutableInt(0);
   p = new MutableInt(0);
   q = new MutableInt(0);

   token.getToken();
   if (token.get_class()==Tok.LPar) token.getToken();
    else err ("expected left parentheses in for statement");
   if (token.get_class()!=Tok.SEMI) AssignExpr(p);      // initialization
   if (token.get_class()==Tok.SEMI) token.getToken();
    else err ("expected semicolon in for statement");
   l1 = newlab();
   atom ("LBL",zero,zero,zero,zero,l1);
   if (token.get_class()!=Tok.SEMI) BoolExpr(l2);       // loop condition
    else l2 = newlab();
   if (token.get_class()==Tok.SEMI) token.getToken();
    else err ("expected semicolon in for statement");
   l3 = newlab();
   atom ("JMP", zero, zero, zero, zero, l3);
   l4 = newlab();
   atom ("LBL", zero, zero, zero, zero, l4);
   if (token.get_class()!=Tok.RPar) AssignExpr(q);      // increment
   if (token.get_class()==Tok.RPar) token.getToken();
    else err ("expected ')' in for statement");
   atom ("JMP", zero, zero, zero, zero, l1);
   atom ("LBL", zero, zero, zero, zero, l3);
   Stmt ();
   atom ("JMP", zero, zero, zero, zero, l4);
   atom ("LBL", zero, zero, zero, zero, l2);
}

void WhileStmt()
{
   MutableInt l1,l2;
   l1 = new MutableInt (0);
   l2 = new MutableInt (0);
   token.getToken();
   l1 = newlab();
   atom ("LBL", zero, zero, zero, zero, l1);
   if (token.get_class()==Tok.LPar) token.getToken();
    else err ("expected left parenthesis in while statement");
   BoolExpr (l2);
   if (token.get_class()==Tok.RPar) token.getToken();
    else err ("expected right parenthesis in while statement");
   Stmt();
   atom ("JMP", zero, zero, zero, zero, l1);
   atom ("LBL", zero, zero, zero, zero, l2);
}

void DoWhileStmt()
{
   MutableInt l1, l2;
   l1 = new MutableInt (0);
   l2 = new MutableInt (0);

   // current token is DO (haeseong)
   token.getToken();  // skip 'haeseong'

   l1 = newlab();
   atom ("LBL", zero, zero, zero, zero, l1);  // label at top of loop body

   // loop body: haeseong { ... } dangsi (J >= 20);
   if (token.get_class()==Tok.LBrace)
       CompoundStmt();
   else
       Stmt();

   // 'dangsi' (while)
   if (token.get_class()==Tok.WHILE) token.getToken();
   else err ("expected 'dangsi' after haeseong body");

   if (token.get_class()==Tok.LPar) token.getToken();
   else err ("expected '(' in do-while");

   BoolExpr (l2);

   if (token.get_class()==Tok.RPar) token.getToken();
   else err ("expected ')' in do-while");

   if (token.get_class()==Tok.SEMI) token.getToken();
   else err ("expected ';' after do-while");

   atom ("JMP", zero, zero, zero, zero, l1);  // go back to start of body
   atom ("LBL", zero, zero, zero, zero, l2);  // exit label
}

void IfStmt()
   {
   MutableInt l1, l2;
   l1 = new MutableInt (0);
   l2 = new MutableInt (0);
   token.getToken();
   if (token.get_class()==Tok.LPar) token.getToken();
    else err ("expected left parenthesis in if statement");
   BoolExpr (l1);
   if (token.get_class()==Tok.RPar) token.getToken();
    else err ("expected right parenthesis in if statement");
   Stmt();
   l2 = newlab();
   atom ("JMP", zero, zero, zero, zero, l2);
   atom ("LBL", zero, zero, zero, zero, l1);
   ElsePart();
   atom ("LBL", zero, zero, zero, zero, l2);
   }

void ElsePart()
{
   if (token.get_class()==Tok.ELSE)
    { token.getToken();
      Stmt();
    }
}

void CompoundStmt()
{
   token.getToken();        // get next input after '{'
   StmtList();
   token.getToken();        // get next input after '}'
}

void StmtList()
{
   while (token.get_class()!=Tok.RBrace) 
   {
       Stmt();
    }
}

void AssignStmt ()
{  MutableInt p;
   p = new MutableInt (0);
   AssignExpr (p);
   if (token.get_class()==Tok.SEMI)
    token.getToken();
   else err ("expected ';' after assignment statement");
 }

void BoolExpr (MutableInt lbl)
{  MutableInt p,q;
   p =  new MutableInt(0);
   q =  new MutableInt(0);
   lbl.set (newlab());
   int c;
   Expr(p);

   c = (new Integer (token.getValue())).intValue(); // comparison code
   if (token.get_class()==Tok.COMPARE) 
    token.getToken();
    else err ("expected comparison operator");
   Expr (q);
   atom ("TST", p, q, zero, new MutableInt (7-c), lbl);
    // 7-c is the boolean complement of the comparison operator
}


void Expr(MutableInt p)
{
   MutableInt q;
 //  q = new MutableInt(0);
   Tok next;
   next = new Tok (lexer);
   if (token.get_class()==Tok.IDENT)
    { 
      next.peek();
      if (next.get_class()==Tok.ASSIGN)     // assignment expr LL(2)
        AssignExpr (p);
      else 
         {  
        Rvalue(p);
 //       Term (q);
  //      Elist (q,p);
         }
         }
    else if (token.get_class()==Tok.LPar || token.get_class()==Tok.NUM || 
        token.get_class()==Tok.PLUS || token.get_class()==Tok.MINUS)
    Rvalue(p);
    else err ("syntax error in expression");
}

void AssignExpr (MutableInt p)
{  MutableInt q, ref;
   q = new MutableInt (0);
   if (token.get_class() != Tok.IDENT) 
    err ("expected identifier");
   ref = (MutableInt) symbols.get (token.getValue());
      if (ref==null) err ("error: " + token.getValue() + " is undeclared");
   p.set (ref);
   token.getToken();            // read past identifier
   if (token.get_class() == Tok.ASSIGN) 
    token.getToken();       // read past =
    else err ("expected '='");
   Expr (q);
   atom ("MOV", q, zero, p, zero, zero);
}

void Rvalue (MutableInt p)
{  MutableInt q;
   q = new MutableInt (0);
   if (token.get_class()==Tok.LPar || token.get_class()==Tok.IDENT || 
    token.get_class()==Tok.NUM
    || token.get_class()==Tok.PLUS || token.get_class()==Tok.MINUS)
    { Term (q);
      Elist (q,p);
    }
   else err ("expected (, +, -, identifier, or number in expression");
}

void Elist (MutableInt p, MutableInt q)
   {
   MutableInt r,s;
   r = new MutableInt (0);
   s = new MutableInt (0);
   if (token.get_class()==Tok.PLUS) 
    { token.getToken();
      Term (r);
      s = alloc();
      atom ("ADD",p,r,s,zero, zero);
      Elist (s,q);
    }
   else if (token.get_class()==Tok.MINUS)
    { token.getToken();
      Term (r);
      s = alloc();
      atom ("SUB",p,r,s,zero, zero);
      Elist (s,q);
    }
   else if (token.get_class()==Tok.RPar || token.get_class()==Tok.SEMI ||
        token.get_class()==Tok.ASSIGN || token.get_class()==Tok.COMPARE) 
            q.set (p.get());    // null
   else err ("expected  ), ;, =, or comparison in expression");
   }

void Term (MutableInt p)
   {
   MutableInt q;
   q = new MutableInt (0);
   if (token.get_class()==Tok.IDENT || token.get_class()==Tok.NUM || 
    token.get_class()==Tok.LPar
    || token.get_class()==Tok.PLUS || token.get_class()==Tok.MINUS)
    { Factor (q);
      Tlist (q,p);
    }
   else err ("expected identifier, number, or ( in expression");
   }

void Tlist (MutableInt p, MutableInt q)
{
   MutableInt r,s;
   r = new MutableInt (0);
   s = new MutableInt (0);
   if (token.get_class()==Tok.MUL) 
    { token.getToken();
      Factor (r);
      s = alloc();
      atom ("MUL",p,r,s,zero,zero);
      Tlist (s,q);
    }
   else if (token.get_class()==Tok.DIV)
    { token.getToken();
      Factor (r);
      s = alloc();
      atom ("DIV",p,r,s,zero,zero);
      Tlist (s,q);
    }
   else if (token.get_class()==Tok.RPar || token.get_class()==Tok.SEMI ||
        token.get_class()==Tok.ASSIGN || token.get_class()==Tok.PLUS ||
        token.get_class()==Tok.MINUS || token.get_class()==Tok.COMPARE) 
            q.set (p.get());    // null
   else err ("expected *, /, ), +, -, ;, =, or comparison in expression");
}

void Factor (MutableInt p)
   {
   MutableInt q, ref;
   ref = new MutableInt (0);
   q = new MutableInt (0);
   double val;
   if (token.get_class()==Tok.LPar)
    { token.getToken();
      Expr (p);
      if (token.get_class()==Tok.RPar) token.getToken();
         else err ("expected ) in expression");
    }
   else if (token.get_class()==Tok.PLUS) 
    { token.getToken();
      Factor (p);
    }
   else if (token.get_class()==Tok.MINUS)
    { token.getToken();
      Factor (q);
      ref = alloc();
      atom ("NEG",q,zero,ref,zero,zero);
      p.set (ref.get());
    }
   else if (token.get_class()==Tok.IDENT)
    { ref = (MutableInt) symbols.get (token.getValue());
      if (ref==null) err ("error: " + token.getValue() + "is undeclared");
      p.set (ref.get());
      token.getToken();
    }
   else if (token.get_class()==Tok.NUM)  
    { ref = (MutableInt) numbers.get (token.getValue());
      if (ref==null)
        {    ref = alloc();
         numbers.put (token.getValue(), ref);
        }
      val = (new Double (token.getValue())).doubleValue();
      memory[ref.get()] = val;
      p.set (ref.get());
      token.getToken();         // read past the num
    }
   else err ("expected (, identifier, +, - or number in expression");
   }



}