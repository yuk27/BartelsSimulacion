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
    double timeout = 0;
    int numServidor = 0;
    double  tiempoEntrada;
    int numBloques = 0;
    Random r = new Random();
 
    
    public Conexion(double tiempoEntrada,int  timeoutGlobal, int numServidor){
       this.tiempoEntrada = tiempoEntrada;
       this.numServidor = numServidor;
    }
    
    public static double generarTiempoArribo(double rnd){
        return (-1/30*(Math.log(1 - rnd)));
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
    
    public void generarTimeout(double  timeoutGlobal){
        timeout = tiempoEntrada + timeoutGlobal; 
    }
    
    public double getTimeout(){
        return timeout;     
    }
    
        public int getServidor(){
        return numServidor;     
    }
    
        public double getTiempoEntrada(){
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
