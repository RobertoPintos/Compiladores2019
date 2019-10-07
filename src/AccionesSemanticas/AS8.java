package AccionesSemanticas;
import Lexico.Controller;

public class AS8 extends AccSemantica{

	//ACC SEMANTICA QUE VALIDA FLOAT Y CHEQUEA RANGOS
	
	public int ejecutar (char c, Controller ct) {
		
		String bf = ct.getBuffer();
		float valor = Float.parseFloat(bf);
		if (((valor > ct.minPosF) && (valor < ct.maxPosF)) || ((valor > ct.minNegF) && (valor < ct.maxNegF)) || (valor == ct.zeroF)) {
			ct.addTokenListCompuesto(bf, ct.getNroLinea());
			ct.addTokenTS(bf, "CONSTFLOAT");
		} else {
			if (valor > ct.maxPosF) {
				valor = ct.maxPosF;
				String aux = Float.toString(valor);
				ct.addTokenListCompuesto(aux, ct.getNroLinea());
				ct.addTokenTS(aux, "CONSTFLOAT");
				ct.addWarning("Warning: Float "+bf+" fuera de rango, truncado.", ct.getNroLinea());
			} else if (valor < ct.minNegF) {
				valor = ct.minNegF;
				String aux = Float.toString(valor);
				ct.addTokenListCompuesto(aux, ct.getNroLinea());
				ct.addTokenTS(aux, "CONSTFLOAT");
				ct.addWarning("Warning: Float "+bf+" fuera de rango, truncado.", ct.getNroLinea());
			} else {
				valor = ct.zeroF;
				String aux = Float.toString(valor);
				ct.addTokenListCompuesto(aux, ct.getNroLinea());
				ct.addTokenTS(aux, "CONSTFLOAT");
				ct.addWarning("Warning: Float "+bf+" fuera de rango, truncado.", ct.getNroLinea());
			}
		}
		ct.setEstadoFinal();
		return 0;
	}
}
