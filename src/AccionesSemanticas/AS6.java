package AccionesSemanticas;
import Lexico.Controller;

public class AS6 extends AccSemantica{
	
	//ACC SEMANTICA QUE FINALIZA IDENTIFICADORES O PALABRAS RESERVADAS Y VERIFICA LONGITUD
	
	public int ejecutar (char c, Controller ct) {
		
		//FALTA GUARDAR ULTIMO SIMBOLO
		String bf = ct.getBuffer();
		if (ct.esReservada(bf))
			ct.addTokenListCompuesto(bf, ct.getNroLinea());
		else 
			if (bf.length() <= ct.maxId) {
				ct.addTokenListCompuesto(bf, ct.getNroLinea());
				ct.addTokenTS(bf, "ID");
			} else { //LA LONGITUD ES MAYOR A 25 Y SE TRUNCA Y AGREGA EL WARNING
				String nuevo = bf.substring(0, 24);
				ct.addTokenListCompuesto(nuevo, ct.getNroLinea());
				ct.addTokenTS(nuevo, "ID");
				ct.addWarning("Warning: ID truncado: "+bf, ct.getNroLinea());
			}
		ct.setEstadoFinal();
		return 0;
	}
}
