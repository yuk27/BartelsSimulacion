package io;

import java.util.ArrayList;
import java.util.List;

public class ModuloAdmClientes {
   
    private List<Conexion> conexiones;
    private boolean[] servidores;
    private double timeOutGlobal;
    
    public ModuloAdmClientes(int k, double timeOutGlobal){
        servidores = new boolean[k];
        conexiones = new ArrayList<>();
        this.timeOutGlobal = timeOutGlobal;
    }
    
    public boolean crearConexion(double tiempoActual){
        
        if(hayServidor()){//si hay servidores desocupados
                for(int i = 0; i < servidores.length; i++){  //se crea la conexion y se pone la posicion del servidor en ocupado
                    if(!servidores[i]){
                         servidores[i] = true;
                         Conexion nuevaConexion = new Conexion(tiempoActual,i);
                         nuevaConexion.generarTimeout(timeOutGlobal);
                         nuevaConexion.generarTipo();
                         conexiones.add(nuevaConexion);
                         return true;
                    }
                }
        }
       return false;
    }
    
    public void eliminarConexion(int pos){
        servidores[pos] = false; 
        for(int i = 0; i < conexiones.size(); i++){
            if(conexiones.get(i).getServidor() == pos){
                conexiones.remove(i);
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
    
    public void verificarTimeout(int pos, int reloj){
            for(int i = 0; i < conexiones.size(); i++){
                if(conexiones.get(i).getServidor() == pos){
                    if(reloj  > conexiones.get(i).getTimeout()){
                        conexiones.remove(i);
                        servidores[pos] = false;
                    }
                }    
        } 
    }
    
    public double ponerEnRed(double tiempo){
        return tiempo;
    }
    
            
    public int getUsedConexiones(){
        return conexiones.size();
    }
    
}
