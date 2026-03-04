package edu.upc.prop.cluster422.Domini.Classes;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaris per a la classe Usuari.
 * Utilitza Mocks per a les dependències Enquesta i RespostaEnquesta.
 */
public class UsuariTest {

    private Usuari usuari;

    @Mock private Enquesta mockEnquesta1;
    @Mock private Enquesta mockEnquesta2;
    @Mock private RespostaEnquesta mockResposta1;

    /**
     * Configuració inicial abans de cada test.
     * S'inicialitzen els Mocks i es defineix el comportament necessari.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        usuari = new Usuari("usuariValid", "passValid123", 1);

        // Definim el comportament dels Mocks
        when(mockEnquesta1.getTitol()).thenReturn("TitolEnquesta1");
        when(mockEnquesta2.getTitol()).thenReturn("TitolEnquesta2");
        when(mockEnquesta1.getId()).thenReturn(1);
        when(mockEnquesta2.getId()).thenReturn(2);

        
        // Simulem la navegabilitat de RespostaEnquesta -> Enquesta
        when(mockResposta1.getEnquesta()).thenReturn(mockEnquesta1);
    }

    /**
     * Test del constructor de la classe Usuari.
     * El nom d'usuari és null.
     * Dona lloc a una excepció.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNomUsuariNull() {
        new Usuari(null, "passValid123",2);
    }

    /**
     * Test del constructor de la classe Usuari.
     * L'usuari és massa curt.
     * Dona lloc a una excepció.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNomUsuariCurt() {
        new Usuari("u", "passValid123",2);
    }

    /**
     * Test del constructor de la classe Usuari.
     * La contrasenya és null.
     * Dona lloc a una excepció.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPasswordNull() {
        new Usuari("usuariValid", null,2);
    }

    /**
     * Test del constructor de la classe Usuari.
     * La contrasenya és massa curta.
     * Dona lloc a una excepció.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPasswordCurt() {
        new Usuari("usuariValid", "p",2);
    }

    /**
     * Test de modificació d'edat amb èxit.
     */
    @Test
    public void testSetEdatExit() {
        usuari.setEdat(30);
        assertEquals(30, usuari.getEdat());
    }

    /**
     * Test de modificació d'edat amb valor negatiu.
     * Dona lloc a una excepció.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetEdatNegativa() {
        usuari.setEdat(-5);
    }

    /**
     * Test de modificació d'edat amb valor massa gran.
     * Dona lloc a una excepció.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetEdatMassaGran() {
        usuari.setEdat(121);
    }

    /**
     * Test de modificació d'email amb èxit.
     */
    @Test
    public void testSetEmailExit() {
        usuari.setEmail("correu@valid.com");
        assertEquals("correu@valid.com", usuari.getEmail());
    }

    /**
     * Test de modificació d'email amb format invàlid.
     * Dona lloc a una excepció.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetEmailInvalid() {
        usuari.setEmail("correuInvalid.com");
    }

    /**
     * Test d'afegir una enquesta creada amb èxit.
     */
    @Test
    public void testAddEnquestaCreadaExit() throws Exception {
        usuari.addEnquestaCreada(mockEnquesta1);
        Integer[] titols = usuari.getEnquestesCreadesTitol();
        assertEquals(1, titols.length);
        assertEquals((Integer)1, titols[0]);
    }

    /**
     * Test d'afegir una enquesta creada duplicada.
     * Dona lloc a una excepció.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddEnquestaCreadaDuplicada() throws Exception {
        usuari.addEnquestaCreada(mockEnquesta1);
        usuari.addEnquestaCreada(mockEnquesta1); // Torna a afegir la mateixa
    }
    
    /**
     * Test d'afegir una resposta creada amb èxit.
     */
    @Test
    public void testAddRespostaCreadaExit() throws Exception {
        usuari.addRespostacreada(mockResposta1);
        Integer[] titols = usuari.getRespostesCreadesTitol();
        assertEquals(1, titols.length);
        assertEquals((Integer)1, titols[0]);
    }

    /**
     * Test d'afegir una resposta creada duplicada.
     * Dona lloc a una excepció.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddRespostaCreadaDuplicada() throws Exception {
        usuari.addRespostacreada(mockResposta1);
        usuari.addRespostacreada(mockResposta1); // Torna a afegir la mateixa
    }

    /**
     * Test d'esborrar una enquesta creada amb èxit.
    */
    @Test
    public void testDeleteEnquestaCreadaExit() throws Exception {
        usuari.addEnquestaCreada(mockEnquesta1);
        usuari.addEnquestaCreada(mockEnquesta2);
        
        usuari.deleteEnquestaCreada(mockEnquesta1); // Esborrem la 1
        
        Integer[] titols = usuari.getEnquestesCreadesTitol();
        assertEquals(1, titols.length);
        assertEquals((Integer)2, titols[0]); // Només ha de quedar la 2
    }

    /**
     * Test d'esborrar una enquesta creada inexistent.
     * Dona lloc a una excepció.
     */
    @Test(expected = NoSuchElementException.class)
    public void testDeleteEnquestaCreadaInexistent() throws Exception {
        usuari.addEnquestaCreada(mockEnquesta1);
        usuari.deleteEnquestaCreada(mockEnquesta2); // Intentem esborrar la 2, que no hi és
    }
}