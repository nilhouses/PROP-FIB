package presentacion.vistas;

import dominio.ControladoraDominio;
import dominio.Usuario;
import dominio.excepciones.NoSesionIniciadaException;
import dominio.excepciones.PrintEstanteriaException;
import dominio.excepciones.EstanteriaVaciaException;
import presentacion.ControladoraPresentacion;
import presentacion.vistas.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

//Ventana que se encarga de gestionar el input/output del usuario en la ventana del menu principal
public class VistaMenuPrincipal extends JFrame{

    private ControladoraDominio ctrlDominio;            //instancia de la controladora del dominio
    private ControladoraPresentacion ctrlPresentacion;  //instancia de la controladora de presentacion
    private JLabel user;                                //texto que indica que usuario tiene esa sesion iniciada

    //Creadora, configura todos los parametros y esconde las ventanas hasta que el usuario haya completado el inicio de sesion
    public VistaMenuPrincipal(ControladoraDominio ctrlDominio, ControladoraPresentacion ctrlPresentacion){
        super("Menú Principal");
        configuraFrame();
        this.ctrlDominio = ctrlDominio;
        this.ctrlPresentacion = ctrlPresentacion;
        setVisible(false);
    }
    //Muestra la ventana previamente configurada
    public void abrirVentana(String username) {
        user.setText(username);
        setVisible(true);
    }
    //Esconde la ventana
    public void cerrarVentana() {
        setVisible(false);
    }
    //Metodo privado que se encarga de gestionar el formato de la ventana de login
    private void configuraFrame() {
        
        // Ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Tamaño
        setLocationRelativeTo(null); // Centrar

        //Objetos
        JPanel mainPanel = new JPanel(new BorderLayout());

        //1) Panel para el LOGOUT (arriba a la derecha)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        user = new JLabel("");
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.WHITE);
        topPanel.add(user);
        topPanel.add(logoutButton);

        //2) Panel para los botones (izquierda)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.setBorder(new EmptyBorder(0, 80, 0, 0));

        // Caso de uso ordenar estanteria
        JButton ordenarEstanteriaButton = new JButton("1) Ordenar Estanteria");
        ordenarEstanteriaButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(ordenarEstanteriaButton);

        // Panel adicional para agrupar los radio buttons
        JPanel radioButtonsPanel = new JPanel();
        radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel, BoxLayout.Y_AXIS));
        
        JRadioButton opcion1Button = new JRadioButton("Backtracking");
        JRadioButton opcion2Button = new JRadioButton("Kruskal");
        JRadioButton opcion3Button = new JRadioButton("Hillclimbing");
        opcion1Button.setBorder(new EmptyBorder(0, 20, 0, 0));
        opcion2Button.setBorder(new EmptyBorder(0, 20, 0, 0));
        opcion3Button.setBorder(new EmptyBorder(0, 20, 0, 0)); 

        //para evitar que se puedan seleccionar 2 botones a la vez
        ButtonGroup group = new ButtonGroup();
        group.add(opcion1Button);
        group.add(opcion2Button);
        group.add(opcion3Button);
        
        radioButtonsPanel.add(opcion1Button);
        radioButtonsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        radioButtonsPanel.add(opcion2Button);
        radioButtonsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        radioButtonsPanel.add(opcion3Button);

        centerPanel.add(radioButtonsPanel);

        // Otros casos de uso (botones en columna)
        JButton altaProductoButton = new JButton("2) Alta Producto");
        JButton modificarProductoButton = new JButton("3) Modificar Producto ");
        JButton eliminarProductoButton = new JButton("4) Eliminar Producto");
        JButton modificarUnGradoDeSimilitudButton = new JButton("5) Modificar un grado de similitud");
        JButton modificarGradosDeSimilitudProductoButton = new JButton("6) Modificar grados de similitud de un producto");
        JButton intercambiaPosicionProductosButton = new JButton("7) Intercambia posicion productos");
        JButton consultaGradoDeSimilitudProductosButton = new JButton("8) Consulta grado de similitud productos");
        JButton consultaGradosDeSimilitudDeUnProductoButton = new JButton("9) Consulta grados de similitud de un producto");
        JButton consultaProductosButton = new JButton("10) Consulta Productos");
        JButton existeProductoButton = new JButton("11) Existe Producto");
        JButton vaciarEstanteriaButton = new JButton("12) Vaciar Estanteria");


        centerPanel.add(altaProductoButton);
        centerPanel.add(modificarProductoButton);
        centerPanel.add(eliminarProductoButton);
        centerPanel.add(modificarUnGradoDeSimilitudButton);
        centerPanel.add(modificarGradosDeSimilitudProductoButton);
        centerPanel.add(intercambiaPosicionProductosButton);
        centerPanel.add(consultaGradoDeSimilitudProductosButton);
        centerPanel.add(consultaGradosDeSimilitudDeUnProductoButton);
        centerPanel.add(consultaProductosButton);
        centerPanel.add(existeProductoButton);
        centerPanel.add(vaciarEstanteriaButton);
        
        //3) Panel Para Salir (abajo a la derecha)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton salirButton = new JButton("EXIT");
        salirButton.setBackground(new Color(255, 99, 71));
        bottomPanel.add(salirButton);

        //Añadimos los 3 pannels 
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.WEST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        this.add(mainPanel);

        //CASOS DE USO
        ordenarEstanteriaButton.addActionListener(e -> {//1
            if (! (opcion1Button.isSelected() || opcion2Button.isSelected() || opcion3Button.isSelected())) {
                JOptionPane.showMessageDialog(this, "Seleccione un algoritmo", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    if (opcion1Button.isSelected()) {
                        ctrlDominio.ordenarEstanteria(1); //Backtracking
                    } else if (opcion2Button.isSelected()) {
                        ctrlDominio.ordenarEstanteria(2); //Kruskal
                    } else if (opcion3Button.isSelected()) {
                        ctrlDominio.ordenarEstanteria(3); //HillClimbing
                    }
                    //mostramos el resultado
                    this.ctrlPresentacion.consultaProductos();
                } catch (EstanteriaVaciaException ex){
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR:", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        
        altaProductoButton.addActionListener(e -> { //2
            this.ctrlPresentacion.AltaProducto();
        });
        modificarProductoButton.addActionListener(e -> {//3
            this.ctrlPresentacion.ModificarProducto();
        });
        eliminarProductoButton.addActionListener(e -> {//4
            this.ctrlPresentacion.EliminarProducto();
        });
        modificarUnGradoDeSimilitudButton.addActionListener(e -> {//5
            this.ctrlPresentacion.ModificarSimilitud();
        });
        modificarGradosDeSimilitudProductoButton.addActionListener(e -> {//6
            this.ctrlPresentacion.ModificarSimilitudes();
        });
        intercambiaPosicionProductosButton.addActionListener(e -> {//7
            this.ctrlPresentacion.intercambiaPosicionProductos();
        });
        consultaGradoDeSimilitudProductosButton.addActionListener(e -> {//8
            this.ctrlPresentacion.consultaGradoDeSimilitudProductos();
        });
        consultaGradosDeSimilitudDeUnProductoButton.addActionListener(e -> {//9
            this.ctrlPresentacion.consultaGradosDeSimilitudDeUnProducto();
        });
        consultaProductosButton.addActionListener(e -> { //10
            this.ctrlPresentacion.consultaProductos();
        });

        existeProductoButton.addActionListener(e -> { //11
            this.ctrlPresentacion.mostrarExistenciaProducto();
        });

        vaciarEstanteriaButton.addActionListener(e -> { //12
            this.ctrlDominio.vaciarEstanteria();
            this.ctrlPresentacion.consultaProductos();
        });
      
        //BOTON LOGOUT
        logoutButton.addActionListener(e -> {
            try {
                this.cerrarVentana(); // Esconder la ventana
                ctrlDominio.logout();
                this.ctrlPresentacion.iniciaAplicacion();
            } catch (NoSesionIniciadaException ex){
                System.out.println("No hay sesion iniciada\n");
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
        //BOTON SALIR
        salirButton.addActionListener(e -> {
            System.exit(0);
        });

        this.cerrarVentana();//al instanciarse la ocultamos
    }

}
