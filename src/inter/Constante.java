package inter; // Archivo Constante.java 
import analizadorLexico.*; import simbolos.*;
public class Constante extends Expr {
   public Constante(Token tok, Tipo p) { 
      super(tok, p);
   }
   public Constante(int i) { 
      super(new Num(i), Tipo.Bet); 
   }
   public static final Constante
      True  = new Constante(Palabra.q,  Tipo.Joker),
      False = new Constante(Palabra.k, Tipo.Joker);
   
      public void salto(int t, int f) {
         if ( this == True && t != 0 ) emitir("goto L" + t);
         else if ( this == False && f != 0) emitir("goto L" + f);
      }
}