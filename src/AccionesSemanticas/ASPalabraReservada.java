package AccionesSemanticas;

import Lexico.Controller;

public class ASPalabraReservada extends AccSemantica{

	@Override
	public int ejecutar(char c, Controller ac) {
		// TODO Auto-generated method stub
		String palabra = ac.getBuffer()+c;
		if(ac.esReservada(palabra))
			return 0;
		else
			// falta ver el ERROR que se genera en este caso
			return 1;
	}

}
