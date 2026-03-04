package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una pregunta de resposta única, on l'usuari tria una opció d'una
 * llista.
 * 
 * @author Guillem Revuelta
 * @version 17/11/2025/Entrega
 */

public class PreguntaFormatUnica extends Pregunta {

    private List<String> opcions;

    /**
     * Constructora per a preguntes de Resposta Única.
     * 
     * @param enunciat El text de la pregunta.
     * @param opcions  La llista d'opcions possibles. No pot ser nul·la ni buida.
     * @throws CustomException Si els paràmetres base són invàlids o si la llista
     *                         d'opcions és nul·la o buida.
     */

    public PreguntaFormatUnica(String enunciat, List<String> opcions) throws CustomException {
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
     * @return "UNIC"
     */
    @Override
    public String tipusPregunta() {
        return "UNIC";
    }

    /**
     * Crea una RespostaInteger a partir d'un objecte.
     * 
     * @param valor S'espera que sigui un Integer (l'índex de l'opció triada).
     * @return Una RespostaInteger amb l'índex.
     * @throws CustomException Si el valor no és un Integer o l'índex està fora de
     *                         rang.
     */
    @Override
    public RespostaPregunta crearResposta(Object valor) throws CustomException {
        if (valor == null) {
            // Permetem respostes buides (null)
            return new RespostaInteger(null);
        }
        if (!(valor instanceof Integer)) {
            throw new CustomException(
                    "El valor per a una pregunta de format únic ha de ser un Integer (l'índex de l'opció).");
        }
        Integer index = (Integer) valor;
        if (index < 0 || index >= this.getOpcions().size()) {
            throw new CustomException("L'índex de la resposta (" + index + ") està fora de rang. Ha d'estar entre 0 i "
                    + (this.getOpcions().size() - 1) + ".");
        }
        return new RespostaInteger(index);
    }
}
