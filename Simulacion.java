/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;
/**
 *
 * @author b26441
 */
public class Simulacion {
    
    int timeOutGlobal = 0;
    int tiempoActual = 0;
    

    Random r = new Random();
    AdmClientes adm;
    
    void addConexion(){
    
       if(adm.hayServidor()){
       
          adm.crearConexion(tiempoActual,timeOutGlobal);
          
           
       }
        
    }
    
}
