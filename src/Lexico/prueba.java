package Lexico;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.JFileChooser;

import java.io.FileReader;

import Lexico.Controller;
import Lexico.Fuente;
import Sintactico.*;
import GeneracionDeCodigo.*;

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
		
		JFileChooser file = new JFileChooser();
		file.showOpenDialog(file);
		File ruta = file.getSelectedFile();
        if (!(ruta.isFile()))
        	System.out.println("Archivo no encontrado.");
        else {
        	String direccion = new String(ruta.getAbsolutePath());
        	InputStream is = new ByteArrayInputStream(direccion.getBytes());
        	BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        	StringBuilder codigo = null;
        	codigo = new StringBuilder(getCodigo(bf));
        
      //--------------------------------------------------------------------------------
       
        	Fuente archivo = new Fuente(codigo);
        	Controller controlador = new Controller(archivo);
            TercetosController tc = new TercetosController();

        	//MUESTRO CODIGO POR CONSOLA, JUNTO CON SUS TOKENS DETECTADOS, TS, WARNINGS Y ERRORES.
        	System.out.println("Codigo fuente:");
        	System.out.println(codigo);
        	System.out.println("--------------------------------");
        	System.out.println("--------------------------------");
        	
        	Parser parser = new Parser(controlador, tc);
            System.out.println(parser.yyparser());
            

        	
            File f = new File (ruta.getParent()+"\\resultadoCompilacion.txt");
        	try {
					PrintWriter writer = new PrintWriter(f, "UTF-8");
					writer.println("Codigo:");
					writer.println(codigo);
					writer.close();
	        } catch (Exception e) {
	        		e.printStackTrace();
	        }
      
            //controlador.recorrerCodFuente();
        	//controlador.mostrarListaTokens();
        	controlador.mostrarTablaSimbolos(f);
        	controlador.getEstructuras(f);
        	controlador.mostrarWarnings(f);
        	controlador.mostrarErrores(f);
        	tc.printTercetos();
        }	
	 }
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		mainr();
	}

}

