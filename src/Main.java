import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main extends QueryFunction {

    public static String[] lundicour = new String[7];
    public static String[] mardicour = new String[7];
    public static String[] mecredicour = new String[7];
    public static String[] jeudicour = new String[7];
    public static String[] vendredicour = new String[7];

    public static void main(String[] args) throws SQLException {
        System.out.println("Java working...\n");

        try (Connection db = connect(); Scanner sc = new Scanner(System.in)) {

            System.out.println("                 *** GESTION DES EMPLOIS DU TEMPS (console) ***              \n");
            System.out.println("Quelle action voulez-vous effectuer ? \n");
            System.out.println("1. Enregistrer une classe");
            System.out.println("2. Consulter les classes");
            System.out.println("3. Enregistrer un emploi du temps");
            System.out.println("4. Consulter un emploi du temps");
            System.out.println("5. Enregistrer des matières");
            System.out.println("6. Consulter les matières d'une classe\n");

            System.out.print("Renseignez une option (1-6) : ");
            String choice = sc.next();

            switch (choice) {
                case "1":
                    System.out.print("Entrez le nom de la classe à enregistrer : ");
                    String value = sc.next().trim();
                    System.out.print("Entrez le cycle de la classe (1 ou 2) : ");
                    String cycle = sc.next().trim();

                    if (value.isEmpty() || cycle.isEmpty()) {
                        System.out.println("ERREUR : Veuillez saisir toutes les valeurs.");
                    } else {
                        addClasse(value, cycle, db);
                        System.out.println("Classe enregistrée avec succès !");
                    }
                    break;

                case "2":
                    System.out.println("                 *** Liste des Classes Enregistrées ***");
                    System.out.println(viewClassroom(db) + "\n");
                    break;

                case "3":
                    String[][] week = {lundicour, mardicour, mecredicour, jeudicour, vendredicour};
                    System.out.println("                 *** Enregistrer un emploi du temps ***");
                    System.out.println(viewClassroom(db) + "\n");

                    System.out.print("Sélectionnez une classe : ");
                    String classname = sc.next().trim();

                    if (!verifyClassroom(classname, db)) {
                        System.out.println("ERREUR : Cette classe n'existe pas.");
                    } else {
                        if (countMatiere(classname, db) < 10) {
                            System.out.println("ATTENTION : Cette classe ne possède pas suffisamment de matières pour former un emploi du temps.");
                        } else {
                            System.out.println("Matières disponibles pour " + classname + " :");
                            System.out.println(viewMatiere(classname, db) + "\n");

                            String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"};

                            for (int i = 0; i < week.length; i++) {
                                System.out.println(jours[i] + " :");
                                for (int j = 0; j < week[i].length; j++) {
                                    System.out.print("Période " + (j + 1) + ": ");
                                    String matiere = sc.next().trim();
                                    if (!matiere.isEmpty()) {
                                        week[i][j] = matiere;
                                    } else {
                                        System.out.println("ERREUR : Une matière ne peut pas être vide !");
                                        j--; // Re-demande l'entrée
                                    }
                                }
                            }

                            // Enregistrement dans la base de données
                            insertEmploiTemps(classname, week, db);
                            System.out.println("Emploi du temps enregistré avec succès !");
                        }
                    }
                    break;

                case "4":
                    System.out.println("                 ***Consultation des emploie de temps***\n");
                    System.out.println(viewClassroom(db));
                    System.out.print("selection la classe dont vous voulez consultez l'emploie de temps : ");
                    String classs = sc.next();
                    if(verifyClassroom(classs, db) == true) {
                    	getEmploiTemps(classs, db);
                    }else {
                    	System.out.println("WARNING: cette classe n'existe pas");
                    }
                    break;

                case "5":
                    System.out.println("                 *** Enregistrement des matières ***\n");
                    System.out.println(viewClassroom(db));

                    System.out.print("Entrez le nom de la classe : ");
                    String className = sc.next().trim();

                    System.out.print("Combien de matières voulez-vous enregistrer ? ");
                    int numberMat = sc.nextInt();

                    if (verifyClassroom(className, db)) {
                        if (countMatiere(className, db) >= 14) {
                            System.out.println("ATTENTION : Cette classe possède déjà suffisamment de matières.");
                        } else {
                            for (int i = 0; i < numberMat; i++) {
                                System.out.print("Matière " + (i + 1) + " : ");
                                String nameMat = sc.next().trim();
                                if (!nameMat.isEmpty()) {
                                    insertMatiere(className, nameMat, db);
                                } else {
                                    System.out.println("ERREUR : Le nom de la matière ne peut pas être vide !");
                                    i--; // Redemande l'entrée
                                }
                            }
                            System.out.println("Matières enregistrées avec succès !");
                        }
                    } else {
                        System.out.println("ERREUR : Cette classe n'existe pas.");
                    }
                    break;

                case "6":
                    System.out.println("                 *** Liste des matières par classe ***");
                    System.out.println(viewClassroom(db) + "\n");

                    System.out.print("Renseignez le nom de la classe : ");
                    String classn = sc.next().trim();

                    if (verifyClassroom(classn, db)) {
                        System.out.println(viewMatiere(classn, db) + "\n");
                    } else {
                        System.out.println("ERREUR : Cette classe n'existe pas.");
                    }
                    break;

                default:
                    System.out.println("Veuillez renseigner une option valide.\n");
            }

            System.out.println("Java end task...");
        }
    }
}
