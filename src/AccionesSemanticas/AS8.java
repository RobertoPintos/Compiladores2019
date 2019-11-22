package AccionesSemanticas;
import Lexico.Controller;

public class AS8 extends AccSemantica{

	//ACC SEMANTICA QUE VALIDA FLOAT Y CHEQUEA RANGOS
	
	public int ejecutar (char c, Controller ct) {
		
		String bf = ct.getBuffer();
		float valor = Float.parseFloat(bf);
		if (((valor > ct.minPosF) && (valor < ct.maxPosF)) || ((valor > ct.minNegF) && (valor < ct.maxNegF)) || (valor == ct.zeroF)) {
			ct.addTokenListCompuesto(bf, ct.getNroLinea());
			ct.addTokenTS(bf, "CONST FLOAT");
		} else {
			if (valor > ct.maxPosF) {
				valor = ct.maxPosF;
				String aux = Float.toString(valor);
				ct.addTokenListCompuesto(aux, ct.getNroLinea());
				ct.addTokenTS(aux, "CONST FLOAT");
				ct.addError("Error: Constante float "+bf+" fuera de rango.", ct.getNroLinea());
			} else if (valor < ct.minNegF) {
				valor = ct.minNegF;
				String aux = Float.toString(valor);
				ct.addTokenListCompuesto(aux, ct.getNroLinea());
				ct.addTokenTS(aux, "CONST FLOAT");
				ct.addError("Error: Constante float "+bf+" fuera de rango.", ct.getNroLinea());
			} else {
				valor = ct.zeroF;
				String aux = Float.toString(valor);
				ct.addTokenListCompuesto(aux, ct.getNroLinea());
				ct.addTokenTS(aux, "CONST FLOAT");
				ct.addError("Error: Constante float "+bf+" fuera de rango.", ct.getNroLinea());
			}
		}
		ct.setEstadoFinal();
		ct.setSimboloAnt();
		return 0;
	}
}
