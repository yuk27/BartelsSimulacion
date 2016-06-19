package io;
import java.util.Random;

/**
 * Clase objeto que será utilizada para simular cada conexión entrante a la base de datos.
 * 
 */
public class Conexion {
    
    private int tipo = 0; //tipo de instruccion 0 SELECT, 1 UPDATE, 2 JOIN, 3 DLL 
    private double timeout = 0; //tiempo antes de ser eliminado por timeout
    private int numServidor = 0; //llave para identificar si logro entrar y en que servidor del procesamiento de entradas quedo
    private double  tiempoEntrada; //tiempo de reloj en el cual fue hecho el objeto
    private int numBloques = 0; //cantidad de bloques de memoria que ocupa la conexion
    private double tiempoEntradaModulo = 0.0; //tiempo en que entra a un modulo para llevar estadisticas
    private Random r = new Random();
 
    /**
     *Contructor de la clase, encargado de inicializar las variables
     * @param tiempoEntrada double que representa el tiempo de reloj en el cual entro
     * @param timeoutGlobal tiempo que dura desde ser creada la conexión hasta el momento que se debe eliminar por timeout
     * @param numServidor  //llave para identificar si logro entrar y en que servidor del procesamiento de entradas quedo
     */
    public Conexion(double tiempoEntrada,double timeoutGlobal, int numServidor){
       this.tiempoEntrada = tiempoEntrada;
       this.numServidor = numServidor;
       generarTimeout(timeoutGlobal);
    }
    
    /**
     * Metodo encargado de generar el tiempo en el cual llegará la conexión
     * @param rnd entrada random para generar la llegada no excta e diferente entre conexiones
     * @return devuelve un double que representa el tiempo de llegada de la conexión
     */
    public static double generarTiempoArribo(double rnd){
        return -1/30d *((Math.log(1 - rnd)));
    }
    
    /**
     * Metodo encargado segun un valor random decidir que tipo de de intruccion contiene la conexion
     */
    public void generarTipo(){   
        double val = r.nextDouble();
        if(val < 0.3){
            tipo = 0;
        }
        else if(val < 0.55){    
            tipo = 1;      
        }
        else if(val < 0.9){
            tipo = 2;
        }
        else{
            tipo = 3;
        }
    }
    
    /**
     * Metodo encargado de generar el timeout especifico para la conexión
     * toma el tiempo de reloj en que entró la conexion y le suma el tiempo que el usuario eligio como timeout
     * @param timeoutGlobal tiempo que dura desde ser creada la conexión hasta el momento que se debe eliminar por timeout
     */
    public void generarTimeout(double  timeoutGlobal){
       this.timeout = tiempoEntrada + timeoutGlobal; 
    }
    
    /**
     * Metodo que devuelve el timeout de la conexion especifica
     * @return devuelve un double representando un tiempo de reloj
     */
    public double getTimeout(){
        return timeout;     
    }
    
    /**
     * Metodo que devuelve el tiempo de reloj en donde se creo de la conexion especifica
     * @return devuelve un double representando un tiempo de reloj
     */
    public double getTiempoEntrada(){
        return tiempoEntrada;     
    }
        
    /**
     * Metodo que devuelve el atributo de escritura del tipo de instruccion que contiene la conexion
     * @return devuelve un false si es tipo 1 o 3, devuelve true si es 0 o 2
     */
    public boolean isReadOnly(){
        return tipo == 0 || tipo == 2;
    }
    
    /**
     * Metodo que devuelve el tipo de instruccion que contiene la conexion
     * @return devuelve un enum tipo
     */
    public int getTipo(){
        return this.tipo;
    }

    /**
     * Metodo que devuelve el numero de sevidor en donde se creo de la conexion especifica
     * @return devuelve un double representando un tiempo de reloj
     */
    public int getNumServidor(){
        return this.numServidor;
    }

    /**
     * Metodo que devuelve cantidad de bloques de memoria que ocupa la conexion 
     * @return devuelve un int representando cantidad de bloques de memoria que ocupa la conexion
     */
    public int getNumBloques(){
        return numBloques;
    }

    /**
     * Metodo que inserta cantidad de bloques de memoria que ocupa la conexion 
     * @param numBloques  cantidad de bloques de memoria que ocupa la conexion 
     */
    public void setNumBloques(int numBloques){
        this.numBloques = numBloques;
    }

    /**
     *Metodo que inserta el tiempo de reloj en el cual se creo la conexion
     * @param reloj double representando el tiempo de reloj
     */
    public void setTiempoEntradaModulo(double reloj){
        this.tiempoEntradaModulo = reloj;
    }
    
    /**
     * Metodo que devuelve el tiempo de reloj en el cual entro a un modulo la conexion
     * @return devuelve un double representando el tiempo
     */
    public double getTiempoEntradaModulo(){
        return this.tiempoEntradaModulo;
    }
    
}
