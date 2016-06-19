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
    private List<InterfazEstadisticas> cambiosEstadisticas = new ArrayList<>(); //lista que guardará todos los cambios de interfaz, a medida que se vayan generando para luego ejecutarlos
    CambioInterfaz actual; //variable auxiliar para guardar el cambio de interfaz sucediendo en un momento dado
    Timer displayTimer; //timer que hace la funcion del delay en la corrida lenta
    boolean simulando = false; //flag para saber cuando el sistema esta corriendo y no correrlo dos veces simultaneas
    
    int tamPromCHilot, tamPromCConsultast, tamPromCTranst,tamPromCEjecuciont;
    double PromVidat, tiempoSelectt, tiempoJoint, tiempoUpdatet,tiempoDDLt;
    
    boolean pausado = false;
    
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
          limpiarLabels(); //limpia los labes antes de empezar a correr.
          lento = lentoInput.isSelected(); //ve si el sistema esta simulando en modo lento
          s = new Simulacion();
          s.iniciarSimulación(nc, tm, k, n, p, m, t, this);
          
          if(lento){ //si esta en modo lento genera el hilo de interfaz y el tiempo de espera.
            displayTimer = new Timer(100, listener);
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
    
    private void playInterfaz(){
        displayTimer.start();
        pausado = false;
        EmprezarBtn.setBackground(Color.red);
        EmprezarBtn.setText("corriendo");
    }
    
    private void pauseInterfaz(){
        displayTimer.stop();
        pausado = true;
        EmprezarBtn.setBackground(Color.green);
        EmprezarBtn.setText("seguir");
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
                if(actual.getVal() == -3){
                    InterfazEstadisticas estadistica = cambiosEstadisticas.remove(0);
                    EventoEstadisticas(estadistica);
                    limpiarLabels();
                }
                else{
                relojLabel.setText(Double.toString(actual.cargarLabel()));
                }
            }
        }
    };
    
    /**
     * Método encargado de limpiar la pantalla antes de empezar la simulación.
     */
    private void limpiarLabels(){
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
        setSize(1200,825); //TAMAÑO DE LA PANTALLA
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
     * Método encargado de poner el boton de simulación activo y la flag de simulando en falso para poder volver a utilizar el programa.
     */
    public void terminarSimulacion(){
        displayTimer.stop();
        ImprimirEstadisticasTotales();
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
    
    public void estadisticasCorrida(int numCorrida,int tamPromCHilo,int tamPromCConsultas,int tamPromCTrans,int tamPromCEjecucion,double PromVida, double tiempoSelect,double tiempoJoin,double tiempoUpdate,double tiempoDDL, double reloj){
        int id = -3;
        retrasar(null,null,null,id,reloj);
        InterfazEstadisticas e =  new InterfazEstadisticas(numCorrida,tamPromCHilo,tamPromCConsultas,tamPromCTrans,tamPromCEjecucion,PromVida,tiempoSelect,tiempoJoin,tiempoUpdate,tiempoDDL,reloj);
        cambiosEstadisticas.add(e);
    }
    
    public void EventoEstadisticas(InterfazEstadisticas e){
        int[] ints = e.returnInts();
        double[] doubles = e.returnDoubles();
        
        nc.setText(Integer.toString(ints[0]));
        ch.setText(Integer.toString(ints[1]));
        cc.setText(Integer.toString(ints[2]));
        ct.setText(Integer.toString(ints[3]));
        ce.setText(Integer.toString(ints[4]));
        pv.setText(Double.toString(doubles[0]));
        ts.setText(Double.toString(doubles[1]));
        tj.setText(Double.toString(doubles[2]));
        tu.setText(Double.toString(doubles[3]));
        td.setText(Double.toString(doubles[4]));
        //relojLabel.setText(Double.toString(doubles[5]));
        pauseInterfaz();
    }
    
        public void estadisticasTotales(int tamPromCHilo,int tamPromCConsultas,int tamPromCTrans,int tamPromCEjecucion,double PromVida, double tiempoSelect,double tiempoJoin,double tiempoUpdate,double tiempoDDL){

        this.tamPromCHilot = tamPromCHilo;
        this.tamPromCConsultast = tamPromCConsultas;
        this.tamPromCTranst = tamPromCTrans;
        this.tamPromCEjecuciont = tamPromCEjecucion;
        this.PromVidat = PromVida;
        this.tiempoSelectt = tiempoSelect;
        this.tiempoJoint = tiempoJoin;
        this.tiempoUpdatet = tiempoUpdate;
        this.tiempoDDLt = tiempoDDL; 
        
    }
        
        public void ImprimirEstadisticasTotales(){
            
        cth.setText(Integer.toString(tamPromCHilot));
        ctc.setText(Integer.toString(tamPromCConsultast));
        ctt.setText(Integer.toString(tamPromCTranst));
        cte.setText(Integer.toString(tamPromCEjecuciont));
        tpv.setText(Double.toString(PromVidat));
        tts.setText(Double.toString(tiempoSelectt));
        ttj.setText(Double.toString(tiempoJoint));
        ttu.setText(Double.toString(tiempoUpdatet));
        ttd.setText(Double.toString(tiempoDDLt));
            
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
            ImprimirEstadisticasTotales();
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
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        ch = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        cc = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        ct = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        ce = new javax.swing.JTextField();
        pv = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        ts = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        tj = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        tu = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        td = new javax.swing.JTextField();
        scrollPane1 = new java.awt.ScrollPane();
        nc = new java.awt.Label();
        jPanel25 = new javax.swing.JPanel();
        jLabel277 = new javax.swing.JLabel();
        jLabel278 = new javax.swing.JLabel();
        jLabel279 = new javax.swing.JLabel();
        cth = new javax.swing.JTextField();
        jLabel280 = new javax.swing.JLabel();
        ctc = new javax.swing.JTextField();
        jLabel281 = new javax.swing.JLabel();
        ctt = new javax.swing.JTextField();
        jLabel282 = new javax.swing.JLabel();
        cte = new javax.swing.JTextField();
        tpv = new javax.swing.JTextField();
        jLabel283 = new javax.swing.JLabel();
        jLabel284 = new javax.swing.JLabel();
        jLabel285 = new javax.swing.JLabel();
        tts = new javax.swing.JTextField();
        jLabel286 = new javax.swing.JLabel();
        ttj = new javax.swing.JTextField();
        jLabel287 = new javax.swing.JLabel();
        ttu = new javax.swing.JTextField();
        jLabel288 = new javax.swing.JLabel();
        ttd = new javax.swing.JTextField();
        scrollPane23 = new java.awt.ScrollPane();

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

        ncInput.setText("3");
        ncInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ncInputActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel2.setText("Numero de corridas");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel3.setText("Tiempo Max Corrida");

        tmInput.setText("5");
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

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel13.setText("Estadisticas de corrida:");

        jLabel14.setText("Tamaño promedio en colas:");

        jLabel15.setText("Cola de hilo de conexion:");

        ch.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ch.setText("0");

        jLabel16.setText("Cola de consultas:");

        cc.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cc.setText("0");

        jLabel17.setText("Cola de transacciones:");

        ct.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ct.setText("0");

        jLabel18.setText("Cola de ejecución:");

        ce.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ce.setText("0");

        pv.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pv.setText("0");

        jLabel20.setText("Tiempo promedio pasado por tipo de modulo:");

        jLabel21.setText("Promedio de vida de conexión:");

        jLabel22.setText("SELECT");

        ts.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ts.setText("0");

        jLabel23.setText("JOIN");

        tj.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tj.setText("0");

        jLabel24.setText("UPDATE");

        tu.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tu.setText("0");

        jLabel25.setText("DDL");

        td.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        td.setText("0");

        nc.setAlignment(java.awt.Label.RIGHT);
        nc.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ch))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cc))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ct))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ce))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(pv, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ts))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tj))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tu))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(td))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel20))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel13)
                        .addGap(19, 19, 19)
                        .addComponent(nc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nc, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(ch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(cc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(ct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(ce, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(ts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(tj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(tu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(td, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(scrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel25.setBackground(new java.awt.Color(204, 204, 204));

        jLabel277.setText("Estadisticas totales:");

        jLabel278.setText("Tamaño promedio en colas:");

        jLabel279.setText("Cola de hilo de conexion:");

        cth.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cth.setText("0");

        jLabel280.setText("Cola de consultas:");

        ctc.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ctc.setText("0");

        jLabel281.setText("Cola de transacciones:");

        ctt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ctt.setText("0");

        jLabel282.setText("Cola de ejecución:");

        cte.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cte.setText("0");

        tpv.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tpv.setText("0");

        jLabel283.setText("Tiempo promedio pasado por tipo de modulo:");

        jLabel284.setText("Promedio de vida de conexión:");

        jLabel285.setText("SELECT");

        tts.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tts.setText("0");

        jLabel286.setText("JOIN");

        ttj.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ttj.setText("0");

        jLabel287.setText("UPDATE");

        ttu.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ttu.setText("0");

        jLabel288.setText("DDL");

        ttd.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ttd.setText("0");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrollPane23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel279, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cth))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel280, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ctc))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel281, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ctt))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel282, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cte))
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(tpv, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel285, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tts))
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel286, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ttj))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel287, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ttu))
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(jLabel288, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ttd))
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel278)
                                    .addComponent(jLabel283))
                                .addGap(0, 43, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel277)
                        .addGap(87, 87, 87))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel284)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel277, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel278)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel279)
                    .addComponent(cth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel280)
                    .addComponent(ctc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel281)
                    .addComponent(ctt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel282)
                    .addComponent(cte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel284)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tpv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel283)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel285)
                    .addComponent(tts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel286)
                    .addComponent(ttj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel287)
                    .addComponent(ttu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel288)
                    .addComponent(ttd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(scrollPane23, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
                        .addContainerGap()
                        .addComponent(Imagenes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(borradasTimeoutLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(relojLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Imagenes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(relojLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(borradasTimeoutLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
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
       
       else{
           if(pausado){
               playInterfaz();
           }
       
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
    private javax.swing.JTextField cc;
    private javax.swing.JTextField ce;
    private javax.swing.JTextField ch;
    private javax.swing.JLabel cierreLabel;
    private javax.swing.JLabel colaEjecutorLabel;
    private javax.swing.JLabel colaHiloLabel;
    private javax.swing.JLabel colaProcesadorLabel;
    private javax.swing.JLabel colaTransaccionesLabel;
    private javax.swing.JTextField ct;
    private javax.swing.JTextField ctc;
    private javax.swing.JTextField cte;
    private javax.swing.JTextField cth;
    private javax.swing.JTextField ctt;
    private javax.swing.JLabel hiloLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel277;
    private javax.swing.JLabel jLabel278;
    private javax.swing.JLabel jLabel279;
    private javax.swing.JLabel jLabel280;
    private javax.swing.JLabel jLabel281;
    private javax.swing.JLabel jLabel282;
    private javax.swing.JLabel jLabel283;
    private javax.swing.JLabel jLabel284;
    private javax.swing.JLabel jLabel285;
    private javax.swing.JLabel jLabel286;
    private javax.swing.JLabel jLabel287;
    private javax.swing.JLabel jLabel288;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField kInput;
    private javax.swing.JLabel kLabel;
    private javax.swing.JCheckBox lentoInput;
    private javax.swing.JTextField mInput;
    private javax.swing.JLabel mLabel;
    private javax.swing.JTextField nInput;
    private javax.swing.JLabel nLabel;
    private java.awt.Label nc;
    private javax.swing.JTextField ncInput;
    private javax.swing.JTextField pInput;
    private javax.swing.JLabel pLabel;
    private javax.swing.JTextField pv;
    private javax.swing.JLabel rechazadasLabel;
    private javax.swing.JLabel relojLabel;
    private java.awt.ScrollPane scrollPane1;
    private java.awt.ScrollPane scrollPane23;
    private javax.swing.JTextField tInput;
    private javax.swing.JTextField td;
    private javax.swing.JTextField tj;
    private javax.swing.JTextField tmInput;
    private javax.swing.JTextField tpv;
    private javax.swing.JTextField ts;
    private javax.swing.JTextField ttd;
    private javax.swing.JTextField ttj;
    private javax.swing.JTextField tts;
    private javax.swing.JTextField ttu;
    private javax.swing.JTextField tu;
    // End of variables declaration//GEN-END:variables
}
