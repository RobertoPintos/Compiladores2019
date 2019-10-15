package Lexico;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileReader;

import Lexico.Controller;
import Lexico.Fuente;


public class prueba {
	
	private static BufferedReader codigo;
	
	//ESTE METODO GUARDA EL CODIGO EN UN STRINGBUILDER, AGREGANDO SALTOS DE LINEA Y EL SIMBOLO $ AL FINAL
	private static StringBuilder getCodigo(BufferedReader ubicacion){
        StringBuilder buffer = new StringBuilder();
        try{
            codigo = new BufferedReader(new FileReader(ubicacion.readLine()));
            String readLine;
            while ((readLine = codigo.readLine())!= null) {
                buffer.append(readLine+"\n");
            }
            buffer.append("$");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return buffer;
    }
	
	 public static void mainr () {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Recuende que los subdirectorios van con doble barra invertida(\\\\)");
		System.out.println("Ingrese la ruta del archivo con el codigo fuente:");
		String s = new String();
        try {
			 s = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        File f = new File(s);
        if (!(f.isFile()))
        	System.out.println("Archivo no encontrado.");
        else {
        	String direccion = new String(s);
        	InputStream is = new ByteArrayInputStream(direccion.getBytes());
        	BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        	StringBuilder codigo = null;
        	codigo = new StringBuilder(getCodigo(bf));
        
      //--------------------------------------------------------------------------------
       
        	Fuente archivo = new Fuente(codigo);
        	Controller controlador = new Controller(archivo);

        	//MUESTRO CODIGO POR CONSOLA, JUNTO CON SUS TOKENS DETECTADOS, TS, WARNINGS Y ERRORES.
        	System.out.println("Codigo fuente:");
        	System.out.println(codigo);
        	System.out.println("--------------------------------");
        	System.out.println("--------------------------------");
        	controlador.recorrerCodFuente();
        	controlador.mostrarListaTokens();
        	controlador.mostrarTablaSimbolos();
        	controlador.mostrarWarnings();
        	controlador.mostrarErrores();
        	}
	 }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		mainr();
	}

}

