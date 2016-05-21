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
 
    
    public Conexion(int tiempoEntrada,int  timeoutGlobal, int numServidor){
       this.tiempoEntrada = tiempoEntrada;
       this.numServidor = numServidor;
    }
    
    public double generarTiempoArribo(){
        return (-1/30*(Math.log(1 - r.nextDouble())));
    }
    
    public void generarTipo(){   
        double val = r.nextDouble();
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
    
        public int getServidor(){
        return numServidor;     
    }
    
        public int getTiempoEntrada(){
        return tiempoEntrada;     
    }
        
        public boolean isReadOnly(){
            return tipo == 0 || tipo == 2;
        }
    
        public int getTipo(){
            return this.tipo;
        }
        
        public int getNumServidor(){
            return this.numServidor;
        }
}
