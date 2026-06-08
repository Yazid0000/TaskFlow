package view;
import controller.TaskController;
import model.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
public class ColumnPanel extends JPanel {
private static final long serialVersionUID = 1L;
private final Project project;
private final Column column;
private final TaskController tc = TaskController.getInstance();
private JPanel cardsPanel; // contient les TaskCard
public ColumnPanel(Project project, Column column) {
this.project = project;
this.column = column;
setLayout(new BorderLayout(0, 8));
setBackground(new Color(225, 228, 240));
setBorder(new CompoundBorder(
new LineBorder(new Color(210, 213, 230), 1, true),
new EmptyBorder(0, 0, 8, 0)
));
add(buildHeader(), BorderLayout.NORTH);
add(buildCards(), BorderLayout.CENTER);
add(buildAddBtn(), BorderLayout.SOUTH);
}
private JPanel buildHeader() {
JPanel header = new JPanel(new BorderLayout());
// Couleur d'en-tête depuis le modèle Column
Color headerColor = Color.decode(column.getColor());
header.setBackground(headerColor);
header.setBorder(new EmptyBorder(10, 14, 10, 14));
// Nom + compteur de tâches
JLabel lblName = new JLabel(column.getName());
lblName.setFont(new Font("SansSerif", Font.BOLD, 13));
lblName.setForeground(new Color(40, 40, 70));
JLabel lblCount = new JLabel(String.valueOf(column.getTaskCount()));
lblCount.setFont(new Font("SansSerif", Font.BOLD, 12));
lblCount.setForeground(new Color(100, 100, 130));
header.add(lblName, BorderLayout.CENTER);
header.add(lblCount, BorderLayout.EAST);
return header;
}
private JScrollPane buildCards() {
cardsPanel = new JPanel();
cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
cardsPanel.setBackground(new Color(225, 228, 240));
cardsPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
// Créer une TaskCard pour chaque tâche de la colonne
for (Task task : column.getTasks()) {
cardsPanel.add(new TaskCard(project, column, task));
cardsPanel.add(Box.createVerticalStrut(6)); // espace entre cartes
}
JScrollPane scroll = new JScrollPane(cardsPanel);
scroll.setBorder(null);
scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
return scroll;
}
private JPanel buildAddBtn() {
    JPanel footer = new JPanel(new BorderLayout());
    footer.setBackground(new Color(225, 228, 240));
    footer.setBorder(new EmptyBorder(0, 8, 0, 8));

    JButton btn = new JButton("＋ Ajouter une tâche");
    btn.setFocusPainted(false);
    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    btn.setPreferredSize(new Dimension(200, 38));
    btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
    btn.addActionListener(e -> showAddTaskDialog());
    footer.add(btn, BorderLayout.CENTER);
    return footer;
}
private void showAddTaskDialog() {
String title = JOptionPane.showInputDialog(
this, "Titre de la tache :",
"Nouvelle tache", JOptionPane.PLAIN_MESSAGE
);
if (title != null && !title.trim().isEmpty()) {
tc.addTask(project, column, title.trim(), "", Priority.MEDIUM);
}
}
}