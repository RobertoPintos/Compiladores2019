package AccionesSemanticas;
import Lexico.Controller;

public class AS7 extends AccSemantica{
	
	//ACC SEMANTICA QUE RECONOCE ENTEROS Y VERIFICA EL RANGO
	
	public int ejecutar (char c, Controller ct) {
		
		String bf = ct.getBuffer();
		int i = Integer.parseInt(bf);
		if ((i <= ct.maxE) && (i >= ct.minE)) {
			ct.addTokenListCompuesto(bf, ct.getNroLinea());
			ct.addTokenTS(bf, "CONST INT");
		} else { //ESTA FUERA DEL RANGO
			ct.addError("Error: Constante "+bf+" fuera de rango.", ct.getNroLinea());
			if (i >= ct.maxE) {
				i = ct.maxE;
				String aux = Integer.toString(i);
				ct.addTokenListCompuesto(aux, ct.getNroLinea());
				ct.addTokenTS(aux, "CONST INT");
			}	else {
				i = ct.minE;
				String aux = Integer.toString(i);
				ct.addTokenListCompuesto(aux, ct.getNroLinea());
				ct.addTokenTS(aux, "CONST INT");
			}
		}
		ct.setEstadoFinal();
		ct.setSimboloAnt();
		return 0;
	}
}
