package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitaris per a la classe PreguntaFormatNumeric.
 */
public class PreguntaFormatNumericTest {

    /**
     * Test del constructor amb paràmetres vàlids.
     * Es comprova que l'enunciat i el tipus de pregunta es configuren correctament.     */
    @Test
    public void testConstructorExit() throws CustomException {
        Pregunta p = new PreguntaFormatNumeric("Edat?");
        assertEquals("Edat?", p.getEnunciat());
        assertEquals("NUMERIC", p.tipusPregunta());
    }

    /**
     * Test del constructor amb enunciat buit.
     * Es comprova que es llença una excepció quan l'enunciat és buit.     */
    @Test
    public void testCrearRespostaCorrecta() throws CustomException {
        Pregunta p = new PreguntaFormatNumeric("Edat?");
        RespostaPregunta rp = p.crearResposta(25);
        
        assertTrue(rp instanceof RespostaInteger);
        assertEquals(Integer.valueOf(25), ((RespostaInteger) rp).getEntrega());
    }

    /**
     * Test de creació de resposta amb valor null.
     * Es comprova que la resposta creada és del tipus correcte i conté null.     */
    @Test
    public void testCrearRespostaNull() throws CustomException {
        Pregunta p = new PreguntaFormatNumeric("Edat?");
        RespostaPregunta rp = p.crearResposta(null); // Permès
        
        assertTrue(rp instanceof RespostaInteger);
        assertNull(((RespostaInteger) rp).getEntrega());
    }

    /**
     * Test de creació de resposta amb tipus incorrecte.
     * Es comprova que es llença una excepció quan el tipus de resposta no és Integer.     */
    @Test(expected = CustomException.class)
    public void testCrearRespostaTipusIncorrecte() throws CustomException {
        Pregunta p = new PreguntaFormatNumeric("Edat?");
        p.crearResposta("Vint-i-cinc"); // Ha de fallar, espera Integer
    }
}