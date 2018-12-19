 package AnalizadorLexico;


public class MatrizLexica {

    private final int cantEstados = 21;
    private final int cantCaracteres = 27;

    private CasillaLexica[][] MatrizCL = null;

    public MatrizLexica() {
        MatrizCL = new CasillaLexica[cantEstados][cantCaracteres];
        construirMatriz();
    }

    private void construirMatriz() {
        int i = 0;
        AccionSemantica[] AS = {new AccionSemantica0(), new AccionSemantica1(), new AccionSemantica2(), new AccionSemantica3(), new AccionSemantica4(), new AccionSemantica5(), new AccionSemantica6(), new AccionSemantica7(), new AccionSemantica8(), new AccionSemanticaE(), new AccionSemantica10(), new AccionSemantica11(), new AccionSemantica12()};
        CasillaLexica[] filaLexica;
        //0
        filaLexica = new CasillaLexica[]{c(AS[1], 4), c(AS[1], 16), c(AS[1], 16), c(AS[1], 1), c(AS[7], -1), c(AS[7], -1), c(AS[7], -1), c(AS[7], -1), c(AS[1], 12), c(AS[1], 13), c(AS[1], 14), c(AS[1], 15), c(AS[3], 0), c(AS[1], 18), c(AS[1], 5), c(AS[7], -1), c(AS[7], -1), c(AS[7], -1), c(AS[7], -1), c(AS[1], 17), c(AS[1], 16), c(AS[1], 16), c(AS[7], -1), c(AS[7], -1),c(AS[3], 0),c(AS[3], 0),c(AS[0],0)};
        addFila(filaLexica, i++);
        //1
        filaLexica = new CasillaLexica[]{c(AS[1], 2), c(AS[1], 2), c(AS[1], 2), c(AS[1], 3), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[1], 2), c(AS[1], 2), c(AS[9],-1), c(AS[9],-1),c(AS[9],-1),c(AS[9],-1),c(AS[0],1)};
        addFila(filaLexica, i++);
        //2
        filaLexica = new CasillaLexica[]{c(AS[1], 2), c(AS[1], 2), c(AS[1], 2), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1), c(AS[1], 2), c(AS[1], 2), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1), c(AS[4], -1),c(AS[0],2)};
        addFila(filaLexica, i++);
        //3
        filaLexica = new CasillaLexica[]{c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[3], 0), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3), c(AS[1], 3),c(AS[0],3)};
        addFila(filaLexica, i++);
        //4
        filaLexica = new CasillaLexica[]{c(AS[1], 4), c(AS[9],-1), c(AS[12],9), c(AS[1], 7), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[1], 6), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1),c(AS[9],-1),c(AS[9],-1),c(AS[0],4)};
        addFila(filaLexica, i++);
        //5
        filaLexica = new CasillaLexica[]{c(AS[1], 6), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1),c(AS[9],-1),c(AS[9],-1),c(AS[0],5)};
        addFila(filaLexica, i++);

        //6
        filaLexica = new CasillaLexica[]{c(AS[1], 6), c(AS[6], -1), c(AS[12], 9), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1),c(AS[0],6)};
        addFila(filaLexica, i++);
        //7
        filaLexica = new CasillaLexica[]{c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[1], 8), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1),c(AS[9],-1),c(AS[9],-1),c(AS[0],7)};
        addFila(filaLexica, i++);
        //8
        filaLexica = new CasillaLexica[]{c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[5], -1), c(AS[9],-1), c(AS[9],-1),c(AS[9],-1),c(AS[9],-1),c(AS[0],8)};
        addFila(filaLexica, i++);
        //9
        filaLexica = new CasillaLexica[]{c(AS[1], 11), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[1], 10), c(AS[1], 10), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1),c(AS[9],-1),c(AS[9],-1),c(AS[0],9)};
        addFila(filaLexica, i++);
        //10
        filaLexica = new CasillaLexica[]{c(AS[1], 11), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1),c(AS[9],-1),c(AS[9],-1),c(AS[0],10)};
        addFila(filaLexica, i++);
        //11
        filaLexica = new CasillaLexica[]{c(AS[1], 11), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1), c(AS[6], -1),c(AS[0],11)};
        addFila(filaLexica, i++);
        //12
        filaLexica = new CasillaLexica[]{c(AS[10], -1), c(AS[10], -1), c(AS[10], -1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[8], -1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10], -1), c(AS[10], -1), c(AS[10],-1), c(AS[10],-1),c(AS[10],-1),c(AS[10],-1),c(AS[0],12)};
        addFila(filaLexica, i++);
        //13
        filaLexica = new CasillaLexica[]{c(AS[10], -1), c(AS[10], -1), c(AS[10], -1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[8], -1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10], -1), c(AS[10], -1), c(AS[10],-1), c(AS[10],-1),c(AS[10],-1),c(AS[10],-1),c(AS[0],13)};
        addFila(filaLexica, i++);
        //14
        filaLexica = new CasillaLexica[]{c(AS[10], -1), c(AS[10], -1), c(AS[10], -1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[8], -1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10],-1), c(AS[10], -1), c(AS[10], -1), c(AS[10],-1), c(AS[10],-1),c(AS[10],-1),c(AS[10],-1),c(AS[0],14)};
        addFila(filaLexica, i++);
        //15
        filaLexica = new CasillaLexica[]{c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[8], -1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1),c(AS[9],-1),c(AS[9],-1),c(AS[0],15)};
        addFila(filaLexica, i++);
        //16
        filaLexica = new CasillaLexica[]{c(AS[2], -1), c(AS[1], 16), c(AS[1], 16), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1), c(AS[1], 16), c(AS[1], 16), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1), c(AS[2], -1),c(AS[0],16)};
        addFila(filaLexica, i++);
        //17
        filaLexica = new CasillaLexica[]{c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[8], -1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1),c(AS[9],-1),c(AS[9],-1),c(AS[0],17)};
        addFila(filaLexica, i++);
        //18
        filaLexica = new CasillaLexica[]{c(AS[1], 18), c(AS[1], 18), c(AS[1], 18), /*Recien cambiado1*/c(AS[1],18), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[0], 19), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[8], -1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[1],18), c(AS[1], 18), c(AS[1], 18), c(AS[9],-1), c(AS[9],-1),c(AS[1], 18),c(AS[1], 18),c(AS[0],18)};
        addFila(filaLexica, i++);
        //19
        filaLexica = new CasillaLexica[]{c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[11], 20), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1),c(AS[9],-1),c(AS[9],-1),c(AS[0],19)};
        addFila(filaLexica, i++);
        //20
        filaLexica = new CasillaLexica[]{c(AS[1], 18), c(AS[1], 18), c(AS[1], 18), /*Recien cambiado1*/c(AS[1],18), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[0], 19), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[11],-1), c(AS[8], -1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[9],-1), c(AS[1],18), c(AS[1], 18), c(AS[1], 18), c(AS[9],-1), c(AS[9],-1),c(AS[0], 20),c(AS[0], 20),c(AS[0],18)};
        addFila(filaLexica, i++);
    }
/*Recien cambiado 1: Es para que las cadenas admitan _ en su formacion*/

    private CasillaLexica c(AccionSemantica a, int e) {//Retorna una instancia de CasillaLexica
        return new CasillaLexica(a, e);
    }

    private void addFila(CasillaLexica[] valores, int fila) {
        if (fila < cantEstados) {
            for (int i = 0; i < cantCaracteres; i++) {
                MatrizCL[fila][i] = valores[i];
            }
        }
    }

    private void add(CasillaLexica valor, int i, int j) {
        if (verificarIndices(i, j))
            MatrizCL[i][j] = valor;
    }

    public CasillaLexica get(int i, int j) {
        if (verificarIndices(i, j))
            return MatrizCL[i][j];
        return null;
    }

    public AccionSemantica getAS(int i, int j) {
        if (verificarIndices(i, j))
            return MatrizCL[i][j].AS;
        return null;
    }

    public int getEstado(int i, int j) {
        if (verificarIndices(i, j))
            return MatrizCL[i][j].estado;
        return -1;
    }

    public int getSiguienteEstado(int estadoActual, char c) {
        return MatrizCL[estadoActual][c].estado;
    }

    private boolean verificarIndices(int i, int j) {
        if (i < cantEstados && j < cantCaracteres)
            if (i > -1 && j > -1)
                return true;
        return false;
    }
}