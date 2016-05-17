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
 * @author b26441
 */
public class AdmClientes {
   
    List<Conexion> conexiones = new ArrayList<>();//Lista que contendra los eventos
    boolean[] servidores;
    
    
    public AdmClientes(int k){
    
        servidores = new boolean[k];

    }
    
    public void crearConexion(int tiempoActual, int timeOutGlobal){
    //Metodo poco ortodoxo con for
        for(int i = 0; i < servidores.length; i++){
        
            if(!servidores[i]){
                 servidores[i] = true;
                 Conexion conexion = new Conexion(tiempoActual, timeOutGlobal, i);
                 conexiones.add(conexion);
                 break;
            }
        }
        
    }
    
    public void eliminarConexion(int pos){
        servidores[pos] = false; 
        
        for(int i = 0; i < conexiones.size(); i++){
        
            if(conexiones.get(i).getServidor() == pos){
            
                
            } 
            
        }
        
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
    
}
