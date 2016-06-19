package io;

import java.util.Vector;

/**
 * Clase encargada de calcular los promedios de cada dato perdido y guardarlo
 * @author Ulises
 */
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
    

   /**
    * Calcula el tiempo promedio de las conexiones en el modulo de procesos
    * @param conexiones vector que contiene los tiempos de todas las conexiones que salieron del modulo de procesos
    * @param type tipo de conexion a la que se le calculará el promedio
    */
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
    
      /**
    * Calcula el tiempo promedio de las conexiones en el modulo de consultas
    * @param conexiones vector que contiene los tiempos de todas las conexiones que salieron del modulo de consutlas
    * @param type tipo de conexion a la que se le calculará el promedio
    */
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
    
      /**
    * Calcula el tiempo promedio de las conexiones en el modulo de transacciones
    * @param conexiones vector que contiene los tiempos de todas las conexiones que salieron del modulo de transacciones
    * @param type tipo de conexion a la que se le calculará el transacciones
    */
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
    
    
      /**
    * Calcula el tiempo promedio de las conexiones en el modulo ejecutor
    * @param conexiones vector que contiene los tiempos de todas las conexiones que salieron del modulo ejecutor
    * @param type tipo de conexion a la que se le calculará el promedio
    */
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
    
    /**
     * Calcula el tiempo promedio de las conexiones en el sistema
     * @param conexiones vector que contiene el tiempo que estuvieron todas las conexiones que salieron del sistema
     */
        public void calcularTiempoPromedio(Vector<Double> conexiones){
        double temp = 0.0;
        for(int i = 0; i < conexiones.size()-1; i++){
            temp += conexiones.get(i);
        }
        if(!conexiones.isEmpty()){
                this.tiempoPromedioGeneral = temp/conexiones.size();
        }
    }
 
        
    /**
     * Calcula el tamano promedio de la colas de los modulos 
     * @param queue vector que contiene el tamaño de las colas en distintos momentos
     * @param type tipo de cola a la que se le calculará el promedio
     */
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
    
 /**
  * Calcula todos los promedios de todas las corridas de la simulación
  * @param tamanoCola tamaño de la cola utilizado para calcular el promedio
  */
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
    
    /**
     * Devuelve el tamaño de la cola de clientes
     * @return tamaño de la cola de clientes
     */
    public int getColaClientes(){
        return this.colaClientes;
    }
    
    /**
     * Devuelve el tamaño de la cola de procesos
     * @return tamaño de la cola de procesos
     */
    public int getColaProcesos(){
        return this.colaProcesos;
    }
    
    /**
     * Devuelve el tamaño de la cola de consultas
     * @return tamaño de la cola de consultas
     */
    public int getColaProcesamientoConsultas(){
        return this.colaProcesamientoConsultas;
    }
    
    
    /**
     * Devuelve el tamaño de la cola del ejecutor
     * @return tamaño de la cola del ejecutor
     */
    public int getColaEjecutor(){
        return this.colaEjecutor;
    }
    
    
    /**
     * Devuelve el tamaño de la cola de transacciones
     * @return tamaño de la cola de transacciones
     */
    public int getColaTransacciones(){
        return this.colaTransacciones;
    }
    
    /**
     * Pone el valor especificado en la variable colaClientes
     * @param cola valor a poner
     */
    public void setColaClientes(int cola){
        this.colaClientes += cola;
    }
    
    /**
     * Pone el valor especificado en la variable colaProcesos
     * @param cola valor a poner
     */
    public void setColaProcesos(int cola){
        this.colaProcesos += cola;
    }
    
    /**
     * Pone el valor especificado en la variable colaProcesamientoConsultas
     * @param cola valor a poner
     */
    public void setColaProcesamientoConsultas(int cola){
        this.colaProcesamientoConsultas += cola;
    }
    
    /**
     * Pone el valor especificado en la variable colaTransacciones
     * @param cola valor a poner
     */
    public void setColaTransacciones(int cola){
        this.colaTransacciones += cola;
    }
    
    /**
     * Pone el valor especificado en la variable colaEjecutor
     * @param cola valor a poner
     */
    public void setColaEjecutor(int cola){
        this.colaEjecutor += cola;
    }
    
    /**
     * Devuelve el tiempo promedio de una conexion de tipo Select en el módulo de procesos
     * @return tiempo promedio del tipo de conexion
     */
    public double getTiempoPromedioSelectP(){
        return this.tiempoPromedioSelectP;
    }
    
    /**
     * Devuelve el tiempo promedio de una conexion de tipo Update en el módulo de procesos
     * @return tiempo promedio del tipo de conexion
     */
    public double getTiempoPromedioUpdateP(){
        return this.tiempoPromedioUpdateP;
    }  
    
    /**
     * Devuelve el tiempo promedio de una conexion de tipo Join en el módulo de procesos
     * @return tiempo promedio del tipo de conexion
     */
    public double getTiempoPromedioJoinP(){
        return this.tiempoPromedioJoinP;
    }
    
    /**
     * Devuelve el tiempo promedio de una conexion de tipo DDL en el módulo de procesos
     * @return tiempo promedio del tipo de conexion
     */
    public double getTiempoPromedioDDLP(){
        return this.tiempoPromedioDDLP;
    }
    
    /**
     * Cambia el valor de tiempoPromedioSelectP por el especificado
     * @param tiempo valor por el que se cambiará la variable
     */
    public void setTiempoPromedioSelectP(double tiempo){
        this.tiempoPromedioSelectP += tiempo;
    }
    
    /**
     * Cambia el valor de tiempoPromedioUpdateP por el especificado
     * @param tiempo valor por el que se cambiará la variable
     */
    public void setTiempoPromedioUpdateP(double tiempo){
        this.tiempoPromedioUpdateP += tiempo;
    }
    
    /**
     * Cambia el valor de tiempoPromedioJoinP por el especificado
     * @param tiempo valor por el que se cambiará la variable
     */
    public void setTiempoPromedioJoinP(double tiempo){
        this.tiempoPromedioJoinP += tiempo;
    }
    
    /**
     * Cambia el valor de tiempoPromedioDDLP por el especificado
     * @param tiempo valor por el que se cambiará la variable
     */
    public void setTiempoPromedioDDLP(double tiempo){
        this.tiempoPromedioDDLP += tiempo;
    }
    
    /**
     * Devuelve el tiempo promedio de una conexion de tipo Select en el módulo de consultas
     * @return tiempo promedio del tipo de conexion
     */
    public double getTiempoPromedioSelectC(){
        return this.tiempoPromedioSelectC;
    }
    
    /**
     * Devuelve el tiempo promedio de una conexion de tipo Update en el módulo de consultas
     * @return tiempo promedio del tipo de conexion
     */
    public double getTiempoPromedioUpdateC(){
        return this.tiempoPromedioUpdateC;
    }
    
    /**
     * Devuelve el tiempo promedio de una conexion de tipo Join en el módulo de consultas
     * @return tiempo promedio del tipo de conexion
     */
    public double getTiempoPromedioJoinC(){
        return this.tiempoPromedioJoinC;
    }
    
    /**
     * Devuelve el tiempo promedio de una conexion de tipo DDL en el módulo de consultas
     * @return tiempo promedio del tipo de conexion
     */
    public double getTiempoPromedioDDLC(){
        return this.tiempoPromedioDDLC;
    }
    
    /**
     * Cambia el valor de tiempoPromedioSelectC por el especificado
     * @param tiempo valor por el que se cambiará la variable
     */
    public void setTiempoPromedioSelectC(double tiempo){
        this.tiempoPromedioSelectC += tiempo;
    }
    
    /**
     * Cambia el valor de tiempoPromedioUpdateC por el especificado
     * @param tiempo valor por el que se cambiará la variable
     */
    public void setTiempoPromedioUpdateC(double tiempo){
        this.tiempoPromedioUpdateC += tiempo;
    }
    
    /**
     * Cambia el valor de tiempoPromedioJoinC por el especificado
     * @param tiempo valor por el que se cambiará la variable
     */
    public void setTiempoPromedioJoinC(double tiempo){
        this.tiempoPromedioJoinC += tiempo;
    }
    
    /**
     * Cambia el valor de tiempoPromedioDDLC por el especificado
     * @param tiempo valor por el que se cambiará la variable
     */
    public void setTiempoPromedioDDLC(double tiempo){
        this.tiempoPromedioDDLC += tiempo;
    }
    
    /**
     * Devuelve el tiempo promedio de una conexion de tipo Select en el módulo de transacciones
     * @return tiempo promedio del tipo de conexion
     */
    public double getTiempoPromedioSelectT(){
        return this.tiempoPromedioSelectT;
    }
    
    /**
     * Devuelve el tiempo promedio de una conexion de tipo Update en el módulo de transacciones
     * @return tiempo promedio del tipo de conexion
     */
    public double getTiempoPromedioUpdateT(){
        return this.tiempoPromedioUpdateT;
    }
    
    /**
     * Devuelve el tiempo promedio de una conexion de tipo Join en el módulo de transacciones
     * @return tiempo promedio del tipo de conexion
     */
    public double getTiempoPromedioJoinT(){
        return this.tiempoPromedioJoinT;
    }
    
    /**
     * Devuelve el tiempo promedio de una conexion de tipo DDL en el módulo de transacciones
     * @return tiempo promedio del tipo de conexion
     */
    public double getTiempoPromedioDDLT(){
        return this.tiempoPromedioDDLT;
    }
    
    /**
     * Cambia el valor de tiempoPromedioSelectT por el especificado
     * @param tiempo valor por el que se cambiará la variable
     */
    public void setTiempoPromedioSelectT(double tiempo){
        this.tiempoPromedioSelectT += tiempo;
    }
    
    /**
     * Cambia el valor de tiempoPromedioUpdateT por el especificado
     * @param tiempo valor por el que se cambiará la variable
     */
    public void setTiempoPromedioUpdateT(double tiempo){
        this.tiempoPromedioUpdateT += tiempo;
    }
    
    /**
     * Cambia el valor de tiempoPromedioJoinT por el especificado
     * @param tiempo valor por el que se cambiará la variable
     */
    public void setTiempoPromedioJoinT(double tiempo){
        this.tiempoPromedioJoinT += tiempo;
    }
    
    /**
     * Cambia el valor de tiempoPromedioDDLT por el especificado
     * @param tiempo valor por el que se cambiará la variable
     */
    public void setTiempoPromedioDDLT(double tiempo){
        this.tiempoPromedioDDLT += tiempo;
    }
    
    /**
     * Devuelve el tiempo promedio de una conexion de tipo Select en el módulo ejecutor
     * @return tiempo promedio del tipo de conexion
     */
    public double getTiempoPromedioSelectE(){
        return this.tiempoPromedioSelectE;
    }
    
    /**
     * Devuelve el tiempo promedio de una conexion de tipo Update en el módulo ejecutor
     * @return tiempo promedio del tipo de conexion
     */
    public double getTiempoPromedioUpdateE(){
        return this.tiempoPromedioUpdateE;
    }
    
    /**
     * Devuelve el tiempo promedio de una conexion de tipo Join en el módulo ejecutor
     * @return tiempo promedio del tipo de conexion
     */
    public double getTiempoPromedioJoinE(){
        return this.tiempoPromedioJoinE;
    }
    
    /**
     * Devuelve el tiempo promedio de una conexion de tipo DDL en el módulo ejecutor
     * @return tiempo promedio del tipo de conexion
     */
    public double getTiempoPromedioDDLE(){
        return this.tiempoPromedioDDLE;
    }
    
    /**
     * Cambia el valor de tiempoPromedioSelectE por el especificado
     * @param tiempo valor por el que se cambiará la variable
     */
    public void setTiempoPromedioSelectE(double tiempo){
        this.tiempoPromedioSelectE += tiempo;
    }
    
    /**
     * Cambia el valor de tiempoPromedioUpdateE por el especificado
     * @param tiempo valor por el que se cambiará la variable
     */
    public void setTiempoPromedioUpdateE(double tiempo){
        this.tiempoPromedioUpdateE += tiempo;
    }
    
    /**
     * Cambia el valor de tiempoPromedioJoinE por el especificado
     * @param tiempo valor por el que se cambiará la variable
     */
    public void setTiempoPromedioJoinE(double tiempo){
        this.tiempoPromedioJoinE += tiempo;
    }
    
    /**
     * Cambia el valor de tiempoPromedioDDLEpor el especificado
     * @param tiempo valor por el que se cambiará la variable
     */
    public void setTiempoPromedioDDLE(double tiempo){
        this.tiempoPromedioDDLE += tiempo;
    }
    
    /**
     * Devuelve el número de conexiones descartadas
     * @return número de conxiones descartadas
     */
    public double getConexionesDescartadas(){
        return this.conexionesDescartadas;
    }
    
    /**
     * Devuelve el tiempo promedio que una conexion pasa en el sistema
     * @return tiempo promedio  de uina conexion en el sistema
     */
    public double getTiempoPromedio(){
        return this.tiempoPromedioGeneral;
    }
    
    /**
     * Pone el valor de conexionesDescartadas por el valor especificado
     * @param conexiones valor a poner
     */
    public void setConexionesDescartadas(double conexiones){
        this.conexionesDescartadas += conexiones;
    }
    
    /**
     * Pone el valor de tiempoPromedioGeneral por el valor especificado
     * @param tiempo valor a poner
     */
    public void setTiempoPromedioGeneral(double tiempo){
        this.tiempoPromedioGeneral += tiempo;
    }
        
}
