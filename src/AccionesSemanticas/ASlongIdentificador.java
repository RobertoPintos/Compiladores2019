package AccionesSemanticas;

import Lexico.Controller;

public class ASlongIdentificador extends AccSemantica{

	@Override
	public int ejecutar(char c, Controller ac) {
		// TODO Auto-generated method stub
		String id = ac.getBuffer()+c;
		if(ac.maxId >= id.length()) 
			return 0;
		else 
			//Controlar el Warning en este caso debido a que hay un descarte del caracter
			return 1;
	}

}
