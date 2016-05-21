/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ulises
 */
public class Transacciones {
    boolean hayDDL;
    int[] servidores;
    List<Conexion> conexionesConPrioridad;
    
    public Transacciones(int p){
        servidores = new int[p];
        conexionesConPrioridad = new ArrayList<>();
        hayDDL = false;
    }
    
    public void asignarConexion(Conexion c){
        int i = 0;
        while(i < servidores.length){
            if(servidores[i] == -1){
                servidores[i] = c.getNumServidor();
                break;
            }
            i++;
        }
        conexionesConPrioridad.add(c);
    }
    
    Conexion getConexionDePrioridad(){
        for(int i = 0; i < conexionesConPrioridad.size(); i++){
            
        }
    }
    
}
