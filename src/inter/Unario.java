package inter;
import analizadorLexico.*; import simbolos.*;
public class Unario extends Op{
    public Expr expr;
    public Unario(Token tok, Expr x) { // maneja el menos, para ! vea Not -1, !true
      super(tok, null);  expr = x;
      tipo = Tipo.max(Tipo.Bet, expr.tipo);
      if (tipo == null ) error("error de tipo");
   }
   public Expr gen() { 
      return new Unario(op, expr.reducir()); 
   }
   public String toString() { 
      return op.toString()+" "+expr.toString(); 
   }
}
