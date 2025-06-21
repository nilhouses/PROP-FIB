package presentacion.vistas;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dominio.Pair;

public class DialegConsultaSimilitudProductos {

    //Muestra un dialogo al usuario para que indique de cuales productos quiere ver el grado de similitud.
    public Pair<Pair<String,String>,Pair<String, String>> mostrardialeg() {
        JTextField NomProd1 = new JTextField();
        JTextField MarcaProd1 = new JTextField();
        JTextField NomProd2 = new JTextField();
        JTextField MarcaProd2 = new JTextField();
        
        // Imagen
        ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono3.png");
        ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        // Creamos el panel con los datos
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Nombre del primer producto:"));
        panel.add(NomProd1);
        panel.add(new JLabel("Marca del primer producto:"));
        panel.add(MarcaProd1);

        panel.add(new JLabel("Nombre del segundo producto:"));
        panel.add(NomProd2);

        panel.add(new JLabel("Marca del segundo producto:"));
        panel.add(MarcaProd2);

        Pair<Pair<String,String>,Pair<String, String>> result = null;

        // Bucle per mantenir el diàleg obert fins que l'usuari ompli els camps correctament
        while (result == null) {
            int opcion = JOptionPane.showOptionDialog(
                null,
                panel,
                "Consulta el grado de similitud entre dos productos",
                JOptionPane.DEFAULT_OPTION,  
                JOptionPane.PLAIN_MESSAGE,
                scaledIcon, 
                new Object[] { "OK" }, 
                "OK"   
            );

            if (opcion == JOptionPane.OK_OPTION) {
                String nombreA = NomProd1.getText().trim();
                String marcaA = MarcaProd1.getText().trim();
                String nombreN = NomProd2.getText().trim();
                String marcaN = MarcaProd2.getText().trim();

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

    //Muestra el string indicado al usuario mediante un dialogo.
    public void mostrarconsulta(String similitud)
    {
        
        //Objetos
        ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono2.png");
        ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
    
        // Mostrar el diàleg
        JOptionPane.showMessageDialog(
            null,
            similitud,
            "Consulta Productos",
            JOptionPane.INFORMATION_MESSAGE,
            scaledIcon 
        );
    }
}
