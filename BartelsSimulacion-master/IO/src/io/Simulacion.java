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
    0 - Llega conexion
    1 - Sale del servidor de creacion de hilos
    2 - Salir del servidor de consultas
    3 - Salir del servidor de transacciones
    4 -Salir del servidor que ejecuta las consultas
    5 - Termina conexion y sale del sistema 
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
    
    double reloj = 0;                          //variable que cuenta el tiempo actual en el cual nos encontramos dentro del sistema
    
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
       if(admC.hayServidor()){                                                  //si hay servidores desocupados
          admC.crearConexion(reloj, timeOutGlobal);    //se crea la conexion y se pone la posicion del servidor en ocupado
          crearHiloConexion(admC.getSiguienteConexion());
       }
       else{
            conexionesRechazadas++;                             //sino se puede guardar se agrega al contador de rechazadas
       }
       
        Evento siguienteLlegada = new Evento();             //se genera el evento para la siguiente entrada de una conexion
        siguienteLlegada.id = 0;
        siguienteLlegada.tiempo = Conexion.generarTiempoArribo(r.nextDouble()) + reloj; 
        eventos.add(siguienteLlegada);                          // se agrega a la lista de eventos.
    }
    
    void crearHiloConexion(Conexion c){  
                admP.crearHilo(c);                                                      //se guarda la conexion entrante ya sea en el servidor si no hay cola, o se agrega a la cola
                if(admP.getServidor()){
                    if(c.getTimeout() > reloj){                                              
                     admC.eliminarConexion(c.getNumServidor());         //elimnamos la conexion en timeout
                     administrarServidorDeCreacionHilo();
                     }
                    else{
                        Evento siguienteHilo= new Evento();             //se genera el evento de Procesado de consulta de la siguiente conexion 
                        siguienteHilo.id = 1;
                        siguienteHilo.tiempo = admP.generarTiempoSalida() + reloj;
                        siguienteHilo.c = c;
                        eventos.add(siguienteHilo); 
                    }
                    admP.SetServidor();
                }
    }
    
    void administrarServidorDeCreacionHilo(){       //acomoda el servidor de acuerdo a la salida. Si hay conexiones en cola, lo pasa a servicio, sino, libera el servidor
        admP.administrarServidor();
        if(admP.servidorOcupado){
                Evento siguienteHilo= new Evento();             //se genera el evento de Procesado de consulta de la siguiente conexion 
                siguienteHilo.id = 1;
                siguienteHilo.tiempo = admP.generarTiempoSalida() + reloj;
                siguienteHilo.c = admP.SiguienteConexion();
                eventos.add(siguienteHilo); 
        }
    }
    
    void procesarConsultas(Conexion c){
        administrarServidorDeCreacionHilo();        //ordena el servidor anterior
        
        if(c.getTimeout() > reloj){
                admC.eliminarConexion(c.getNumServidor());
       }
       else{           
                double procesado = pc.asignarConsultaAServidor(c, reloj);             //se asigna el servidor y se calcula el tiempo de reloj en donde terminara de procesarse       
                if(procesado != -1){                                                                             //De devolver un tiempo -1 significa que el servidor estaba ocupado y se agrego la conexion a la lista de espera
                    Evento siguienteConsultaProcesada = new Evento();
                    siguienteConsultaProcesada.id = 2;
                    siguienteConsultaProcesada.tiempo = procesado;
                    eventos.add(siguienteConsultaProcesada);
                }
       }  
    }
    
    void administrarServidorDeConsultas(Conexion c){             //ordena el servidor de consultas con respecto a la conexion que sale. 
            Conexion nuevaConexion = pc.administrarServidor(c);
            if(nuevaConexion != null){
                Evento siguienteConsultaProcesada = new Evento();
                siguienteConsultaProcesada.id = 2;
                siguienteConsultaProcesada.tiempo = pc.calcularTiempoTotal(nuevaConexion);
                eventos.add(siguienteConsultaProcesada);
            }
    }
    
    void procesarTransaccion(Conexion c){       
        administrarServidorDeConsultas(c);
        if(c.getTimeout() > reloj){                                  //si hay timeout elimina
            admC.eliminarConexion(c.getNumServidor());
       }
       else{
            if(transacciones.asignarConexion(c)){                        //se agrega la conexion al servidor de transaciones o a la cola
                  calcularTiempoTransaccion(c);
           }
        }     
    }
    
    void calcularTiempoTransaccion(Conexion c){
         Evento siguienteTransaccion = new Evento();         //se genera el evento
         siguienteTransaccion.id = 3;

         switch(c.getTipo()){
             case 0:        //SELECT
                 siguienteTransaccion.tiempo = transacciones.calcularTiempoTransaccion() + 1/10;
                 c.numBloques = 1;
                 break;
             case 2:        //JOIN
                 c.numBloques = (int)transacciones.randomWithRange(1, 64);
                 siguienteTransaccion.tiempo = transacciones.calcularTiempoTransaccion() + (c.numBloques * 1/10);     //se calcula el tiempo del join
                 break;
             case 3:        //DDL
                 siguienteTransaccion.tiempo = transacciones.calcularTiempoTransaccion() + transacciones.mayorTiempoEjecucion;
                 transacciones.setDDL();
                 break;
             default:       //UPDATE
                 siguienteTransaccion.tiempo = transacciones.calcularTiempoTransaccion();  
                 break;
         }
         if(c.getTipo() != 3 && siguienteTransaccion.tiempo > transacciones.mayorTiempoEjecucion){
                transacciones.mayorTiempoEjecucion = siguienteTransaccion.tiempo;
         }
         eventos.add(siguienteTransaccion);
    }
    
    void administrarServidorDeTransacciones(Conexion c){            //ordena el servidor de transacciones con las coenxiones en cola
        Conexion nuevaConexion = transacciones.administrarServidorDeTransacciones(c);
        if(nuevaConexion != null){
                calcularTiempoTransaccion(nuevaConexion);
        }
    }
    
    void ejecutarConsulta(Conexion c){
       administrarServidorDeTransacciones(c);       //acomoda el servidor anterior con la nueva conexion
       if(c.getTimeout() > reloj){
         admC.eliminarConexion(c.getNumServidor());
       }
       else{
            if(pc.asignarConsultaAEjecutor(c)){         
                    Evento siguienteEjecucion = new Evento();
                    siguienteEjecucion.id = 4;
                    siguienteEjecucion.tiempo = pc.calcularTiempoAlgoritmoEjecucion(c.numBloques, c);
                    eventos.add(siguienteEjecucion);
            }
       }    
    }
    
    void administrarEjecutor(Conexion c){
        Conexion nuevaConexion = pc.administrarEjecutor(c);
        if(nuevaConexion != null){
                Evento siguienteConsultaProcesada = new Evento();
                siguienteConsultaProcesada.id = 4;
                siguienteConsultaProcesada.tiempo = pc.calcularTiempoAlgoritmoEjecucion(c.numBloques, c);
                eventos.add(siguienteConsultaProcesada);
        }
    }
    
    void ponerResultadoEnRed(Conexion c){
        administrarEjecutor(c);
        Evento siguienteSalidaDelSistema = new Evento();
        siguienteSalidaDelSistema.id = 5;
        siguienteSalidaDelSistema.tiempo =  admC.ponerEnRed(pc.calcularTamanoRespuesta(c.numBloques));
        eventos.add(siguienteSalidaDelSistema);
    }
    
    void terminarConexion(Conexion c){
        admC.eliminarConexion(c.getNumServidor());
    }
    
    void iniciarSimulación(int numC){       
        admC = new AdmClientes(10);
        admP = new AdmProcesos();
        pc = new ProcesamientoConsultas(10, 10);
        transacciones = new Transacciones(10);
        
        correrSimulacion(numC);
    }
    
    void correrSimulacion(int numC){
        crearConexion();
        
        for(int i = 0; i < numC; i++){
            Evento siguienteEvento = eventos.poll();
            reloj = siguienteEvento.tiempo;
            switch(siguienteEvento.id){
                case 0:
                    crearConexion();
                    break;
                case 1:
                    procesarConsultas(siguienteEvento.c);
                    break;
                case 2:
                    procesarTransaccion(siguienteEvento.c);
                    break;
                case 3:
                    ejecutarConsulta(siguienteEvento.c);
                    break;
                case 4:
                    ponerResultadoEnRed(siguienteEvento.c);
                    break;
                case 5:
                    terminarConexion(siguienteEvento.c);
                    break;  
            }
        }
    }
}
