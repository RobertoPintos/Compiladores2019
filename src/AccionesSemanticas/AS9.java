package AccionesSemanticas;
import Lexico.Controller;

public class AS9 extends AccSemantica{

	//ACC SEMANTICA QUE VALIDA UN TOKEN SIMPLE, CASO DE LOS < > E =
	
	public int ejecutar (char c, Controller ct) {
		
		ct.addTokenListSimple(ct.getBuffer().charAt(0), ct.getNroLinea());
		ct.setEstadoFinal();
		return 0;
	}
}
