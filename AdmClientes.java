/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

/**
 *
 * @author b26441
 */
public class AdmClientes {
    
    boolean[] servidores;
    
    
    public AdmClientes(int k){
    
        servidores = new boolean[k];

    }
    
    public int crearConexion(int pos){
    //Metodo poco ortodoxo con for
        for(int i = 0; i < servidores.length; i++){
        
            if(!servidores[i]){
                 servidores[i] = true;
                 return i;
            }
        }
        
      /* int pos = 0; 
       while(pos < servidores.length && )
       */
        
        return -1;
        
    }
    
    public void eliminarConexion(int pos){
        servidores[pos] = false; 
    }
    
    public int numServidores(){
    
        int libres = 0;
        
        for(int i = 0; i < servidores.length; i++){
        
            if(!servidores[i])
                libres++;
        }
        
        return libres;
    }
    
}
