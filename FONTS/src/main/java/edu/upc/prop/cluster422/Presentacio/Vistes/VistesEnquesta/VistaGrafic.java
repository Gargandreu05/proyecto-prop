package edu.upc.prop.cluster422.Presentacio.Vistes.VistesEnquesta;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Aquesta vista serveix per a representar gràficament els resultats del
 * clustering.
 * 
 * @author Andreu Puerto
 * @version 15/12/2025
 */
public class VistaGrafic extends JPanel {

    private Map<Integer, List<String>> dadesClusters;

    private final Color[] COLORS = {
            new Color(65, 105, 225), new Color(220, 20, 60),
            new Color(50, 205, 50), new Color(255, 165, 0),
            new Color(148, 0, 211), new Color(0, 191, 255)
    };

    /**
     * Constructor de la vista gràfica.
     */
    public VistaGrafic() {
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setPreferredSize(new Dimension(600, 400));
    }

    /**
     * Actualitza les dades del gràfic.
     * 
     * @param resultats Mapa amb les dades del clustering.
     */
    public void actualitzarDades(Map<Integer, List<String>> resultats) {
        System.out.println("--> VISTAGRAFIC: Dades rebudes!");
        if (resultats == null)
            System.out.println("    AVÍS: El mapa és NULL");
        else
            System.out.println("    Clústers trobats: " + resultats.size());

        this.dadesClusters = resultats;
        this.revalidate();
        this.repaint();
    }

    /**
     * Pinta el gràfic. Se pinta tot sol mitjançant el repaint de la funció
     * actualitzarDades.
     * 
     * @param g Graphics object.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Neteja el fons (blanc)

        System.out.println("--> VISTAGRAFIC: paintComponent cridat. Mida: " + getWidth() + "x" + getHeight());

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (dadesClusters == null || dadesClusters.isEmpty()) {
            g2.setColor(Color.RED);
            g2.drawLine(0, 0, w, h);
            g2.drawLine(0, h, w, 0);
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("PANELL GRÀFIC ACTIU (SENSE DADES)", w / 2 - 150, h / 2);
            return;
        }

        int padding = 50;
        int numClusters = dadesClusters.size();
        int alçadaFranja = (h - (2 * padding)) / Math.max(1, numClusters);

        g2.setColor(Color.LIGHT_GRAY);
        g2.drawLine(padding, padding, padding, h - padding);
        g2.drawLine(padding, h - padding, w - padding, h - padding);

        int clusterIndex = 0;
        Random rand = new Random(1234);

        for (Map.Entry<Integer, List<String>> entry : dadesClusters.entrySet()) {
            Integer cId = entry.getKey();
            List<String> usuaris = entry.getValue();

            int yBase = padding + (clusterIndex * alçadaFranja) + (alçadaFranja / 2);

            g2.setColor(Color.BLACK);
            g2.drawString("Grup " + cId + " (" + usuaris.size() + ")", 10, yBase + 5);

            g2.setColor(new Color(240, 240, 240));
            g2.drawLine(padding, yBase, w - padding, yBase);

            g2.setColor(COLORS[cId % COLORS.length]);
            for (String user : usuaris) {
                int x = padding + 20 + rand.nextInt(Math.max(1, w - (2 * padding) - 40));
                int y = yBase - 10 + rand.nextInt(20);
                g2.fillOval(x, y, 10, 10);
            }
            clusterIndex++;
        }
    }
}