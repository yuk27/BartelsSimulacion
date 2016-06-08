package io;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class ModuloTransacciones {
    private Menu menu;
    private boolean hayDDL;
    private int[] servidores;
    private List<Conexion> conexionesConPrioridad;
    
    private double mayorTiempoEjecucion;
    
    public ModuloTransacciones(int p,Menu menu){
        servidores = new int[p];
        conexionesConPrioridad = new ArrayList<>();
        mayorTiempoEjecucion = 0;
        hayDDL = false;
    }
    
    void InicializarVector(){
    
        for(int i = 0; i < servidores.length; i++){
            servidores[i] = -1; 
        }
    }
      
    public double calcularTiempoTransaccion(){
        return servidores.length *0.03;
    }
    
    public double calcularTiempoBloquesDeDisco(Conexion c){
    
        double tiempoEnDisco = 0;
        
        switch (c.getTipo()){
                
                case 0: 
                    
                    tiempoEnDisco =  1/10;
                    break;
                
                case 2:
                   tiempoEnDisco =  randomConRango(0,64); 
                    break;
                   
                 default:
                     tiempoEnDisco = 0;
                     break;
        }
        
        return tiempoEnDisco;
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
                    hayDDL = true;
                }
                servidores[i] = c.getNumServidor();
                calcularTiempoTransaccion(c,eventos);
                return true;
            }
            i++;
        }
        conexionesConPrioridad.add(c);
        menu.aplicarInterfazColaTransacciones(conexionesConPrioridad.size(), reloj);
        return false;
    }
    
    Conexion getConexionDePrioridad(){        
        int priorMax = 0;
        int index = 0;
        
        for(int i = 0; i < conexionesConPrioridad.size(); i++){     
            if(conexionesConPrioridad.get(i).getTipo() > priorMax){
                priorMax = conexionesConPrioridad.get(i).getTipo();
                index = i;
            }
        }   
        return conexionesConPrioridad.get(index);      
    }
    
        private void calcularTiempoTransaccion(Conexion c,PriorityQueue<Evento> eventos){
        
        double tiempo;
         
         switch(c.getTipo()){
             case 0:        //SELECT
                 tiempo = calcularTiempoTransaccion() + 1/10;
                 c.setNumBloques(1);
                 break;
             case 2:        //JOIN
                 c.setNumBloques((int)randomConRango(1, 64));
                 tiempo = calcularTiempoTransaccion() + (c.getNumBloques() * 1/10); //se calcula el tiempo del join
                 break;
             case 3:        //DDL
                 tiempo = calcularTiempoTransaccion() + getMayorTiempoEjecucion();
                 setDDL();
                 break;
             default:       //UPDATE
                 tiempo = calcularTiempoTransaccion();  
                 break;
         }
         
         Evento siguienteTransaccion = new Evento(tiempo,c,TipoEvento.SALE_DE_TRANSACCIONES);
         
         if(c.getTipo() != 3 && siguienteTransaccion.getTiempo() > getMayorTiempoEjecucion()){
           setMayorTiempoEjecucion(siguienteTransaccion.getTiempo());
         }
         eventos.add(siguienteTransaccion);
    }
    
    public int eliminarConexion(Conexion c){
       int posLibre = -1;
        if(c.getTipo() == 3){
            hayDDL = false;
        }
        for(int i = 0; i < servidores.length; i++){
            if(servidores[i] == c.getNumServidor()){
                servidores[i] = -1;
                posLibre = i;
            }
        }
        return posLibre;
    }
    
    public void administrarServidorDeTransacciones(Conexion c,PriorityQueue<Evento> eventos){
        int posLibre = eliminarConexion(c);
        if(!conexionesConPrioridad.isEmpty()){
            Conexion nuevaConexion = getConexionDePrioridad();
            servidores[posLibre] = nuevaConexion.getNumServidor();
            calcularTiempoTransaccion(c,eventos);
        }
    }
    
    public void setDDL(){
        hayDDL = true;
    }
    
    public double getMayorTiempoEjecucion(){
        return mayorTiempoEjecucion;
    } 
    
    public void setMayorTiempoEjecucion(double mayorTiempoEjecucion){
        this.mayorTiempoEjecucion = mayorTiempoEjecucion;
    }
    
}
