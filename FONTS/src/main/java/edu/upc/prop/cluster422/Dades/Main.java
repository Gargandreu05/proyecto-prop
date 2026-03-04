package edu.upc.prop.cluster422.Dades;

import edu.upc.prop.cluster422.Domini.Classes.*;
import edu.upc.prop.cluster422.Domini.Controladors.CustomException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;


public class Main {

    private static List<String> toList(String[] s) {
        List<String> res = new ArrayList<>();
        Collections.addAll(res, s);
        return res;
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt + " ");
            String line = sc.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Introdueix un numero vàlid.");
            }
        }
    }

    private static String readNonEmpty(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt + " ");
            String line = sc.nextLine();
            if (!line.trim().isEmpty()) return line.trim();
            System.out.println("El valor no pot ser buit.");
        }
    }

    private static Pregunta readPreguntaInteractive(Scanner sc) throws CustomException {
        System.out.println("Selecciona el tipus de pregunta: 1=UNIC 2=MULTIPLE 3=NUMERIC 4=LLIURE");
        int t = readInt(sc, "Tipus (1-4):");
        String enunciat = readNonEmpty(sc, "Enunciat:");
        switch (t) {
            case 1: {
                int n = readInt(sc, "Numero d'opcions:");
                List<String> opc = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    opc.add(readNonEmpty(sc, "Opcio " + i + ":"));
                }
                return new PreguntaFormatUnica(enunciat, opc);
            }
            case 2: {
                int n = readInt(sc, "Numero d'opcions:");
                List<String> opc = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    opc.add(readNonEmpty(sc, "Opcio " + i + ":"));
                }
                return new PreguntaMultipleResposta(enunciat, opc);
            }
            case 3:
                return new PreguntaFormatNumeric(enunciat);
            default:
                return new PreguntaFormatLliure(enunciat);
        }
    }

    private static void printEnquesta(Enquesta e) {
        System.out.println("--- ENQUESTA ---");
        System.out.println("Titol: " + e.getTitol());
        System.out.println("Id: " + e.getId());
        System.out.println("Creador: " + e.getCreador());
        List<Pregunta> ps = e.getPreguntes();
        for (int i = 0; i < ps.size(); i++) {
            Pregunta p = ps.get(i);
            System.out.println("[" + i + "] " + p.getEnunciat() + " (" + p.tipusPregunta() + ")");
            List<String> opc = p.getOpcions();
            for (int j = 0; j < opc.size(); j++) {
                System.out.println("   - (" + j + ") " + opc.get(j));
            }
        }
        System.out.println("-----------------");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GestorEnquestes gestor;
        GestorRespostes gestorRespostes;
        try {
            gestor = GestorEnquestes.getInstance(Paths.get("."));
        } catch (IOException e) {
            System.out.println("No s'ha pogut inicialitzar GestorEnquestes: " + e.getMessage());
            return;
        }

        while (true) {
            System.out.println("\nTriar opcio:");
            System.out.println("1. Crear enquesta");
            System.out.println("2. Mostrar enquesta per titol");
            System.out.println("3. Mostrar enquesta per id");
            System.out.println("4. Llistar enquestes");
            System.out.println("5. Afegir pregunta a enquesta");
            System.out.println("6. Eliminar pregunta d'enquesta");
            System.out.println("7. Canviar titol d'enquesta");
            System.out.println("8. Esborrar enquesta");
            System.out.println("10. Respondre una enquesta");
            System.out.println("9. Sortir");
            int op = readInt(sc, "Opcio:");

            try {
                switch (op) {
                    case 1: {
                        String titol = readNonEmpty(sc, "Titol de l'enquesta:");
                        int creador = readInt(sc, "Id creador (numero):");
                        Enquesta e = new Enquesta(titol, creador, 0);
                        int numPreg = readInt(sc, "Quantes preguntes vols afegir?:");
                        for (int i = 0; i < numPreg; i++) {
                            System.out.println("Pregunta " + i + ":");
                            Pregunta p = readPreguntaInteractive(sc);
                            e.afegeixPregunta(p);
                        }
                        Integer id = gestor.crearEnquesta(e);
                        System.out.println("Enquesta creada amb id: " + id);
                        break;
                    }
                    case 2: {
                        String titol = readNonEmpty(sc, "Titol de l'enquesta a mostrar:");
                        Enquesta e = gestor.getEnquesta(titol);
                        printEnquesta(e);
                        break;
                    }
                    case 3: {
                        int id = readInt(sc, "Id de l'enquesta:");
                        Enquesta e = gestor.getEnquestaById(id);
                        printEnquesta(e);
                        break;
                    }
                    case 4: {
                        Map<String, Integer> l = gestor.getLlistatEnquestes();
                        System.out.println("Llistat d'enquestes:");
                        for (Map.Entry<String, Integer> entry : l.entrySet()) {
                            System.out.println(" - " + entry.getKey() + " (id=" + entry.getValue() + ")");
                        }
                        break;
                    }
                    case 5: {
                        String titol = readNonEmpty(sc, "Titol de l'enquesta on afegir pregunta:");
                        Pregunta p = readPreguntaInteractive(sc);
                        gestor.addQuestionToEnquestaJSON(titol, p);
                        System.out.println("Pregunta afegida.");
                        break;
                    }
                    case 6: {
                        String titol = readNonEmpty(sc, "Titol de l'enquesta on eliminar pregunta:");
                        int idx = readInt(sc, "Index de la pregunta a eliminar:");
                        gestor.deleteQuestionFromEnquestaJSON(titol, idx);
                        System.out.println("Pregunta eliminada.");
                        break;
                    }
                    case 7: {
                        String titol = readNonEmpty(sc, "Titol actual de l'enquesta:");
                        String nou = readNonEmpty(sc, "Nou titol:");
                        gestor.setTitleEnquesta(titol, nou);
                        System.out.println("Titol actualitzat.");
                        break;
                    }
                    case 8: {
                        String titol = readNonEmpty(sc, "Titol de l'enquesta a esborrar:");
                        gestor.deleteEnquesta(titol);
                        System.out.println("Enquesta esborrada.");
                        break;
                    }
                    case 9:
                        System.out.println("Fins aviat.");
                        sc.close();
                        return;
                    case 10:
                        gestorRespostes = GestorRespostes.getInstance(Paths.get("."));
                        List<RespostaPregunta> respostes = new ArrayList<>();
                        respostes.add(new RespostaString("Resposta a pregunta 0"));
                        RespostaEnquesta re = new RespostaEnquesta(1,2,respostes);
                        gestorRespostes.crearResposta(re, 2,1);
                        break;
                    case 11:
                        gestorRespostes = GestorRespostes.getInstance(Paths.get("."));
                        gestorRespostes.deleteResposta(1,2);
                        break;
                    default:
                        System.out.println("Opcio desconeguda.");
                }
            } catch (CustomException ce) {
                System.out.println("Error de domini: " + ce.getMessage());
            } catch (IOException ioe) {
                System.out.println("Error IO: " + ioe.getMessage());
            } catch (Exception ex) {
                System.out.println("Error inesperat: " + ex.getMessage());
            }
        }
    }
}
