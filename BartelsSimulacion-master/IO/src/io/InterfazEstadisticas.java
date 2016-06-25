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
    
public InterfazEstadisticas(int numCorrida,Estadisticas estadistica, double reloj){
        ints[0] = numCorrida;
        ints[1] = estadistica.getColaProcesos();
        ints[2] = estadistica.getColaProcesamientoConsultas();
        ints[3] = estadistica.getColaTransacciones();
        ints[4] = estadistica.getColaEjecutor();
        doubles[0] = estadistica.getTiempoPromedio();
        tiempoSelectHilo = estadistica.getTiempoPromedioSelectP();
        tiempoJoinHilo = estadistica.getTiempoPromedioJoinP();
        tiempoUpdateHilo = estadistica.getTiempoPromedioUpdateP();
        tiempoDDLHilo = estadistica.getTiempoPromedioDDLP();
        tiempoSelectTransacciones = estadistica.getTiempoPromedioSelectT();
        tiempoJoinTransacciones = estadistica.getTiempoPromedioJoinT();
        tiempoUpdateTransacciones = estadistica.getTiempoPromedioUpdateT();
        tiempoDDLTransacciones = estadistica.getTiempoPromedioDDLT();
        tiempoSelectEjecucion = estadistica.getTiempoPromedioSelectE();
        tiempoJoinEjecucion = estadistica.getTiempoPromedioJoinE();
        tiempoUpdateEjecucion = estadistica.getTiempoPromedioUpdateE();
        tiempoDDLEjecucion= estadistica.getTiempoPromedioDDLE();
        tiempoSelectConsulta = estadistica.getTiempoPromedioSelectC();
        tiempoJoinConsulta = estadistica.getTiempoPromedioJoinC();
        tiempoUpdateConsulta = estadistica.getTiempoPromedioUpdateC();
        tiempoDDLConsulta = estadistica.getTiempoPromedioDDLC();
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
