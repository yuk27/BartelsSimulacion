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
          Evento siguienteCreacionHilo= new Evento();//Se crea el evento de salida
          siguienteCreacionHilo.id = 1; //se genera si id
          siguienteCreacionHilo.tiempo = admP.generarTiempoSalida() + reloj; //se guarda si momento de salida
          siguienteCreacionHilo.c = admC.getSiguienteConexion();//se guarda la conexion que se acaba de hacer
          eventos.add(siguienteCreacionHilo); //se agraga el evento a la lista de eventos
          
            //CrearHiloConexion(admC.getSiguienteConexion());
       }
       else{
       conexionesRechazadas++; //sino se puede guardar se agrega al contador de rechazadas
       }
       
        Evento siguienteLlegada = new Evento(); //se genera el evento para la siguiente entrada
        siguienteLlegada.id = 0;
        siguienteLlegada.tiempo = Conexion.generarTiempoArribo(r.nextDouble()) + reloj; //se guarda su momento de arribo
        eventos.add(siguienteLlegada);// y se agraga a la lista de eventos ya que no necesita conexion 
    }
    
    void CrearHiloConexion(Conexion c){  
        
          admP.crearHilo(c); //se guardar la conexion entrante ya sea en el servidor si no hay cola, o se agraga a la cola
          
          Conexion siguiente = admP.SiguienteConexion(); //Se recupera la conexion que este en el servidor para ser procesada
               
          while(siguiente.getTimeout() > reloj){//Recuperamos el siguiente evento hasta que no este en timeout
             admC.eliminarConexion(c.getNumServidor()); //elimnamos la conexion en timeout
             siguiente = admP.SiguienteConexion(); //y se pasa la siguiente al servidor
          }            
                 
          Evento siguienteCreacionHilo= new Evento(); //se genera el evento de Procesado de consulta de la siguiente conexion 
          siguienteCreacionHilo.id = 2;
          siguienteCreacionHilo.tiempo = admP.generarTiempoSalida() + reloj;
          siguienteCreacionHilo.c = siguiente;
          eventos.add(siguienteCreacionHilo);
        
    }
    
    void ProcesarConsultas(Conexion c){
    
       if(c.getTimeout() > reloj){
       admP.liberarServidor();
       admC.eliminarConexion(c.getNumServidor());
       }
       
       else{
           
           double procesado = pc.asignarConsultaAServidor(c,reloj);//se asigna el servidor y se calcula el tiempo de reloj en donde terminara de procesarse     
           
         if(procesado != -1){ //De devolver un tiempo -1 significa que el servidor estaba ocupado y se agrago la conexion a la lista de espera
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
