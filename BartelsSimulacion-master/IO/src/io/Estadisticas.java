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
        for(int i = 0; i < conexiones.size()-1; i++){
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
            for(int i = 0; i < conexiones.size()-1; i++){
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
        for(int i = 0; i < conexiones.size()-1; i++){
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
        for(int i = 0; i < conexiones.size()-1; i++){
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
    
        public void calcularTiempoPromedio(Vector<Double> conexiones){
        double temp = 0.0;
        for(int i = 0; i < conexiones.size()-1; i++){
            temp += conexiones.get(i);
        }
        if(!conexiones.isEmpty()){
                this.tiempoPromedioGeneral = temp/conexiones.size();
        }
    }
    
    //calcula el tamano promedio de la colas de los modulos 
    public void calcularPromedioInt(Vector<Integer> queue, int type){
        int temp = 0;
        for(int i = 0; i < queue.size()-1; i++){
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
    
    public void calcularPromedioFinal(int tamanoCola){
        this.colaClientes /= tamanoCola;
        this.colaEjecutor  /= tamanoCola;
        this.colaProcesamientoConsultas /= tamanoCola;
        this.colaProcesos /= tamanoCola;
        this.colaTransacciones /= tamanoCola;
        
        this.conexionesDescartadas  /= tamanoCola;
        
        this.tiempoPromedioDDLC /= tamanoCola;
        this.tiempoPromedioDDLE /= tamanoCola;
        this.tiempoPromedioDDLP /= tamanoCola;
        this.tiempoPromedioDDLT /= tamanoCola;
        
        this.tiempoPromedioGeneral /= tamanoCola;
        
        this.tiempoPromedioJoinC /= tamanoCola;
        this.tiempoPromedioJoinE /= tamanoCola;
        this.tiempoPromedioJoinP /= tamanoCola;
        this.tiempoPromedioJoinT /= tamanoCola;
        
        this.tiempoPromedioSelectC /= tamanoCola;
        this.tiempoPromedioSelectE /= tamanoCola;
        this.tiempoPromedioSelectP /= tamanoCola;
        this.tiempoPromedioSelectT /= tamanoCola;
        
        this.tiempoPromedioUpdateC /= tamanoCola;
        this.tiempoPromedioUpdateE /= tamanoCola;
        this.tiempoPromedioUpdateP /= tamanoCola;
        this.tiempoPromedioUpdateT /= tamanoCola;
    }
    
    public int getColaClientes(){
        return this.colaClientes;
    }
    
    public int getColaProcesos(){
        return this.colaProcesos;
    }
    
    public int getColaProcesamientoConsultas(){
        return this.colaProcesamientoConsultas;
    }
    
    public int getColaEjecutor(){
        return this.colaEjecutor;
    }
    
    public int getColaTransacciones(){
        return this.colaTransacciones;
    }
    
    public void setColaClientes(int cola){
        this.colaClientes += cola;
    }
    
    public void setColaProcesos(int cola){
        this.colaProcesos += cola;
    }
    
    public void setColaProcesamientoConsultas(int cola){
        this.colaProcesamientoConsultas += cola;
    }
    
    public void setColaTransacciones(int cola){
        this.colaTransacciones += cola;
    }
    
    public void setColaEjecutor(int cola){
        this.colaEjecutor += cola;
    }
    
    public double getTiempoPromedioSelectP(){
        return this.tiempoPromedioSelectP;
    }
    
    public double getTiempoPromedioUpdateP(){
        return this.tiempoPromedioUpdateP;
    }
    
    public double getTiempoPromedioJoinP(){
        return this.tiempoPromedioJoinP;
    }
    
    public double getTiempoPromedioDDLP(){
        return this.tiempoPromedioDDLP;
    }
    
    public void setTiempoPromedioSelectP(double tiempo){
        this.tiempoPromedioSelectP += tiempo;
    }
    
    public void setTiempoPromedioUpdateP(double tiempo){
        this.tiempoPromedioUpdateP += tiempo;
    }
    
    public void setTiempoPromedioJoinP(double tiempo){
        this.tiempoPromedioJoinP += tiempo;
    }
    
    public void setTiempoPromedioDDLP(double tiempo){
        this.tiempoPromedioDDLP += tiempo;
    }
    
    public double getTiempoPromedioSelectC(){
        return this.tiempoPromedioSelectC;
    }
    
    public double getTiempoPromedioUpdateC(){
        return this.tiempoPromedioUpdateC;
    }
    
    public double getTiempoPromedioJoinC(){
        return this.tiempoPromedioJoinC;
    }
    
    public double getTiempoPromedioDDLC(){
        return this.tiempoPromedioDDLC;
    }
    
    public void setTiempoPromedioSelectC(double tiempo){
        this.tiempoPromedioSelectC += tiempo;
    }
    
    public void setTiempoPromedioUpdateC(double tiempo){
        this.tiempoPromedioUpdateC += tiempo;
    }
    
    public void setTiempoPromedioJoinC(double tiempo){
        this.tiempoPromedioJoinC += tiempo;
    }
    
    public void setTiempoPromedioDDLC(double tiempo){
        this.tiempoPromedioDDLC += tiempo;
    }
    
    public double getTiempoPromedioSelectT(){
        return this.tiempoPromedioSelectT;
    }
    
    public double getTiempoPromedioUpdateT(){
        return this.tiempoPromedioUpdateT;
    }
    
    public double getTiempoPromedioJoinT(){
        return this.tiempoPromedioJoinT;
    }
    
    public double getTiempoPromedioDDLT(){
        return this.tiempoPromedioDDLT;
    }
    
    public void setTiempoPromedioSelectT(double tiempo){
        this.tiempoPromedioSelectT += tiempo;
    }
    
    public void setTiempoPromedioUpdateT(double tiempo){
        this.tiempoPromedioUpdateT += tiempo;
    }
    
    public void setTiempoPromedioJoinT(double tiempo){
        this.tiempoPromedioJoinT += tiempo;
    }
    
    public void setTiempoPromedioDDLT(double tiempo){
        this.tiempoPromedioDDLT += tiempo;
    }
    
    public double getTiempoPromedioSelectE(){
        return this.tiempoPromedioSelectE;
    }
    
    public double getTiempoPromedioUpdateE(){
        return this.tiempoPromedioUpdateE;
    }
    
    public double getTiempoPromedioJoinE(){
        return this.tiempoPromedioJoinE;
    }
    
    public double getTiempoPromedioDDLE(){
        return this.tiempoPromedioDDLE;
    }
    
    public void setTiempoPromedioSelectE(double tiempo){
        this.tiempoPromedioSelectE += tiempo;
    }
    
    public void setTiempoPromedioUpdateE(double tiempo){
        this.tiempoPromedioUpdateE += tiempo;
    }
    
    public void setTiempoPromedioJoinE(double tiempo){
        this.tiempoPromedioJoinE += tiempo;
    }
    
    public void setTiempoPromedioDDLE(double tiempo){
        this.tiempoPromedioDDLE += tiempo;
    }
    
    public double getConexionesDescartadas(){
        return this.conexionesDescartadas;
    }
    
    public double getTiempoPromedio(){
        return this.tiempoPromedioGeneral;
    }
    
    public void setConexionesDescartadas(double conexiones){
        this.conexionesDescartadas += conexiones;
    }
    
    public void setTiempoPromedioGeneral(double tiempo){
        this.tiempoPromedioGeneral += tiempo;
    }
        
}
