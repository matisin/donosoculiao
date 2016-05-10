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
        int n_hebras = 1;
        int n_registros = 10;
        double Pe = 0.005;
        PrintWriter stats = new PrintWriter(new FileOutputStream(new File("Salida_Stats.txt"), false));
        MonitorHebras mh = new MonitorHebras();
        ArrayList<LectorArchivo> hebras = new ArrayList();
        for (int i = 0; i < n_hebras; i++) {
            hebras.add(new LectorArchivo(stats, mh, i + 1, Pe, "datap/sample_1.fastq", n_hebras));

        }
        long time_start, time_end;
        time_start = System.currentTimeMillis();     
        
        for (int i = 0; i < n_hebras; i++) {
            hebras.get(i).start();
        }
        for (int i = 0; i < n_hebras; i++) {
            hebras.get(i).join();
        }
        time_end = System.currentTimeMillis();
        System.out.println("the task has taken " + (time_end - time_start) + " milliseconds");

        //la.descartarNucleotidos(50,"text");
        //la.contenidoAT_GC();
    }

}
