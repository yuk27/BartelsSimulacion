/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Ulises
 */
public class ProcesamientoConsultas {
    int[] servidoresConsultas;
    int[] ejecutorConsultas;
    double[] tiempoProcesamiento; 
    List<Conexion> consultas;
    List<Conexion> ejecutor;
    Random r = new Random();
    
    public ProcesamientoConsultas(int p, int m){
        servidoresConsultas = new int [p];
        tiempoProcesamiento = new double [p];
        ejecutorConsultas = new int[m];
        consultas = new ArrayList<>();
        ejecutor = new ArrayList<>();
    }
    
        public double asignarConsultaAServidor(Conexion c, double reloj){
        int i = 0;
        while(i < servidoresConsultas.length){
            if(servidoresConsultas[i] == -1){
                servidoresConsultas[i] = c.getNumServidor();
                return (calcularTiempoTotal(c));
            }
            i++;
        }
        consultas.add(c);
        return -1;
    }
        
    public int getSiguienteProcesado(){ 
       
            double menorTiempo = -1; //se inicializa el valor
            int siguiente = 0; 
        for(int i = 0; i < tiempoProcesamiento.length; i++){
        
            if(tiempoProcesamiento[i] > menorTiempo || menorTiempo == -1){ //si es el menor tiempo o el primero se toma como el menor
                menorTiempo = tiempoProcesamiento[i];
                siguiente = i;
            }
        }
    return siguiente;
    } 
        
    
        
    void InicializarVectores(){
    
        for(int i = 0; i < servidoresConsultas.length; i++){
            servidoresConsultas[i] = -1; 
        }
        
        for(int i = 0; i < ejecutorConsultas.length; i++){
            ejecutorConsultas[i] = -1; 
        }
    
    }
        
    public double calcularTiempoTotal(Conexion c){
        return (calcularTiempoLexico() + calcularTiempoSintactico() + calcularTiempoSemantico() + calcularTiempoPermisos() + calcularTiempoOptimizacion(c));
    }
    
    public double calcularTiempoLexico(){
        return (1/10);
    }
    
    public double calcularTiempoSintactico(){
        //resultado de la integral despejada
        double temp = r.nextDouble();
        return temp;
    }
    
    public double calcularTiempoSemantico(){
        //resultado de la integral despejada
        double temp = r.nextDouble();
        return temp;
    }
    
    public double calcularTiempoPermisos(){
        return (-(Math.log(1 - r.nextDouble()))/0.07);
    }
            
    public double calcularTiempoOptimizacion(Conexion c){
        if(c.isReadOnly()){
            return 0.1;
        }
        else{
            return 0.25;
        }
    }
    
    public boolean asignarConsultaAEjecutor(Conexion c){
        int i = 0;
        while(i < ejecutorConsultas.length){
            if(ejecutorConsultas[i] == -1){
                ejecutorConsultas[i] = c.getNumServidor();
                return true;
            }
            i++;
        }
        ejecutor.add(c);
        return false;
    }
    
    public double calcularTiempoAlgoritmoEjecucion(int cantidadDeBloques, Conexion c){
        double tiempoDeEjecucionSegundos = Math.pow(cantidadDeBloques, 2.0) / 1000;
        int tipoConexion = c.getTipo();
        if(tipoConexion == 1){
            tiempoDeEjecucionSegundos += 1;
        }
        else if(tipoConexion == 3){
            tiempoDeEjecucionSegundos += 0.5;
        }
        return tiempoDeEjecucionSegundos;
    }
    
    public double calcularTamanoRespuesta(int cantidadDeBloques){
        return cantidadDeBloques/64;
    }
    
    public int eliminarConexionServidor(Conexion c){
        int posLibre = -1;
        for(int i = 0; i < servidoresConsultas.length; i++){
            if(servidoresConsultas[i] == c.getNumServidor()){
                servidoresConsultas[i] = -1;
                posLibre = i;
            }
        }
        return posLibre;
    }
    
    public Conexion administrarEjecutor(Conexion c){
        int posLibre = eliminarConexionEjecutor(c);
        if(!ejecutor.isEmpty()){
            ejecutorConsultas[posLibre] = ejecutor.get(0).getNumServidor();
            return (ejecutor.remove(0));
        }
        else{
            return null;
        }
    }
    
    public Conexion administrarServidor(Conexion c){             
        int posLibre = eliminarConexionServidor(c);
        if(!consultas.isEmpty()){
            servidoresConsultas[posLibre] = consultas.get(0).getNumServidor();      //asigno la primera conexion de la lista al servidor que acabo de liberar
            return (consultas.remove(0));    
        }
        else{
            return null;
        }
    }
    
    public int eliminarConexionEjecutor(Conexion c){
        int posLibre = -1;
        for(int i = 0; i < ejecutorConsultas.length; i++){
            if(ejecutorConsultas[i] == c.getNumServidor()){
                ejecutorConsultas[i] = -1;
                posLibre = 0;
            }
        }
        return posLibre;
    }
}
