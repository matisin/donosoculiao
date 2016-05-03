/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea2;

import java.io.IOException;

/**
 *
 * @author diego
 */
public class Tarea2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        LectorArchivo la = new LectorArchivo("/home/diego/Documentos/UDEC/Sistemas Operativos/donosoculiao/tarea2/archivos/text");
        la.comprobarNucleotidos();
        la.descartarNucleotidos(50,"/home/diego/Documentos/UDEC/Sistemas Operativos/donosoculiao/tarea2/archivos/text");
    }

}
