package inter;
import analizadorLexico.*;

public class Nodo {
    int linealex = 0;
    private String codigointermedio = "";
    Nodo() { 
        linealex = AnalizadorLexico.linea;
    }
    void error(String s) {
        throw new Error("cerca de la linea "+linealex+": "+s); 
    }
    static int etiquetas = 0;
    public int nuevaEtiqueta() { 
        return ++etiquetas; 
    }
    public void emitirEtiqueta(int i) {
        codigointermedio += "L" + i + ":";
        System.out.print("L" + i + ":");
    }
    public void emitir(String s) {
        codigointermedio += s + "\n";
        System.out.println("\t" + s);
    }
    public String getCodigoIntermedio()
    {
        return codigointermedio;
    }
}
