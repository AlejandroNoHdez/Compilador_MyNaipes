package analizadorLexico;

public class Num extends Token
{
      public final int valor;

      public Num(int v)
      {
            super(Etiqueta.JOT);
            valor = v;
      }

      public String toString()
      {
            return "" + valor;
      }
}
