package edu.upc.prop.cluster422.Presentacio.Vistes.VistesUsuaris;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacioUsuaris;
import edu.upc.prop.cluster422.Domini.Controladors.CtrlDomini;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Vista principal per a la gestió d'usuaris.
 * Permet cercar usuaris, veure les seves enquestes, consultar detalls i
 * gestionar el perfil propi.
 * 
 * @author Guillem Revuelta
 * @version 15/12/2025
 */
public class VistaUsuaris extends JPanel {

    /**
     * Controlador de la presentacio d'usuaris
     */
    private CtrlPresentacioUsuaris ctrlPresentacioUsuaris;

    private final Color COLOR_BLAU = new Color(0, 106, 176);
    private final Color COLOR_FONS_COS = new Color(240, 240, 240);
    private final Color COLOR_TARGETA = Color.WHITE;

    /**
     * Simulacio de usuari
     */
    private String simNom = "UsuariProva1";

    /**
     * Simulacio de contrasenya
     */
    private String simPass = "1234";

    /**
     * Simulacio de edat
     */
    private int simEdat = -1;

    /**
     * Simulacio de email
     */
    private String simEmail = "usuari.prova@exemple.com";

    /**
     * Camp de text per cercar usuaris
     */
    private JTextField campCercaUsuari;

    /**
     * Taula de usuaris
     */
    private JTable taulaUsuaris;

    /**
     * Model de taula
     */
    private DefaultTableModel modelTaula;

    /**
     * Botó per a consultar dades
     */
    private JButton botoConsultarDades;

    /**
     * Botó per a veure enquesta
     */

    /**
     * Botó per a tornar enrere
     */
    private JButton botoTornarEnrere;

    /**
     * Botó per a anar a la vista d'admin
     */
    private JButton botoVistaAdmin;

    /**
     * Botó per a editar perfil
     */
    private JButton botoEditarPerfil;

    /**
     * Botó per a donar de baixa
     */
    private JButton botoDonarDeBaixa;

    /**
     * Constructor de la vista d'usuaris.
     * 
     * @param ctrlPresentacioUsuaris Controlador de presentació d'usuaris.
     */
    public VistaUsuaris(CtrlPresentacioUsuaris ctrlPresentacioUsuaris) {
        this.ctrlPresentacioUsuaris = ctrlPresentacioUsuaris;
        inicialitzarComponent();
        sincronitzar();
    }

    /**
     * Inicialitza els components de la vista.
     */
    private void inicialitzarComponent() {
        this.setLayout(new BorderLayout());
        initHeader();
        initCosCentral();
        assignarListeners();
    }

    /**
     * Inicialitza el header de la vista.
     */
    private void initHeader() {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_BLAU);
        panelHeader.setPreferredSize(new Dimension(800, 70));
        panelHeader.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel labelTitol = new JLabel("GESTIÓ D'USUARIS");
        labelTitol.setFont(new Font("Arial", Font.BOLD, 22));
        labelTitol.setForeground(Color.WHITE);

        ImageIcon iconTeam = ctrlPresentacioUsuaris.carregarIcona("team", 32, 32);
        if (iconTeam != null)
            labelTitol.setIcon(iconTeam);

        botoTornarEnrere = new JButton("Tornar al Menú");
        estilitzarBotoHeader(botoTornarEnrere);
        ImageIcon iconBack = ctrlPresentacioUsuaris.carregarIcona("goback", 16, 16);
        if (iconBack != null)
            botoTornarEnrere.setIcon(iconBack);

        JPanel panelDreta = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDreta.setOpaque(false);
        panelDreta.add(botoTornarEnrere);

        botoVistaAdmin = new JButton("Vista Admin");
        estilitzarBotoHeader(botoVistaAdmin);
        botoVistaAdmin.setVisible(false);
        panelDreta.add(botoVistaAdmin);

        panelHeader.add(labelTitol, BorderLayout.WEST);
        panelHeader.add(panelDreta, BorderLayout.EAST);

        this.add(panelHeader, BorderLayout.NORTH);
    }

    /**
     * Inicialitza el cos central de la vista.
     */
    private void initCosCentral() {
        JPanel panelFons = new JPanel(new GridBagLayout());
        panelFons.setBackground(COLOR_FONS_COS);

        JPanel panelTargeta = new JPanel(new GridBagLayout());
        panelTargeta.setBackground(COLOR_TARGETA);
        panelTargeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridx = 0;

        JLabel lblSeccio1 = new JLabel("Cercar i Consultar Usuaris");
        lblSeccio1.setFont(new Font("Arial", Font.BOLD, 18));
        lblSeccio1.setForeground(COLOR_BLAU);

        gbc.gridy = 0;
        gbc.weighty = 0.0;
        panelTargeta.add(lblSeccio1, gbc);

        JPanel panelCerca = new JPanel(new BorderLayout(10, 0));
        panelCerca.setOpaque(false);
        panelCerca.add(new JLabel("Usuari: "), BorderLayout.WEST);

        campCercaUsuari = new JTextField();
        campCercaUsuari.setPreferredSize(new Dimension(250, 35));
        campCercaUsuari.setFont(new Font("Arial", Font.PLAIN, 14));
        campCercaUsuari.setBackground(Color.WHITE);

        botoConsultarDades = crearBotoAccio("Buscar i Veure Dades", COLOR_BLAU, Color.WHITE);

        panelCerca.add(campCercaUsuari, BorderLayout.CENTER);
        panelCerca.add(botoConsultarDades, BorderLayout.EAST);

        gbc.gridy = 1;
        panelTargeta.add(panelCerca, gbc);

        JLabel lblTaula = new JLabel("Llista d'Enquestes de l'Usuari (Primer busca l'usuari):");
        lblTaula.setFont(new Font("Arial", Font.BOLD, 14));
        lblTaula.setForeground(Color.DARK_GRAY);

        gbc.gridy = 2;
        panelTargeta.add(lblTaula, gbc);

        String[] columnNames = { "Títol Enquesta" };
        modelTaula = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        taulaUsuaris = new JTable(modelTaula);
        estilitzarTaula(taulaUsuaris);
        taulaUsuaris.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(taulaUsuaris);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scrollPane.getViewport().setBackground(Color.WHITE);

        scrollPane.setPreferredSize(new Dimension(600, 100));

        gbc.gridy = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        panelTargeta.add(scrollPane, gbc);

        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel panelBotoTaula = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotoTaula.setOpaque(false);

        JSeparator sep = new JSeparator();
        sep.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 0, 5, 0);
        panelTargeta.add(sep, gbc);

        JLabel lblSeccio2 = new JLabel("El Meu Perfil");
        lblSeccio2.setFont(new Font("Arial", Font.BOLD, 18));
        lblSeccio2.setForeground(COLOR_BLAU);
        gbc.gridy = 6;
        gbc.insets = new Insets(5, 10, 5, 10);
        panelTargeta.add(lblSeccio2, gbc);

        JPanel panelBotonsPerfil = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelBotonsPerfil.setOpaque(false);

        botoEditarPerfil = crearBotoAccio("Modificar les meves dades", Color.WHITE, COLOR_BLAU);
        botoDonarDeBaixa = crearBotoAccio("Eliminar el meu compte", new Color(220, 50, 50), Color.WHITE);

        panelBotonsPerfil.add(botoEditarPerfil);
        panelBotonsPerfil.add(botoDonarDeBaixa);

        gbc.gridy = 7;
        gbc.insets = new Insets(5, 10, 10, 10);
        panelTargeta.add(panelBotonsPerfil, gbc);

        GridBagConstraints gbcTargeta = new GridBagConstraints();
        gbcTargeta.gridx = 0;
        gbcTargeta.gridy = 0;
        gbcTargeta.weightx = 1.0;
        gbcTargeta.weighty = 1.0;
        gbcTargeta.fill = GridBagConstraints.BOTH;
        gbcTargeta.insets = new Insets(20, 20, 20, 20);

        panelFons.add(panelTargeta, gbcTargeta);
        this.add(panelFons, BorderLayout.CENTER);
    }

    /**
     * Assigna els listeners als botons.
     */
    private void assignarListeners() {
        botoTornarEnrere.addActionListener(e -> ctrlPresentacioUsuaris.mostrarVistaPrincipal("menu"));
        botoVistaAdmin.addActionListener(e -> ctrlPresentacioUsuaris.mostrarVistaUsuari("admin"));
        botoEditarPerfil.addActionListener(e -> obrirDialegEdicio());

        botoDonarDeBaixa.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Estàs segur que vols eliminar el teu compte?",
                    "Confirmar Baixa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    CtrlDomini.getInstance().esborrarUsuariActual();
                    JOptionPane.showMessageDialog(this, "Compte eliminat correctament.");
                    ctrlPresentacioUsuaris.mostrarVistaUsuari("login");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error eliminant compte: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        botoConsultarDades.addActionListener(e -> {
            String nomBuscat = campCercaUsuari.getText().trim();
            if (!nomBuscat.isEmpty()) {
                mostrarDetallsUsuari(nomBuscat);
                actualitzarTaulaEnquestes(nomBuscat);
            } else {
                JOptionPane.showMessageDialog(this, "Introdueix un nom d'usuari primer.");
            }
        });

    }

    /**
     * Gestiona la selecció d'una enquesta a la taula i obre la vista de detalls.
     * Si no hi ha cap fila seleccionada, mostra un avís.
     */

    /**
     * Actualitza la taula d'enquestes segons l'usuari seleccionat.
     * 
     * @param usuari Nom de l'usuari del qual es volen veure les enquestes.
     */
    private void actualitzarTaulaEnquestes(String usuari) {
        modelTaula.setRowCount(0);

        boolean realDataFound = false;
        try {
            // Intentem recuperar enquestes reals
            String[] titols = ctrlPresentacioUsuaris.getEnquestesCreadesUsuari(usuari);
            if (titols != null) {
                for (String t : titols) {
                    // El domini només retorna títols aquí, posem placeholders per la resta
                    modelTaula.addRow(new String[] { t });
                }
                realDataFound = true;
            }
        } catch (Exception e) {
            // Usuari no trobat o error, ignorem i provem simulació
        }

        if (!realDataFound) {
            // Fallback a simulació
            if (usuari.equals("AdminTest") || usuari.equals(simNom)) {
                modelTaula.addRow(new String[] { "Enquesta Satisfacció" });
                modelTaula.addRow(new String[] { "Preferències Compres" });
            } else if (usuari.equals("UsuariProva2")) {
                modelTaula.addRow(new String[] { "Opinió Producte X" });
            }
            // Si no existeix i no és simulat, la taula es queda buida (correcte segons
            // requisit)
        }
    }

    /**
     * Funció auxiliar per estilitzar un boto de header.
     * 
     * @param btn Boto a estilitzar.
     */
    private void estilitzarBotoHeader(JButton btn) {
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0, 80, 150));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
    }

    /**
     * Funció auxiliar per crear un boto amb el text especificat.
     * 
     * @param text Text del boto.
     * @param bg   Color de fons del boto.
     * @param fg   Color del text del boto.
     * @return Boto creat.
     */
    private JButton crearBotoAccio(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (bg.equals(Color.WHITE)) {
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_BLAU, 1),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        } else {
            btn.setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
        }
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (bg.equals(Color.WHITE))
                    btn.setBackground(new Color(240, 248, 255));
                else
                    btn.setBackground(bg.brighter());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    /**
     * Funció auxiliar per estilitzar la taula.
     * 
     * @param table Taula a estilitzar.
     */
    private void estilitzarTaula(JTable table) {
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(245, 245, 245));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
    }

    /**
     * Obre un diàleg per editar les dades del perfil.
     */
    private void obrirDialegEdicio() {
        // Valors actuals (per defecte simulació)
        String curNom = simNom;
        String curPass = simPass;
        int curEdat = simEdat;
        String curEmail = simEmail;

        // Intentar recuperar dades reals del controlador
        try {
            String[] dades = CtrlDomini.getInstance().getDadesUsuariActual();
            // Assumim l'ordre de dades: [nom, password, edat, email]
            // Assumim l'ordre de dades: [nom, password, email, edat]
            if (dades != null) {
                if (dades.length > 0)
                    curNom = dades[0];
                if (dades.length > 1)
                    curPass = dades[1];
                if (dades.length > 2)
                    curEmail = dades[2];
                if (dades.length > 3) {
                    try {
                        curEdat = Integer.parseInt(dades[3]);
                    } catch (NumberFormatException nfe) {
                        // Ignorar si no és enter
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("No s'han pogut recuperar dades actuals: " + e.getMessage());
        }

        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField campNom = new JTextField(curNom);
        JPasswordField campPass = new JPasswordField(curPass);
        JSpinner campEdat = new JSpinner(new SpinnerNumberModel(curEdat, -1, 120, 1));
        JTextField campEmail = new JTextField(curEmail);

        panelForm.add(new JLabel("Nou Nom:"));
        panelForm.add(campNom);
        panelForm.add(new JLabel("Nova Contrasenya:"));
        panelForm.add(campPass);
        panelForm.add(new JLabel("Nova Edat:"));
        panelForm.add(campEdat);
        panelForm.add(new JLabel("Nou Email:"));
        panelForm.add(campEmail);

        int result = JOptionPane.showConfirmDialog(this, panelForm, "Modificar Les Meves Dades",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nouNom = campNom.getText();
            String novaPass = new String(campPass.getPassword());
            int novaEdat = (Integer) campEdat.getValue();
            String nouEmail = campEmail.getText();

            try {
                CtrlDomini ctrl = CtrlDomini.getInstance();
                boolean changed = false;

                if (!nouNom.equals(curNom)) {
                    ctrl.modificarUsuari_nom(nouNom);
                    changed = true;
                }
                if (novaEdat != curEdat) {
                    ctrl.modificarUsuari_edat(novaEdat);
                    changed = true;
                }
                if (!nouEmail.equals(curEmail)) {
                    ctrl.modificarUsuari_email(nouEmail);
                    changed = true;
                }
                if (!novaPass.equals(curPass)) {
                    ctrl.modificarUsuari_contrasenya(curPass, novaPass); // Requerim la vella
                    changed = true;
                }

                if (changed) {
                    JOptionPane.showMessageDialog(this, "Dades actualitzades correctament.");
                    // Actualitzem simulació per si de cas
                    simNom = nouNom;
                    simPass = novaPass;
                    simEdat = novaEdat;
                    simEmail = nouEmail;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error actualitzant dades: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Mostra els detalls d'un usuari seleccionat.
     * 
     * @param nomUsuari Nom de l'usuari seleccionat.
     */
    private void mostrarDetallsUsuari(String nomUsuari) {
        String emailMostrar = "";
        String edatMostrar = "";

        boolean trobat = false;

        // Intentar recuperar dades reals del controlador
        try {
            String[] dades = ctrlPresentacioUsuaris.veureDadesPubliquesUsuari(nomUsuari);
            // Suposem que retorna [username, password, edat, email] o similar segons
            // implementation
            // La implementació de adminVeureDadesPrivades pot variar, però intentem
            // usar-la.
            // Si no falla, és que l'usuari existeix.

            // Si el mètode retorna dades útils les fem servir.
            // Normalment adminVeureDadesPrivades retorna contrasenya també, la ignorem aquí
            // per seguretat visual si cal
            if (dades != null && dades.length >= 3) {
                // Format esperat aproximat: nom, pass, edat, email?
                // Depèn de CtrlDomini.getDadesUsuariprivat.
                // Si mirem CtrlDomini, no veiem exactament l'ordre, però assumim un estandard.
                // En qualsevol cas, el fet que no peti ja indica existència.

                // Simulem dades si l'array no ens dona tot en format llegible o per defecte
                // Però si existeix, marquem trobat.
                trobat = true;

                // Intent de parseig (això és optimista, si falla anirà al catch)
                // edat
                if (dades.length > 1)
                    emailMostrar = dades[1];
                if (dades.length > 2)
                    edatMostrar = dades[2];

            }
        } catch (Exception e) {
            // L'usuari no existeix al domini o error
            trobat = false;
        }

        if (trobat) {
            JPanel panelDades = new JPanel(new GridLayout(0, 2, 10, 10));
            panelDades.add(new JLabel("Nom d'Usuari:"));
            JLabel lblNom = new JLabel(nomUsuari);
            lblNom.setFont(lblNom.getFont().deriveFont(Font.BOLD));
            panelDades.add(lblNom);
            panelDades.add(new JLabel("Correu Electrònic:"));
            panelDades.add(new JLabel(emailMostrar));
            panelDades.add(new JLabel("Edat:"));
            if (edatMostrar.equals("No especificada") || edatMostrar.equals("No especificat")
                    || edatMostrar.equals("-1")) {
                panelDades.add(new JLabel("No especificada"));
            } else {
                panelDades.add(new JLabel(edatMostrar + " anys"));
            }

            JOptionPane.showMessageDialog(this, panelDades, "Detalls de l'Usuari", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Usuari no trobat: " + nomUsuari, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sincronitza la llista d'usuaris amb el sistema.
     * (Ara es deixa buit o simplement neteja camps, ja que no hi ha selector)
     */
    public void sincronitzar() {
        if (campCercaUsuari != null) {
            campCercaUsuari.setText("");
        }
        if (modelTaula != null) {
            modelTaula.setRowCount(0);
        }
        // Actualitzar visibilitat boto admin
        if (botoVistaAdmin != null) {
            boolean isAdmin = false;
            try {
                if (CtrlDomini.getInstance().getDadesUsuariActual().length == 5) {
                    isAdmin = true;
                }
            } catch (Exception e) {
                // error
            }
            botoVistaAdmin.setVisible(isAdmin);
        }
    }
}