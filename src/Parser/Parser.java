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






//#line 2 "gramaticaGrupo16.y"
package Parser;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import AnalizadorLexico.Analizador_Lexico;
import AnalizadorLexico.Token;
import AnalizadorLexico.FileManager;
import AnalizadorLexico.Main;
import AnalizadorLexico.TokenValue;
import java.util.ArrayList;
//#line 30 "Parser.java"




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
    0,    1,    1,    2,    2,    4,    4,    7,    7,    7,
    8,    8,    8,    9,    9,    9,    6,    6,   11,   11,
   11,   11,   12,   12,   10,   10,   10,   10,   14,   14,
   14,   14,    3,    3,    3,    3,    3,    3,    3,    3,
   16,   16,   16,   16,   17,   18,   18,   18,   18,   18,
   18,   15,   15,   13,   13,   13,   19,   19,   19,   20,
   20,   20,   21,   20,   22,   22,   22,   22,    5,    5,
   23,   23,   23,   23,   23,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    1,    3,    4,    4,    3,    3,
    4,    3,    3,    2,    1,    1,    3,    1,    3,    1,
    2,    2,    2,    1,    3,    2,    2,    2,    3,    3,
    2,    2,    3,    2,    4,    6,    4,    5,    5,    4,
    3,    2,    2,    2,    3,    1,    1,    1,    1,    1,
    1,    3,    2,    3,    3,    1,    3,    3,    1,    1,
    1,    1,    0,    6,    5,    4,    4,    4,    1,    1,
    1,    1,    1,    3,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   70,   69,    0,    0,    3,    4,
    5,    0,    0,    0,   60,   61,   62,    0,    0,    0,
    0,    0,    0,   59,    0,    0,    0,    0,    2,    0,
    0,   34,    0,   63,   42,    0,   49,   48,   50,   51,
    0,    0,   46,   47,    0,    0,    0,    0,    0,    0,
   44,    0,    0,    0,   32,    0,    0,   33,    0,    0,
    0,    6,    0,    0,   41,    0,    0,    0,   40,    0,
   24,    0,    0,   35,   21,   23,   57,   58,   37,   30,
   29,    0,    0,    0,    0,    7,   17,    0,   38,   19,
   39,    0,    0,   10,    0,    0,    0,    0,    0,   16,
    0,    0,    0,    0,    0,   36,    8,   14,   28,    0,
    0,   12,   27,   13,    0,    0,   64,   25,   11,   71,
    0,   73,    0,    0,    0,   75,    0,   66,    0,   68,
   74,   65,
};
final static short yydgoto[] = {                          7,
    8,    9,   10,   11,   12,   31,   61,   86,   99,  100,
   49,   50,  101,   28,   13,   21,   22,   45,   23,   24,
   64,  105,  123,
};
final static short yysindex[] = {                      -155,
 -248,  -28,  -28,  -36,    0,    0,    0, -155,    0,    0,
    0, -225,   -5,   50,    0,    0,    0, -180,    8,   34,
  -93,   52,    9,    0,   51,   56, -203,   59,    0,  -38,
  -24,    0,   55,    0,    0,   60,    0,    0,    0,    0,
   50,   50,    0,    0,   50,  -85, -189,    0, -192,   57,
    0,   50,   50,   64,    0,   63,   73,    0, -216, -142,
 -115,    0, -125, -124,    0,    9,    9,   55,    0, -129,
    0,   62,  -73,    0,    0,    0,    0,    0,    0,    0,
    0, -121,   96,  -40,  -40,    0,    0,  -39,    0,    0,
    0, -123,  100,    0, -248,  -12,   28,  -40,   18,    0,
   33,   19,   91, -106,  109,    0,    0,    0,    0,   47,
   29,    0,    0,    0, -136,  -56,    0,    0,    0,    0,
  -53,    0,  115, -136,  116,    0, -104,    0,  117,    0,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,  170,    0,    0,
    0,    0,    0,  129,    0,    0,    0,    0,    0,    0,
    0,    0,  -18,    0,    0,    0,    0,    0,    0,  -22,
    0,    0,  131,    0,    0,  -62,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   37,    0,    0,
    0,    0,    0,    0,    0,    0,  133,    0,    0,    0,
    0,    0,    0,    0,    0,    2,   22,  -34,    0,    0,
    0,   -8,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -109,    0,   81,   58,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   61,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -26,    0,    0,    0,    0,    0,    0,    0,  136,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  -50,    1,   66,    0,  -11,    0,    0,    0,  -69,   89,
  -15,  145,   38,    0,    0,  190,  175,    0,   69,   65,
    0,    0, -103,
};
final static int YYTABLESIZE=336;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         97,
  104,   59,  124,   27,   18,  127,   45,   84,   29,   54,
   85,   19,  125,    9,   72,  102,   18,   72,   60,   62,
  129,   18,   56,   14,   56,   56,   56,   97,  111,   47,
   70,   30,   18,   98,   63,   22,   18,   47,   32,   20,
   20,   56,   54,   56,   54,   54,   54,   82,   35,   47,
   52,   33,   18,   56,   57,   53,   20,   92,    5,    6,
   43,   54,   55,   54,   55,   55,   55,    1,  109,   73,
   74,    2,   18,  113,    3,   41,   41,   42,   42,   34,
   20,   55,   68,   55,    4,   29,   48,  118,   45,   41,
   48,   42,   51,   43,   18,   44,   55,   41,   29,   42,
   65,    1,   58,   80,   56,    2,   56,   79,    3,   66,
   67,   48,   71,   81,   83,   76,   77,   78,    4,    5,
    6,   60,   60,   60,   54,   60,   54,   60,  120,  121,
  122,   87,   88,   89,  110,   93,   94,   76,   48,  106,
  107,    1,  112,  114,   55,    2,   55,    9,    3,  115,
  116,    9,  117,  119,    9,  128,  130,  132,    4,    5,
    6,   24,  131,    1,    9,    9,    9,    2,   46,    1,
    3,    1,   53,   47,   52,    2,   31,   69,    3,   67,
    4,   75,   15,    1,  108,   26,   90,    2,    4,   91,
    3,   72,   25,   36,   43,    0,    0,    0,   43,   43,
    4,   43,    0,    0,    0,    0,    0,    0,  120,  121,
  122,   43,    0,  126,    0,    0,   95,  103,   16,   17,
    2,   26,   45,    3,    0,    0,   45,   45,   15,   45,
   16,   17,   96,    4,    5,    6,    5,    6,   56,   45,
    0,    0,   56,   56,   15,   56,   16,   17,    0,   56,
   56,   56,   56,   22,   22,   56,    0,    0,   54,    0,
    0,    0,   54,   54,   15,   54,   16,   17,    0,   54,
   54,   54,   54,    0,    0,   54,    0,    0,   55,    0,
    0,    0,   55,   55,   15,   55,   16,   17,    0,   55,
   55,   55,   55,   24,    0,   55,    0,   24,   20,   20,
   24,   37,   38,   39,   40,    0,   15,    1,   16,   17,
   24,    2,    0,    1,    3,    0,    0,    2,    1,    0,
    3,    0,    2,    0,    4,    3,    0,    0,    0,    0,
    4,    0,    0,    0,    0,    4,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   40,   40,   59,   40,   45,   59,   41,  123,    8,   25,
   61,   40,  116,  123,   41,   85,   45,   44,   30,   44,
  124,   44,   41,  272,   43,   44,   45,   40,   98,  123,
   46,  257,   45,   84,   59,   44,   59,  123,   44,    2,
    3,   60,   41,   62,   43,   44,   45,   59,   41,  123,
   42,   14,   45,  257,  258,   47,   19,   73,  275,  276,
  123,   60,   41,   62,   43,   44,   45,  257,   41,  262,
  263,  261,   45,   41,  264,   43,   43,   45,   45,  260,
   44,   60,   45,   62,  274,   85,   21,   41,  123,   43,
   25,   45,   41,   60,   45,   62,   41,   43,   98,   45,
   41,  257,   44,   41,  123,  261,  125,   44,  264,   41,
   42,   46,   47,   41,  257,   50,   52,   53,  274,  275,
  276,   41,   42,   43,  123,   45,  125,   47,  265,  266,
  267,  257,  257,  263,   97,  257,   41,   72,   73,  263,
   41,  257,  125,  125,  123,  261,  125,  257,  264,   59,
  257,  261,   44,  125,  264,   41,   41,   41,  274,  275,
  276,  125,  267,  257,  274,  275,  276,  261,  262,    0,
  264,  257,   44,  123,   44,  261,   44,  263,  264,   44,
  274,  125,  125,  257,   96,  125,  125,  261,  274,  263,
  264,   47,    3,   19,  257,   -1,   -1,   -1,  261,  262,
  274,  264,   -1,   -1,   -1,   -1,   -1,   -1,  265,  266,
  267,  274,   -1,  267,   -1,   -1,  257,  257,  259,  260,
  261,  258,  257,  264,   -1,   -1,  261,  262,  257,  264,
  259,  260,  273,  274,  275,  276,  275,  276,  257,  274,
   -1,   -1,  261,  262,  257,  264,  259,  260,   -1,  268,
  269,  270,  271,  262,  263,  274,   -1,   -1,  257,   -1,
   -1,   -1,  261,  262,  257,  264,  259,  260,   -1,  268,
  269,  270,  271,   -1,   -1,  274,   -1,   -1,  257,   -1,
   -1,   -1,  261,  262,  257,  264,  259,  260,   -1,  268,
  269,  270,  271,  257,   -1,  274,   -1,  261,  262,  263,
  264,  268,  269,  270,  271,   -1,  257,  257,  259,  260,
  274,  261,   -1,  257,  264,   -1,   -1,  261,  257,   -1,
  264,   -1,  261,   -1,  274,  264,   -1,   -1,   -1,   -1,
  274,   -1,   -1,   -1,   -1,  274,
};
}
final static short YYFINAL=7;
final static short YYMAXTOKEN=277;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'",null,"'>'",null,null,null,null,null,null,null,null,null,null,null,null,
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
"sentenciaDEC : tipo ID parametrosDef cuerpofuncion",
"parametrosDef : '(' tipo ID ')'",
"parametrosDef : '(' tipo ID",
"parametrosDef : tipo ID ')'",
"cuerpofuncion : '{' BS retorno '}'",
"cuerpofuncion : '{' retorno '}'",
"cuerpofuncion : BS retorno '}'",
"retorno : RETURN expresioncparentesis",
"retorno : RETURN",
"retorno : expresioncparentesis",
"lista_variables : lista_variables ';' ID",
"lista_variables : ID",
"BCE : '{' BCE2 '}'",
"BCE : sentenciaCE",
"BCE : BCE2 '}'",
"BCE : '{' BCE2",
"BCE2 : BCE2 sentenciaCE",
"BCE2 : sentenciaCE",
"expresioncparentesis : '(' expresion ')'",
"expresioncparentesis : '(' expresion",
"expresioncparentesis : expresion ')'",
"expresioncparentesis : '(' ')'",
"printeable : '(' CADENA ')'",
"printeable : '(' ID ')'",
"printeable : '(' CADENA",
"printeable : CADENA ')'",
"sentenciaCE : PRINT printeable ','",
"sentenciaCE : asignacion ','",
"sentenciaCE : IF condicioncparentesis BCE ENDIF",
"sentenciaCE : IF condicioncparentesis BCE ELSE BCE ENDIF",
"sentenciaCE : WHILE condicioncparentesis BCE ','",
"sentenciaCE : IF condicioncparentesis ELSE BCE ENDIF",
"sentenciaCE : IF condicioncparentesis BCE ELSE ENDIF",
"sentenciaCE : IF condicioncparentesis ELSE ENDIF",
"condicioncparentesis : '(' condicion ')'",
"condicioncparentesis : '(' ')'",
"condicioncparentesis : '(' condicion",
"condicioncparentesis : condicion ')'",
"condicion : expresion operador_logico expresion",
"operador_logico : '<'",
"operador_logico : '>'",
"operador_logico : MENORIGUAL",
"operador_logico : MAYORIGUAL",
"operador_logico : IGUALIGUAL",
"operador_logico : DISTINTO",
"asignacion : ID ASIGN expresion",
"asignacion : ID ASIGN",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : USLINTEGER",
"factor : SINGLE",
"$$1 :",
"factor : '-' SINGLE $$1 ID parametros ','",
"parametros : '(' ID ';' lista_permisos ')'",
"parametros : ID ';' lista_permisos ')'",
"parametros : '(' ID ';' lista_permisos",
"parametros : '(' ID lista_permisos ')'",
"tipo : USLINTEGERPR",
"tipo : SINGLEPR",
"lista_permisos : READONLY",
"lista_permisos : WRITE",
"lista_permisos : PASS",
"lista_permisos : WRITE ';' PASS",
"lista_permisos : WRITE PASS",
};

//#line 186 "gramaticaGrupo16.y"

public Analizador_Lexico al;
public ArrayList<AnalizadorLexico.Error> erroresGram;
public static TokenValue ultimoTokenleido;
public static ArrayList<String> estructuras;
public int yylex(){
	Token t=null;
	try {
		t = this.al.getToken();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.println("IOException  en el metodo getToken");
	}
	if(t!=null){
		yylval = new ParserVal(t.lexema);
		TokenValue tv = new TokenValue (t.getLexema(), Analizador_Lexico.cantLN);
		ultimoTokenleido=tv;
		System.out.println("leyo : "+t.lexema+" 	Numero de token: "+t.getNro());
		return t.getNro();
	}
	return 0;
}

public void yyerror ( String e){
	System.out.println(e);
}
public int parsepublico(){
	return this.yyparse();
}
public static void main(String [] args) throws IOException{
	//BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    //File f = new File(reader.readLine());
	File f = new File("prueba.txt");
    Analizador_Lexico lexico = null;
    ArrayList<AnalizadorLexico.Error> erroresGram = null;
    ArrayList<Token> tokens = null;
    try {
        lexico = new Analizador_Lexico(f);
        tokens = Main.getAllTokens(lexico);
        erroresGram = new ArrayList<>();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
	Analizador_Lexico al=new Analizador_Lexico(f);
	Parser p=new Parser(false);
	p.erroresGram=erroresGram;
	Parser.estructuras=new ArrayList<>();
	p.al=al;
	int unint=p.parsepublico();
	if(unint==0)
		System.out.println("ACCEPT, se reconocio la gramatica");
	else
		System.out.println("n");
	
	File fErroresOut = new File("S_Errores.txt");
	FileManager.write(erroresGram.toString(), fErroresOut);
    File fTokensOut = new File("S_Tokens.txt");
    FileManager.write(tokens.toString(), fTokensOut);
	File estructurasOut = new File("Estructuras.txt");
	FileManager.write(Parser.estructuras.toString(), estructurasOut);
}
//#line 447 "Parser.java"
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
//#line 37 "gramaticaGrupo16.y"
{Parser.estructuras.add("Se detecto la declaracion de variables en la linea "+Analizador_Lexico.cantLN+"\n");}
break;
case 7:
//#line 38 "gramaticaGrupo16.y"
{Parser.estructuras.add("Se detecto la declaracion de una funcion en la linea "+Analizador_Lexico.cantLN+"\n");
	Token t=al.tablaSimbolos.get(val_peek(2).sval);
	t.declarada=true;}
break;
case 8:
//#line 52 "gramaticaGrupo16.y"
{Token t=al.tablaSimbolos.get(val_peek(1).sval);if(t.declarada==false)this.erroresGram.add(new ErrorG("Error 18: La variable "+val_peek(1).sval+" no esta declarada ", al.cantLN));}
break;
case 9:
//#line 54 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error SIN NUMERO : Falta un ) despues del identificador", al.cantLN));}
break;
case 10:
//#line 55 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error SIN NUMERO : Falta un ( antes del tipo del parametro", al.cantLN));}
break;
case 12:
//#line 60 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error 23: Se esperaba un bloque de sentencias en el cuerpo de la funcion", al.cantLN));}
break;
case 13:
//#line 62 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error SIN NUMERO: Se esperaba un { al principio de la funcion", al.cantLN));}
break;
case 15:
//#line 66 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error 24: La funcion debe retornar un valor", al.cantLN));}
break;
case 16:
//#line 67 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error 24.5: Se esperaba un return", al.cantLN));}
break;
case 17:
//#line 70 "gramaticaGrupo16.y"
{Token t=al.tablaSimbolos.get(val_peek(0).sval);
	if(t!=null)t.declarada=true;}
break;
case 18:
//#line 72 "gramaticaGrupo16.y"
{	Token t=al.tablaSimbolos.get(val_peek(0).sval);
	if(t!=null)t.declarada=true;}
break;
case 21:
//#line 79 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error SIN NUMERO: Se esperaba un { al principio del bloque de sentencias", al.cantLN));}
break;
case 22:
//#line 80 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error SIN NUMERO: Se esperaba un } al final del bloque de sentencias", al.cantLN));}
break;
case 26:
//#line 88 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error 25: Se esperaba un )", al.cantLN));}
break;
case 27:
//#line 89 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error 26: Se esperaba un (", al.cantLN));}
break;
case 28:
//#line 90 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error 27: Se esperaba una expresion", al.cantLN));}
break;
case 30:
//#line 94 "gramaticaGrupo16.y"
{System.out.println("--------------------------------------------------------------------------------------------");
		System.out.println("DEBUGUEANDO: esto es un separador");
		System.out.println("--------------------------------------------------------------------------------------------");}
break;
case 31:
//#line 97 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error 21 : Falta un )", al.cantLN));}
break;
case 32:
//#line 98 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error 22 : Falta un (", al.cantLN));}
break;
case 33:
//#line 101 "gramaticaGrupo16.y"
{Parser.estructuras.add("Se detecto un print en la linea "+Analizador_Lexico.cantLN+"\n");}
break;
case 34:
//#line 102 "gramaticaGrupo16.y"
{Parser.estructuras.add("Se detecto una asignacion en  la linea "+Analizador_Lexico.cantLN+"\n");}
break;
case 35:
//#line 103 "gramaticaGrupo16.y"
{Parser.estructuras.add("Se detecto un if en la linea "+Analizador_Lexico.cantLN+"\n");}
break;
case 36:
//#line 104 "gramaticaGrupo16.y"
{Parser.estructuras.add("Se detecto un if en la linea "+Analizador_Lexico.cantLN+"\n");}
break;
case 37:
//#line 105 "gramaticaGrupo16.y"
{Parser.estructuras.add("Se detecto un while en la linea "+Analizador_Lexico.cantLN+"\n");}
break;
case 38:
//#line 110 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error 9: Se esperaba un bloque de sentencias en la rama del if", al.cantLN));}
break;
case 39:
//#line 111 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error 10: Se esperaba un bloque de sentencias en la rama del else", al.cantLN));}
break;
case 40:
//#line 112 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error 11: Se esperaba un bloque de sentencias en la rama del if y del else", al.cantLN));}
break;
case 42:
//#line 120 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error14: No se definio una condicion", al.cantLN));}
break;
case 43:
//#line 121 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error4: Se esperaba un ) despues de la condicion", al.cantLN));}
break;
case 44:
//#line 122 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error5: Se esperaba un ( antes de la condicion", al.cantLN));}
break;
case 52:
//#line 140 "gramaticaGrupo16.y"
{	Token t=al.tablaSimbolos.get(val_peek(2).sval);
	if(t.declarada==false)this.erroresGram.add(new ErrorG("Error 34 : La variable "+val_peek(2).sval+" no esta declarada ", al.cantLN));}
break;
case 53:
//#line 144 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error SIN NUMERO: Se esperaba una expresion del lado derecho de la asignacion", al.cantLN));}
break;
case 63:
//#line 160 "gramaticaGrupo16.y"
{	Token t=al.tablaSimbolos.get(val_peek(0).sval);
	t.lexema="-"+t.lexema;}
break;
case 64:
//#line 162 "gramaticaGrupo16.y"
{Parser.estructuras.add("Se detecto la invocacion de una funcion en la linea "+Analizador_Lexico.cantLN+"\n");}
break;
case 65:
//#line 164 "gramaticaGrupo16.y"
{Token t=al.tablaSimbolos.get(val_peek(4).sval);
	if(t!=null&&t.declarada==false)this.erroresGram.add(new ErrorG("Error 35: La variable "+val_peek(3).sval+" no esta declarada ", al.cantLN));}
break;
case 66:
//#line 167 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error 15: Se esperaba un (", al.cantLN));}
break;
case 67:
//#line 168 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error 16: Se esperaba un )", al.cantLN));}
break;
case 68:
//#line 169 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error 17: Se esperaba un ;", al.cantLN));}
break;
case 75:
//#line 181 "gramaticaGrupo16.y"
{this.erroresGram.add(new ErrorG("Error3: Se esperaba un ; entre los permisos", al.cantLN));}
break;
//#line 761 "Parser.java"
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
