package view;
import model.*;
import javax.swing.*;
import java.awt.*;
/**
* Panneau central affichant les colonnes Kanban d'un projet.
*/
public class KanbanPanel extends JPanel {
private static final long serialVersionUID = 1L;
private Project currentProject;
private JPanel columnsContainer; // contient les ColumnPanel
private JLabel placeholder;
public KanbanPanel() {
setLayout(new BorderLayout());
setBackground(new Color(235, 238, 245));
// Message affiché quand aucun projet n'est sélectionné
placeholder = new JLabel(
"<- Selectionne un projet pour afficher le Kanban",
SwingConstants.CENTER
);
placeholder.setFont(new Font("SansSerif", Font.PLAIN, 14));
placeholder.setForeground(new Color(150, 150, 170));
add(placeholder, BorderLayout.CENTER);
// Conteneur horizontal des colonnes
columnsContainer = new JPanel();
columnsContainer.setLayout(new GridLayout(1, 0, 12, 0));
columnsContainer.setBackground(new Color(235, 238, 245));
columnsContainer.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
}
// Charge et affiche les colonnes d'un projet
public void loadProject(Project project) {
this.currentProject = project;
// Remplace le placeholder par les colonnes
remove(placeholder);
add(columnsContainer, BorderLayout.CENTER);
refresh();
}
// Redessine toutes les colonnes
public void refresh() {
if (currentProject == null) return;
columnsContainer.removeAll();
for (Column col : currentProject.getColumns()) {
columnsContainer.add(new ColumnPanel(currentProject, col));
}
columnsContainer.revalidate();
columnsContainer.repaint();
}
}