/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea2;

/**
 *
 * @author matisin
 */
public class Escritor extends Thread{
    private MonitorHebras mh;
    private int id;
    private static int number = 0;
    
    public Escritor(MonitorHebras mh, Integer id){
        this.mh = mh;
        this.id = id.intValue();
        this.setName(new String(id.toString()));
    }
    
    public void run() {
        try {
            mh.puedeEscribir();
            mh.liberaEscribir();
        } catch (InterruptedException e) {
            System.out.println("Error al producir : " + e.getMessage());
        }
    }
}
