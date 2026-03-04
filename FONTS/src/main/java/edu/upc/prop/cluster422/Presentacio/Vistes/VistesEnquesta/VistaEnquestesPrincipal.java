package edu.upc.prop.cluster422.Presentacio.Vistes.VistesEnquesta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacioEnquestes;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Vista principal d'enquestes. Mostra un menú per gestionar les enquestes.
 * Serveix de gestor per a les altres vistes d'enquestes.
 * 
 * @author Andreu Puerto
 * @version 15/12/2025
 */
public class VistaEnquestesPrincipal extends JPanel {

    /**
     * Controlador de la presentació d'enquestes
     */
    private CtrlPresentacioEnquestes ctrlPresentacioEnquestes;

    /**
     * Panel principal de la vista
     */
    private JPanel panelPrincipal;

    /**
     * Layout per a la gestió de les vistes
     */
    private CardLayout cardsEnquestes;

    /**
     * Vista de gestió d'enquestes. És el menú principal.
     */
    private JPanel panelGestioEnquestes;

    /**
     * Vista de selecció d'enquestes.
     */
    private VistaEnquestaSeleccio panelSeleccio;

    /**
     * Vista de menu d'enquestes.
     */
    private VistaEnquestaMenu panelEnquesta;

    /**
     * Vista de respondre a enquestes.
     */
    private VistaEnquestaRespondre panelRespondre;

    /**
     * Vista de modificacio d'enquestes.
     */
    private VistaEnquestaModificar panelModificar;

    // Colors Corporatius (Iguals al Menu Principal)
    private final Color COLOR_BLAU = new Color(0, 106, 176);
    private final Color COLOR_FONS_COS = new Color(240, 240, 240);
    private final Color COLOR_TARGETA = Color.WHITE;

    /**
     * Constructor de la vista principal d'enquestes.
     * 
     * @param ctrlPresentacioEnquestes
     */
    public VistaEnquestesPrincipal(CtrlPresentacioEnquestes ctrlPresentacioEnquestes) {
        this.ctrlPresentacioEnquestes = ctrlPresentacioEnquestes;
        initComponents();
        initVistesEnquesta();

        mostrarVista("gestioenquestes");
    }

    /**
     * Inicialitza els components de la vista principal d'enquestes.
     */
    private void initComponents() {
        this.setLayout(new BorderLayout());
        cardsEnquestes = new CardLayout();
        panelPrincipal = new JPanel(cardsEnquestes);

        initPanelGestioEnquestes();

        this.add(panelPrincipal, BorderLayout.CENTER);
    }

    /**
     * Inicialitza totes les vistes d'enquestes.
     */
    private void initVistesEnquesta() {
        panelPrincipal.add(panelGestioEnquestes, "gestioenquestes");

        panelSeleccio = new VistaEnquestaSeleccio(ctrlPresentacioEnquestes);
        panelPrincipal.add(panelSeleccio, "seleccio");

        panelEnquesta = new VistaEnquestaMenu(ctrlPresentacioEnquestes);
        panelPrincipal.add(panelEnquesta, "menuenquesta");

        panelRespondre = new VistaEnquestaRespondre(ctrlPresentacioEnquestes);
        panelPrincipal.add(panelRespondre, "respondre");

        panelPrincipal.add(new VistaEnquestaClustering(ctrlPresentacioEnquestes), "clustering");
        panelPrincipal.add(new VistaCrearEnquesta(ctrlPresentacioEnquestes), "crearenquesta");
        panelPrincipal.add(new VistaEnquestaImportarResposta(ctrlPresentacioEnquestes), "importarresposta");
        panelPrincipal.add(new VistaEnquestaImportar(ctrlPresentacioEnquestes), "importarenquesta");

        panelModificar = new VistaEnquestaModificar(ctrlPresentacioEnquestes);
        panelPrincipal.add(panelModificar, "modificarenquesta");
    }

    /**
     * Inicialitza el panell de gestió d'enquestes.
     */
    private void initPanelGestioEnquestes() {
        panelGestioEnquestes = new JPanel(new BorderLayout());

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_BLAU);
        panelHeader.setPreferredSize(new Dimension(800, 70));
        panelHeader.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel labelTitol = new JLabel("Gestió d'Enquestes");
        labelTitol.setFont(new Font("Arial", Font.BOLD, 22));
        labelTitol.setForeground(Color.WHITE);
        ImageIcon iconEdit = ctrlPresentacioEnquestes.carregarIcona("edit", 32, 32);
        if (iconEdit != null)
            labelTitol.setIcon(iconEdit);

        JButton botoTornarMenu = new JButton("Tornar al Menú");
        estilitzarBotoHeader(botoTornarMenu);
        ImageIcon iconBack = ctrlPresentacioEnquestes.carregarIcona("goback", 16, 16);
        if (iconBack != null)
            botoTornarMenu.setIcon(iconBack);

        panelHeader.add(labelTitol, BorderLayout.WEST);

        JPanel panelDreta = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelDreta.setOpaque(false);
        panelDreta.add(botoTornarMenu);
        panelHeader.add(panelDreta, BorderLayout.EAST);

        panelGestioEnquestes.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCos = new JPanel(new GridBagLayout());
        panelCos.setBackground(COLOR_FONS_COS);

        JPanel panelTargeta = new JPanel(new GridBagLayout());
        panelTargeta.setBackground(COLOR_TARGETA);
        panelTargeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(30, 50, 30, 50)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        JLabel labelAccions = new JLabel("Què vols fer?", SwingConstants.LEFT);
        labelAccions.setFont(new Font("Arial", Font.PLAIN, 18));
        labelAccions.setForeground(new Color(80, 80, 80));
        labelAccions.setBorder(new EmptyBorder(0, 0, 15, 0));

        gbc.gridy = 0;
        panelTargeta.add(labelAccions, gbc);

        ImageIcon iconAdd = ctrlPresentacioEnquestes.carregarIcona("add", 32, 32);
        ImageIcon iconImport = ctrlPresentacioEnquestes.carregarIcona("download", 32, 32);
        ImageIcon iconManage = ctrlPresentacioEnquestes.carregarIcona("cluster", 32, 32);

        JButton botoCrear = crearBotoModul("Crear Nova Enquesta", "Defineix una enquesta des de zero", iconAdd);
        JButton botoImportar = crearBotoModul("Importar Enquesta", "Carrega una enquesta des d'un fitxer", iconImport);
        JButton botoSeleccionar = crearBotoModul("Gestionar Existent",
                "Selecciona una enquesta per editar, respondre o analitzar", iconManage);

        gbc.gridy = 1;
        panelTargeta.add(botoCrear, gbc);
        gbc.gridy = 2;
        panelTargeta.add(botoImportar, gbc);
        gbc.gridy = 3;
        panelTargeta.add(botoSeleccionar, gbc);

        panelCos.add(panelTargeta);
        panelGestioEnquestes.add(panelCos, BorderLayout.CENTER);

        botoCrear.addActionListener(e -> ctrlPresentacioEnquestes.mostrarVistaEnquestes("crearenquesta"));
        botoImportar.addActionListener(e -> ctrlPresentacioEnquestes.mostrarVistaEnquestes("importarenquesta"));
        botoSeleccionar.addActionListener(e -> {
            panelSeleccio.refrescar();
            ctrlPresentacioEnquestes.mostrarVistaEnquestes("seleccio");
        });
        botoTornarMenu.addActionListener(e -> ctrlPresentacioEnquestes.mostrarVistaPrincipal("menu"));
    }

    /**
     * Mostra la vista especificada.
     * 
     * @param nomVista Nom de la vista a mostrar.
     */
    public void mostrarVista(String nomVista) {
        if (nomVista.equals("modificarenquesta")) {
            panelModificar.carregarDadesEnquestaActual();
        } else if (nomVista.equals("respondre")) {
            ctrlPresentacioEnquestes.prepararVistaRespondre();
        } else if (nomVista.equals("menuenquesta")) {
            panelEnquesta.setNomEnquesta(ctrlPresentacioEnquestes.getNomEnquesta());
            panelEnquesta.refrescarBotons();
        }

        cardsEnquestes.show(panelPrincipal, nomVista);
    }

    /**
     * Retorna el panell de respondre.
     * 
     * @return Panell de respondre.
     */
    public VistaEnquestaRespondre getPanelRespondre() {
        return panelRespondre;
    }

    /**
     * Retorna la vista de modificar.
     * 
     * @return Vista de modificar.
     */
    public VistaEnquestaModificar getVistaModificar() {
        return panelModificar;
    }

    /**
     * Crea un boto per a un mòdul amb una icona específica.
     * 
     * @param titol      Títol del boto.
     * @param descripcio Descripció del boto.
     * @param icona      Icona del boto.
     * @return Boto creat.
     */
    private JButton crearBotoModul(String titol, String descripcio, ImageIcon icona) {

        JButton boto = new JButton();
        boto.setLayout(new BorderLayout(15, 0));
        if (icona != null) {
            JLabel iconLabel = new JLabel(icona);
            boto.add(iconLabel, BorderLayout.WEST);
        }
        JPanel panelText = new JPanel(new GridLayout(2, 1));
        panelText.setOpaque(false);

        JLabel labelTitol = new JLabel(titol);
        labelTitol.setFont(new Font("Arial", Font.BOLD, 14));
        labelTitol.setForeground(COLOR_BLAU);

        JLabel labelDesc = new JLabel(descripcio);
        labelDesc.setFont(new Font("Arial", Font.PLAIN, 11));
        labelDesc.setForeground(Color.GRAY);

        panelText.add(labelTitol);
        panelText.add(labelDesc);

        boto.add(panelText, BorderLayout.CENTER);

        boto.setHorizontalAlignment(SwingConstants.LEFT);
        boto.setPreferredSize(new Dimension(400, 70));
        boto.setBackground(Color.WHITE);
        boto.setFocusPainted(false);
        boto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boto.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        boto.setIconTextGap(15);

        boto.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boto.setBackground(new Color(245, 250, 255));
                boto.setBorder(BorderFactory.createLineBorder(COLOR_BLAU, 1));
            }

            public void mouseExited(MouseEvent e) {
                boto.setBackground(Color.WHITE);
                boto.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
            }
        });
        return boto;
    }

    /**
     * Estiliza un boto per al header.
     * 
     * @param boto Boto a estilitzar.
     */
    private void estilitzarBotoHeader(JButton boto) {
        boto.setForeground(Color.WHITE);
        boto.setBackground(new Color(0, 80, 150));
        boto.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        boto.setFocusPainted(false);
        boto.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boto.setFont(new Font("Arial", Font.PLAIN, 12));
    }
}