package view;
import model.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;
/**
* Panneau de statistiques d'un projet.
* Dessine un graphique à barres avec Graphics2D.
*/
public class StatsPanel extends JPanel {
private static final long serialVersionUID = 1L;
private Project project;
public StatsPanel(Project project) {
this.project = project;
setLayout(new BorderLayout(0, 16));
setBackground(Color.WHITE);
setBorder(new EmptyBorder(20, 24, 20, 24));
add(buildSummary(), BorderLayout.NORTH);
add(buildChart(), BorderLayout.CENTER);
}
// ======= Résumé chiffré en haut =======
private JPanel buildSummary() {
JPanel panel = new JPanel(new GridLayout(1, 3, 16, 0));
panel.setBackground(Color.WHITE);
// Calculs
int total = project.getTotalTaskCount();
int overdue = countOverdue();
double rate = project.getCompletionRate();
panel.add(buildStatCard("Total tâches",
String.valueOf(total), new Color(70, 130, 200)));
panel.add(buildStatCard("Complétion",
String.format("%.0f%%", rate), new Color(46, 160, 100)));
panel.add(buildStatCard("En retard",
String.valueOf(overdue), new Color(200, 70, 70)));
return panel;
}
// Construit une carte de statistique (chiffre + libellé)
private JPanel buildStatCard(String label, String value, Color color) {
JPanel card = new JPanel(new BorderLayout(0, 6));
card.setBackground(color);
card.setBorder(new EmptyBorder(16, 20, 16, 20));
JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
lblValue.setFont(new Font("SansSerif", Font.BOLD, 32));
lblValue.setForeground(Color.WHITE);
JLabel lblLabel = new JLabel(label, SwingConstants.CENTER);
lblLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
lblLabel.setForeground(new Color(255, 255, 255, 200));
card.add(lblValue, BorderLayout.CENTER);
card.add(lblLabel, BorderLayout.SOUTH);
return card;
}
// ======= Graphique à barres Java2D =======
private JPanel buildChart() {
// Classe interne anonyme qui surcharge paintComponent
JPanel chart = new JPanel() {
private static final long serialVersionUID = 1L;
@Override
protected void paintComponent(Graphics g) {
super.paintComponent(g);
drawBarChart((Graphics2D) g);
}
};
chart.setBackground(new Color(248, 249, 252));
chart.setBorder(new LineBorder(new Color(220, 222, 235)));
return chart;
}
private void drawBarChart(Graphics2D g2) {
// Antialiasing pour des bords lisses
g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
RenderingHints.VALUE_ANTIALIAS_ON);
List<Column> columns = project.getColumns();
int nbCols = columns.size();
int maxTasks = 1; // évite division par zéro
for (Column c : columns)
if (c.getTaskCount() > maxTasks) maxTasks = c.getTaskCount();
// Dimensions du graphique
int w = getWidth() - 48;
int h = getHeight() - 80;
int originX = 40;
int originY = 20 + h;
int barWidth = (w / nbCols) - 30;
int gap = (w / nbCols);
// Axe Y : lignes de grille horizontales
g2.setColor(new Color(220, 222, 235));
g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
BasicStroke.JOIN_MITER, 10, new float[]{4}, 0));
for (int i = 1; i <= maxTasks; i++) {
int y = originY - (i * h / maxTasks);
g2.drawLine(originX, y, originX + w, y);
g2.setColor(new Color(140, 140, 160));
g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
g2.drawString(String.valueOf(i), 4, y + 4);
g2.setColor(new Color(220, 222, 235));
}
// Axe X (ligne de base)
g2.setStroke(new BasicStroke(2));
g2.setColor(new Color(180, 182, 200));
g2.drawLine(originX, originY, originX + w, originY);
// Dessin des barres
for (int i = 0; i < nbCols; i++) {
Column col = columns.get(i);
int count = col.getTaskCount();
int barH = (count == 0) ? 0 : (count * h / maxTasks);
int barX = originX + 15 + i * gap;
int barY = originY - barH;
// Couleur de la barre = couleur de la colonne
Color barColor = Color.decode(col.getColor());
// Version plus saturée pour la barre
Color darker = barColor.darker();
// Dégradé vertical sur la barre
GradientPaint gp = new GradientPaint(
barX, barY, darker,
barX, originY, barColor
);
g2.setPaint(gp);
g2.fillRoundRect(barX, barY, barWidth, barH, 6, 6);
// Valeur au-dessus de la barre
g2.setColor(new Color(60, 60, 80));
g2.setFont(new Font("SansSerif", Font.BOLD, 13));
String val = String.valueOf(count);
int valX = barX + barWidth / 2
- g2.getFontMetrics().stringWidth(val) / 2;
g2.drawString(val, valX, barY - 6);
// Nom de la colonne sous l'axe X
g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
g2.setColor(new Color(100, 100, 120));
int lblX = barX + barWidth / 2
- g2.getFontMetrics().stringWidth(col.getName()) / 2;
g2.drawString(col.getName(), lblX, originY + 18);
}
}
// Compte les tâches en retard dans tous les colonnes
private int countOverdue() {
int count = 0;
for (Column col : project.getColumns())
for (Task t : col.getTasks())
if (t.isOverdue()) count++;
return count;
}
}