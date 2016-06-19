package io;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Vector;

/**
 * Clase que representa el modulo de administración de procesos,
 * el cual se encarga de generar el hilo de proceso como crear los eventos para la llamada 
 * a el modulo de consultas
 */
public class ModuloAdmProcesos {
     private Menu menu;
     private boolean servidorOcupado; //booleano que representa el servidor de hilo de proceso
     private boolean entroAlServidor = false;
     private List<Conexion> conexiones; //cola de conexiones
     private Random r = new Random();
     private Conexion enServicio;
     
    private Vector<Integer> colaProcesos;
    private Vector<Double> tiempoSelect;
    private Vector<Double> tiempoUpdate;
    private Vector<Double> tiempoJoin;
    private Vector<Double> tiempoDDL;
     
  /**
     *Contructor de la clase que tiene por proposito inicializar las variables del objeto 
     * @param menu //variable que conecta al modulo con el menu para poder hacer la llamadas necesarias para actualizar la interfaz.
     */
    public ModuloAdmProcesos(Menu menu){
         this.servidorOcupado = false;
         this.conexiones =  new ArrayList<>();
         this.colaProcesos = new Vector<>();
         this.tiempoSelect  = new Vector<>();    
         this.tiempoUpdate   = new Vector<>();    
         this.tiempoJoin   = new Vector<>();    
         this.tiempoDDL  = new Vector<>();   
         this.menu = menu;
     }
     
    /**
     * Metodo encargado d eliminar la conexión que asi lo requiera
     * @param c conexión por eliminar
     */
    public void eliminarConexion(Conexion c){
         for(int i = 0; i < conexiones.size(); i++){
             if(conexiones.get(i) == c){
                 conexiones.remove(i);
                 return;
             }
         }
     }
     
    /**
     * Metodo que devuelve la cola procesos de estadisticas 
     * @return devuelve una lista de integers representando el numero procesos
     * hechos a lo largo de la simulación 
     */
    public Vector<Integer> getCola(){
        return this.colaProcesos;
    }
     
    /**
     * Metodo encargado de meter la conexion entrante el hilo de ejecución de estar libre, sino se inserta en cola
     * @param c conexion entrante
     * @param reloj tiempo de reloj en el momento de la ejecucion del metodo
     * @param eventos Lista de eventos en donde se insertará el timeout de ser necesario
     */
    public void crearHilo(Conexion c,double reloj, PriorityQueue<Evento> eventos){
         
         if(!servidorOcupado){
             if(conexiones.isEmpty()){
                this.servidorOcupado = true;
                this.enServicio = c;
                this.entroAlServidor = true;
                Evento siguienteTimeout = new Evento(c.getTimeout() ,c,TipoEvento.TIMEOUT);
                eventos.add(siguienteTimeout);
             }
             else{     
                this.conexiones.add(c);
                Evento siguienteTimeout = new Evento(c.getTimeout() ,c,TipoEvento.TIMEOUT); 
                eventos.add(siguienteTimeout);
             }  
         }
         else{
                this.conexiones.add(c);
                Evento siguienteTimeout = new Evento(c.getTimeout() ,c,TipoEvento.TIMEOUT); 
                eventos.add(siguienteTimeout);
                
         } 
        this.colaProcesos.add(this.conexiones.size());
         menu.aplicarInterfazNuevoHilo(true,reloj);
         menu.aplicarInterfazColaHilo(conexiones.size(), reloj);
     }
     
    /**
     * Metodo que limpia el servidor del hilo de procesamiento
     */
    public void setServidor(){
         this.entroAlServidor = false;
     }
     
    /**
     * Metodo que devuelve si el servidor del hilo esta vacio 
     * @return devuelve un booleano, que de ser false representa que esta vacio el servidor
     */
    public boolean getServidor(){
         return this.entroAlServidor;
     }
     
    /**
     * Metodo encargado de sacar el servidor del hilo de procesamiento y generar el evento de entrada al modulo de consultas, de
     * De no estar vacia la cola se pone el siguiente en cola en en el servidor, si no se pone como desocupado
     * @param reloj tiempo de reloj en el momento del llamado al metodo
     * @param eventos lista de eventos donde se meterá el evento de llamado al modulo de consultas
     * @return devuelve un booleano que representa el estado del servidor, false vacio true lleno.
     */
    public boolean procesarSalida(double reloj,PriorityQueue<Evento> eventos){
                if(conexiones.isEmpty()){
                    this.enServicio = null;
                    this.servidorOcupado = false;                 
                }
                else{
                    this.enServicio = conexiones.get(0);
                    this.conexiones.remove(0);
                    this.servidorOcupado = true;
                    
                    Evento siguienteHilo= new Evento(generarTiempoSalida() + reloj, siguienteConexion(), TipoEvento.SALE_DE_HILO);             //se genera el evento de Procesado de consulta de la siguiente conexion 
                    eventos.add(siguienteHilo); 
                }
        menu.aplicarInterfazColaHilo(conexiones.size(),reloj);
        menu.aplicarInterfazNuevoHilo(true,reloj);
        return this.servidorOcupado;
     }
     
    /**
     * Metodo encargado de devolver el numero de conexiones en cola
     * @return devuelve un integer representando las conexiones en cola
     */
    public int getConexionesNum(){
         return this.conexiones.size();
     }
     
    /**
     * Metodo encargado de generar el tiempo de salida de una conexion del servidor.
     * @return devuelve un double que representa el tiempo que dura la conexion en salir del servidor.
     */
    public double generarTiempoSalida(){      //distribucion normal
         double z = 0;
         for(int i = 0; i < 12; i++){
             z += r.nextDouble();
         }
         z -= 6;
         return (1 + (0.01*z));
     }
     
    /**
     * Metodo que devuelve el estado del servidor
     * @return booleano representando si el servidor se encuentra lleno o vacio
     */
    public boolean estaOcupado(){
         return this.servidorOcupado;
     }
     
    /**
     * Metodo que devuelve la conexion que estaba en servidor y limpia el servidor
     * @return la conexión que se encontraba en el servidor
     */
    public Conexion siguienteConexion(){
        servidorOcupado = false;
        return enServicio;
     }
     
    /**
     * Metodo encargado de generar el evento de salida del hilo de procesamiento y entrada a el modulo de consulta
     * @param eventos lista de eventos donde se meterá el evento de llamado al modulo de consultas
     */
    public void siguienteHilo(Conexion c,double reloj,PriorityQueue<Evento> eventos){
        Evento siguienteHilo = new Evento(generarTiempoSalida() + reloj,c,TipoEvento.SALE_DE_HILO); //se genera el evento de Procesado de consulta de la siguiente conexion 
        eventos.add(siguienteHilo); 
        menu.aplicarInterfazNuevoHilo(true,reloj);
        menu.aplicarInterfazColaHilo(conexiones.size(),reloj);
        System.out.println("evento anadido de salida de hilo");
    }
    
    /**
     * Devuelve el tiempo que se dura en un select para sacar estadisticas
     * @return devuelve un vector de doubles representado los tiempos de duracion de los selects durante la simulacion
     */
    public Vector<Double> getTiempoSelect(){
        return this.tiempoSelect;
    }
    
    /**
     * Devuelve el tiempo que se dura en un join para sacar estadisticas
     * @return devuelve un vector de doubles representado los tiempos de duracion de los joins durante la simulacion
     */
    public Vector<Double> getTiempoJoin(){
        return this.tiempoJoin;
    }
        
    /**
     * Devuelve el tiempo que se dura en un update para sacar estadisticas
     * @return devuelve un vector de doubles representado los tiempos de duracion de los updates durante la simulacion
     */
    public Vector<Double> getTiempoUpdate(){
        return this.tiempoUpdate;
    }
            
    /**
     * Devuelve el tiempo que se dura en un DDL para sacar estadisticas
     * @return devuelve un vector de doubles representado los tiempos de duracion de los DDLs durante la simulacion
     */
    public Vector<Double> getTiempoDDL(){
        return this.tiempoDDL;
    }
    
}
