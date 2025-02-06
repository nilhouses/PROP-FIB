package presentacion.vistas;

import dominio.ControladoraDominio;
import dominio.Usuario;
import dominio.excepciones.ContrasenaIncorrectaException;
import dominio.excepciones.SobreescribirUsuarioException;
import dominio.excepciones.UsuarioActivoException;
import dominio.excepciones.UsuarioExistenteException;
import dominio.excepciones.UsuarioNoExistenteException;
import dominio.excepciones.DialogoSobreescrituraException;
import presentacion.ControladoraPresentacion;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.io.File;
import java.net.SocketException;

//Ventana que se encarga de gestionar el input/output del usuario en la ventana de login
public class VistaLogin extends JFrame {

    private ControladoraDominio ctrlDominio;            //instancia de la controladora del dominio
    private ControladoraPresentacion ctrlPresentacion;  //instancia de la controladora de presentacion

    //Creadora, configura todos los parametros y esconde las ventanas hasta que el usuario haya completado la carga (o no) de datos
    public VistaLogin(ControladoraDominio ctrlDominio,  ControladoraPresentacion ctrlPresentacion) {
        super("No hay ninguna sesión iniciada");
        configuraFrame();
        this.ctrlDominio = ctrlDominio;
        this.ctrlPresentacion = ctrlPresentacion;
    }
    //Muestra la ventana previamente configurada
    public void abrirVentana() {
        this.setVisible(true);
    }
    //Esconde la ventana
    public void cerrarVentana() {
        this.setVisible(false);
    }

    //Metodo privado que se encarga de gestionar el formato de la ventana de login
    private void configuraFrame() {

        // Ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300); // Tamaño
        setLocationRelativeTo(null); // Centrar

        //Objetos
        //1) Username y password
        JTextField usernameField = new JTextField(15); 
        JPasswordField passwordField = new JPasswordField(15); 
        //2) Botones con su panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");
        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);
        JPanel datosPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton cargarUsuarioButton = new JButton("Cargar Usuario");
        cargarUsuarioButton.setBackground(new Color(211, 211, 211));//color gris
        datosPanel.add(cargarUsuarioButton);
        JPanel borrarDatosPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton borrarDatosButton = new JButton("Borrar Usuarios");
        borrarDatosButton.setBackground(new Color(255, 99, 71));
        borrarDatosPanel.add(borrarDatosButton);
        //3) Panel principal, 
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new java.awt.Insets(5, 0, 5, 0); // Espacio entre componentes

        // Username
        c.gridx = 0; 
        c.gridy = 0; 
        
        panel.add(new JLabel("Username:"), c);

        c.gridx = 1;
        c.gridy = 0;
        panel.add(usernameField, c);

        // Password
        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("Password:"), c);

        c.gridx = 1;
        c.gridy = 1;
        panel.add(passwordField, c);

        // Botones
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2; // Ocupa 2 columnas
        c.insets = new java.awt.Insets(20, 0, 0, 0); // Espacio entre credenciales y botones
        panel.add(buttonPanel, c);
        
        //cargar usuario
        c.gridx = 0;
        c.gridy = 3;
        panel.add(datosPanel,c);
        this.add(panel);

        //borrar usuarios
        c.gridx = 0;
        c.gridy = 4;
        panel.add(borrarDatosPanel,c);

        this.add(panel);

        // Funcion Login
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() & password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Usuario y contraseña inválidos", "ERROR", JOptionPane.ERROR_MESSAGE);
            }else if (username.isEmpty() ) {
                JOptionPane.showMessageDialog(this, "Usuario inválido", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else if (password.isEmpty() ) {
                JOptionPane.showMessageDialog(this, "Contraseña inválida", "ERROR", JOptionPane.ERROR_MESSAGE);
            }else {
                try {
                    if (ctrlDominio.login(username, password)) {
                        usernameField.setText("");
                        passwordField.setText("");
                        this.cerrarVentana(); // Cerrar la ventana
                        this.ctrlPresentacion.abreMenuPrincipal(username);
                    }
                } catch (UsuarioActivoException | ContrasenaIncorrectaException | UsuarioNoExistenteException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                } 
            }
        });

        // Funcion SignUp
        signUpButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            Usuario user = new Usuario(username, password);

            if (username.isEmpty() & password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Usuario y contraseña inválidos", "ERROR", JOptionPane.ERROR_MESSAGE);
            }else if (username.isEmpty() ) {
                JOptionPane.showMessageDialog(this, "Usuario inválido", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else if (password.isEmpty() ) {
                JOptionPane.showMessageDialog(this, "Contraseña inválida", "ERROR", JOptionPane.ERROR_MESSAGE);
            }else try {
                if (ctrlDominio.signUp(user)) {
                    usernameField.setText("");
                    passwordField.setText("");
                    this.cerrarVentana(); // Cerrar la ventana
                    this.ctrlPresentacion.abreMenuPrincipal(username);
                } 
            } catch (UsuarioActivoException | UsuarioExistenteException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } 
        });

        cargarUsuarioButton.addActionListener(e -> cargarUsuarioDesdeArchivo());
        borrarDatosButton.addActionListener(e -> {

            String[] options = {"SÍ", "NO"};
            ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono6.png");
            ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
            int option = JOptionPane.showOptionDialog(
                null, 
                "Va a eliminar todos los usuarios ¿Está seguro?", 
                "ATENCIÓN", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE, 
                scaledIcon,
                options,
                options[0] 
            );
            if (option == JOptionPane.YES_OPTION) {
                ctrlDominio.borrarInfo();
            }
        });
        this.cerrarVentana();
    }

        //Funcionalidad de leer informacion de ficheros
        private void cargarUsuarioDesdeArchivo() {
            // Abre un cuadro de diálogo de selección de archivo
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Selecciona el archivo del usuario");
            
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                // Obten el archivo seleccionado
                File file = fileChooser.getSelectedFile();
                try {
                    String filePath = file.getAbsolutePath();
                    ctrlPresentacion.cargarUsuarioDesdeArchivo(filePath);
                    // Mostrar mensaje de exito
                    JOptionPane.showMessageDialog(this, "Usuario cargado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } catch (DialogoSobreescrituraException ex)  {
                    int overwriteOption = JOptionPane.showConfirmDialog(this,
                    "El usuario ya existe. ¿Quiere sobreescribirlo?",
                    "Confirmar sobreescritura",
                    JOptionPane.YES_NO_OPTION);
                    if (overwriteOption == JOptionPane.YES_OPTION) {
                        String filePath = file.getAbsolutePath();
                        try {
                            ctrlDominio.SobreescribirUsuario(filePath);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, "Error al cargar el usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        } 
                    } else {
                        JOptionPane.showMessageDialog(this, "Operación cancelada, el usuario antiguo se mantiene", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al cargar el usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } 
            }
        }
}
