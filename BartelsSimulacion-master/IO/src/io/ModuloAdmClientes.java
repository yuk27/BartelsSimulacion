package io;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ModuloAdmClientes {
    Menu menu;
    private List<Conexion> conexiones;
    private boolean[] servidores;
    private double timeOutGlobal;
    
    public ModuloAdmClientes(int k, double timeOutGlobal,Menu menu){
        servidores = new boolean[k];
        conexiones = new ArrayList<>();
        this.timeOutGlobal = timeOutGlobal;
        this.menu = menu;
    }
    
    public boolean crearConexion(double tiempoActual){
        
        if(hayServidor()){//si hay servidores desocupados
                for(int i = 0; i < servidores.length; i++){  //se crea la conexion y se pone la posicion del servidor en ocupado
                    if(!servidores[i]){
                         servidores[i] = true;
                         Conexion nuevaConexion = new Conexion(tiempoActual,timeOutGlobal,i);
                         nuevaConexion.generarTimeout(timeOutGlobal);
                         nuevaConexion.generarTipo();
                         conexiones.add(nuevaConexion);
                         return true;
                    }
                }
        }
       return false;
    }
    
    public int getOcupados(){
        int aux = 0;
        
        for(int i = 0; i < servidores.length; i++){ 
            if(servidores[i]){
                aux++; 
            }
        }
        
        return aux;
    }
    
    
    public void eliminarConexion(int pos,double reloj){
        servidores[pos] = false; 
        for(int i = 0; i < conexiones.size(); i++){
            if(conexiones.get(i).getServidor() == pos){
                conexiones.remove(i);
                menu.aplicarInterfazClientes(conexiones.size(),reloj);
            }    
        }   
    }
    
    public Conexion getSiguienteConexion(){
        return conexiones.get(conexiones.size() - 1);
    }
    
    public boolean hayServidor(){
        int pos = 0;
        while(pos < servidores.length){
            if(!servidores[pos])
                return true;
            pos++;
        }  
        return false;
    }
   
    /*
    public void verificarTimeout(int pos, int reloj){
        for(int i = 0; i < conexiones.size(); i++){
            if(conexiones.get(i).getServidor() == pos){
                if(reloj  > conexiones.get(i).getTimeout()){
                    conexiones.remove(i);
                    servidores[pos] = false;
                }
            }    
        } 
    }*/
      
    public int getUsedConexiones(){
        return conexiones.size();
    }
    
    public void sacarDelSistema(Conexion c,double tamanoRespuesta,PriorityQueue<Evento> eventos){
        Evento siguienteSalidaDelSistema = new Evento(tamanoRespuesta, c, TipoEvento.TERMINO_CONEXION);
        eventos.add(siguienteSalidaDelSistema);
    }
    
    public void crearArribo(double reloj,double r,PriorityQueue<Evento> eventos){
        double tiempoArribo = Conexion.generarTiempoArribo(r);
        Evento siguienteLlegada = new Evento(tiempoArribo + reloj,null,TipoEvento.LLEGA_CONEXION); //se genera el evento para la siguiente entrada de una conexion
        eventos.add(siguienteLlegada);// se agrega a la lista de eventos.
    }
    

    
}
