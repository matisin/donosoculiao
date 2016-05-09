/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author matisin
 */
public class MonitorHebras {

    private int numLects = 0, escribiendo = 0, escesperando = 0;
    private int[] datos;
    
   /*0 totalnucleotidos,
     1 totalvalidos, 
     2 totalerrores;
     3 A
     4 G
     5 C
     6 T*/

    public MonitorHebras() {
        this.datos = new int[7];
    }
    public void acumulaDatos(int[] datos){
        for(int i = 0 ; i < 7 ; i++){
            this.datos[i] += datos[i];
        }
    }
   
     
    public void puedeEscribir()throws InterruptedException {
       while(escribiendo != 0){
           wait();
       }
       escribiendo = 1;
    }
    
    public void liberaEscribir() throws InterruptedException{
        escribiendo = 0;
        notifyAll();
        
    }

}
