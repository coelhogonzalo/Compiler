//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
package Parser;
import java.io.IOException;
import AnalizadorLexico.Analizador_Lexico;
import AnalizadorLexico.Token;
import java.util.ArrayList;
import GeneracionCodigoIntermedio.PolacaInversa;
import java.util.Arrays;
import java.util.HashMap;
//#line 26 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short CADENA=258;
public final static short USLINTEGER=259;
public final static short SINGLE=260;
public final static short IF=261;
public final static short ELSE=262;
public final static short ENDIF=263;
public final static short WHILE=264;
public final static short READONLY=265;
public final static short WRITE=266;
public final static short PASS=267;
public final static short MAYORIGUAL=268;
public final static short MENORIGUAL=269;
public final static short IGUALIGUAL=270;
public final static short DISTINTO=271;
public final static short ASIGN=272;
public final static short RETURN=273;
public final static short PRINT=274;
public final static short SINGLEPR=275;
public final static short USLINTEGERPR=276;
public final static short INVALIDO=277;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    4,    4,    4,    4,    7,
    7,    7,    8,    8,    8,    9,   10,   10,   12,   12,
   13,   13,   13,   11,   11,    6,    6,   15,   15,   16,
   16,   14,   14,   14,   18,   18,   18,   18,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,   20,
   21,   22,   24,   23,   23,   23,   23,   25,   25,   26,
   26,   26,   26,   26,   26,   19,   19,   17,   17,   17,
   27,   27,   27,   28,   28,   28,   28,   28,   29,   29,
   29,    5,    5,   30,   30,   30,   30,   30,   30,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    1,    3,    3,    2,    2,    2,
    1,    1,    4,    3,    3,    4,    2,    0,    1,    1,
    3,    2,    3,    2,    1,    3,    1,    3,    1,    2,
    1,    3,    2,    2,    3,    3,    2,    2,    3,    2,
    3,    5,    2,    2,    4,    4,    3,    2,    1,    2,
    1,    2,    1,    3,    2,    2,    2,    3,    2,    1,
    1,    1,    1,    1,    1,    3,    2,    3,    3,    1,
    3,    3,    1,    1,    1,    1,    2,    3,    5,    4,
    4,    1,    1,    1,    1,    1,    3,    0,    2,
};
final static short yydefred[] = {                         0,
    0,    0,   53,    0,   83,   82,    0,    0,    3,    4,
    5,    0,    0,    0,    0,    0,    0,    0,    0,   75,
   76,    0,    0,    0,    0,   73,   63,   62,   65,    0,
   60,   61,   64,    0,   50,    0,    0,    0,    0,    0,
    2,    0,    0,    8,    0,    0,   18,    0,    0,    9,
   40,    0,   51,   44,    0,   29,    0,    0,   43,   52,
    0,    0,    0,   77,    0,    0,    0,    0,   55,    0,
    0,   57,    0,   38,    0,    0,   39,    6,   26,    0,
    0,    0,    7,   31,    0,   41,    0,   47,    0,    0,
   78,    0,    0,   71,   72,   54,    0,   36,   35,    0,
    0,   19,    0,    0,    0,    0,   17,   20,   15,   28,
   30,   46,    0,   45,   84,    0,   86,    0,    0,   13,
    0,   24,    0,   22,    0,   16,   42,   89,    0,    0,
   81,   34,    0,   21,   23,   87,   79,   32,
};
final static short yydgoto[] = {                          7,
    8,    9,   56,   11,   12,   13,   14,   49,   50,   81,
  106,  107,  108,  122,   57,   85,   34,   40,   15,   16,
   58,   17,   35,   18,   36,   37,   25,   26,   62,  119,
};
final static short yysindex[] = {                      -204,
  -39,  163,    0,  -32,    0,    0,    0, -204,    0,    0,
    0, -239,  -29,  -28,  -16, -106,  182,  163,   -2,    0,
    0,  137, -228,   53,   -6,    0,    0,    0,    0,  380,
    0,    0,    0,  -41,    0,   10,  137,   24, -176,   14,
    0,    0,  -20,    0, -156, -170,    0, -154,  -36,    0,
    0,  -39,    0,    0,  -62,    0, -143,  -86,    0,    0,
 -146,   72,   53,    0,  137,  137,  137,  137,    0,   80,
  137,    0,   53,    0,   82,   91,    0,    0,    0, -121,
  194,   96,    0,    0, -112,    0,  -74,    0, -125,  -50,
    0,   -6,   -6,    0,    0,    0,   53,    0,    0,   98,
  100,    0, -239,   -9,  -40,   18,    0,    0,    0,    0,
    0,    0, -119,    0,    0,  -48,    0, -187,  101,    0,
  109,    0,   -4,    0,  -36,    0,    0,    0, -120,  119,
    0,    0,   11,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
  -37,    0,    0,    0,    0,    0,    0,  167,    0,    0,
    0,  -24,    0,    0,  125,    0,    0,    0,    1,    0,
    0,    0,    0,   89,   23,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  153,
    0,  -30,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   97,    0,    0,    0,    0,    0,    0,  -98,
    0,    0,   -8,    0,    0,  117,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  128,
    0,   47,   69,    0,    0,    0,  145,    0,    0,   50,
   49,    0,  -35,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   32,    0,   33,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  135,
    0,    0,   55,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  173,  395,    0,   13,   19,  103,   86,  -23,    0,
    0,    0,    0,    0,   17,    0,  411,    0,    0,    0,
  136,    0,  178,    0,  155,  164,   62,   67,    0,   79,
};
final static int YYTABLESIZE=651;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         46,
   74,   65,   12,   66,   11,   23,   27,   39,  118,   10,
  129,   46,  110,   27,   44,   11,   55,   42,   31,   33,
   32,   27,   70,   78,   56,   83,   48,   51,   27,   45,
   43,   64,   59,   59,  124,   67,   55,   61,   45,  134,
   68,   74,   74,   74,   74,   74,   68,   74,   55,   45,
   72,  138,    1,   65,   45,   66,    2,   77,   80,    3,
   74,   74,   74,   70,   74,   70,   70,   70,   69,    4,
    5,    6,   85,   88,   89,   85,   88,  115,  116,  117,
   75,   76,   70,   70,   70,   12,   47,   68,   67,   68,
   68,   68,   10,  103,   47,   65,   66,   66,   11,  104,
   79,  135,   82,  113,    5,    6,   68,   68,   68,   69,
   90,   69,   69,   69,   59,   91,   37,   48,   53,   86,
   96,  123,   98,   74,   49,   74,   92,   93,   69,   69,
   69,   99,   67,   94,   95,  100,  109,  114,  120,  121,
   66,  131,  126,  127,   52,   70,  136,   70,    2,  132,
   52,    3,   48,   23,    2,   53,   54,    3,   56,  137,
   37,    4,   56,   56,   56,   56,    1,    4,   88,   68,
   52,   68,   14,   25,    2,   56,   88,    3,   80,   33,
   41,   23,   52,  105,   70,   58,    2,    4,  112,    3,
  125,   69,   87,   69,   52,   60,  130,   71,    2,    4,
    0,    3,   30,    0,    0,    0,    0,   23,    0,    0,
    0,    4,    0,   67,  115,  116,  117,   19,  128,   20,
   21,   66,   31,   33,   32,   38,   27,   28,    0,   29,
    0,    0,   22,    0,    5,    6,    0,   12,   12,   11,
   11,   37,    0,    0,   10,   10,    5,    6,   59,   49,
   11,   11,   59,   59,   59,   59,    0,   74,    0,    0,
    0,   74,   74,   74,   74,   59,    0,   58,   74,   74,
    0,   74,    0,   74,   74,   74,   74,   48,    0,   70,
    0,    0,    0,   70,   70,   70,   70,    0,    0,    0,
   70,   70,    0,   70,    0,   70,   70,   70,   70,    0,
    0,    0,    0,   68,   55,    0,    0,   68,   68,   68,
   68,    0,    0,    0,   68,   68,    0,   68,    0,   68,
   68,   68,   68,    0,    0,   69,    0,    0,    0,   69,
   69,   69,   69,    0,    0,    0,   69,   69,    0,   69,
    0,   69,   69,   69,   69,   67,    0,    0,    0,   67,
   67,   67,   67,   66,    0,    0,    0,   66,   66,   66,
   66,   67,   67,   67,   67,   19,    0,   20,   21,   66,
   66,   66,   66,   37,    0,    0,    0,   37,   37,   37,
   37,   49,    0,    0,    0,   49,   49,   49,   49,   37,
   37,   37,   37,   19,   10,   20,   21,   49,   49,   49,
   49,   58,   10,    0,    0,   58,   58,   58,   58,   48,
    0,   24,    0,   48,   48,   48,   48,    0,   58,   19,
   69,   20,   21,    0,   23,   48,   48,   48,   48,    0,
   27,   28,   63,   29,    0,    0,    0,    0,   52,   31,
   33,   32,    2,    0,    0,    3,    0,   73,    0,   84,
    1,    0,    0,    0,    2,    4,    0,    3,    0,    0,
    0,    0,   24,    0,    0,    0,  101,    4,    5,    6,
    0,    0,    0,    0,    0,  102,    0,    0,    0,  111,
    0,   97,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  133,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   19,    0,   20,   21,
    0,    0,    0,    0,    0,    0,    0,   27,   28,    0,
   29,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   43,   40,   45,   40,   45,   44,   40,   59,   40,
   59,   40,  125,   44,   44,   40,  123,  257,   60,   61,
   62,   59,    0,   44,  123,   49,   14,   44,   59,   59,
   12,  260,   41,   17,   44,   42,  123,   40,   59,   44,
   47,   41,   42,   43,   44,   45,    0,   47,  123,   59,
   41,   41,  257,   43,   59,   45,  261,   44,   46,  264,
   60,   61,   62,   41,   41,   43,   44,   45,    0,  274,
  275,  276,   41,   41,   58,   44,   44,  265,  266,  267,
  257,  258,   60,   61,   62,  123,  123,   41,    0,   43,
   44,   45,  123,   81,  123,   43,    0,   45,  123,   81,
  257,  125,  257,   87,  275,  276,   60,   61,   62,   41,
  257,   43,   44,   45,  123,   44,    0,  105,  262,  263,
   41,  103,   41,  123,    0,  125,   65,   66,   60,   61,
   62,   41,   44,   67,   68,  257,   41,  263,   41,   40,
   44,   41,  125,  263,  257,  123,  267,  125,  261,   41,
  257,  264,    0,   45,  261,  262,  263,  264,  257,   41,
   44,  274,  261,  262,  263,  264,    0,  274,   41,  123,
  257,  125,  123,  125,  261,  274,  263,  264,   44,  125,
    8,   45,  257,   81,   30,   41,  261,  274,  263,  264,
  105,  123,   57,  125,  257,   18,  118,   34,  261,  274,
   -1,  264,   40,   -1,   -1,   -1,   -1,   45,   -1,   -1,
   -1,  274,   -1,  125,  265,  266,  267,  257,  267,  259,
  260,  125,   60,   61,   62,  258,  268,  269,   -1,  271,
   -1,   -1,  272,   -1,  275,  276,   -1,  275,  276,  275,
  276,  125,   -1,   -1,  275,  276,  275,  276,  257,  125,
  275,  276,  261,  262,  263,  264,   -1,  257,   -1,   -1,
   -1,  261,  262,  263,  264,  274,   -1,  123,  268,  269,
   -1,  271,   -1,  273,  274,  275,  276,  125,   -1,  257,
   -1,   -1,   -1,  261,  262,  263,  264,   -1,   -1,   -1,
  268,  269,   -1,  271,   -1,  273,  274,  275,  276,   -1,
   -1,   -1,   -1,  257,  123,   -1,   -1,  261,  262,  263,
  264,   -1,   -1,   -1,  268,  269,   -1,  271,   -1,  273,
  274,  275,  276,   -1,   -1,  257,   -1,   -1,   -1,  261,
  262,  263,  264,   -1,   -1,   -1,  268,  269,   -1,  271,
   -1,  273,  274,  275,  276,  257,   -1,   -1,   -1,  261,
  262,  263,  264,  257,   -1,   -1,   -1,  261,  262,  263,
  264,  273,  274,  275,  276,  257,   -1,  259,  260,  273,
  274,  275,  276,  257,   -1,   -1,   -1,  261,  262,  263,
  264,  257,   -1,   -1,   -1,  261,  262,  263,  264,  273,
  274,  275,  276,  257,    0,  259,  260,  273,  274,  275,
  276,  257,    8,   -1,   -1,  261,  262,  263,  264,  257,
   -1,    1,   -1,  261,  262,  263,  264,   -1,  274,  257,
   41,  259,  260,   -1,   45,  273,  274,  275,  276,   -1,
  268,  269,   22,  271,   -1,   -1,   -1,   -1,  257,   60,
   61,   62,  261,   -1,   -1,  264,   -1,   37,   -1,   55,
  257,   -1,   -1,   -1,  261,  274,   -1,  264,   -1,   -1,
   -1,   -1,   52,   -1,   -1,   -1,  273,  274,  275,  276,
   -1,   -1,   -1,   -1,   -1,   81,   -1,   -1,   -1,   85,
   -1,   71,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  121,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,   -1,  259,  260,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  268,  269,   -1,
  271,
};
}
final static short YYFINAL=7;
final static short YYMAXTOKEN=277;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","CADENA","USLINTEGER","SINGLE","IF",
"ELSE","ENDIF","WHILE","READONLY","WRITE","PASS","MAYORIGUAL","MENORIGUAL",
"IGUALIGUAL","DISTINTO","ASIGN","RETURN","PRINT","SINGLEPR","USLINTEGERPR",
"INVALIDO",
};
final static String yyrule[] = {
"$accept : program",
"program : BS",
"BS : BS sentencia",
"BS : sentencia",
"sentencia : sentenciaCE",
"sentencia : sentenciaDEC",
"sentenciaDEC : tipo lista_variables ','",
"sentenciaDEC : tipoFunID parametrosDef cuerpofuncion",
"sentenciaDEC : lista_variables ','",
"sentenciaDEC : tipoFunID cuerpofuncion",
"tipoFunID : tipo ID",
"tipoFunID : tipo",
"tipoFunID : ID",
"parametrosDef : '(' tipo ID ')'",
"parametrosDef : '(' tipo ID",
"parametrosDef : tipo ID ')'",
"cuerpofuncion : '{' BSFuncion retorno '}'",
"BSFuncion : BSFuncion sentenciaFuncion",
"BSFuncion :",
"sentenciaFuncion : sentenciaCE",
"sentenciaFuncion : sentenciaDECFuncion",
"sentenciaDECFuncion : tipo lista_variables ','",
"sentenciaDECFuncion : lista_variables ','",
"sentenciaDECFuncion : tipoFunID parametrosDef cuerpofuncion",
"retorno : RETURN expresioncparentesis",
"retorno : RETURN",
"lista_variables : lista_variables ';' ID",
"lista_variables : ID",
"BCE : '{' BCE2 '}'",
"BCE : sentenciaCE",
"BCE2 : BCE2 sentenciaCE",
"BCE2 : sentenciaCE",
"expresioncparentesis : '(' expresion ')'",
"expresioncparentesis : '(' expresion",
"expresioncparentesis : '(' ')'",
"printeable : '(' CADENA ')'",
"printeable : '(' ID ')'",
"printeable : '(' CADENA",
"printeable : CADENA ')'",
"sentenciaCE : PRINT printeable ','",
"sentenciaCE : asignacion ','",
"sentenciaCE : ifcond BCE ENDIF",
"sentenciaCE : ifcond BCE elsecond BCE ENDIF",
"sentenciaCE : whilecond BCE",
"sentenciaCE : ifcond ENDIF",
"sentenciaCE : ifcond elsecond BCE ENDIF",
"sentenciaCE : ifcond BCE elsecond ENDIF",
"sentenciaCE : ifcond elsecond ENDIF",
"sentenciaCE : PRINT printeable",
"sentenciaCE : asignacion",
"ifcond : IF condicioncparentesis",
"elsecond : ELSE",
"whilecond : whileparaapilar condicioncparentesis",
"whileparaapilar : WHILE",
"condicioncparentesis : '(' condicion ')'",
"condicioncparentesis : '(' ')'",
"condicioncparentesis : '(' condicion",
"condicioncparentesis : condicion ')'",
"condicion : expresion operador_logico expresion",
"condicion : operador_logico expresion",
"operador_logico : '<'",
"operador_logico : '>'",
"operador_logico : MENORIGUAL",
"operador_logico : MAYORIGUAL",
"operador_logico : '='",
"operador_logico : DISTINTO",
"asignacion : ID ASIGN expresion",
"asignacion : ID expresion",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : USLINTEGER",
"factor : SINGLE",
"factor : '-' SINGLE",
"factor : ID parametros ','",
"parametros : '(' ID ';' lista_permisos ')'",
"parametros : '(' ID ';' lista_permisos",
"parametros : '(' ID lista_permisos ')'",
"tipo : USLINTEGERPR",
"tipo : SINGLEPR",
"lista_permisos : READONLY",
"lista_permisos : WRITE",
"lista_permisos : PASS",
"lista_permisos : WRITE ';' PASS",
"lista_permisos :",
"lista_permisos : WRITE PASS",
};

//#line 349 "gramatica.y"

private static final int Rd = 0;
private static final int Wr = 1;
private static final int Ps = 2;
private static final int Wrps = 3;


public PolacaInversa PI = new PolacaInversa();
public Analizador_Lexico al;
public ArrayList<AnalizadorLexico.Error> errores;
public static ArrayList<String> estructuras;
public String idFun;
public boolean estoyEnFuncion = false;
public String idParam;
public String ambitoActual="@main";
public String ultimaFuncion;
private HashMap<String,String> parametrosFunciones=new HashMap<>();

public int yylex(){
	Token t=null;
	try {
		t = this.al.getToken();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	if(t!=null){
		yylval = new ParserVal(t.lexema);
		return t.nro;
	}
	return 0;
}

public void yyerror ( String e){
	System.out.println(e);
}
public int parsepublico(){
	return this.yyparse();
}
public void registrarTipo(String listaVariables,String tipo){
	ArrayList<String> items= new ArrayList<String>(Arrays.asList(listaVariables.split(" ")));
	for (String item:items){
		//System.out.println(items.size()+" El identificador es :'"+item+"'");
		Token t=Analizador_Lexico.tablaSimbolos.get(item);
		t.tipo=tipo;
	}
	/*while(pos<listaVariables.length()){
		char nuevoChar=listaVariables.charAt(pos);
		if(nuevoChar!=' '){
			idVariable=idVariable+nuevoChar;
			System.out.println(idVariable);
		}
		else{
			Token t=Analizador_Lexico.tablaSimbolos.get(idVariable);
			t.tipo=tipo;
			idVariable="";
		}
	}*/
}

public static boolean isPermited(int permisoFuncion, int permisoInvocacion){
    	if(permisoFuncion==permisoInvocacion)
    		return true;
    	if(permisoFuncion==Parser.Rd)
    		return true;
    	if(permisoFuncion==Parser.Wr&&permisoInvocacion==Parser.Wrps)
    		return true;
    	if(permisoFuncion==Parser.Ps&&permisoInvocacion==Parser.Wrps)
    		return true;
    	return false;
    }

    public static void main(String [] args) {
    	System.out.println();
    	System.out.println("Testing con readonly en la funcion:");
    	System.out.println();
    	if(isPermited(Parser.Rd,Parser.Rd))
    		System.out.println("Recibi un readonly y la funcion tenia un readonly, lo acepte");
    	if(isPermited(Parser.Rd,Parser.Ps))
    		System.out.println("Recibi un pass y la funcion tenia readonly, lo acepte");
    	if(isPermited(Parser.Rd,Parser.Wr))
    		System.out.println("Recibi un write y la funcion tenia readonly, lo acepte");
    	if(isPermited(Parser.Rd,Parser.Wrps))
    		System.out.println("Recibi un write;pass y la funcion tenia readonly, lo acepte");
    	System.out.println();
    	System.out.println("Testing con pass en la funcion:");
    	System.out.println();
    	if(isPermited(Parser.Ps,Parser.Ps))
    		System.out.println("Recibi un pass y la funcion tenia un pass, lo acepte");
    	if(!isPermited(Parser.Ps,Parser.Rd))
    		System.out.println("Recibi un readonly y la funcion tenia pass, RECHAZADO");
    	if(!isPermited(Parser.Ps,Parser.Wr))
    		System.out.println("Recibi un write y la funcion tenia pass, RECHAZADO");
    	if(isPermited(Parser.Ps,Parser.Wrps))
    		System.out.println("Recibi un write;pass y la funcion tenia pass, lo acepte");
    	System.out.println();
    	System.out.println("Testing con write en la funcion:");
    	System.out.println();
    	if(isPermited(Parser.Wr,Parser.Wr))
    		System.out.println("Recibi un write y la funcion tenia write, lo acepte");
    	if(!isPermited(Parser.Wr,Parser.Ps))
    		System.out.println("Recibi un pass y la funcion tenia un write, RECHAZADO");
    	if(!isPermited(Parser.Wr,Parser.Rd))
    		System.out.println("Recibi un readonly y la funcion tenia write, RECHAZADO");
    	if(isPermited(Parser.Wr,Parser.Wrps))
    		System.out.println("Recibi un write;pass y la funcion tenia write, lo acepte");
    	System.out.println();
    	System.out.println("Testing con write;pass en la funcion:");
    	System.out.println();
    	if(!isPermited(Parser.Wrps,Parser.Wr))
    		System.out.println("Recibi un write y la funcion tenia write;pass, RECHAZADO");
    	if(!isPermited(Parser.Wrps,Parser.Ps))
    		System.out.println("Recibi un pass y la funcion tenia un write;pass, RECHAZADO");
    	if(!isPermited(Parser.Wrps,Parser.Rd))
    		System.out.println("Recibi un readonly y la funcion tenia write;pass, RECHAZADO");
    	if(isPermited(Parser.Wrps,Parser.Wrps))
    		System.out.println("Recibi un write;pass y la funcion tenia write;pass, lo acepte");
    	System.out.println();
    	System.out.println("Testing con noseusaelparametro en la funcion:");
    	System.out.println();
    }
//#line 581 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 6:
//#line 33 "gramatica.y"
{ registrarTipo( val_peek(1).sval, val_peek(2).sval); /*System.out.println($2.sval);*/
Parser.estructuras.add("Se detecto la declaracion de variables en la linea "+Analizador_Lexico.cantLN+"\r\n");}
break;
case 7:
//#line 35 "gramatica.y"
{ parametrosFunciones.put(val_peek(2).sval,val_peek(1).sval);
	Parser.estructuras.add("Se detecto la declaracion de una funcion en la linea "+Analizador_Lexico.cantLN+"\r\n");}
break;
case 8:
//#line 38 "gramatica.y"
{this.errores.add(new ErrorG("Error 001: Falta definir el tipo de las variables", Analizador_Lexico.cantLN));}
break;
case 9:
//#line 39 "gramatica.y"
{this.errores.add(new ErrorG("Error 004: Falta definir los parametros de la funcion", Analizador_Lexico.cantLN));}
break;
case 10:
//#line 43 "gramatica.y"
{ estoyEnFuncion = true; yyval=val_peek(0);/*Le paso el nombre de la funcion para arriba*/
                idFun = val_peek(0).sval;
 Token t=Analizador_Lexico.tablaSimbolos.get(val_peek(0).sval); 
	if(t.declarada){
		this.errores.add(new ErrorG("Error SIN NUMERO: El identificador '"+t.lexema+"' ,de uso '"+t.uso+"' ya esta declarado", Analizador_Lexico.cantLN));
		PI.inicioFuncion(val_peek(0).sval);
	}
	else{
		t.declarada=true;
		PI.inicioFuncion(val_peek(0).sval);
		t.uso="funcion";
		t.tipo=val_peek(1).sval;
		t.ambito=ambitoActual;
	    }
	this.ambitoActual=this.ambitoActual+"@"+val_peek(0).sval;	
	}
break;
case 11:
//#line 59 "gramatica.y"
{PI.inicioFuncion("NOMBRE_FALTANTE"); this.ambitoActual=this.ambitoActual+"@"+"NOMBRE_FALTANTE";
	this.errores.add(new ErrorG("Error 002: Falta definir el nombre de la funcion", Analizador_Lexico.cantLN));}
break;
case 12:
//#line 61 "gramatica.y"
{PI.inicioFuncion(val_peek(0).sval); this.ambitoActual=this.ambitoActual+"@"+val_peek(0).sval;
	this.errores.add(new ErrorG("Error 003: Falta definir el tipo de la funcion", Analizador_Lexico.cantLN));}
break;
case 13:
//#line 65 "gramatica.y"
{ yyval=val_peek(2);/*Le paso el tipo del parametro para arriba*/
                idParam = val_peek(1).sval;
PI.paramFun(val_peek(1).sval); 
Token t=Analizador_Lexico.tablaSimbolos.get(val_peek(1).sval);
	if(t!=null){
		if(!t.declarada){
			t.uso="parametro";
			t.declarada=true;
			t.ambito=this.ambitoActual;
			t.tipo=val_peek(2).sval;
		}
		else
			this.errores.add(new ErrorG("Error 009 : El identificador '"+t.lexema+"' de tipo '"+t.uso+"' no puede ser redeclarado", Analizador_Lexico.cantLN));
	}
	else
		System.out.println("El token que quisiste recuperar es null (ndmpp)"); }
break;
case 14:
//#line 81 "gramatica.y"
{idParam = val_peek(0).sval;PI.paramFun(val_peek(0).sval);
	this.errores.add(new ErrorG("Error 005: Falta un ) despues del identificador", Analizador_Lexico.cantLN));}
break;
case 15:
//#line 83 "gramatica.y"
{idParam = val_peek(1).sval;PI.paramFun(val_peek(1).sval);
	this.errores.add(new ErrorG("Error 006 : Falta un ( antes del tipo del parametro", Analizador_Lexico.cantLN));}
break;
case 16:
//#line 87 "gramatica.y"
{

    this.ambitoActual=Analizador_Lexico.cortarAmbito(this.ambitoActual);}
break;
case 21:
//#line 98 "gramatica.y"
{ registrarTipo( val_peek(1).sval, val_peek(2).sval); 
Parser.estructuras.add("Se detecto la declaracion de variables en la linea "+Analizador_Lexico.cantLN+"\r\n");}
break;
case 22:
//#line 101 "gramatica.y"
{this.errores.add(new ErrorG("Error 001: Falta definir el tipo de las variables", Analizador_Lexico.cantLN));}
break;
case 23:
//#line 102 "gramatica.y"
{ this.errores.add(new ErrorG("Error 007: Se declaro una funcion dentro de otra funcion", Analizador_Lexico.cantLN));}
break;
case 24:
//#line 105 "gramatica.y"
{ estoyEnFuncion = false; PI.put("return");
                idFun = "None";
 }
break;
case 25:
//#line 108 "gramatica.y"
{this.errores.add(new ErrorG("Error 008: La funcion debe retornar un valor", Analizador_Lexico.cantLN));}
break;
case 26:
//#line 112 "gramatica.y"
{Token t=Analizador_Lexico.tablaSimbolos.get(val_peek(0).sval);
	if(t!=null){
		if(t.declarada==false&&t.uso!="parametro"){
			t.declarada=true;
			t.uso="variable";
			t.ambito=this.ambitoActual;
		}
		else
			this.errores.add(new ErrorG("Error SIN NUMERO: Se redeclaro el identificador de uso "+t.uso+" :'"+t.lexema+"' ", Analizador_Lexico.cantLN));
		yyval.sval = yyval.sval+" "+ val_peek(0).sval;
	}
	else
		System.out.println("El token que quisiste recuperar es null");}
break;
case 27:
//#line 125 "gramatica.y"
{	Token t=Analizador_Lexico.tablaSimbolos.get(val_peek(0).sval);
	if(t!=null){
		if(t.declarada==false&&t.uso!="parametro"){
			t.declarada=true;
			t.uso="variable";
			t.ambito=this.ambitoActual;
		}
		else
			this.errores.add(new ErrorG("Error SIN NUMERO: Se redeclaro el identificador de tipo "+t.uso+" :'"+t.lexema+"' ", Analizador_Lexico.cantLN));
		yyval.sval = yyval.sval+" "+ val_peek(0).sval;
	}
	else
		System.out.println("El token que quisiste recuperar es null");
	}
break;
case 33:
//#line 151 "gramatica.y"
{this.errores.add(new ErrorG("Error 010: Se esperaba un )", Analizador_Lexico.cantLN));}
break;
case 34:
//#line 152 "gramatica.y"
{this.errores.add(new ErrorG("Error 011: Se esperaba una expresion", Analizador_Lexico.cantLN));}
break;
case 35:
//#line 155 "gramatica.y"
{ PI.put(val_peek(1).sval); }
break;
case 36:
//#line 156 "gramatica.y"
{
		PI.put(val_peek(1).sval); }
break;
case 37:
//#line 158 "gramatica.y"
{this.errores.add(new ErrorG("Error 012 : Falta un )", Analizador_Lexico.cantLN));}
break;
case 38:
//#line 159 "gramatica.y"
{this.errores.add(new ErrorG("Error 013 : Falta un (", Analizador_Lexico.cantLN));}
break;
case 39:
//#line 162 "gramatica.y"
{ Parser.estructuras.add("Se detecto un print en la linea "+Analizador_Lexico.cantLN+"\r\n"); PI.put("print");}
break;
case 40:
//#line 163 "gramatica.y"
{ Parser.estructuras.add("Se detecto una asignacion en  la linea "+Analizador_Lexico.cantLN+"\r\n");}
break;
case 41:
//#line 164 "gramatica.y"
{ Parser.estructuras.add("Se detecto un if en la linea "+Analizador_Lexico.cantLN+"\r\n"); PI.desapilar(); }
break;
case 42:
//#line 165 "gramatica.y"
{ Parser.estructuras.add("Se detecto un if en la linea "+Analizador_Lexico.cantLN+"\r\n"); PI.desapilar();}
break;
case 43:
//#line 166 "gramatica.y"
{ Parser.estructuras.add("Se detecto un while en la linea "+Analizador_Lexico.cantLN+"\r\n"); PI.saltoIncond(); PI.desapilar(); }
break;
case 44:
//#line 170 "gramatica.y"
{this.errores.add(new ErrorG("Error 015: Se esperaba un bloque de sentencias en la rama del if", Analizador_Lexico.cantLN));}
break;
case 45:
//#line 171 "gramatica.y"
{this.errores.add(new ErrorG("Error 015: Se esperaba un bloque de sentencias en la rama del if", Analizador_Lexico.cantLN));}
break;
case 46:
//#line 172 "gramatica.y"
{this.errores.add(new ErrorG("Error 016: Se esperaba un bloque de sentencias en la rama del else", Analizador_Lexico.cantLN));}
break;
case 47:
//#line 173 "gramatica.y"
{this.errores.add(new ErrorG("Error 017: Se esperaba un bloque de sentencias en la rama del if y del else", Analizador_Lexico.cantLN));}
break;
case 48:
//#line 175 "gramatica.y"
{this.errores.add(new ErrorG("Error 014: Se esperaba un ,", Analizador_Lexico.cantLN));}
break;
case 49:
//#line 176 "gramatica.y"
{this.errores.add(new ErrorG("Error 014: Se esperaba un ,", Analizador_Lexico.cantLN));}
break;
case 50:
//#line 179 "gramatica.y"
{ PI.bifurcacion(); }
break;
case 51:
//#line 183 "gramatica.y"
{ PI.desapilarElse(); PI.bifurcacionElse(); }
break;
case 52:
//#line 187 "gramatica.y"
{ PI.bifurcacion(); }
break;
case 53:
//#line 191 "gramatica.y"
{ PI.setSaltoIncond(); }
break;
case 55:
//#line 198 "gramatica.y"
{this.errores.add(new ErrorG("Error 018: No se definio una condicion", Analizador_Lexico.cantLN));}
break;
case 56:
//#line 199 "gramatica.y"
{this.errores.add(new ErrorG("Error 019: Se esperaba un ) despues de la condicion", Analizador_Lexico.cantLN));}
break;
case 57:
//#line 200 "gramatica.y"
{this.errores.add(new ErrorG("Error 020: Se esperaba un ( antes de la condicion", Analizador_Lexico.cantLN));}
break;
case 58:
//#line 206 "gramatica.y"
{ PI.put(val_peek(1).sval); }
break;
case 59:
//#line 207 "gramatica.y"
{this.errores.add(new ErrorG("Error 021: Se esperaba un operador logico valido", Analizador_Lexico.cantLN));}
break;
case 60:
//#line 211 "gramatica.y"
{ yyval.sval = "<"; }
break;
case 61:
//#line 212 "gramatica.y"
{ yyval.sval = ">"; }
break;
case 62:
//#line 213 "gramatica.y"
{ yyval.sval = "<="; }
break;
case 63:
//#line 214 "gramatica.y"
{ yyval.sval = ">="; }
break;
case 64:
//#line 215 "gramatica.y"
{ yyval.sval = "="; }
break;
case 65:
//#line 216 "gramatica.y"
{ yyval.sval = "!="; }
break;
case 66:
//#line 220 "gramatica.y"
{
	System.out.println("El tipo de ID :'"+Analizador_Lexico.tablaSimbolos.get(val_peek(2).sval).tipo+"' y el tipo de la expresion:'"+val_peek(0).sval+"'");
	if(!val_peek(0).sval.equals(Analizador_Lexico.tablaSimbolos.get(val_peek(2).sval).tipo))
		this.errores.add(new ErrorG("Error 035 : El tipo de la variable "+val_peek(2).sval+" no coincide con el de la expresion", Analizador_Lexico.cantLN));
    if ( estoyEnFuncion ){
    if ( val_peek(2).sval.equals(idParam) )
        if ( Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun == Ps )
            Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun = Wrps;
        else
	   if ( isPermited(Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun, Wr) )
            Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun = Wr;
    }

	Token t=Analizador_Lexico.tablaSimbolos.get(val_peek(2).sval); PI.put(val_peek(2).sval); PI.put(":=");
	if(t!=null){/*Primero me fijo si esta declarada*/
		if(t.declarada==false)
			this.errores.add(new ErrorG("Error 033 : La variable "+val_peek(2).sval+" no esta declarada ", Analizador_Lexico.cantLN));
		else{
			t=Analizador_Lexico.getEntradaTS(val_peek(2).sval,this.ambitoActual);
			if(t==null)/*Despues me fijo si esta en el ambito*/
				this.errores.add(new ErrorG("Error 34 : El identificador "+val_peek(2).sval+" no esta en el ambito "+this.ambitoActual, Analizador_Lexico.cantLN));
		}
	}
	else
		System.out.println(val_peek(2).sval+" No esta en la tabla de simbolos ndmpp ");
}
break;
case 67:
//#line 246 "gramatica.y"
{this.errores.add(new ErrorG("Error 022: Falta el operador de asignacion", Analizador_Lexico.cantLN));}
break;
case 68:
//#line 249 "gramatica.y"
{ PI.put("+"); }
break;
case 69:
//#line 250 "gramatica.y"
{ PI.put("-"); }
break;
case 70:
//#line 251 "gramatica.y"
{yyval.sval=val_peek(0).sval;}
break;
case 71:
//#line 254 "gramatica.y"
{ PI.put("*"); }
break;
case 72:
//#line 255 "gramatica.y"
{ PI.put("/"); }
break;
case 73:
//#line 256 "gramatica.y"
{yyval.sval=val_peek(0).sval;}
break;
case 74:
//#line 259 "gramatica.y"
{ PI.put(val_peek(0).sval);  
	Token t=Analizador_Lexico.tablaSimbolos.get(val_peek(0).sval);
	if(t!=null){
		yyval.sval=t.tipo;
		if(t.declarada==false)/*Primero me fijo si esta declarada*/
			this.errores.add(new ErrorG("Error 024 : La variable "+val_peek(0).sval+" no esta declarada ", Analizador_Lexico.cantLN));
		else{
			t=Analizador_Lexico.getEntradaTS(val_peek(0).sval,this.ambitoActual);
			if(t==null)/*Despues me fijo si esta en el ambito*/
				this.errores.add(new ErrorG("Error 025 : El identificador "+val_peek(0).sval+" no esta en el ambito "+this.ambitoActual, Analizador_Lexico.cantLN));
		}
	}
	else
		System.out.println(" No esta en la tabla de simbolos ndmpp ");
	}
break;
case 75:
//#line 275 "gramatica.y"
{ PI.put(val_peek(0).sval); yyval.sval=Analizador_Lexico.tablaSimbolos.get(val_peek(0).sval).tipo; }
break;
case 76:
//#line 276 "gramatica.y"
{ PI.put(val_peek(0).sval); yyval.sval=Analizador_Lexico.tablaSimbolos.get(val_peek(0).sval).tipo; }
break;
case 77:
//#line 277 "gramatica.y"
{	Token t=Analizador_Lexico.tablaSimbolos.get(val_peek(0).sval); 
	/*Analizador_Lexico.tablaSimbolos.remove($2.sval); Lo saque porque puede borrar otra instancia positiva de un single*/
	t.lexema="-"+t.lexema; PI.put("-" + val_peek(0).sval);
	Analizador_Lexico.tablaSimbolos.put(t.lexema,t);	yyval.sval=t.tipo;}
break;
case 78:
//#line 281 "gramatica.y"
{ yyval.sval=Analizador_Lexico.tablaSimbolos.get(val_peek(2).sval).tipo;
                    if ( estoyEnFuncion ){
                        /*System.out.println(idParam + " contra " + $2.sval);*/
                        if ( idParam.equals(val_peek(1).sval) )
                            if ( Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun == Rd )
                                Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun = Ps;
                            else if ( Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun == Wr )
                                    Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun = Wrps;

                    }
                    /*System.out.println("\n idFun " + idFun + "\n" + "CantLN: " + Analizador_Lexico.cantLN);*/
                    /*System.out.println(Analizador_Lexico.tablaSimbolos.get($1.sval).permisoFun + " y pase " + $2.ival);*/
	                if ( !isPermited(Analizador_Lexico.tablaSimbolos.get(val_peek(2).sval).permisoFun, val_peek(1).ival) )
                        this.errores.add(new ErrorG("Error 026 : La funcion "+val_peek(2).sval+" no puede ser invocada con " + val_peek(1).ival, Analizador_Lexico.cantLN));
					String tipoParametro=Analizador_Lexico.tablaSimbolos.get(val_peek(1).sval).tipo;
					if(!tipoParametro.equals(parametrosFunciones.get(val_peek(2).sval)))
						this.errores.add(new ErrorG("Error 034 : La funcion "+val_peek(2).sval+" no puede ser invocada con un parametro de tipo '"+tipoParametro+"' ", Analizador_Lexico.cantLN));
					
	PI.jumpToFun(val_peek(2).sval); Token t=Analizador_Lexico.tablaSimbolos.get(val_peek(2).sval);
	if(t!=null){
		if(t.declarada==false)
			this.errores.add(new ErrorG("Error 027 : La funcion "+val_peek(2).sval+" no esta declarada ", Analizador_Lexico.cantLN));
		else
			if(t.uso!="funcion")
				this.errores.add(new ErrorG("Error 028 : El identificador "+t.lexema+" no es una funcion. ", Analizador_Lexico.cantLN));
	}
	else
		System.out.println("El identificador "+val_peek(2).sval+" no se agrego a la tabla de simbolos (El identificador es una funcion) (ndmpp)"); }
break;
case 79:
//#line 311 "gramatica.y"
{
                        yyval.ival = val_peek(1).ival; yyval.sval = val_peek(3).sval;

 PI.put(val_peek(3).sval);
	Token t=Analizador_Lexico.tablaSimbolos.get(val_peek(3).sval);
	if(t!=null){/*Primero me fijo si esta declarada*/
		if(t.declarada==false)
			this.errores.add(new ErrorG("Error 029: La variable "+val_peek(3).sval+" no esta declarada ", Analizador_Lexico.cantLN));
		else{
			t=Analizador_Lexico.getEntradaTS(val_peek(3).sval,this.ambitoActual);
			if(t==null)/*Despues me fijo si esta en el ambito*/
				this.errores.add(new ErrorG("Error 030 : El identificador "+val_peek(3).sval+" no esta en el ambito "+this.ambitoActual, Analizador_Lexico.cantLN));
		}
	}
	else
		System.out.println(" No esta en la tabla de simbolos ndmpp ");
	}
break;
case 80:
//#line 330 "gramatica.y"
{this.errores.add(new ErrorG("Error 031: Se esperaba un )", Analizador_Lexico.cantLN));}
break;
case 81:
//#line 331 "gramatica.y"
{this.errores.add(new ErrorG("Error 032: Se esperaba un ;", Analizador_Lexico.cantLN));}
break;
case 84:
//#line 338 "gramatica.y"
{ yyval.ival = Rd; }
break;
case 85:
//#line 339 "gramatica.y"
{ yyval.ival = Wr; }
break;
case 86:
//#line 340 "gramatica.y"
{ yyval.ival = Ps; }
break;
case 87:
//#line 341 "gramatica.y"
{ yyval.ival = Wrps; }
break;
case 89:
//#line 344 "gramatica.y"
{this.errores.add(new ErrorG("Error 033: Se esperaba un ; entre los permisos", Analizador_Lexico.cantLN));}
break;
//#line 1166 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
