/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;
import java.util.*;
import java.util.Random;
import java.io.IOException;
/**
 *
 * @author b26441
 */



public class Simulacion {
    
    static class Evento{

    double tiempo;
    int id;
    Conexion c;
    
    public double getTiempo(){
        return tiempo;
    }
    /*Tipos de eventos
    0 - CrearConexion 
    1 - CrearHiloConexion
    2 - ProcesarConsultas
    3 - ProcesarTransaccion
    4 - EjecutarConsulta
    5 - TerminarConexion
    */

    
}
          
//clase que compara el tiempo de reloj de los eventos para ordenarlos 
    //Es usada por el priority queue para ordenar los eventos 
        static class MyComparator implements Comparator<Evento> { 
 
    public int compare(Evento a,Evento b) { 
        int result = new Double(a.getTiempo()).compareTo(b.getTiempo()); 
        return result; 
    } 
}
        
    MyComparator comparator = new MyComparator();
    PriorityQueue<Evento> eventos = new PriorityQueue<>(13,comparator);
    
    int reloj = 0; //ariable que cuente el tiempo actual en el cual nos encontramos dentro del sistema
    
    int timeOutGlobal = 0;
    int tiempoActual = 0;
    
    int conexionesRechazadas = 0;

    Random r = new Random();
    AdmClientes admC;
    AdmProcesos admP;
    ProcesamientoConsultas pc;
    double TiempoProxHilo;
    Transacciones transacciones;
    
    void crearConexion(){
       if(admC.hayServidor()){ //si hay servidores desocupados
          admC.crearConexion(tiempoActual,timeOutGlobal); //se crea la conexion y se pone la posicion en ocupado 
          Evento siguienteCreacionHilo= new Evento();
          siguienteCreacionHilo.id = 1;
          siguienteCreacionHilo.tiempo = admP.generarTiempoSalida() + reloj;
          siguienteCreacionHilo.c = admC.getSiguienteConexion();
          eventos.add(siguienteCreacionHilo);
          
            //CrearHiloConexion(admC.getSiguienteConexion());
       }
       else{
       conexionesRechazadas++;
       }
       
        Evento siguienteLlegada = new Evento();
        siguienteLlegada.id = 0;
        siguienteLlegada.tiempo = Conexion.generarTiempoArribo(r.nextDouble()) + reloj;
        eventos.add(siguienteLlegada);
    }
    
    void CrearHiloConexion(Conexion c){   
        
          admP.crearHilo(c); 
          
          Conexion siguiente = admP.SiguienteConexion();
                 if(siguiente.getTimeout() > reloj){
                        admC.eliminarConexion(c.getNumServidor());
                  }
          Evento siguienteCreacionHilo= new Evento();
          siguienteCreacionHilo.id = 2;
          siguienteCreacionHilo.tiempo = admP.generarTiempoSalida() + reloj;
          eventos.add(siguienteCreacionHilo);
          siguienteCreacionHilo.c = siguiente;
         //ProcesarConsultas(siguiente);
        
    }
    
    void ProcesarConsultas(Conexion c){
    
       //Conexion c =  admP.SiguienteConexion();
       if(c.getTimeout() > reloj){
       admP.liberarServidor();
       admC.eliminarConexion(c.getNumServidor());
       }
       
       else{
           
           double procesado = pc.asignarConsultaAServidor(c,reloj);
           
         if(procesado != -1){ //se asigna el servidor y se calcula el tiempo de reloj en donde terminara de procesarse     
            Evento siguienteConsultaProcesada = new Evento();
            siguienteConsultaProcesada.id = 2;
            siguienteConsultaProcesada.tiempo = procesado;
            eventos.add(siguienteConsultaProcesada);
           // ProcesarTransaccion(siguienteConsultaProcesada);
        }
       }  
    }
    
    void ProcesarTransaccion(Conexion c){
       if(c.getTimeout() > reloj){
       pc.eliminarConexionServidor(c);
       admP.liberarServidor();
       admC.eliminarConexion(c.getNumServidor());
       }
       
       else{
       transacciones.asignarConexion(c);
       }
       
    }
    
    void EjecutarConsulta(Conexion c, int numBloques){
        
       if(c.getTimeout() > reloj){
         pc.eliminarConexionServidor(c);
         admP.liberarServidor();
         admC.eliminarConexion(c.getNumServidor());
       }
       else{
        pc.calcularTiempoAlgoritmoEjecucion(numBloques, c);
        
       }    
    }
    
    void TerminarConexion(int numBloques){
        int R = numBloques;
        
    }
    
    
    void IniciarSimulación(int numC){
       
        
        
    }
    
}
