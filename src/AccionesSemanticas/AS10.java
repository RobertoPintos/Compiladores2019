package AccionesSemanticas;
import Lexico.Controller;

public class AS10 extends AccSemantica{
	
	//ACC SEMANTICA QUE DETECTA UN <> Y LO AGREGA A LA LISTA DE TOKENS
	
	public int ejecutar (char c, Controller ct) {
		
		if (c == '>') {
			ct.setBuffer(ct.getBuffer()+c);
			String aux = new String(ct.getBuffer());
			ct.addTokenListCompuesto(aux, ct.getNroLinea());
			ct.setEstadoFinal();
		}
		return 0;
	}
}
