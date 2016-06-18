package io;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Vector;

public class ModuloAdmProcesos {
     private Menu menu;
     private boolean servidorOcupado;
     private boolean entroAlServidor = false;
     private List<Conexion> conexiones;
     private Random r = new Random();
     private Conexion enServicio;
     
    private Vector<Integer> colaProcesos;
    private Vector<Double> tiempoSelect;
    private Vector<Double> tiempoUpdate;
    private Vector<Double> tiempoJoin;
    private Vector<Double> tiempoDDL;
     
     public ModuloAdmProcesos(Menu menu){
         this.servidorOcupado = false;
         this.conexiones =  new ArrayList<>();
         this.colaProcesos = new Vector<>();
         this.tiempoSelect  = new Vector<>();    
         this.tiempoUpdate   = new Vector<>();    
         this.tiempoJoin   = new Vector<>();    
         this.tiempoDDL  = new Vector<>();    
         this.menu = menu;
     }
     
     public void eliminarConexion(Conexion c){
         for(int i = 0; i < conexiones.size(); i++){
             if(conexiones.get(i) == c){
                 conexiones.remove(i);
                 return;
             }
         }
     }
     
    public Vector<Integer> getCola(){
        return this.colaProcesos;
    }
     
     public void crearHilo(Conexion c,double reloj, PriorityQueue<Evento> eventos){
         
         if(!servidorOcupado){
             if(conexiones.isEmpty()){
                this.servidorOcupado = true;
                this.enServicio = c;
                this.entroAlServidor = true;
                Evento siguienteTimeout = new Evento(c.getTimeout() ,c,TipoEvento.TIMEOUT);
                eventos.add(siguienteTimeout);
             }
             else{     
                this.conexiones.add(c);
                Evento siguienteTimeout = new Evento(c.getTimeout() ,c,TipoEvento.TIMEOUT); 
                eventos.add(siguienteTimeout);
             }  
         }
         else{
                this.conexiones.add(c);
                Evento siguienteTimeout = new Evento(c.getTimeout() ,c,TipoEvento.TIMEOUT); 
                eventos.add(siguienteTimeout);
                
         }
        this.colaProcesos.add(this.conexiones.size());
         menu.aplicarInterfazNuevoHilo(true,reloj);
         menu.aplicarInterfazColaHilo(conexiones.size(), reloj);
     }
     
     public void setServidor(){
         this.entroAlServidor = false;
     }
     
     public boolean getServidor(){
         return this.entroAlServidor;
     }
     
     public boolean procesarSalida(double reloj,PriorityQueue<Evento> eventos){
                if(conexiones.isEmpty()){
                    this.enServicio = null;
                    this.servidorOcupado = false;                 
                }
                else{
                    this.enServicio = conexiones.get(0);
                    this.conexiones.remove(0);
                    this.servidorOcupado = true;
                    
                    Evento siguienteHilo= new Evento(generarTiempoSalida() + reloj, siguienteConexion(), TipoEvento.SALE_DE_HILO);             //se genera el evento de Procesado de consulta de la siguiente conexion 
                    eventos.add(siguienteHilo); 
                    switch(siguienteConexion().getTipo()){
                        case 0:
                            tiempoSelect.add(reloj - siguienteConexion().getTiempoEntrada());
                            break;
                        case 1:
                            tiempoUpdate.add(reloj - siguienteConexion().getTiempoEntrada());
                            break;
                        case 2:
                            tiempoJoin.add(reloj - siguienteConexion().getTiempoEntrada());
                            break;
                        case 3:
                            tiempoDDL.add(reloj - siguienteConexion().getTiempoEntrada());
                            break;
                    }
                    siguienteConexion().setTiempoEntradaModulo(reloj);
                }
        menu.aplicarInterfazColaHilo(conexiones.size(),reloj);
        menu.aplicarInterfazNuevoHilo(servidorOcupado,reloj);
        return this.servidorOcupado;
     }
     
     public int getConexionesNum(){
         return this.conexiones.size();
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
         return this.servidorOcupado;
     }
     
    public Conexion siguienteConexion(){
        servidorOcupado = false;
        return enServicio;
     }
     
    public void siguienteHilo(Conexion c,double reloj,PriorityQueue<Evento> eventos){
        Evento siguienteHilo = new Evento(generarTiempoSalida() + reloj,c,TipoEvento.SALE_DE_HILO); //se genera el evento de Procesado de consulta de la siguiente conexion 
        eventos.add(siguienteHilo); 
        switch(siguienteConexion().getTipo()){
            case 0:
                tiempoSelect.add(reloj - siguienteConexion().getTiempoEntrada());
                break;
            case 1:
                tiempoUpdate.add(reloj - siguienteConexion().getTiempoEntrada());
                break;
            case 2:
                tiempoJoin.add(reloj - siguienteConexion().getTiempoEntrada());
                break;
            case 3:
                tiempoDDL.add(reloj - siguienteConexion().getTiempoEntrada());
                break;
        }
        siguienteConexion().setTiempoEntradaModulo(reloj);
        menu.aplicarInterfazNuevoHilo(true,reloj);
        menu.aplicarInterfazColaHilo(conexiones.size(),reloj);
        System.out.println("evento anadido de salida de hilo");
    }
    
}
