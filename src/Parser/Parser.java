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
   10,    6,    6,   12,   12,   13,   13,   11,   15,   16,
   15,    3,    3,    3,    3,    3,   18,   19,   20,   21,
   23,   22,   24,   25,   25,   25,   25,   25,   25,   17,
   14,   14,   14,   26,   26,   26,   27,   27,   27,   27,
   27,   28,    5,    5,   29,   29,   29,   29,
};
final static short yylen[] = {                            2,
    1,    2,    1,    1,    1,    3,    3,    2,    4,    4,
    2,    3,    1,    3,    1,    2,    1,    3,    3,    0,
    4,    3,    2,    3,    5,    3,    2,    1,    1,    2,
    1,    3,    3,    1,    1,    1,    1,    1,    1,    3,
    3,    3,    1,    3,    3,    1,    1,    1,    1,    2,
    3,    5,    1,    1,    1,    1,    1,    3,
};
final static short yydefred[] = {                         0,
    0,    0,   31,    0,   54,   53,    0,    0,    3,    4,
    5,    0,    0,    0,    0,    0,    0,    0,    0,   27,
    0,    0,    2,    0,    0,    0,    0,   23,    0,   15,
    0,    0,    0,   30,    0,   48,   49,    0,    0,    0,
   46,    0,    0,    0,    0,   22,    6,    0,    0,    0,
    7,   17,    0,   29,    0,   24,   26,    0,    0,   50,
    0,    0,    0,    0,   37,   36,   38,   39,   34,   35,
    0,   32,   20,   19,   12,    0,    0,   14,   16,    0,
    0,   51,    0,    0,   44,   45,    0,   21,    9,    0,
    0,   25,    0,    0,   11,   10,   55,    0,   57,    0,
    0,    0,   52,   18,   58,
};
final static short yydgoto[] = {                          7,
    8,    9,   10,   11,   12,   25,   13,   27,   51,   91,
   95,   31,   53,   39,   22,   88,   14,   15,   32,   55,
   16,   20,   17,   43,   71,   40,   41,   59,  100,
};
final static short yysindex[] = {                      -201,
 -247,   37,    0,   39,    0,    0,    0, -201,    0,    0,
    0, -219,   49,    1, -108, -108,   37,  -34,  -34,    0,
 -199,    3,    0,    0,   13, -190,  -57,    0, -193,    0,
 -192, -173,   51,    0,   56,    0,    0, -163,    5,   -3,
    0,  -25,   57,   58,   59,    0,    0, -156, -155, -201,
    0,    0, -109,    0, -108,    0,    0, -154,   60,    0,
  -34,  -34,  -34,  -34,    0,    0,    0,    0,    0,    0,
  -34,    0,    0,    0,    0,   64, -221,    0,    0, -157,
   48,    0,   -3,   -3,    0,    0,    5,    0,    0,   68,
  -16,    0, -183,  -34,    0,    0,    0,   52,    0,   69,
   35, -153,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,  112,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    2,    0,    0,    0,    0,    0,    0,
 -150,    0,    0,    0,  -41,    0,    0,    0,   71,  -36,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -31,  -11,    0,    0,   75,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   76,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   70,   14,   12,    0,   92,    0,    0,    0,    0,    0,
    0,    7,    0,   -2,    0,    0,    0,    0,    0,    0,
    0,  102,    0,    0,    0,   26,   30,    0,    0,
};
final static int YYTABLESIZE=260;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         47,
   47,   47,   47,   47,   43,   47,   43,   43,   43,   41,
   38,   41,   41,   41,   29,   78,   42,   61,   47,   62,
   47,   23,   33,   43,   18,   43,   30,   30,   41,   42,
   41,   42,   42,   42,   69,    1,   70,   24,   63,    2,
   52,    8,    3,   64,   28,   13,   46,   61,   42,   62,
   42,   90,    4,    5,    6,    1,   47,   44,   45,    2,
   13,   80,    3,    1,   79,   50,   30,    2,   87,   54,
    3,   48,    4,    5,    6,  104,   19,   61,   21,   62,
    4,   97,   98,   99,    5,    6,   83,   84,   26,   56,
   23,  101,   85,   86,   57,   58,   60,   72,   73,   74,
   75,   76,   81,   82,   89,   92,   93,   94,   96,  103,
  102,    1,   28,  105,   40,   33,   56,   49,   34,   77,
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
    0,    0,   35,    0,   36,   37,   47,   47,   47,   47,
    0,   43,   43,   43,   43,    0,   41,   41,   41,   41,
    0,    0,   65,   66,   67,   68,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   42,   42,   42,   42,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   41,   47,   43,   44,   45,   41,
   45,   43,   44,   45,  123,  125,   19,   43,   60,   45,
   62,    8,   16,   60,  272,   62,   15,   16,   60,   41,
   62,   43,   44,   45,   60,  257,   62,  257,   42,  261,
   29,   40,  264,   47,   44,   44,   44,   43,   60,   45,
   62,  273,  274,  275,  276,  257,   44,  257,  258,  261,
   59,   55,  264,  257,   53,  123,   55,  261,   71,  262,
  264,   59,  274,  275,  276,   41,   40,   43,   40,   45,
  274,  265,  266,  267,  275,  276,   61,   62,   40,  263,
   77,   94,   63,   64,   44,   40,  260,   41,   41,   41,
  257,  257,  257,   44,   41,  263,   59,   40,  125,   41,
   59,    0,  263,  267,   44,   41,   41,   26,   17,   50,
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
"sentenciaDEC : tipoFunID parametrosDef cuerpofuncion",
"tipoFunID : tipo ID",
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
"sentenciaCE : ifcond cuerpoif ENDIF",
"sentenciaCE : ifcond BCE elsecond BCE ENDIF",
"sentenciaCE : whilecond BCE ','",
"ifcond : IF condicioncparentesis",
"cuerpoif : BCE",
"elsecond : ELSE",
"whilecond : whileparaapilar condicioncparentesis",
"whileparaapilar : WHILE",
"condicioncparentesis : '(' condicion ')'",
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

//#line 220 "gramaticaLucho.y"

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




//#line 409 "Parser.java"
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
	Token t=al.tablaSimbolos.get(val_peek(1).sval);
	t.declarada=true;
	}
break;
case 8:
//#line 53 "gramaticaLucho.y"
{ PI.inicioFuncion(val_peek(0).sval); }
break;
case 9:
//#line 58 "gramaticaLucho.y"
{Token t=al.tablaSimbolos.get(val_peek(1).sval);if(t.declarada==false)this.erroresGram.add(new ErrorG("Error 18: La variable "+val_peek(1).sval+" no esta declarada ", al.cantLN));}
break;
case 11:
//#line 71 "gramaticaLucho.y"
{PI.finFuncion(); }
break;
case 12:
//#line 76 "gramaticaLucho.y"
{Token t=al.tablaSimbolos.get(val_peek(0).sval);
	if(t!=null)t.declarada=true;}
break;
case 13:
//#line 78 "gramaticaLucho.y"
{	Token t=al.tablaSimbolos.get(val_peek(0).sval);
	if(t!=null)t.declarada=true;}
break;
case 19:
//#line 99 "gramaticaLucho.y"
{ PI.put(val_peek(1).sval); }
break;
case 20:
//#line 100 "gramaticaLucho.y"
{ PI.put(val_peek(1).sval); }
break;
case 21:
//#line 100 "gramaticaLucho.y"
{System.out.println("--------------------------------------------------------------------------------------------");
		System.out.println("DEBUGUEANDO: esto es un separador");
		System.out.println("--------------------------------------------------------------------------------------------");}
break;
case 22:
//#line 107 "gramaticaLucho.y"
{Parser.estructuras.add("Se detecto un print en la linea "+Analizador_Lexico.cantLN+"\n"); PI.put("print");}
break;
case 23:
//#line 108 "gramaticaLucho.y"
{Parser.estructuras.add("Se detecto una asignacion en  la linea "+Analizador_Lexico.cantLN+"\n");}
break;
case 24:
//#line 109 "gramaticaLucho.y"
{Parser.estructuras.add("Se detecto un if en la linea "+Analizador_Lexico.cantLN+"\n"); PI.desapilar(); }
break;
case 25:
//#line 110 "gramaticaLucho.y"
{Parser.estructuras.add("Se detecto un if en la linea "+Analizador_Lexico.cantLN+"\n"); PI.desapilar();}
break;
case 26:
//#line 111 "gramaticaLucho.y"
{Parser.estructuras.add("Se detecto un while en la linea "+Analizador_Lexico.cantLN+"\n"); PI.saltoIncond(); PI.desapilar(); }
break;
case 27:
//#line 125 "gramaticaLucho.y"
{ PI.bifurcacion(); }
break;
case 29:
//#line 134 "gramaticaLucho.y"
{ PI.desapilarElse(); PI.bifurcacion(); }
break;
case 30:
//#line 138 "gramaticaLucho.y"
{ PI.bifurcacion(); }
break;
case 31:
//#line 142 "gramaticaLucho.y"
{ PI.setSaltoIncond(); }
break;
case 33:
//#line 157 "gramaticaLucho.y"
{ PI.put(val_peek(1).sval); }
break;
case 34:
//#line 164 "gramaticaLucho.y"
{ yyval.sval = "<"; }
break;
case 35:
//#line 165 "gramaticaLucho.y"
{ yyval.sval = ">"; }
break;
case 36:
//#line 166 "gramaticaLucho.y"
{ yyval.sval = "<="; }
break;
case 37:
//#line 167 "gramaticaLucho.y"
{ yyval.sval = ">="; }
break;
case 38:
//#line 168 "gramaticaLucho.y"
{ yyval.sval = "=="; }
break;
case 39:
//#line 169 "gramaticaLucho.y"
{ yyval.sval = "!="; }
break;
case 40:
//#line 173 "gramaticaLucho.y"
{	Token t=al.tablaSimbolos.get(val_peek(2).sval); PI.put(val_peek(2).sval); PI.put(":=");
	if(t.declarada==false)this.erroresGram.add(new ErrorG("Error 34 : La variable "+val_peek(2).sval+" no esta declarada ", al.cantLN));}
break;
case 41:
//#line 180 "gramaticaLucho.y"
{ PI.put("+"); }
break;
case 42:
//#line 181 "gramaticaLucho.y"
{ PI.put("-"); }
break;
case 44:
//#line 185 "gramaticaLucho.y"
{ PI.put("*"); }
break;
case 45:
//#line 186 "gramaticaLucho.y"
{ PI.put("/"); }
break;
case 47:
//#line 190 "gramaticaLucho.y"
{ PI.put(val_peek(0).sval); }
break;
case 48:
//#line 191 "gramaticaLucho.y"
{ PI.put(val_peek(0).sval); }
break;
case 49:
//#line 192 "gramaticaLucho.y"
{ PI.put(val_peek(0).sval); }
break;
case 50:
//#line 193 "gramaticaLucho.y"
{	Token t=al.tablaSimbolos.get(val_peek(0).sval);
	t.lexema="-"+t.lexema; PI.put(val_peek(1).sval);}
break;
case 51:
//#line 195 "gramaticaLucho.y"
{Parser.estructuras.add("Se detecto la invocacion de una funcion en la linea "+Analizador_Lexico.cantLN+"\n"); PI.jumpToFun(val_peek(2).sval); }
break;
case 52:
//#line 198 "gramaticaLucho.y"
{Token t=al.tablaSimbolos.get(val_peek(4).sval);
	if(t!=null&&t.declarada==false)this.erroresGram.add(new ErrorG("Error 35: La variable "+val_peek(3).sval+" no esta declarada ", al.cantLN));}
break;
//#line 716 "Parser.java"
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
