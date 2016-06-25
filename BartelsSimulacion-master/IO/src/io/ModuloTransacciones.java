package io;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

/**
 * Clase que se encarga de toda la logica involucrada dentro de la transaccion de una conexion en el sistema
 * @author Ulises
 */
public class ModuloTransacciones {
    private Menu menu;
    private boolean hayDDL;
    private int[] servidores;
    private List<Conexion> conexionesConPrioridad;
    
    private Vector<Integer> colaTransacciones;
    private Vector<Double> tiempoSelect;
    private Vector<Double> tiempoUpdate;
    private Vector<Double> tiempoJoin;
    private Vector<Double> tiempoDDL;
    
    private double mayorTiempoEjecucion;
    
    /**
     * Contructor de la clase. Inicializa variables.
     * @param p tamaño de los servidores 
     * @param menu interfaz del sistema
     */
    public ModuloTransacciones(int p,Menu menu){
        this.servidores = new int[p];
        this.conexionesConPrioridad = new ArrayList<>();
        this.mayorTiempoEjecucion = 0;
        this.hayDDL = false;
        this.colaTransacciones = new Vector<>();
        this.tiempoSelect  = new Vector<>();    
        this.tiempoUpdate  = new Vector<>();    
        this.tiempoJoin   = new Vector<>();    
        this.tiempoDDL  = new Vector<>();    
        this.menu = menu;
    }
    
    /**
     * Inicializa los servidores en -1. Los pone en desocupados.
     */
    public void inicializarVector(){
        for(int i = 0; i < servidores.length; i++){
            this.servidores[i] = -1; 
        }
    }
    
    /**
     * Devuelve el tamaño de la cola de transacciones. Conexiones en espera
     * @return tamaño de la cola de transacciones.
     */
    public Vector<Integer> getCola(){
        return this.colaTransacciones;
    }

    /**
     * Calcula el tiempo que dura el sistema procesando una transaccion
     * @return tiempo que dura procesándose una transacción. 
     */
    public double calcularTiempoTransaccion(){
        return this.servidores.length *0.03;
    }
    
    /**
     * Genera un número aleatorio dentro del rango especificado en los parámetros
     * @param min límite inferior para generación de número aleatorio
     * @param max límita superar para generación de número aleatorio
     * @return número aleatorio que se encuentra entre los número indicados
     */
       public double randomConRango(int min, int max){ 
            int range = (max - min) + 1; 
            return (Math.random()*range) + min; 
    }
    
       /**
        * Asigna una conexión entrante al servidor del módulo o lo pone en la cola de espera
        * @param c conexión entrante
        * @param eventos cola de prioridad de eventos
        * @param reloj  tiempo actual del sistema
        * @param pc instancia del módulo de procesamiento de consultas
        * @return booleano que indica si la conexión logró entrar al servidor o fue añadida a la cola de espera
        */
    public boolean asignarConexion(Conexion c,PriorityQueue<Evento> eventos,double reloj, ModuloProcesamientoConsultas pc){
        int i = 0; 
        while(i < servidores.length && !hayDDL){
            if(servidores[i] == -1){
                if(c.getTipo() == 3){
                    this.hayDDL = true;
                    menu.aplicarInterfazProcesarTransacciones(getOcupados(),reloj);
                }
                this.servidores[i] = c.getNumServidor();
                this.calcularTiempoTransaccion(c,eventos, reloj);
                this.colaTransacciones.add(this.conexionesConPrioridad.size());
                this.setTiempoModulo(c, pc, reloj);
                menu.aplicarInterfazProcesarTransacciones(getOcupados(),reloj);
                return true;
            }
            i++;
        }
        
        this.conexionesConPrioridad.add(c);
        this.colaTransacciones.add(this.conexionesConPrioridad.size());
        this.setTiempoModulo(c, pc, reloj);
        menu.aplicarInterfazProcesarTransacciones(getOcupados(),reloj);
        menu.aplicarInterfazColaTransacciones(conexionesConPrioridad.size(), reloj);
        return false;
    }
    
    /**
     * Calcula y guarda el tiempo que una conexión estuvo en el módulo de procesamiento de consultas
     * @param c conexión entrarnte
     * @param pc instancia del módulo de procesamiento de consultas
     * @param reloj tiempo actual del sistema
     */
        public void setTiempoModulo(Conexion c, ModuloProcesamientoConsultas pc, double reloj){
            switch(c.getTipo()){
                case 0:
                    pc.getTiempoSelect().add(reloj - c.getTiempoEntradaModulo());
                    break;
                case 1:
                    pc.getTiempoUpdate().add(reloj - c.getTiempoEntradaModulo());
                    break;
                case 2:
                    pc.getTiempoJoin().add(reloj - c.getTiempoEntradaModulo());
                    break;
                case 3:
                   pc.getTiempoDDL().add(reloj - c.getTiempoEntradaModulo());
                    break;
            }
            c.setTiempoEntradaModulo(reloj);
        }
    
    /**
     * Devuelve el tiempo que una conexión de tipo select duró el en módulo.
     * @return tiempo de duración del tipo de conexión en el módulo
     */
    public Vector<Double> getTiempoSelect(){
        return this.tiempoSelect;
    }
    
    /**
     * Devuelve el tiempo que una conexión de tipo join duró el en módulo.
     * @return tiempo de duración del tipo de conexión en el módulo
     */
    public Vector<Double> getTiempoJoin(){
        return this.tiempoJoin;
    }
        
    /**
     * Devuelve el tiempo que una conexión de tipo update duró el en módulo.
     * @return tiempo de duración del tipo de conexión en el módulo
     */
    public Vector<Double> getTiempoUpdate(){
        return this.tiempoUpdate;
    }
    
    /**
     * Devuelve el tiempo que una conexión de tipo DDL duró el en módulo.
     * @return tiempo de duración del tipo de conexión en el módulo
     */        
    public Vector<Double> getTiempoDDL(){
        return this.tiempoDDL;
    }
        
   /**
    * Cuenta cuántos servidores están ocupados dentro del módulo
    * @return cantidad de servidores ocupados
    */ 
    public int getOcupados(){
        int aux = 0;     
        for(int i = 0; i < servidores.length; i++){
             if(servidores[i] != -1){
                 aux++;
             }      
        }
        return aux;  
    }
       
    /**
     * Calcula el tamaño de la cola del módulo
     * @return tamaño de la cola de prioridades
     */
    public int getConexionNum(){
       return this.conexionesConPrioridad.size();
    }
    
    /**
     * Determina la próxima conexión a procesar de acuerdo a su prioridad en la cola
     * @return siguiente conexión de prioridad para procesar
     */
    private Conexion getConexionDePrioridad(){        
        int priorMax = 0;
        int index = 0;
        
        for(int i = 0; i < conexionesConPrioridad.size(); i++){     
            if(conexionesConPrioridad.get(i).getTipo() > priorMax){
                priorMax = this.conexionesConPrioridad.get(i).getTipo();
                index = i;
            }
        }   
        return this.conexionesConPrioridad.get(index);      
    }
    
    /**
     * Calcula el tiempo de salida de una conexión en este módulo de acuerdo a su tipo
     * @param c conexión entrante 
     * @param eventos cola de prioridad de eventos
     * @param reloj tiempo actual del sistema
     */
        private void calcularTiempoTransaccion(Conexion c,PriorityQueue<Evento> eventos, double reloj){        
                double tiempo;
                System.out.println("jiji " + c.getTipo() + " " + reloj);
                 switch(c.getTipo()){
                     case 0:        //SELECT
                         tiempo = this.calcularTiempoTransaccion() + 1/10 + reloj;
                         c.setNumBloques(1);
                         break;
                     case 2:        //JOIN
                         c.setNumBloques((int)randomConRango(1, 64));
                         tiempo = this.calcularTiempoTransaccion() + (c.getNumBloques() * 1/10) + reloj; //se calcula el tiempo del join
                         break;
                     case 3:        //DDL
                         tiempo = this.calcularTiempoTransaccion() + this.getMayorTiempoEjecucion() + reloj;
                         setDDL();
                         break;
                     default:       //UPDATE
                         tiempo = this.calcularTiempoTransaccion() + reloj;  
                         break;
                 }

                Evento siguienteTransaccion = new Evento(tiempo,c,TipoEvento.SALE_DE_TRANSACCIONES);

                if(c.getTipo() != 3 && siguienteTransaccion.getTiempo() > getMayorTiempoEjecucion()){
                  this.setMayorTiempoEjecucion(siguienteTransaccion.getTiempo());
                }
                eventos.add(siguienteTransaccion);
    }
    
        /**
         * Elimina la conexión del servidor
         * @param c conexión a eliminar
         * @return posición del servidor que acaba de ser liberada
         */
    public int eliminarConexion(Conexion c){
        System.out.println("julio es un idiota");
       int posLibre = -1;
        if(c.getTipo() == 3){
            this.hayDDL = false;
        }
        for(int i = 0; i < servidores.length; i++){
            if(servidores[i] == c.getNumServidor()){
                this.servidores[i] = -1;
                posLibre = i;
            }
        }
        return posLibre;
    }
    
    /**
     * Maneja la salida de la conexión del módulo y acomoda las conexiones que se encuentran en cola para ponerlas en servicio
     * @param c conexión que sale
     * @param eventos cola de prioridad de eventos
     * @param reloj tiempo actual del sistema
     */
    public void procesarSalida(Conexion c,PriorityQueue<Evento> eventos, double reloj){
        System.out.println("julio es un imbecil");
        int posLibre = eliminarConexion(c);
        if(posLibre != -1){
            if(!conexionesConPrioridad.isEmpty()){
                Conexion nuevaConexion = getConexionDePrioridad();
                this.servidores[posLibre] = nuevaConexion.getNumServidor();
                calcularTiempoTransaccion(nuevaConexion, eventos, reloj);
            }
        }
        menu.aplicarInterfazProcesarTransacciones(getOcupados(),reloj);
        menu.aplicarInterfazColaTransacciones(conexionesConPrioridad.size(), reloj);
    }
    
    /**
     * Elimina la conexión de la cola de espera ya que se le acabó el tiempo
     * @param c conexión a eliminar
     */
    public void eliminarConexionTimeout(Conexion c){
        this.eliminarConexion(c);
        for(int i = 0; i < conexionesConPrioridad.size(); i++){
             if(conexionesConPrioridad.get(i) == c){
                 this.conexionesConPrioridad.remove(i);
                 return;
             }
        }
    }
    
    /**
     * Setea la variable hayDDL a true
     */
    public void setDDL(){
        this.hayDDL = true;
    }
    
    /**
     * Devuelve el tiempo de ejecución mayor de las conexiones en servicio
     * @return tiempo mayor de ejecución de las conexiones en servicio
     */
    public double getMayorTiempoEjecucion(){
        return this.mayorTiempoEjecucion;
    } 
    
    /**
     * Setea mayorTiempoEjecucución al valor especificado
     * @param mayorTiempoEjecucion valor por cambiar la variable
     */
    public void setMayorTiempoEjecucion(double mayorTiempoEjecucion){
        this.mayorTiempoEjecucion = mayorTiempoEjecucion;
    }
    
}
