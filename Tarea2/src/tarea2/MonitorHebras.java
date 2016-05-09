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
