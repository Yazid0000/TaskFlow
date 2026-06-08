import controller.PersistenceManager;
import controller.ProjectController;
import controller.TaskController;
import model.*;
import view.MainWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.time.LocalDate;
/**
* Point d'entrée de TaskFlow.
* Charge les données sauvegardées ou crée un projet de démonstration.
*/
public class Main {
public static void main(String[] args) {
// 1. Look & feel système
try {
UIManager.setLookAndFeel(
UIManager.getSystemLookAndFeelClassName());
} catch (Exception e) {
System.err.println("Look&Feel : " + e.getMessage());
}
// 2. Charger les données sauvegardées
ProjectController.getInstance().loadAll();
// 3. Si aucune sauvegarde → créer les données de démo
if (!PersistenceManager.getInstance().hasSaveFile()
|| ProjectController.getInstance().getProjects().isEmpty()) {
createDemoData();
}
// 4. Lancer la fenêtre sur l'EDT
SwingUtilities.invokeLater(() -> {
new MainWindow().setVisible(true);
});
}
/**
* Crée deux projets de démonstration bien remplis.
* Appelé uniquement au premier démarrage.
*/
private static void createDemoData() {
ProjectController pc = ProjectController.getInstance();
TaskController tc = TaskController.getInstance();
// ============================================
// PROJET 1 : Application Mobile
// ============================================
Project p1 = pc.createProject(
"Application Mobile",
"Développement d'une app mobile de livraison"
);
Column p1Todo = p1.getColumns().get(0);
Column p1Progress = p1.getColumns().get(1);
Column p1Done = p1.getColumns().get(2);
// Tâches "À faire"
Task t1 = tc.addTask(p1, p1Todo,
"Concevoir la base de données",
"Modéliser les tables utilisateurs, commandes et produits",
Priority.HIGH);
t1.setDueDate(LocalDate.now().plusDays(5));
Task t2 = tc.addTask(p1, p1Todo,
"Rédiger les spécifications",
"Document de spécifications fonctionnelles v1.0",
Priority.MEDIUM);
t2.setDueDate(LocalDate.now().plusDays(3));
tc.addTask(p1, p1Todo,
"Configurer l'environnement de dev",
"Installer Android Studio, configurer Git",
Priority.LOW);
// Tâches "En cours"
Task t4 = tc.addTask(p1, p1Progress,
"Développer l'écran de connexion",
"Login / Register avec validation des champs",
Priority.HIGH);
t4.setDueDate(LocalDate.now().minusDays(1)); // EN RETARD
tc.addTask(p1, p1Progress,
"Intégrer l'API de paiement",
"Intégration Stripe pour les paiements en ligne",
Priority.CRITICAL);
// Tâches "Terminé"
tc.addTask(p1, p1Done,
"Créer les maquettes UI",
"Wireframes et prototypes Figma validés",
Priority.MEDIUM);
tc.addTask(p1, p1Done,
"Charte graphique",
"Définir couleurs, typographies et composants",
Priority.LOW);
tc.addTask(p1, p1Done,
"Réunion de lancement",
"Kick-off meeting avec toute l'équipe",
Priority.LOW);
// ============================================
// PROJET 2 : Site E-commerce
// ============================================
Project p2 = pc.createProject(
"Site E-commerce",
"Refonte complète du site de vente en ligne"
);
Column p2Todo = p2.getColumns().get(0);
Column p2Progress = p2.getColumns().get(1);
Column p2Done = p2.getColumns().get(2);
// Tâches "À faire"
tc.addTask(p2, p2Todo,
"Optimiser les performances",
"Améliorer le temps de chargement des pages",
Priority.HIGH);
Task t8 = tc.addTask(p2, p2Todo,
"Ajouter le module de avis clients",
"Système de notation et commentaires produits",
Priority.MEDIUM);
t8.setDueDate(LocalDate.now().plusDays(7));
// Tâches "En cours"
Task t9 = tc.addTask(p2, p2Progress,
"Refonte de la page d'accueil",
"Nouveau design responsive mobile-first",
Priority.CRITICAL);
t9.setDueDate(LocalDate.now().minusDays(2)); // EN RETARD
tc.addTask(p2, p2Progress,
"Intégrer le système de filtres",
"Filtres par catégorie, prix, marque",
Priority.HIGH);
// Tâches "Terminé"
tc.addTask(p2, p2Done,
"Audit du site existant",
"Analyse des performances et points d'amélioration",
Priority.MEDIUM);
tc.addTask(p2, p2Done,
"Mise en place du SSL",
"Certificat HTTPS installé et configuré",
Priority.CRITICAL);
tc.addTask(p2, p2Done,
"Migration vers le nouveau serveur",
"Transfert des données et configuration DNS",
Priority.HIGH);
// Sauvegarde des données de démo
pc.saveAll();
System.out.println("[Demo] Données de démonstration créées.");
}
}