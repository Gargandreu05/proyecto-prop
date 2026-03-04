package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

/**
 * Test unitari per a la classe PreguntaFormatUnica.
 * Prova la creació de preguntes i respostes, així com la gestió d'errors.
 */
public class PreguntaFormatUnicaTest {

    private List<String> opcions;
    private PreguntaFormatUnica p;

    /**
     * Configuració inicial per als tests.     */
    @Before
    public void setUp() throws CustomException {
        opcions = Arrays.asList("Opció A", "Opció B", "Opció C");
        p = new PreguntaFormatUnica("Tria una", opcions);
    }

    /**
     * Test del constructor amb paràmetres vàlids.
     * Es comprova que l'enunciat, el tipus de pregunta i les opcions es configuren correctament.     */
    @Test
    public void testConstructorExit() {
        assertEquals("Tria una", p.getEnunciat());
        assertEquals("UNIC", p.tipusPregunta());
        assertEquals(3, p.getOpcions().size());
    }

    /**
     * Test del constructor amb opcions buides.
     * Es comprova que es llença una excepció quan la llista d'opcions està buida.     */
    @Test(expected = CustomException.class)
    public void testConstructorOpcionsBuides() throws CustomException {
        new PreguntaFormatUnica("Tria una", new ArrayList<>());
    }
    
    /**
     * Test de creació de resposta correcta.
     * Es comprova que la resposta creada és del tipus correcte i conté l'índex esperat.     */
    @Test
    public void testCrearRespostaCorrecta() throws CustomException {
        RespostaPregunta rp = p.crearResposta(1); // Tria "Opció B"
        assertTrue(rp instanceof RespostaInteger);
        assertEquals(Integer.valueOf(1), ((RespostaInteger) rp).getEntrega());
    }

    /**
     * Test de creació de resposta amb índex fora de rang.
     * Es comprova que es llença una excepció quan l'índex proporcionat no és vàlid.     */
    @Test(expected = CustomException.class)
    public void testCrearRespostaIndexForaDeRang() throws CustomException {
        p.crearResposta(3); // Error, índexs vàlids són 0, 1, 2
    }

    /**
     * Test de creació de resposta amb tipus incorrecte.
     * Es comprova que es llença una excepció quan el tipus de resposta no és Integer.     */
    @Test(expected = CustomException.class)
    public void testCrearRespostaTipusIncorrecte() throws CustomException {
        p.crearResposta("Opció A"); // Error, ha de passar l'índex (Integer)
    }
}