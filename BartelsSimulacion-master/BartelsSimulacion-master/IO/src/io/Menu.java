package io;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JLabel;
//import javax.swing.SwingUtilities;

public class Menu extends javax.swing.JFrame {
    boolean lento = true; 
    Simulacion s;
    BackgroundPanel[] paneles; 
    BackgroundPanel[] flechas; 

    public Menu() {
        initComponents(); 
    }
    
    public void getEntradas(){ //toma cada input de entrada, revisa que sea un integer mayor a -1, si cumple se simula el simula
        int nc,k,n,p,m;
        double tm,t;

        try {
            nc = Integer.parseInt(ncInput.getText());
        } catch (NumberFormatException nfe) {
            nc = -1; // or null if that is your preference
        }
        try {
            
            tm = Double.parseDouble(tmInput.getText());
        } catch (NumberFormatException nfe) {
            tm = -1; // or null if that is your preference
        }
        try {
            k = Integer.parseInt(kInput.getText());
        } catch (NumberFormatException nfe) {
            k = -1; // or null if that is your preference
        } 
        try {
            n = Integer.parseInt(nInput.getText());
        } catch (NumberFormatException nfe) {
            n = -1; // or null if that is your preference
        }
        try {
            p = Integer.parseInt(pInput.getText());
        } catch (NumberFormatException nfe) {
            p = -1; // or null if that is your preference
        }
        try {
            m = Integer.parseInt(mInput.getText());
        } catch (NumberFormatException nfe) {
            m = -1; // or null if that is your preference
        }
        try {
            t = Double.parseDouble(tInput.getText());
        } catch (NumberFormatException nfe) {
            t = -1; // or null if that is your preference
        }

        if(nc > 0 && tm > 0 && k > 0 && n > 0 && p> 0 && m > 0 && t > 0){
          System.out.println("correcto");
          lento = lentoInput.isSelected();
          s = new Simulacion();
          s.iniciarSimulación(nc, tm, k, n, p, m, t, this);
        }
        else{
          System.out.println("incorrecto");
        }

    }
    
    public void generarImagenes(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        pack();
        setSize(800,825);
        paneles = new BackgroundPanel[11];
        flechas = new BackgroundPanel[14];

        flechas[0] = new BackgroundPanel("flechaBasura.png");
        flechas[1] = new BackgroundPanel("flechaHilo.png");
        flechas[2] = new BackgroundPanel("InColaHilo.png");
        flechas[3] = new BackgroundPanel("OutColaHilo.png");
        flechas[4] = new BackgroundPanel("flechaProcesador.png");
        flechas[5] = new BackgroundPanel("InColaProcesador.png");
        flechas[6] = new BackgroundPanel("OutColaProcesador.png");
        flechas[7] = new BackgroundPanel("flechaTransaccion.png");
        flechas[8] = new BackgroundPanel("InColaTransaccion.png");
        flechas[9] = new BackgroundPanel("OutColaTransaccion.png");
        flechas[10] = new BackgroundPanel("flechaEjecutor.png");
        flechas[11] = new BackgroundPanel("InColaEjecutor.png");
        flechas[12] = new BackgroundPanel("OutColaEjecutor.png");
        flechas[13] = new BackgroundPanel("flechaSalida.png");

        paneles[0] = new BackgroundPanel("clientes.png");
        paneles[1] = new BackgroundPanel("Hilo.png");
        paneles[2] = new BackgroundPanel("colaHilo.png");
        paneles[3] = new BackgroundPanel("Procesador.png");
        paneles[4] = new BackgroundPanel("colaServidor.png");
        paneles[5] = new BackgroundPanel("transacciones.png");
        paneles[6] = new BackgroundPanel("colaTransaccion.png");
        paneles[7] = new BackgroundPanel("Ejecutor.png");
        paneles[8] = new BackgroundPanel("colaEjecutor.png");
        paneles[9] = new BackgroundPanel("salida.png");
        paneles[10] = new BackgroundPanel("fondo.png");
        Imagenes.setLayout(new BorderLayout());

        for(int j = 0; j < flechas.length; j++){
           Imagenes.add(flechas[j]);
           flechas[j].setOpaque(false);
           flechas[j].setLocation(0,0);
           flechas[j].setVisible(false);
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
    
    void retrasar(){
        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    
    void limpiarFlechas(){
        for (BackgroundPanel flecha : flechas) {
            flecha.setVisible(false);
        }
    }
    
    void aplicarInterfazRechazadas(int val){
        flechas[0].setVisible(true);
        if(lento){
            limpiarFlechas();
            retrasar();
        }
        flechas[0].setVisible(false);
        rechazadasLabel.setText(Integer.toString(val));
    }
    
    void aplicarInterfazClientes(int val){
        if(lento){
            limpiarFlechas();
            retrasar();
        }
        kLabel.setText(Integer.toString(val));
        if(val > 0){
            paneles[0].setVisible(true);
            flechas[1].setVisible(true);
        }
        else{
            paneles[0].setVisible(false);
        }
    }
    
    void hilo(boolean ocupado){
        if(lento){
            limpiarFlechas();
            retrasar();
        }
        if(ocupado){
            nLabel.setText("ocupado");
            paneles[1].setVisible(true);
        }
        else{
            nLabel.setText("limpio");
            paneles[1].setVisible(false);
        }
        
        //flechas[1].setVisible(true);
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
        jLabel15 = new javax.swing.JLabel();
        rechazadasLabel = new javax.swing.JLabel();
        pLabel = new javax.swing.JLabel();
        colaHiloLabel = new javax.swing.JLabel();
        rechazadasLabel2 = new javax.swing.JLabel();
        rechazadasLabel3 = new javax.swing.JLabel();
        rechazadasLabel4 = new javax.swing.JLabel();

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

        ncInput.setText("10");
        ncInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ncInputActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel2.setText("Numero de corridas");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel3.setText("Tiempo Max Corrida");

        tmInput.setText("10");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jLabel10.setText("Timeout(t)");

        tInput.setText("10");
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
        hiloLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        nLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nLabel.setText("0");
        nLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        mLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        mLabel.setText("0");
        mLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("0");
        jLabel15.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        rechazadasLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        rechazadasLabel.setText("0");
        rechazadasLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        pLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        pLabel.setText("0");
        pLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        colaHiloLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        colaHiloLabel.setText("0");
        colaHiloLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        rechazadasLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        rechazadasLabel2.setText("0");
        rechazadasLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        rechazadasLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        rechazadasLabel3.setText("0");
        rechazadasLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        rechazadasLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        rechazadasLabel4.setText("0");
        rechazadasLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

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
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(129, 129, 129)
                        .addGroup(ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(mLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ImagenesLayout.createSequentialGroup()
                        .addComponent(rechazadasLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ImagenesLayout.createSequentialGroup()
                        .addGroup(ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rechazadasLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rechazadasLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rechazadasLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
            .addGroup(ImagenesLayout.createSequentialGroup()
                .addGap(237, 237, 237)
                .addComponent(hiloLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
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
                            .addComponent(rechazadasLabel2)
                            .addComponent(jLabel15))
                        .addGap(55, 55, 55)))
                .addGroup(ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mLabel)
                    .addGroup(ImagenesLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(rechazadasLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                .addGroup(ImagenesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pLabel)
                    .addComponent(rechazadasLabel4)))
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(333, 333, 333)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Imagenes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(351, 351, 351)
                                        .addComponent(EmprezarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 261, Short.MAX_VALUE)))
                .addContainerGap())
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
                .addGap(6, 6, 6)
                .addComponent(EmprezarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(Imagenes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
       
        getEntradas(); //se cargan los datos y se empieza la simulacion  
         
    }//GEN-LAST:event_EmprezarBtnActionPerformed

    private void ncInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ncInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ncInputActionPerformed

    private void nInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nInputActionPerformed

    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
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
    private javax.swing.JLabel colaHiloLabel;
    private javax.swing.JLabel hiloLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JLabel rechazadasLabel2;
    private javax.swing.JLabel rechazadasLabel3;
    private javax.swing.JLabel rechazadasLabel4;
    private javax.swing.JTextField tInput;
    private javax.swing.JTextField tmInput;
    // End of variables declaration//GEN-END:variables
}
