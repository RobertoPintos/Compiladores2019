package AccionesSemanticas;

import Lexico.Controller;

public class AS5 extends AccSemantica{

	//ACCION SEMANTICA QUE FINALIZA AL LLEGAR UN = PARA LA ASIGNACION Y EN EL CASO DE <= >= ==
	
	public int ejecutar (char c, Controller ct) {
		
		String aux = String.valueOf(c);
		if ((aux.equals("="))) {
			ct.setBuffer(ct.getBuffer()+c);
			ct.addTokenListCompuesto(ct.getBuffer(), ct.getNroLinea());
			ct.setEstadoFinal();
		}	
		return 0;
	}

}