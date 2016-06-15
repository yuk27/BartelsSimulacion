package io;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class ModuloAdmProcesos {
     private Menu menu;
     private boolean servidorOcupado;
     private boolean entroAlServidor = false;
     private List<Conexion> conexiones;
     private Random r = new Random();
     private Conexion enServicio;
     
     public ModuloAdmProcesos(Menu menu){
         servidorOcupado = false;
         conexiones =  new ArrayList<>();
         this.menu = menu;
     }
     
     public void crearHilo(Conexion c,double reloj){
         if(!servidorOcupado){
             if(conexiones.isEmpty()){
                servidorOcupado = true;
                enServicio = c;
                entroAlServidor = true;
                menu.aplicarInterfazNuevoHilo(servidorOcupado,reloj);
             }
             else{
                conexiones.add(c);
             }  
         }
         else{
             conexiones.add(c);
         }
         menu.aplicarInterfazColaHilo(conexiones.size(), reloj);
     }
     
     public void setServidor(){
         entroAlServidor = false;
     }
     
     public boolean getServidor(){
         return entroAlServidor;
     }
     
     public boolean procesarSalida(double reloj,PriorityQueue<Evento> eventos){
                if(conexiones.isEmpty()){
                    enServicio = null;
                    servidorOcupado = false;                 
                }
                else{
                    enServicio = conexiones.get(0);
                    conexiones.remove(0);
                    servidorOcupado = true;
                    menu.aplicarInterfazColaHilo(conexiones.size(),reloj);
                    Evento siguienteHilo= new Evento(generarTiempoSalida() + reloj, siguienteConexion(), TipoEvento.SALE_DE_HILO);             //se genera el evento de Procesado de consulta de la siguiente conexion 
                    eventos.add(siguienteHilo); 
                }
        menu.aplicarInterfazNuevoHilo(servidorOcupado,reloj);
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
     
    public Conexion siguienteConexion(){
        servidorOcupado = false;
        return enServicio;
     }
     
    public void siguienteHilo(Conexion c,double reloj,PriorityQueue<Evento> eventos){
        Evento siguienteHilo= new Evento(generarTiempoSalida() + reloj,c,TipoEvento.SALE_DE_HILO); //se genera el evento de Procesado de consulta de la siguiente conexion 
        eventos.add(siguienteHilo); 
        System.out.println("evento anadido de salida de hilo");
    }
    
}
