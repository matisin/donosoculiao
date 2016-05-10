/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author diego
 */
public class Tarea2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        //argumentos de la terminal
        String archivo = args[0];
        int num_lineas = Integer.parseInt(args[1]);
        double Pe = Double.parseDouble(args[2]);
        int n_hebras = Integer.parseInt(args[3]);
        int n_registros = Integer.parseInt(args[4]);
        String archivo_secuencia = args[5];
        String archivo_stats = args[6];
        //se crea un archivo vacio para los stats
        PrintWriter writerStats = new PrintWriter(new FileOutputStream(new File(archivo_stats), false));
        //se instancia el monitor de las hebras
        MonitorHebras mh = new MonitorHebras(n_registros,Pe,archivo_secuencia);
        //arreglo de hebras
        ArrayList<LectorArchivo> hebras = new ArrayList();
        //se crean n hebras
        for (int i = 0; i < n_hebras; i++) {
            //cada hebra conoce al monitor, la probabilidad y el archivo a leer.
            hebras.add(new LectorArchivo( mh, i + 1, Pe,archivo, n_hebras));
        }
        //se utiliza para medir el tiempo
        long time_start, time_end;
        time_start = System.currentTimeMillis();
        //se corren las hebras
        for (int i = 0; i < n_hebras; i++) {
            hebras.get(i).start();
        }
        //con esto se espera a que terminen todas las hebras
        for (int i = 0; i < n_hebras; i++) {
            hebras.get(i).join();
        }
        //tiempo transucrrido
        time_end = System.currentTimeMillis();
        System.out.println("Se ha demorado " + (time_end - time_start) + " milliseconds");
        //se escribe el archivo con las estadisticas.
        mh.escribe(writerStats);
        //se cierra el archivo de secuencias.
        mh.cerrarArchivo();
    }
}
