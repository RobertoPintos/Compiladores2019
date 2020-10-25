package GeneracionDeCodigo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

import Lexico.Error;
import Lexico.Controller;

import Lexico.*;

public class ConversorAssembler {

	public static final String labelOverflowSuma = "LabelOverflowSuma";
	public static final String labelDivCero = "LabelDivCero";
	public static final String labelOverflowMul = "LabelOverflowMul";
	public static final String labelEnd = "LabelEnd";

	static File arch;
	static TercetosController controladorTercetos;
	static Controller controller;
	private ArrayList<Error> erroresCI;
	
	public ConversorAssembler( TercetosController controladorTercetos, Controller controlador ) {
		this.controladorTercetos = controladorTercetos;
		this.controller = controlador;
		erroresCI = new ArrayList<Error>();
	}
	
	
	public void generarAssembler (File r) throws IOException{
		//controladorTercetos.generarAssembler();
		String nameFile = r.getName().substring(0, r.getName().length()-4);
		arch = new File(r.getParent() + "\\" + nameFile + ".asm");
		writeFile();
	}

	private static void writeFile() throws IOException {
		FileOutputStream fos = new FileOutputStream(arch);
	 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		
		
		bw.write(".386" + '\n' 
				+ ".model flat, stdcall" + '\n'
				+ ".stack 200h" + '\n'
				+ "option casemap :none" + '\n'
				+ "include \\masm32\\include\\windows.inc" + '\n'
				+ "include \\masm32\\include\\kernel32.inc" + '\n'
				+ "include \\masm32\\include\\user32.inc" + '\n'
				+ "include \\masm32\\include\\masm32.inc" + '\n'
				+ "includelib \\masm32\\lib\\kernel32.lib" + '\n'
				+ "includelib \\masm32\\lib\\user32.lib" + '\n'
				+ "includelib \\masm32\\lib\\masm32.lib" + '\n'
				+ '\n' +".data" + '\n');	
		String data = "";
		data = data + controller.generarAssemblerTS() ;
		data = data + "DividirCero DB \"Error al dividir por cero!\", 0" + '\n';
		data = data + "OverflowSuma DB \"Error por overflow en suma!\", 0" + '\n';
		data = data + "OverflowMul DB \"Error por overflow en multiplicacion!\", 0" + '\n';
		data = data + "_min_float_neg DD -3.40282347E38" + '\n';
		//data = data + "_max_float_neg DD -1.17549435E-38" + '\n';
		//data = data + "_min_float_pos DD 1.17549435E-38" + '\n';
		data = data + "_max_float_pos DD 3.40282347E38" + '\n';
		//data = data + "_zero_float DD 0.0" + '\n';
		
		data = data + '\n' + ".code"+ "\n";
		data = data + controladorTercetos.generarAssemblerFunctions() + "\n";
		
		bw.write( data );
		
		//Inicia el codigo
		String code = "start:" + '\n' + (String) controladorTercetos.generarAssembler() + '\n'; 


		bw.write( code );
		String errores = labelDivCero + ":" + '\n';
		errores = errores + "invoke MessageBox, NULL, addr DividirCero, addr DividirCero, MB_OK" + '\n';
		errores = errores + "invoke ExitProcess, 0" + '\n';
		errores = errores + labelOverflowSuma + ":" + '\n';
		errores = errores + "invoke MessageBox, NULL, addr OverflowSuma, addr OverflowSuma, MB_OK" + '\n';
		errores = errores + "invoke ExitProcess, 0" + '\n';
		errores = errores + labelOverflowMul + ":" + '\n';
		errores = errores + "invoke MessageBox, NULL, addr OverflowMul, addr OverflowMul, MB_OK" + '\n';
		errores = errores + "invoke ExitProcess, 0" + '\n';
		errores = errores + labelEnd + ":" + '\n';
		errores = errores + "invoke ExitProcess, 0" + '\n';
		bw.write(errores);
		bw.write( "end start" );

		bw.close();
	}
	
	public void addErrorCI (String desc, int nLinea) {
		Error e = new Error(desc, nLinea);
		erroresCI.add(e);
	}
	
	public ArrayList<Error> getErroresCI () {
		return erroresCI;
	}
	
	public boolean hayErroresCI () {
		if (erroresCI.isEmpty())
			return false;
		else return true;
	}
	
	public ConversorAssembler getConversorAssembler () {
		return this;
	}
	
	public void mostrarErrores(File f) {
		try {
			FileWriter fwriter = new FileWriter(f, true);
			PrintWriter writer = new PrintWriter(fwriter);
			writer.println("-----------------------------------");
			writer.println("-----------------------------------");
			writer.println("Lista de errores en Codigo Intermedio:");
			for (Error e: erroresCI) {
				writer.println("-"+e.getDescripcion()+" en la linea: "+e.getNroLinea());
			}
			writer.close();
		} catch (Exception e) {
    		e.printStackTrace();
		}
	}
}

