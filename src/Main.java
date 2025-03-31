import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main extends QueryFunction {
	
	public static String[] lundicour = new String[7];
	public static String[] mardicour = new String[7];
	public static String[] mecredicour = new String[7];
	public static String[] jeudicour = new String[7];
	public static String[] vendredicour = new String[7];
	
	public static void main(String[] args) throws SQLException {
		System.out.println("Java working...\n");
		
		try(Connection db = connect(); Scanner sc = new Scanner(System.in);){
			
			System.out.println("                 *** GESTION DES EMPLOIES DE TEMPS(console) ****              \n");
			// option
			System.out.println("quelle action voulez vous effectuer ? \n");
			System.out.println("1. enregistrer une Classe  \n2. consulter les classes");
			System.out.println("3. enregistrer un emploie de temps\n4. consulter un emploie de temps\n5. enregistrement des matières\n6. consulter les matière d'une classe \n");
			
			System.out.print("renseignez une option comprise de 1-5 : ");
			String choice = sc.next();
			
			switch (choice){
				case "1":
					System.out.print("Entrez le nom de la classe à enregistrer : ");
					String value = sc.next();
					System.out.print("Entrez le cycle de la classe enregistrer precedement (1 ou 2): ");
					String cycle = sc.next();
					
					if(value.isEmpty()  && cycle.isEmpty())
						System.out.println("ERROR : veullez saisir toute les valeurs");
						
					else
						addClasse(value, cycle, db);
					
					break;
				case "2":
					System.out.println("                 *** Liste des Classes Enregistrés ***");
					System.out.println(viewClassroom(db) + "\n");
					break;
				case "3":
					String [][] week = {
							lundicour,
							mardicour,
							mecredicour,
							jeudicour,
							vendredicour
							
					};
					System.out.println("                 *** Enregistrés un emploie de temps ***");
					System.out.println(viewClassroom(db) +"\n");
					
					System.out.print("selectionnez une classe : ");
					String classname = sc.next();
					
					if(!verifyClassroom(classname, db)) {
						System.out.println("ERROR: cette classe n'existe pas");
					}else {
						if(countMatiere(classname,db) <10 ) {
							System.out.println("WARNING: cette classe ne possède pas suffisament de matière pour former un emploie de temp");
						}else {
							// creation de l'emploie de temps
							System.out.println("matieres disponible pour la "+classname+"");
							System.out.println(viewMatiere(classname, db)+"\n");
							
							String[] jour = {"lundi","mardi","mercredi", "jeudi", "vendredi" };
							// remplir le tableau de matière
							int compt =1;
							for(int i = 0; i< week.length; i++) {
								System.out.println("jour : " + jour[i]);
								for(int j=0; j<week[i].length; j++) {
									
									System.out.print("periode " + compt +": ");
									week[i][j] = sc.nextLine();
									compt++;
								}
							}
							
							
							//System.out.println("création de l'emploie de temps en cours...\n");
						}
					}
				
					break;
				case "4":
					System.out.println("option indisponible");
					break;
				case "5":
					System.out.println("                 *** Enregsitrement des matière ***\n");
					System.out.println(viewClassroom(db));
					
					System.out.println("Entrez le nom de la classe");
					String className = sc.next();
					
					System.out.print("combien de matière voulez-vous enregistrez ? ");
					int numberMat = sc.nextInt();
					
					if(verifyClassroom(className, db) == true) {
						if(countMatiere(className,db) >= 14) {
							System.out.println("WARNING: cette classe possede suffisament de matière");
						}else {
							for(int i = 1; i<numberMat; i++) {
								System.out.print("matière "+ i +": ");
								String nameMat = sc.next();
								insertMatiere(className, nameMat, db);
							}
						}
						
					} else {
						System.out.println("ERROR: cette classe n'existe pas");
					}
					
				case "6":
					System.out.println("                 ***Liste de matière par classe***");
					System.out.println(viewClassroom(db) +"\n");
					System.out.print("renseignez le nom de classe : ");
					String classn = sc.next();
					if(verifyClassroom(classn, db) == true)
						System.out.println(viewMatiere(classn, db) + "\n");
					else
						System.out.println("ERROR: cette classe n'existe pas");
					break;
				default:
					System.out.println("Veuillez renseignez une option valide\n");
			}
			System.out.println("Java end task...");
		}
	}
}
