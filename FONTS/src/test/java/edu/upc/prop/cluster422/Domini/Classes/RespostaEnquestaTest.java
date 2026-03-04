package edu.upc.prop.cluster422.Domini.Classes;

import edu.upc.prop.cluster422.Domini.Controladors.CustomException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test unitari per a la classe RespostaEnquesta.
 * Comprova que les dependències s'emmagatzemen i que la lògica de
 * construcció i esborrat (amb dependències circulars) es crida correctament.
 */
public class RespostaEnquestaTest {

    @Mock private Enquesta mockEnquesta;
    @Mock private Usuari mockUsuari;
    @Mock private RespostaPregunta mockRP1;
    @Mock private RespostaPregunta mockRP2;
    
    private List<RespostaPregunta> llistaRespostes;
    private RespostaEnquesta respostaEnquesta;

    /**
     * Configuració prèvia als tests.
     */
    @Before
    public void setUp() throws CustomException {
        MockitoAnnotations.openMocks(this);
        llistaRespostes = new ArrayList<>();
        llistaRespostes.add(mockRP1);
        // Configurem els mocks per a les crides del constructor
        doNothing().when(mockUsuari).addRespostacreada(any(RespostaEnquesta.class));
        doNothing().when(mockEnquesta).afegirResposta(any(RespostaEnquesta.class));
        
        // Ara podem crear l'objecte sense que falli
        respostaEnquesta = new RespostaEnquesta(mockEnquesta, mockUsuari, llistaRespostes);
    }

    /**
     * Test del constructor de RespostaEnquesta.
     * Comprova que les dependències s'emmagatzemen correctament
     */
    @Test
    public void testConstructorExit() throws CustomException {
        verify(mockUsuari, times(1)).addRespostacreada(respostaEnquesta);
        verify(mockEnquesta, times(1)).afegirResposta(respostaEnquesta);

        assertEquals(mockEnquesta, respostaEnquesta.getEnquesta());
        assertEquals(mockUsuari, respostaEnquesta.getUsuari());
        assertEquals(llistaRespostes, respostaEnquesta.getRespostaAPregunta());
        assertEquals(1, respostaEnquesta.getRespostaAPregunta().size());
    }

    /**
     * Test de l'esborrat de RespostaEnquesta.
     * Comprova que les dependències es netegen correctament.
     */
    @Test
    public void testEsborrarRespostaEnquesta() throws NoSuchElementException {
        // Configurem els mocks per a les crides d'esborrat
        doNothing().when(mockUsuari).deleteRespostacreada(any(RespostaEnquesta.class));
        doNothing().when(mockEnquesta).eliminarRespostaEnquesta(any(RespostaEnquesta.class));
        
        respostaEnquesta.esborrarRespostaEnquesta();

        // Verifiquem que s'han cridat els mètodes d'esborrat
        verify(mockUsuari, times(1)).deleteRespostacreada(respostaEnquesta);
        verify(mockEnquesta, times(1)).eliminarRespostaEnquesta(respostaEnquesta);
        
        // Verifiquem que la llista interna s'ha posat a null
        assertNull(respostaEnquesta.getRespostaAPregunta());
    }

    /**
     * Test de modificació de resposta a una pregunta.
     * Comprova que es crida el mètode correcte del RespostaPregunta.
     */
    @Test
    public void testModificarRespostaPregunta() throws CustomException {
        // Afegim un segon mock a la llista per provar l'índex
        respostaEnquesta.afegirRespostaPregunta(mockRP2);
        
        String novaResposta = "Nou text";
        
        // Cridem al mètode a provar
        respostaEnquesta.modificarRespostaPregunta(1, novaResposta); // Modifiquem la segona resposta

        // Verifiquem que s'ha cridat el mètode modificarResposta
        // de l'objecte RespostaPregunta CORRECTE (mockRP2)
        verify(mockRP2, times(1)).modificarResposta(novaResposta);
        // Verifiquem que NO s'ha cridat a l'altre
        verify(mockRP1, never()).modificarResposta(any());
    }
}