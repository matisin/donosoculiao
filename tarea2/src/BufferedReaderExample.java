package tarea2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BufferedReaderExample {

    private BufferedReader br;
    int numLinea;

    public BufferedReaderExample() {
        br = null;
    }
    public void checkText(String archivo) {
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(archivo));
            int count = 0,lineaError = 0;
            ArrayList lineas = new ArrayList();
            while ((sCurrentLine = br.readLine()) != null) {
                lineaError++;
                if(sCurrentLine.startsWith("@")){
                    numLinea = 1;
                }
                if (numLinea == 2) {
                    char[] array = sCurrentLine.toCharArray();
                    count = 0;
                    for (int i = 0; i < array.length; i++) {
                        if(array[i] != 'A' && array[i] != 'G' && array[i] != 'T' && array[i] != 'C'){
                            count++;
                            lineas.add(lineaError);
                        }
                    }
                }
                numLinea++;
            }
            System.out.println("La cadena de nucleótidos contiene " + count + " elementos no válidos en las líneas " + lineas);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
