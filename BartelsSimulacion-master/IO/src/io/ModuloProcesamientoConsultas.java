package io;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class ModuloProcesamientoConsultas {
    private Menu menu;
    private int[] servidoresConsultas;
    private int[] ejecutorConsultas;
    private double[] tiempoProcesamiento; 
    private List<Conexion> consultas;
    private List<Conexion> ejecutor;
    private Random r = new Random();
    
    public ModuloProcesamientoConsultas(int p, int m, Menu menu){
        servidoresConsultas = new int [p];
        tiempoProcesamiento = new double [p];
        ejecutorConsultas = new int[m];
        consultas = new ArrayList<>();
        ejecutor = new ArrayList<>();
        this.menu = menu;
    }
    
        public void asignarConsultaAServidor(Conexion c, double reloj,PriorityQueue<Evento> eventos){
                int i = 0;
                while(i < servidoresConsultas.length){
                    if(servidoresConsultas[i] == -1){
                        servidoresConsultas[i] = c.getNumServidor();
                        menu.aplicarInterfazProcesarConsulta(reloj);
                        Evento siguienteConsultaProcesada = new Evento(calcularTiempoTotal(c) + reloj, c, TipoEvento.PROCESO_CONSULTA);
                        eventos.add(siguienteConsultaProcesada);
                        return;
                    }
                    i++;
                }
                consultas.add(c);
                menu.aplicarInterfazColaProcesador(consultas.size(), reloj);
    }
        
    public int getSiguienteProcesado(){ 
            double menorTiempo = -1; //se inicializa el valor
            int siguiente = 0; 
            for(int i = 0; i < tiempoProcesamiento.length; i++){
                if(tiempoProcesamiento[i] > menorTiempo || menorTiempo == -1){ //si es el menor tiempo o el primero se toma como el menor
                    menorTiempo = tiempoProcesamiento[i];
                    siguiente = i;
                }
            }
            return siguiente;
    } 
        
        
    public void inicializarVectores(){    
        for(int i = 0; i < servidoresConsultas.length; i++){
            servidoresConsultas[i] = -1; 
        }
        for(int i = 0; i < ejecutorConsultas.length; i++){
            ejecutorConsultas[i] = -1; 
        }   
    }
        
    public double calcularTiempoTotal(Conexion c){
        return (calcularTiempoLexico() + calcularTiempoSintactico() + calcularTiempoSemantico() + calcularTiempoPermisos() + calcularTiempoOptimizacion(c));
    }
    
    public double calcularTiempoLexico(){
        return (1/10);
    }
    
    public double calcularTiempoSintactico(){
        //resultado de la integral despejada
        double temp = r.nextDouble();
        return temp;
    }
    
    public double calcularTiempoSemantico(){
        //resultado de la integral despejada
        double temp = r.nextDouble();
        return temp;
    }
    
    public double calcularTiempoPermisos(){
        return (-(Math.log(1 - r.nextDouble()))/0.07);
    }
            
    public double calcularTiempoOptimizacion(Conexion c){
        if(c.isReadOnly()){
            return 0.1;
        }
        else{
            return 0.25;
        }
    }
    
    public void asignarConsultaAEjecutor(Conexion c,PriorityQueue<Evento> eventos, double reloj){
        int i = 0;
        while(i < ejecutorConsultas.length){
            if(ejecutorConsultas[i] == -1){
                ejecutorConsultas[i] = c.getNumServidor();
                Evento siguienteEjecucion = new Evento(calcularTiempoAlgoritmoEjecucion(c.getNumBloques(), c) + reloj, c, TipoEvento.EJECUTO_CONSULTA);
                eventos.add(siguienteEjecucion);
                return;
            }
            i++;
        }
        ejecutor.add(c);
    }
    
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
    
    public double calcularTamanoRespuesta(int cantidadDeBloques){
        return cantidadDeBloques/64;
    }
    
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
    
    public void procesarSalidaEjecutor(Conexion c,PriorityQueue<Evento> eventos, double reloj){
        int posLibre = eliminarConexionEjecutor(c);
        if(!ejecutor.isEmpty()){
            ejecutorConsultas[posLibre] = ejecutor.get(0).getNumServidor();
            Evento siguienteConsultaProcesada = new Evento(calcularTiempoAlgoritmoEjecucion(ejecutor.get(0).getNumBloques(), ejecutor.get(0)) + reloj, ejecutor.remove(0), TipoEvento.EJECUTO_CONSULTA);
            eventos.add(siguienteConsultaProcesada);
        }
    }
    
    public void procesarSalidaConsulta(Conexion c,PriorityQueue<Evento> eventos, double reloj){             
        int posLibre = eliminarConexionServidor(c);
        if(!consultas.isEmpty()){
            servidoresConsultas[posLibre] = consultas.get(0).getNumServidor(); //asigno la primera conexion de la lista al servidor que acabo de liberar  
            Evento siguienteConsultaProcesada = new Evento(calcularTiempoTotal(consultas.get(0)) + reloj, consultas.remove(0), TipoEvento.PROCESO_CONSULTA);
            eventos.add(siguienteConsultaProcesada);
        }
    }
    
    public int eliminarConexionEjecutor(Conexion c){
        int posLibre = -1;
        for(int i = 0; i < ejecutorConsultas.length; i++){
            if(ejecutorConsultas[i] == c.getNumServidor()){
                ejecutorConsultas[i] = -1;
                posLibre = 0;
            }
        }
        return posLibre;
    }
    
    public void eliminarConexion(Conexion c){
        this.eliminarConexionServidor(c);
        for(int i = 0; i < consultas.size(); i++){
             if(consultas.get(i) == c){
                 consultas.remove(i);
                 break;
             }
         }
        this.eliminarConexionEjecutor(c);
        for(int i = 0; i < ejecutor.size(); i++){
             if(ejecutor.get(i) == c){
                 ejecutor.remove(i);
                 return;
             }
        }
    }
}
