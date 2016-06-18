package io;

import javax.swing.JLabel;

/**
 * Clase del objeto CambioInterfaz, el cual se utiliza para administrar los cambios gráficos que se la irán haciendo a la interfaz paso por paso en modo lento.
 */
public class CambioInterfaz {
    
    private JLabel label = null;
    private PanelFondo flecha = null;
    private PanelFondo panel = null;
    private int val = -1;
    private double valD = -0.1;
    private String valS = "";
    private double reloj = 0;
    
    /**
     * Constructor de la clase utilizado en el caso que el valor de entrada sea un integer, se utiñiza más que todo para los paneles con valores numerales como el panel de entrada de clientes, rechazados, entre otros.
     * @param label variable auxiliar donde se mete la label que será modificada cuando se llame a este objeto
     * @param panel variable auxiliar que representa el panel de la interfaz que se iluminará o se apagará cuando se llame a este objeto
     * @param flecha variable auxiliar que representa la imagen de flecha de la interfaz que se iluminará o se apagará cuando se llame a este objeto
     * @param val variable auxiliar integer que guarda número que aparecerá en la label especifica de este objeto.
     * @param reloj variable double que gurdará el valor de reloj en el momento especifio que se llame a este objeto
     */
    public CambioInterfaz(JLabel label,PanelFondo panel, PanelFondo flecha, int val, double reloj){
        this.label = label; 
        this.panel = panel;
        this.flecha = flecha;
        this.val = val;
        this.reloj = reloj;
    }
    
    /**
     * Constructor de la clase utilizado en el caso que el valor de entrada sea un double
     * @param label variable auxiliar donde se mete la label que será modificada cuando se llame a este objeto
     * @param panel variable auxiliar que representa el panel de la interfaz que se iluminará o se apagará cuando se llame a este objeto
     * @param flecha variable auxiliar que representa la imagen de flecha de la interfaz que se iluminará o se apagará cuando se llame a este objeto
     * @param vald variable auxiliar double que guarda número que aparecerá en la label especifica de este objeto.
     * @param reloj variable double que gurdará el valor de reloj en el momento especifio que se llame a este objeto
     */
    public CambioInterfaz(JLabel label,PanelFondo panel, PanelFondo flecha, double vald, double reloj){
        this.label = label;
        this.panel = panel;
        this.flecha = flecha;
        this.valD = vald;
        this.reloj = reloj;
    }
    
    /**
     * Constructor de la clase utilizado en el caso que el valor de entrada sea un string, primordialmente utilizado para el panel de creación de hilo
     * @param label variable auxiliar donde se mete la label que será modificada cuando se llame a este objeto
     * @param panel variable auxiliar que representa el panel de la interfaz que se iluminará o se apagará cuando se llame a este objeto
     * @param flecha variable auxiliar que representa la imagen de flecha de la interfaz que se iluminará o se apagará cuando se llame a este objeto
     * @param vals variable auxiliar string que guarda los caracteres que serán puestos en la label al llamarse al objeto.
     * @param reloj variable double que gurdará el valor de reloj en el momento especifio que se llame a este objeto
     */
    public CambioInterfaz(JLabel label,PanelFondo panel, PanelFondo flecha, String vals, double reloj){
        this.label = label;
        this.panel = panel;
        this.flecha = flecha;
        this.valS = vals;
        this.reloj = reloj;
    }

    /**
     * Metodo encargado de transformar el valor int o double de entrada en un string para poder representarse en el label. 
     * @return devuelve el valor de entrada casteado en un string 
     */
    public String getValor(){
        if(val < 0){ //si el integer val tiene por valor -1 significa que no fue sobreescrito ya que el valor -1 es inalcansable, por lo cual significa que el valor modificado fue el double
            return Double.toString(valD); 
        }
        return Integer.toString(val);
    }
    
    /**
     * Metodo encargado de devolver el Jlabel especifico de este objeto
     * @return JLabel perteneciente al objeto especifico.
     */
    public JLabel getLabel(){
        return label;
    }
    
    /**
     * Metodo encargado de cargar las acciones de interfaz especificas ha este momento de reloj.
     * @return devuelve el tiempo de reloj donde se generó la acción
     */
    public double cargarLabel(){ 
        
        if(label != null) label.setVisible(true); 
        
            if(flecha != null) flecha.setVisible(true);

                if(val > -1){ //si el integer val tiene por valor -1 significa que no fue sobreescrito ya que el valor -1 es inalcansable, por lo cual significa que el valor modificado fue el double o el string
                    label.setText(Integer.toString(val));
                    if(val > 0){ //si el valor es mayor a 0 significa que contiene objetos por lo cual se enciende el panel, sino representa por ejemplo que no hay objetos en cola y se apaga el panel.
                        if(panel != null) panel.setVisible(true);
                    }
                    else{
                        if(panel != null) panel.setVisible(false);
                    }
                }
                else{
                    if(valS.equals("")){ //si el valor del string no fue modificado entonces entró un double
                        label.setText(Double.toString(valD));
                    }
                    else{
                        if(valS.equals("ocupado")){ //si la entrada es un ocupado significa que el panel esta en uso
                            panel.setVisible(true);

                        }
                        else{
                            panel.setVisible(false); 
                        }
                        label.setText(valS);
                    }

                }
        
        return reloj;
    }
    
}
