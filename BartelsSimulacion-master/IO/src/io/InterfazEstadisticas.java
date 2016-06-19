package io;

/**
 * Clase encargada de guardar los resultados de las estadisticas en cada corrida y mostrarlas en interfaz
 */
public class InterfazEstadisticas {
    int numCorrida,tamPromCHilo, tamPromCConsultas, tamPromCTrans,tamPromCEjecucion;
    double PromVida;  
    double tiempoSelectHilo, tiempoJoinHilo, tiempoUpdateHilo,tiempoDDLHilo;
    double tiempoSelectConsulta, tiempoJoinConsulta, tiempoUpdateConsulta,tiempoDDLConsulta;
    double tiempoSelectTransacciones, tiempoJoinTransacciones, tiempoUpdateTransacciones,tiempoDDLTransacciones;
    double tiempoSelectEjecucion, tiempoJoinEjecucion, tiempoUpdateEjecucion,tiempoDDLEjecucion;
    int[] ints = new int[5];
    double[] doubles = new double[6];
    
public InterfazEstadisticas(int numCorrida,int tamPromCHilo,int tamPromCConsultas,int tamPromCTrans,int tamPromCEjecucion,double PromVida, double tiempoSelectH,double tiempoJoinH,double tiempoUpdateH,double tiempoDDLH, double tiempoSelectC,double tiempoJoinC,double tiempoUpdateC,double tiempoDDLC, double tiempoSelectT,double tiempoJoinT,double tiempoUpdateT,double tiempoDDLT, double tiempoSelectE,double tiempoJoinE,double tiempoUpdateE,double tiempoDDLE, double reloj){
        ints[0] = numCorrida;
        ints[1] = tamPromCHilo;
        ints[2] = tamPromCConsultas;
        ints[3] = tamPromCTrans;
        ints[4] = tamPromCEjecucion;
        doubles[0] = PromVida;
        doubles[5] = reloj;
        tiempoSelectHilo = tiempoSelectH;
        tiempoJoinHilo = tiempoJoinH;
        tiempoUpdateHilo = tiempoUpdateH;
        tiempoDDLHilo = tiempoDDLH;
        tiempoSelectTransacciones = tiempoSelectT;
        tiempoJoinTransacciones = tiempoJoinT;
        tiempoUpdateTransacciones = tiempoUpdateT;
        tiempoDDLTransacciones = tiempoDDLT;
        tiempoSelectEjecucion = tiempoSelectE;
        tiempoJoinEjecucion = tiempoJoinE;
        tiempoUpdateEjecucion = tiempoUpdateE;
        tiempoDDLEjecucion= tiempoDDLE;
}   

public int[] returnInts(){
    
    return ints;
}

public double[] returnDoublesHilo(){
    doubles[1] = tiempoSelectHilo;
    doubles[2] = tiempoJoinHilo;
    doubles[3] = tiempoUpdateHilo;
    doubles[4] = tiempoDDLHilo; 
    return doubles;
}

public double[] returnDoublesConsulta(){
    doubles[1] = tiempoSelectConsulta;
    doubles[2] = tiempoJoinConsulta;
    doubles[3] = tiempoUpdateConsulta;
    doubles[4] = tiempoDDLConsulta; 
    return doubles;
}

public double[] returnDoublesTransacciones(){
    doubles[1] = tiempoSelectTransacciones;
    doubles[2] = tiempoJoinTransacciones;
    doubles[3] = tiempoUpdateTransacciones;
    doubles[4] = tiempoDDLTransacciones; 
    return doubles;
}

public double[] returnDoublesEjecucion(){
    doubles[1] = tiempoSelectEjecucion;
    doubles[2] = tiempoJoinEjecucion;
    doubles[3] = tiempoUpdateEjecucion;
    doubles[4] = tiempoDDLEjecucion; 
    return doubles;
}
    
}
