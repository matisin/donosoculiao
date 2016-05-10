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
    //dos contadores, uno para escribir estadisticas y el otro para escribir el buffer.
    private int escribiendo = 0, escesperando = 0;
    private int[] datos;
    private char[] buffer;
    private int size,write;
    private PrintWriter writerStatsSequencia;
    private double Pe;
    //orden de los datos en el arreglo
    /*0 totalnucleotidos,
     1 totalvalidos,
     2 totalerrores;
     3 A
     4 G
     5 C
     6 T*/
     //constuctor del monitor, con el numero de registros, probabilidad y archivo de secuencias.
    public MonitorHebras(int m,double Pe,String archivo_secuencia) throws FileNotFoundException, UnsupportedEncodingException {
        this.datos = new int[7];
        //tamaño del buffer
        size = 1024*m;
        //se comenzará a escribir en la posicion 0
        write = 0;
        //se inicializa el buffer
        buffer = new char[size];
        //se crea el archivo de secuencia
        writerStatsSequencia = new PrintWriter(archivo_secuencia, "UTF-8");
        this.Pe = Pe;
    }
    //se escribe el archivo cuando se llene el buffer
    protected void escribirArchivo(){
        //escribimos todo el buffer
        writerStatsSequencia.write(buffer, 0, size);
        //volvemos a la posición 0
        write = 0;
    }
    //se escribe lo que quede en el buffer
    protected void cerrarArchivo(){
        //se escribe lo que hay hasta write.
        writerStatsSequencia.write(buffer, 0, write - 1);
        writerStatsSequencia.close();
    }
    //se escriben los caracteres en el buffuer
    protected void writeBuffer(char[] seq){
        for(int i = 0 ; i < seq.length ; i++){
            //si se llena el buffer se debe escriir en el archivo
            if(this.isFull()){
                escribirArchivo();
            }
            buffer[write++] = seq[i];
        }
    }
    //se comprueba si el buffer esta lleno
    protected boolean isFull(){
        return write -1 == size -1;
    }
    //suma los datos que ya hay con los de las hebras
    private void acumulaDatos(int[] datos) {
        for (int i = 0; i < 7; i++) {
            this.datos[i] += datos[i];
        }
    }
    //entra en la cola si alguna hebra está escribiendo en el buffer
    public synchronized void puedeEscribirBuffer() throws InterruptedException{
        while (escribiendo != 0) {
            wait();
        }
        escribiendo = 1;
    }
    //notifica a las hebras que libero el buffer en el monitor
    public synchronized void liberaEscribirBuffer() throws InterruptedException {
        escribiendo = 0;
        notifyAll();

    }
    //entra en la cola si alguna hebra está escribiendo los datos
    public synchronized void puedeEscribir(int[] datos) throws InterruptedException {
        while (escribiendo != 0) {
            wait();
        }
        escribiendo = 1;
        acumulaDatos(datos);
    }
    //notifica a las hebras que libero los datos del monitor
    public synchronized void liberaEscribir() throws InterruptedException {
        escribiendo = 0;
        notifyAll();

    }
    //finalmente escribe los datos calculados en el archivo.
    public void escribe(PrintWriter writerStats) {
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
