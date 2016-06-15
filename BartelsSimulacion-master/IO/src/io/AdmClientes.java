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
   
    List<Conexion> conexiones;
    boolean[] servidores;
    
    
    public AdmClientes(int k){
        servidores = new boolean[k];
        conexiones = new ArrayList<>();
    }
    
    public void crearConexion(double tiempoActual, int timeOutGlobal){
        for(int i = 0; i < servidores.length; i++){
            if(!servidores[i]){
                 servidores[i] = true;
                 Conexion nuevaConexion = new Conexion(tiempoActual, timeOutGlobal, i);
                 nuevaConexion.generarTimeout(timeOutGlobal);
                 nuevaConexion.generarTipo();
                 conexiones.add(nuevaConexion);
                 break;
            }
        }
        
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
    
}
