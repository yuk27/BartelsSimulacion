package io;
import java.util.Random;

public class Conexion {
    
    private int tipo = 0; //tipo de instruccion 0 SELECT, 1 UPDATE, 2 JOIN, 3 DLL 
    private double timeout = 0;
    private int numServidor = 0;
    private double  tiempoEntrada;
    private int numBloques = 0;
    private Random r = new Random();
 
    
    public Conexion(double tiempoEntrada, int numServidor){
       this.tiempoEntrada = tiempoEntrada;
       this.numServidor = numServidor;
    }
    
    public static double generarTiempoArribo(double rnd){
        return -1/30d *((Math.log(1 - rnd)));
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

    public int getNumBloques(){
        return numBloques;
    }

    public void setNumBloques(int numBloques){
        this.numBloques = numBloques;
    }

}
