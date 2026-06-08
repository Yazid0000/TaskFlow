package view;
import controller.ProjectController;
import controller.ProjectListener;
import model.Project;
import model.Task;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
* Fenêtre principale de TaskFlow.
* Orchestre la sidebar, le Kanban et la barre de statut.
* Implémente ProjectListener pour réagir aux changements de données.
*/
public class MainWindow extends JFrame implements ProjectListener {
private static final long serialVersionUID = 1L;
private static final String APP_NAME = "TaskFlow";
private final ProjectController pc = ProjectController.getInstance();
private SidebarPanel sidebar;
private KanbanPanel kanban;
private JLabel statusLabel; // barre de statut
public MainWindow() {
super(APP_NAME);
pc.addListener(this);
initUI();
setupCloseHandler();
setupKeyboardShortcuts();
}
private void initUI() {
setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // géré manuellement
setSize(new Dimension(1100, 700));
setMinimumSize(new Dimension(800, 500));
setLocationRelativeTo(null);
setJMenuBar(buildMenuBar());
sidebar = new SidebarPanel();
kanban = new KanbanPanel();
JSplitPane split = new JSplitPane(
JSplitPane.HORIZONTAL_SPLIT, sidebar, kanban);
split.setDividerLocation(220);
split.setDividerSize(4);
split.setBorder(null);
add(split, BorderLayout.CENTER);
add(buildStatusBar(), BorderLayout.SOUTH);
}
// ======= Barre de statut =======
private JPanel buildStatusBar() {
JPanel bar = new JPanel(new BorderLayout());
bar.setBorder(new javax.swing.border.MatteBorder(
1, 0, 0, 0, new Color(210, 210, 220)));
bar.setBackground(new Color(248, 248, 252));
statusLabel = new JLabel(" Bienvenue dans TaskFlow");
statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
statusLabel.setForeground(new Color(120, 120, 140));
statusLabel.setBorder(new javax.swing.border.EmptyBorder(3, 8, 3, 8));
JLabel version = new JLabel("TaskFlow v1.0 ");
version.setFont(new Font("SansSerif", Font.PLAIN, 11));
version.setForeground(new Color(180, 180, 200));
bar.add(statusLabel, BorderLayout.CENTER);
bar.add(version, BorderLayout.EAST);
return bar;
}
private void setStatus(String msg) {
statusLabel.setText(" " + msg);
}
// ======= Menu =======
private JMenuBar buildMenuBar() {
JMenuBar bar = new JMenuBar();
// --- Menu Fichier ---
JMenu mFichier = new JMenu("Fichier");
JMenuItem iSave = new JMenuItem("Sauvegarder");
iSave.setAccelerator(KeyStroke.getKeyStroke(
KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
iSave.addActionListener(e -> {
pc.saveAll();
setStatus("Sauvegarde effectuée.");
});
JMenuItem iQuit = new JMenuItem("Quitter");
iQuit.addActionListener(e -> confirmAndQuit());
mFichier.add(iSave);
mFichier.addSeparator();
mFichier.add(iQuit);
// --- Menu Projet ---
JMenu mProjet = new JMenu("Projet");
JMenuItem iNew = new JMenuItem("Nouveau projet...");
iNew.setAccelerator(KeyStroke.getKeyStroke(
KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
iNew.addActionListener(e -> showNewProjectDialog());
JMenuItem iStats = new JMenuItem("Statistiques...");
iStats.addActionListener(e -> showStats());
mProjet.add(iNew);
mProjet.addSeparator();
mProjet.add(iStats);
bar.add(mFichier);
bar.add(mProjet);
return bar;
}
// ======= Handlers =======
private void setupCloseHandler() {
addWindowListener(new WindowAdapter() {
@Override
public void windowClosing(WindowEvent e) {
confirmAndQuit();
}
});
}
private void setupKeyboardShortcuts() {
// Ctrl+N et Ctrl+S déjà gérés via setAccelerator dans le menu
}
private void confirmAndQuit() {
int choice = JOptionPane.showConfirmDialog(
this,
"Sauvegarder avant de quitter ?",
"Quitter TaskFlow",
JOptionPane.YES_NO_CANCEL_OPTION
);
if (choice == JOptionPane.YES_OPTION) {
pc.saveAll();
System.exit(0);
} else if (choice == JOptionPane.NO_OPTION) {
System.exit(0);
}
// CANCEL → ne rien faire, rester dans l'app
}
private void showNewProjectDialog() {
String name = JOptionPane.showInputDialog(
this, "Nom du projet :",
"Nouveau projet", JOptionPane.PLAIN_MESSAGE);
if (name != null && !name.trim().isEmpty())
pc.createProject(name.trim(), "");
}
private void showStats() {
Project current = pc.getCurrentProject();
if (current == null) {
JOptionPane.showMessageDialog(this,
"Selectionne d'abord un projet.",
"Aucun projet", JOptionPane.INFORMATION_MESSAGE);
return;
}
JDialog dlg = new JDialog(this,
"Statistiques — " + current.getName(), true);
dlg.setSize(600, 460);
dlg.setLocationRelativeTo(this);
dlg.add(new StatsPanel(current));
dlg.setVisible(true);
}
// ======= ProjectListener =======
@Override
public void onProjectListChanged() {
sidebar.refresh();
int n = pc.getProjects().size();
setStatus(n + " projet(s) chargé(s).");
}
@Override
public void onProjectSelected(Project project) {
kanban.loadProject(project);
// Titre dynamique : "TaskFlow — Mon Projet"
setTitle(APP_NAME + " — " + project.getName());
setStatus("Projet \"" + project.getName() + "\" — "
+ project.getTotalTaskCount() + " tâche(s)"
+ " | Complétion : "
+ String.format("%.0f%%", project.getCompletionRate()));
}
@Override
public void onTaskChanged(Project project, Task task) {
kanban.refresh();
setStatus("Tâche \"" + task.getTitle() + "\" mise à jour."
+ " | Complétion : "
+ String.format("%.0f%%", project.getCompletionRate()));
}
}