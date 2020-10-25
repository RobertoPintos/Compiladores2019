package AccionesSemanticas;
import Lexico.Controller;

public class Error5 extends AccSemantica{

	//ACC SEMANTICA QUE DETECTA ERROR EN APERTURA DE COMENTARIO, CARACTER INVALIDO
	
	public int ejecutar (char c, Controller ct) {
		
		String aux = String.valueOf(c);
		if(!(aux.equals("+")))
			ct.addError("Error: Se esperaba un '+' para iniciar comentario y vino un: "+c, ct.getNroLinea());
		return 1;
	}
}
