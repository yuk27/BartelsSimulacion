package io;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

/**
 * Calse que representa el modulo de administración de clientes, la parte de creación de las conexiones a la base de datos simulada
 */
public class ModuloAdmClientes {
    Menu menu;
    private List<Conexion> conexiones;
    private boolean[] servidores; //array que representa los servidores utilizables por nuevos clientes
    private double timeOutGlobal;
    private Vector<Integer> colaClientes; 
    
    private Vector<Double> tiempoSelect;
    private Vector<Double> tiempoUpdate;
    private Vector<Double> tiempoJoin;
    private Vector<Double> tiempoDDL;
    
    /**
     *Contructor de la clase que tiene por proposito inicializar las variables del objeto 
     * @param k cantidad maxima de espacios con los que contará el servidor de clientes
     * @param timeOutGlobal  //variable que guarda el timeout que tendrá cada conexion nueva que entre
     * @param menu //variable que conecta al modulo con el menu para poder hacer la llamadas necesarias para actualizar la interfaz.
     */
    public ModuloAdmClientes(int k, double timeOutGlobal,Menu menu){
        this.servidores = new boolean[k];
        this.conexiones = new ArrayList<>();
        this.timeOutGlobal = timeOutGlobal;
        this.colaClientes =  new Vector<>();
        this.tiempoSelect  = new Vector<>();    
        this.tiempoUpdate   = new Vector<>();    
        this.tiempoJoin   = new Vector<>();    
        this.tiempoDDL  = new Vector<>();    
        this.menu = menu;
    }
    
    public Vector<Integer> getCola(){
        return this.colaClientes;
    }
    
    /**
     * Metodo encargado de crear cadda nueva conexión que tenga espacio de entrada 
     * @param tiempoActual tiempo en el que se crea la nueva conexión, para usar de referencia a la hora de generar el timeout de la conexión
     * @return devuelve un true en el caso que pudiera meter una nueva conexión.
     */
    public boolean crearConexion(double tiempoActual){
        if(hayServidor()){//si hay servidores desocupados
                for(int i = 0; i < servidores.length; i++){  //se crea la conexion y se pone la posicion del servidor en ocupado
                    if(!servidores[i]){
                         servidores[i] = true;
                         Conexion nuevaConexion = new Conexion(tiempoActual,timeOutGlobal,i);
                         nuevaConexion.generarTimeout(timeOutGlobal);
                         nuevaConexion.generarTipo();
                         conexiones.add(nuevaConexion);
                         this.colaClientes.add(this.getOcupados());
                         return true;
                    }
                }
        }
       this.colaClientes.add(this.getOcupados());
       return false;
    }
    
    /**
     * Metodo encargado de buscar todos los espacios ocupados que se encuentre en el servidor de entrada.
     * @return devuelve en numero de servidores ocupados
     */
    public int getOcupados(){
        int aux = 0;
        for(int i = 0; i < servidores.length; i++){ 
            if(servidores[i]){
                aux++; 
            }
        }
        
        return aux;
    }
    
    /**
     * Metodo encargado d eliminar la conexión que asi lo requiera
     * @param pos le entra la posicion en el servidor en la cual se encuentra
     * @param reloj y el tiempo de reloj en el que ocurre la accion
     */
    public void eliminarConexion(int pos,double reloj){
        servidores[pos] = false;  //se pone el servidor en limpio 
        for(int i = 0; i < conexiones.size(); i++){ //se busca la conexión en la cola de servicioes y se elimina.
            if(conexiones.get(i).getServidor() == pos){
                conexiones.remove(i);
                menu.aplicarInterfazClientes(conexiones.size(),reloj); //se actualiza la interfaz
            }    
        }   
    }
    
    /**
     * Metodo encargado de devolver la ultima conexión que este en la lista de conexiones.
     * @return devuelve la conexión para poder ser referenciada
     */
    public Conexion getSiguienteConexion(){
        return conexiones.get(conexiones.size() - 1);
    }
    
    /**
     * Metodo encargado de buscar por todos los espacios del servidor y encontrar si existe un espacio desocupado.
     * @return devuelve true si encuentra un espacio vacio, false de otra forma.
     */
    public boolean hayServidor(){
        int pos = 0;
        while(pos < servidores.length){
            if(!servidores[pos])
                return true;
            pos++;
        }  
        return false;
    }
 
    /**
     * Metodo encargado de devolver el número de conexiones generadas.
     * @return devuelve un int con el número de conexiones.
     */
    public int getUsedConexiones(){
        return conexiones.size();
    }
    
    /**
     * Metodo encargado de terminar la ejecución de una conexión si la misma cumplió con ciclo de procesamiento.
     * @param c la conexión que terminó todo su proceso
     * @param tamanoRespuesta doubl que representa el tamaños en disco de la respueta para poder calcular su tiempo de salida
     * @param eventos lista de eventos en donde se agrará el evento creado.
     */
    public void sacarDelSistema(Conexion c,double tamanoRespuesta,PriorityQueue<Evento> eventos){
        Evento siguienteSalidaDelSistema = new Evento(tamanoRespuesta, c, TipoEvento.TERMINO_CONEXION);
        eventos.add(siguienteSalidaDelSistema);
    }
    
    /**
     * Metodo encargado de generar la proxima llegada al sistema 
     * @param reloj tiempo de reloj actual que se usa de base para generar el tiempo de creación de la conexión
     * @param r número random que define el momento de reloj exacto donde se crea el evento 
     * @param eventos lista de eventos donde se agregará el nuevo evento
     */
    public void crearArribo(double reloj,double r,PriorityQueue<Evento> eventos){
        double tiempoArribo = Conexion.generarTiempoArribo(r);
        Evento siguienteLlegada = new Evento(tiempoArribo + reloj,null,TipoEvento.LLEGA_CONEXION); //se genera el evento para la siguiente entrada de una conexion
        eventos.add(siguienteLlegada);// se agrega a la lista de eventos.
    }
    

    
}
