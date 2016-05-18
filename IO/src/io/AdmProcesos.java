/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 *
 * @author Ulises
 */
public class AdmProcesos {
     boolean servidorOcupado;
     List<Conexion> conexiones;
     Random r = new Random();
     
     public AdmProcesos(){
         servidorOcupado = false;
         conexiones =  new ArrayList<>();
     }
     
     public void crearHilo(){
         servidorOcupado = true;
     }
     
     public void liberarServidor(){
         servidorOcupado = false;
     }
     
     public double generarTiempoSalida(){      //distribucion normal
         double z = 0;
         for(int i = 0; i < 12; i++){
             z += r.nextDouble();
         }
         z -= 6;
         return (1 + (0.01*z));
     }
}
