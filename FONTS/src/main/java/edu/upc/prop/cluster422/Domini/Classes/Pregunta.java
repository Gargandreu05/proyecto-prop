package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

import java.util.List;

/**
 * Representa una pregunta abstracta dins d'una enquesta.
 * Defineix la funcionalitat bàsica de totes les preguntes.
 * 
 * @author Guillem Revuelta
 * @version 17/11/2025/Entrega
 */

public abstract class Pregunta {

    // ---- ATRIBUTS ----

    private String enunciat;

    // ---- CONSTRUCTORA ----

    /**
     * Constructora de la classe abstracta Pregunta.
     * 
     * @param enunciat El text de la pregunta. No pot ser nul ni buit.
     * @throws CustomException Si l'enunciat és nul o buit.
     */

    public Pregunta(String enunciat) throws CustomException {
        if (enunciat == null || enunciat.trim().isEmpty()) {
            throw new CustomException("L'enunciat de la pregunta no pot ser buit.");
        }
        CustomException.validarSenseCometes(enunciat, "L'enunciat de la pregunta");
        this.enunciat = enunciat;
        // S'ha eliminat: this.respostes = new ArrayList<>();
    }

    // ---- SETTERS / MODIFICADORS ----

    /**
     * Modifica el text de l'enunciat de la pregunta.
     * 
     * @param nouEnunciat El nou text per a l'enunciat. No pot ser nul ni buit.
     * @throws CustomException Si el nou enunciat és nul o buit.
     */

    public void modificarPregunta(String nouEnunciat) throws CustomException {
        if (nouEnunciat == null || nouEnunciat.trim().isEmpty()) {
            throw new CustomException("L'enunciat de la pregunta no pot ser buit.");
        }
        CustomException.validarSenseCometes(nouEnunciat, "L'enunciat de la pregunta");
        this.enunciat = nouEnunciat;
    }

    // S'ha eliminat el mètode afegirResposta(RespostaPregunta resposta)

    // ---- GETTERS ----

    /**
     * Retorna l'enunciat de la pregunta.
     * 
     * @return El text de l'enunciat.
     */

    public String getEnunciat() {
        return enunciat;
    }

    // S'ha eliminat el mètode getRespostes()

    // ---- MÈTODES ABSTRACTES ----

    /**
     * Retorna el tipus de pregunta com a String (ex: "Resposta Única").
     * 
     * @return El tipus de pregunta.
     */

    public abstract String tipusPregunta();

    /**
     * Retorna la llista d'opcions disponibles per a aquesta pregunta.
     * 
     * @return Llista d'opcions (pot ser buida si no aplica).
     */
    public abstract List<String> getOpcions();

    /**
     * Crea un objecte RespostaPregunta adequat per a aquest tipus de pregunta,
     * validant i encapsulant el valor proporcionat.
     *
     * @param valor El valor de la resposta (tipus esperat depèn de la subclasse).
     * @return Un objecte RespostaPregunta (RespostaInteger, RespostaString,
     *         RespostaVector, etc.)
     * @throws CustomException Si el tipus de 'valor' és incorrecte o invàlid.
     */
    public abstract RespostaPregunta crearResposta(Object valor) throws CustomException;
}