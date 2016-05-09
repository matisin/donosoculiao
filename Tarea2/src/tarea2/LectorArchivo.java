package tarea2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class LectorArchivo extends Thread {

    private BufferedReader br;
    private String sCurrentLine;
    private PrintWriter stats;
    private MonitorHebras mh;
    private int id;
    private static int number = 0;

    public LectorArchivo(PrintWriter stats, MonitorHebras mh, Integer id) throws FileNotFoundException {        
        this.stats = stats;
        this.mh = mh;
        this.id = id.intValue();
        this.setName(new String(id.toString()));
        
    }
    
    public void run() {
        try {
            int nlinea = 0, totalnucleotidos = 0, totalvalidos = 0, totalerrores = 0;
        } catch (InterruptedException e){
            System.out.println("Error al producir : " + e.getMessage());
        }
    }
    
    public void descartarNucleotidos(double Pe, String archivo) throws IOException {
        int nlinea = 0, totalnucleotidos = 0, totalvalidos = 0, totalerrores = 0;
        double Q = -10 * (Math.log10(Pe));
        ArrayList nucleotidos = new ArrayList();
        ArrayList indices = new ArrayList();
        PrintWriter writer = new PrintWriter("Salida_Secuencias.txt", "UTF-8");    
    
        br = new BufferedReader(new FileReader(archivo));
        
        while ((sCurrentLine = br.readLine()) != null) {
            nlinea++;
            if (sCurrentLine.startsWith("@")) {
                nlinea = 1;
            }
            if (nlinea == 2) {
                nucleotidos = new ArrayList();
                indices = new ArrayList();
                for (int i = 0; i < sCurrentLine.length(); i++) {
                    nucleotidos.add(sCurrentLine.charAt(i));
                    if (!checkLetter(sCurrentLine.charAt(i))) {
                        indices.add(i);
                    } else {
                        totalerrores++;
                    }
                }
                totalnucleotidos += nucleotidos.size();
                System.out.println(nucleotidos);
                System.out.println(indices);
            }
            if (nlinea == 4) {
                char[] linea = sCurrentLine.toCharArray();
                for (int i = 0; i < linea.length; i++) {
                    if (indices.contains(i)) {
                        if (linea[i] - 33 >= Q) {
                            System.out.print(nucleotidos.get(i));
                            writer.print(nucleotidos.get(i));
                            totalvalidos++;
                        }
                    }
                }
                writer.println("");
                System.out.println("");
            }
        }
        writer.close();
        stats.println("Pe ingresado: " + Pe);
        stats.println("Total Nucleotidos: " + totalnucleotidos);
        stats.println("Total Válidos: " + totalvalidos);
        stats.println("Total errores: " + totalerrores);
        stats.println("Total Nucleotidos no válidos por Pe: " + (totalnucleotidos - totalvalidos - totalerrores));
        //stats.close();
        br.close();
    }

    public void contenidoAT_GC() throws FileNotFoundException, IOException {        
        br = new BufferedReader(new FileReader("Salida_Secuencias.txt"));
        int G = 0, A = 0, T = 0, C = 0, total = 0, ch;
        float contenidoGC, contenidoAT,razon;
        char c;
        while ((ch = br.read()) != -1) {
            c = (char) ch;
            System.out.print(c);
            total++;
            if (c == 'G') {
                G++;
            } else if (c == 'C') {
                C++;
            } else if (c == 'A') {
                A++;
            } else if (c == 'T') {
                T++;
            }
        }
        System.out.println(100 * (float)(G+C)/total);
        contenidoGC = 100 * ((float)(G + C) / total);
        contenidoAT = 100 * ((float)(A + T) / total);
        razon = (float)(A + T) / (G + C);
        stats.println("Contenido Guanina y Citocina: " + contenidoGC);
        stats.println("Contenido Adenina y Timina: " + contenidoAT);
        stats.println("Razón AT/GC: " + razon);
        if (contenidoGC > 60) {
            stats.println("Contenido GC: Alto");
        } else if (contenidoGC < 40) {
            stats.println("Contenido GC: Bajo");
        } else {
            stats.println("Contenido GC: Moderado");
        }
        stats.close();
        br.close();

    }

    private boolean checkLetter(char c) {
        return c != 'A' && c != 'G' && c != 'T' && c != 'C';
    }
   
 
    
}
