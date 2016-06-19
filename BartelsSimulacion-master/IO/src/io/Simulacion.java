package io;
import java.util.*;
import java.util.Random;

/**
 * Clase principal encargada de controlar el ciclo de ejecución de la simulación, 
 * crear los eventos y llamar a los distintos modulos.
 */
public class Simulacion {

    private Comparador comparador = new Comparador(); //comparador utilizado para ordenar los eventos por tiempos de reloj 
    public PriorityQueue<Evento> eventos = new PriorityQueue<>(13,comparador); //lista de eventos
    
    private double reloj = 0; //variable que cuenta el tiempo actual en el cual nos encontramos dentro del sistema
    private Menu menu;
    private double timeOutGlobal = 0; //variable que contiene el tiempo que durará desde ser creado un evento hasta ser eliminado por timeout
    private int conexionesRechazadas = 0; 
    private int conexionesTerminadas = 0;
    private int conexionesBorradasTimeOut = 0;
    private Random r = new Random();
    private ModuloAdmClientes admC;
    private ModuloAdmProcesos admP;
    private ModuloProcesamientoConsultas pc;
    private ModuloTransacciones transacciones;
    
    //lista que tiene las estadisticas de cada corrida de la simulacion
    private ArrayList estadisticas = new ArrayList();
    
    //Vectores que llevan cuenta de los tiempos de cada conexion en los diferentes modulos 
    private Vector<Double> tiempoPromedio = new Vector<>();

    
    /**
    * Método encargado de llamar a la creación de  crear la conexión inicial a la base de datos 
    * y redireccionar la conexion a coneciones rechazadas si el servidor esta lleno, 
    * o a el hilo de procesamiento de otra forma. 
    */
    private void crearConexion(){
        if(admC.crearConexion(reloj)){ //si la conexión pudo ser agregada al servidor 
          crearHiloConexion(admC.getSiguienteConexion()); //crea un hilo de conexión
          menu.aplicarInterfazClientes(admC.getUsedConexiones(),reloj);//refrezca el panel de conexiones en la interfaz
        }
        else{
          conexionesRechazadas++;  //sino se puede guardar se agrega al contador de rechazadas
          menu.aplicarInterfazRechazadas(conexionesRechazadas,reloj); //refrezca el panel de rechazadas
        }
        admC.crearArribo(reloj,r.nextDouble(),eventos); //se genera la proxima entrada al sistema.
        }
 
    /**
    * Método encargado de llamar a la creación de la conexión con hilo de procesamiento, y
    * llama al metodo que genera el evento de entrada al modulo de procesos o 
    * definir si la conexión esta en timout y elimnarla de ser necesario. 
    * @param c conexión perteneciente al evento siendo ejecutado
    */
    
    private void crearHiloConexion(Conexion c){      
        admP.crearHilo(c,reloj, eventos); //se pone la conexion entrante ya sea en el servidor si no hay cola, o se agrega a la cola
        if(admP.getServidor()){           
            if(c.getTimeout() < reloj){ //si la conexión esta en tiemout                
                admC.eliminarConexion(c.getNumServidor(),reloj); //eliminamos la conexion en timeout
                admP.procesarSalida(reloj,eventos); //y si hay conexiones en cola se trae la siguiente para ser procesada  
             }
            else{
                admP.siguienteHilo(c,reloj,eventos); //se genera el evento salida del procesamiento del hilo y paso al servidor de procesos.
            }
            admP.setServidor();  //se limpia el servidor 
        }
    }
    
    /**
    * Método encargado de llamar a la creación de la conexión con el modulo de prosesamiento de consulta y
    * llama al metodo que genera el evento de entrada al modulo de procesos o 
    * definir si la conexión esta en timeout y elimnarla de ser necesario. 
    * @param c conexión perteneciente al evento siendo ejecutado
    */
    private void procesarConsultas(Conexion c){ 
        admP.procesarSalida(reloj,eventos); //ordena el servidor del hilo de procesamiento con respecto a la conexion que sale.        
        if(c.getTimeout() < reloj){ 
            admC.eliminarConexion(c.getNumServidor(),reloj);
       }
       else{           
            pc.asignarConsultaAServidor(c, reloj,eventos, admP); //se asigna el servidor y se calcula el tiempo de reloj en donde terminara de procesarse       
       }  
    }

    /**
    * Método encargado de crear la conexión con el modulo de transacciones y de
    * llamar al metodo que genera el evento que lo devuelve al modulo de procesos y lo ejecuta ,o en otro caso 
    * definir si la conexión esta en timeout y eliminarla de ser necesario.
    * @param c conexión perteneciente al evento siendo ejecutado
    */
    private void procesarTransaccion(Conexion c){       
        pc.procesarSalidaConsulta(c,eventos, reloj); //ordena el servidor de consultas con respecto a la conexion que sale. 
        if(c.getTimeout() < reloj){     //si hay timeout elimina
            admC.eliminarConexion(c.getNumServidor(),reloj);
       }
       else{
            transacciones.asignarConexion(c,eventos,reloj, pc);//se agrega la conexion al servidor de transaciones o a la cola
        }     
    }
   
    /**
    * Método encargado de ejecutar la consulta en el modulo de procesamiento y de
    * llamar al metodo que genera el evento que lo devuelve al modulo de procesos y lo ejecuta ,o en otro caso 
    * definir si la conexión esta en timeout y eliminarla de ser necesario. 
    * @param c conexión perteneciente al evento siendo ejecutado
    */
    private void ejecutarConsulta(Conexion c){
       transacciones.procesarSalida(c,eventos, reloj); //acomoda el servidor de transacciones con la nueva conexion
       if(c.getTimeout() < reloj){
            admC.eliminarConexion(c.getNumServidor(),reloj);
       }
       else{
            pc.asignarConsultaAEjecutor(c,eventos, reloj, transacciones);
       }    
    }
    
    /**
    * Método encargado de llamar a la salida de la ejecución terminada del modulo de procesamiento 
    * y de llamar a la creación del sisguiente evento de salida
    * @param c conexión perteneciente al evento siendo ejecutado
    */
    private void ponerResultadoEnRed(Conexion c){
        pc.procesarSalidaEjecutor(c,eventos, reloj);
        admC.sacarDelSistema(c,pc.calcularTamanoRespuesta(c.getNumBloques()) + reloj, eventos, pc, reloj);
    }
    
    
    /**
    * Método encargado de eliminar la conexión del servidor de administración de clientes
    * y aumentar el contador de conexiones terminadas.
    * @param c conexión perteneciente al evento siendo ejecutado
    */
    private void terminarConexion(Conexion c){
        admC.eliminarConexion(c.getNumServidor(),reloj);
        this.conexionesTerminadas++;
        this.tiempoPromedio.add(reloj - c.getTiempoEntrada());
        menu.aplicarInterfazProcesarCierreConexion(conexionesTerminadas,reloj);
        conexionesTerminadas++;
        menu.aplicarInterfazProcesarCierreConexion(conexionesTerminadas,reloj); //se actualiza el panel de terminadas de la interfaz
    }
    
    /**
    * Método encargado de eliminar las conexiones que su timeout se cumplio,
    * elminandolo primero de todos los modulos y aumentando el contador de conexiones borradas por timeout
    * @param c conexión perteneciente al evento siendo ejecutado
    */
    private void procesarTimeout(Conexion c){
        admC.eliminarConexion(c.getNumServidor() , reloj); 
        admP.eliminarConexion(c);
        pc.eliminarConexion(c);
        transacciones.eliminarConexionTimeout(c);
        conexionesBorradasTimeOut++;
        menu.aplicarInterfazBorradasTimeOut(conexionesBorradasTimeOut,reloj);
    }
    
    private void calcularEstadisticas(){
        Estadisticas estadistica = new Estadisticas();
        
    //calcula tamanos promedios de las colas
        estadistica.calcularPromedioInt(admC.getCola(), 0);
        estadistica.calcularPromedioInt(admP.getCola(), 1);
        estadistica.calcularPromedioInt(pc.getColaConsultas(), 2);
        estadistica.calcularPromedioInt(transacciones.getCola(), 3);
        estadistica.calcularPromedioInt(pc.getColaEjecutor(), 4);
        
    //calcula tiempos promedios en el modulo de procesos
        estadistica.calcularTiempoPromedioProcesos(admP.getTiempoSelect(), 0);
        estadistica.calcularTiempoPromedioProcesos(admP.getTiempoUpdate(), 2);
        estadistica.calcularTiempoPromedioProcesos(admP.getTiempoJoin(), 1);
        estadistica.calcularTiempoPromedioProcesos(admP.getTiempoUpdate(), 3);
        
    //calcula tiempos promedios en el modulo de procesamiento de consultas
        estadistica.calcularTiempoPromedioConsultas(pc.getTiempoSelect(), 0);
        estadistica.calcularTiempoPromedioConsultas(pc.getTiempoUpdate(), 2);
        estadistica.calcularTiempoPromedioConsultas(pc.getTiempoJoin(), 1);
        estadistica.calcularTiempoPromedioConsultas(pc.getTiempoUpdate(), 3);
        
    //calcula tiempos promedios en el modulo de transacciones
        estadistica.calcularTiempoPromedioTransacciones(transacciones.getTiempoSelect(), 0);
        estadistica.calcularTiempoPromedioTransacciones(transacciones.getTiempoUpdate(), 2);
        estadistica.calcularTiempoPromedioTransacciones(transacciones.getTiempoJoin(), 1);
        estadistica.calcularTiempoPromedioTransacciones(transacciones.getTiempoUpdate(), 3);
        
    //calcula tiempos promedios en el modulo ejecutor
        estadistica.calcularTiempoPromedioEjecutor(pc.getTiempoSelectEjecutor(), 0);
        estadistica.calcularTiempoPromedioEjecutor(pc.getTiempoUpdateEjecutor(), 2);
        estadistica.calcularTiempoPromedioEjecutor(pc.getTiempoJoinEjecutor(), 1);
        estadistica.calcularTiempoPromedioEjecutor(pc.getTiempoUpdateEjecutor(), 3);        
        
        estadisticas.add(estadistica);
    }
  
    /**
     * Contructor de la clase, encargado de inicializar todos los modulos,
     * colocar los valores enviados de la interfaz en las variables de la clase correspondiente,
     * y ejecutar la simulación
     * @param numC número de corridas que se hará en la simulación
     * @param tiempoMax tiempo max que puede durar cada corrida
     * @param k conexiones maximas en el modulo de administración de clientes.
     * @param n conexiones maximas en el hilo de procesamiento.
     * @param p conexiones maximas en el servidor de procesamiento de consultas.
     * @param m conexiones maximas en el servidor de ejecución de consultas.
     * @param t conexiones maximas en el servidor de transacciones.
     * @param menu referencia al objeto de interfaz 
     */
    
    public void iniciarSimulación(int numC, double tiempoMax,int k,int n, int p, int m,double t,Menu menu){    
        System.out.println(numC+"-"+tiempoMax+" - "+k+ " - " + n+ " - " + p+" - "+m +" - " +t);
        this.timeOutGlobal = t;
        admC = new ModuloAdmClientes(k,timeOutGlobal,menu);
        admP = new ModuloAdmProcesos(menu);
        pc = new ModuloProcesamientoConsultas(n,m,menu);
        pc.inicializarVectores();
        transacciones = new ModuloTransacciones(p,menu);
        transacciones.inicializarVector();
        
        this.menu = menu;
        this.correrSimulacion(numC,tiempoMax);
    }
    
    
    /**
    * Método encargado de controlar la lógica de la simulación, 
    * recorriendo cada evento en la lista de eventos y llamando a los metodos encargados de su ejecución,
    * haciendo esto para el número de corridas elegidas por el usuario con el tiempo maximo para cada una.
    * @param numC número de corridas que se hará en la simulación
    * @param tiempoMax tiempo max que puede durar cada corrida
    */
    private void correrSimulacion(int numC, double tiempoMax){
        this.crearConexion(); //se genera la primera conexión para empezar la simulación
        
        for(int i = 0; i < numC; i++){ //se corre la simulación el número de veces indicadas por el usuario
            while(reloj < tiempoMax){ //mientras el tiempo de reloj sea menor al tiempo maximo de corrida
                
                Evento siguienteEvento = eventos.poll(); //se saca el siguiente evento
                reloj = siguienteEvento.getTiempo(); //se toma el tiempo de reloj actual
                System.out.println(reloj);
                System.out.println("evento de tipo: " + siguienteEvento.getTipo());
                switch(siguienteEvento.getTipo()){ //se elige el tipo de evento el cual se esta tratando
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
                    case TIMEOUT:
                        this.procesarTimeout(siguienteEvento.getConexion());
                        break;
                }
            }
            
            this.calcularEstadisticas();
            
            System.out.println("Termino: " + i);
        }
        if(!menu.isLento()){ //si la simulación esta funcionando en modo rapido, se llama el método que refresca la interfaz
        
            menu.ModoRapido(admC.getOcupados(), conexionesRechazadas, admP.getServidor(), admP.getConexionesNum(),pc.getOcupados(),pc.getConsultasNum(), transacciones.getOcupados(), transacciones.getConexionNum(), pc.getOcupadosEjecutor(), pc.getEjecutorNum(), conexionesTerminadas, reloj, conexionesBorradasTimeOut);
        
        }
        System.out.println("Termino todo");
    }
}
