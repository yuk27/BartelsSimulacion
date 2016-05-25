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
     Conexion enServicio;
     
     public AdmProcesos(){
         servidorOcupado = false;
         conexiones =  new ArrayList<>();
     }
     
     public void crearHilo(Conexion c){
         if(!servidorOcupado){
            
             if(conexiones.size() == 0){
             
                servidorOcupado = true;
                enServicio = c;
             }
             
             else{
             conexiones.add(c);
             enServicio =conexiones.get(0);
             conexiones.remove(0);
             servidorOcupado = true;
             }
         
         }
         else{
             conexiones.add(c);
         }
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
     
     public boolean estaOcupado(){
     return servidorOcupado;
     }
     
    Conexion SiguienteConexion(){
     servidorOcupado = false;
     return enServicio;
     }
     
}
