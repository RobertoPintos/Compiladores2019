package AccionesSemanticas;
import Lexico.Controller;

public class AS3 extends AccSemantica{
	
	//ACCION SEMANTICA QUE SOLO CONCATENA EL CARACTER LEIDO Y CHEQUEA COMENTARIO SIN CERRAR
	public int ejecutar (char c, Controller ct) {
		
		String viejo = ct.getBuffer();
		ct.setBuffer(viejo+c);
		if (ct.getBuffer().equals("/+"))
			ct.setComent(true);
		if ((ct.getBuffer().charAt(ct.getBuffer().length()-1) == '+') && c == '/')
			ct.setComent(false);			
		if ((ct.getPosFuente() == ct.getFuenteSize()-2) && (ct.getComent())) {
			ct.addError("Comentario sin finalizar. Compilacion abortada.", ct.getNroLinea());
		}
		return 0;
	
	}
}
