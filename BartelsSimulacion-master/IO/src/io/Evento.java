package io;

public class Evento {

    private double tiempo;
    private TipoEvento tipo;
    private Conexion c;
    
    public Evento(double tiempo, Conexion c, TipoEvento tipo){
    this.tiempo = tiempo;
    this.c = c;
    this.tipo = tipo;
    }
    
    public double getTiempo(){
        return tiempo;
    }
    
    public TipoEvento getTipo(){
        return tipo;
    }
    
    public Conexion getConexion(){
        return c;
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
