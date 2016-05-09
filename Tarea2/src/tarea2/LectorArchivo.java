package tarea2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LectorArchivo extends Thread {

    private BufferedReader br;
    private String sCurrentLine;
    private PrintWriter stats;
    private MonitorHebras mh;
    private int id, n_hebras;
    private static int number = 0;
    private double Pe;
    private String archivo;

    public LectorArchivo(PrintWriter stats, MonitorHebras mh, Integer id, double Pe, String archivo, int n_hebras) throws FileNotFoundException {
        this.stats = stats;
        this.mh = mh;
        this.id = id.intValue();
        this.setName(new String(id.toString()));
        this.Pe = Pe;
        this.n_hebras = n_hebras;

    }

    public void run() { 
       try {
            int totalnucleotidos = 0, totalvalidos = 0, totalerrores = 0;
            double Q = -10 * (Math.log10(Pe));
            ArrayList nucleotidos;
            ArrayList indices;
            //hay que usar buffer//PrintWriter writer = new PrintWriter("Salida_Secuencias.txt", "UTF-8"); 
            try {
                br = new BufferedReader(new FileReader(archivo));
            } catch (IOException e) {
                System.out.println("Error al leer archivo : " + e.getMessage());
            }
            int A = 0, G = 0, C = 0, T = 0;
            
            while ((sCurrentLine = br.readLine()) != null) {

                for (int i = 1; i < id; i++) {
                    br.readLine();
                    br.readLine();
                    br.readLine();
                    br.readLine();
                }
                //avanzamos linea 2
                br.readLine();
                br.readLine();
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
                //avanzamos a la linea 4
                br.readLine();
                br.readLine();
                char[] linea = sCurrentLine.toCharArray();
                for (int i = 0; i < linea.length; i++) {
                    if (indices.contains(i)) {
                        if (linea[i] - 33 >= Q) {
                            if ((char)nucleotidos.get(i) == 'G') {
                                G++;
                            } else if ((char)nucleotidos.get(i) == 'C') {
                                C++;
                            } else if ((char)nucleotidos.get(i) == 'A') {
                                A++;
                            } else if ((char)nucleotidos.get(i) == 'T') {
                                T++;
                            }
                            //escribe en el buffer//writer.print(nucleotidos.get(i));
                            totalvalidos++;
                        }
                    }
                }
                //escribe buffer//writer.println("");
                 for (int i = 1; i < n_hebras; i++) {
                    br.readLine();
                    br.readLine();
                    br.readLine();
                    br.readLine();
                }

            }

            mh.puedeEscribir();
            int[] datos = new int[7];
            datos[0] = totalnucleotidos;
            datos[1] = totalvalidos;
            datos[2] = totalerrores;
            datos[3] = A;
            datos[4] = G;
            datos[5] = C;
            datos[6] = T;
            mh.acumulaDatos(datos);
            mh.liberaEscribir();

        } catch (InterruptedException e) {
            System.out.println("Error al producir : " + e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(LectorArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    private boolean checkLetter(char c) {
        return c != 'A' && c != 'G' && c != 'T' && c != 'C';
    }

}
