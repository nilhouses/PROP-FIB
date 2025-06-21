package presentacion.vistas;

import dominio.ControladoraDominio;
import dominio.Usuario;

import javax.swing.*;

public class DialogoCargarDatos {

    //Muestra un dialogo que permite al usuario cargar los datos de todos los usuarios que conoce el sistema
    public DialogoCargarDatos(ControladoraDominio ctrlDominio) {
        
        //Objetos
        Object[] buttons = {"Sí", "No"};
        ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono1.png");
        ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));//usamos awt unicamente para reescalar el icono

        int pushed = JOptionPane.showOptionDialog(null,
            "¿Quiere cargar la informacion de la Base de Datos?", 
            "Datos antiguos encontrados", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            scaledIcon, 
            buttons, 
            buttons[0]);

        
        if (pushed == 0) { // "SI"
            ctrlDominio.cargarInfo();
        } 
    }
}
