package io;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Clase encargada de administrar el menu visual utilizado para la comunicación del usuario con el sistema.
 */
public class Menu extends javax.swing.JFrame {
    boolean lento = true; //variable flag para saber si la simulación debe ir rapido o lento
    Simulacion s;
    PanelFondo[] paneles; 
    PanelFondo[] flechas; 
    private List<CambioInterfaz> cambiosInterfaz = new ArrayList<>(); //lista que guardará todos los cambios de interfaz, a medida que se vayan generando para luego ejecutarlos
    CambioInterfaz actual; //variable auxiliar para guardar el cambio de interfaz sucediendo en un momento dado
    Timer displayTimer; //timer que hace la funcion del delay en la corrida lenta
    boolean simulando = false; //flag para saber cuando el sistema esta corriendo y no correrlo dos veces simultaneas
    
    /**
     * Constructor inicia los componentes.
     */
    public Menu() {
        initComponents(); 
    }

    /**
     * Método encargado de cargar las entradas de los inputs a variables, para su uso en la simulación del sistema.
     */
    public void getEntradas(){ //toma cada input de entrada, revisa que sea un integer mayor a -1, si cumple se simula el simula
        
        int nc,k,n,p,m; //variables para número de corridas, y tanaños de servidores k,,n,p,m.
        double tm,t; // variables para tiempo maximo de corrida y timeout.
        
        try {
            nc = Integer.parseInt(ncInput.getText());
        } catch (NumberFormatException nfe) {
            nc = -1; 
        }
        try {
            
            tm = Double.parseDouble(tmInput.getText());
        } catch (NumberFormatException nfe) {
            tm = -1; 
        }
        try {
            k = Integer.parseInt(kInput.getText());
        } catch (NumberFormatException nfe) {
            k = -1; 
        } 
        try {
            n = Integer.parseInt(nInput.getText());
        } catch (NumberFormatException nfe) {
            n = -1; 
        }
        try {
            p = Integer.parseInt(pInput.getText());
        } catch (NumberFormatException nfe) {
            p = -1; 
        }
        try {
            m = Integer.parseInt(mInput.getText());
        } catch (NumberFormatException nfe) {
            m = -1; 
        }
        try {
            t = Double.parseDouble(tInput.getText());
        } catch (NumberFormatException nfe) {
            t = -1; 
        }

        if(nc > 0 && tm > 0 && k > 0 && n > 0 && p> 0 && m > 0 && t > 0){ //si se encotraron valores correctos
          System.out.println("correcto");
          simulando = true; 
          LimpiarLabels(); //limpia los labes antes de empezar a correr.
          lento = lentoInput.isSelected(); //ve si el sistema esta simulando en modo lento
          s = new Simulacion();
          s.iniciarSimulación(nc, tm, k, n, p, m, t, this);
          
          if(lento){ //si esta en modo lento genera el hilo de interfaz y el tiempo de espera.
            displayTimer = new Timer(1, listener);
            displayTimer.start(); 
          }
        }
        else{ //si entraron datos erroneos se vuelve a poner la interfaz lista para ser reutilizada.
          System.out.println("incorrecto");
          EmprezarBtn.setText("Simular");
          EmprezarBtn.setBackground(Color.lightGray);
          simulando = false;
        }

    }
    
    /**
     * Método encargado de limpiar la pantalla antes de empezar la simulación.
     */
    private void LimpiarLabels(){
        nLabel.setText("0");
        pLabel.setText("0");
        mLabel.setText("0");
        kLabel.setText("0");
        rechazadasLabel.setText("0");
        colaHiloLabel.setText("0");
        colaProcesadorLabel.setText("0");
        colaTransaccionesLabel.setText("0");
        cierreLabel.setText("0");
        borradasTimeoutLabel.setText("0");
    }
    
    /**
     * Método encargado de cargar las imagenes del sistema en arrays de tanto paneles como flechas
     */
    public void generarImagenes(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        pack();
        setSize(800,825);
        paneles = new PanelFondo[11];
        flechas = new PanelFondo[14];

        flechas[0] = new PanelFondo("flechaBasura.png");
        flechas[1] = new PanelFondo("flechaHilo.png");
        flechas[2] = new PanelFondo("InColaHilo.png");
        flechas[3] = new PanelFondo("OutColaHilo.png");
        flechas[4] = new PanelFondo("flechaProcesador.png");
        flechas[5] = new PanelFondo("InColaProcesador.png");
        flechas[6] = new PanelFondo("OutColaProcesador.png");
        flechas[7] = new PanelFondo("flechaTransacción.png");
        flechas[8] = new PanelFondo("InColaTransacción.png");
        flechas[9] = new PanelFondo("OutColaTransacción.png");
        flechas[10] = new PanelFondo("flechaEjecutor.png");
        flechas[11] = new PanelFondo("InColaEjecutor.png");
        flechas[12] = new PanelFondo("OutColaEjecutor.png");
        flechas[13] = new PanelFondo("flechaSalida.png");

        paneles[0] = new PanelFondo("clientes.png");
        paneles[1] = new PanelFondo("Hilo.png");
        paneles[2] = new PanelFondo("colaHilo.png");
        paneles[3] = new PanelFondo("Procesador.png");
        paneles[4] = new PanelFondo("colaServidor.png");
        paneles[5] = new PanelFondo("transacciónes.png");
        paneles[6] = new PanelFondo("colaTransacción.png");
        paneles[7] = new PanelFondo("Ejecutor.png");
        paneles[8] = new PanelFondo("colaEjecutor.png");
        paneles[9] = new PanelFondo("salida.png");
        paneles[10] = new PanelFondo("fondo.png");
        Imagenes.setLayout(new BorderLayout());

        for (PanelFondo flecha : flechas) {//limpia las flechas al inicializarlas
            flecha.setOpaque(false);
            flecha.setLocation(0, 0);
            flecha.setVisible(false);
            Imagenes.add(flecha);
        }

        int tam = paneles.length-1;
        for(int i = 0; i < tam; i++){   
           Imagenes.add(paneles[i]);
           paneles[i].setOpaque(false);
           paneles[i].setLocation(0,0);
           paneles[i].setVisible(false);
           }

        Imagenes.add(paneles[tam]);
        paneles[tam].setOpaque(false);
        paneles[tam].setLocation(0,0);

    }
    
    /**
     * ActionListener encargado de llevar control de los cambiosInterfaz siendo hechos, toma el primero de la lista y le hace el llamado necesario.
     */
    ActionListener listener = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            if(cambiosInterfaz.isEmpty()){
                System.out.println("no hay nada para cargar");
                terminarSimulacion();
            }
            else{
            limpiarFlechas();
                actual = cambiosInterfaz.remove(0);
                relojLabel.setText(Double.toString(actual.cargarLabel()));
            }
        }
    };
    
    /**
     * Método encargado de poner el boton de simulación activo y la flag de simulando en falso para poder volver a utilizar el programa.
     */
    public void terminarSimulacion(){
        displayTimer.stop();
        EmprezarBtn.setText("Simular");
        EmprezarBtn.setBackground(Color.lightGray);
        simulando = false;
    }
    
    /**
     * Método utilizado para saber si la simulación esta corriendo en modo lento
     * @return devuelve un booleano true si corre en modo lento, falso si no 
     */
    public boolean isLento(){
        return lento;
    }
    
    //método encargado de añadir a la cola de cambiosInterfaz el proximo cambio, el cual contiene un int por valor de label
    private void retrasar(JLabel label,PanelFondo panel,PanelFondo flecha, int val, double relojP){
        CambioInterfaz c =  new CambioInterfaz(label,panel,flecha,val,relojP);
        cambiosInterfaz.add(c);
    }
    
    //método encargado de añadir a la cola de cambiosInterfaz el proximo cambio, el cual contiene un double por valor de label
    private void retrasar(JLabel label,PanelFondo panel,PanelFondo flecha, double val, double relojP){
        CambioInterfaz c =  new CambioInterfaz(label,panel,flecha,val,relojP);
        cambiosInterfaz.add(c);
    }
    
    //método encargado de añadir a la cola de cambiosInterfaz el proximo cambio, el cual contiene un string por valor de label
    private void retrasar(JLabel label,PanelFondo panel,PanelFondo flecha, String val, double relojP){
        CambioInterfaz c =  new CambioInterfaz(label,panel,flecha,val,relojP);
        cambiosInterfaz.add(c);
    }
    
    /**
     * Método encargado de limpiar las flechas que fueron utilizadas en el ultimo llamada, para ejecutar la siguiente.
     */
    public void limpiarFlechas(){
        for (PanelFondo flecha : flechas) {
            flecha.setVisible(false);
        }
    }
    
    /**
     * Método encargado de añadir a la cola de cambios en interfaz la acción de un nuevo rechazo de red
     * @param val cantidad de conexiones rechazadas hasta el momento
     * @param reloj variable representando el reloj actual.
     */
    public void aplicarInterfazRechazadas(int val, double reloj){
        if(lento){
            retrasar(rechazadasLabel,null,flechas[0],val,reloj);
        }
    }
    
    /**
     * Método encargado de añadir a la cola de cambios en interfaz la acción de una nueva entrada de cliente en red
     * @param val cantidad de conexiones logradas hasta el momento
     * @param reloj variable representando el reloj actual.
     */
    public void aplicarInterfazClientes(int val, double reloj){
        if(lento){
            retrasar(kLabel,paneles[0],flechas[1],val,reloj);
        }
    }
    
    /**
     * Método encargado de añadir a la cola de cambios en interfaz la acción de una nueva entrada al hilo de conexión
     * @param ocupado flag para saber en que estado esta el procesador de hilos en este momento
     * @param reloj variable representando el reloj actual.
     */
    public void aplicarInterfazNuevoHilo(boolean ocupado, double reloj){
        
        
        if(lento){
            if(ocupado){
                retrasar(hiloLabel,paneles[1],null,"ocupado", reloj);
            }
            else{
                retrasar(hiloLabel,paneles[1],null,"limpio", reloj);
            }
        }
    }
    
    /**
     * Método encargado de añadir a la cola de cambios en interfaz la acción de una nueva entrada a la cola de hilos
     * @param val cantidad de conexiones en cola en este momento.
     * @param reloj variable representando el reloj actual.
     */
    public void aplicarInterfazColaHilo(int val, double reloj){
       
        if(lento){
            if(val > Integer.parseInt(colaHiloLabel.getText())){
                retrasar(colaHiloLabel,paneles[2],flechas[2],val,reloj); 
            }
            else{
                retrasar(colaHiloLabel,paneles[2],flechas[3],val,reloj); 
            }
           
        }
    }
       
    /**
     * Método encargado de añadir a la cola de cambios en interfaz la acción de una nueva entrada de procesamiento de consulta
     * @param val cantidad de prcesos en el servidor
     * @param reloj variable representando el reloj actual.
     */
    public void aplicarInterfazProcesarConsulta(int val,double reloj){
        if(lento){
            retrasar(nLabel,paneles[3],flechas[4],val,reloj);
        } 
    }
    
    /**
     * Método encargado de añadir a la cola de cambios en interfaz la acción de una nueva entrada a la cola de procesador
     * @param val cantidad de conexiones en cola en este momento.
     * @param reloj variable representando el reloj actual.
     */
    public void aplicarInterfazColaProcesador(int val, double reloj){
        if(lento){
            if(val > Integer.parseInt(colaHiloLabel.getText())){
                retrasar(colaProcesadorLabel,paneles[4],flechas[5],val,reloj); 
            }
            else{
                retrasar(colaProcesadorLabel,paneles[4],flechas[6],val,reloj); 
            }
           
        }
    }
   
    /**
     * Método encargado de añadir a la cola de cambios en interfaz la acción de una nueva transacción de consulta
     * @param val cantidad de prcesos en el servidor
     * @param reloj variable representando el reloj actual.
     */
    public void aplicarInterfazProcesarTransacciones(int val, double reloj){
        if(lento){
            retrasar(pLabel,paneles[5],flechas[6],val,reloj);
        }
    }

    /**
     * Método encargado de añadir a la cola de cambios en interfaz la acción de una nueva entrada a la cola de transacciónes
     * @param val cantidad de conexiones en cola en este momento.
     * @param reloj variable representando el reloj actual.
     */
    public void aplicarInterfazColaTransacciones(int val, double reloj){
        
        if(lento){
            if(val > Integer.parseInt(colaTransaccionesLabel.getText())){
                retrasar(colaTransaccionesLabel,paneles[2],flechas[7],val,reloj); 
            }
            else{
                retrasar(colaTransaccionesLabel,paneles[2],flechas[8],val,reloj); 
            }
           
        }
    }
    
    /**
     * Método encargado de añadir a la cola de cambios en interfaz la acción de una nueva entrada de ejecución de consulta
     * @param val cantidad de prcesos en el servidor
     * @param reloj variable representando el reloj actual.
     */
    public void aplicarInterfazProcesarEjecutor(int val, double reloj){
        if(lento){
            retrasar(mLabel,paneles[7],flechas[8],val,reloj);
        }
    }
    
    /**
     * Método encargado de añadir a la cola de cambios en interfaz la acción de una nueva entrada a la cola de ejecución
     * @param val cantidad de conexiones en cola en este momento.
     * @param reloj variable representando el reloj actual.
     */
    public void aplicarInterfazColaEjecutor(int val, double reloj){
        
        if(lento){
            if(val > Integer.parseInt(colaEjecutorLabel.getText())){
                retrasar(colaEjecutorLabel,paneles[8],flechas[11],val,reloj); 
            }
            else{
                retrasar(colaEjecutorLabel,paneles[8],flechas[12],val,reloj); 
            }
        }
    }
        
    /**
     * Método encargado de añadir a la cola de cambios en interfaz la acción de un cierre de conexión en la simulación
     * @param val cantidad de conexiones en el servidor en este momento.
     * @param reloj variable representando el reloj actual.
     */
    public void aplicarInterfazProcesarCierreConexion(int val, double reloj){
        if(lento){
            retrasar(cierreLabel,paneles[9],flechas[13],val,reloj);
        }
    }
        
    /**
     * Método encargado de añadir a la cola de cambios en interfaz la acción de añadir una al contador de conexiones elminadas por timeout
     * @param val cantidad de conexiones elminadas por tiemout hasta el momento
     * @param reloj variable representando el reloj actual.
     */
    public void aplicarInterfazBorradasTimeOut(int val,double reloj){
            if(lento){
                retrasar(borradasTimeoutLabel,null,null,val,reloj);
            }
        }
        
    /**
     * Método encargado de modificar toda la interfaz de un solo en el caso de haber sido ejecutada la simulación de modo rapido.
     * @param k variable de número de conexiones concurrentes al final de la simulacion
     * @param rechazadas variable de número de conexiones rechazadas al final de la simulacion
     * @param hilo variable tipo flag de si el hilo de procesamiento está ocupado  al final de la simulacion
     * @param colaHilo variable de número de conexiones en cola de hilo al final de la simulacion
     * @param n variable de número de consultas al final de la simulacion
     * @param colaProcesador variable de número de conexiones en cola de procesador al final de la simulacion
     * @param p variable de número de transacciónes al final de la simulacion
     * @param colaTransacciónes variable de número de conexiones en cola de transacciónes al final de la simulacion
     * @param m variable de número de procesos en servidor al final de la simulacion
     * @param colaEjecutor variable de número de conexiones en cola de ejecución al final de la simulacion
     * @param cierre variable de número de conexiones terminadas al final de la simulacion
     * @param reloj variable de tiempo de reloj en que la simulación terminó
     * @param borradas variable de número de conexiones borradas por tiemout
     */
    public void ModoRapido(int k, int rechazadas, boolean hilo, int colaHilo, int n, int colaProcesador, int p, int colaTransacciónes, int m, int colaEjecutor, int cierre, double reloj, int borradas){
        
            kLabel.setText(Integer.toString(k));
            rechazadasLabel.setText(Integer.toString(rechazadas));
            if(hilo){
                hiloLabel.setText("limpio");
            }
            else{
                hiloLabel.setText("ocupado");
            }
            colaHiloLabel.setText(Integer.toString(colaHilo));
            nLabel.setText(Integer.toString(n));
            colaProcesadorLabel.setText(Integer.toString(colaProcesador));
            colaTransaccionesLabel.setText(Integer.toString(colaTransacciónes));
            mLabel.setText(Integer.toString(m));
            colaEjecutorLabel.setText(Integer.toString(colaEjecutor));
            cierreLabel.setText(Integer.toString(cierre));
            relojLabel.setText(Double.toString(reloj));
            borradasTimeoutLabel.setText(Integer.toString(borradas));
           
            EmprezarBtn.setText("Simular");
            EmprezarBtn.setBackground(Color.lightGray);
            simulando = false;
        }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        kInput = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        nInput = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        pInput = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        mInput = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        lentoInput = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        ncInput = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tmInput = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        tInput = new javax.swing.JTextField();
        EmprezarBtn = new javax.swing.JButton();
        Imagenes = new javax.swing.JPanel();
        kLabel = new javax.swing.JLabel();
        hiloLabel = new javax.swing.JLabel();
        nLabel = new javax.swing.JLabel();
        mLabel = new javax.swing.JLabel();
        cierreLabel = new javax.swing.JLabel();
        rechazadasLabel = new javax.swing.JLabel();
        pLabel = new javax.swing.JLabel();
        colaHiloLabel = new javax.swing.JLabel();
        colaProcesadorLabel = new javax.swing.JLabel();
        colaEjecutorLabel = new javax.swing.JLabel();
        colaTransaccionesLabel = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        relojLabel = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        borradasTimeoutLabel = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel5.setText("<html>Numero de conexiones<br>    concurrentes(k)</html>");

        kInput.setText("10");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel7.setText("Procesos disponibles para el procesamiento de:");

        nInput.setText("10");
        nInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nInputActionPerformed(evt);
            }
        });

        jLabel4.setText("Consultas (n)");

        jLabel8.setText("Transacciones (p)");

        pInput.setText("10");

        jLabel9.setText("Procesos (m)");

        mInput.setText("10");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(kInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(nInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(pInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLabel9))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(jLabel7)))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(mInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("SIMULACIÓN");

        lentoInput.setSelected(true);
        lentoInput.setText("modo lento");
        lentoInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lentoInputActionPerformed(evt);
            }
        });

        ncInput.setText("2");
        ncInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ncInputActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel2.setText("Numero de corridas");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel3.setText("Tiempo Max Corrida");

        tmInput.setText("60");
        tmInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tmInputActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel10.setText("Timeout(t)");

        tInput.setText("1000");
        tInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tInputActionPerformed(evt);
            }
        });

        EmprezarBtn.setText("Empezar");
        EmprezarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmprezarBtnActionPerformed(evt);
            }
        });

        kLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        kLabel.setText("0");
        kLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        hiloLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        hiloLabel.setText("Libre");
        hiloLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        nLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nLabel.setText("0");
        nLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        mLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        mLabel.setText("0");
        mLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        cierreLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cierreLabel.setText("0");
        cierreLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        rechazadasLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        rechazadasLabel.setText("0");
        rechazadasLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        pLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pLabel.setText("0");
        pLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        colaHiloLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        colaHiloLabel.setText("0");
        colaHiloLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        colaProcesadorLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        colaProcesadorLabel.setText("0");
        colaProcesadorLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        colaEjecutorLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        colaEjecutorLabel.setText("0");
        colaEjecutorLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        colaTransaccionesLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        colaTransaccionesLabel.setText("0");
        colaTransaccionesLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout ImagenesLayout = new javax.swing.GroupLayout(Imagenes);
        Imagenes.setLayout(ImagenesLayout);
        ImagenesLayout.setHorizontalGroup(
            ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ImagenesLayout.createSequentialGroup()
                .addGroup(ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ImagenesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(kLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(130, 130, 130))
                    .addGroup(ImagenesLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(cierreLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(129, 129, 129)
                        .addGroup(ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hiloLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)))
                .addGroup(ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ImagenesLayout.createSequentialGroup()
                        .addComponent(rechazadasLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ImagenesLayout.createSequentialGroup()
                        .addGroup(ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(colaProcesadorLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(colaEjecutorLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(colaTransaccionesLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
            .addGroup(ImagenesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(colaHiloLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );
        ImagenesLayout.setVerticalGroup(
            ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ImagenesLayout.createSequentialGroup()
                .addGroup(ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ImagenesLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(rechazadasLabel))
                    .addGroup(ImagenesLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(kLabel)))
                .addGap(134, 134, 134)
                .addGroup(ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hiloLabel)
                    .addComponent(colaHiloLabel))
                .addGroup(ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ImagenesLayout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(nLabel)
                        .addGap(47, 47, 47))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ImagenesLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(colaProcesadorLabel)
                            .addComponent(cierreLabel))
                        .addGap(55, 55, 55)))
                .addGroup(ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mLabel)
                    .addGroup(ImagenesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(colaEjecutorLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                .addGroup(ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pLabel)
                    .addComponent(colaTransaccionesLabel)))
        );

        jLabel11.setText("Tiempo de reloj:");

        relojLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        relojLabel.setText("0");

        jLabel12.setText("Conexiones borradas por timeout:");

        borradasTimeoutLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        borradasTimeoutLabel.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(ncInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(tmInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(32, 32, 32)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lentoInput)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(10, 10, 10)
                                    .addComponent(tInput, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(333, 333, 333)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(116, 116, 116)
                        .addComponent(Imagenes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12)
                                .addGap(0, 66, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(borradasTimeoutLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(relojLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(54, 54, 54)))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(333, 333, 333)
                .addComponent(EmprezarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ncInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tmInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lentoInput)
                            .addComponent(jLabel6))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(EmprezarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addComponent(Imagenes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(relojLabel))
                        .addGap(88, 88, 88)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(borradasTimeoutLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lentoInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lentoInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lentoInputActionPerformed

    private void tInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tInputActionPerformed

    private void EmprezarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmprezarBtnActionPerformed
       if(!simulando){
        EmprezarBtn.setBackground(Color.red);
        EmprezarBtn.setText("corriendo");
        getEntradas(); //se cargan los datos y se empieza la simulacion  
       } 
    }//GEN-LAST:event_EmprezarBtnActionPerformed

    private void ncInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ncInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ncInputActionPerformed

    private void nInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nInputActionPerformed

    private void tmInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tmInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tmInputActionPerformed

    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
              
                Menu menu = new Menu();
                menu.generarImagenes();
                menu.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton EmprezarBtn;
    private javax.swing.JPanel Imagenes;
    private javax.swing.JLabel borradasTimeoutLabel;
    private javax.swing.JLabel cierreLabel;
    private javax.swing.JLabel colaEjecutorLabel;
    private javax.swing.JLabel colaHiloLabel;
    private javax.swing.JLabel colaProcesadorLabel;
    private javax.swing.JLabel colaTransaccionesLabel;
    private javax.swing.JLabel hiloLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField kInput;
    private javax.swing.JLabel kLabel;
    private javax.swing.JCheckBox lentoInput;
    private javax.swing.JTextField mInput;
    private javax.swing.JLabel mLabel;
    private javax.swing.JTextField nInput;
    private javax.swing.JLabel nLabel;
    private javax.swing.JTextField ncInput;
    private javax.swing.JTextField pInput;
    private javax.swing.JLabel pLabel;
    private javax.swing.JLabel rechazadasLabel;
    private javax.swing.JLabel relojLabel;
    private javax.swing.JTextField tInput;
    private javax.swing.JTextField tmInput;
    // End of variables declaration//GEN-END:variables
}
