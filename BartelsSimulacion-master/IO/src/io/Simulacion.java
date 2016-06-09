package io;
import java.util.*;
import java.util.Random;

public class Simulacion {
    
        //----------------------------------------DECLARACION DE VARIABLES-------------------------------------------------------------------
        
    Comparador comparador = new Comparador();
    PriorityQueue<Evento> eventos = new PriorityQueue<>(13,comparador);
    Estadisticas estadistica = new Estadisticas();
    
    private double reloj = 0;                          //variable que cuenta el tiempo actual en el cual nos encontramos dentro del sistema
    private Menu menu;
    private int timeOutGlobal = 0;
    private int tiempoActual = 0;
    
    int conexionesRechazadas = 0;

    private Random r = new Random();
    private ModuloAdmClientes admC;
    private ModuloAdmProcesos admP;
    private ModuloProcesamientoConsultas pc;
    private ModuloTransacciones transacciones;
    
    //lista que tiene las estadisticas de cada corrida de la simulacion
    private ArrayList estadisticas = new ArrayList();
    
    //vectores que llevan cuenta de los tamanos de la cola generados por cada corrida
    private Vector<Integer> colaClientes = new Vector<>();
    private Vector<Integer> colaProcesos = new Vector<>();
    private Vector<Integer> colaProcesamientoConsutlas = new Vector<>();
    private Vector<Integer> colaTransacciones = new Vector<>();
    
    //Vectores que llevan cuenta de los tiempos de cada conexion en los diferentes modulos 
    private Vector<Double> tiempoPromedio = new Vector<>();
    private Vector<Double> tiempoSelect = new Vector<>();    
    private Vector<Double> tiempoUpdate = new Vector<>(); 
    private Vector<Double> tiempoJoin = new Vector<>();
    private Vector<Double> tiempoDDL = new Vector<>();
    
    
    //----------------------------------FIN DE DECLARACION DE VARIABLES---------------------------------------------------------
    
    void crearConexion(){                                              
        if(admC.crearConexion(reloj)){ 
          crearHiloConexion(admC.getSiguienteConexion());
          menu.aplicarInterfazClientes(admC.getUsedConexiones());//pruebas de interfaz
        }
        else{
          conexionesRechazadas++;  //sino se puede guardar se agrega al contador de rechazadas
          menu.aplicarInterfazRechazadas(conexionesRechazadas);
        }
        Evento siguienteLlegada = new Evento(Conexion.generarTiempoArribo(r.nextDouble()) + reloj,null,TipoEvento.LLEGA_CONEXION); //se genera el evento para la siguiente entrada de una conexion
        eventos.add(siguienteLlegada);                          // se agrega a la lista de eventos.
        }
    
    void crearHiloConexion(Conexion c){  
        admP.crearHilo(c); //se guarda la conexion entrante ya sea en el servidor si no hay cola, o se agrega a la cola
        if(admP.getServidor()){
            if(c.getTimeout() > reloj){                                              
             admC.eliminarConexion(c.getNumServidor());         //elimnamos la conexion en timeout
             administrarServidorDeCreacionHilo();
             }
            else{
                Evento siguienteHilo= new Evento(admP.generarTiempoSalida() + reloj,c,TipoEvento.SALE_DE_HILO); //se genera el evento de Procesado de consulta de la siguiente conexion 
                eventos.add(siguienteHilo); 
            }
            admP.SetServidor();
        }
    }
    
    void administrarServidorDeCreacionHilo(){ //acomoda el servidor de acuerdo a la salida. Si hay conexiones en cola, lo pasa a servicio, sino, libera el servidor
        if(admP.administrarServidor()){
            Evento siguienteHilo= new Evento(admP.generarTiempoSalida() + reloj,admP.SiguienteConexion(),TipoEvento.SALE_DE_HILO);             //se genera el evento de Procesado de consulta de la siguiente conexion 
            eventos.add(siguienteHilo); 
        }
    }
    
    void procesarConsultas(Conexion c){
        administrarServidorDeCreacionHilo();        //ordena el servidor anterior
        if(c.getTimeout() > reloj){
            admC.eliminarConexion(c.getNumServidor());
       }
       else{           
        double procesado = pc.asignarConsultaAServidor(c, reloj); //se asigna el servidor y se calcula el tiempo de reloj en donde terminara de procesarse       
        if(procesado != -1){ //De devolver un tiempo -1 significa que el servidor estaba ocupado y se agrego la conexion a la lista de espera
            Evento siguienteConsultaProcesada = new Evento(procesado,c,TipoEvento.EJECUTO_CONSULTA);
        }
       }  
    }
    
    void administrarServidorDeConsultas(Conexion c){             //ordena el servidor de consultas con respecto a la conexion que sale. 
            Conexion nuevaConexion = pc.administrarServidor(c);
            if(nuevaConexion != null){
                Evento siguienteConsultaProcesada = new Evento(pc.calcularTiempoTotal(nuevaConexion),c,TipoEvento.EJECUTO_CONSULTA);
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
        
        double tiempo;
         
         switch(c.getTipo()){
             case 0:        //SELECT
                 tiempo = transacciones.calcularTiempoTransaccion() + 1/10;
                 c.setNumBloques(1);
                 break;
             case 2:        //JOIN
                 c.setNumBloques((int)transacciones.randomWithRange(1, 64));
                 tiempo = transacciones.calcularTiempoTransaccion() + (c.getNumBloques() * 1/10); //se calcula el tiempo del join
                 break;
             case 3:        //DDL
                 tiempo = transacciones.calcularTiempoTransaccion() + transacciones.getMayorTiempoEjecucion();
                 transacciones.setDDL();
                 break;
             default:       //UPDATE
                 tiempo = transacciones.calcularTiempoTransaccion();  
                 break;
         }
         
         Evento siguienteTransaccion = new Evento(tiempo,c,TipoEvento.SALE_DE_TRANSACCIONES);
         
         if(c.getTipo() != 3 && siguienteTransaccion.getTiempo() > transacciones.getMayorTiempoEjecucion()){
            transacciones.setMayorTiempoEjecucion(siguienteTransaccion.getTiempo());
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
                Evento siguienteEjecucion = new Evento(pc.calcularTiempoAlgoritmoEjecucion(c.getNumBloques(),c),c,TipoEvento.EJECUTO_CONSULTA);
                eventos.add(siguienteEjecucion);
            }
       }    
    }
    
    void administrarEjecutor(Conexion c){
        Conexion nuevaConexion = pc.administrarEjecutor(c);
        if(nuevaConexion != null){
                Evento siguienteConsultaProcesada = new Evento(pc.calcularTiempoAlgoritmoEjecucion(c.getNumBloques(), c),c,TipoEvento.EJECUTO_CONSULTA);
                eventos.add(siguienteConsultaProcesada);
        }
    }
    
    void ponerResultadoEnRed(Conexion c){
        administrarEjecutor(c);
        Evento siguienteSalidaDelSistema = new Evento(admC.ponerEnRed(pc.calcularTamanoRespuesta(c.getNumBloques())),c,TipoEvento.TERMINO_CONEXION);
        eventos.add(siguienteSalidaDelSistema);
    }
    
    void terminarConexion(Conexion c){
        admC.eliminarConexion(c.getNumServidor());
    }
    
    void iniciarSimulación(int numC, double tiempoMax,int k,int n, int p, int m,double t,Menu menu){       
        admC = new ModuloAdmClientes(k,timeOutGlobal);
        admP = new ModuloAdmProcesos();
        pc = new ModuloProcesamientoConsultas(n,m);
        transacciones = new ModuloTransacciones(p);
        this.menu = menu;
        correrSimulacion(numC,tiempoMax);
    }
    
    void correrSimulacion(int numC, double tiempoMax){
        System.out.println("simulando");
        crearConexion();
        for(int i = 0; i < numC; i++){
            while(reloj < tiempoMax){
                Evento siguienteEvento = eventos.poll();
                reloj = siguienteEvento.getTiempo();
                switch(siguienteEvento.getTipo()){
                    case LLEGA_CONEXION:
                        crearConexion();
                        break;
                    case SALE_DE_HILO:
                        procesarConsultas(siguienteEvento.getConexion());
                        break;
                    case PROCESO_CONSULTA:
                        procesarTransaccion(siguienteEvento.getConexion());
                        break;
                    case SALE_DE_TRANSACCIONES:
                        ejecutarConsulta(siguienteEvento.getConexion());
                        break;
                    case EJECUTO_CONSULTA:
                        ponerResultadoEnRed(siguienteEvento.getConexion());
                        break;
                    case TERMINO_CONEXION:
                        terminarConexion(siguienteEvento.getConexion());
                        break;  
                }
            }
        }
    }
}
