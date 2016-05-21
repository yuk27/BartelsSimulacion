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
    List<Conexion> consultas;
    List<Conexion> ejecutor;
    Random r = new Random();
    
    public ProcesamientoConsultas(int p, int m){
        servidoresConsultas = new int [p];
        ejecutorConsultas = new int[m];
        consultas = new ArrayList<>();
        ejecutor = new ArrayList<>();
    }
    
        public void asignarConsultaAServidor(Conexion c){
        int i = 0;
        while(i < servidoresConsultas.length){
            if(servidoresConsultas[i] == -1){
                servidoresConsultas[i] = c.getNumServidor();
                break;
            }
            i++;
        }
        consultas.add(c);
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
    
    public void asignarConsultaAEjecutor(Conexion c){
        int i = 0;
        while(i < ejecutorConsultas.length){
            if(ejecutorConsultas[i] == -1){
                ejecutorConsultas[i] = c.getNumServidor();
                break;
            }
            i++;
        }
        ejecutor.add(c);
    }
    
    public double calcularTiempoAlgoritmoEjecucion(int cantidadDeBloques, Conexion c){
        double tiempoDeEjecucion = Math.pow(cantidadDeBloques, 2.0);
        int tipoConexion = c.getTipo();
        if(tipoConexion == 1){
            tiempoDeEjecucion += 1;
        }
        else if(tipoConexion == 3){
            tiempoDeEjecucion += 0.5;
        }
        return tiempoDeEjecucion;
    }
    
    public double calcularTamanoRespuesta(int cantidadDeBloques){
        return cantidadDeBloques/64;
    }
    
    public void eliminarConexionServidor(Conexion c){
        for(int i = 0; i < servidoresConsultas.length; i++){
            if(servidoresConsultas[i] == c.getNumServidor()){
                servidoresConsultas[i] = -1;
            }
        }
    }
    
    public void eliminarConexionEjecutor(Conexion c){
        for(int i = 0; i < ejecutorConsultas.length; i++){
            if(ejecutorConsultas[i] == c.getNumServidor()){
                ejecutorConsultas[i] = -1;
            }
        }
    }
}
