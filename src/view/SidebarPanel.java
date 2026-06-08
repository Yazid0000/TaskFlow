package view;
import controller.ProjectController;
import model.Project;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
/**
* Panneau latéral gauche : liste des projets + bouton nouveau projet.
*/
public class SidebarPanel extends JPanel {
	private static final long serialVersionUID = 1L;
private final ProjectController pc = ProjectController.getInstance();
private final DefaultListModel<Project> listModel = new DefaultListModel<>();
private final JList<Project> projectList = new JList<>(listModel);
public SidebarPanel() {
setLayout(new BorderLayout());
setPreferredSize(new Dimension(220, 0));
setBackground(new Color(245, 245, 250));
setBorder(new MatteBorder(0, 0, 0, 1, new Color(220, 220, 225)));
add(buildHeader(), BorderLayout.NORTH);
add(buildList(), BorderLayout.CENTER);
add(buildFooter(), BorderLayout.SOUTH);
refresh(); // charge les projets existants dès l'ouverture
}
private JPanel buildHeader() {
JPanel header = new JPanel(new BorderLayout());
header.setBackground(new Color(245, 245, 250));
header.setBorder(new EmptyBorder(14, 14, 8, 14));
JLabel title = new JLabel("📋 Mes Projets");
title.setFont(new Font("SansSerif", Font.BOLD, 13));
title.setForeground(new Color(60, 60, 80));
header.add(title, BorderLayout.CENTER);
return header;
}
private JScrollPane buildList() {
// Rendu personnalisé de chaque élément de la liste
projectList.setCellRenderer(new ProjectCellRenderer());
projectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
projectList.setBackground(new Color(245, 245, 250));
projectList.setFixedCellHeight(44);
projectList.setBorder(new EmptyBorder(4, 8, 4, 8));
// Sélection → notifier le contrôleur
projectList.addListSelectionListener(e -> {
if (!e.getValueIsAdjusting()) {
Project selected = projectList.getSelectedValue();
if (selected != null) pc.selectProject(selected);
}
});
// Menu contextuel clic droit
projectList.addMouseListener(new MouseAdapter() {
@Override
public void mousePressed(MouseEvent e) {
if (e.isPopupTrigger()) showContextMenu(e);
}
@Override
public void mouseReleased(MouseEvent e) {
if (e.isPopupTrigger()) showContextMenu(e);
}
});
JScrollPane scroll = new JScrollPane(projectList);
scroll.setBorder(null);
return scroll;
}
private JPanel buildFooter() {
JPanel footer = new JPanel(new BorderLayout());
footer.setBackground(new Color(245, 245, 250));
footer.setBorder(new EmptyBorder(8, 8, 8, 8));
JButton btnNew = new JButton("+ Nouveau projet");
btnNew.setFocusPainted(false);
btnNew.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
btnNew.addActionListener(e -> {
String name = JOptionPane.showInputDialog(
SidebarPanel.this, "Nom du projet :",
"Nouveau projet", JOptionPane.PLAIN_MESSAGE
);
if (name != null && !name.trim().isEmpty()) {
pc.createProject(name.trim(), "");
}
});
footer.add(btnNew, BorderLayout.CENTER);
return footer;
}
private void showContextMenu(MouseEvent e) {
int index = projectList.locationToIndex(e.getPoint());
if (index < 0) return;
projectList.setSelectedIndex(index);
Project p = projectList.getSelectedValue();
JPopupMenu menu = new JPopupMenu();
JMenuItem itemDelete = new JMenuItem("🗑 Supprimer ce projet");
itemDelete.setForeground(new Color(180, 40, 40));
itemDelete.addActionListener(ev -> {
int confirm = JOptionPane.showConfirmDialog(
SidebarPanel.this,
"Supprimer le projet \"" + p.getName() + "\" ?",
"Confirmation", JOptionPane.YES_NO_OPTION
);
if (confirm == JOptionPane.YES_OPTION) pc.deleteProject(p);
});
menu.add(itemDelete);
menu.show(projectList, e.getX(), e.getY());
}
// Appelé par MainWindow quand la liste change
public void refresh() {
listModel.clear();
for (Project p : pc.getProjects()) {
listModel.addElement(p);
}
}
// ---- Renderer personnalisé pour chaque ligne de projet ----
private static class ProjectCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
@Override
public Component getListCellRendererComponent(
JList<?> list, Object value, int index,
boolean isSelected, boolean cellHasFocus) {
super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
Project p = (Project) value;
setText("📁 " + p.getName());
setFont(new Font("SansSerif", Font.PLAIN, 13));
setBorder(new EmptyBorder(6, 10, 6, 10));
if (isSelected) {
setBackground(new Color(210, 225, 255));
setForeground(new Color(30, 60, 130));
} else {
setBackground(new Color(245, 245, 250));
setForeground(new Color(60, 60, 80));
}
return this;
}
}
}