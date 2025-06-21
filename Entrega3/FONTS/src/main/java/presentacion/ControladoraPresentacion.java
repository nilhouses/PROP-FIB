package presentacion;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import dominio.ControladoraDominio;
import dominio.Pair;
import dominio.Producto;
import presentacion.vistas.*;
import dominio.excepciones.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ControladoraPresentacion { 
    //Clase que se encarga de mostrar mediante una intefaz programada con Swing (y a veces awt) 
    //los cambios hechos por el usuario sobre la capa del domino

    private ControladoraDominio ctrlDominio;            //Instancia de la controladora del dominio que trata esta controladora de presentacion
    private VistaLogin vistaLogin;                      //Vista del menu de inicio de sesion
    private VistaMenuPrincipal vistaMenuPrincipal;      //Vista del menu principal

    // Constructor
    public ControladoraPresentacion() {
        this.ctrlDominio = new ControladoraDominio();
        new DialogoCargarDatos(ctrlDominio);
        this.vistaLogin = new VistaLogin(ctrlDominio, this);
        this.vistaMenuPrincipal = new VistaMenuPrincipal(ctrlDominio, this);
    }

    //Funcion que se encarga de iniciar la aplicacion una vez el usuario ha cargado (o no) la informacion del sistema
    public void iniciaAplicacion() {
        this.vistaLogin.abrirVentana();
    }

    //Funcion que se encarga de mostrar el menu principal del sistema
    public void abreMenuPrincipal(String usu) { 
        this.vistaMenuPrincipal.abrirVentana(usu);
    }

    //Funcion que gestiona el caso de uso de Alta Producto
    public void AltaProducto() {
        DialegAltaProducte dap = new DialegAltaProducte();
        Pair<String, String> nouprod = dap.mostrardialeg();
        if (nouprod != null) {
            Producto p = new Producto(nouprod.getFirst(), nouprod.getSecond());
            if (ctrlDominio.existeProducto(p) == -1) {
                HashMap<Integer, Producto> hmap = ctrlDominio.getProds();
                ArrayList<Pair<Integer, Float>> simlist = new ArrayList<>();

                // Obtain products to add similitudes one by one.
                Float[] f = new Float[] { -1.0f };
                hmap.forEach((i, prodsim) -> {
                    while (f[0] != null && f[0] == -1.0f) {
                        f[0] = dap.modsimilitud(p, prodsim);
                        if (f[0] != null && f[0] != -1.0f) {
                            simlist.add(new Pair<>(i, f[0]));
                        }
                    }
                    if (f[0] != null) {
                        f[0] = -1.0f;
                    }
                });

                if (simlist.size() == hmap.size()) {
                    this.ctrlDominio.anadirProductoEstanteria(p);

                    simlist.forEach((pr) -> {
                        this.ctrlDominio.anadirSimilitudProducto(p.getid(), pr.getFirst(), pr.getSecond());
                    });
                    ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono4.png");
                    ImageIcon scaledIcon = new ImageIcon(
                            icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                    JOptionPane.showMessageDialog(
                            null,
                            "El producto se ha añadido correctamente.",
                            "Alta Producto",
                            JOptionPane.INFORMATION_MESSAGE,
                            scaledIcon);
                }

            } else {
                ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                ImageIcon scaledIcon = new ImageIcon(
                        icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(
                        null,
                        "El producto ya está en el sistema.",
                        "Producto Existe",
                        JOptionPane.INFORMATION_MESSAGE,
                        scaledIcon);
                AltaProducto();
            }
        }
    }

    //Funcion que gestiona el caso de uso de Modificar Producto
    public void ModificarProducto() {
        DialegModificarProducto dmp = new DialegModificarProducto();
        Pair<Pair<String, String>, Pair<String, String>> modprod = dmp.mostrardialeg();
        if (modprod != null) {
            Producto pa = new Producto(modprod.getFirst().getFirst(), modprod.getFirst().getSecond());
            Producto pn = new Producto(modprod.getSecond().getFirst(), modprod.getSecond().getSecond());
            if (ctrlDominio.existeProducto(pa) != -1) {
                if (ctrlDominio.existeProducto(pn) == -1) {
                    ctrlDominio.modificarProductoEstanteria(pa, pn.getNom(), pn.getMarca());
                    ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono4.png");
                    ImageIcon scaledIcon = new ImageIcon(
                            icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                    JOptionPane.showMessageDialog(
                            null,
                            "El producto se ha modificado correctamente.",
                            "Producto Modificado",
                            JOptionPane.INFORMATION_MESSAGE,
                            scaledIcon);
                } else {
                    ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                    ImageIcon scaledIcon = new ImageIcon(
                            icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                    JOptionPane.showMessageDialog(
                            null,
                            "El producto ya está en el sistema.",
                            "Producto Existe",
                            JOptionPane.INFORMATION_MESSAGE,
                            scaledIcon);
                    ModificarProducto();
                }
            } else {
                ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                ImageIcon scaledIcon = new ImageIcon(
                        icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(
                        null,
                        "El producto no está en el sistema.",
                        "Producto Inexistente",
                        JOptionPane.INFORMATION_MESSAGE,
                        scaledIcon);
                ModificarProducto();
            }
        }
    }

    //Funcion que gestiona el caso de uso de Eliminar Producto
    public void EliminarProducto() {
        DialegEliminarProducte dep = new DialegEliminarProducte();
        Pair<String, String> eliminarprod = dep.mostrardialeg();
        if (eliminarprod != null) {
            Producto p = new Producto(eliminarprod.getFirst(), eliminarprod.getSecond());
            if (!this.ctrlDominio.eliminarProductoEstanteria(p)) {
                ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                ImageIcon scaledIcon = new ImageIcon(
                        icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(
                        null,
                        "El producto no está en el sistema.",
                        "Producto Inexistente",
                        JOptionPane.INFORMATION_MESSAGE,
                        scaledIcon);
                EliminarProducto();
            } else {
                ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono4.png");
                ImageIcon scaledIcon = new ImageIcon(
                        icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(
                        null,
                        "El producto se ha eliminado correctamente.",
                        "Producto Eliminado",
                        JOptionPane.INFORMATION_MESSAGE,
                        scaledIcon);
            }
        }
    }

    //Funcion que gestiona el caso de uso de Modifcar un grado de similitud
    public void ModificarSimilitud() {
        DialegModificarSimilitudProductos dmsp = new DialegModificarSimilitudProductos();
        Pair<Pair<String, String>, Pair<String, String>> cp = dmsp.mostrardialeg();
        if (cp != null) {
            Producto p1 = new Producto(cp.getFirst().getFirst(), cp.getFirst().getSecond());
            Producto p2 = new Producto(cp.getSecond().getFirst(), cp.getSecond().getSecond());
            if (this.ctrlDominio.existeProducto(p1) != -1) {
                if (this.ctrlDominio.existeProducto(p2) != -1) {
                    Float[] f = new Float[] { -1.0f };
                    while (f[0] != null && f[0] == -1.0f) {
                        f[0] = dmsp.modsimilitud(p1, p2);
                    }
                    if (f[0] != null) {
                        this.ctrlDominio.modificarSimilitudProductos(p1, p2, f[0]);
                        ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono4.png");
                        ImageIcon scaledIcon = new ImageIcon(
                                icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                        JOptionPane.showMessageDialog(
                                null,
                                "El grado de la similitud de los productos ha sido cambiada correctamente.",
                                "Grado Similitud Cambiada",
                                JOptionPane.INFORMATION_MESSAGE,
                                scaledIcon);
                    }
                } else {
                    ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                    ImageIcon scaledIcon = new ImageIcon(
                            icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                    JOptionPane.showMessageDialog(
                            null,
                            "El producto " + p2.getNom() + " " + p2.getMarca() + " no está en el sistema.",
                            "Producto Existe",
                            JOptionPane.INFORMATION_MESSAGE,
                            scaledIcon);
                    ModificarSimilitud();
                }
            } else {
                ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                ImageIcon scaledIcon = new ImageIcon(
                        icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(
                        null,
                        "El producto " + p1.getNom() + " " + p1.getMarca() + " no está en el sistema.",
                        "Producto Existe",
                        JOptionPane.INFORMATION_MESSAGE,
                        scaledIcon);
                ModificarSimilitud();
            }
        }
    }

    //Funcion que gestiona el caso de uso de Modificar grados de similitud de un producto
    public void ModificarSimilitudes() {
        DialegModificarSimilitudesProducto dmsp = new DialegModificarSimilitudesProducto();
        Pair<String, String> prod = dmsp.mostrardialeg();
        if (prod != null) {
            Producto p = new Producto(prod.getFirst(), prod.getSecond());
            if (this.ctrlDominio.existeProducto(p) != -1) {
                HashMap<Integer, Producto> hmap = ctrlDominio.getProds();
                ArrayList<Pair<Producto, Float>> simlist = new ArrayList<>();

                // Obtain products to add similitudes one by one.
                Float[] f = new Float[] { -1.0f };
                hmap.forEach((i, prodsim) -> {
                    while (f[0] != null && f[0] == -1.0f) {
                        if ((p.getNom()).equals(prodsim.getNom()) && (p.getMarca()).equals(prodsim.getMarca())) {
                            f[0] = 1.0f;
                        } else {
                            f[0] = dmsp.modsimilitud(p, prodsim);
                            if (f[0] != null && f[0] != -1.0f) {
                                simlist.add(new Pair<>(prodsim, f[0]));
                            }
                        }
                    }
                    if (f[0] != null) {
                        f[0] = -1.0f;
                    }
                });
                if (simlist.size() == (hmap.size() - 1)) {

                    simlist.forEach((pr) -> {
                        this.ctrlDominio.modificarSimilitudProductos(p, pr.getFirst(), pr.getSecond());
                    });
                    ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono4.png");
                    ImageIcon scaledIcon = new ImageIcon(
                            icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                    JOptionPane.showMessageDialog(
                            null,
                            "Los grados de similitud se han cambiado correctamente.",
                            "Cambiar Grado Similitud",
                            JOptionPane.INFORMATION_MESSAGE,
                            scaledIcon);
                }
            } else {
                ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                ImageIcon scaledIcon = new ImageIcon(
                        icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(
                        null,
                        "El producto " + p.getNom() + " " + p.getMarca() + " no está en el sistema.",
                        "Producto Inexistente",
                        JOptionPane.INFORMATION_MESSAGE,
                        scaledIcon);
                ModificarSimilitudes();
            }
        }
    }

    //Funcion que gestiona el caso de uso de Intercambiar la posicion de dos productos
    public void intercambiaPosicionProductos() {
        DialegIntercambiaPosicionProductos dipp = new DialegIntercambiaPosicionProductos();
        Pair<Pair<String, String>, Pair<String, String>> cp = dipp.mostrardialeg();
        if (cp != null) {
            Producto p1 = new Producto(cp.getFirst().getFirst(), cp.getFirst().getSecond());
            Producto p2 = new Producto(cp.getSecond().getFirst(), cp.getSecond().getSecond());
            if (this.ctrlDominio.existeProducto(p1) != -1) {
                if (this.ctrlDominio.existeProducto(p2) != -1) {
                    this.ctrlDominio.intercambiarProductos(p1, p2);
                    ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono4.png");
                    ImageIcon scaledIcon = new ImageIcon(
                            icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                    JOptionPane.showMessageDialog(
                            null,
                            "Los productos se han intercambiado correctamente.",
                            "Productos Intercambiados",
                            JOptionPane.INFORMATION_MESSAGE,
                            scaledIcon);
                } else {
                    ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                    ImageIcon scaledIcon = new ImageIcon(
                            icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                    JOptionPane.showMessageDialog(
                            null,
                            "El producto " + p2.getNom() + " " + p2.getMarca() + " no está en el sistema.",
                            "Producto Inexistente",
                            JOptionPane.INFORMATION_MESSAGE,
                            scaledIcon);
                    intercambiaPosicionProductos();
                }
            } else {
                ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                ImageIcon scaledIcon = new ImageIcon(
                        icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(
                        null,
                        "El producto " + p1.getNom() + " " + p1.getMarca() + " no está en el sistema.",
                        "Producto Inexistente",
                        JOptionPane.INFORMATION_MESSAGE,
                        scaledIcon);
                intercambiaPosicionProductos();
            }
        }
    }

    //Funcion que gestiona el caso de uso Consultar el grado de similitud entre dos productos
    public void consultaGradoDeSimilitudProductos() {
        DialegConsultaSimilitudProductos dcsp = new DialegConsultaSimilitudProductos();
        Pair<Pair<String, String>, Pair<String, String>> cp = dcsp.mostrardialeg();
        if (cp != null) {
            Producto p1 = new Producto(cp.getFirst().getFirst(), cp.getFirst().getSecond());
            Producto p2 = new Producto(cp.getSecond().getFirst(), cp.getSecond().getSecond());
            if (this.ctrlDominio.existeProducto(p1) != -1) {
                if (this.ctrlDominio.existeProducto(p2) != -1) {
                    Float f = this.ctrlDominio.getSimilitudProductos(p1, p2);
                    String s = ("La similitud entre " + p1.getNom() + " " + p1.getMarca() + " y " + p2.getNom() + " "
                            + p2.getMarca() + " es: " + f);
                    dcsp.mostrarconsulta(s);
                } else {
                    ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                    ImageIcon scaledIcon = new ImageIcon(
                            icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                    JOptionPane.showMessageDialog(
                            null,
                            "El producto " + p2.getNom() + " " + p2.getMarca() + " no está en el sistema.",
                            "Producto Inexistente",
                            JOptionPane.INFORMATION_MESSAGE,
                            scaledIcon);
                    consultaGradoDeSimilitudProductos();
                }
            } else {
                ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                ImageIcon scaledIcon = new ImageIcon(
                        icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(
                        null,
                        "El producto " + p1.getNom() + " " + p1.getMarca() + " no está en el sistema.",
                        "Producto Inexistente",
                        JOptionPane.INFORMATION_MESSAGE,
                        scaledIcon);
                consultaGradoDeSimilitudProductos();
            }
        }
    }

    //Funcion que gestiona el caso de uso de Consultar los grados de similitud de un producto
    public void consultaGradosDeSimilitudDeUnProducto() {
        DialegConsultaSimilitudesProducto dcsp = new DialegConsultaSimilitudesProducto();
        Pair<String, String> cp = dcsp.mostrardialeg();
        if (cp != null) {
            Producto p = new Producto(cp.getFirst(), cp.getSecond());
            try {
                this.ctrlDominio.getSimilitudesProducto(p);
            } catch (PrintConsultaSimilitudesProductoException ex) {
                dcsp.mostrarconsulta(ex.getMessage());
            } catch (ProductoNoExisteException ex) {
                ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                ImageIcon scaledIcon = new ImageIcon(
                        icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(
                        null,
                        "El producto " + p.getNom() + " " + p.getMarca() + " no está en el sistema.",
                        "Producto Inexistente",
                        JOptionPane.INFORMATION_MESSAGE,
                        scaledIcon);
                consultaGradosDeSimilitudDeUnProducto();
            } catch (NoSesionIniciadaException ex) {

            }
        }
    }

    //Funcion que gestiona el caso de uso de Consulta productos (muestra la estanteria)
    public void consultaProductos() {
        try {
            this.ctrlDominio.print();
        } catch (PrintEstanteriaException ex) {
            new DialogoConsultaProductos(ex.getMessage());
        }
    }

    //Funcion que gestiona el caso de uso de Existe Producto
    public void mostrarExistenciaProducto() {
        DialogoExisteProducto d = new DialogoExisteProducto();
        Pair<String, String> valores = d.mostrarDialogo();
        if (valores != null) {
            Producto p = new Producto(valores.getFirst(), valores.getSecond());

            if (ctrlDominio.existeProducto(p) != -1) {
                // Producto existe, lo mostramos
                ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono4.png");
                ImageIcon scaledIcon = new ImageIcon(
                        icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(
                        null,
                        "El producto " + p.getNom() + " " + p.getMarca() + " está en el sistema.",
                        "Producto Existente",
                        JOptionPane.INFORMATION_MESSAGE,
                        scaledIcon);
            } else {
                // Producto no existe, lo mostramos
                ImageIcon icon = new ImageIcon("src/main/java/presentacion/iconos/icono5.png");
                ImageIcon scaledIcon = new ImageIcon(
                        icon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
                JOptionPane.showMessageDialog(
                        null,
                        "El producto " + p.getNom() + " " + p.getMarca() + " no está en el sistema.",
                        "Producto No Existente",
                        JOptionPane.ERROR_MESSAGE,
                        scaledIcon);
            }
        }
    }

    //Funcion que gestiona el caso de uso de Cargar un usuario desde un archivo
    public void cargarUsuarioDesdeArchivo(String filePath) throws DialogoSobreescrituraException, Exception {
        try {
            ctrlDominio.cargarUsuarioDesdeArchivo(filePath);
        } catch (SobreescribirUsuarioException e) {
            throw new DialogoSobreescrituraException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
