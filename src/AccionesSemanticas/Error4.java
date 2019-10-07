package AccionesSemanticas;
import Lexico.Controller;

public class Error4 extends AccSemantica{

	//ACC SEMANTICA QUE DETECTA ERROR EN FLOAT, CUANDO VIENE SOLO UN ".", LUEGO ES NECESARIO UN DIGITO
	
	public int ejecutar (char c, Controller ct) {
		
		if(!(Character.isDigit(c)))
			ct.addError("Error: Se esperaba un digito en float y vino un: "+c, ct.getNroLinea());
		return 1;
	}
}
