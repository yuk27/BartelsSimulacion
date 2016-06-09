package io;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModuloAdmProcesos {
     private boolean servidorOcupado;
     private boolean entroAlServidor = false;
     private List<Conexion> conexiones;
     private Random r = new Random();
     private Conexion enServicio;
     
     public ModuloAdmProcesos(){
         servidorOcupado = false;
         conexiones =  new ArrayList<>();
     }
     
     public void crearHilo(Conexion c){
         if(!servidorOcupado){
             if(conexiones.size() == 0){
                servidorOcupado = true;
                enServicio = c;
                entroAlServidor = true;
             }
             else{
                conexiones.add(c);
             }  
         }
         else{
             conexiones.add(c);
         }
     }
     
     public void SetServidor(){
         entroAlServidor = false;
     }
     
     public boolean getServidor(){
         return entroAlServidor;
     }
     
     public boolean administrarServidor(){
                if(conexiones.isEmpty()){
                    enServicio = null;
                    servidorOcupado = false;
                }
                else{
                    enServicio =conexiones.get(0);
                    conexiones.remove(0);
                    servidorOcupado = true;
                }
        return servidorOcupado;
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
