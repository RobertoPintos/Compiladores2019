package AccionesSemanticas;
import Lexico.Controller;

public class AS4 extends AccSemantica{
	
	//ACCION SEMANTICA QUE FINALIZA LA CADENA DE CARACTERES Y LA AGREGA A LA LISTA DE TOKENS Y SIMBOLOS
	
	public int ejecutar (char c, Controller ct) {
		String seq = ct.getBuffer();
		ct.setBuffer(ct.getBuffer()+c);
		ct.addTokenListCompuesto(seq, ct.getNroLinea());
		ct.addTokenTS(seq, "CHARSEQ");
		return 0;
	}

}
