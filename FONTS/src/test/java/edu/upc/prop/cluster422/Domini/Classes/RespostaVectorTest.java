package edu.upc.prop.cluster422.Domini.Classes;

import org.junit.Test;
import java.util.Arrays;
import java.util.Vector;
import static org.junit.Assert.*;

/**
 * Tests unitaris per a la classe RespostaVector.
 */
public class RespostaVectorTest {

    private final double DELTA = 1e-9;

    private Vector<Integer> vec(Integer... valors) {
        return new Vector<>(Arrays.asList(valors));
    }

    /**
     * Test del càlcul de la distància local utilitzant l'algorisme de Jaccard.
     * Es comprova que la distància calculada és correcta segons l'algorisme
     */
    @Test
    public void testCalcularDistanciaJaccard() {
        RespostaPregunta r1 = new RespostaVector(vec(1, 2, 3));
        RespostaPregunta r2 = new RespostaVector(vec(3, 4, 5));
        assertEquals(0.8, r1.calcularDistanciaLocal(r2), DELTA);
    }
    
    /**
     * Test de distància entre respostes idèntiques.
     * Es comprova que la distància és zero quan les respostes són iguals
     */
    @Test
    public void testDistanciaIdentica() {
        RespostaPregunta r1 = new RespostaVector(vec(1, 2, 3));
        RespostaPregunta r2 = new RespostaVector(vec(1, 2, 3));
        assertEquals(0.0, r1.calcularDistanciaLocal(r2), DELTA);
    }
    
    /**
     * Test de distància entre respostes disjuntes.
     * Es comprova que la distància és 1.0 quan les respostes no tenen elements en comú.
     */
    @Test
    public void testDistanciaDisjunta() {
        RespostaPregunta r1 = new RespostaVector(vec(1, 2));
        RespostaPregunta r2 = new RespostaVector(vec(3, 4));
        assertEquals(1.0, r1.calcularDistanciaLocal(r2), DELTA);
    }
    
    /**
     * Test d'identificació de resposta buida.
     * Es comprova que el mètode esBuida funciona correctament.
     */
    @Test
    public void testEsBuida() {
        RespostaPregunta rBuida1 = new RespostaVector();
        RespostaPregunta rBuida2 = new RespostaVector(null);
        RespostaPregunta rPlena = new RespostaVector(vec(1));
        
        assertTrue("Una resposta per defecte hauria d'estar buida", rBuida1.esBuida());
        assertTrue("Una resposta amb vector nul hauria d'estar buida", rBuida2.esBuida());
        assertFalse("Una resposta amb valor no hauria d'estar buida", rPlena.esBuida());
    }
    
    /**
     * Test de distància amb resposta buida.
     * Es comprova que la distància és 1.0 en aquest cas.
     */
    @Test
    public void testDistanciaAmbBuida() {
        RespostaPregunta r1 = new RespostaVector(vec(1, 2));
        RespostaPregunta r2 = new RespostaVector(); // Buida
        assertEquals("Distància amb buida ha de ser 1.0", 1.0, r1.calcularDistanciaLocal(r2), DELTA);
    }

    /**
     * Test de distància amb ambdues respostes buides.
     * Es comprova que la distància és 0.0 en aquest cas.
     */
    @Test
    public void testDistanciaTotsDosBuits() {
        RespostaPregunta r1 = new RespostaVector(); // Buida
        RespostaPregunta r2 = new RespostaVector(); // Buida
        // Unió = 0. El codi ha de gestionar la divisió per zero.
        // El codi d'Enzo ho fa: if (tamUnio == 0) return 0.0;
        assertEquals(0.0, r1.calcularDistanciaLocal(r2), DELTA);
    }
}