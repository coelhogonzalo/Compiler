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






//#line 2 "gramaticaLucho.y"
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
import GeneracionCodigoIntermedio.Polaca_Inversa;
//#line 31 "Parser.java"




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
    0,    1,    1,    2,    2,    4,    4,    7,    8,    9,
    6,    6,   11,   11,   12,   12,   10,   14,   15,   14,
    3,    3,    3,    3,    3,   17,   18,   19,   20,   21,
   23,   22,   24,   24,   24,   24,   24,   24,   16,   13,
   13,   13,   25,   25,   25,   26,   26,   26,   26,   26,
   27,    5,    5,   28,   28,   28,   28,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    1,    3,    4,    4,    4,    2,
    3,    1,    3,    1,    2,    1,    3,    3,    0,    4,
    3,    2,    3,    5,    3,    2,    1,    2,    3,    3,
    1,    3,    1,    1,    1,    1,    1,    1,    3,    3,
    3,    1,    3,    3,    1,    1,    1,    1,    2,    3,
    5,    1,    1,    1,    1,    1,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,   53,   52,    0,    0,    3,    4,
    5,    0,    0,    0,    0,    0,    0,   26,   31,   28,
    0,    0,    0,    2,    0,    0,   22,    0,   14,    0,
    0,    0,   47,   48,    0,    0,    0,   45,    0,    0,
    0,    0,    0,   21,    0,    0,    6,    0,   16,    0,
   27,   23,    0,   25,    0,    0,   49,    0,    0,    0,
    0,   36,   35,   37,   38,   33,   34,    0,   29,   30,
   19,   18,    0,    0,    7,   11,   13,   15,    0,    0,
   50,    0,    0,   43,   44,    0,   20,    0,    0,   24,
    0,    8,    0,    0,   54,    0,   56,    0,    0,   10,
    9,    0,   51,    0,   57,   17,
};
final static short yydgoto[] = {                          7,
    8,    9,   10,   11,   12,   26,   46,   75,   94,  100,
   30,   50,   39,   23,   87,   13,   14,   53,   15,   18,
   20,   40,   21,   68,   37,   38,   56,   98,
};
final static short yysindex[] = {                      -199,
 -247,   16,   20,   27,    0,    0,    0, -199,    0,    0,
    0, -219,   26, -108, -108,  -34,  -34,    0,    0,    0,
  -34, -185,   44,    0,   51,   -2,    0, -193,    0, -180,
   48,   53,    0,    0, -166,    2,   -3,    0,  -25,   54,
   55,   56,   57,    0, -191,  -24,    0, -156,    0, -109,
    0,    0, -108,    0, -155,   60,    0,  -34,  -34,  -34,
  -34,    0,    0,    0,    0,    0,    0,  -34,    0,    0,
    0,    0, -152, -199,    0,    0,    0,    0, -157,   49,
    0,   -3,   -3,    0,    0,    2,    0,   66, -221,    0,
 -187,    0,   69,  -15,    0,   52,    0,   71,  -34,    0,
    0, -154,    0,    5,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,  114,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   15,    0,    0,    0,    0,    0,
    0,  -41,    0,    0,    0,   72,  -36,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -31,  -11,    0,    0,   74,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   76,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   45,   14,   13,    0,   73,    0,    0,    0,    0,    0,
    8,    0,    1,    0,    0,    0,    0,    0,    0,    0,
    0,   99,    0,    0,   28,   29,    0,    0,
};
final static int YYTABLESIZE=260;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         46,
   46,   46,   46,   46,   42,   46,   42,   42,   42,   40,
   35,   40,   40,   40,   28,   77,   36,   58,   46,   59,
   46,   24,   31,   42,   16,   42,   29,   29,   40,   41,
   40,   41,   41,   41,   66,    1,   67,   25,   60,    2,
   49,   47,    3,   61,   58,  106,   59,   58,   41,   59,
   41,   93,    4,    5,    6,   17,   48,    1,   12,   19,
   79,    2,   78,    1,    3,   29,   22,    2,   86,   27,
    3,   42,   43,   12,    4,    5,    6,   95,   96,   97,
    4,   51,   52,    5,    6,   82,   83,   44,   84,   85,
   45,   54,   55,   57,   69,   70,   71,   72,   74,  104,
   76,   80,   24,   81,   88,   90,   92,   91,   99,  101,
  102,  103,  105,    1,   32,   39,   55,   73,   89,   41,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    1,    1,    0,
    0,    2,    2,    0,    3,    3,    0,    0,    0,    0,
    0,    0,    0,    0,    4,    4,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   32,    0,   33,   34,   46,   46,   46,   46,
    0,   42,   42,   42,   42,    0,   40,   40,   40,   40,
    0,    0,   62,   63,   64,   65,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   41,   41,   41,   41,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   41,   47,   43,   44,   45,   41,
   45,   43,   44,   45,  123,  125,   16,   43,   60,   45,
   62,    8,   15,   60,  272,   62,   14,   15,   60,   41,
   62,   43,   44,   45,   60,  257,   62,  257,   42,  261,
   28,   44,  264,   47,   43,   41,   45,   43,   60,   45,
   62,  273,  274,  275,  276,   40,   59,  257,   44,   40,
   53,  261,   50,  257,  264,   53,   40,  261,   68,   44,
  264,  257,  258,   59,  274,  275,  276,  265,  266,  267,
  274,  262,  263,  275,  276,   58,   59,   44,   60,   61,
   40,   44,   40,  260,   41,   41,   41,   41,  123,   99,
  257,  257,   89,   44,  257,  263,   41,   59,   40,  125,
   59,   41,  267,    0,   41,   44,   41,   45,   74,   21,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  257,   -1,
   -1,  261,  261,   -1,  264,  264,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  274,  274,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  257,   -1,  259,  260,  268,  269,  270,  271,
   -1,  268,  269,  270,  271,   -1,  268,  269,  270,  271,
   -1,   -1,  268,  269,  270,  271,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  268,  269,  270,  271,
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
"cuerpofuncion : '{' BS retorno '}'",
"retorno : RETURN expresioncparentesis",
"lista_variables : lista_variables ';' ID",
"lista_variables : ID",
"BCE : '{' BCE2 '}'",
"BCE : sentenciaCE",
"BCE2 : BCE2 sentenciaCE",
"BCE2 : sentenciaCE",
"expresioncparentesis : '(' expresion ')'",
"printeable : '(' CADENA ')'",
"$$1 :",
"printeable : '(' ID ')' $$1",
"sentenciaCE : PRINT printeable ','",
"sentenciaCE : asignacion ','",
"sentenciaCE : ifcond BCE ENDIF",
"sentenciaCE : ifcond BCE elsecond BCE ENDIF",
"sentenciaCE : whilecond BCE ','",
"ifcond : IF condicioncparentesis",
"elsecond : ELSE",
"whilecond : WHILE condicioncparentesiswhile",
"condicioncparentesis : '(' condicion ')'",
"condicioncparentesiswhile : iniciocondicioncparentesiswhile condicion ')'",
"iniciocondicioncparentesiswhile : '('",
"condicion : expresion operador_logico expresion",
"operador_logico : '<'",
"operador_logico : '>'",
"operador_logico : MENORIGUAL",
"operador_logico : MAYORIGUAL",
"operador_logico : IGUALIGUAL",
"operador_logico : DISTINTO",
"asignacion : ID ASIGN expresion",
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
"tipo : USLINTEGERPR",
"tipo : SINGLEPR",
"lista_permisos : READONLY",
"lista_permisos : WRITE",
"lista_permisos : PASS",
"lista_permisos : WRITE ';' PASS",
};

//#line 209 "gramaticaLucho.y"

public Polaca_Inversa PI = new Polaca_Inversa();
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




//#line 408 "Parser.java"
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
//#line 38 "gramaticaLucho.y"
{Parser.estructuras.add("Se detecto la declaracion de variables en la linea "+Analizador_Lexico.cantLN+"\n");}
break;
case 7:
//#line 39 "gramaticaLucho.y"
{Parser.estructuras.add("Se detecto la declaracion de una funcion en la linea "+Analizador_Lexico.cantLN+"\n");
	Token t=al.tablaSimbolos.get(val_peek(2).sval);
	t.declarada=true;}
break;
case 8:
//#line 53 "gramaticaLucho.y"
{Token t=al.tablaSimbolos.get(val_peek(1).sval);if(t.declarada==false)this.erroresGram.add(new ErrorG("Error 18: La variable "+val_peek(1).sval+" no esta declarada ", al.cantLN));}
break;
case 11:
//#line 71 "gramaticaLucho.y"
{Token t=al.tablaSimbolos.get(val_peek(0).sval);
	if(t!=null)t.declarada=true;}
break;
case 12:
//#line 73 "gramaticaLucho.y"
{	Token t=al.tablaSimbolos.get(val_peek(0).sval);
	if(t!=null)t.declarada=true;}
break;
case 18:
//#line 94 "gramaticaLucho.y"
{ PI.put(val_peek(1).sval); }
break;
case 19:
//#line 95 "gramaticaLucho.y"
{ PI.put(val_peek(1).sval); }
break;
case 20:
//#line 95 "gramaticaLucho.y"
{System.out.println("--------------------------------------------------------------------------------------------");
		System.out.println("DEBUGUEANDO: esto es un separador");
		System.out.println("--------------------------------------------------------------------------------------------");}
break;
case 21:
//#line 102 "gramaticaLucho.y"
{Parser.estructuras.add("Se detecto un print en la linea "+Analizador_Lexico.cantLN+"\n"); PI.put("print");}
break;
case 22:
//#line 103 "gramaticaLucho.y"
{Parser.estructuras.add("Se detecto una asignacion en  la linea "+Analizador_Lexico.cantLN+"\n");}
break;
case 23:
//#line 104 "gramaticaLucho.y"
{Parser.estructuras.add("Se detecto un if en la linea "+Analizador_Lexico.cantLN+"\n"); PI.desapilar(); }
break;
case 24:
//#line 105 "gramaticaLucho.y"
{Parser.estructuras.add("Se detecto un if en la linea "+Analizador_Lexico.cantLN+"\n"); PI.desapilar();}
break;
case 25:
//#line 106 "gramaticaLucho.y"
{Parser.estructuras.add("Se detecto un while en la linea "+Analizador_Lexico.cantLN+"\n"); PI.desapilar(); PI.salto(); PI.desapilar(); }
break;
case 26:
//#line 120 "gramaticaLucho.y"
{ PI.bifurcacion(); }
break;
case 27:
//#line 124 "gramaticaLucho.y"
{ PI.desapilar(); PI.bifurcacion(); }
break;
case 28:
//#line 128 "gramaticaLucho.y"
{ PI.bifurcacion(); }
break;
case 31:
//#line 142 "gramaticaLucho.y"
{ PI.apilar(); }
break;
case 33:
//#line 154 "gramaticaLucho.y"
{ PI.put("<"); }
break;
case 34:
//#line 155 "gramaticaLucho.y"
{ PI.put(">"); }
break;
case 35:
//#line 156 "gramaticaLucho.y"
{ PI.put("<="); }
break;
case 36:
//#line 157 "gramaticaLucho.y"
{ PI.put(">="); }
break;
case 37:
//#line 158 "gramaticaLucho.y"
{ PI.put("=="); }
break;
case 38:
//#line 160 "gramaticaLucho.y"
{ PI.put("!="); }
break;
case 39:
//#line 163 "gramaticaLucho.y"
{	Token t=al.tablaSimbolos.get(val_peek(2).sval); PI.put(":=");
	if(t.declarada==false)this.erroresGram.add(new ErrorG("Error 34 : La variable "+val_peek(2).sval+" no esta declarada ", al.cantLN));}
break;
case 40:
//#line 170 "gramaticaLucho.y"
{ PI.put("+"); }
break;
case 41:
//#line 171 "gramaticaLucho.y"
{ PI.put("-"); }
break;
case 43:
//#line 175 "gramaticaLucho.y"
{ PI.put("*"); }
break;
case 44:
//#line 176 "gramaticaLucho.y"
{ PI.put("/"); }
break;
case 46:
//#line 180 "gramaticaLucho.y"
{ PI.put(val_peek(0).sval); }
break;
case 47:
//#line 181 "gramaticaLucho.y"
{ PI.put(val_peek(0).sval); }
break;
case 48:
//#line 182 "gramaticaLucho.y"
{ PI.put(val_peek(0).sval); }
break;
case 49:
//#line 183 "gramaticaLucho.y"
{	Token t=al.tablaSimbolos.get(val_peek(0).sval);
	t.lexema="-"+t.lexema; PI.put(val_peek(1).sval);}
break;
case 50:
//#line 185 "gramaticaLucho.y"
{Parser.estructuras.add("Se detecto la invocacion de una funcion en la linea "+Analizador_Lexico.cantLN+"\n");}
break;
case 51:
//#line 187 "gramaticaLucho.y"
{Token t=al.tablaSimbolos.get(val_peek(4).sval);
	if(t!=null&&t.declarada==false)this.erroresGram.add(new ErrorG("Error 35: La variable "+val_peek(3).sval+" no esta declarada ", al.cantLN));}
break;
//#line 702 "Parser.java"
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
