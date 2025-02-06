package presentacion.vistas;

import dominio.Pair;
import javax.swing.*;

public class DialogoExisteProducto {
    
    //Muestra la existencia de un producto, que el usuario indicara
    public Pair<String, String> mostrarDialogo() {
        JTextField inputNombre = new JTextField();
        JTextField inputMarca = new JTextField();
        
        // Imagen
        ImageIcon icon;
        ImageIcon scaledIcon;

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

        // Bucle para mantener el dialogo abierto hasta que el usuario llene los campos correctamente
        while (result == null) {
            icon = new ImageIcon("src/main/java/presentacion/iconos/icono3.png");
            scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
            int opcion = JOptionPane.showOptionDialog(
                null,
                panel,
                "Existe Producto",
                JOptionPane.DEFAULT_OPTION,  
                JOptionPane.PLAIN_MESSAGE,
                scaledIcon, 
                new Object[] { "OK" }, 
                "OK"   
            );

            if (opcion == JOptionPane.OK_OPTION) {
                String nombre = inputNombre.getText().trim();
                String marca = inputMarca.getText().trim();

                if (nombre.isEmpty() || marca.isEmpty()) { // Algun input vacio
                    icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                    scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                    JOptionPane.showMessageDialog(
                        null,
                        "Ambos campos deben ser llenados.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE,
                        scaledIcon);
                } else {
                    result = new Pair<>(nombre, marca); // Si los campos son validos, salimos del bucle
                }
            } else {
                return null; // si se cancela devuelve null
            }
        }
        return result;
    }
}
