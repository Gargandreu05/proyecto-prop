package edu.upc.prop.cluster422.Domini.Algorismes;
import java.util.Map;

import edu.upc.prop.cluster422.Domini.Classes.Cluster;
import edu.upc.prop.cluster422.Domini.Classes.Enquesta;
import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

/**
 * Interfície que defineix estratègia per tots els algorismes de clustering del projecte
 * Permet que s'afegeixin més algorismes sense modificar les classes existents, i és flexible.
 * 
 * @author Andreu Puerto
 * @version 17/11/2025/Entrega
 */

public interface StrategyClustering{

    /**
     * Executa l'algorisme de clustering sobre l'enquesta e.
     * @param e Enquesta que conté totes les dades.
     * @param k Nombre de clústers desitjats.
     * @return Resultat de l'agrupament -> Map de clusters.
     * @throws CustomException Les dades no són vàlides per l'algorisme.
     */

    Map<Integer, Cluster> executa(Enquesta e, int k) throws CustomException;

}