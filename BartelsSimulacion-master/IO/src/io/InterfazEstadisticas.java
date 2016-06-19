package io;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 */
public class InterfazEstadisticas {
    int numCorrida,tamPromCHilo, tamPromCConsultas, tamPromCTrans,tamPromCEjecucion;
    double PromVida, tiempoSelect, tiempoJoin, tiempoUpdate,tiempoDDL;
    int[] ints = new int[5];
    double[] doubles = new double[6];
    
public InterfazEstadisticas(int numCorrida,int tamPromCHilo,int tamPromCConsultas,int tamPromCTrans,int tamPromCEjecucion,double PromVida, double tiempoSelect,double tiempoJoin,double tiempoUpdate,double tiempoDDL, double reloj){
        ints[0] = numCorrida;
        ints[1] = tamPromCHilo;
        ints[2] = tamPromCConsultas;
        ints[3] = tamPromCTrans;
        ints[4] = tamPromCEjecucion;
        doubles[0] = PromVida;
        doubles[1] = tiempoSelect;
        doubles[2] = tiempoJoin;
        doubles[3] = tiempoUpdate;
        doubles[4] = tiempoDDL; 
        doubles[5] = reloj; 
}   

public int[] returnInts(){
    
    return ints;
}

public double[] returnDoubles(){
    return doubles;
}
    
}
