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
import javax.swing.JOptionPane;

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
		
		JFileChooser chooser = new JFileChooser();
		String location = AppPrefs.FileLocation.get(System.getProperty("user.home"));
	    chooser.setCurrentDirectory(new File(location));
		int retVal = chooser.showOpenDialog(null);
        if (retVal != chooser.APPROVE_OPTION)
        	System.out.println("Archivo no encontrado.");
        else {
        	File selectedFile = chooser.getSelectedFile();
        	AppPrefs.FileLocation.put(selectedFile.getParentFile().getAbsolutePath());
        	String direccion = new String(selectedFile.getAbsolutePath());
        	InputStream is = new ByteArrayInputStream(direccion.getBytes());
        	BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        	StringBuilder codigo = null;
        	codigo = new StringBuilder(getCodigo(bf));
        
      //--------------------------------------------------------------------------------
       
        	Fuente archivo = new Fuente(codigo);
        	Controller controlador = new Controller(archivo);
            TercetosController tc = new TercetosController(controlador);
            ConversorAssembler conversor = new ConversorAssembler(tc, controlador);

        	//MUESTRO CODIGO POR CONSOLA, JUNTO CON SUS TOKENS DETECTADOS, TS, WARNINGS Y ERRORES.
        	System.out.println("Codigo fuente:");
        	System.out.println(codigo);
        	System.out.println("--------------------------------");
        	System.out.println("--------------------------------");
        	
        	Parser parser = new Parser(controlador, tc, conversor);
            System.out.println(parser.yyparser());
            

        	
            File f = new File (selectedFile.getParent()+"\\resultadoCompilacion.txt");
        	try {
					PrintWriter writer = new PrintWriter(f, "UTF-8");
					writer.println("Codigo:");
					writer.println(codigo);
					writer.close();
	        } catch (Exception e) {
	        		e.printStackTrace();
	        }
      
        	controlador.mostrarTablaSimbolos(f);
        	tc.mostrarTercetos(f);
        	controlador.mostrarListaTokens(f);
        	controlador.getEstructuras(f);
        	controlador.mostrarWarnings(f);
        	controlador.mostrarErrores(f);
        	conversor.mostrarErrores(f);
        	tc.printTercetos();
        	
        	
        	if (controlador.hayErrores() || conversor.hayErroresCI()) {
        		JOptionPane.showMessageDialog(null, "No se genera codigo assembler por errores en el codigo", null, JOptionPane.ERROR_MESSAGE);
        	}else {
        		try {
        			System.out.println("----");
                	System.out.println("----");
                	System.out.println("Assembler:");
                	System.out.println(".data");
                	System.out.println(controlador.generarAssemblerTS());
                	System.out.println(".code");        	
                	System.out.println(tc.generarAssembler());
        			conversor.generarAssembler(selectedFile);
        			System.out.println("reduccion simple");
        			tc.printTercetos();
        			System.out.println("----");
                	System.out.println("----");
                	System.out.println("Assembler:");
        			System.out.println(controlador.generarAssemblerTS());
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
          	}
        }	
	 }
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		mainr();
	}

}

