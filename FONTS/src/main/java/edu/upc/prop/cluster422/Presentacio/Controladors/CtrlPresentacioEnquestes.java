package edu.upc.prop.cluster422.Presentacio.Controladors;

import edu.upc.prop.cluster422.Domini.Controladors.CtrlDomini;
import edu.upc.prop.cluster422.Presentacio.Vistes.VistesEnquesta.VistaEnquestesPrincipal;
import edu.upc.prop.cluster422.Presentacio.Vistes.VistesEnquesta.VistaEnquestaRespondre;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;

/**
 * Controlador de presentacio d'enquestes. Actua com a gestor de les vistes
 * d'enquestes, i es comunica amb el domini.
 * 
 * @author Andreu Puerto
 * @version 12/12/2025
 */
public class CtrlPresentacioEnquestes {
    /**
     * Controlador de presentacio
     */
    private CtrlPresentacio ctrlPresentacio;

    /**
     * Controlador de domini
     */
    private CtrlDomini ctrlDomini;

    /**
     * Vista d'enquestes principal
     */
    private VistaEnquestesPrincipal vistaEnquestesPrincipal;

    /**
     * Vista d'enquesta respondre
     */
    private VistaEnquestaRespondre vistaEnquestaRespondre;

    /**
     * Nom de l'enquesta seleccionada
     */
    private String nomEnquestaSeleccionada;

    // Constructor
    /**
     * Constructor de CtrlPresentacioEnquestes.
     * 
     * @param ctrlPresentacio El controlador de presentacio.
     */
    public CtrlPresentacioEnquestes(CtrlPresentacio ctrlPresentacio) throws IOException {
        this.ctrlPresentacio = ctrlPresentacio;
        this.ctrlDomini = CtrlDomini.getInstance();
        this.vistaEnquestesPrincipal = new VistaEnquestesPrincipal(this);
    }

    // --- GETTERS I NAVEGACIÓ BÀSICA ---

    /**
     * Retorna la vista d'enquestes principal.
     * 
     * @return La vista d'enquestes principal.
     */
    public VistaEnquestesPrincipal getVistaEnquestes() {
        return vistaEnquestesPrincipal;
    }

    /**
     * Mostra una vista principal.
     * 
     * @param nomVista El nom de la vista a mostrar.
     */
    public void mostrarVistaPrincipal(String nomVista) {
        ctrlPresentacio.mostrarVista(nomVista);
    }

    /**
     * Mostra una vista d'enquestes.
     * 
     * @param nomVista El nom de la vista a mostrar.
     */
    public void mostrarVistaEnquestes(String nomVista) {
        vistaEnquestesPrincipal.mostrarVista(nomVista);
    }

    /**
     * Selecciona una enquesta.
     * 
     * @param titolEnquesta El titol de l'enquesta a seleccionar.
     */
    public void seleccionarEnquesta(String titolEnquesta) {
        // System.out.println("Seleccionant enquesta: " + titolEnquesta);
        this.nomEnquestaSeleccionada = titolEnquesta;
    }

    /**
     * Retorna el nom de l'enquesta seleccionada.
     * 
     * @return El nom de l'enquesta seleccionada.
     */
    public String getNomEnquesta() {
        return nomEnquestaSeleccionada;
    }

    // --- MÈTODES DE DOMINI (OBTENIR DADES) ---

    /**
     * Retorna la llista d'enquestes.
     * 
     * @return La llista d'enquestes.
     */
    public ArrayList<String> getLlistaEnquestes() {
        return ctrlDomini.getEnquestes();
    }

    /**
     * Retorna la llista de preguntes d'una enquesta.
     * 
     * @param titol El titol de l'enquesta.
     * @return La llista de preguntes de l'enquesta.
     */
    public ArrayList<String> getPreguntesEnquesta(String titol) {
        try {
            return new ArrayList<>(ctrlDomini.getPreguntesEnquesta(titol));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retorna el format d'una pregunta.
     * 
     * @param titol El titol de l'enquesta.
     * @param index L'index de la pregunta.
     * @return El format de la pregunta.
     */
    public String getFormatPregunta(String titol, int index) {
        try {
            return ctrlDomini.getFormatPregunta(titol, index);
        } catch (Exception e) {
            return "DESCONEGUT";
        }
    }

    /**
     * Retorna les opcions d'una pregunta.
     * 
     * @param titol El titol de l'enquesta.
     * @param index L'index de la pregunta.
     * @return Les opcions de la pregunta.
     */
    public ArrayList<String> getOpcionsPregunta(String titol, int index) {
        try {
            return new ArrayList<>(ctrlDomini.getOpcionsPregunta(titol, index));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // --- GESTIÓ D'ENQUESTES (CREAR, IMPORTAR, MODIFICAR, ELIMINAR) ---

    /**
     * Crea una enquesta completa.
     * 
     * @param titol     El titol de l'enquesta.
     * @param enunciats La llista d'enunciats de les preguntes.
     * @param tipus     La llista de tipus de les preguntes.
     * @param opcions   La llista d'opcions de les preguntes.
     * @throws Exception Si hi ha un error al crear l'enquesta.
     */
    public void crearEnquestaCompleta(String titol, ArrayList<String> enunciats,
            ArrayList<String> tipus, ArrayList<List<String>> opcions) throws Exception {
        ctrlDomini.crearEnquesta(titol);
        for (int i = 0; i < enunciats.size(); i++) {
            ctrlDomini.afegirPregunta(titol, enunciats.get(i), opcions.get(i), tipus.get(i));
        }
    }

    /**
     * Modifica una enquesta completa. Esborra l'enquesta original i crea una nova
     * amb els canvis fets.
     * 
     * @param titolOriginal El titol original de l'enquesta.
     * @param titolNou      El titol nou de l'enquesta.
     * @param enunciats     La llista d'enunciats de les preguntes.
     * @param tipus         La llista de tipus de les preguntes.
     * @param opcions       La llista d'opcions de les preguntes.
     * @throws Exception Si hi ha un error al modificar l'enquesta.
     */
    public void modificarEnquestaCompleta(String titolOriginal, String titolNou,
            ArrayList<String> enunciats, ArrayList<String> tipus,
            ArrayList<List<String>> opcions) throws Exception {
        // Esborrem l'antiga i creem la nova
        eliminarEnquesta(titolOriginal);
        crearEnquestaCompleta(titolNou, enunciats, tipus, opcions);
        seleccionarEnquesta(titolNou);
    }

    /**
     * Elimina una enquesta.
     * 
     * @param titol El titol de l'enquesta a eliminar.
     * @throws Exception Si hi ha un error al eliminar l'enquesta.
     */
    public void eliminarEnquesta(String titol) throws Exception {
        ctrlDomini.eliminarEnquesta(titol);
    }

    /**
     * Elimina l'enquesta seleccionada.
     * 
     * @throws Exception Si hi ha un error al eliminar l'enquesta.
     */
    public void eliminarEnquestaActual() throws Exception {
        if (nomEnquestaSeleccionada != null) {
            eliminarEnquesta(nomEnquestaSeleccionada);
            nomEnquestaSeleccionada = null;
        }
    }

    /**
     * Elimina la resposta de l'enquesta seleccionada.
     * 
     * @throws Exception Si hi ha un error al eliminar la resposta.
     */
    public void eliminarRespostaEnquesta() throws Exception {
        ctrlDomini.eliminarRespostaEnquesta(nomEnquestaSeleccionada);
    }

    /**
     * Importa una enquesta.
     * 
     * @param nomFitxer El nom del fitxer a importar.
     * @throws Exception Si hi ha un error al importar l'enquesta.
     */
    public void importarEnquesta(String nomFitxer) throws Exception {
        System.out.println("Important enquesta: " + nomFitxer);
        ctrlDomini.importarEnquesta(nomFitxer);
    }

    /**
     * Importa una resposta.
     * 
     * @param titol  El titol de l'enquesta.
     * @param fitxer El nom del fitxer a importar.
     * @throws Exception Si hi ha un error al importar la resposta.
     */
    public void importarResposta(String titol, String fitxer) throws Exception {
        ctrlDomini.importarResposta(titol, fitxer);
    }

    // --- GESTIÓ DE RESPOSTES (RESPONDRE) ---

    /**
     * Prepara la vista de respondre.
     * Prepara totes les preguntes de l'enquesta seleccionada i actualitza la vista
     * per a que es pugui respondre l'enquesta.
     */
    public void prepararVistaRespondre() {
        String titol = getNomEnquesta();
        if (titol == null)
            return;

        vistaEnquestaRespondre = vistaEnquestesPrincipal.getPanelRespondre();

        try {
            ArrayList<String> enunciats = getPreguntesEnquesta(titol);
            ArrayList<String> tipus = new ArrayList<>();
            ArrayList<ArrayList<String>> opcions = new ArrayList<>();

            for (int i = 0; i < enunciats.size(); i++) {
                tipus.add(getFormatPregunta(titol, i));
                opcions.add(new ArrayList<>(getOpcionsPregunta(titol, i)));
            }

            vistaEnquestaRespondre.configEnquesta(titol, enunciats, tipus, opcions);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error preparant vista: " + e.getMessage());
        }
    }

    /**
     * Envia la resposta. Implementa la lògica de sobreescriptura.
     */
    public void enviarResposta(String titolEnquesta, ArrayList<Object> respostes) throws Exception {
        if (usuariHaRespost()) {
            try {
                ctrlDomini.eliminarRespostaEnquesta(titolEnquesta);
            } catch (Exception e) {
                // No fem res
            }
        }

        ctrlDomini.crearRespostaEnquesta(titolEnquesta, respostes);
    }

    // --- MÈTODES DE SUPORT I CLUSTERING ---

    /**
     * Comprova si l'usuari ha respost a l'enquesta seleccionada.
     * 
     * @return true si l'usuari ha respost, false altrament.
     */
    public boolean usuariHaRespost() {
        try {
            return ctrlDomini.haRespostEnquesta(nomEnquestaSeleccionada);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Executa el clustering per a l'enquesta seleccionada.
     * 
     * @param selectedAlgIndex L'index del algoritme seleccionat.
     * @param kValue           El valor de k.
     * @return Un map amb els resultats del clustering.
     * @throws Exception Si hi ha un error al executar el clustering.
     */
    public Map<Integer, List<String>> executarClustering(int selectedAlgIndex, int kValue) throws Exception {
        return ctrlDomini.clusterAlgorithm(nomEnquestaSeleccionada, kValue, selectedAlgIndex);
    }

    /**
     * Retorna la silhouette de l'últim clustering.
     * 
     * @return La silhouette de l'últim clustering.
     */
    public double getUltimaSilhouette() {
        return ctrlDomini.getSilhouetteUltimClustering();
    }

    /**
     * Retorna la llista de representants del clustering.
     * 
     * @return La llista de representants del clustering.
     */
    public List<String> getRepresentantsCluster() {
        try {
            return ctrlDomini.getRepresentantsCluster();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Carrega una icona. Delega la càrrega al CtrlPresentacio general.
     * 
     * @param nom El nom de la icona.
     * @param w   La amplada de la icona.
     * @param h   L'alçada de la icona.
     * @return La icona carregada.
     */
    public ImageIcon carregarIcona(String nom, int w, int h) {
        return ctrlPresentacio.carregarIcona(nom, w, h);
    }
}