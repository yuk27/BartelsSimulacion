/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;
import java.util.Random;
/**
 *
 * @author b26441
 */
public class Conexion {
    
    int tipo = 0; //tipo de instruccion 0 SELECT, 1 UPDATE, 2 JOIN, 3 DLL 
    int timeout = 0;
    int numServidor = 0;
    int  tiempoEntrada;
    Random r = new Random();
 
    
    public Conexion(int  tiempoEntrada,int  timeoutGlobal, int numServidor){
        this.tiempoEntrada = tiempoEntrada;
        generarTimeout(timeoutGlobal);
        definirTipo();
    }
    
    
    public void definirTipo(){   
        
        float val = r.nextFloat();
        
        if(val < 0.3){
        
            tipo = 0;
            
        }
        
        else if(val < 0.55){
            
            tipo = 1;
            
        }
        
        else if(val < 0.9){
        
            tipo = 2;
            
        }
        
        else{
            tipo = 3;
        }
   
        
    }
    
    public void generarTimeout(int  timeoutGlobal){
    
        timeout = tiempoEntrada + timeoutGlobal; 
    }
    
    public int getTimeout(){
        return timeout;     
    }
    
        public int getTiempoEntrada(){
        return tiempoEntrada;     
    }
    
}
