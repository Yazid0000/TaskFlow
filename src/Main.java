import controller.ProjectController;
import view.MainWindow;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
/**
* Point d'entrée de TaskFlow.
* Lance l'application sur l'Event Dispatch Thread (EDT) de Swing.
*/
public class Main {
public static void main(String[] args) {
// 1. Appliquer le look & feel système (natif Windows/Mac/Linux)
try {
UIManager.setLookAndFeel(
UIManager.getSystemLookAndFeelClassName()
);
} catch (Exception e) {
// Si ça échoue, Swing utilise son look & feel par défaut
System.err.println("Look&Feel non appliqué : " + e.getMessage());
}
// 2. Charger les données sauvegardées
ProjectController.getInstance().loadAll();
// 3. Lancer la fenêtre sur l'EDT Swing
SwingUtilities.invokeLater(() -> {
new MainWindow().setVisible(true);
});
}
}