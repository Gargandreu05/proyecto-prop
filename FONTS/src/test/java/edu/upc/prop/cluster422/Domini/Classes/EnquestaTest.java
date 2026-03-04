package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

/**
 * Tests unitaris per a la classe Enquesta.
 * S'utilitza Mockito per simular les dependències (Usuari, Pregunta,
 * RespostaEnquesta).
 */
public class EnquestaTest {

    private Enquesta enquesta;

    @Mock
    private Usuari mockCreador;
    @Mock
    private Usuari mockResponedor;
    @Mock
    private Pregunta mockPregunta1;
    @Mock
    private RespostaEnquesta mockResposta1;

    /**
     * Setup inicial abans de cada test.
     * Crea una instància d'Enquesta i inicialitza els mocks.
     */
    @Before
    public void setUp() throws CustomException {
        MockitoAnnotations.openMocks(this);

        // Definim el comportament bàsic dels mocks
        when(mockCreador.getUsername()).thenReturn("creadorTest");
        when(mockResponedor.getUsername()).thenReturn("responedorTest");
        when(mockResposta1.getUsuari()).thenReturn(mockResponedor);
        when(mockResposta1.getRespostaAPregunta()).thenReturn(new ArrayList<RespostaPregunta>());

        enquesta = new Enquesta("Enquesta de Prova", mockCreador, 1);
    }

    /**
     * Test del constructor d'Enquesta.
     * Comprova que els atributs s'inicialitzen correctament.
     */
    @Test
    public void testConstructorExit() {
        assertEquals("Enquesta de Prova", enquesta.getTitol());
        assertEquals(mockCreador, enquesta.getCreador());
        assertEquals(0, enquesta.getNumPreguntes().intValue());
        assertEquals(0, enquesta.getNumRespostes().intValue());
    }

    /**
     * Test del constructor d'Enquesta amb títol buit o creador null.
     * Dona lloc a una excepció.
     */
    @Test(expected = CustomException.class)
    public void testConstructorTitolBuit() throws CustomException {
        new Enquesta("", mockCreador, 2);
    }

    /**
     * Test del constructor d'Enquesta amb creador null.
     * Dona lloc a una excepció.
     */
    @Test(expected = CustomException.class)
    public void testConstructorCreadorNull() throws CustomException {
        new Enquesta("Títol Vàlid", null, 2);
    }

    /**
     * Test d'afegir una pregunta amb èxit.
     */
    @Test
    public void testAfegeixPreguntaExit() throws CustomException {
        enquesta.afegeixPregunta(mockPregunta1);
        assertEquals(1, enquesta.getNumPreguntes().intValue());
        assertTrue(enquesta.getPreguntes().contains(mockPregunta1));
    }

    /**
     * Test d'afegir una pregunta null.
     * Dona lloc a una excepció.
     */
    @Test(expected = CustomException.class)
    public void testAfegeixPreguntaNull() throws CustomException {
        enquesta.afegeixPregunta(null);
    }

    /**
     * Test d'afegir una pregunta duplicada.
     * Dona lloc a una excepció.
     */
    @Test(expected = CustomException.class)
    public void testAfegeixPreguntaDuplicada() throws CustomException {
        enquesta.afegeixPregunta(mockPregunta1);
        enquesta.afegeixPregunta(mockPregunta1);
    }

    /**
     * Test d'afegir una resposta amb èxit.
     */
    @Test
    public void testAfegirRespostaExit() throws CustomException {
        enquesta.afegirResposta(mockResposta1);

        assertEquals(1, enquesta.getNumRespostes().intValue());
        assertTrue(enquesta.getRespostesEnquesta().containsKey("responedorTest"));
        // assertTrue(enquesta.DirtyFlag); no hi podem accedir directament
    }

    /**
     * Test d'afegir una resposta null.
     * Dona lloc a una excepció.
     */
    @Test(expected = CustomException.class)
    public void testAfegirRespostaDuplicada() throws CustomException {
        enquesta.afegirResposta(mockResposta1);
        enquesta.afegirResposta(mockResposta1); // Mateix usuari
    }

    /**
     * Test d'eliminar una resposta amb èxit.
     */
    @Test
    public void testEliminarRespostaEnquesta() throws CustomException {
        enquesta.afegirResposta(mockResposta1);
        assertEquals(1, enquesta.getNumRespostes().intValue());

        enquesta.eliminarRespostaEnquesta(mockResposta1);
        assertEquals(0, enquesta.getNumRespostes().intValue());
        assertFalse(enquesta.getRespostesEnquesta().containsKey("responedorTest"));
    }

    /**
     * Test d'eliminar una resposta no existent.
     * Dona lloc a una excepció.
     */
    @Test(expected = CustomException.class)
    public void testAfegeixPreguntaAmbRespostes() throws CustomException {
        enquesta.afegirResposta(mockResposta1);
        enquesta.afegeixPregunta(mockPregunta1);
    }

    /**
     * Test d'eliminar una pregunta amb respostes existents.
     * Dona lloc a una excepció.
     */
    @Test(expected = CustomException.class)
    public void testEliminaPreguntaAmbRespostes() throws CustomException {
        enquesta.afegeixPregunta(mockPregunta1);
        enquesta.afegirResposta(mockResposta1);
        enquesta.eliminaPregunta(mockPregunta1);
    }

    /**
     * Test de l'obtenció de la matriu de respostes amb DirtyFlag.
     */
    @Test
    public void testGetMatriuRespostesAmbDirtyFlag() throws CustomException {
        enquesta.afegeixPregunta(mockPregunta1);
        enquesta.afegirResposta(mockResposta1); // Posa el DirtyFlag a true

        when(mockResposta1.getRespostaAPregunta()).thenReturn(new ArrayList<>(List.of(new RespostaString("test"))));

        ArrayList<ArrayList<RespostaPregunta>> matriu = enquesta.getMatriuRespostes();

        assertEquals("La matriu hauria de tenir 1 fila", 1, matriu.size());
        assertEquals("La fila hauria de tenir 1 columna", 1, matriu.get(0).size());

        ArrayList<ArrayList<RespostaPregunta>> matriu2 = enquesta.getMatriuRespostes();
        assertEquals("La matriu de cache no coincideix", 1, matriu2.size());
    }
}