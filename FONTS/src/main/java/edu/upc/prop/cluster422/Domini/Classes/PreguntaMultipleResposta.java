package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Representa una pregunta de resposta múltiple, on l'usuari pot triar una o més
 * opcions.
 * 
 * @author Guillem Revuelta
 * @version 17/11/2025/Entrega
 */

public class PreguntaMultipleResposta extends Pregunta {

    /**
     * Llista d'opcions disponibles per a la pregunta, son strings amb els valors
     * possibles.
     * La resposta de l'usuari serà una llista d'índexs corresponents a les opcions
     * triades.
     */
    private List<String> opcions;

    /**
     * Constructora per a preguntes de Resposta Múltiple.
     * 
     * @param enunciat El text de la pregunta.
     * @param opcions  La llista d'opcions possibles. No pot ser nul·la ni buida.
     * @throws CustomException Si els paràmetres base són invàlids o si la llista
     *                         d'opcions és nul·la o buida.
     */

    public PreguntaMultipleResposta(String enunciat, List<String> opcions) throws CustomException {
        super(enunciat);
        if (opcions == null || opcions.isEmpty()) {
            throw new CustomException("La llista d'opcions no pot ser nul·la ni buida.");
        }
        this.setOpcions(opcions);
    }

    /**
     * Estableix o modifica les opcions de la pregunta.
     * 
     * @param opcions La nova llista d'opcions.
     * @throws CustomException Si la llista d'opcions és invàlida.
     */
    private void setOpcions(List<String> opcions) throws CustomException {
        if (opcions == null || opcions.isEmpty()) {
            throw new CustomException("La llista d'opcions no pot ser nul·la ni buida.");
        }
        for (String opcio : opcions) {
            CustomException.validarSenseCometes(opcio, "L'opció de la pregunta");
        }
        this.opcions = new ArrayList<>(opcions);
    }

    /**
     * Retorna una còpia de la llista d'opcions.
     * 
     * @return Una llista de Strings amb les opcions.
     */
    public List<String> getOpcions() {
        return new ArrayList<>(this.opcions);
    }

    /**
     * Retorna el tipus de pregunta.
     * 
     * @return "MULTIPLE"
     */
    @Override
    public String tipusPregunta() {
        return "MULTIPLE";
    }

    /**
     * Crea una RespostaVector a partir d'un objecte.
     * 
     * @param valor S'espera que sigui una List<Integer> (els índexs de les opcions
     *              triades).
     * @return Una RespostaVector amb la llista d'índexs.
     * @throws CustomException Si el valor no és una List, conté no-Integers, o
     *                         algun índex està fora de rang.
     */
    @Override
    public RespostaPregunta crearResposta(Object valor) throws CustomException {
        if (valor == null) {
            // Permetem respostes buides (null)
            return new RespostaVector(null);
        }
        if (!(valor instanceof List)) {
            throw new CustomException("El valor per a una pregunta de resposta múltiple ha de ser una List.");
        }

        List<?> llistaValors = (List<?>) valor;
        Vector<Integer> indexos = new Vector<Integer>();
        int maxIndex = this.getOpcions().size() - 1;

        for (Object obj : llistaValors) {
            if (!(obj instanceof Integer)) {
                throw new CustomException(
                        "La llista de respostes ha de contenir només valors Integer (els índexs de les opcions).");
            }
            Integer index = (Integer) obj;
            if (index < 0 || index > maxIndex) {
                throw new CustomException("L'índex de la resposta (" + index
                        + ") està fora de rang. Ha d'estar entre 0 i " + maxIndex + ".");
            }
            if (!indexos.contains(index)) { // Evitar duplicats
                indexos.add(index);
            }
        }
        // Ordenar per consistència
        Collections.sort(indexos);
        return new RespostaVector(indexos);
    }
}
