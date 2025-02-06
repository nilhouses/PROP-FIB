package dominio;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import dominio.ControladoraUsuario;
import dominio.Usuario;
import persistencia.ControladoraPersistencia;
import dominio.Pair;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

import dominio.excepciones.*;

public class ControladoraDominio implements Serializable { //Antiguamente CjtUsuarios

    private HashMap<String, ControladoraUsuario> userController;
    private Usuario usuarioActivo;
    private ControladoraUsuario controladoraActiva;
    private ControladoraPersistencia persistencia;

    public ControladoraDominio() {
        this.userController = new HashMap<>();
        this.usuarioActivo = null;
        this.controladoraActiva = null;
        this.persistencia = new ControladoraPersistencia();
    }
    //Capa de persistencia
    
        //Funciones para tratar todos los datos cargarInfo(), guardarInfo(), borrarInfo() y guardarInfoUsuario()
        public void cargarInfo() {
            try {
                // Cargar los datos de los usuarios
                this.userController = persistencia.cargarUsuarios(); // Carregar usuaris amb controladora i credencials
            } catch (IOException e) {
                System.out.println("Error al carregar dades: " + e.getMessage());
                this.userController = new HashMap<>();
            }
        }
        public void guardarInfo() {
            try {
                // Guardar les dades de tots els usuaris
                persistencia.guardarUsuarios(this.userController);
            } catch (IOException e) {
                System.out.println("Error al guardar dades: " + e.getMessage());
            }
        }
        public void borrarInfo() {
            persistencia.borrarInfo();
            this.userController = new HashMap<>();
            this.usuarioActivo = null;
            this.controladoraActiva = null;
        }
        
        public void guardarInfoUsuario(String Usuario) {
            try {
                persistencia.guardarControladoraUsuario(this.usuarioActivo.getUsername(), this.controladoraActiva);
                //actualizamos tambien el otro atributo 
                this.userController.put(this.usuarioActivo.getUsername(), this.controladoraActiva);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    //FUNCIONES PARA CARGAR FICHEROS JSON
    
    //Funcion que se encarga de escribir un usuario en el diorectorio gestionado por la capa de persistencia
    public void cargarUsuarioDesdeArchivo(String filePath) throws Exception,  SobreescribirUsuarioException {
        try {
            if (!filePath.endsWith(".json")) {
                throw new Exception("El fichero tiene que tener formato .json");
            }
            
            String UsuarioACargar = Paths.get(filePath).getFileName().toString();
        
            if (UsuarioACargar.length() > 5 && UsuarioACargar.endsWith(".json")) {
                String usernameFromFile = UsuarioACargar.substring(0, UsuarioACargar.length() - 5); // Quitamos ".json"
                if (userController.containsKey(usernameFromFile)) {
                    throw new SobreescribirUsuarioException("El usuario " + usernameFromFile + " ya existe");
                }
                else {
                    ControladoraUsuario cu = persistencia.cargarUsuarioDesdeArchivo(filePath);
                    userController.put(cu.getUsername(), cu);
                }
            } else {
                throw new Exception("El fichero no tiene el formato correcto.");
            }
            
        } catch (SobreescribirUsuarioException e) {
            throw e;
        }catch (Exception e) {//s'executa
            throw new Exception(e.getMessage());
        }
    }
    // Funcion que se encarga de sobreescribir un usuario que ya existe en el directorio de usuarios que gestiona 
    // la capa de persistencia con el contenido del fichero pasado como parámetro 
    public void SobreescribirUsuario(String filePath) throws Exception {
        try {
            if (!filePath.endsWith(".json")) {
                throw new Exception("El fichero tiene que tener formato .json");
            }
            ControladoraUsuario cu = persistencia.cargarUsuarioDesdeArchivo(filePath);
            userController.put(cu.getUsername(), cu);
            
        } catch (Exception e) {
            throw new Exception("Error loading user: " + e.getMessage());
        }
    }

    ////////////////////////////////// FIN FUNCIONES PERSISTENCIA

    public boolean signUp(Usuario u) throws UsuarioActivoException, UsuarioExistenteException {// Caso de uso 12

        if (!(this.usuarioActivo == null)) {
            // Dominio (terminal)
            System.out.println("Sesion iniciada como " + this.usuarioActivo.getUsername() + ", cierrela y vuelva a intentarlo.");
            // Presentacion
            throw new UsuarioActivoException("Sesion iniciada como " + this.usuarioActivo.getUsername() + ", cierrela y vuelva a intentarlo.");
        } else {
            String username = u.getUsername();
            if (!userController.containsKey(username)) {
                ControladoraUsuario ctrl = new ControladoraUsuario(u.getUsername(),u.getPassword());
                this.userController.put(username, ctrl);
                this.usuarioActivo = u;
                this.controladoraActiva = ctrl;
                // Dominio (terminal)
                System.out.println("Se ha creado una cuenta para el usuario " + username + " y se ha iniciado sesion");
                // Presentacion
                // No muestra nada por pantalla, simplemente avanza a la siguiente vista
                guardarInfo();
                return true;
            }
            // Dominio (terminal)
            System.out.println("El usuario " + username + " ya existe");
            // Presentacion
            throw new UsuarioExistenteException("El usuario " + username + " ya existe");
        }
        // return false; --> Unreachable statement
    }

    public boolean login(String username, String psswd)
            throws UsuarioActivoException, ContrasenaIncorrectaException, UsuarioNoExistenteException {// Caso de uso 13
        if (!(this.usuarioActivo == null)) {
            if (username.equals(this.usuarioActivo)) {
                // Dominio (terminal)
                System.out.println("La sesión ya esta iniciada");
                // Presentacion
                throw new UsuarioActivoException("Sesion iniciada");
            } else
                System.out.println("Sesion iniciada como " + this.usuarioActivo.getUsername() + ", cierrela y vuelvalo a intentar");
        } else {
            if (userController.containsKey(username)) {
                ControladoraUsuario ctrlUsuario = userController.get(username);
                Usuario u = new Usuario(ctrlUsuario.getUsername(), ctrlUsuario.getPassword());
                if ((u.entraContrasena(psswd))) {
                    this.usuarioActivo = u;
                    this.controladoraActiva = ctrlUsuario;
                    // Dominio (terminal)
                    System.out.println("Sesion iniciada con usuario " + username);
                    // Presentacion
                    // No muestra nada por pantalla, simplemente avanza a la siguiente vista
                    return true;
                } else {
                    // Dominio (terminal)
                    System.out.println("Incorrect Password");
                    // Presentacion
                    throw new ContrasenaIncorrectaException("Incorrect password");
                }
            } else {
                // Dominio (terminal)
                System.out.println("El usuario " + username + " no existe");
                // Presentacion
                throw new UsuarioNoExistenteException("El usuario " + username + " no existe");
            }
        }
        return false;
    }

    public boolean logout() throws NoSesionIniciadaException { // Caso de uso 14

        if (!(this.usuarioActivo == null)) {
            System.out.println("Cerrando sesion del usuario " + this.usuarioActivo.getUsername());
            usuarioActivo = null;
            controladoraActiva = null;
            return true;
        } else {
            // Dominio (terminal)
            System.out.println("No hay ninguna sesión iniciada");
            // Presentacion
            throw new NoSesionIniciadaException("No hay ninguna sesión iniciada");
        }
    }

    // Funcion para que el usuario sepa en que usuario tiene la sesion iniciada
    public void printUsuarioActual() {
        if (this.usuarioActivo == null) {
            System.out.println(" _____________________________________________________");
            System.out.println("|                                                     |Logged out");
        } else {
            System.out.println(" _____________________________________________________");
            System.out.println("|                                                     |Usuario: "
                    + this.usuarioActivo.getUsername());
        }
    }

    // Funcion consultora
    public boolean loggedIn() {
        return (!(this.usuarioActivo == null));
    }

    // CASOS DE USO
    // 1
    public boolean ordenarEstanteria(Integer num) throws EstanteriaVaciaException {
        if (this.controladoraActiva != null) {
            if (this.controladoraActiva.ordenarEstanteria(num)) {
                guardarInfo();
                return true;
            } else
                throw new EstanteriaVaciaException("La Estantería está vacía.");
        }
        System.out.println("Se tiene que iniciar sesion");
        return false;
    }

    // 2
    public Integer anadirProductoEstanteria(Producto p) {
        if (this.controladoraActiva != null) {
            Integer res = this.controladoraActiva.anadirProductoEstanteria(p);
            if (res != -1) {
                //Persistencia
                guardarInfoUsuario(this.usuarioActivo.getUsername());
                //Dominio
                return res;
            }
        }
        System.out.println("Se tiene que iniciar sesion");
        return -1;// como si no se hubiese añadido producto
    }

    @JsonIgnore
    public HashMap<Integer, Producto> getProds() {
        if (this.controladoraActiva != null) {
            return this.controladoraActiva.getProds();
        }
        return null;
    }

    public boolean anadirSimilitudProducto(Integer p1, Integer p2, Float sim) {
        if (this.controladoraActiva != null) {
            if (this.controladoraActiva.anadirSimilitudProducto(p1, p2, sim)) {
                //Persistencia
                guardarInfoUsuario(this.usuarioActivo.getUsername());
                //Dominio
                return true;
            }
        }
        return false;
    }

    public boolean modificarProductoEstanteria(Producto p, String nombre, String marca) {
        if (this.controladoraActiva != null) {
            if (this.controladoraActiva.modificarProductoEstanteria(p, nombre, marca)) {
                //Persistencia
                guardarInfoUsuario(this.usuarioActivo.getUsername());
                //Dominio
                return true;
            }
            return false;
        }
        System.out.println("Se tiene que iniciar sesion");
        return false;
    }

    // 4
    public boolean eliminarProductoEstanteria(Producto p) {
        if (this.controladoraActiva != null) {
            if (this.controladoraActiva.eliminarProductoEstanteria(p)) {
                //Persistencia
                guardarInfoUsuario(this.usuarioActivo.getUsername());
                //Dominio
                return true;
            }
        }
        System.out.println("Se tiene que iniciar sesion");
        return false;
    }

    // 5 y 6
    public boolean modificarSimilitudProductos(Producto p1, Producto p2, Float sim) {
        if (this.controladoraActiva != null) {
            if (this.controladoraActiva.modificarSimilitudProductos(p1, p2, sim)) {
                //Persistencia
                guardarInfoUsuario(this.usuarioActivo.getUsername());
                //Dominio
                return true;
            }
        }
        System.out.println("Se tiene que iniciar sesion");
        return false;
    }

    public void validarProductos(Producto p1, Producto p2) throws NoSesionIniciadaException {
        try {
            if (this.controladoraActiva != null) {
                this.controladoraActiva.validarProductos(p1, p2);
            } else {
                throw new NoSesionIniciadaException(null);
            }
        } catch (NoExistenProductosException | ProductoNoExisteException ex) {
            // Throw exception from other validar producto.
        }
    }

    // 7
    public boolean intercambiarProductos(Producto p1, Producto p2) {
        if (this.controladoraActiva != null) {
            if (this.controladoraActiva.intercambiarProductos(p1, p2)) {
                //Persistencia
                guardarInfoUsuario(this.usuarioActivo.getUsername());
                //Dominio
                return true;
            }
        }
        System.out.println("Se tiene que iniciar sesion");
        return false;
    }

    // 8
    public Float getSimilitudProductos(Producto p1, Producto p2) {
        return this.controladoraActiva.getSimilitudProductos(p1, p2);
    }

    // 9
    public void getSimilitudesProducto(Producto p) throws NoSesionIniciadaException, PrintConsultaSimilitudesProductoException, ProductoNoExisteException {

        if (this.controladoraActiva != null) {
            if(this.controladoraActiva.existeProducto(p) != -1)
            {
                String similitudes = this.controladoraActiva.getSimilitudesProducto(p);
                throw new PrintConsultaSimilitudesProductoException(similitudes);
            }
            else
            {
                throw new ProductoNoExisteException("El producto " + p.getNom() + " " + p.getMarca() + " no existe.\n");
            }
        } else {
            throw new NoSesionIniciadaException(null);
        }
    }

    // 10
    public void print() throws PrintEstanteriaException {
        if (this.controladoraActiva != null) {
            String contenido = this.controladoraActiva.print();
            if (contenido.contains("[  ]")) {
                contenido = "Estanteria vacia\n";
            }
            System.out.print(contenido);
            throw new PrintEstanteriaException(contenido);
        } else
            System.out.println("Se tiene que iniciar sesion");
    }

    // 11
    public Integer existeProducto(Producto p) {
        if (this.controladoraActiva != null) {
            Integer id = this.controladoraActiva.existeProducto(p);
            if (id == -1)System.out.println("El producto " + p.getNom() + " " + p.getMarca() + " no esta en el sistema");
            else System.out.println("El producto " + p.getNom() + " " + p.getMarca() + " esta en el sistema");
            return id;
        }
        System.out.println("Se tiene que iniciar sesion");
        return -1;
    }

    // 15
    public boolean vaciarEstanteria() {
        if (this.controladoraActiva != null) {
            if (this.controladoraActiva.vaciarEstanteria()) {
                //Persistencia
                guardarInfoUsuario(this.usuarioActivo.getUsername());
                //Dominio
                return true;
            }
        }
        System.out.println("Se tiene que iniciar sesion");
        return false;
    }

}