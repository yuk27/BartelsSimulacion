package io;

import java.util.ArrayList;
import java.util.List;

public class ModuloTransacciones {
    private boolean hayDDL;
    private int[] servidores;
    private List<Conexion> conexionesConPrioridad;
    
    private double mayorTiempoEjecucion;
    
    public ModuloTransacciones(int p){
        servidores = new int[p];
        conexionesConPrioridad = new ArrayList<>();
        mayorTiempoEjecucion = 0;
        hayDDL = false;
    }
    
    void InicializarVector(){
    
        for(int i = 0; i < servidores.length; i++){
            servidores[i] = -1; 
        }
    }
      
    public double calcularTiempoTransaccion(){
        return servidores.length *0.03;
    }
    
    public double calcularTiempoBloquesDeDisco(Conexion c){
    
        double tiempoEnDisco = 0;
        
        switch (c.getTipo()){
                
                case 0: 
                    
                    tiempoEnDisco =  1/10;
                    break;
                
                case 2:
                   tiempoEnDisco =  randomWithRange(0,64); 
                    break;
                   
                 default:
                     tiempoEnDisco = 0;
                     break;
        }
        
        return tiempoEnDisco;
    }
    
       public double randomWithRange(int min, int max){ 
        int range = (max - min) + 1; 
        return (Math.random()*range) + min; 
    }
    
    public boolean asignarConexion(Conexion c){
        int i = 0; 
        while(i < servidores.length && !hayDDL){
            if(servidores[i] == -1){
                if(c.getTipo() == 3){
                    hayDDL = true;
                }
                servidores[i] = c.getNumServidor();
                return true;
            }
            i++;
        }
        conexionesConPrioridad.add(c);
        return false;
    }
    
    Conexion getConexionDePrioridad(){        
        int priorMax = 0;
        int index = 0;
        
        for(int i = 0; i < conexionesConPrioridad.size(); i++){     
            if(conexionesConPrioridad.get(i).getTipo() > priorMax){
                priorMax = conexionesConPrioridad.get(i).getTipo();
                index = i;
            }
        }   
        return conexionesConPrioridad.get(index);      
    }
    
    public int eliminarConexion(Conexion c){
       int posLibre = -1;
        if(c.getTipo() == 3){
            hayDDL = false;
        }
        for(int i = 0; i < servidores.length; i++){
            if(servidores[i] == c.getNumServidor()){
                servidores[i] = -1;
                posLibre = i;
            }
        }
        return posLibre;
    }
    
    public Conexion administrarServidorDeTransacciones(Conexion c){
        int posLibre = eliminarConexion(c);
        if(!conexionesConPrioridad.isEmpty()){
            Conexion nuevaConexion = getConexionDePrioridad();
            servidores[posLibre] = nuevaConexion.getNumServidor();
            return nuevaConexion;
        }
        return null;
    }
    
    public void setDDL(){
        hayDDL = true;
    }
    
    public double getMayorTiempoEjecucion(){
        return mayorTiempoEjecucion;
    } 
    
    public void setMayorTiempoEjecucion(double mayorTiempoEjecucion){
        this.mayorTiempoEjecucion = mayorTiempoEjecucion;
    }
    
}
