package simbolos;
import analizadorLexico.*;

public class Tipo extends Palabra
{
      public int anchura = 0;

      public Tipo(String s, int etiqueta, int w)
      {
            super(s,etiqueta);
            anchura = w;
      }

      public static final Tipo
      Bet = new Tipo("bet", Etiqueta.BASIC, 4),
      King = new Tipo("king", Etiqueta.BASIC, 8),
      Queen = new Tipo("queen", Etiqueta.BASIC, 1),
      Joker = new Tipo("joker", Etiqueta.BASIC, 1);

      public static boolean numerico(Tipo p)
      {
            if(p==Tipo.Queen || p==Tipo.Bet || p==Tipo.King) return true;
            else return false;
      }

      public static Tipo max(Tipo p1, Tipo p2)
      {
            if(! numerico(p1) || ! numerico(p2)) return null;
            else if(p1 == Tipo.King || p2 == Tipo.King) return Tipo.King;
            else if(p1 == Tipo.Bet || p2 == Tipo.Bet) return Tipo.Bet;
            else return Tipo.Queen;
      }
}
