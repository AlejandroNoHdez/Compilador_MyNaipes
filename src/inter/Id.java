package inter;
import analizadorLexico.*; import simbolos.*;
public class Id extends Expr{
    public int desplazamiento; // direccion relativa
    public Id(Palabra id, Tipo p, int b) { 
        super(id, p); 
        desplazamiento = b; 
    }
}
