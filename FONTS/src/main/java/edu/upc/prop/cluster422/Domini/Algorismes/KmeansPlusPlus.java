package edu.upc.prop.cluster422.Domini.Algorismes;

import edu.upc.prop.cluster422.Domini.Classes.*;
import edu.upc.prop.cluster422.Domini.Controladors.CustomException;


import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Vector;



/**
 * Implementa l'algorisme K-Means amb inicialització K-Means++.
 * La lògica d'inicialització està integrada (inlined) dins del mètode executa.
 *
 * @author Enzo Miquel
 * @version 17/11/2025/Entrega
 */
public class KmeansPlusPlus implements StrategyClustering {

    private int maxIteracions = 100;
    private Random rand = new Random();

    /**
     * Executa l'algorisme de K-Means++ sobre una enquesta donada.
     * @param e L'enquesta sobre la qual s'executa l'algorisme.
     * @param k El nombre de clústers a crear.
     * @return Un mapa que associa cada clúster amb els seus membres.
     * @throws CustomException Si hi ha dades insuficients per al càlcul.
     */

    @Override
    public Map<Integer, Cluster> executa(Enquesta e, int k) throws CustomException {
        
        // ---- 1. INICI I PREPARACIÓ ----
        ArrayList<ArrayList<RespostaPregunta>> MatriuRespostes = e.getMatriuRespostes();
        ArrayList<Pregunta> Preguntes = e.getPreguntes();
        ArrayList<RespostaEnquesta> ListRespostes = e.getRespostesEnquestaComAList();

        // Comprova que almenys es pugui posar una resposta per grup
        if (MatriuRespostes.size() < k) {
            throw new CustomException("Dades Insuficients per al càlcul (menys respostes que k).");
        }

        int numResp = MatriuRespostes.size();
        int numPreg = Preguntes.size();

        // ---- 2. INICIALITZACIÓ DELS CENTROIDES ----

        List<ArrayList<RespostaPregunta>> centroidesReals = new ArrayList<>();
        double[] distanciesQuadrat = new double[numResp];
        Arrays.fill(distanciesQuadrat, Double.MAX_VALUE);

        // Tria el primer centroide aleatòriament
        centroidesReals.add(MatriuRespostes.get(this.rand.nextInt(numResp)));

        // Tria els k-1 centroides restants
        for (int c = 1; c < k; c++) {
            double sumaTotalDistQuad = 0.0;
            ArrayList<RespostaPregunta> ultimCentroideTriat = centroidesReals.get(c - 1);

            // Per a cada punt, calcula la distància al quadrat al centroide mes proper
            for (int i = 0; i < numResp; i++) {
                // Distància Real-vs-Real (la de KMedoids)
                double dist = calculaDistanciaGlobalKMedoids(MatriuRespostes.get(i), ultimCentroideTriat, Preguntes);
                double distQuad = dist * dist;
                // Actualitza la distància mínima al quadrat per a aquest punt
                distanciesQuadrat[i] = Math.min(distanciesQuadrat[i], distQuad);
                sumaTotalDistQuad += distanciesQuadrat[i];
            }

            // Tria el següent centroide amb probabilitat ponderada
            double r = this.rand.nextDouble() * sumaTotalDistQuad;
            for (int i = 0; i < numResp; i++) {
                if (distanciesQuadrat[i] == 0) continue; // No seleccionis un punt que ja és centroide
                
                r -= distanciesQuadrat[i];
                if (r <= 0) {
                    centroidesReals.add(MatriuRespostes.get(i)); // S'ha trobat el següent centroide
                    break;
                }
            }
        }

        // Converteix els punts REALS en centroides VIRTUALS
        // Aquesta és la variable 'centroides' que utilitzarà la resta de l'algorisme
        ArrayList<ArrayList<Object>> centroides = new ArrayList<>();
        for (ArrayList<RespostaPregunta> centroideReal : centroidesReals) {
            centroides.add(convertirRealAVirtual(centroideReal));
        }
        
        // ---- FI DE LA INICIALITZACIÓ ----


        // ---- 3. BUCLE PRINCIPAL ----
        int[] assignacions = new int[numResp];
        boolean haCanviat = true;
        int it = 0;

        while (haCanviat && it < maxIteracions) {
            haCanviat = false;

            // Per cada resposta, calcula la distància al centre de cada clúster i l'assigna al més proper.
            for (int i = 0; i < numResp; i++) {
                double minDist = Double.MAX_VALUE;
                int millorCluster = -1;
                // Per cada clúster, calcula la distància entre la resposta i el seu centre.
                for (int j = 0; j < k; j++) {
                    double distancia = calculaDistanciaGlobal(MatriuRespostes.get(i), centroides.get(j), Preguntes);
                    if (distancia < minDist) {
                        minDist = distancia;
                        millorCluster = j;
                    }
                }
                
                if (assignacions[i] != millorCluster) {
                    assignacions[i] = millorCluster;
                    haCanviat = true; 
                }
            }
            // Recalcula els centroides basant-se en les assignacions actuals.
            if (haCanviat) {
                centroides = recalculaCentroides(MatriuRespostes, assignacions, k, numPreg, Preguntes);
            }
            
            it++;
        }

        // ---- 4. GENERACIÓ DEL RESULTAT ----
        return generaClusters(k, assignacions, ListRespostes, centroides);
    }

    
    // ===============================================
    // MÈTODES PRIVATS (Helpers)
    // ===============================================

    /** Càlcul de distància Real-vs-Real (Mètode auxiliar per a la inicialització)
     * @param resposta1 Primera resposta a comparar.
     * @param resposta2 Segona resposta a comparar.
     * @param Preguntes Llista de preguntes per a interpretar les respostes
     * @return Distància normalitzada entre les dues respostes.
     */
    private double calculaDistanciaGlobalKMedoids(ArrayList<RespostaPregunta> resposta1, ArrayList<RespostaPregunta> resposta2, ArrayList<Pregunta> Preguntes) {
        
        int N = Preguntes.size();
        if (N == 0) return 1.0;
        double distancia = 0.0;
        for (int i = 0; i < N; i++) {
            distancia += resposta1.get(i).calcularDistanciaLocal(resposta2.get(i));
        }
        return distancia / N;
    }

    /**
     * Calcula la distància entre un PUNT REAL (ArrayList<RespostaPregunta>)
     * i un CENTROIDE VIRTUAL (ArrayList<Object>).
     * @param puntReal Punt real a comparar.
     * @param centroideVirtual Centroide virtual a comparar.
     * @param preguntes Llista de preguntes per a interpretar les respostes.
     * @return Distància normalitzada entre el punt i el centroide.
     */
    private double calculaDistanciaGlobal(ArrayList<RespostaPregunta> puntReal, ArrayList<Object> centroideVirtual, ArrayList<Pregunta> preguntes) {
        double distanciaTotal = 0.0;
        int numPreguntesConsiderades = 0;

        for (int i = 0; i < preguntes.size(); i++) {
            Pregunta p = preguntes.get(i);
            RespostaPregunta valPunt = puntReal.get(i); 
            Object valCentroide = (centroideVirtual.size() > i) ? centroideVirtual.get(i) : null; 
            
            RespostaPregunta centroideEmbolicat = crearWrapperDeValor(p, valCentroide);

            if (centroideEmbolicat != null && !valPunt.esBuida()) {
                distanciaTotal += valPunt.calcularDistanciaLocal(centroideEmbolicat);
                numPreguntesConsiderades++;
            } else if (centroideEmbolicat != null || !valPunt.esBuida()) {
                distanciaTotal += 1.0;
                numPreguntesConsiderades++;
            }
        }

        if (numPreguntesConsiderades == 0) return 1.0;
        return distanciaTotal / numPreguntesConsiderades;
    }

    /**
     * Mètode "Wrapper" que converteix un valor VIRTUAL (Object)
     * en un objecte RespostaPregunta "fals".
     * @param p Pregunta associada al valor.
     * @param valorVirtual Valor a convertir.
     * @return RespostaPregunta corresponent al valor, o null si no es pot convertir. 
     */
    private RespostaPregunta crearWrapperDeValor(Pregunta p, Object valorVirtual) {
        if (valorVirtual == null) return null;
        try {
            if (p instanceof PreguntaFormatNumeric) {
                return new RespostaInteger((int) Math.round((Double) valorVirtual));
            } else if (p instanceof PreguntaFormatUnica) {
                return new RespostaInteger((Integer) valorVirtual);
            } else if (p instanceof PreguntaFormatLliure) {
                return new RespostaString((String) valorVirtual);
            } else if (p instanceof PreguntaMultipleResposta) {
                return new RespostaVector((Vector<Integer>) valorVirtual);
            }
        } catch (Exception e) { return null; }
        return null;
    }

    /** Recalcula els centroides (mitjana/moda)
     * @param matriu Matriu de respostes.
     * @param assignacions Vector d'assignacions actual.
     * @param k Nombre de clusters.
     * @param numPreg Nombre de preguntes.
     * @return Nous centroides com a llista d'ArrayList<Object>
     */
    private ArrayList<ArrayList<Object>> recalculaCentroides(ArrayList<ArrayList<RespostaPregunta>> matriu, int[] assignacions, int k, int numPreg, ArrayList<Pregunta> preguntes) {
        
        ArrayList<ArrayList<Object>> nousCentroides = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            ArrayList<Object> nouCentroide = new ArrayList<>();
            ArrayList<ArrayList<RespostaPregunta>> membresCluster = new ArrayList<>();
            
            for (int j = 0; j < matriu.size(); j++) {
                if (assignacions[j] == i) {
                    membresCluster.add(matriu.get(j));
                }
            }
            if (membresCluster.isEmpty()) {
                nousCentroides.add(convertirRealAVirtual(matriu.get(rand.nextInt(matriu.size()))));
                continue;
            }

            for (int pIdx = 0; pIdx < numPreg; pIdx++) {
                Pregunta p = preguntes.get(pIdx);
                List<RespostaPregunta> valorsColumna = new ArrayList<>();
                for (ArrayList<RespostaPregunta> membre : membresCluster) {
                    valorsColumna.add(membre.get(pIdx));
                }
                
                if (p instanceof PreguntaFormatNumeric) {
                    nouCentroide.add(calcularMitjana(valorsColumna));
                } 
                else if (p instanceof PreguntaFormatUnica || p instanceof PreguntaFormatLliure || p instanceof PreguntaMultipleResposta) {
                    nouCentroide.add(calcularModa(valorsColumna));
                } 
                else {
                    nouCentroide.add(null);
                }
            }
            nousCentroides.add(nouCentroide);
        }
        return nousCentroides;
    }

    /**
     * Calcula la mitjana dels valors numèrics en una llista de RespostaPregunta.
     * @param valors Llista de RespostaPregunta a processar.
     * @return Mitjana com a Double.
     */
    private Double calcularMitjana(List<RespostaPregunta> valors) {
        double suma = 0.0;
        int count = 0;
        for (RespostaPregunta rp : valors) {
            if (rp instanceof RespostaInteger && !rp.esBuida()) {
                suma += ((RespostaInteger) rp).getEntrega();
                count++;
            }
        }
        return (count == 0) ? 0.0 : suma / count;
    }

    /**
     * Calcula la moda dels valors en una llista de RespostaPregunta.
     * @param valors Llista de RespostaPregunta a processar.
     * @return Moda com a Object.
     */
    private Object calcularModa(List<RespostaPregunta> valors) {
        Map<Object, Integer> freqs = new HashMap<>();
        for (RespostaPregunta rp : valors) {
            if (!rp.esBuida()) {
                Object val;
                if (rp instanceof RespostaInteger) val = ((RespostaInteger) rp).getEntrega();
                else if (rp instanceof RespostaString) val = ((RespostaString) rp).getEntrega();
                else if (rp instanceof RespostaVector) val = ((RespostaVector) rp).getEntrega();
                else val = null;

                if (val != null) {
                    freqs.put(val, freqs.getOrDefault(val, 0) + 1);
                }
            }
        }
        return freqs.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /** Converteix un punt Real (ArrayList<RespostaPregunta>) a Virtual (ArrayList<Object>)
     * @param puntReal Punt real a convertir.
     * @return Punt convertit
     */
    private ArrayList<Object> convertirRealAVirtual(ArrayList<RespostaPregunta> puntReal) {
        ArrayList<Object> virtual = new ArrayList<>();
        for (RespostaPregunta rp : puntReal) {
            if (rp instanceof RespostaInteger) virtual.add(((RespostaInteger) rp).getEntrega());
            else if (rp instanceof RespostaString) virtual.add(((RespostaString) rp).getEntrega());
            else if (rp instanceof RespostaVector) virtual.add(((RespostaVector) rp).getEntrega());
            else virtual.add(null);
        }
        return virtual;
    }

    /** Genera el Map de resultats final
     * @param k Nombre de clusters
     * @param assignacions Vector d'assignacions
     * @param ListRespostes Llista de respostes de l'enquesta
     * @param centroidesFinals Centroides finals obtinguts
     * @return Map de clusters amb els seus membres
     */
    private Map<Integer, Cluster> generaClusters(int k, int[] assignacions, ArrayList<RespostaEnquesta> ListRespostes, ArrayList<ArrayList<Object>> centroidesFinals) {
        Map<Integer, Cluster> clusterMap = new HashMap<>();
        for (int i = 0; i < k; i++) {
            Cluster c = new Cluster(new ArrayList<>()); // Constructor K-Means
            if (centroidesFinals != null && i < centroidesFinals.size()) {
                c.afegirCentroide(centroidesFinals.get(i));
            }
            clusterMap.put(i, c);
        }

        for (int i = 0; i < assignacions.length; i++) {
            int clusterIndex = assignacions[i];
            Cluster clusterAssignat = clusterMap.get(clusterIndex);
            Integer usernameMembre = ListRespostes.get(i).getUsuari();
            clusterAssignat.afegirMembre(usernameMembre);
        }

        return clusterMap;
    }
}