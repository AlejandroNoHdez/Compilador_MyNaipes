//import analizadorLexico.AnalizadorLexico;
//import analizadorLexico.Token;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import analizador.Analizador;
import analizadorLexico.*; 
public class App
{
      public static void main(String[] args) throws Exception
      {
            /*AnalizadorLexico al = new AnalizadorLexico();
            do
            {
                  Token t = al.explorar();
                  System.out.println(t.toString());
            }
            while(al.continuar());

            System.out.write('\n');*/
            AnalizadorLexico lex = new AnalizadorLexico();
            Analizador analizar = new Analizador(lex);
            analizar.programa();
            System.out.write('\n');
      }
      /*
      Ejemplo
      {bet x; bet y;x=5;y=4; HEART(x >= y){y=2; HEART(x==y){bet k;k=5;}   }}<EOF>

      {}
      
      {bet x; bet y; x = 10; y = 200 + x;}<EOF>

      {
            joker y; 
            y=false; 
            CLOVER(y)
            {
                  bet x; x=x+1;
            }
      }
      <EOF>

      C:\Users\nohbr\Downloads\prueba.txt
      
      */
} 
