package presentacion.vistas;

import dominio.ControladoraDominio;

import javax.swing.*;

public class DialogoConsultaProductos {

    //Muestra un dialogo que muestra los productos que tiene almacenado ese usuario en la estanteria, junto con su respectivo orden
    public DialogoConsultaProductos (String productos) {
        //Objetos
        ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono2.png");
        ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        // Mostrar el diàleg
        JOptionPane.showMessageDialog(
            null,
            productos,
            "Consulta Productos",
            JOptionPane.INFORMATION_MESSAGE,
            scaledIcon 
        );
    
    }
}    

