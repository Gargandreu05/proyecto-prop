package edu.upc.prop.cluster422.Domini.Algorismes;

import edu.upc.prop.cluster422.Domini.Classes.*;
import edu.upc.prop.cluster422.Domini.Controladors.CustomException;
import java.util.*;

/**
 * Implementació de l'algorisme Hierarchical. Aquest algorisme és determinista,
 * és a dir, donada una mateixa entrada, sempre donarà el mateix resultat.
 * 
 * @author Andreu Puerto
 * @version 12/12/2025
 */
public class Hierarchical implements StrategyClustering {

    @Override
    public Map<Integer, Cluster> executa(Enquesta e, int k) throws CustomException {
        ArrayList<RespostaEnquesta> totesRespostes = e.getRespostesEnquestaComAList();

        if (totesRespostes.size() < k) {
            throw new CustomException(
                    "No es poden fer " + k + " clústers amb només " + totesRespostes.size() + " respostes.");
        }

        List<Cluster> clustersActuals = new ArrayList<>();

        for (RespostaEnquesta resp : totesRespostes) {
            List<Integer> membres = new ArrayList<>();
            membres.add(resp.getUsuari());
            Cluster c = new Cluster(membres);
            c.afegirCentroide(convertirRespostaAVirtual(resp.getRespostaAPregunta()));
            clustersActuals.add(c);
        }
        while (clustersActuals.size() > k) {
            double minDistancia = Double.MAX_VALUE;
            int indexC1 = -1;
            int indexC2 = -1;

            for (int i = 0; i < clustersActuals.size(); i++) {
                for (int j = i + 1; j < clustersActuals.size(); j++) {

                    double dist = calcularDistanciaEntreClusters(
                            clustersActuals.get(i),
                            clustersActuals.get(j),
                            e.getPreguntes());

                    if (dist < minDistancia) {
                        minDistancia = dist;
                        indexC1 = i;
                        indexC2 = j;
                    }
                }
            }

            if (indexC1 == -1)
                break;

            Cluster c1 = clustersActuals.get(indexC1);
            Cluster c2 = clustersActuals.get(indexC2);

            Cluster nouCluster = fusionarClusters(c1, c2, e.getPreguntes());

            clustersActuals.remove(indexC2);
            clustersActuals.remove(indexC1);

            clustersActuals.add(nouCluster);
        }
        Map<Integer, Cluster> resultat = generaClusters(clustersActuals);
        return resultat;
    }

    /**
     * Calcula la distància entre dos clústers utilitzant la distància entre els
     * seus centroides.
     * @param c1        El primer clúster.
     * @param c2        El segon clúster.
     * @param preguntes La llista de preguntes de l'enquesta.
     * @return La distància entre els dos clústers.
     */
    private double calcularDistanciaEntreClusters(Cluster c1, Cluster c2, ArrayList<Pregunta> preguntes) {
        ArrayList<Object> cent1 = new ArrayList<>(c1.getValorsCentroide());
        ArrayList<Object> cent2 = new ArrayList<>(c2.getValorsCentroide());
        double distTotal = 0.0;

        for (int i = 0; i < preguntes.size(); i++) {
            try {
                Pregunta p = preguntes.get(i);
                RespostaPregunta r1 = p.crearResposta(cent1.get(i));
                RespostaPregunta r2 = p.crearResposta(cent2.get(i));

                distTotal += r1.calcularDistanciaLocal(r2);
            } catch (Exception ex) {
                distTotal += 1.0;
            }
        }
        return distTotal;
    }

    /**
     * Fusiona dos clústers en un nou clúster.
     * @param c1        El primer clúster.
     * @param c2        El segon clúster.
     * @param preguntes La llista de preguntes de l'enquesta.
     * @return El clúster fusionat.
     */
    private Cluster fusionarClusters(Cluster c1, Cluster c2, ArrayList<Pregunta> preguntes) {
        List<Integer> nousMembres = new ArrayList<>(c1.getMembres());
        nousMembres.addAll(c2.getMembres());

        Cluster nou = new Cluster(nousMembres);

        ArrayList<Object> nouCentre = new ArrayList<>();
        ArrayList<Object> cent1 = new ArrayList<>(c1.getValorsCentroide());
        ArrayList<Object> cent2 = new ArrayList<>(c2.getValorsCentroide());

        for (int i = 0; i < preguntes.size(); i++) {
            Pregunta p = preguntes.get(i);
            Object v1 = cent1.get(i);
            Object v2 = cent2.get(i);

            if (p instanceof PreguntaFormatNumeric) {
                try {
                    double d1 = Double.parseDouble(v1.toString());
                    double d2 = Double.parseDouble(v2.toString());
                    nouCentre.add((d1 + d2) / 2.0);
                } catch (Exception e) {
                    nouCentre.add(v1);
                }
            } else {
                nouCentre.add(v1);
            }
        }
        nou.afegirCentroide(nouCentre);
        return nou;
    }

    /**
     * Converteix una resposta a una resposta virtual.
     * 
     * @param llista La llista de respostes a convertir.
     * @return Una llista d'Object que representa la resposta virtual.
     */
    private ArrayList<Object> convertirRespostaAVirtual(List<RespostaPregunta> llista) {
        ArrayList<Object> virtual = new ArrayList<>();
        for (RespostaPregunta rp : llista) {
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
     * Converteix la llista final de clústers en el Mapa que necessita el
     * controlador.
     * 
     * @param clustersAcabats La llista final de clústers fusionats.
     * 
     * @return Mapa indexat (0..k-1) amb els clústers.
     */
    private Map<Integer, Cluster> generaClusters(List<Cluster> clustersAcabats) {
        Map<Integer, Cluster> resultat = new HashMap<>();

        for (int i = 0; i < clustersAcabats.size(); i++) {
            resultat.put(i, clustersAcabats.get(i));
        }

        return resultat;
    }

}