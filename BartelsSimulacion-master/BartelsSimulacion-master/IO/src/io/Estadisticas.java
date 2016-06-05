package io;

import java.util.Vector;

public class Estadisticas {
    public double tiempoPromedioGeneral;
    public double tiempoPromedioSelect;
    public double tiempoPromedioUpdate;
    public double tiempoPromedioJoin;
    public double tiempoPromedioDDL;
    public double conexionesDescartadas;
    
    public int colaClientes;
    public int colaProcesos;
    public int colaProcesamientoConsultas;
    public int colaTransacciones;
    
    
            //calcula el tiempo promedio de las conexiones en el sistema  en el sistema
        public void calcularPromedioDouble(Vector<Double> conexiones, int type){
            double temp = 0.0;
            for(int i = 0; i < conexiones.size(); i++){
                temp += conexiones.get(i);
            }
            if(!conexiones.isEmpty()){
                switch(type){
                    case 0:
                        this.tiempoPromedioSelect = temp/conexiones.size();
                        break; 
                    case 1:
                        this.tiempoPromedioJoin = temp/conexiones.size();
                        break;                
                    case 2:
                        this.tiempoPromedioUpdate = temp/conexiones.size();
                        break;        
                    case 3:
                        this.tiempoPromedioDDL = temp/conexiones.size();
                        break;     
                    default:
                        this.tiempoPromedioGeneral = temp/conexiones.size();
                        break;
                }
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
                }
            }
        }
}
