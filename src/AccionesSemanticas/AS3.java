package AccionesSemanticas;
import Lexico.Controller;

public class AS3 extends AccSemantica{
	
	//ACCION SEMANTICA QUE SOLO CONCATENA EL CARACTER LEIDO
	public int ejecutar (char c, Controller ct) {
		
		String viejo = ct.getBuffer();
		ct.setBuffer(viejo+c);
		return 0;
	
	}
}
