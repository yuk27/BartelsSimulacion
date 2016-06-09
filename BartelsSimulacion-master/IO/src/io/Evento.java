package io;

public class Evento {

    private double tiempo;
    private TipoEvento tipo;
    private Conexion conexion;
    
    public Evento(double tiempo, Conexion c, TipoEvento tipo){
    this.tiempo = tiempo;
    this.conexion = c;
    this.tipo = tipo;
    }
    
    public double getTiempo(){
        return tiempo;
    }
    
    public TipoEvento getTipo(){
        return tipo;
    }
    
    public Conexion getConexion(){
        return conexion;
    }
}
