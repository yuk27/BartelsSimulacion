package io;

import java.util.Vector;

public class Estadisticas {
    private double tiempoPromedioGeneral;
    private double conexionesDescartadas;
    
//Mod Adm de Procesos
    private double tiempoPromedioSelectP;
    private double tiempoPromedioUpdateP;
    private double tiempoPromedioJoinP;
    private double tiempoPromedioDDLP;

    
//Mod Procesamiento de consultas
    private double tiempoPromedioSelectC;
    private double tiempoPromedioUpdateC;
    private double tiempoPromedioJoinC;
    private double tiempoPromedioDDLC;

//Mod Transacciones
    private double tiempoPromedioSelectT;
    private double tiempoPromedioUpdateT;
    private double tiempoPromedioJoinT;
    private double tiempoPromedioDDLT;

//Mod Ejecutor
    private double tiempoPromedioSelectE;
    private double tiempoPromedioUpdateE;
    private double tiempoPromedioJoinE;
    private double tiempoPromedioDDLE;
 
//Colas 
    private int colaClientes;
    private int colaProcesos;
    private int colaProcesamientoConsultas;
    private int colaEjecutor;
    private int colaTransacciones;
    

    //calcula el tiempo promedio de las conexiones en el sistema  en el sistema
    public void calcularTiempoPromedioProcesos(Vector<Double> conexiones, int type){
        double temp = 0.0;
        for(int i = 0; i < conexiones.size(); i++){
            temp += conexiones.get(i);
        }
        if(!conexiones.isEmpty()){
            switch(type){
                case 0:
                    this.tiempoPromedioSelectP = temp/conexiones.size();
                    break; 
                case 1:
                    this.tiempoPromedioJoinP = temp/conexiones.size();
                    break;                
                case 2:
                    this.tiempoPromedioUpdateP = temp/conexiones.size();
                    break;        
                case 3:
                    this.tiempoPromedioDDLP = temp/conexiones.size();
                    break;     
            }
        }
    }
    
    public void calcularTiempoPromedioConsultas(Vector<Double> conexiones, int type){
            double temp = 0.0;
            for(int i = 0; i < conexiones.size(); i++){
                temp += conexiones.get(i);
            }
            if(!conexiones.isEmpty()){
                switch(type){
                    case 0:
                        this.tiempoPromedioSelectC = temp/conexiones.size();
                        break; 
                    case 1:
                        this.tiempoPromedioJoinC = temp/conexiones.size();
                        break;                
                    case 2:
                        this.tiempoPromedioUpdateC = temp/conexiones.size();
                        break;        
                    case 3:
                        this.tiempoPromedioDDLC = temp/conexiones.size();
                        break;     
                }
            }
    }
    
    public void calcularTiempoPromedioTransacciones(Vector<Double> conexiones, int type){
        double temp = 0.0;
        for(int i = 0; i < conexiones.size(); i++){
            temp += conexiones.get(i);
        }
        if(!conexiones.isEmpty()){
            switch(type){
                case 0:
                    this.tiempoPromedioSelectT = temp/conexiones.size();
                    break; 
                case 1:
                    this.tiempoPromedioJoinT = temp/conexiones.size();
                    break;                
                case 2:
                    this.tiempoPromedioUpdateT = temp/conexiones.size();
                    break;        
                case 3:
                    this.tiempoPromedioDDLT = temp/conexiones.size();
                    break;     
            }
        }
    }
    
    public void calcularTiempoPromedioEjecutor(Vector<Double> conexiones, int type){
        double temp = 0.0;
        for(int i = 0; i < conexiones.size(); i++){
            temp += conexiones.get(i);
        }
        if(!conexiones.isEmpty()){
            switch(type){
                case 0:
                    this.tiempoPromedioSelectE = temp/conexiones.size();
                    break; 
                case 1:
                    this.tiempoPromedioJoinE = temp/conexiones.size();
                    break;                
                case 2:
                    this.tiempoPromedioUpdateE = temp/conexiones.size();
                    break;        
                case 3:
                    this.tiempoPromedioDDLE = temp/conexiones.size();
                    break;     
            }
        }
    }
    
        public void calcularTiempoPromedio(Vector<Double> conexiones, int type){
        double temp = 0.0;
        for(int i = 0; i < conexiones.size(); i++){
            temp += conexiones.get(i);
        }
        if(!conexiones.isEmpty()){
                this.tiempoPromedioGeneral = temp/conexiones.size();
        }
    }
    
    //calcula el tamano promedio de la colas de los modulos 
    public void calcularPromedioInt(Vector<Integer> queue, int type){
        int temp = 0;
        for(int i = 0; i < queue.size(); i++){
            temp += queue.get(i);
        }
        if(!queue.isEmpty()){
            switch(type){
                case 0:
                    this.colaClientes= temp/queue.size();
                    break; 
                case 1:
                    this.colaProcesos = temp/queue.size();
                    break;                
                case 2:
                    this.colaProcesamientoConsultas = temp/queue.size();
                    break;
                case 3:
                    this.colaTransacciones = temp/queue.size();
                    break;
                 case 4:
                    this.colaEjecutor = temp/queue.size();
                    break;
            }
        }
    }
    
    public void setConexionesDescartadas(int conexionesDescartadas){
        this.conexionesDescartadas = conexionesDescartadas;
    }
}
