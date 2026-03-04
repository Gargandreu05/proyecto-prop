package edu.upc.prop.cluster422.Domini.Algorismes;

import edu.upc.prop.cluster422.Domini.Classes.*;
import edu.upc.prop.cluster422.Domini.Controladors.CustomException;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementació de l'algorisme K-Means seguint la interfície
 * StrategyClustering.
 * Aquest algorisme agrupa les respostes de l'enquesta en 'k' clústers.
 * Funciona calculant centroides sintètics (mitjanes/modes) per a cada clúster.
 *
 * @author Guillem Revuelta
 * @version 17/11/2025/Entrega
 */

public class Kmeans implements StrategyClustering {
    int maxIteracions = 100;
    Random rand = new Random();

    @Override
    public Map<Integer, Cluster> executa(Enquesta e, int k) throws CustomException {

        // ---- INICI I COMPROVACIONS ----
        /**
         * Matriu de respostes de l'enquesta, on cada fila representa una resposta
         * completa a l'enquesta.
         * Cada element dins de la fila és una RespostaPregunta corresponent a una
         * pregunta específica
         */
        ArrayList<ArrayList<RespostaPregunta>> MatriuRespostes = e.getMatriuRespostes();
        /**
         * Llista de Preguntes de l'enquesta.
         * Cada element de la llista és una Pregunta que forma part de l'enquesta.
         */
        ArrayList<Pregunta> Preguntes = e.getPreguntes();
        /**
         * Llista de RespostaEnquesta de l'enquesta.
         * Cada element de la llista és una RespostaEnquesta que representa una resposta
         * completa a l'enquesta.
         */
        ArrayList<RespostaEnquesta> Respostes = e.getRespostesEnquestaComAList();

        // Comprova que almenys es pugui posar una resposta per grup
        if (MatriuRespostes.size() < k)
            throw new CustomException("Dades Insuficients per al càlcul.");

        /**
         * Nombre total de respostes.
         */
        int numResp = MatriuRespostes.size();
        /**
         * Nombre total de preguntes.
         */
        int numPreg = Preguntes.size();

        // ---- INICIALITZACIÓ ----

        /**
         * Centroides dels clústers, on cada element és una llista d'objectes
         * representant les característiques del centre del clúster.
         */
        ArrayList<ArrayList<Object>> centroides = new ArrayList<>();
        /**
         * Llista que conté tots els índexos de les respostes, l'inicialitza amb tots
         * els disponibles i després fa un shuffle per a assegurar aleatorietat.
         */
        ArrayList<Integer> indexosDisponibles = new ArrayList<>();
        for (int i = 0; i < numResp; i++)
            indexosDisponibles.add(i);
        Collections.shuffle(indexosDisponibles, rand);

        /*
         * Assigna els primers 'k' centroides de manera aleatòria utilitzant les
         * respostes barrejades.
         */
        for (int i = 0; i < k; i++) {
            ArrayList<RespostaPregunta> resposta = MatriuRespostes.get(indexosDisponibles.get(i));
            centroides.add(convertirRealAVirtual(resposta));
        }
        /**
         * Contador d'iteracions per a limitar el màxim (fer que es rendeixi).
         */
        int it = 0;
        /**
         * Vector d'assignacions, per cada i representant una resposta de la
         * MatriuRespostes, el seu valor diu en quin cluster està (entre 0 i k).
         */
        int[] assignacions = new int[numResp];
        /**
         * Booleà que manté el bucle actiu mentres es facin canvis.
         */
        Boolean haCanviat = true;

        while (haCanviat && it < maxIteracions) {
            // ---- ASSIGNACIÓ DE CLÚSTERS ----
            // Per cada resposta, calcula la distància al centre de cada clúster i l'assigna
            // al més proper.
            for (int i = 0; i < numResp; i++) {
                Double minDist = Double.MAX_VALUE;
                int millorCluster = -1;
                // Per cada clúster, calcula la distància entre la resposta i el seu centre.
                for (int j = 0; j < k; j++) {
                    double distancia = calculaDistanciaGlobal(MatriuRespostes.get(i), centroides.get(j), Preguntes);
                    if (minDist > distancia) {
                        minDist = distancia;
                        millorCluster = j;
                    }
                }
                if (assignacions[i] != millorCluster) {
                    haCanviat = true;
                    assignacions[i] = millorCluster;
                }
            }
            // ---- ACTUALITZACIÓ DE CENTROIDES ----
            // Recalcula els centroides basant-se en les assignacions actuals. Només es duu
            // a terme si hi ha hagut canvis en les assignacions.
            if (haCanviat) {
                centroides = recalculaCentroides(MatriuRespostes, assignacions, k, numPreg, Preguntes);
            }
            // Afegim iteració
            it++;
        }
        return generaClusters(k, assignacions, Respostes, centroides);
    }

    /**
     * Calcula la distància global entre una llista de respostes i un centreide
     * donat.
     * 
     * @param respostes ArrayList de RespostaPregunta a comparar.
     * @param centroide ArrayList d'Object representant el centroide.
     * @param preguntes ArrayList de Pregunta corresponent a les preguntes de
     *                  l'enquesta.
     * @return Distància global mitjana entre les respostes i el centroide.
     */
    private double calculaDistanciaGlobal(ArrayList<RespostaPregunta> respostes, ArrayList<Object> centroide,
            ArrayList<Pregunta> preguntes) {
        double distanciaTotal = 0.0;

        for (int i = 0; i < preguntes.size(); i++) {
            Pregunta p = preguntes.get(i);
            RespostaPregunta rp = respostes.get(i);
            Object c = centroide.get(i);
            double distancia = 0.0;
            try {
                RespostaPregunta rpCentroide = p.crearResposta(c);
                distancia = rp.calcularDistanciaLocal(rpCentroide);
            } catch (CustomException ex) {
                if (!rp.esBuida())
                    distancia = 1.0; // Distància màxima si no es pot calcular
            }
            distanciaTotal += distancia;
        }
        return distanciaTotal / preguntes.size();
    }

    /**
     * Recalcula els centroides dels clústers basant-se en les assignacions actuals.
     * Només es fa per als clústers que tenen respostes assignades.
     * 
     * @param MatriuRespostes Matriu de respostes de l'enquesta.
     * @param assignacions    Vector d'assignacions de respostes a clústers.
     * @param k               Nombre de clústers.
     * @param numPreg         Nombre de preguntes.
     * @param preguntes       Llista de preguntes de l'enquesta.
     * @return Llista de nous centroides per als clústers.
     */
    private ArrayList<ArrayList<Object>> recalculaCentroides(ArrayList<ArrayList<RespostaPregunta>> MatriuRespostes,
            int[] assignacions, int k, int numPreg, ArrayList<Pregunta> preguntes) {
        ArrayList<ArrayList<Object>> nousCentroides = new ArrayList<>();
        // Per cada clúster, recull les respostes assignades i calcula el nou centre.
        for (int i = 0; i < k; i++) {
            ArrayList<ArrayList<RespostaPregunta>> respostesCluster = new ArrayList<>();
            // Recull les respostes assignades al clúster i actualitza el centroide.
            for (int j = 0; j < assignacions.length; j++) {
                if (assignacions[j] == i) {
                    respostesCluster.add(MatriuRespostes.get(j));
                }
            }
            // Si hi ha respostes assignades, calcula el nou centroide.
            if (!respostesCluster.isEmpty()) {
                ArrayList<Object> nouCentroide = new ArrayList<>();
                for (int p = 0; p < numPreg; p++) {
                    Pregunta preguntaActual = preguntes.get(p);
                    // Recull totes les respostes per a la pregunta p dins del clúster.
                    ArrayList<RespostaPregunta> respostesPregunta = new ArrayList<>();
                    for (ArrayList<RespostaPregunta> respostaEnquesta : respostesCluster) {
                        respostesPregunta.add(respostaEnquesta.get(p));
                    }
                    // Calcula el nou valor del centroide per a la pregunta p.
                    Object valorCentroide = calculaCentroide(respostesPregunta, preguntaActual);
                    nouCentroide.add(valorCentroide);
                }
                nousCentroides.add(nouCentroide);
            } else {
                // Si no hi ha respostes assignades, reinicialitza el centroide aleatòriament.
                nousCentroides.add(new ArrayList<>(
                        convertirRealAVirtual(MatriuRespostes.get(rand.nextInt(MatriuRespostes.size())))));
            }
        }
        return nousCentroides;
    }

    /**
     * Calcula el valor del centroide per a una pregunta específica basant-se en les
     * respostes del clúster.
     * Segons el tipus de pregunta, utilitza mitjana (numèriques) o moda
     * (categòriques).
     * 
     * @param respostesPregunta Llista de RespostaPregunta per a una pregunta
     *                          específica dins del clúster.
     * @param PreguntaActual    La Pregunta corresponent a les respostes.
     * @return El valor calculat del centroide (mitjana o moda).
     */
    private Object calculaCentroide(ArrayList<RespostaPregunta> respostesPregunta, Pregunta PreguntaActual) {
        if (PreguntaActual instanceof PreguntaFormatNumeric) {
            return calcularMitjana(respostesPregunta);
        } else if (PreguntaActual instanceof PreguntaFormatUnica || PreguntaActual instanceof PreguntaFormatLliure
                || PreguntaActual instanceof PreguntaMultipleResposta) {
            return calcularModa(respostesPregunta);
        } else {
            return null; // Tipus de pregunta no suportat
        }
    }

    /**
     * Calcula la mitjana de les puntuacions de les respostes numèriques.
     * 
     * @param respostesPregunta Llista de RespostaPregunta numèriques.
     * @return La mitjana com a Double.
     */
    private Double calcularMitjana(ArrayList<RespostaPregunta> respostesPregunta) {
        Double suma = 0.0;
        int count = 0;
        for (RespostaPregunta rp : respostesPregunta) {
            if (!rp.esBuida()) {
                suma += ((RespostaInteger) rp).getEntrega();
                count++;
            }
        }
        if (count == 0)
            return 0.0; // Evita divisió per zero
        return suma / count;
    }

    /**
     * Calcula la moda de les puntuacions de les respostes categòriques.
     * 
     * @param respostesPregunta Llista de RespostaPregunta categòriques.
     * @return La moda com a Double.
     */
    private Object calcularModa(ArrayList<RespostaPregunta> respostesPregunta) {
        Map<Object, Integer> freqMap = new HashMap<>();
        // Calcula la freqüència de cada valor.
        for (RespostaPregunta rp : respostesPregunta) {
            if (!rp.esBuida()) {
                Object val;
                if (rp instanceof RespostaInteger)
                    val = ((RespostaInteger) rp).getEntrega();
                else if (rp instanceof RespostaString)
                    val = ((RespostaString) rp).getEntrega();
                else if (rp instanceof RespostaVector)
                    val = ((RespostaVector) rp).getEntrega();
                else
                    val = null;
                if (val != null) {
                    freqMap.put(val, freqMap.getOrDefault(val, 0) + 1);
                }
            }
        }
        // Si no hi ha valors, retorna null.
        if (freqMap.isEmpty())
            return null;
        // Troba el valor amb la freqüència màxima.
        Object moda = null;
        int maxFreq = -1;
        // Es tria la moda amb la freqüència més alta.
        for (Map.Entry<Object, Integer> entry : freqMap.entrySet()) {
            if (entry.getValue() > maxFreq) {
                maxFreq = entry.getValue();
                moda = entry.getKey();
            }
        }
        // Es retorna la moda trobada.
        return moda;
    }

    private ArrayList<Object> convertirRealAVirtual(ArrayList<RespostaPregunta> resposta) {
        ArrayList<Object> virtual = new ArrayList<>();
        for (RespostaPregunta rp : resposta) {

            if (rp instanceof RespostaInteger)
                virtual.add(((RespostaInteger) rp).getEntrega());
            else if (rp instanceof RespostaString)
                virtual.add(((RespostaString) rp).getEntrega());
            else if (rp instanceof RespostaVector)
                virtual.add(((RespostaVector) rp).getEntrega());
            else
                virtual.add(null);
        }
        return virtual;
    }

    /**
     * Genera els clústers finals a partir de les assignacions i els centroides.
     * 
     * @param k            Nombre de clústers.
     * @param assignacions Vector d'assignacions de respostes a clústers.
     * @param Respostes    Llista de RespostaEnquesta de l'enquesta.
     * @param centroides   Llista de centroides per als clústers.
     * @return Mapa de clústers amb els seus membres i centroides.
     */
    private Map<Integer, Cluster> generaClusters(int k, int[] assignacions, ArrayList<RespostaEnquesta> Respostes,
            ArrayList<ArrayList<Object>> centroides) {
        Map<Integer, Cluster> clusters = new HashMap<>();
        // Inicialitza els clústers
        for (int i = 0; i < k; i++) {
            List<Integer> membres = new ArrayList<>();
            for (int j = 0; j < assignacions.length; j++) {
                if (assignacions[j] == i) {
                    membres.add(Respostes.get(j).getUsuari());
                }
            }
            Cluster c = new Cluster(membres);
            c.afegirCentroide(centroides.get(i));
            clusters.put(i, c);
        }
        return clusters;
    }

}