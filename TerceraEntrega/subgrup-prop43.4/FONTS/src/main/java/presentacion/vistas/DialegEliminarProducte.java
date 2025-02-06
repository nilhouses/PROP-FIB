package presentacion.vistas;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dominio.Pair;

public class DialegEliminarProducte {

    //Muestra un dialogo al usuario que le permite indicar cual producto quiere eliminar.
    public Pair<String, String> mostrardialeg() {
        JTextField inputNombre = new JTextField();
        JTextField inputMarca = new JTextField();

        // Imagen
        ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono3.png");
        ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        // Creamos el panel con los datos
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalStrut(10)); // Separador
        panel.add(new JLabel("Nombre del producto:"));
        panel.add(inputNombre);
        panel.add(Box.createVerticalStrut(10)); // Separador
        panel.add(new JLabel("Marca del producto:"));
        panel.add(inputMarca);

        Pair<String, String> result = null;

        // Bucle per mantenir el diàleg obert fins que l'usuari ompli els camps
        // correctament
        while (result == null) {
            int opcion = JOptionPane.showOptionDialog(
                    null,
                    panel,
                    "Eliminar Producto",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    scaledIcon,
                    new Object[] { "OK" },
                    "OK");

            if (opcion == JOptionPane.OK_OPTION) {
                String nombre = inputNombre.getText().trim();
                String marca = inputMarca.getText().trim();

                if (nombre.isEmpty() || marca.isEmpty()) { // Si algun input està buit
                    ImageIcon icon2 = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                ImageIcon scaledIcon2 = new ImageIcon(
                        icon2.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                    JOptionPane.showMessageDialog(
                            null,
                            "Ambos campos deben ser llenados.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE,
                            scaledIcon2);
                } else {
                    result = new Pair<>(nombre, marca); // Si els camps són vàlids, sortim del bucle
                }
            } else {
                return null; // Si es prem el botó de cancel·lar, retorna null
            }
        }
        return result;
    }
}
