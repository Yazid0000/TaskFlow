package view;
import controller.TaskController;
import model.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
public class TaskCard extends JPanel {
private static final long serialVersionUID = 1L;
private final Project project;
private final Column column;
private final Task task;
private final TaskController tc = TaskController.getInstance();
public TaskCard(Project project, Column column, Task task) {
this.project = project;
this.column = column;
this.task = task;
setLayout(new BorderLayout(6, 4));
setBackground(Color.WHITE);
setBorder(new CompoundBorder(
new LineBorder(new Color(220, 222, 235), 1, true),
new EmptyBorder(8, 10, 8, 10)
));
setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
buildContent();
addInteractions();
}
private void buildContent() {
// Titre de la tâche
JLabel lblTitle = new JLabel(task.getTitle());
lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
lblTitle.setForeground(new Color(30, 30, 50));
// Puce de priorité colorée
JLabel lblPriority = new JLabel(task.getPriority().getLabel());
lblPriority.setFont(new Font("SansSerif", Font.BOLD, 10));
lblPriority.setForeground(Color.decode(task.getPriority().getColor()));
// Indicateur retard ou pièce jointe
JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
bottom.setBackground(Color.WHITE);
if (task.isOverdue()) {
JLabel overdue = new JLabel(" [EN RETARD]");
overdue.setFont(new Font("SansSerif", Font.BOLD, 10));
overdue.setForeground(new Color(180, 40, 40));
bottom.add(overdue);
}
if (task.hasAttachment()) {
JLabel att = new JLabel(" [PJ]");
att.setFont(new Font("SansSerif", Font.ITALIC, 10));
att.setForeground(new Color(80, 120, 200));
bottom.add(att);
}
add(lblTitle, BorderLayout.NORTH);
add(lblPriority, BorderLayout.CENTER);
add(bottom, BorderLayout.SOUTH);
}
private void addInteractions() {
addMouseListener(new MouseAdapter() {
@Override
public void mouseClicked(MouseEvent e) {
if (e.getClickCount() == 2) { // double-clic → éditer
new TaskDialog(
SwingUtilities.getWindowAncestor(TaskCard.this),
project, task
).setVisible(true);
}
}
@Override
public void mouseEntered(MouseEvent e) { // survol → légère ombre
setBackground(new Color(245, 247, 255));
}
@Override
public void mouseExited(MouseEvent e) { // fin survol → blanc
setBackground(Color.WHITE);
}
@Override
public void mousePressed(MouseEvent e) {
if (e.isPopupTrigger()) showContextMenu(e);
}
@Override
public void mouseReleased(MouseEvent e) {
if (e.isPopupTrigger()) showContextMenu(e);
}
});
}
private void showContextMenu(MouseEvent e) {
JPopupMenu menu = new JPopupMenu();
// Sous-menu "Déplacer vers..."
JMenu moveMenu = new JMenu("Deplacer vers...");
for (Column col : project.getColumns()) {
if (col == column) continue; // pas la colonne actuelle
JMenuItem item = new JMenuItem(col.getName());
item.addActionListener(ev -> tc.moveTask(project, task, col));
moveMenu.add(item);
}
// Item supprimer
JMenuItem itemDelete = new JMenuItem("Supprimer");
itemDelete.setForeground(new Color(180, 40, 40));
itemDelete.addActionListener(ev -> {
int ok = JOptionPane.showConfirmDialog(
this, "Supprimer \"" + task.getTitle() + "\" ?",
"Confirmation", JOptionPane.YES_NO_OPTION
);
if (ok == JOptionPane.YES_OPTION) tc.deleteTask(project, task);
});
menu.add(moveMenu);
menu.addSeparator();
menu.add(itemDelete);
menu.show(this, e.getX(), e.getY());
}
}