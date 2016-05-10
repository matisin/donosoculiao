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
    private String sCurrentLine,archivo;
    private PrintWriter stats;
    private MonitorHebras mh;
    private int id, n_hebras;
    private static int number = 0;
    private double Pe;
    //constructor de la hebra con el monitor, id, archivo stats, numero hebras, probabilidad y archivo a leer
    public LectorArchivo(MonitorHebras mh, Integer id, double Pe, String archivo, int n_hebras) throws FileNotFoundException {
        this.stats = stats;
        this.mh = mh;
        this.id = id.intValue();
        this.setName(new String(id.toString()));
        this.Pe = Pe;
        this.n_hebras = n_hebras;
        this.archivo = archivo;

    }
    //acción concurrente de las hebras
    public void run() {
        try {
            //inicializamos los valores de la estadistica en 0
            int totalnucleotidos = 0, totalvalidos = 0, totalerrores = 0;
            //calculamos Q a partir de la probabilidad
            double Q = -10 * (Math.log10(Pe));
            ArrayList nucleotidos;
            ArrayList indices;
            //leemos el archivo
            try {
                br = new BufferedReader(new FileReader(archivo));
            } catch (IOException e) {
                System.out.println("Error al leer archivo : " + e.getMessage());

            }
            //inicializamos la cantidad de los nucleotidos
            int A = 0;
            int G = 0;
            int C = 0;
            int T = 0;
            //avanzamos en lineas para no leer las mismas lineas con las hebras
            for (int i = 1; i < id; i++) {
                br.readLine();
                br.readLine();
                br.readLine();
                br.readLine();
            }
            //leemos el archivo hasta el final
            while ((sCurrentLine = br.readLine()) != null) {
                //avanzamos linea 2
                sCurrentLine = br.readLine();
                //arreglo de todos los nucleotidos sin verificar
                nucleotidos = new ArrayList();
                indices = new ArrayList();
                //recorremos los caracteres en la linea
                for (int i = 0; i < sCurrentLine.length(); i++) {
                    nucleotidos.add(sCurrentLine.charAt(i));
                    //verificamos que el caracter sea valido
                    if (!checkLetter(sCurrentLine.charAt(i))) {
                        indices.add(i);
                    } else {
                        //calculamos los errores de nucleotidos
                        totalerrores++;
                    }
                }
                //sumamos la cantidad de nucleotidos leidos
                totalnucleotidos += nucleotidos.size();
                //avanzamos a la linea 4
                br.readLine();
                sCurrentLine = br.readLine();
                //convertimos la linea en un arreglo de caracteres
                char[] linea = sCurrentLine.toCharArray();
                //String de secuencia leída.
                String seq = new String();
                //recorremos cada caracter de la linea
                for (int i = 0; i < linea.length; i++) {
                    //si el caracter es valido
                    if (indices.contains(i)) {
                        //si pasa prueba de calidad
                        if (linea[i] - 33 >= Q) {
                            //aumentamos la cantidad dependiendo de cual sea el caso
                            switch ((char) nucleotidos.get(i)) {
                                case 'G':
                                    G++;
                                    break;
                                case 'C':
                                    C++;
                                    break;
                                case 'A':
                                    A++;
                                    break;
                                case 'T':
                                    T++;
                                    break;
                                default:
                                    break;
                            }
                            //agregamos los nucleotidos con calidad a un arreglo de secuencia
                            seq = seq + (char) nucleotidos.get(i);
                            //calculamos la cantidad de nucleotidos validos
                            totalvalidos++;
                        }
                    }
                }
                seq = seq + '\n';
                //escribimos al buffer a través del monitor
                mh.puedeEscribirBuffer();
                //agregamos la secuencia valida al buffer
                mh.writeBuffer(seq.toCharArray());
                //salimos del monitor
                mh.liberaEscribirBuffer();
                //avanzamos en el texto dependiendo de cuantas hebras esten leyendo.
                for (int i = 1; i < n_hebras; i++) {
                    br.readLine();
                    br.readLine();
                    br.readLine();
                    br.readLine();
                }

            }
            //guardamos los datos en un arreglo.
            int[] datos = new int[7];
            datos[0] = totalnucleotidos;
            datos[1] = totalvalidos;
            datos[2] = totalerrores;
            datos[3] = A;
            datos[4] = G;
            datos[5] = C;
            datos[6] = T;
            //entramos al monitor y se suman los datos
            mh.puedeEscribir(datos);
            //se libera el monitor
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
