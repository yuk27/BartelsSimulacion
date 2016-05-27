/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ulises
 */
public class Transacciones {
    boolean hayDDL;
    int[] servidores;
    List<Conexion> conexionesConPrioridad;
    
    public double mayorTiempoEjecucion;
    
    public Transacciones(int p){
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
    
    public void eliminarConexion(Conexion c){
        for(int i = 0; i < servidores.length; i++){
            if(servidores[i] == c.getNumServidor()){
                servidores[i] = -1;
            }
        }
        
        if(c.tipo == 3){
            hayDDL = false;
        }
    }
    
}
