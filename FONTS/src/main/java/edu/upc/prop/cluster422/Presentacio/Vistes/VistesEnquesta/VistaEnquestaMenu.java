package edu.upc.prop.cluster422.Presentacio.Vistes.VistesEnquesta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacioEnquestes;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Vista de menú per a una enquesta seleccionada. Aquesta vista permet gestionar
 * la enquesta seleccionada.
 * 
 * @author Andreu Puerto
 * @version 15/12/2025
 */
public class VistaEnquestaMenu extends JPanel {

    /**
     * Controlador de la presentacio d'enquestes
     */
    private CtrlPresentacioEnquestes ctrlPresentacioEnquestes;

    /**
     * Etiqueta per a la visualitzacio del titol de l'enquesta
     */
    private JLabel labelTitolEnquesta;

    /**
     * Nom de l'enquesta seleccionada
     */
    private String nomEnquesta;

    /**
     * Panell on es posaran els botons
     */
    private JPanel panelBotonsContainer;

    /**
     * Colors
     */
    private final Color COLOR_BLAU = new Color(0, 106, 176);
    private final Color COLOR_FONS_COS = new Color(240, 240, 240);
    private final Color COLOR_TARGETA = Color.WHITE;

    /**
     * Constructor de la vista de menú per a una enquesta seleccionada.
     * 
     * @param ctrlPresentacioEnquestes
     */
    public VistaEnquestaMenu(CtrlPresentacioEnquestes ctrlPresentacioEnquestes) {
        this.ctrlPresentacioEnquestes = ctrlPresentacioEnquestes;
        inicialitzarComponent();
    }

    /**
     * Inicialitza els components de la vista.
     */
    private void inicialitzarComponent() {
        this.setLayout(new BorderLayout());

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_BLAU);
        panelHeader.setPreferredSize(new Dimension(800, 70));
        panelHeader.setBorder(new EmptyBorder(0, 20, 0, 20));

        labelTitolEnquesta = new JLabel("ENQUESTA SELECCIONADA");
        labelTitolEnquesta.setFont(new Font("Arial", Font.BOLD, 22));
        labelTitolEnquesta.setForeground(Color.WHITE);

        ImageIcon iconBack = ctrlPresentacioEnquestes.carregarIcona("goback", 16, 16);
        JButton botoTornar = new JButton("Tornar a Llista");
        botoTornar.setForeground(Color.WHITE);
        botoTornar.setBackground(new Color(0, 80, 150));
        botoTornar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        if (iconBack != null)
            botoTornar.setIcon(iconBack);

        botoTornar.addActionListener(e -> ctrlPresentacioEnquestes.mostrarVistaEnquestes("gestioenquestes"));

        panelHeader.add(labelTitolEnquesta, BorderLayout.WEST);
        JPanel pRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pRight.setOpaque(false);
        pRight.add(botoTornar);
        panelHeader.add(pRight, BorderLayout.EAST);

        this.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCos = new JPanel(new GridBagLayout());
        panelCos.setBackground(COLOR_FONS_COS);

        JPanel panelTargeta = new JPanel(new BorderLayout());
        panelTargeta.setBackground(COLOR_TARGETA);
        panelTargeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(30, 30, 30, 30)));

        JLabel lblAccions = new JLabel("Panell d'Accions", SwingConstants.LEFT);
        lblAccions.setFont(new Font("Arial", Font.PLAIN, 18));
        lblAccions.setForeground(Color.GRAY);
        lblAccions.setBorder(new EmptyBorder(0, 0, 20, 0));
        panelTargeta.add(lblAccions, BorderLayout.NORTH);

        panelBotonsContainer = new JPanel(new GridBagLayout());
        panelBotonsContainer.setBackground(COLOR_TARGETA);
        panelTargeta.add(panelBotonsContainer, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        panelCos.add(panelTargeta, gbc);

        this.add(panelCos, BorderLayout.CENTER);

        refrescarBotons();
    }

    /**
     * Mètode per regenerar els botons segons l'estat (si ha respost o no)
     */
    public void refrescarBotons() {
        panelBotonsContainer.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0);

        ImageIcon iconEdit = ctrlPresentacioEnquestes.carregarIcona("edit", 32, 32);
        ImageIcon iconCluster = ctrlPresentacioEnquestes.carregarIcona("cluster", 32, 32);
        ImageIcon iconImport = ctrlPresentacioEnquestes.carregarIcona("download", 32, 32);
        ImageIcon iconDelete = ctrlPresentacioEnquestes.carregarIcona("delete", 32, 32);
        ImageIcon iconSuccess = ctrlPresentacioEnquestes.carregarIcona("success", 32, 32);

        boolean haRespost = ctrlPresentacioEnquestes.usuariHaRespost();

        if (haRespost) {
            JButton botoModificarResp = crearBotoModul("Modificar la meva Resposta",
                    "Ja has respost. Clica per canviar les teves respostes.", iconSuccess);

            botoModificarResp.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 180, 100), 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)));

            botoModificarResp.addActionListener(e -> {
                ctrlPresentacioEnquestes.prepararVistaRespondre();
                ctrlPresentacioEnquestes.mostrarVistaEnquestes("respondre");
            });
            panelBotonsContainer.add(botoModificarResp, gbc);
            gbc.gridy++;
            JButton botoEliminarResp = crearBotoModul("Eliminar la meva Resposta",
                    "Esborra permanentment la teva resposta", iconDelete);
            botoEliminarResp.addActionListener(e -> {
                try {
                    ctrlPresentacioEnquestes.eliminarRespostaEnquesta();
                    JOptionPane.showMessageDialog(this, "Resposta eliminada correctament.", "Resposta Eliminada",
                            JOptionPane.INFORMATION_MESSAGE);
                    ctrlPresentacioEnquestes.mostrarVistaEnquestes("menuenquesta");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar la resposta: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            panelBotonsContainer.add(botoEliminarResp, gbc);
        } else {
            JButton botoRespondre = crearBotoModul("Respondre", "Omple l'enquesta com a usuari", iconEdit);
            botoRespondre.addActionListener(e -> {
                ctrlPresentacioEnquestes.prepararVistaRespondre();
                ctrlPresentacioEnquestes.mostrarVistaEnquestes("respondre");
            });
            panelBotonsContainer.add(botoRespondre, gbc);

            gbc.gridy++;
            JButton botoImportar = crearBotoModul("Importar Resposta", "Importa la resposta des d'un fitxer",
                    iconImport);
            botoImportar.addActionListener(e -> ctrlPresentacioEnquestes.mostrarVistaEnquestes("importarresposta"));
            panelBotonsContainer.add(botoImportar, gbc);
        }

        gbc.gridy++;
        JButton botoCluster = crearBotoModul("Anàlisi (Clustering)", "Executa K-Means sobre les respostes",
                iconCluster);
        botoCluster.addActionListener(e -> ctrlPresentacioEnquestes.mostrarVistaEnquestes("clustering"));
        panelBotonsContainer.add(botoCluster, gbc);

        gbc.gridy++;
        JButton botoModifEnq = crearBotoModul("Modificar Enquesta", "Edita preguntes o canvia el títol", iconEdit);
        botoModifEnq.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Estàs segur? Modificar l'enquesta esborrarà totes les respostes existents?", "Confirmació",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ctrlPresentacioEnquestes.mostrarVistaEnquestes("modificarenquesta");
            }
        });
        panelBotonsContainer.add(botoModifEnq, gbc);

        gbc.gridy++;
        JButton botoEliminar = crearBotoModul("Eliminar Enquesta", "Esborra permanentment l'enquesta", iconDelete);
        botoEliminar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Estàs segur?", "Confirmació", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    ctrlPresentacioEnquestes.eliminarEnquestaActual();
                    JOptionPane.showMessageDialog(this, "L'enquesta s'ha eliminat correctament.", "Enquesta Eliminada",
                            JOptionPane.INFORMATION_MESSAGE);
                    ctrlPresentacioEnquestes.mostrarVistaEnquestes("gestioenquestes");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panelBotonsContainer.add(botoEliminar, gbc);

        panelBotonsContainer.revalidate();
        panelBotonsContainer.repaint();
    }

    /**
     * Estableix el nom de l'enquesta seleccionada.
     * 
     * @param nom Nom de l'enquesta seleccionada.
     */
    public void setNomEnquesta(String nom) {
        nomEnquesta = nom;
        if (labelTitolEnquesta != null)
            labelTitolEnquesta.setText("Enquesta: " + nomEnquesta);
    }

    /**
     * Funció auxiliar per crear botons.
     * 
     * @param titol      Títol del botó.
     * @param descripcio Descripció del botó.
     * @param icona      Icona del botó.
     * @return Botó creat.
     */
    private JButton crearBotoModul(String titol, String descripcio, ImageIcon icona) {
        JButton boto = new JButton();
        boto.setLayout(new BorderLayout(15, 0));
        if (icona != null)
            boto.add(new JLabel(icona), BorderLayout.WEST);
        JPanel pText = new JPanel(new GridLayout(2, 1));
        pText.setOpaque(false);
        JLabel l1 = new JLabel(titol);
        l1.setFont(new Font("Arial", Font.BOLD, 14));
        l1.setForeground(COLOR_BLAU);
        JLabel l2 = new JLabel(descripcio);
        l2.setFont(new Font("Arial", Font.PLAIN, 11));
        l2.setForeground(Color.GRAY);
        pText.add(l1);
        pText.add(l2);

        boto.add(pText, BorderLayout.CENTER);
        boto.setPreferredSize(new Dimension(400, 65));
        boto.setBackground(Color.WHITE);
        boto.setFocusPainted(false);
        boto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        boto.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boto.setBackground(new Color(245, 250, 255));
            }

            public void mouseExited(MouseEvent e) {
                boto.setBackground(Color.WHITE);
            }
        });
        return boto;
    }
}