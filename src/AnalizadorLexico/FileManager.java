package AnalizadorLexico;

import java.io.*;

public class FileManager {
	private static final int asciiLN = 10;
    private FileReader in = null;
    private boolean salto = false;
    private boolean LN = false;
    public FileManager(File fuente) throws FileNotFoundException {
        in = new FileReader(fuente);
    }
    
    public Character readChar() throws IOException {
    	if ( LN == true ) {
            LN = false;
            return ' ';
        }
        int ret = in.read();
        if (ret != -1){
        	if(ret == asciiLN)
        		LN=true;
            return (char) ret;
        }else{
        	if (!salto){
        		salto = true;
        		return new Character('\n');
        	}
        	else 
        		return null;
        }
    }
    /*
    public Character readChar() throws IOException {

        int ret = in.read();
        if (ret != -1){
            return (char) ret;
        }else{
        	if (!salto){
        		salto = true;
        		return new Character('\n');
        	}
        	else 
        		return null;
        }
    }*/
    public static void write(String entry, File f) throws IOException {
        f.delete();
        entry = entry.replace("[", "");
        entry = entry.replace("]", "");
        BufferedWriter writer = new BufferedWriter(new FileWriter(f, true));
        writer.write(entry);
        writer.close();
    }

}