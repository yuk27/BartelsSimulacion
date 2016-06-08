package io;
import java.util.*;
import java.util.Random;

public class Simulacion {

    private Comparador comparador = new Comparador();
    public PriorityQueue<Evento> eventos = new PriorityQueue<>(13,comparador);
    Estadisticas estadistica = new Estadisticas();
    
    private double reloj = 0; //variable que cuenta el tiempo actual en el cual nos encontramos dentro del sistema
    private Menu menu;
    private int timeOutGlobal = 0;
    private int conexionesRechazadas = 0;
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

    private void crearConexion(){
          
        if(admC.crearConexion(reloj)){ 
          crearHiloConexion(admC.getSiguienteConexion());
          menu.aplicarInterfazClientes(admC.getUsedConexiones(),reloj);//pruebas de interfaz
        }
        else{
          conexionesRechazadas++;  //sino se puede guardar se agrega al contador de rechazadas
          menu.aplicarInterfazRechazadas(conexionesRechazadas,reloj);
        }
        admC.crearArribo(reloj,r.nextDouble(),eventos);
        }
 
    private void crearHiloConexion(Conexion c){  
        admP.crearHilo(c,reloj); //se guarda la conexion entrante ya sea en el servidor si no hay cola, o se agrega a la cola
        if(admP.getServidor()){
            if(c.getTimeout() > reloj){                                              
                admC.eliminarConexion(c.getNumServidor(),reloj);         //elimnamos la conexion en timeout
                admP.procesarSalida(reloj,eventos);   
             }
            else{
                admP.siguienteHilo(c,reloj,eventos);
            }
            admP.setServidor();
        }
    }
    
    private void procesarConsultas(Conexion c){
        admP.procesarSalida(reloj,eventos);    //ordena el servidor anterior
        if(c.getTimeout() > reloj){
            admC.eliminarConexion(c.getNumServidor(),reloj);
       }
       else{           
        pc.asignarConsultaAServidor(c, reloj,eventos); //se asigna el servidor y se calcula el tiempo de reloj en donde terminara de procesarse       
       }  
    }

    
    private void procesarTransaccion(Conexion c){       
        pc.procesarSalida(c,eventos);   //ordena el servidor de consultas con respecto a la conexion que sale. 
        if(c.getTimeout() > reloj){     //si hay timeout elimina
            admC.eliminarConexion(c.getNumServidor(),reloj);
       }
       else{
            transacciones.asignarConexion(c,eventos,reloj);//se agrega la conexion al servidor de transaciones o a la cola
        }     
    }
   
    private void ejecutarConsulta(Conexion c){
       transacciones.administrarServidorDeTransacciones(c,eventos);       //acomoda el servidor anterior con la nueva conexion
       if(c.getTimeout() > reloj){
            admC.eliminarConexion(c.getNumServidor(),reloj);
       }
       else{
            pc.asignarConsultaAEjecutor(c,eventos);
       }    
    }
    
    private void ponerResultadoEnRed(Conexion c){
        pc.administrarEjecutor(c,eventos);
        admC.sacarDelSistema(c,pc.calcularTamanoRespuesta(c.getNumBloques()),eventos);
    }
    
    private void terminarConexion(Conexion c){
        admC.eliminarConexion(c.getNumServidor(),reloj);
    }
    
    void iniciarSimulación(int numC, double tiempoMax,int k,int n, int p, int m,double t,Menu menu){    
        System.out.println(numC+"-"+tiempoMax+" - "+k+ " - " + n+ " - " + p+" - "+m +" - " +t);
        admC = new ModuloAdmClientes(k,timeOutGlobal,menu);
        admP = new ModuloAdmProcesos(menu);
        pc = new ModuloProcesamientoConsultas(n,m,menu);
        transacciones = new ModuloTransacciones(p,menu);
        this.menu = menu;
        this.correrSimulacion(numC,tiempoMax);
    }
    
    private void correrSimulacion(int numC, double tiempoMax){
        this.crearConexion();
        for(int i = 0; i < numC; i++){
            while(reloj < tiempoMax){
                Evento siguienteEvento = eventos.poll();
                reloj = siguienteEvento.getTiempo();
                System.out.println(reloj);
                switch(siguienteEvento.getTipo()){
                    case LLEGA_CONEXION:
                        this.crearConexion();
                        break;
                    case SALE_DE_HILO:
                        this.procesarConsultas(siguienteEvento.getConexion());
                        break;
                    case PROCESO_CONSULTA:
                        this.procesarTransaccion(siguienteEvento.getConexion());
                        break;
                    case SALE_DE_TRANSACCIONES:
                        this.ejecutarConsulta(siguienteEvento.getConexion());
                        break;
                    case EJECUTO_CONSULTA:
                        this.ponerResultadoEnRed(siguienteEvento.getConexion());
                        break;
                    case TERMINO_CONEXION:
                        this.terminarConexion(siguienteEvento.getConexion());
                        break;  
                }
            }
            
            System.out.println("Termino: " + i);
        }
        
        System.out.println("Termino todo");
    }
}
