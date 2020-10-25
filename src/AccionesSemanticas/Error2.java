package AccionesSemanticas;
import Lexico.Controller;

public class Error2 extends AccSemantica{
	
	//ERROR AL ESPERAR UN '=' EN EL LEXEMA DE ASIGNACION, PERO LLEGA OTRO CARACTER
	
	public int ejecutar (char c, Controller ct) {
		String aux = String.valueOf(c);
		if (!(aux.equals("=")))
			ct.addError("Error: Se esperaba un '=' pero llego un: " +c, ct.getNroLinea());
		return 1;
	}
}
