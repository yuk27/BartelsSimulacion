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
    
    int reloj = 0; //variable que cuenta el tiempo actual en el cual nos encontramos dentro del sistema
    
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
       if(admC.hayServidor()){                                              //si hay servidores desocupados
          admC.crearConexion(tiempoActual,timeOutGlobal); //se crea la conexion y se pone la posicion del servidor en ocupado 
          Evento siguienteCreacionHilo= new Evento();       //Se crea el evento de salida (entrada al sigueinte modulo)
          siguienteCreacionHilo.id = 1;                                
          siguienteCreacionHilo.tiempo = admP.generarTiempoSalida() + reloj;    
          siguienteCreacionHilo.c = admC.getSiguienteConexion();                    //se guarda la conexion en el evento que se acaba de hacer
          eventos.add(siguienteCreacionHilo);                    //se agrega el evento a la lista de eventos
       }
       else{
            conexionesRechazadas++;                             //sino se puede guardar se agrega al contador de rechazadas
       }
       
        Evento siguienteLlegada = new Evento();             //se genera el evento para la siguiente entrada de una conexion
        siguienteLlegada.id = 0;
        siguienteLlegada.tiempo = Conexion.generarTiempoArribo(r.nextDouble()) + reloj; 
        eventos.add(siguienteLlegada);                          // se agrega a la lista de eventos.
    }
    
    void CrearHiloConexion(Conexion c){  //arreglar este metodo. Si entra una conexion directo al servidor, se le calcular el tiempo, sino se mete  a la cola. Cuando sale es otro evento. 
        
         if(c.getTimeout() > reloj){                                                //Recuperamos el siguiente evento hasta que no este en timeout
                admC.eliminarConexion(c.getNumServidor());         //elimnamos la conexion en timeout
          }  
         else{
                admP.crearHilo(c);                                                      //se guarda la conexion entrante ya sea en el servidor si no hay cola, o se agrega a la cola
                if(admP.getServidor()){
                        Evento siguienteHilo= new Evento();             //se genera el evento de Procesado de consulta de la siguiente conexion 
                        siguienteHilo.id = 2;
                        siguienteHilo.tiempo = admP.generarTiempoSalida() + reloj;
                        siguienteHilo.c = c;
                        eventos.add(siguienteHilo); 
                        admP.SetServidor();
                }
         }
    }
    
    void administrarServidorDeCreacionHilo(){
        admP.administrarServidor();
        if(admP.servidorOcupado){
                Evento siguienteHilo= new Evento();             //se genera el evento de Procesado de consulta de la siguiente conexion 
                siguienteHilo.id = 2;
                siguienteHilo.tiempo = admP.generarTiempoSalida() + reloj;
                siguienteHilo.c = admP.SiguienteConexion();
                eventos.add(siguienteHilo); 
        }
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
    
    void AdministrarConsultas(Conexion c){ 
    int indice = 0; //variable donde se meterá la conexion que saldrá
       indice = pc.eliminarConexionServidor(c); //se mete su posicion en servidor
       
       if(pc.consultas.size() > 0){ //si hay alguien en cola
           pc.servidoresConsultas[indice] = pc.consultas.remove(0).numServidor; //metemos el siguiente en cola en servidor y se borra de la lista
       } 
    }
    
    void ProcesarTransaccion(Conexion c){
        
       if(c.getTimeout() > reloj){ //si hay timeout elmina
       pc.eliminarConexionServidor(c);
       admP.liberarServidor();
       admC.eliminarConexion(c.getNumServidor());
       }
       
       else{
           
       AdministrarConsultas(c); //se elimina del servidor de procesar consultas y se agrega una nueva
      if(transacciones.asignarConexion(c)){  //se agrega la conexion al servidor de transaciones o a la cola
          
        Evento siguienteTransaccion = new Evento(); //se genera el evento
        siguienteTransaccion.id = 3;
   
       if(c.getTipo() == 3){ //si el evento en salir es un ddl
           siguienteTransaccion.tiempo = transacciones.calcularTiempoTransaccion() + transacciones.mayorTiempoEjecucion;
        }
        else{ //si no es dll le calculo el tiempo (p*0.03) y se calcula su tiempo de ejecucion
           
            if(c.getTipo() == 2){ //si el evento en salir es un join
                 siguienteTransaccion.tiempo = transacciones.calcularTiempoTransaccion() + (transacciones.randomWithRange(1,64) *1/10); //se calcula el tiempo del join
            }
            else if(c.getTipo() == 0){
                siguienteTransaccion.tiempo = transacciones.calcularTiempoTransaccion() + 1/10; //se calcula el tiempo del select
            }
            else {
                siguienteTransaccion.tiempo = transacciones.calcularTiempoTransaccion(); //se calcula el tiempo del select
            }
           
            if(siguienteTransaccion.tiempo > transacciones.mayorTiempoEjecucion){
                transacciones.mayorTiempoEjecucion = siguienteTransaccion.tiempo;
                }
             }
       
       eventos.add(siguienteTransaccion);
            }
        }
       
       
    }
    
    
    //HAY QUE ELIMINAR DEL SERVIDOR DE TRANSACCIONES EL C CON NOS ENTRE
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
