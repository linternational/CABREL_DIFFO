package cabrel.diffo;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Menu extends JFrame {
    private JTextField titreField;
    private JTextField auteurField;
    private JTable table;
    private GestionFichiers gestionFichiers;

    public Menu() {
        gestionFichiers = new GestionFichiers();
        initialiser();
    }

    private void initialiser() {
        setTitle("Gestion de Location de Bikes");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel des champs de texte
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Titre:"));
        titreField = new JTextField();
        panel.add(titreField);
        panel.add(new JLabel("Auteur:"));
        auteurField = new JTextField();
        panel.add(auteurField);

        JButton ajouterButton = new JButton("Ajouter");
        ajouterButton.addActionListener(e -> ajouterBike());
        panel.add(ajouterButton);

        add(panel, BorderLayout.NORTH);

        // Table des bikes
        table = new JTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton supprimerButton = new JButton("Supprimer");
        supprimerButton.addActionListener(e -> supprimerBike());
        add(supprimerButton, BorderLayout.SOUTH);

        mettreAJourTable();
    }

    private void ajouterBike() {
        try {
            String titre = titreField.getText();
            String auteur = auteurField.getText();
            Bike bike = new Bike(titre, auteur);
            gestionFichiers.ajouterBike(bike);
            mettreAJourTable();
            titreField.setText("");
            auteurField.setText("");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout du bike.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerBike() {
        try {
            int row = table.getSelectedRow();
            if (row != -1) {
                String titre = table.getValueAt(row, 0).toString();
                gestionFichiers.supprimerBike(titre);
                mettreAJourTable();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du bike.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mettreAJourTable() {
        try {
            java.util.List<Bike> bikes = gestionFichiers.lireBikes();
            String[][] data = new String[bikes.size()][2];
            for (int i = 0; i < bikes.size(); i++) {
                data[i][0] = bikes.get(i).getTitre();
                data[i][1] = bikes.get(i).getAuteur();
            }
            String[] columnNames = {"Titre", "Auteur"};
            table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la lecture des bikes.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu menu = new Menu();
            menu.setVisible(true);
        });
    }
}
