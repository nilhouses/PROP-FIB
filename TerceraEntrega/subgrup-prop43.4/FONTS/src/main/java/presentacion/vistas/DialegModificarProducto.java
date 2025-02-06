package presentacion.vistas;

import dominio.Pair;
import javax.swing.*;

public class DialegModificarProducto {

    //Muestra un dialogo que permite al usuario indicar cual producto quiere modificar y con que nombre y marca lo quiere sustituir.
    public Pair<Pair<String,String>,Pair<String, String>> mostrardialeg() {
        JTextField inputNombreAntic = new JTextField();
        JTextField inputMarcaAntic = new JTextField();
        JTextField inputNombreNou = new JTextField();
        JTextField inputMarcaNou = new JTextField();
        
        // Imagen
        ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono3.png");
        ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        // Creamos el panel con los datos
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Nombre del producto:"));
        panel.add(inputNombreAntic);
        panel.add(new JLabel("Marca del producto:"));
        panel.add(inputMarcaAntic);

        panel.add(new JLabel("Nuevo nombre:"));
        panel.add(inputNombreNou);

        panel.add(new JLabel("Nueva marca:"));
        panel.add(inputMarcaNou);

        Pair<Pair<String,String>,Pair<String, String>> result = null;

        // Bucle per mantenir el diàleg obert fins que l'usuari ompli els camps correctament
        while (result == null) {
            int opcion = JOptionPane.showOptionDialog(
                null,
                panel,
                "Modificar Producto",
                JOptionPane.DEFAULT_OPTION,  
                JOptionPane.PLAIN_MESSAGE,
                scaledIcon, 
                new Object[] { "OK" }, 
                "OK"   
            );

            if (opcion == JOptionPane.OK_OPTION) {
                String nombreA = inputNombreAntic.getText().trim();
                String marcaA = inputMarcaAntic.getText().trim();
                String nombreN = inputNombreNou.getText().trim();
                String marcaN = inputMarcaNou.getText().trim();

                if (nombreA.isEmpty() || marcaA.isEmpty() ||nombreN.isEmpty() || marcaN.isEmpty()) { // Si algun input està buit
                    ImageIcon icon2 = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                    ImageIcon scaledIcon2 = new ImageIcon(
                        icon2.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                    JOptionPane.showMessageDialog(
                        null,
                        "Todos los campos deben ser llenados.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE,
                        scaledIcon2
                    );
                } else {
                    result = new Pair<>(new Pair<>(nombreA,marcaA), new Pair<>(nombreN, marcaN)); // Si els camps són vàlids, sortim del bucle
                }
            } else {
                return null; // Si es prem el botó de cancel·lar, retorna null
            }
        }
        return result;
    }
}
