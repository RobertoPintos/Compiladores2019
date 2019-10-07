package AccionesSemanticas;
import Lexico.Controller;

public class Error3 extends AccSemantica{

	// ACC SEMANTICA QUE REGISTRA ERROR AL TRATAR DE AGREGAR CUALQUIER CARACTER DESPUES DE LA "E" EN FLOAT
	
	public int ejecutar (char c, Controller ct) {
		
		String aux = String.valueOf(c);
		if (!(aux.equals("+")) || !(aux.equals("-")) || !(Character.isDigit(c))) {
			ct.addError("Error: Caracter invalido en flotante luego de la E: " +c, ct.getNroLinea());
		}
		return 1;
	}
}
