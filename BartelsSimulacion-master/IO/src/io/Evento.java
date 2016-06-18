package io;

/**
 * Clase que representa el objeto Evento utilizado para los distintos eventos generados en la simulación y almacenados en una lista de eventos.
 */
public class Evento {

    private double tiempo;
    private TipoEvento tipo;
    private Conexion conexion;
    
    /**
     *
     * @param tiempo variable que almacena el tiempo de reloj en el que se generó el objeto
     * @param c variable que almacena la conexión con la que cuenta el evento.
     * @param tipo variable que almacena el enum TipoEvento que representará el tipo de evento generado
     */
    public Evento(double tiempo, Conexion c, TipoEvento tipo){
        this.tiempo = tiempo;
        this.conexion = c;
        this.tipo = tipo;
    }
    
    /**
     * Metodo encargado de devolver el tiempo de reloj en el que genero este evento
     * @return devuelve un double tiempo.
     */
    public double getTiempo(){
        return tiempo;
    }
    
    /**
     * Metodo encargado de devolver el tipo de evento generado
     * @return devuelve el TipoEvento especifico de este objeto
     */
    public TipoEvento getTipo(){
        return tipo;
    }
    
    /**
     * Metodo encargado de devolver la conexion almacenada en este evento
     * @return devuelve la conexión especifica de este objeto
     */
    public Conexion getConexion(){
        return conexion;
    }
}
