package edu.upc.prop.cluster422.Presentacio.Vistes.VistesPrincipals;

import javax.swing.*;
import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacio;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Vista de benvinguda. Mostra la pantalla principal amb les opcions de login,
 * registre, credits i sortir.
 * 
 * @author Andreu Puerto
 * @version 15/12/2025
 */
public class VistaBenvinguda extends JPanel {

    /**
     * Controlador de la presentacio
     */
    private final CtrlPresentacio ctrlPresentacio;

    private final Color COLOR_FONS = new Color(240, 245, 250);
    private final Color COLOR_TITOL = new Color(40, 60, 100);
    private final Color COLOR_BOTO_PRIMARY = new Color(60, 130, 200);
    private final Color COLOR_BOTO_DANGER = new Color(200, 80, 80);

    /**
     * Constructor de la vista de benvinguda.
     * 
     * @param ctrlPresentacio Controlador de la presentacio.
     */
    public VistaBenvinguda(CtrlPresentacio ctrlPresentacio) {
        this.ctrlPresentacio = ctrlPresentacio;
        inicialitzarComponent();
    }

    /**
     * Inicialitza els components de la vista.
     */
    private void inicialitzarComponent() {
        this.setLayout(new GridBagLayout());
        this.setBackground(COLOR_FONS);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JPanel panelLogos = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        panelLogos.setOpaque(false);

        ImageIcon iconUpc = ctrlPresentacio.carregarIcona("upc", 100, 100);
        ImageIcon iconFib = ctrlPresentacio.carregarIcona("fib", 200, 100);

        if (iconUpc != null)
            panelLogos.add(new JLabel(iconUpc));
        if (iconFib != null)
            panelLogos.add(new JLabel(iconFib));

        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 30, 0);
        this.add(panelLogos, gbc);

        JLabel titol = new JLabel("GESTOR D'ENQUESTES", SwingConstants.CENTER);
        titol.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titol.setForeground(COLOR_TITOL);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 10, 0);
        this.add(titol, gbc);

        JLabel subtitol = new JLabel("GRUP 42.2 - PROP", SwingConstants.CENTER);
        subtitol.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitol.setForeground(Color.GRAY);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 50, 0);
        this.add(subtitol, gbc);

        JPanel panelBotons = new JPanel(new GridLayout(4, 1, 15, 15));
        panelBotons.setOpaque(false);

        JButton botoLogin = crearBotoEstilitzat("Iniciar Sessió", COLOR_BOTO_PRIMARY);
        JButton botoRegistre = crearBotoEstilitzat("Registrar-se", COLOR_BOTO_PRIMARY);
        JButton botoCredits = crearBotoEstilitzat("Crèdits", new Color(70, 70, 70));
        JButton botoSortir = crearBotoEstilitzat("Sortir", COLOR_BOTO_DANGER);

        panelBotons.add(botoLogin);
        panelBotons.add(botoRegistre);
        panelBotons.add(botoCredits);
        panelBotons.add(botoSortir);

        gbc.gridy = 3;
        gbc.ipadx = 120;
        gbc.insets = new Insets(0, 0, 0, 0);
        this.add(panelBotons, gbc);

        botoLogin.addActionListener(e -> ctrlPresentacio.mostrarVistaUsuari("login"));
        botoRegistre.addActionListener(e -> ctrlPresentacio.mostrarVistaUsuari("registre"));
        botoCredits.addActionListener(e -> ctrlPresentacio.mostrarVista("credits"));
        botoSortir.addActionListener(e -> System.exit(0));
    }

    /**
     * Funció auxiliar per crear botons estilitzats.
     * 
     * @param text Text del botó.
     * @param bg   Color de fons del botó.
     * @return Botó estilitzat.
     */
    private JButton crearBotoEstilitzat(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.brighter());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }
}