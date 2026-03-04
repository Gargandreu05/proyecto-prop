package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa una enquesta.
 * Conté preguntes i informació de qui l'ha creat i qui l'ha respost.
 * Conté també totes les funcions necessàries per operar amb enquestes i després
 * per dur a terme el clustering.
 * 
 * @author Andreu Puerto
 * @version 17/11/2025/Entrega
 */
public class Enquesta {

    // ---- ATRIBUTS ----
    /**
     * Títol de l'enquesta.
     */
    private String titol;

    /**
     * Identificador de l'usuari creador de l'enquesta.
     */
    private final Integer creador;

    /**
     * Identificador únic de l'enquesta.
     */
    private final int id;

    /**
     * ArrayList de preguntes.
     * Conté les preguntes que formen part de l'enquesta.
     */
    private ArrayList<Pregunta> preguntes;

    /**
     * Map de respostes.
     * Conté totes les respostes dels diferents usuaris.
     */
    private Map<Integer, RespostaEnquesta> respostes;

    /**
     * Llista de la matriu de respostes, per enviar-la al algorisme de clustering.
     * Aquesta matriu està definida amb un vector de vectors de RespostesPregunta és
     * a dir que cada fila "i" tindrà totes les respostes per cada usuari diferent,
     * mentre que les columnes "j" representen una pregunta de l'enquesta.
     * Conté un dirty bit flag que ens diu si la matriu està actualitzada o no. En
     * cada inserció o esborrat de resposta, s'haurà d'actualitzar la matriu abans
     * d'utilitzar-la.
     */
    private boolean DirtyFlag;
    private ArrayList<ArrayList<RespostaPregunta>> MatriuRespostes;

    // ---- CONSTRUCTORA ----

    /**
     * Constructora principal d'una enquesta.
     * Dona un títol i un usuari creador es registra l'enquesta a l'usuari.
     * 
     * @param titol   Títol de l'enquesta. No pot ser buit.
     * @param creador Usuari que crea l'enquesta.
     * @throws CustomException Si el títol és nul o buit.
     */
    public Enquesta(String titol, int creador, int id) throws CustomException {
        if (titol == null || titol.trim().isEmpty())
            throw new CustomException("El títol de l'enquesta no pot ser buit.");
        CustomException.validarSenseCometes(titol, "El títol de l'enquesta");
        this.titol = titol;
        this.creador = creador;
        this.preguntes = new ArrayList<>();
        this.respostes = new HashMap<>();
        this.MatriuRespostes = new ArrayList<>();
        this.DirtyFlag = false;
        this.id = id;
    }

    // ---- SETTERS ----

    /**
     * Afegeix una nova pregunta a l'enquesta.
     *
     * @param p La pregunta a afegir.
     * @throws CustomException Si la pregunta ja existeix o és nul·la, o ja hi han
     *                         usuaris que l'han respost.
     */
    public void afegeixPregunta(Pregunta p) throws CustomException {
        if (p == null)
            throw new CustomException("No es pot afegir una pregunta nul·la.");
        if (preguntes.contains(p))
            throw new CustomException("Aquesta pregunta ja existeix a l'enquesta.");
        if (!respostes.isEmpty())
            throw new CustomException("L'enquesta ja té respostes fetes.");
        this.preguntes.add(p);
    }

    /**
     * Canvia el títol de l'enquesta.
     * 
     * @param titol Títol de l'enquesta. No pot ser buit.
     * @throws CustomException Si el títol és nul o buit.
     */
    public void setTitol(String titol) throws CustomException {
        if (titol.isBlank())
            throw new CustomException("El títol de l'enquesta no pot ser buit.");
        CustomException.validarSenseCometes(titol, "El títol de l'enquesta");
        this.titol = titol;
    }

    /**
     * Estableix la llista de respostes de l'enquesta.
     * 
     * @param llista Mapa amb les respostes a establir.
     */
    public void setResposta(HashMap<Integer, RespostaEnquesta> llista) {
        respostes = llista;
        this.DirtyFlag = true;
    }

    // ---- GETTERS ----

    /**
     * S'obté la llista de preguntes de aquella enquesta.
     * 
     * @return Una llista amb totes les preguntes de l'enquesta. Es tracta d'una
     *         còpia de l'arraylist.
     */
    public ArrayList<Pregunta> getPreguntes() {
        return new ArrayList<>(this.preguntes);
    }

    /**
     * S'obté la llista d'enunciats de les preguntes de l'enquesta.
     * 
     * @return Una llista amb els enunciats de totes les preguntes de l'enquesta.
     */
    public ArrayList<String> getEnunciatPreguntes() {
        ArrayList<String> enunciats = new ArrayList<>();
        for (Pregunta p : this.preguntes) {
            enunciats.add(p.getEnunciat());
        }
        return enunciats;
    }

    /**
     * Retorna un mapa de totes les respostes dels diferents usuaris a l'enquesta.
     * Ho converteix perquè enlloc de l'id
     * retorni el username, això és correcte ja que mentre es fa el clustering no es
     * podrà canviar el nom d'usuari
     * 
     * @return Un mapa amb objectes RespostaEnquesta de tots els usuaris que han
     *         respost. Es tracta d'una còpia del mapa.
     */
    public Map<Integer, RespostaEnquesta> getRespostesEnquesta() {
        Map<Integer, RespostaEnquesta> result = new HashMap<>();
        for (Map.Entry<Integer, RespostaEnquesta> entry : respostes.entrySet()) {
            result.put(entry.getValue().getUsuari(), entry.getValue());
        }
        return result;
    }

    /**
     * Retorna una llista de les respostes dels diferents usuaris a l'enquesta.
     * 
     * @return Una llista amb objectes RespostaEnquesta de tots els usuaris que han
     *         respost. Es tracta d'una còpia del mapa.
     */
    public ArrayList<RespostaEnquesta> getRespostesEnquestaComAList() {
        return new ArrayList<>(this.respostes.values());
    }

    /**
     * Retorna el títol de l'enquesta.
     * 
     * @return Títol de l'enquesta.
     */
    public String getTitol() {
        return this.titol;
    }

    /**
     * Retorna l'usuari creador de l'enquesta.
     * 
     * @return Objecte Usuari creador d'aquella enquesta.
     */
    public Integer getCreador() {
        return this.creador;
    }

    /**
     * Consulta el nombre total de preguntes que té l'enquesta.
     * 
     * @return Retorna el nombre de preguntes que hi han a l'enquesta.
     */
    public Integer getNumPreguntes() {
        return this.preguntes.size();
    }

    /**
     * Consulta el nombre total de respostes que s'han fet.
     * 
     * @return Retorna el nombre de respostes que diferents usuaris han fet.
     */
    public Integer getNumRespostes() {
        return this.respostes.size();
    }

    /**
     * Retorna la resposta d'un usuari a l'enquesta.
     * 
     * @param p L'usuari del qual es vol obtenir la resposta.
     * @throws CustomException Si l'usuari és nul o no ha respost l'enquesta.
     */
    public RespostaEnquesta getRespostaEnquestaUsuari(Usuari p) throws CustomException {
        if (p == null)
            throw new CustomException("L'usuari no és vàlid.");
        Integer userID = p.getId();
        if (!respostes.containsKey(userID)) {
            throw new CustomException("L'usuari " + userID + " no ha respost aquesta enquesta.");
        }
        return respostes.get(userID);
    }

    // ---- ALTRES ----

    /**
     * Elimina una pregunta de l'enquesta. Per eliminar una pregunta, l'enquesta no
     * pot tenir respostes.
     * 
     * @param p La pregunta a eliminar.
     * @throws CustomException Si la pregunta no existeix a l'enquesta o l'enquesta
     *                         ja té respostes.
     */
    public void eliminaPregunta(Pregunta p) throws CustomException {
        if (!respostes.isEmpty()) { // es podria eliminar aquesta comprovació i fer que s'eliminin les respostes
                                    // d'aquelles preguntes eliminades
            throw new CustomException("L'enquesta ja té respostes.");
        }
        if (!this.preguntes.remove(p)) {
            throw new CustomException("La pregunta no es troba en aquesta enquesta.");
        }
    }

    /**
     * Elimina la pregunta en la posició indicada de l'enquesta.
     * 
     * @param index Índex de la pregunta a eliminar.
     */
    public void eliminarPregunta(int index) {
        this.preguntes.remove(index);
    }

    /**
     * Elimina la resposta d'un usuari a l'enquesta (Elimina la relació d'enquesta
     * amb resposta).
     * 
     * @param re RespostaEnquesta a eliminar.
     */
    public void eliminarRespostaEnquesta(RespostaEnquesta re) {
        DirtyFlag = true;
        respostes.remove(re.getUsuari());
    }

    /**
     * Retorna la Matriu de respostes actualitzada i preparada per a fer el
     * clustering.
     * Aquesta matriu es una representació de totes les respostes fetes a
     * l'enquesta.
     * 
     * @return Una matriu d'objectes MatriuResposta, feta amb totes les respostes
     *         d'aquella enquesta.
     */
    public ArrayList<ArrayList<RespostaPregunta>> getMatriuRespostes() {
        if (this.DirtyFlag) {
            updateMatriuRespostes();
        }
        return this.MatriuRespostes;
    }

    /**
     * Retorna el format de la pregunta en l'índex indicat. L'índex ha de ser vàlid.
     * 
     * @param index Índex de la pregunta.
     * @return String que representa el format de la pregunta.
     * @throws IndexOutOfBoundsException Si l'índex és invàlid.
     */
    public String getFormatPregunta(int index) throws IndexOutOfBoundsException {
        return this.preguntes.get(index).tipusPregunta();
    }

    /**
     * Actualitza els valors de la Matriu de respostes i posa el DirtyFlag a false.
     * Prepara la matriu per ser utilitzada en l'algorisme de clustering.
     */
    private void updateMatriuRespostes() {
        this.MatriuRespostes = new ArrayList<>();
        for (RespostaEnquesta re : respostes.values()) {
            List<RespostaPregunta> Listrp = re.getRespostaAPregunta();
            ArrayList<RespostaPregunta> elements = new ArrayList<>();
            for (RespostaPregunta rp : Listrp) {
                elements.add(rp);
            }
            MatriuRespostes.add(elements);
        }
        this.DirtyFlag = false;
    }

    /**
     * Retorna les opcions d'una pregunta.
     * L'index ha de ser vàlid i la pregunta ha de ser de tipus tancada.
     * 
     * @param numPregunta Índex de la pregunta.
     * @return Llista de Strings amb les opcions de la pregunta o buit.
     * @throws IndexOutOfBoundsException Si l'índex és invàlid.
     */
    public List<String> getOpcionsPregunta(int numPregunta) throws IndexOutOfBoundsException {
        return preguntes.get(numPregunta).getOpcions();
    }

    /**
     * Retorna el id unic de l'enquesta
     * 
     * @return id de l'enquesta
     */
    public int getId() {
        return this.id;
    }

    // es podria afegir com una "cache" de instruccions fetes per agilitzar la
    // actualitzacio de la matriurespostes:
    // Es podrien posar dues instruccions add num o del num on num representa líndex
    // de la resposta a afegir o eliminar, així resulta més simple tot.
    // Altra idea: que la matriu només s'actualitzi si es borra una resposta, ja que
    // al afegir es pot afegir paral·lelament a la MatriuRespostes

}