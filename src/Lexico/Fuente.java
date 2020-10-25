package Lexico;

public class Fuente {
	
    static final char saltoLinea = '\n';
    static final char finArch = '$';
    public int linea;
    private static char actual; 
    private int pos;     //POSICION EN EL ARCHIVO
    private StringBuilder archivo;     //ARCHIVO

    public Fuente(StringBuilder archivo) {
        this.archivo = archivo;
        pos = 0;
        actual = archivo.charAt(pos);
        linea = 1; 
    }
     
    public int getLinea (){
        return linea;
    }

    public boolean hasFinished() {//TERMINO DE LEER EL CODIGO FUENTE
        if( actual == finArch){
            return true;
        }
        else return false;
    }

    public char getChar() {//devuelve el caracter a leer
        return actual;
    }

    public void siguiente() {
        if (actual == saltoLinea)
            linea++;
        if (pos<archivo.length()){
            pos++;
            actual=archivo.charAt(pos);
        }
    }
    
    public void anterior() {
    	pos--;
    	actual=archivo.charAt(pos);
    }
    
    public int getFuenteSize () {
    	return archivo.length();
    }
    
    public int getPos () {
    	return pos;
    }
}