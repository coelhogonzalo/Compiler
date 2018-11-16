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






//#line 2 "g16.y"
package Parser;
import java.io.IOException;
import AnalizadorLexico.Analizador_Lexico;
import AnalizadorLexico.Token;
import AnalizadorLexico.TokenValue;
import java.util.ArrayList;
import GeneracionCodigoIntermedio.Polaca_Inversa;
import java.util.Arrays;
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
            0,    1,    1,    2,    2,    4,    4,    7,    8,    9,
            10,   10,   12,   12,   13,   13,   11,    6,    6,   15,
            15,   16,   16,   14,   18,   18,    3,    3,    3,    3,
            3,   20,   21,   22,   24,   23,   25,   26,   26,   26,
            26,   26,   26,   19,   17,   17,   17,   27,   27,   27,
            28,   28,   28,   28,   28,   29,    5,    5,   30,   30,
            30,   30,   30,
    };
    final static short yylen[] = {                            2,
            1,    2,    1,    1,    1,    3,    3,    2,    4,    4,
            2,    0,    1,    1,    3,    3,    2,    3,    1,    3,
            1,    2,    1,    3,    3,    3,    3,    2,    3,    5,
            2,    2,    1,    2,    1,    3,    3,    1,    1,    1,
            1,    1,    1,    3,    3,    3,    1,    3,    3,    1,
            1,    1,    1,    2,    2,    5,    1,    1,    1,    1,
            1,    3,    0,
    };
    final static short yydefred[] = {                         0,
            0,    0,   35,    0,   58,   57,    0,    0,    3,    4,
            5,    0,    0,    0,    0,    0,    0,    0,    0,   32,
            0,    0,    2,    0,    0,    0,    0,   28,    0,   21,
            0,   31,   34,    0,   52,   53,    0,    0,    0,   50,
            0,    0,    0,    0,   27,    6,    0,    0,   12,    7,
            23,    0,   33,   29,    0,    0,   55,   54,    0,    0,
            0,    0,   41,   40,   42,   43,   38,   39,    0,   36,
            26,   25,   18,    0,    0,   20,   22,    0,    0,    0,
            0,   48,   49,    0,    9,    0,   13,    0,    0,    0,
            11,   14,   30,    0,    0,   17,    0,    0,   10,   59,
            0,   61,    0,    0,   15,   16,    0,   56,   24,   62,
    };
    final static short yydgoto[] = {                          7,
            8,    9,   30,   11,   12,   25,   13,   27,   50,   75,
            90,   91,   92,   96,   31,   52,   38,   22,   14,   15,
            55,   16,   20,   17,   42,   69,   39,   40,   57,  103,
    };
    final static short yysindex[] = {                      -180,
            -245,    1,    0,    4,    0,    0,    0, -180,    0,    0,
            0, -209,   10,   15, -108, -108,    1,  -34,  -34,    0,
            -204,   35,    0,    0,  -19, -206,  -58,    0, -219,    0,
            -173,    0,    0,   45,    0,    0, -163,   33,    5,    0,
            -25,   57,   58,   59,    0,    0, -156, -155,    0,    0,
            0, -109,    0,    0, -108, -153,    0,    0,  -34,  -34,
            -34,  -34,    0,    0,    0,    0,    0,    0,  -34,    0,
            0,    0,    0,   64, -201,    0,    0, -157,   48,    5,
            5,    0,    0,   33,    0,   68,    0, -209,   10,  -16,
            0,    0,    0, -179,  -34,    0,    2,  -58,    0,    0,
            51,    0,   70,   23,    0,    0, -154,    0,    0,    0,
    };
    final static short yyrindex[] = {                         0,
            0,    0,    0,    0,    0,    0,    0,  112,    0,    0,
            0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
            0,    0,    0,   -1,    0,    0,    0,    0,    0,    0,
            0,    0,    0,  -41,    0,    0,    0,   71,  -36,    0,
            0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
            0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
            0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
            0,    0,    0,    0,    0,    0,    0,    0,    0,  -31,
            -11,    0,    0,   73,    0,    0,    0,    0,    0,    0,
            0,    0,    0,   75,    0,    0,    0,    0,    0,    0,
            76,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    };
    final static short yygindex[] = {                         0,
            0,  110,   28,    0,   -4,   31,   46,   34,   22,    0,
            0,    0,    0,    0,    7,    0,   -2,    0,    0,    0,
            0,    0,  105,    0,    0,    0,   32,   21,    0,    0,
    };
    final static int YYTABLESIZE=260;
    static short yytable[];
    static { yytable();}
    static void yytable(){
        yytable = new short[]{                         51,
                51,   51,   51,   51,   47,   51,   47,   47,   47,   45,
                37,   45,   45,   45,   29,   76,   41,   59,   51,   60,
                51,   48,   32,   47,   46,   47,   18,   10,   45,   46,
                45,   46,   46,   46,   67,   10,   68,    1,    8,   47,
                19,    2,   19,   21,    3,  105,   61,   24,   46,   26,
                46,   62,   43,   44,    4,    1,   51,   19,   28,    2,
                47,   78,    3,  109,   49,   59,   84,   60,    5,    6,
                88,   86,    4,    5,    6,   59,    1,   60,   45,   77,
                2,   82,   83,    3,   56,  100,  101,  102,   53,   54,
                80,   81,  104,    4,    5,    6,   58,   70,   71,   72,
                73,   74,   87,   79,   85,   93,   94,   95,   99,  107,
                108,    1,  110,   37,   44,   63,   60,   23,   97,  106,
                89,   33,   98,    0,    0,    0,    0,    0,    0,    0,
                0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
                0,    0,    0,    0,    0,    0,    0,    1,    1,    0,
                0,    2,    2,    0,    3,    3,    0,    0,    0,    0,
                0,    0,    0,    0,    4,    4,    0,    0,    0,    0,
                0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
                0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
                0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
                0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
                0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
                0,    0,   34,    0,   35,   36,   51,   51,   51,   51,
                0,   47,   47,   47,   47,    0,   45,   45,   45,   45,
                0,    0,   63,   64,   65,   66,    0,    0,    0,    0,
                0,    0,    0,    0,    0,    0,   46,   46,   46,   46,
        };
    }
    static short yycheck[];
    static { yycheck(); }
    static void yycheck() {
        yycheck = new short[] {                         41,
                42,   43,   44,   45,   41,   47,   43,   44,   45,   41,
                45,   43,   44,   45,  123,  125,   19,   43,   60,   45,
                62,   26,   16,   60,   44,   62,  272,    0,   60,   41,
                62,   43,   44,   45,   60,    8,   62,  257,   40,   59,
                40,  261,   44,   40,  264,   44,   42,  257,   60,   40,
                62,   47,  257,  258,  274,  257,   29,   59,   44,  261,
                59,   55,  264,   41,  123,   43,   69,   45,  275,  276,
                75,  273,  274,  275,  276,   43,  257,   45,   44,   52,
                261,   61,   62,  264,   40,  265,  266,  267,  262,  263,
                59,   60,   95,  274,  275,  276,  260,   41,   41,   41,
                257,  257,   75,  257,   41,  263,   59,   40,  125,   59,
                41,    0,  267,   41,   44,   41,   41,    8,   88,   98,
                75,   17,   89,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
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
            "cuerpofuncion : '{' BSFuncion retorno '}'",
            "BSFuncion : BSFuncion sentenciaFuncion",
            "BSFuncion :",
            "sentenciaFuncion : sentenciaCE",
            "sentenciaFuncion : sentenciaDECFuncion",
            "sentenciaDECFuncion : tipo lista_variables ','",
            "sentenciaDECFuncion : tipoFunID parametrosDef cuerpofuncion",
            "retorno : RETURN expresioncparentesis",
            "lista_variables : lista_variables ';' ID",
            "lista_variables : ID",
            "BCE : '{' BCE2 '}'",
            "BCE : sentenciaCE",
            "BCE2 : BCE2 sentenciaCE",
            "BCE2 : sentenciaCE",
            "expresioncparentesis : '(' expresion ')'",
            "printeable : '(' CADENA ')'",
            "printeable : '(' ID ')'",
            "sentenciaCE : PRINT printeable ','",
            "sentenciaCE : asignacion ','",
            "sentenciaCE : ifcond BCE ENDIF",
            "sentenciaCE : ifcond BCE elsecond BCE ENDIF",
            "sentenciaCE : whilecond BCE",
            "ifcond : IF condicioncparentesis",
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
            "factor : ID parametros",
            "parametros : '(' ID ';' lista_permisos ')'",
            "tipo : USLINTEGERPR",
            "tipo : SINGLEPR",
            "lista_permisos : READONLY",
            "lista_permisos : WRITE",
            "lista_permisos : PASS",
            "lista_permisos : WRITE ';' PASS",
            "lista_permisos :",
    };

//#line 353 "g16.y"

    private static final int Rd = 0;
    private static final int Wr = 1;
    private static final int Ps = 2;
    private static final int Wrps = 3;


    public Polaca_Inversa PI = new Polaca_Inversa();
    public Analizador_Lexico al;
    public ArrayList<AnalizadorLexico.Error> errores;
    public static TokenValue ultimoTokenleido;
    public static ArrayList<String> estructuras;
    public String idFun;
    public boolean estoyEnFuncion = false;
    public String idParam;
    public String ambitoActual="@main";
    public String ultimaFuncion;

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
            TokenValue tv = new TokenValue (t.lexema, Analizador_Lexico.cantLN);
            ultimoTokenleido=tv;
            //System.out.println("leyo : "+t.lexema+" 	Numero de token: "+t.nro);
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
        System.out.println("");
        System.out.println("Testing con readonly en la funcion:");
        System.out.println("");
        if(isPermited(Parser.Rd,Parser.Rd))
            System.out.println("Recibi un readonly y la funcion tenia un readonly, lo acepte");
        if(isPermited(Parser.Rd,Parser.Ps))
            System.out.println("Recibi un pass y la funcion tenia readonly, lo acepte");
        if(isPermited(Parser.Rd,Parser.Wr))
            System.out.println("Recibi un write y la funcion tenia readonly, lo acepte");
        if(isPermited(Parser.Rd,Parser.Wrps))
            System.out.println("Recibi un write;pass y la funcion tenia readonly, lo acepte");
        System.out.println("");
        System.out.println("Testing con pass en la funcion:");
        System.out.println("");
        if(isPermited(Parser.Ps,Parser.Ps))
            System.out.println("Recibi un pass y la funcion tenia un pass, lo acepte");
        if(!isPermited(Parser.Ps,Parser.Rd))
            System.out.println("Recibi un readonly y la funcion tenia pass, RECHAZADO");
        if(!isPermited(Parser.Ps,Parser.Wr))
            System.out.println("Recibi un write y la funcion tenia pass, RECHAZADO");
        if(isPermited(Parser.Ps,Parser.Wrps))
            System.out.println("Recibi un write;pass y la funcion tenia pass, lo acepte");
        System.out.println("");
        System.out.println("Testing con write en la funcion:");
        System.out.println("");
        if(isPermited(Parser.Wr,Parser.Wr))
            System.out.println("Recibi un write y la funcion tenia write, lo acepte");
        if(!isPermited(Parser.Wr,Parser.Ps))
            System.out.println("Recibi un pass y la funcion tenia un write, RECHAZADO");
        if(!isPermited(Parser.Wr,Parser.Rd))
            System.out.println("Recibi un readonly y la funcion tenia write, RECHAZADO");
        if(isPermited(Parser.Wr,Parser.Wrps))
            System.out.println("Recibi un write;pass y la funcion tenia write, lo acepte");
        System.out.println("");
        System.out.println("Testing con write;pass en la funcion:");
        System.out.println("");
        if(!isPermited(Parser.Wrps,Parser.Wr))
            System.out.println("Recibi un write y la funcion tenia write;pass, RECHAZADO");
        if(!isPermited(Parser.Wrps,Parser.Ps))
            System.out.println("Recibi un pass y la funcion tenia un write;pass, RECHAZADO");
        if(!isPermited(Parser.Wrps,Parser.Rd))
            System.out.println("Recibi un readonly y la funcion tenia write;pass, RECHAZADO");
        if(isPermited(Parser.Wrps,Parser.Wrps))
            System.out.println("Recibi un write;pass y la funcion tenia write;pass, lo acepte");
        System.out.println("");
        System.out.println("Testing con noseusaelparametro en la funcion:");
        System.out.println("");
    }
    //#line 466 "Parser.java"
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
//#line 34 "g16.y"
                { registrarTipo( val_peek(1).sval, val_peek(2).sval); /*System.out.println($2.sval);*/
                    Parser.estructuras.add("Se detecto la declaracion de variables en la linea "+Analizador_Lexico.cantLN+"\r\n");}
                break;
                case 7:
//#line 36 "g16.y"
                {


                    Parser.estructuras.add("Se detecto la declaracion de una funcion en la linea "+Analizador_Lexico.cantLN+"\r\n");}
                break;
                case 8:
//#line 50 "g16.y"
                { estoyEnFuncion = true;
                    idFun = val_peek(0).sval;

                    Token t=Analizador_Lexico.tablaSimbolos.get(val_peek(0).sval); this.ambitoActual=this.ambitoActual+"@"+val_peek(0).sval;
                    if(t.declarada){
                        this.errores.add(new ErrorG("Error SIN NUMERO: El identificador '"+t.lexema+"' ,de uso '"+t.uso+"' ya esta declarado", Analizador_Lexico.cantLN));
                        PI.inicioFuncion(val_peek(0).sval);
                    }
                    else{
                        t.declarada=true;
                        PI.inicioFuncion(val_peek(0).sval);
                        t.uso="funcion";
                        t.tipo=val_peek(1).sval;
                    }
                }
                break;
                case 9:
//#line 68 "g16.y"
                {
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
                            this.errores.add(new ErrorG("Error SIN NUMERO : El identificador '"+t.lexema+"' de tipo '"+t.uso+"' no puede ser redeclarado", Analizador_Lexico.cantLN));
                    }
                    else
                        System.out.println("El token que quisiste recuperar es null (ndmpp)"); }
                break;
                case 10:
//#line 89 "g16.y"
                {

                    this.ambitoActual=Analizador_Lexico.cortarAmbito(this.ambitoActual);}
                break;
                case 15:
//#line 103 "g16.y"
                { registrarTipo( val_peek(1).sval, val_peek(2).sval); /*System.out.println($2.sval);*/
                    Parser.estructuras.add("Se detecto la declaracion de variables en la linea "+Analizador_Lexico.cantLN+"\r\n");}
                break;
                case 16:
//#line 107 "g16.y"
                { this.errores.add(new ErrorG("Error SIN NUMERO: Se declar? una funcion dentro de otra funcion", Analizador_Lexico.cantLN));}
                break;
                case 17:
//#line 114 "g16.y"
                { estoyEnFuncion = false;
                    idFun = "None";
                    PI.finFuncion(); }
                break;
                case 18:
//#line 121 "g16.y"
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
                case 19:
//#line 134 "g16.y"
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
                case 25:
//#line 167 "g16.y"
                { PI.put(val_peek(1).sval); }
                break;
                case 26:
//#line 168 "g16.y"
                {
                    PI.put(val_peek(1).sval); }
                break;
                case 27:
//#line 174 "g16.y"
                { Parser.estructuras.add("Se detecto un print en la linea "+Analizador_Lexico.cantLN+"\r\n"); PI.put("print");}
                break;
                case 28:
//#line 175 "g16.y"
                { Parser.estructuras.add("Se detecto una asignacion en  la linea "+Analizador_Lexico.cantLN+"\r\n");}
                break;
                case 29:
//#line 176 "g16.y"
                { Parser.estructuras.add("Se detecto un if en la linea "+Analizador_Lexico.cantLN+"\r\n"); PI.desapilar(); }
                break;
                case 30:
//#line 177 "g16.y"
                { Parser.estructuras.add("Se detecto un if en la linea "+Analizador_Lexico.cantLN+"\r\n"); PI.desapilar();}
                break;
                case 31:
//#line 178 "g16.y"
                { Parser.estructuras.add("Se detecto un while en la linea "+Analizador_Lexico.cantLN+"\r\n"); PI.saltoIncond(); PI.desapilar(); }
                break;
                case 32:
//#line 189 "g16.y"
                { PI.bifurcacion(); }
                break;
                case 33:
//#line 193 "g16.y"
                { PI.desapilarElse(); PI.bifurcacionElse(); }
                break;
                case 34:
//#line 197 "g16.y"
                { PI.bifurcacion(); }
                break;
                case 35:
//#line 201 "g16.y"
                { PI.setSaltoIncond(); }
                break;
                case 37:
//#line 216 "g16.y"
                { PI.put(val_peek(1).sval); }
                break;
                case 38:
//#line 223 "g16.y"
                { yyval.sval = "<"; }
                break;
                case 39:
//#line 224 "g16.y"
                { yyval.sval = ">"; }
                break;
                case 40:
//#line 225 "g16.y"
                { yyval.sval = "<="; }
                break;
                case 41:
//#line 226 "g16.y"
                { yyval.sval = ">="; }
                break;
                case 42:
//#line 227 "g16.y"
                { yyval.sval = "=="; }
                break;
                case 43:
//#line 228 "g16.y"
                { yyval.sval = "!="; }
                break;
                case 44:
//#line 232 "g16.y"
                {
                    if ( val_peek(2).sval.equals(idParam) )
                        if ( Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun == Ps )
                            Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun = Wrps;
                        else
                        if ( isPermited(Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun, Wr) )
                            Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun = Wr;


                    Token t=Analizador_Lexico.tablaSimbolos.get(val_peek(2).sval); PI.put(val_peek(2).sval); PI.put(":=");
                    if(t!=null){/*Primero me fijo si esta declarada*/
                        if(t.declarada==false)
                            this.errores.add(new ErrorG("Error 35 : La variable "+val_peek(2).sval+" no esta declarada ", Analizador_Lexico.cantLN));
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
                case 45:
//#line 258 "g16.y"
                { PI.put("+"); }
                break;
                case 46:
//#line 259 "g16.y"
                { PI.put("-"); }
                break;
                case 48:
//#line 263 "g16.y"
                { PI.put("*"); }
                break;
                case 49:
//#line 264 "g16.y"
                { PI.put("/"); }
                break;
                case 51:
//#line 268 "g16.y"
                { PI.put(val_peek(0).sval);
                    Token t=Analizador_Lexico.tablaSimbolos.get(val_peek(0).sval);
                    if(t!=null){
                        if(t.declarada==false)/*Primero me fijo si esta declarada*/
                            this.errores.add(new ErrorG("Error 34 : La variable "+val_peek(0).sval+" no esta declarada ", Analizador_Lexico.cantLN));
                        else{
                            t=Analizador_Lexico.getEntradaTS(val_peek(0).sval,this.ambitoActual);
                            if(t==null)/*Despues me fijo si esta en el ambito*/
                                this.errores.add(new ErrorG("Error 34 : El identificador "+val_peek(0).sval+" no esta en el ambito "+this.ambitoActual, Analizador_Lexico.cantLN));
                        }
                    }
                    else
                        System.out.println(" No esta en la tabla de simbolos ndmpp ");
                }
                break;
                case 52:
//#line 283 "g16.y"
                { PI.put(val_peek(0).sval); }
                break;
                case 53:
//#line 284 "g16.y"
                { PI.put(val_peek(0).sval); }
                break;
                case 54:
//#line 285 "g16.y"
                {	Token t=Analizador_Lexico.tablaSimbolos.get(val_peek(0).sval);/*Este no es con ambito*/
                    t.lexema="-"+t.lexema; PI.put("-" + val_peek(1).sval);}
                break;
                case 55:
//#line 287 "g16.y"
                {
                    if ( estoyEnFuncion ){
                        System.out.println(idParam + " contra " + val_peek(0).sval);
                        if ( idParam.equals(val_peek(0).sval) ){
                            System.out.println("entro");
                            if ( isPermited(Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun, val_peek(0).ival) )
                                Analizador_Lexico.tablaSimbolos.get(idFun).permisoFun = val_peek(0).ival;
                        }
                    }
                    System.out.println("CantLN: " + Analizador_Lexico.cantLN);
                    System.out.println(Analizador_Lexico.tablaSimbolos.get(val_peek(1).sval).permisoFun + " versus " + val_peek(0).ival);
                    if ( !isPermited(Analizador_Lexico.tablaSimbolos.get(val_peek(1).sval).permisoFun, val_peek(0).ival) )
                        this.errores.add(new ErrorG("Error SIN NUMERO : La funcion "+val_peek(1).sval+" no puede ser invocada con ese permiso ", Analizador_Lexico.cantLN));
                    else{
                        /*System.out.println("Permiso aceptado");*/
                    }
                    PI.jumpToFun(val_peek(1).sval); Token t=Analizador_Lexico.tablaSimbolos.get(val_peek(1).sval);
                    if(t!=null){
                        if(t.declarada==false)
                            this.errores.add(new ErrorG("Error 34.6 : La funcion "+val_peek(1).sval+" no esta declarada ", Analizador_Lexico.cantLN));
                        else
                        if(t.uso!="funcion")
                            this.errores.add(new ErrorG("Error 34.7 : El identificador "+t.lexema+" no es una funcion. ", Analizador_Lexico.cantLN));
                    }
                    else
                        System.out.println("El identificador "+val_peek(1).sval+" no se agrego a la tabla de simbolos (El identificador es una funcion) (ndmpp)"); }
                break;
                case 56:
//#line 315 "g16.y"
                {
                    yyval.ival = val_peek(1).ival; yyval.sval = val_peek(3).sval;

                    PI.put(val_peek(3).sval);
                    Token t=Analizador_Lexico.tablaSimbolos.get(val_peek(3).sval);
                    if(t!=null){/*Primero me fijo si esta declarada*/
                        if(t.declarada==false)
                            this.errores.add(new ErrorG("Error 35: La variable "+val_peek(3).sval+" no esta declarada ", Analizador_Lexico.cantLN));
                        else{
                            t=Analizador_Lexico.getEntradaTS(val_peek(3).sval,this.ambitoActual);
                            if(t==null)/*Despues me fijo si esta en el ambito*/
                                this.errores.add(new ErrorG("Error 34 : El identificador "+val_peek(3).sval+" no esta en el ambito "+this.ambitoActual, Analizador_Lexico.cantLN));
                        }
                    }
                    else
                        System.out.println(" No esta en la tabla de simbolos ndmpp ");
                }
                break;
                case 59:
//#line 342 "g16.y"
                { yyval.ival = Rd; }
                break;
                case 60:
//#line 343 "g16.y"
                { yyval.ival = Wr; }
                break;
                case 61:
//#line 344 "g16.y"
                { yyval.ival = Ps; }
                break;
                case 62:
//#line 345 "g16.y"
                { yyval.ival = Wrps; }
                break;
//#line 928 "Parser.java"
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
