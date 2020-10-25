package AccionesSemanticas;
import Lexico.Controller;

public class Error1 extends AccSemantica{
	
	//ERROR SALTO DE LINEA EN UNA SEQUENCIA DE CARACTERES
	
	public int ejecutar (char c, Controller ct) {
		ct.addError("Error: Salto de linea en cadena de caracteres.", ct.getNroLinea());
		return 1;
	}
}
