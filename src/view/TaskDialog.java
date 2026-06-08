package view;
import controller.TaskController;
import model.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
/**
* Dialogue modal d'édition complète d'une tâche.
*/
public class TaskDialog extends JDialog {
private static final long serialVersionUID = 1L;
private final Project project;
private final Task task;
private final TaskController tc = TaskController.getInstance();
// Composants du formulaire
private JTextField fieldTitle;
private JTextArea fieldDesc;
private JComboBox<Priority> comboPriority;
private JSpinner spinnerDate;
private JLabel lblAttachment;
private File selectedFile;
public TaskDialog(Window owner, Project project, Task task) {
super(owner, "Modifier la tâche", ModalityType.APPLICATION_MODAL);
this.project = project;
this.task = task;
initUI();
populateFields(); // pré-remplit avec les données actuelles
setSize(460, 420);
setLocationRelativeTo(owner);
setResizable(false);
}
private void initUI() {
JPanel main = new JPanel(new BorderLayout(0, 0));
main.setBorder(new EmptyBorder(16, 20, 16, 20));
main.add(buildForm(), BorderLayout.CENTER);
main.add(buildButtons(), BorderLayout.SOUTH);
add(main);
}
private JPanel buildForm() {
JPanel form = new JPanel(new GridBagLayout());
GridBagConstraints gbc = new GridBagConstraints();
gbc.insets = new Insets(6, 4, 6, 4);
gbc.anchor = GridBagConstraints.WEST;
// --- Ligne 0 : Titre ---
gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
form.add(new JLabel("Titre :"), gbc);
gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
fieldTitle = new JTextField();
form.add(fieldTitle, gbc);
// --- Ligne 1 : Description ---
gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
gbc.anchor = GridBagConstraints.NORTHWEST;
form.add(new JLabel("Description :"), gbc);
gbc.gridx = 1; gbc.weightx = 1; gbc.weighty = 1;
gbc.fill = GridBagConstraints.BOTH;
fieldDesc = new JTextArea(4, 20);
fieldDesc.setLineWrap(true);
fieldDesc.setWrapStyleWord(true);
form.add(new JScrollPane(fieldDesc), gbc);
// --- Ligne 2 : Priorité ---
gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.weighty = 0;
gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
form.add(new JLabel("Priorité :"), gbc);
gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
comboPriority = new JComboBox<>(Priority.values());
form.add(comboPriority, gbc);
// --- Ligne 3 : Date d'échéance ---
gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
form.add(new JLabel("Échéance :"), gbc);
gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
SpinnerDateModel dateModel = new SpinnerDateModel();
spinnerDate = new JSpinner(dateModel);
spinnerDate.setEditor(new JSpinner.DateEditor(spinnerDate, "dd/MM/yyyy"));
form.add(spinnerDate, gbc);
// --- Ligne 4 : Pièce jointe ---
gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
form.add(new JLabel("Pièce jointe :"), gbc);
gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
JPanel attachPanel = new JPanel(new BorderLayout(6, 0));
lblAttachment = new JLabel("Aucune");
lblAttachment.setForeground(new Color(130, 130, 150));
JButton btnBrowse = new JButton("Parcourir...");
btnBrowse.addActionListener(e -> browseFile());
attachPanel.add(lblAttachment, BorderLayout.CENTER);
attachPanel.add(btnBrowse, BorderLayout.EAST);
form.add(attachPanel, gbc);
return form;
}
private JPanel buildButtons() {
JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
panel.setBorder(new EmptyBorder(10, 0, 0, 0));
JButton btnCancel = new JButton("Annuler");
btnCancel.addActionListener(e -> dispose());
JButton btnSave = new JButton("Sauvegarder");
btnSave.setFocusPainted(false);
btnSave.addActionListener(e -> saveAndClose());
// Raccourci clavier : Échap = Annuler
getRootPane().registerKeyboardAction(
e -> dispose(),
KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
JComponent.WHEN_IN_FOCUSED_WINDOW
);
panel.add(btnCancel);
panel.add(btnSave);
return panel;
}
// Pré-remplit les champs avec les valeurs actuelles de la tâche
private void populateFields() {
fieldTitle.setText(task.getTitle());
fieldDesc.setText(task.getDescription());
comboPriority.setSelectedItem(task.getPriority());
if (task.getDueDate() != null) {
// Convertir LocalDate → Date pour le JSpinner
Date date = Date.from(
task.getDueDate()
.atStartOfDay(ZoneId.systemDefault())
.toInstant()
);
spinnerDate.setValue(date);
}
if (task.hasAttachment()) {
lblAttachment.setText(task.getAttachment().getFileName());
lblAttachment.setForeground(new Color(70, 130, 200));
}
}
private void browseFile() {
JFileChooser fc = new JFileChooser();
fc.setDialogTitle("Choisir une image");
fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
"Images", "jpg", "jpeg", "png", "gif"
));
if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
selectedFile = fc.getSelectedFile();
lblAttachment.setText(selectedFile.getName());
lblAttachment.setForeground(new Color(70, 130, 200));
}
}
private void saveAndClose() {
String title = fieldTitle.getText().trim();
if (title.isEmpty()) {
JOptionPane.showMessageDialog(this,
"Le titre ne peut pas être vide.",
"Validation", JOptionPane.WARNING_MESSAGE
);
return; // ne ferme pas le dialogue
}
// Convertir Date du JSpinner → LocalDate
Date picked = (Date) spinnerDate.getValue();
LocalDate dueDate = picked.toInstant()
.atZone(ZoneId.systemDefault())
.toLocalDate();
// Mettre à jour la tâche via le contrôleur
tc.updateTask(
project, task,
title,
fieldDesc.getText().trim(),
(Priority) comboPriority.getSelectedItem(),
dueDate
);
// Attacher le fichier si sélectionné
if (selectedFile != null) {
tc.attachFile(project, task,
selectedFile.getName(),
selectedFile.getAbsolutePath()
);
}
dispose(); // ferme le dialogue
}
}