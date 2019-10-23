package AccionesSemanticas;
import Lexico.Controller;

public class AS11 extends AccSemantica{

	//ACC SEMANTICA QUE DETECTA TOKEN SIMPLE /
	
	public int ejecutar (char c, Controller ct) {
		
		char c1 = ct.getBuffer().charAt(0);
		ct.addTokenListSimple(c1, ct.getNroLinea());
		ct.setSimboloAnt();
		return 0;
	}
}
