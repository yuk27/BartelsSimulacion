package io;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

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
    
    public void inicializarVector(){
        for(int i = 0; i < servidores.length; i++){
            this.servidores[i] = -1; 
        }
    }
    
    public Vector<Integer> getCola(){
        return this.colaTransacciones;
    }
      
    public double calcularTiempoTransaccion(){
        return this.servidores.length *0.03;
    }
    
       public double randomConRango(int min, int max){ 
            int range = (max - min) + 1; 
            return (Math.random()*range) + min; 
    }
    
    public boolean asignarConexion(Conexion c,PriorityQueue<Evento> eventos,double reloj){
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
                menu.aplicarInterfazProcesarTransacciones(getOcupados(),reloj);
                return true;
            }
            i++;
        }
        
        this.conexionesConPrioridad.add(c);
        this.colaTransacciones.add(this.conexionesConPrioridad.size());
        menu.aplicarInterfazProcesarTransacciones(getOcupados(),reloj);
        menu.aplicarInterfazColaTransacciones(conexionesConPrioridad.size(), reloj);
        return false;
    }
    
        public int getOcupados(){
        int aux = 0;     
        for(int i = 0; i < servidores.length; i++){
             if(servidores[i] != -1){
                 aux++;
             }      
        }
        return aux;  
    }
        
    public int getConexionNum(){
       return this.conexionesConPrioridad.size();
    }
    
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
    
        private void calcularTiempoTransaccion(Conexion c,PriorityQueue<Evento> eventos, double reloj){        
                double tiempo;

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
                         tiempo = this.calcularTiempoTransaccion() + this.getMayorTiempoEjecucion();
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
    
    public int eliminarConexion(Conexion c){
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
    
    public void procesarSalida(Conexion c,PriorityQueue<Evento> eventos, double reloj){
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
    
    public void eliminarConexionTimeout(Conexion c){
        this.eliminarConexion(c);
        for(int i = 0; i < conexionesConPrioridad.size(); i++){
             if(conexionesConPrioridad.get(i) == c){
                 this.conexionesConPrioridad.remove(i);
                 return;
             }
        }
    }
    
    public void setDDL(){
        this.hayDDL = true;
    }
    
    public double getMayorTiempoEjecucion(){
        return this.mayorTiempoEjecucion;
    } 
    
    public void setMayorTiempoEjecucion(double mayorTiempoEjecucion){
        this.mayorTiempoEjecucion = mayorTiempoEjecucion;
    }
    
}
