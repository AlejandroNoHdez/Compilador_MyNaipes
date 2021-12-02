package analizadorLexico;

public class Palabra extends Token
{
      public String lexema = "";
      
      public Palabra(String s, int etiqueta)
      {
            super(etiqueta);
            lexema = s;
      }

      public String toString()
      {
            return lexema;
      }

      public static final Palabra
            doublepar = new Palabra("&&", Etiqueta.DOUBLEPAR),
            poker = new Palabra("||", Etiqueta.POKER),
            par = new Palabra("==", Etiqueta.PAR),
            color = new Palabra("!=", Etiqueta.COLOR),
            j = new Palabra("<=", Etiqueta.J),
            a = new Palabra(">=", Etiqueta.A),
            stair = new Palabra("minus", Etiqueta.STAIR),
            q = new Palabra("true", Etiqueta.Q),
            k = new Palabra("false", Etiqueta.K),
            temp = new Palabra("t", Etiqueta.TEMP),
            cc = new Palabra("//", Etiqueta.CC),
            ccb = new Palabra("/*", Etiqueta.CCB),
            ccbc = new Palabra("*/", Etiqueta.CCBC);
}
