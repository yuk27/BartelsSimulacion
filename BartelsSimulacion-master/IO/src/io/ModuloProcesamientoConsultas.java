package io;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Vector;

/**
 * Clase que represenenta el modulo de procesamiento de consultas,
 * el cual se encarga tanto de procesar las consultas, 
 * como de enviarlas a transacciones y ejecutarlas
 */
public class ModuloProcesamientoConsultas {
    private Menu menu;
    private int[] servidoresConsultas;
    private int[] ejecutorConsultas;
    private double[] tiempoProcesamiento; 
    private List<Conexion> consultas;
    private List<Conexion> ejecutor;
    private Random r = new Random();
    
    private Vector<Integer> colaProcesamientoConsultas;
    private Vector<Integer> colaEjecutor;
    
    private Vector<Double> tiempoSelect;
    private Vector<Double> tiempoUpdate;
    private Vector<Double> tiempoJoin;
    private Vector<Double> tiempoDDL;
    
    private Vector<Double> tiempoSelectEjecutor;
    private Vector<Double> tiempoUpdateEjecutor;
    private Vector<Double> tiempoJoinEjecutor;
    private Vector<Double> tiempoDDLEjecutor;
    
  /**
     *Contructor de la clase que tiene por proposito inicializar las variables del objeto 
     * @param p  cantidad maxima de espacios con los que contará el servidor de consultas
     * @param m  cantidad maxima de espacios con los que contará el servidor de ejecución
     * @param menu //variable que conecta al modulo con el menu para poder hacer la llamadas necesarias para actualizar la interfaz.
     */
    public ModuloProcesamientoConsultas(int p, int m, Menu menu){
        this.servidoresConsultas = new int [p];
        this.tiempoProcesamiento = new double [p];
        this.ejecutorConsultas = new int[m];
        this.consultas = new ArrayList<>();
        this.ejecutor = new ArrayList<>();
        this.colaProcesamientoConsultas  = new Vector<>();
        this.colaEjecutor = new Vector<>();
        this.tiempoSelect  = new Vector<>();    
        this.tiempoUpdate   = new Vector<>();    
        this.tiempoJoin   = new Vector<>();    
        this.tiempoDDL  = new Vector<>();    
        this.tiempoDDLEjecutor = new Vector<>();  
        this.tiempoJoinEjecutor = new Vector<>();  
        this.tiempoSelectEjecutor = new Vector<>();  
        this.tiempoUpdateEjecutor = new Vector<>();  
        this.menu = menu;
    }
    
    /**
     * Metodo encargado de insertar una conexión al servidor de consultas, genera el evento de llamada a transaccion, y en el caso de estar todos los espacios ocupados lo inserta en la cola de consultas
     * @param c conexión entrante
     * @param reloj tiempo de reloj en el momento de la llamada
     * @param eventos //lista de eventos en donde se meterá el nuevo evento de proceso_consulta
     * @param admP  //variable que conecta al modulo con modulo de entrada de conexiones, para sacar estadisticas
     */
    public void asignarConsultaAServidor(Conexion c, double reloj,PriorityQueue<Evento> eventos, ModuloAdmProcesos admP){
                int i = 0;
                while(i < servidoresConsultas.length){
                    if(servidoresConsultas[i] == -1){
                        this.servidoresConsultas[i] = c.getNumServidor();
                        Evento siguienteConsultaProcesada = new Evento(calcularTiempoTotal(c) + reloj, c, TipoEvento.PROCESO_CONSULTA);
                        eventos.add(siguienteConsultaProcesada);
                        this.colaProcesamientoConsultas.add(this.consultas.size());
                        this.setTiempoModuloProcesos(c, admP, reloj);
                        menu.aplicarInterfazProcesarConsulta(getOcupados(),reloj);
                        menu.aplicarInterfazColaProcesador(consultas.size(), reloj);
                        return;
                    }
                    i++;
                }
                this.consultas.add(c);  
                this.colaProcesamientoConsultas.add(this.consultas.size());
                this.setTiempoModuloProcesos(c, admP, reloj);
                menu.aplicarInterfazProcesarConsulta(getOcupados(),reloj);
                menu.aplicarInterfazColaProcesador(consultas.size(), reloj);
    }
        
    /**
     * Metodo encargado de enviar estadisticas al modulo de clientes 
     * @param c conexión entrante
     * @param admP variable que conecta al modulo con modulo de entrada de conexiones, para sacar estadisticas
     * @param reloj tiempo de reloj en el momento de la llamada
     */
    public void setTiempoModuloProcesos(Conexion c, ModuloAdmProcesos admP, double reloj){
            switch(c.getTipo()){
                case 0:
                    admP.getTiempoSelect().add(reloj - c.getTiempoEntrada());
                    break;
                case 1:
                    admP.getTiempoUpdate().add(reloj - c.getTiempoEntrada());
                    break;
                case 2:
                    admP.getTiempoJoin().add(reloj - c.getTiempoEntrada());
                    break;
                case 3:
                   admP.getTiempoDDL().add(reloj - c.getTiempoEntrada());
                    break;
            }
            c.setTiempoEntradaModulo(reloj);
        }
        
    /**
     * Metodo encargado de enviar estadisticas al modulo de transacciones
     * @param c conexión entrante
     * @param transacciones variable que conecta al modulo con modulo de entrada de conexiones, para sacar estadisticas
     * @param reloj tiempo de reloj en el momento de la llamada
     */
    public void setTiempoModuloTransacciones(Conexion c, ModuloTransacciones transacciones, double reloj){
            switch(c.getTipo()){
                case 0:
                    transacciones.getTiempoSelect().add(reloj - c.getTiempoEntradaModulo());
                    break;
                case 1:
                    transacciones.getTiempoUpdate().add(reloj - c.getTiempoEntradaModulo());
                    break;
                case 2:
                    transacciones.getTiempoJoin().add(reloj - c.getTiempoEntradaModulo());
                    break;
                case 3:
                   transacciones.getTiempoDDL().add(reloj - c.getTiempoEntradaModulo());
                    break;
            }
            c.setTiempoEntradaModulo(reloj);
        }
    /**
     * Metodo que devuelve la lista de tiempos de select en la consulta, utilizados para estadisticas
     * @return devuelve una lista conteniendo los tiempos durados por el select
     */
    public Vector<Double> getTiempoSelect(){
        return this.tiempoSelect;
    }
    
   /**
     * Metodo que devuelve la lista de tiempos de join en la consulta utilizados para estadisticas
     * @return devuelve una lista conteniendo los tiempos durados por el Join
     */
    public Vector<Double> getTiempoJoin(){
        return this.tiempoJoin;
    }
        
    /**
     * Metodo que devuelve la lista de tiempos de Update en la consulta utilizados para estadisticas
     * @return devuelve una lista conteniendo los tiempos durados por el Update
     */
    public Vector<Double> getTiempoUpdate(){
        return this.tiempoUpdate;
    }
            
     /**
     * Metodo que devuelve la lista de tiempos de DDL en la consulta utilizados para estadisticas
     * @return devuelve una lista conteniendo los tiempos durados por el DDL
     */
    public Vector<Double> getTiempoDDL(){
        return this.tiempoDDL;
    }
    
    /**
     * Metodo que devuelve la lista de tiempos de select en el proceso de ejecución utilizados para estadisticas
     * @return devuelve una lista conteniendo los tiempos durados por el select en proceso de ejecucion
     */
    public Vector<Double> getTiempoSelectEjecutor(){
        return this.tiempoSelectEjecutor;
    }
    
    /**
     * Metodo que devuelve la lista de tiempos de join en el proceso de ejecución utilizados para estadisticas
     * @return devuelve una lista conteniendo los tiempos durados por el Join en el proceso de ejecución
     */
    public Vector<Double> getTiempoJoinEjecutor(){
        return this.tiempoJoinEjecutor;
    }
    
    /**
     * Metodo que devuelve la lista de tiempos de Update en el proceso de ejecución utilizados para estadisticas
     * @return devuelve una lista conteniendo los tiempos durados por el Update en el proceso de ejecución
     */
    public Vector<Double> getTiempoUpdateEjecutor(){
        return this.tiempoUpdateEjecutor;
    }
            
    /**
     * Metodo que devuelve la lista de tiempos de DDL en el proceso de ejecución utilizados para estadisticas
     * @return devuelve una lista conteniendo los tiempos durados por el DDL en el proceso de ejecución
     */
    public Vector<Double> getTiempoDDLEjecutor(){
        return this.tiempoDDLEjecutor;
    }
        
    /**
     * Metodo encargado de buscar en el servidor la siguiente conexión con menor tiempo y la devuelve
     * @param reloj tiempo de reloj en el tiempo de la llamada
     * @return devuelve la siguiente conexión con menor tiempo
     */
    public int getSiguienteProcesado(double reloj){ 
            double menorTiempo = -1; //se inicializa el valor
            int siguiente = 0; 
            for(int i = 0; i < tiempoProcesamiento.length; i++){
                if(tiempoProcesamiento[i] > menorTiempo || menorTiempo == -1){ //si es el menor tiempo o el primero se toma como el menor
                    menorTiempo = tiempoProcesamiento[i];
                    siguiente = i;
                }
            }
            menu.aplicarInterfazProcesarConsulta(getOcupados(),reloj);
            menu.aplicarInterfazColaProcesador(consultas.size(), reloj);
            return siguiente;
    } 
   
    /**
     * Metodo encargado de devolver la cantidad de servidores del procesamiento de consulta ocupados en el instante que fue llamado
     * @return devuelve un int con la cantidad de servidores libres.
     */
    public int getOcupados(){
    
        int aux = 0;
        for(int i = 0; i < servidoresConsultas.length; i++){  
             if(servidoresConsultas[i] != -1){
                 aux++;
             }       
        }
        return aux;  
    }
    
    /**
     * Metodo encargado de devolver la cantidad de conexiones en cola de procesamiento de consulta en el instante que fue llamado
     * @return devuelve un int con la cantidad de conexiones en cola.
     */
    public int getConsultasNum(){
        return this.consultas.size();
    }
        
    /**
     * Metodo encargado de devolver la cantidad de servidores del ejecutor de consulta ocupados en el instante que fue llamado
     * @return devuelve un int con la cantidad de servidores libres.
     */
    public int getOcupadosEjecutor(){
        int aux = 0;
        for(int i = 0; i < ejecutorConsultas.length; i++){
             if(ejecutorConsultas[i] != -1){
                 aux++;
             }
        }
        return aux;  
    }
        
    /**
     * Metodo encargado de devolver la cantidad de conexiones en cola de ejecutor de consulta en el instante que fue llamado
     * @return devuelve un int con la cantidad de conexiones en cola.
     */
    public int getEjecutorNum(){
        return this.ejecutor.size();
    }
    
    /**
     * Metodo encargado de poner los los vectores en -1 que representa que estan limpios,
     * esto lo hace al inicio de la simulación
     */
    public void inicializarVectores(){    
        for(int i = 0; i < servidoresConsultas.length; i++){
            this.servidoresConsultas[i] = -1; 
        }
        for(int i = 0; i < ejecutorConsultas.length; i++){
            this.ejecutorConsultas[i] = -1; 
        }   
    }
        
    /**
     * ;Metodo encargado de calcular el tiempo total que durará la ejecución de una consulta 
     * @param c conexión entrante
     * @return devuelve un double que representa el tiempo
     */
    public double calcularTiempoTotal(Conexion c){
        return (calcularTiempoLexico() + calcularTiempoSintactico() + calcularTiempoSemantico() + calcularTiempoPermisos() + calcularTiempoOptimizacion(c));
    }
    
    /**
     * Metodo encargado de calcular el tiempo lexico
     * @return devuelve un double representando la duración de reloj del tiempo lexico.
     */
    public double calcularTiempoLexico(){
        return (1/10);
    }
    
    /**
     *
     * @return
     */
    public double calcularTiempoSintactico(){
        //resultado de la integral despejada
        double temp = r.nextDouble();
        return temp;
    }
    
    /**
     *
     * @return
     */
    public double calcularTiempoSemantico(){
        //resultado de la integral despejada
        double temp = r.nextDouble();
        return temp;
    }
    
    /**
     *
     * @return
     */
    public double calcularTiempoPermisos(){
        return (-(Math.log(1 - r.nextDouble()))/0.07);
    }
            
    /**
     *
     * @param c
     * @return
     */
    public double calcularTiempoOptimizacion(Conexion c){
        if(c.isReadOnly()){
            return 0.1;
        }
        else{
            return 0.25;
        }
    }
    
    /**
     *
     * @param c
     * @param eventos
     * @param reloj
     * @param transacciones
     */
    public void asignarConsultaAEjecutor(Conexion c,PriorityQueue<Evento> eventos, double reloj, ModuloTransacciones transacciones){
        int i = 0;
        
        while(i < ejecutorConsultas.length){
            if(ejecutorConsultas[i] == -1){
                this.ejecutorConsultas[i] = c.getNumServidor();
                Evento siguienteEjecucion = new Evento(calcularTiempoAlgoritmoEjecucion(c.getNumBloques(), c) + reloj, c, TipoEvento.EJECUTO_CONSULTA);
                eventos.add(siguienteEjecucion);
                this.colaEjecutor.add(this.ejecutor.size());
                this.setTiempoModuloTransacciones(c, transacciones, reloj);
                menu.aplicarInterfazProcesarEjecutor(getOcupadosEjecutor(),reloj);
                return;
            }
            i++;
        }
        this.ejecutor.add(c);
        this.colaEjecutor.add(this.ejecutor.size());
        this.setTiempoModuloTransacciones(c, transacciones, reloj);
        menu.aplicarInterfazProcesarEjecutor(getOcupadosEjecutor(),reloj);
        menu.aplicarInterfazColaEjecutor(ejecutor.size(), reloj);
    }
    
    /**
     *
     * @param cantidadDeBloques
     * @param c
     * @return
     */
    public double calcularTiempoAlgoritmoEjecucion(int cantidadDeBloques, Conexion c){
        double tiempoDeEjecucionSegundos = Math.pow(cantidadDeBloques, 2.0) / 1000;
        int tipoConexion = c.getTipo();
        if(tipoConexion == 1){
            tiempoDeEjecucionSegundos += 1;
        }
        else if(tipoConexion == 3){
            tiempoDeEjecucionSegundos += 0.5;
        }
        return tiempoDeEjecucionSegundos;
    }
    
    /**
     *
     * @param cantidadDeBloques
     * @return
     */
    public double calcularTamanoRespuesta(int cantidadDeBloques){
        return cantidadDeBloques/64;
    }
    
    /**
     *
     * @param c
     * @return
     */
    public int eliminarConexionServidor(Conexion c){
        int posLibre = -1;
        for(int i = 0; i < servidoresConsultas.length; i++){
            if(servidoresConsultas[i] == c.getNumServidor()){
                servidoresConsultas[i] = -1;
                posLibre = i;
            }
        }
        return posLibre;
    }
    
    /**
     *
     * @param c
     * @param eventos
     * @param reloj
     */
    public void procesarSalidaEjecutor(Conexion c,PriorityQueue<Evento> eventos, double reloj){
        int posLibre = eliminarConexionEjecutor(c);
        if(posLibre != -1){
            if(!ejecutor.isEmpty()){
                this.ejecutorConsultas[posLibre] = ejecutor.get(0).getNumServidor();
                Evento siguienteConsultaProcesada = new Evento(calcularTiempoAlgoritmoEjecucion(ejecutor.get(0).getNumBloques(), ejecutor.get(0)) + reloj, ejecutor.remove(0), TipoEvento.EJECUTO_CONSULTA);
                eventos.add(siguienteConsultaProcesada);
            }
        }
        
        menu.aplicarInterfazProcesarEjecutor(getOcupadosEjecutor(),reloj);
        menu.aplicarInterfazColaEjecutor(ejecutor.size(), reloj);
        
    }
    
    /**
     *
     * @param c
     * @param eventos
     * @param reloj
     */
    public void procesarSalidaConsulta(Conexion c,PriorityQueue<Evento> eventos, double reloj){             
        
        int posLibre = eliminarConexionServidor(c);
        if(posLibre != -1){
            if(!consultas.isEmpty()){
                this.servidoresConsultas[posLibre] = consultas.get(0).getNumServidor(); //asigno la primera conexion de la lista al servidor que acabo de liberar  
                Evento siguienteConsultaProcesada = new Evento(calcularTiempoTotal(consultas.get(0)) + reloj, consultas.remove(0), TipoEvento.PROCESO_CONSULTA);
                eventos.add(siguienteConsultaProcesada);
            }
        }
        menu.aplicarInterfazProcesarConsulta(getOcupados(), reloj);
        menu.aplicarInterfazColaProcesador(consultas.size(), reloj);
        
    }
    
    /**
     *
     * @param c
     * @return
     */
    public int eliminarConexionEjecutor(Conexion c){
        int posLibre = -1;
        for(int i = 0; i < ejecutorConsultas.length; i++){
            if(ejecutorConsultas[i] == c.getNumServidor()){
                this.ejecutorConsultas[i] = -1;
                posLibre = 0;
            }
        }
        return posLibre;
    }
    
    /**
     *
     * @param c
     */
    public void eliminarConexion(Conexion c){
        this.eliminarConexionServidor(c);
        for(int i = 0; i < consultas.size(); i++){
             if(consultas.get(i) == c){
                 this.consultas.remove(i);
                 break;
             }
         }
        this.eliminarConexionEjecutor(c);
        for(int i = 0; i < ejecutor.size(); i++){
             if(ejecutor.get(i) == c){
                 this.ejecutor.remove(i);
                 return;
             }
        }
    }
    
    /**
     *
     * @return
     */
    public Vector<Integer> getColaConsultas(){
        return this.colaProcesamientoConsultas;
    }
    
    /**
     *
     * @return
     */
    public Vector<Integer> getColaEjecutor(){
        return this.colaEjecutor;
    }
    
}
