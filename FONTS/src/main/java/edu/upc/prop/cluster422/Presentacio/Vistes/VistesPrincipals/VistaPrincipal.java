package edu.upc.prop.cluster422.Presentacio.Vistes.VistesPrincipals;

import edu.upc.prop.cluster422.Presentacio.Controladors.CtrlPresentacio;
import edu.upc.prop.cluster422.Presentacio.Vistes.VistesUsuaris.VistaUsuarisPrincipal;
import edu.upc.prop.cluster422.Presentacio.Vistes.VistesEnquesta.VistaEnquestesPrincipal;

import javax.swing.*;
import java.awt.*;

/**
 * Vista principal de l'aplicació. Actua com a gestor principal de les vistes.
 * 
 * @author Andreu Puerto
 * 
 * @version 15/12/2025
 */
public class VistaPrincipal extends JFrame {

    /**
     * Controlador de la presentacio
     */
    private final CtrlPresentacio ctrlPresentacio;

    /**
     * Panel principal
     */
    private JPanel panelPrincipal;

    /**
     * Layout de la vista principal
     */
    private CardLayout cardLayout;

    /**
     * Vista d'enquestes principal
     */
    private VistaEnquestesPrincipal vistaEnquestesPrincipal;

    /**
     * Vista del menu principal
     */
    private VistaMenu vistaMenu;

    /**
     * Vista d'usuaris principal
     */
    private VistaUsuarisPrincipal vistaUsuarisPrincipal;

    /**
     * Constructor de la vista principal.
     * 
     * @param ctrlPresentacio El controlador de presentacio.
     */
    public VistaPrincipal(CtrlPresentacio ctrlPresentacio) {
        this.ctrlPresentacio = ctrlPresentacio;

        initComponents();
        inicialitzarVistes();
        mostrarVista("benvinguda");
    }

    /**
     * Inicialitza els components de la vista principal.
     */
    private void initComponents() {
        this.setTitle("GESTOR ENQUESTES GRUP 42.2");
        this.setSize(1000, 700); // Una mica més gran
        this.setMinimumSize(new Dimension(800, 500));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        this.add(panelPrincipal);
    }

    /**
     * Inicialitza totes les vistes de la vista principal.
     */
    private void inicialitzarVistes() {
        // Benvinguda
        panelPrincipal.add(new VistaBenvinguda(ctrlPresentacio), "benvinguda");

        // Crèdits
        panelPrincipal.add(new VistaCredits(ctrlPresentacio), "credits");

        // Menú Principal
        this.vistaMenu = new VistaMenu(ctrlPresentacio);
        panelPrincipal.add(vistaMenu, "menu");

        this.vistaEnquestesPrincipal = ctrlPresentacio.getCtrlEnquestes().getVistaEnquestes();
        panelPrincipal.add(vistaEnquestesPrincipal, "enquestes");

        this.vistaUsuarisPrincipal = ctrlPresentacio.getCtrlUsuaris().getVistaUsuarisPrincipal();
        panelPrincipal.add(vistaUsuarisPrincipal, "usuaris");
    }

    /**
     * Mostra una vista principal.
     * 
     * @param nomVista El nom de la vista a mostrar.
     */
    public void mostrarVista(String nomVista) {
        cardLayout.show(panelPrincipal, nomVista);
    }

    /**
     * Retorna la vista d'enquestes principal.
     * 
     * @return La vista d'enquestes principal.
     */
    public VistaEnquestesPrincipal getVistaEnquestesPrincipal() {
        return this.vistaEnquestesPrincipal;
    }

    /**
     * Retorna la vista d'usuaris principal.
     * 
     * @return La vista d'usuaris principal.
     */
    public VistaUsuarisPrincipal getVistaUsuarisPrincipal() {
        return this.vistaUsuarisPrincipal;
    }

    /**
     * Estableix el nom de l'usuari al menú.
     * 
     * @param nom El nom de l'usuari.
     */
    public void setNomUsuari(String nom) {
        vistaMenu.setNomUsuari(nom);
    }
}