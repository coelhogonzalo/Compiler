package AnalizadorLexico;//asd

import AnalizadorLexico.AccionSemantica;

public class CasillaLexica {
    public AccionSemantica AS = null;
    public int estado = 0;

    public CasillaLexica(AccionSemantica AS, int estado) {
        this.AS = AS;
        this.estado = estado;
    }
}
