package dominio;

import java.util.HashMap;

import dominio.ControladoraDominio;
import dominio.Usuario;
import dominio.Pair;

public class CjtUsuarios {
    
    private HashMap <String,ControladoraDominio> userController;
    private HashMap <String, Usuario> credenciales;
    private Usuario usuarioActivo;
    private ControladoraDominio controladoraActiva;

    public CjtUsuarios() {
        this.userController = new HashMap<>();
        this.credenciales = new HashMap<>();
        this.usuarioActivo = null;
        this.controladoraActiva = null;
    }

    public boolean signUp(Usuario u) {//Caso de uso 11
        
        if (!(this.usuarioActivo == null)) {
            System.out.println("Sesion iniciada como " + this.usuarioActivo.getUsername() +", cierrela y vuelvalo a intentar ");
        }
        else {
            String username = u.getUsername();
            if (getUsr(u) == null) {
                ControladoraDominio ctrl = new ControladoraDominio();
                this.userController.put(username, ctrl);
                this.credenciales.put(username,u);
                this.usuarioActivo = u; 
                this.controladoraActiva = ctrl;
                System.out.println("Se ha creado una cuenta para el usuario " + username + " y se ha iniciado sesion");
                return true;
            }
            System.out.println("El usuario "+ username + " ya existe");
        }
        return false;
    }

    public boolean login(String username, String psswd) {//Caso de uso 12
        if (!(this.usuarioActivo == null)) {
            if (username.equals(this.usuarioActivo)){
                System.out.println("La sesión ya esta iniciada");
            }
            else System.out.println("Sesion iniciada como " + this.usuarioActivo.getUsername() +", cierrela y vuelvalo a intentar");
        }
        else{
            if (getUsr(new Usuario(username, psswd)) != null) {//usuario existe
                if (credenciales.get(username).entraContrasena(psswd)){
                    this.usuarioActivo = new Usuario(username, psswd); 
                    controladoraActiva = userController.get(username);
                    System.out.println("Sesion iniciada con usuario "+ username);
                    return true;
                } else {
                    System.out.println("Incorrect Password");
                }
            }
            else{
                System.out.println("El usuario "+ username +" no existe");
            }
        }
        return false;
    }

    public boolean logout() { //Caso de uso 13
        if (!(this.usuarioActivo == null)) {
            System.out.println("Cerrando sesion del usuario "+ this.usuarioActivo.getUsername());
            usuarioActivo = null;
            controladoraActiva = null;
            return true;
        }else {
            System.out.println("No hay ninguna sesion iniciada");
        }
        return false;
    }

    
    private Usuario getUsr(Usuario u) {
        return credenciales.get(u.getUsername());   
    }    

    //Funcion para que el usuario sepa en que usuario tiene la sesión iniciada
    public void printUsuarioActual(){
        if (this.usuarioActivo == null) {
            System.out.println(" _____________________________________________________");
            System.out.println("|                                                     |Logged out");
        }
        else{
            System.out.println(" _____________________________________________________");
            System.out.println("|                                                     |Usuario: " + this.usuarioActivo.getUsername());
        } 
    }
    //Funcion consultora
    public boolean loggedIn() {
        return (!(this.usuarioActivo == null));
    }
    //CASOS DE USO
    //1
    public boolean ordenarEstanteria(Integer num) {
        if (this.controladoraActiva != null){
            return this.controladoraActiva.ordenarEstanteria(num);
        }
        System.out.println("Se tiene que iniciar sesion");
        return false;
    }
    //2
    public Integer anadirProductoEstanteria(Producto p) {
        if (this.controladoraActiva != null){
            return this.controladoraActiva.anadirProductoEstanteria(p);
        }
        System.out.println("Se tiene que iniciar sesion");
        return -1;//como si no se hubiese añadido producto
    }
    public HashMap<Integer, Producto> getProds() {
        if (this.controladoraActiva != null) {
            return this.controladoraActiva.getProds();
        }
        return null;
    }
    public boolean anadirSimilitudProducto(Integer p1, Integer p2, Float sim) {
        if (this.controladoraActiva != null) {
            return this.controladoraActiva.anadirSimilitudProducto(p1,p2,sim);
        }
        return false;
    }
    //3
    public boolean eliminarProductoEstanteria(Producto p) {
        if (this.controladoraActiva != null){
            return this.controladoraActiva.eliminarProductoEstanteria(p);
        }
        System.out.println("Se tiene que iniciar sesion");
        return false;
    }
    //4 y 5
    public boolean modificarSimilitudProductos(Producto p1, Producto p2, Float sim){
        if (this.controladoraActiva != null){
            return this.controladoraActiva.modificarSimilitudProductos(p1, p2, sim);
        }
        System.out.println("Se tiene que iniciar sesion");  
        return false;
    }
    public Pair<Boolean, Boolean> validarProductos(Producto p1, Producto p2) {
        if (this.controladoraActiva != null){
            return this.controladoraActiva.validarProductos(p1, p2);
        }
        System.out.println("Se tiene que iniciar sesion");  
        return new Pair<>(false,false); 
        // desde el driver main nos encargaremos que solo se ejecuta validar productos si la sesion esta iniciada
        // es decir, users.loggedIn()
    }

    //6
    public boolean intercambiarProductos(Producto p1, Producto p2){
        if (this.controladoraActiva != null){
            return this.controladoraActiva.intercambiarProductos(p1,p2);
        }
        System.out.println("Se tiene que iniciar sesion"); 
        return false;
    }
    //7
    public Float getSimilitudProductos(Producto p1, Producto p2){
        return this.controladoraActiva.getSimilitudProductos(p1, p2);
    }
    //8
    public boolean getSimilitudesProducto(Producto p) {
        if (this.controladoraActiva != null){
            return this.controladoraActiva.getSimilitudesProducto(p);
        }
        System.out.println("Se tiene que iniciar sesion");
        return false;
    }
    //9
    public void print() {
        if (this.controladoraActiva != null){
            this.controladoraActiva.print();
        }
        else System.out.println("Se tiene que iniciar sesion");
    }
    //10
    public Integer existeProducto(Producto p) {
        if (this.controladoraActiva != null){
            return this.controladoraActiva.existeProducto(p);
        }
        System.out.println("Se tiene que iniciar sesion"); 
        return -1;
    }
    //14
    public boolean vaciarEstanteria(){
        if (this.controladoraActiva != null){
            return this.controladoraActiva.vaciarEstanteria();
        }
        System.out.println("Se tiene que iniciar sesion");
        return false;
    }

}