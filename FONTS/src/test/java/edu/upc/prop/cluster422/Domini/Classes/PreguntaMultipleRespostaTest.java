package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import static org.junit.Assert.*;

/**
 * Test unitari per a la classe PreguntaMultipleResposta.
 * Prova la creació de preguntes i respostes, així com la gestió d'errors.
 */
public class PreguntaMultipleRespostaTest {

    private List<String> opcions;
    private PreguntaMultipleResposta p;

    /**
     * Configuració inicial per als tests.     */
    @Before
    public void setUp() throws CustomException {
        opcions = Arrays.asList("Opció A", "Opció B", "Opció C");
        p = new PreguntaMultipleResposta("Tria una o més", opcions);
    }

    /**
     * Test del constructor amb paràmetres vàlids.
     * Es comprova que l'enunciat, el tipus de pregunta i les opcions es configuren correctament.     */
    @Test
    public void testConstructorExit() {
        assertEquals("Tria una o més", p.getEnunciat());
        assertEquals("MULTIPLE", p.tipusPregunta());
    }

    /**
     * Test de creació de resposta correcta.
     * Es comprova que la resposta creada és del tipus correcte i conté els índexs esperats.
     */
    @Test
    public void testCrearRespostaCorrecta() throws CustomException {
        List<Integer> seleccio = Arrays.asList(0, 2); // Tria "Opció A" i "Opció C"
        RespostaPregunta rp = p.crearResposta(seleccio);
        
        assertTrue(rp instanceof RespostaVector);
        Vector<Integer> expected = new Vector<>(Arrays.asList(0, 2));
        assertEquals(expected, ((RespostaVector) rp).getEntrega());
    }

    /**
     * Test de creació de resposta amb índex fora de rang.
     * Es comprova que es llença una excepció quan algun índex proporcionat no és vàlid.     */
    @Test(expected = CustomException.class)
    public void testCrearRespostaIndexForaDeRang() throws CustomException {
        List<Integer> seleccio = Arrays.asList(0, 3); // 3 és invàlid
        p.crearResposta(seleccio);
    }

    /**
     * Test de creació de resposta amb tipus incorrecte.
     * Es comprova que es llença una excepció quan el tipus de resposta no és List<Integer>.     */
    @Test(expected = CustomException.class)
    public void testCrearRespostaTipusIncorrecte() throws CustomException {
        p.crearResposta(1); // Error, ha de passar una List
    }
    
    /**
     * Test de creació de resposta amb llista de tipus incorrecte.
     * Es comprova que es llença una excepció quan els elements de la llista no són Integer.     */
    @Test(expected = CustomException.class)
    public void testCrearRespostaLlistaTipusIncorrecte() throws CustomException {
        List<String> seleccio = Arrays.asList("Opció A"); // Error, ha de ser List<Integer>
        p.crearResposta(seleccio);
    }
}