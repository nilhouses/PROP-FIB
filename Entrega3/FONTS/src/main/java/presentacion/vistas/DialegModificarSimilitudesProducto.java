package presentacion.vistas;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dominio.Pair;
import dominio.Producto;

public class DialegModificarSimilitudesProducto {

    //Muestra un dialogo que permtie al usuario indicar a que producto quiere modificar sus similitudes.
    public Pair<String, String> mostrardialeg() {
        JTextField NomProd1 = new JTextField();
        JTextField MarcaProd1 = new JTextField();

        // Imagen
        ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono3.png");
        ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        // Creamos el panel con los datos
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalStrut(10)); // Separador
        panel.add(new JLabel("Nombre del producto:"));
        panel.add(NomProd1);
        panel.add(Box.createVerticalStrut(10)); // Separador
        panel.add(new JLabel("Marca del producto:"));
        panel.add(MarcaProd1);

        Pair<String, String> result = null;

        // Bucle per mantenir el diàleg obert fins que l'usuari ompli els camps
        // correctament
        while (result == null) {
            int opcion = JOptionPane.showOptionDialog(
                    null,
                    panel,
                    "Modificar los grados de similitud de un producto",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    scaledIcon,
                    new Object[] { "OK" },
                    "OK");

            if (opcion == JOptionPane.OK_OPTION) {
                String nombre = NomProd1.getText().trim();
                String marca = MarcaProd1.getText().trim();

                if (nombre.isEmpty() || marca.isEmpty()) { // Si algun input està buit
                    ImageIcon icon2 = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                ImageIcon scaledIcon2 = new ImageIcon(
                        icon2.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                    JOptionPane.showMessageDialog(
                            null,
                            "Todos los campos deben ser llenados.",
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

    //Muestra un dialogo que permite al usuario insertar el nuevo grado de similitud entre los dos productos indicados.
    public Float modsimilitud(Producto p1, Producto p2) {
        JTextField inputSim = new JTextField();
        Float f = null;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalStrut(10)); // Separador
        panel.add(new JLabel("Proporcione el nuevo grado de similitud entre 0.0 y 1.0 para " + p1.getNom() + " "
                + p1.getMarca() + " y " + p2.getNom() + " " + p2.getMarca()));
        panel.add(inputSim);

        int opcion = JOptionPane.showOptionDialog(
                null,
                panel,
                "Modifica Grado Similitud",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[] { "OK" },
                "OK");
        if (opcion == JOptionPane.OK_OPTION) {
            String sim = inputSim.getText().trim();

            if (sim.isEmpty()) { // Si algun input està buit
                ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                ImageIcon scaledIcon = new ImageIcon(
                        icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(
                        null,
                        "Por favor inserta un valor.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE,
                        scaledIcon);
                return -1.0f;
            } else {
                try {
                    f = Float.parseFloat(sim);
                    if (f >= 0.0f && f <= 1.0f) {
                        return f;
                    } else {
                        ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                ImageIcon scaledIcon = new ImageIcon(
                        icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                        JOptionPane.showMessageDialog(
                                null,
                                "Inserte un valor entre 0.0 y 1.0.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE,
                                scaledIcon);
                        return -1.0f;
                    }
                } catch (NumberFormatException e) {
                    ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                    ImageIcon scaledIcon = new ImageIcon(
                            icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                    // Manejar errors de format
                    JOptionPane.showMessageDialog(null,
                            "El formato tiene que ser un float: 'Entero.Decimal'. \n Ej: 0.5",
                            "Error",
                            JOptionPane.ERROR_MESSAGE,
                            scaledIcon);
                    return -1.0f;
                }
            }
        }
        return null;
    }
}
