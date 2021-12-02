package inter;
import analizadorLexico.*; import simbolos.*;
public class Op extends Expr {
    public Op(Token tok, Tipo p) { //E = E1 + E2 => x = 10 + 5.7
        super(tok, p); 
    }
    public Expr reducir() {
        Expr x = gen();
        Temp t = new Temp(tipo);
        emitir( t.toString() + " = " + x.toString() );
        return t;
    } 
}
