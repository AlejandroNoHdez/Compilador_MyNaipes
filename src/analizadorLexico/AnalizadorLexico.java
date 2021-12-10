package analizadorLexico;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;

import simbolos.*;

public class AnalizadorLexico 
{
    public static int linea = 1;    //cuenta las lineas de entrada
    char preanalisis = ' ';         //guarda siguiente caracter de entrada
    Hashtable <String,Palabra>palabras = new Hashtable<String,Palabra>();//INICIA: Palabras reservadas
    static int cont = 0;
    static String dato = "", lectura;
    static char [] datos;
    static boolean flag=false;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    BufferedReader archivos; 
    
    void reservar(Palabra w) 
    { 
        palabras.put(w.lexema, w); 
    }
    /**
     * 18 - 22 revisa las palabras clave seleccionadas
     * 24-26 revisa los loexemas para los objetos definis en cualquier otra parte
     */
    public AnalizadorLexico() 
    {
        reservar( new Palabra("HEART",Etiqueta.HEART) ); 
        reservar( new Palabra("TRIO", Etiqueta.TRIO) ); 
        reservar( new Palabra("CLOVER", Etiqueta.CLOVER) ); 
        reservar( new Palabra("FULL", Etiqueta.FULL) );
        reservar( new Palabra("REALSTAIR", Etiqueta.REALSTAIR) );
        reservar(Palabra.cc); reservar(Palabra.ccb);
        reservar( Palabra.q ); reservar( Palabra.k );
        reservar( Tipo.Bet ); reservar( Tipo.Queen );
        reservar( Tipo.Joker ); reservar( Tipo.King );
        reservar(Palabra.ccbc);
    }                               //TERMIA: Palabras reservadas

    public void bandera(String v){
        flag=true;
        lectura=v;
    }
    
    void readch() throws IOException 
    {
        if(flag){
            dato=lectura;
        }else{
            if(cont == 0)
            {
                while(!(lectura = br.readLine()).contains("<EOF>"))
                {
                    if(lectura.contains(".txt")){
                       archivos=new BufferedReader(new FileReader(lectura));
                       while((lectura = archivos.readLine()) != null){
                           dato=dato+lectura;
                       }
                       break;
                    }else{
                        dato = dato + lectura;
                    }
                }
                if(!dato.contains("<EOF>")){
                    dato = dato + lectura;
                }
            } 
        }
        datos = dato.toCharArray();
        if(cont < datos.length)
        {
            preanalisis = datos[cont];
            cont++;
        }
    }

    boolean readch(char c) throws IOException 
    {
        readch();
        if( preanalisis != c ) return false;
        preanalisis = ' ';
        return true;
    }

    public Token explorar() throws IOException 
    {
        for( ; ; readch() ) 
        {               //Omite espacios en blanco
           if( preanalisis == ' ' || preanalisis == '\t' ) continue;
           else if( preanalisis == '\n' ) linea = linea + 1;
           else break;
        }
        switch( preanalisis )
        {
            case '&':
                        if( readch('&') ) return Palabra.doublepar; else return new Token('&');
            case '|':
                        if( readch('|') ) return Palabra.poker; else return new Token('|');
            case '=':
                        if( readch('=') ) return Palabra.par; else return new Token('=');
            case '!':
                        if( readch('=') ) return Palabra.color; else return new Token('!');
            case '<':
                        if( readch('=') ) return Palabra.j; else return new Token('<');
            case '>':
                        if( readch('=') ) return Palabra.a; else return new Token('>');
            case '*':
                        if( readch('/') ) return Palabra.ccbc;
                        else return new Token('*');
            case '/':
                        if( readch('/') ) return Palabra.cc;
                        else if(readch('*')) return Palabra.ccb;
                        else return new Token('/');
            case 'A': 
                        return Palabra.a;
        }
        if( Character.isDigit(preanalisis) ) 
        {   //Acumula el valor entero de la secuencia de los digitos
            int v = 0;
            do
            {
                v = 10*v + Character.digit(preanalisis, 10); 
                readch();
            } 
            while ( Character.isDigit(preanalisis) );

            if( preanalisis != '.') 
                return new Num(v);
            float x = v; //10
            float d = 10;
            for(;;) 
            {
                readch();//5
                if( ! Character.isDigit(preanalisis) ) break;
                x = x + Character.digit(preanalisis, 10) / d; 
                d = d*10;
            }
            return new Real(x);
        }
        if( Character.isLetter(preanalisis) ) { StringBuffer b = new StringBuffer(); 
            do
            {
                b.append(preanalisis); readch();
            } 
            while( Character.isLetterOrDigit(preanalisis) );

            String s = b.toString();
            Palabra w = (Palabra)palabras.get(s);
            if( w != null ) return w;
            w = new Palabra(s, Etiqueta.ID);
            palabras.put(s, w);
            return w;
        }
        Token tok = new Token(preanalisis); preanalisis = ' ';
        return tok; 
    }
    public boolean continuar()
    {
        if(preanalisis !='\n')
            return true;
        else
            return false;
    }

    /*public String getPalabras()
    {
        String r = "";
        for(int i = 0; i < palabras.size() ; i++)
        {
            r+= palabras.get(i)+"/n";
        } 
        return r;
    }*/
}