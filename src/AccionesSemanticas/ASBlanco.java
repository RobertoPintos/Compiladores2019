package AccionesSemanticas;
import Lexico.Controller;

public class ASBlanco extends AccSemantica{
	
	//ACCION SEMANTICA PARA ESPACIOS, SALTOS Y TAB
	public int ejecutar (char c, Controller ct) {
		ct.setBuffer("");
		return 0;
	}
}
