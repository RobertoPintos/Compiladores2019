package Lexico;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class prueba {
	
	public static void leerArchivo() {
		{
	        // Cargamos el buffer con el contenido del archivo
	        
	        try {
	        	BufferedReader br = new BufferedReader (new FileReader ("C:\\Users\\uliip\\Desktop\\codigo.txt"));
	        	
		        // Leemos la primera linea
	        	int nrolinea = 1;        
		       	String texto = br.readLine();
	        	while(texto != null)
	            {
	        		System.out.println("Linea leida: " + nrolinea);        
	                System.out.println(texto); 	                //Hacer lo que sea con la línea leída
	                
	                String token;
	                int numTokens = 0;
	                StringTokenizer st = new StringTokenizer (texto);    
	                while (st.hasMoreTokens()) // bucle por todas las palabras
	                {
	                    token = st.nextToken();
	                    numTokens++;
	                    System.out.println ("    Palabra " + numTokens + " es: " + token);
	                }
	                nrolinea++;
	                texto = br.readLine(); //leo la siguiente linea
	            }
	
		    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		leerArchivo();
	}

}

