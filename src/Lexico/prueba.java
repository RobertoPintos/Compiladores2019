package Lexico;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
		String direccion = new String("C:\\Users\\uliip\\Desktop\\codigo.txt");
        InputStream is = new ByteArrayInputStream(direccion.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder codigo = null;
        codigo = new StringBuilder(getCodigo(br));
        
      //--------------------------------------------------------------------------------
       
        Fuente archivo = new Fuente(codigo);
        Controller controlador = new Controller(archivo);

        //AHORA TENDRIA QUE PEDIR LOS TOKENS Y LOS ERRRORES
        System.out.println(codigo);
        controlador.recorrerCodFuente();
        controlador.mostrarListaTokens();
        }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		mainr();
	}

}

