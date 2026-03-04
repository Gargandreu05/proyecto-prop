package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitaris per a la classe PreguntaFormatLliure.
 */
public class PreguntaFormatLliureTest {

    /**
     * Test del constructor amb paràmetres vàlids.
     * Es comprova que l'enunciat i el tipus de pregunta es configuren correctament.     */
    @Test
    public void testConstructorExit() throws CustomException {
        Pregunta p = new PreguntaFormatLliure("Enunciat de prova");
        assertEquals("Enunciat de prova", p.getEnunciat());
        assertEquals("LLIURE", p.tipusPregunta());
    }

    /**
     * Test del constructor amb enunciat buit.
     * Es comprova que es llença una excepció quan l'enunciat és buit.     */
    @Test(expected = CustomException.class)
    public void testConstructorEnunciatBuit() throws CustomException {
        new PreguntaFormatLliure("   ");
    }

    /**
     * Test de creació de resposta amb text vàlid.
     * Es comprova que la resposta creada és del tipus correcte i conté el text esper     */
    @Test
    public void testCrearRespostaCorrecta() throws CustomException {
        Pregunta p = new PreguntaFormatLliure("Enunciat");
        RespostaPregunta rp = p.crearResposta("Text de resposta");
        
        assertTrue(rp instanceof RespostaString);
        assertEquals("Text de resposta", ((RespostaString) rp).getEntrega());
    }
    
    /**
     * Test de creació de resposta amb valor null.
     * Es comprova que la resposta creada és del tipus correcte i conté null.     */
    @Test
    public void testCrearRespostaNull() throws CustomException {
        Pregunta p = new PreguntaFormatLliure("Enunciat");
        RespostaPregunta rp = p.crearResposta(null); // Permès
        
        assertTrue(rp instanceof RespostaString);
        assertNull(((RespostaString) rp).getEntrega());
    }

    /**
     * Test de creació de resposta amb tipus incorrecte.
     * Es comprova que es llença una excepció quan el tipus de resposta no és String.     */
    @Test(expected = CustomException.class)
    public void testCrearRespostaTipusIncorrecte() throws CustomException {
        Pregunta p = new PreguntaFormatLliure("Enunciat");
        p.crearResposta(123); // Ha de fallar, espera String
    }
}