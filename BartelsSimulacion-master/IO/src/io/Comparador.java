package io;

import java.util.Comparator;

//clase que compara el tiempo de reloj de los eventos para ordenarlos 
//Es usada por el priority queue para ordenar los eventos 
 
public class Comparador implements Comparator<Evento> { 
 
    public int compare(Evento a,Evento b) { 
        int result = new Double(a.getTiempo()).compareTo(b.getTiempo()); 
        return result; 
    } 
}
