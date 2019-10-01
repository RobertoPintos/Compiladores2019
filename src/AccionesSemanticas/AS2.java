package AccionesSemanticas;
import Lexico.Controller;

public class AS2 extends AccSemantica{
	
	//ACCION SEMANTICA QUE INICIALIZA EL BUFFER PERO ES UN TOKEN SIMPLE ( _ * + / ( ) - ; , )
	
	public int ejecutar (char c, Controller ct) {
		
		ct.inicializarBuffer(c);
		ct.addTokenListSimple(c, ct.getNroLinea());
		ct.setEstadoFinal();
		return 0;
	}
}
