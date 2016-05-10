/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea2;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author matisin
 */
public class MonitorHebras {

    private int escribiendo = 0, escesperando = 0;
    private int[] datos;
    private char[] buffer;
    private int size;
    private int write;
    private PrintWriter writerStatsSequencia;
    private double Pe;

    /*0 totalnucleotidos,
     1 totalvalidos, 
     2 totalerrores;
     3 A
     4 G
     5 C
     6 T*/
    public MonitorHebras(int m,double Pe) throws FileNotFoundException, UnsupportedEncodingException {
        this.datos = new int[7];        
        size = 1024*m;
        write = 0;
        buffer = new char[size];
        writerStatsSequencia = new PrintWriter("Salida_Secuencias.txt", "UTF-8"); 
        this.Pe = Pe;
    }
    protected void escribirArchivo(){
        writerStatsSequencia.write(buffer, 0, size);       
        write = 0;
    }
    protected void cerrarArchivo(){
        writerStatsSequencia.write(buffer, 0, write - 1);       
        writerStatsSequencia.close();
    }
    protected void writeBuffer(char[] seq){
        for(int i = 0 ; i < seq.length ; i++){
            if(this.isFull()){
                escribirArchivo();
            }
            buffer[write++] = seq[i];
        }       
    }
    protected boolean isFull(){
        return write -1 == size -1;
    }
    private void acumulaDatos(int[] datos) {
        for (int i = 0; i < 7; i++) {
            this.datos[i] += datos[i];
        }
    }
    public synchronized void puedeEscribirBuffer() throws InterruptedException{
        while (escribiendo != 0) {
            wait();
        }
        escribiendo = 1;
    }
    public synchronized void liberaEscribirBuffer() throws InterruptedException {
        escribiendo = 0;
        notifyAll();

    }
    
    public synchronized void puedeEscribir(int[] datos) throws InterruptedException {
        while (escribiendo != 0) {
            wait();
        }
        escribiendo = 1;
        acumulaDatos(datos);
    }

    public synchronized void liberaEscribir() throws InterruptedException {
        escribiendo = 0;
        notifyAll();

    }

    public void imprime(PrintWriter writerStats) {
        writerStats.println("Pe ingresado: " + Pe);
        writerStats.println("Total nucleótidos : " + datos[0]);
        writerStats.println("Total válidos : " + datos[1]);
        writerStats.println("Total errores : " + datos[2]);
        writerStats.println("Total válidos pero erroneos por Pe : " + (datos[0] - datos[1] - datos[2]));
        double contenidoGC = (100 * (double) (datos[4] + datos[5]) / datos[1]);
        writerStats.println("Contenido GC: " + contenidoGC);
        double contenidoAT = (100 * (double) (datos[3] + datos[6]) / datos[1]);
        writerStats.println("Contenido AT: " + contenidoAT);
        writerStats.println("Razón AT/GC: " + ((double) ((datos[3] + datos[6])) / (datos[4] + datos[5])));
        if(contenidoGC > 60){
            writerStats.println("ContenidoGC: Alto");
        }
        else if(contenidoGC < 40){
            writerStats.println("ContenidoGC: Bajo");
        }
        else{
            writerStats.println("ContenidoGC: Moderado");
        }
        writerStats.close();
    }

}
