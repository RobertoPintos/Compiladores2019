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
	        	BufferedReader br = new BufferedReader (new FileReader ("C:\\Users\\rober\\Desktop\\codigo.txt"));
	        	
		        // Leemos la primera linea
			                      
		       	String temp="";
	        	String bfRead;
	        	while((bfRead = br.readLine())!=null){
	        	//haz el ciclo, mientras bfRead tiene datos
	        	temp = temp + bfRead + "\n";
	        	}
	        	
	        	int y = temp.length();
	        	for (int i=0; i < y; i++) {
	        		System.out.println("caracter "+i+": "+temp.charAt(i));
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

