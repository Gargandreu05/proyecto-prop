package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una pregunta de format numèric (l'usuari introdueix un número).
 * 
 * @author Guillem Revuelta
 * @version 17/11/2025/Entrega
 */

public class PreguntaFormatNumeric extends Pregunta {

    /**
     * Constructora per a preguntes de Format Numèric.
     * @param enunciat El text de la pregunta.
     * @throws CustomException Si els paràmetres base són invàlids.
     */
    public PreguntaFormatNumeric(String enunciat) throws CustomException {
        super(enunciat);
    }

    /**
     * Les preguntes de format numèric no tenen opcions predefinides.
     * @return Una llista buida.
     */
    @Override
    public List<String> getOpcions() {
        return new ArrayList<>();
    }

    /**
     * Retorna el tipus de la pregunta en format string.
     * @return "NUMERIC"
     */
    @Override
    public String tipusPregunta() {
        return "NUMERIC";
    }

    /**
     * Crea una RespostaInteger a partir d'un objecte.
     * @param valor S'espera que sigui un objecte Integer.
     * @return Una RespostaInteger amb el valor.
     * @throws CustomException Si el valor no és un Integer.
     */
    @Override
    public RespostaPregunta crearResposta(Object valor) throws CustomException {
        if (valor == null) {
            // Permetem respostes buides (null)
            return new RespostaInteger(null);
        }
        if (!(valor instanceof Integer)) {
            throw new CustomException("El valor per a una pregunta de format numèric ha de ser un Integer.");
        }
        return new RespostaInteger((Integer) valor);
    }
}
