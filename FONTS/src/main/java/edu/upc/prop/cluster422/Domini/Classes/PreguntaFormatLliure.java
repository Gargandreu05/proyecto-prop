package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una pregunta de format lliure.
 * Una pregunta de format lliure permet a l'usuari respondre amb qualsevol text.
 * 
 * @author Grup Guillem Revuelta
 * @version 17/11/2025/Entrega
 */

public class PreguntaFormatLliure extends Pregunta {

    /**
     * Les preguntes de format lliure no tenen opcions predefinides.
     * 
     * @return Una llista buida.
     */
    @Override
    public List<String> getOpcions() {
        return new ArrayList<>();
    }

    /**
     * Constructora per a preguntes de Format Lliure.
     * 
     * @param enunciat El text de la pregunta.
     * @throws CustomException Si els paràmetres base són invàlids.
     */
    public PreguntaFormatLliure(String enunciat) throws CustomException {
        super(enunciat);
    }

    /**
     * Retorna el tipus de pregunta en format string.
     * 
     * @return "LLIURE"
     */
    @Override
    public String tipusPregunta() {
        return "LLIURE";
    }

    /**
     * Crea una RespostaString a partir d'un objecte.
     * 
     * @param valor S'espera que sigui un objecte String.
     * @return Una RespostaString amb el valor.
     * @throws CustomException Si el valor no és un String.
     */
    @Override
    public RespostaPregunta crearResposta(Object valor) throws CustomException {
        if (valor == null) {
            // Permetem respostes buides (null)
            return new RespostaString(null);
        }
        if (!(valor instanceof String)) {
            throw new CustomException("El valor per a una pregunta de format lliure ha de ser un String.");
        }
        String text = (String) valor;
        CustomException.validarSenseCometes(text, "La resposta");
        return new RespostaString(text);
    }
}
