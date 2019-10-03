package AccionesSemanticas;
import Lexico.Controller;

public class AS1 extends AccSemantica{

	//ACCION SEMANTICA QUE INICIALIZA EL STRING Y CONCATENA EL CARACTER
	public int ejecutar (char c, Controller ct) {
		ct.inicializarBuffer(c);
		return 0;
	}
}
