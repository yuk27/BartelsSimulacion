/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import javax.swing.JLabel;

/**
 *
 * @author jsolano
 */
public class CambioInterfaz {
    
    JLabel label = null;
    BackgroundPanel flecha = null;
    BackgroundPanel panel = null;
    int val = -1;
    double valD = -0.1;
    String valS = "";
    double reloj = 0;
    
    
    public CambioInterfaz(JLabel label,BackgroundPanel panel, BackgroundPanel flecha, int val, double reloj){
        this.label = label;
        this.panel = panel;
        this.flecha = flecha;
        if(label != null) this.label.setVisible(false);
        if(panel != null) this.panel.setVisible(false);
        if(flecha != null) this.flecha.setVisible(false);
        this.val = val;
        this.reloj = reloj;
    }
    
    public CambioInterfaz(JLabel label,BackgroundPanel panel, BackgroundPanel flecha, double vald, double reloj){
        this.label = label;
        this.panel = panel;
        this.flecha = flecha;
        if(label != null) this.label.setVisible(false);
        if(panel != null) this.panel.setVisible(false);
        if(flecha != null)this.flecha.setVisible(false);
        this.valD = vald;
        this.reloj = reloj;
    }
    
    public CambioInterfaz(JLabel label,BackgroundPanel panel, BackgroundPanel flecha, String vals, double reloj){
        this.label = label;
        this.panel = panel;
        this.flecha = flecha;
        if(label != null) this.label.setVisible(false);
        if(panel != null) this.panel.setVisible(false);
        if(flecha != null)this.flecha.setVisible(false);
        this.valS = vals;
        this.reloj = reloj;
    }


    
    public String getValue(){
        if(val < 0){
        return Double.toString(valD);
        }
        return Integer.toString(val);
    }
    
    public JLabel getLabel(){
        return label;
    }
    
    public double cargarLabel(){
        if(label != null) label.setVisible(true);
        
            if(flecha != null) flecha.setVisible(true);

                if(val > -2){

                    if(val > -1){
                        label.setText(Integer.toString(val));
                        if(val > 0){
                            if(panel != null) panel.setVisible(true);
                        }
                        else{
                            if(panel != null) panel.setVisible(false);
                        }
                    }
                    else{
                        if(valS.equals("")){    
                            label.setText(Double.toString(valD));
                        }
                        else{
                            if(valS.equals("ocupado")){
                                panel.setVisible(true);
                                
                            }
                            else{
                                panel.setVisible(false); 
                            }
                            label.setText(valS);
                        }
                        
                    }
            }
        
        return reloj;
    }
    
}
