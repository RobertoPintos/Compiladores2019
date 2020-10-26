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
import java.io.StringWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.io.FileReader;

import Lexico.Controller;
import Lexico.Fuente;
import Sintactico.*;
import GeneracionDeCodigo.*;

public class prueba {
	
	private static BufferedReader codigo;
	private static JFileChooser chooser;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		mainr();
	}
	
	 public static void mainr () {
		 
		Fuente archivo;
		Controller controlador;
		TercetosController tercetosController;
		ConversorAssembler conversorAssembler;
		
		try {
		
			chooser = new JFileChooser();
			String location = AppPrefs.FileLocation.get(System.getProperty("user.home"));
		    chooser.setCurrentDirectory(new File(location));
			int retVal = chooser.showOpenDialog(null);
		    if (retVal != chooser.APPROVE_OPTION)
		    	System.out.println("Archivo no encontrado.");
		    else {
		    	File selectedFile = chooser.getSelectedFile();
		    	StringBuilder codigo = cargarCodigo(selectedFile);
		    	
				archivo = new Fuente(codigo);
		    	controlador = new Controller(archivo);
		    	tercetosController = new TercetosController(controlador);
		    	conversorAssembler = new ConversorAssembler(tercetosController, controlador);
		    
		    	//--------------------------------------------------------------------------------
		    	//MUESTRO CODIGO POR CONSOLA, JUNTO CON SUS TOKENS DETECTADOS, TS, WARNINGS Y ERRORES.
		    	System.out.println("Codigo fuente:");
		    	System.out.println(codigo);
		    	System.out.println("--------------------------------");
		    	System.out.println("--------------------------------");
		    	
		    	Parser parser = new Parser(controlador, tercetosController, conversorAssembler);
		        System.out.println(parser.yyparser());
		        	    	
		        File f = new File (selectedFile.getParent()+"\\resultadoCompilacion.txt");
		        imprimirEnArchivo(f, codigo);
		    	
		  
		    	controlador.mostrarTablaSimbolos(f);
		    	tercetosController.mostrarTercetos(f);
		    	controlador.mostrarListaTokens(f);
		    	controlador.getEstructuras(f);
		    	controlador.mostrarWarnings(f);
		    	controlador.mostrarErrores(f);
		    	conversorAssembler.mostrarErrores(f);
		    	
		    	System.out.println("----");
		    	System.out.println("----");
		    	System.out.println("Assembler:");
		    	System.out.println(".data");
		    	System.out.println(controlador.generarAssemblerTS());
		    	System.out.println(".code");        	
		    	System.out.println(tercetosController.generarAssembler());
		    	
		    	tercetosController.printTercetos();
	
		    	if (controlador.hayErrores() || conversorAssembler.hayErroresCI()) {
		    		JOptionPane.showMessageDialog(null, "No se genera código assembler por errores en el código.", null, JOptionPane.ERROR_MESSAGE);
		    	}
		    	else
		    	{
	    			conversorAssembler.generarAssembler(selectedFile);
	    			JOptionPane.showMessageDialog(null, "Archivo de salida generado exitosamente.", null, JOptionPane.INFORMATION_MESSAGE);
		    	}
		    }
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
    		JOptionPane.showMessageDialog(null, "Ha habido un error. Código de error: \n" + errors.toString(), null, JOptionPane.ERROR_MESSAGE);
		}
	 }
	 
	private static void imprimirEnArchivo(File f, StringBuilder codigo) {
		try {
			PrintWriter writer = new PrintWriter(f, "UTF-8");
			writer.println("Codigo:");
			writer.println(codigo);
			writer.close();
		} catch (Exception e) {
    		e.printStackTrace();
		}
	}
	
	//Metodo que carga el codigo desde el archivo seleccionado en un StringBuilder
	private static StringBuilder cargarCodigo (File inputFile) {
    	AppPrefs.FileLocation.put(inputFile.getParentFile().getAbsolutePath());
    	String direccion = new String(inputFile.getAbsolutePath());
    	InputStream is = new ByteArrayInputStream(direccion.getBytes());
    	BufferedReader bf = new BufferedReader(new InputStreamReader(is));
    	return new StringBuilder(getCodigo(bf));
	}
	
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
}

