package edu.upc.prop.cluster422.Domini.Classes; // O .Respostes, segons la teva estructura

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests unitaris per a la classe RespostaString.
 */
public class RespostaStringTest {

    private final double DELTA = 1e-9;

    /**
     * Test del càlcul de la distància local utilitzant l'algorisme de Levenshtein.
     * Es comprova que la distància calculada és correcta segons l'algorisme.
     */
    @Test
    public void testCalcularDistanciaLocalLevenshtein() throws CustomException {
        RespostaPregunta r1 = new RespostaString("kitten");
        RespostaPregunta r2 = new RespostaString("sitting");
        assertEquals(3.0 / 7.0, r1.calcularDistanciaLocal(r2), DELTA);
    }

    /**
     * Test de distància entre respostes idèntiques.
     * Es comprova que la distància és zero quan les respostes són iguals
     */
    @Test
    public void testDistanciaIdentica() throws CustomException {
        RespostaPregunta r1 = new RespostaString("test");
        RespostaPregunta r2 = new RespostaString("test");
        assertEquals(0.0, r1.calcularDistanciaLocal(r2), DELTA);
    }

    /**
     * Test de distància amb resposta buida o tipus incompatible.
     * Es comprova que la distància és 1.0 en aquests casos.
     */
    @Test
    public void testDistanciaAmbBuida() throws CustomException {
        RespostaPregunta r1 = new RespostaString("test");
        RespostaPregunta r2 = new RespostaString(); // Buida (null)
        assertEquals("Distància amb buida ha de ser 1.0", 1.0, r1.calcularDistanciaLocal(r2), DELTA);
    }

    /**
     * Test de distància amb resposta buida (amb espais).
     * Es comprova que la distància és 1.0 en aquest cas.
     */
    @Test
    public void testDistanciaAmbBuidaTrim() throws CustomException {
        RespostaPregunta r1 = new RespostaString("test");
        RespostaPregunta r2 = new RespostaString("   "); // Buida (amb espais)
        assertEquals("Distància amb buida (amb espais) ha de ser 1.0", 1.0, r1.calcularDistanciaLocal(r2), DELTA);
    }

    /**
     * Test de distància amb tipus incompatible.
     * Es comprova que la distància és 1.0 en aquest cas.
     */
    @Test
    public void testDistanciaAmbIncompatible() throws CustomException {
        RespostaPregunta r1 = new RespostaString("test");
        RespostaPregunta r2 = new RespostaInteger(10); // Tipus incompatible
        assertEquals("Distància amb incompatible ha de ser 1.0", 1.0, r1.calcularDistanciaLocal(r2), DELTA);
    }

    /**
     * Test de modificació de resposta amb èxit.
     * Es comprova que la resposta s'ha modificat correctament.
     */
    @Test
    public void testModificarRespostaExit() throws CustomException {
        RespostaPregunta r = new RespostaString("Valor Vell");
        r.modificarResposta("Valor Nou");
        // Comprovem que s'hagi canviat internament
        assertEquals("Valor Nou", ((RespostaString) r).getEntrega());
    }

    /**
     * Test de modificació de resposta amb tipus incorrecte.
     * Es comprova que es llença una excepció CustomException.
     */
    @Test
    public void testModificarRespostaTipusIncorrecte() throws CustomException {
        RespostaPregunta r = new RespostaString("Valor Vell");

        try {
            r.modificarResposta(12345);
            fail("S'esperava una CustomException, però no s'ha llançat.");
        } catch (CustomException e) {
            assertEquals("Tipus de dada invàlid per a RespostaString.", e.getMessage());
        }
    }
}