package analizador;

import java.io.IOException;

import analizadorLexico.*;
import inter.*;
import simbolos.*;

public class Analizador {
	private AnalizadorLexico lex;
	private Token busca;
	Ent sup = null;
	int usado = 0;
	String codintermmedio = null;
	
	public Analizador(AnalizadorLexico l) throws IOException { 
		lex = l; mover(); 
	}
	void mover() throws IOException { 
		busca = lex.explorar(); 
	}
	void error(String s) { 
		throw new Error("cerca de linea "+lex.linea+": "+s); 
	}
	void coincidir(int t) throws IOException {
	//analizador lexico para este analizador sintactico
	// marca de busqueda por adelantado
	// tabla de simbolos actual o superior
	// almacenamiento usado para las declaraciones
		if( busca.etiqueta == t ) mover();
		else error("error de sintaxis");
	}

	public void programa() throws IOException {  // programa −> bloque
		Instr s = bloque();
		int inicio = s.nuevaEtiqueta(); int despues = s.nuevaEtiqueta();
		s.emitirEtiqueta(inicio);  s.gen(inicio, despues);  s.emitirEtiqueta(despues);
	}
	Instr bloque() throws IOException {  // bloque −> { decls instrs }
		coincidir('{'); 
		Ent entGuardado = sup; 
		sup = new Ent(sup);
		decls(); 
		Instr s = instrs();
		coincidir('}'); 
		sup = entGuardado;
		return s; 
	}
	void decls() throws IOException {
		while( busca.etiqueta == Etiqueta.BASIC ) { // D −> tipo ID;
			Tipo p = tipo(); 
			Token tok = busca; 
			coincidir(Etiqueta.ID); 
			coincidir(';');
			Id id = new Id((Palabra)tok, p, usado);
			sup.put( tok, id );
			usado = usado + p.anchura;
		} 
	}
	
	Tipo tipo() throws IOException {
		Tipo p = (Tipo)busca;
		coincidir(Etiqueta.BASIC);
		if( busca.etiqueta != '[' ) 
            return p; // T −> basico
		else return dims(p); // devuelve el tipo del arreglo
	}
	
	Tipo dims(Tipo p) throws IOException {
		coincidir('['); 
		Token tok = busca; 
		coincidir(Etiqueta.JOT); 
		coincidir(']');
		if( busca.etiqueta == '[' )
			p = dims(p);
		return new Arreglo(((Num)tok).valor, p);
	}

	Instr instrs() throws IOException {
		if ( busca.etiqueta == '}' ) 
			return Instr.Null;
		else 
			return new Sec(instr(), instrs());
	}

	Instr instr() throws IOException {
		Expr x; Instr s, s1, s2;
		Instr instrGuardada;
		switch( busca.etiqueta ) {
			case ';':
				mover();
				return Instr.Null;
		case Etiqueta.HEART:
			coincidir(Etiqueta.HEART); coincidir('('); x = bool(); coincidir(')');
			s1 = instr();
			if( busca.etiqueta != Etiqueta.TRIO ) return new If(x, s1);
			coincidir(Etiqueta.CLOVER);
			s2 = instr();
			return new Else(x, s1, s2);
		case Etiqueta.CLOVER:
			While nodowhile = new While();
			instrGuardada = Instr.Circundante; Instr.Circundante = nodowhile;
			coincidir(Etiqueta.CLOVER); coincidir('('); 
			x = bool(); coincidir(')');
			s1 = instr();
			nodowhile.inic(x, s1);
			Instr.Circundante = instrGuardada; // restablece Instr.Circundante
			return nodowhile;
		case Etiqueta.FULL:
			Do nododo = new Do();
			instrGuardada = Instr.Circundante; Instr.Circundante = nododo;
			coincidir(Etiqueta.FULL);
			s1 = instr();
			coincidir(Etiqueta.CLOVER); coincidir('('); 
			x = bool(); coincidir(')');
			coincidir(';');
			nododo.inic(s1, x);
			Instr.Circundante = instrGuardada; // restablece Instr.Circundante
			return nododo;
		case Etiqueta.REALSTAIR:
			coincidir(Etiqueta.REALSTAIR); coincidir(';');
			return new Break();
		case '{':		
			return bloque();
		default:
			return asignar();
		}
	}

	Instr asignar() throws IOException {
		Instr instr; Token t = busca;
		coincidir(Etiqueta.ID);
		Id id = sup.get(t);
		if( id == null ) 
				error(t.toString() + " no declarado");
		if( busca.etiqueta == '=' ) { // S −> id = E ;
			mover(); instr = new Est(id, bool());
		}
		else { // S −> L = E ;
			Acceso x = desplazamiento(id);
			coincidir('='); instr = new EstElem(x, bool());
		}
		coincidir(';');
		return instr;
	}

	Expr bool() throws IOException {
		Expr x = unir();
		while( busca.etiqueta == Etiqueta.POKER ) {
			Token tok = busca; mover(); x = new Or(tok, x, unir());
		}
		return x;
	}
	
	Expr unir() throws IOException {
		Expr x = igualdad();
		while( busca.etiqueta == Etiqueta.DOUBLEPAR) {
			Token tok = busca; mover(); x = new And(tok, x, igualdad());
		}
		return x;
	}

	Expr igualdad() throws IOException {
		Expr x = rel();
		while( busca.etiqueta == Etiqueta.PAR || busca.etiqueta == Etiqueta.A ) {
			Token tok = busca; mover(); x = new Rel(tok, x, rel());
		}
		return x;
	}

	Expr rel() throws IOException {
		Expr x = expr();
		switch( busca.etiqueta ) {
			case '<': 
			case Etiqueta.STAIR: 
			case Etiqueta.COLOR: 
			case '>':
				Token tok = busca; mover(); 
				return new Rel(tok, x, expr());
			default:
				return x;
		} 
	}
	
	Expr expr() throws IOException {
		Expr x = term();
		while( busca.etiqueta == '+' || busca.etiqueta == '−' ) {
			Token tok = busca; mover(); 
			x = new Arit(tok, x, term());
		}
		return x; 
	}
		
	Expr term() throws IOException {
		Expr x = unario();
		while(busca.etiqueta == '*' || busca.etiqueta == '/') {
			Token tok = busca; mover(); 
			x = new Arit(tok, x, unario());
		}
		return x; 
	}
		
	Expr unario() throws IOException {
		if( busca.etiqueta == '−' ) {
			mover();  
			return new Unario(Palabra.j, unario());
		}
		else if( busca.etiqueta == '!' ) {
			Token tok = busca; 
			mover(); 
			return new Not(tok, unario());
		}
		else return factor();
	}
		
	Expr factor() throws IOException {
		Expr x = null;
		switch( busca.etiqueta ) {
			case '(':
				mover(); 
				x = bool(); 
				coincidir(')');
				return x;
			case Etiqueta.JOT:
				x = new Constante(busca, Tipo.Bet);
                mover(); return x;
			case Etiqueta.REAL:
				x = new Constante(busca, Tipo.King);
                mover(); return x;
			case Etiqueta.Q:
				x = Constante.True;
                mover(); return x;
			case Etiqueta.K:
				x = Constante.False;
                mover(); return x;
			default:
				error("error de sintaxis");
				return x;
			case Etiqueta.ID:
				String s = busca.toString();
				Id id = sup.get(busca);
				if( id == null ) error(busca.toString() + " no declarado");
					mover();
				if (busca.etiqueta != '[' ) 
					return id;
				else 
					return desplazamiento(id);
		}
	}
			
	Acceso desplazamiento(Id a) throws IOException {
		Expr i; 
        Expr w;
        Expr t1, t2; 
        Expr ubic;
		Tipo tipo = a.tipo;
		coincidir('['); i = bool(); coincidir(']');
		tipo = ((Arreglo)tipo).de;
		// I −> [E] | [E] I
		// hereda id
		// primer indice, I −> [ E ]
		w = new Constante(tipo.anchura);
		t1 = new Arit(new Token('*'), i, w);
		ubic = t1;
		while( busca.etiqueta == '[' ) {        // multi−dimensional I −> [ E ] I
			coincidir('['); 
			i = bool(); 
			coincidir('[');
			tipo = ((Arreglo)tipo).de;
			w = new Constante(tipo.anchura);
			t1 = new Arit(new Token('*'), i, w);
			t2 = new Arit(new Token('+'), ubic, t1);
			ubic = t2; 
		}
		return new Acceso(a, ubic, tipo);	
    }
}
