package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitaris per a la classe RespostaInteger.
 */
public class RespostaIntegerTest {

    private final double DELTA = 1e-9;
    
    /**
     * Test del constructor amb paràmetres vàlids.
     * Es comprova que el valor s'ha assignat correctament.     */
    @Test
    public void testCalcularDistanciaLocal() {
        RespostaPregunta r1 = new RespostaInteger(10);
        RespostaPregunta r2 = new RespostaInteger(5);
        assertEquals(5.0, r1.calcularDistanciaLocal(r2), DELTA);
    }

    /**
     * Test del constructor amb valor null.
     * Es comprova que la resposta es considera buida.     */
    @Test
    public void testModificarRespostaTipusIncorrecte() {
        RespostaPregunta r = new RespostaInteger(10);
        
        try {
            r.modificarResposta("un string");
            fail("S'esperava una CustomException, però no s'ha llançat.");
            
        } catch (CustomException e) {
            assertEquals("La nova dada ha de ser un Integer vàlid.", e.getMessage());
        }
    }
    /**
     * Test del càlcul de puntuació.
     * Es comprova que la puntuació retornada és correcta.     */
    @Test
    public void testModificarRespostaCorrecte() throws CustomException{
        RespostaPregunta r = new RespostaInteger(10);
        r.modificarResposta(20);
        assertEquals(20.0, r.TreurePuntuacio(), DELTA);
    }
    /**
     * Test de modificació de resposta a null.
     * Es comprova que la resposta es considera buida després de la modificació.     */
    @Test 
    public void testModificarRespostaNull() throws CustomException {
        RespostaPregunta r = new RespostaInteger(10);
        assertFalse(r.esBuida()); // Comprovem que inicialment NO està buida
        r.modificarResposta(null); 
        assertTrue(r.esBuida()); 
    }
}