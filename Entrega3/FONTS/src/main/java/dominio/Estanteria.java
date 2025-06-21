package dominio;
import dominio.Producto;

import java.awt.color.ProfileDataException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonIgnore;


//Esta clase se encarga de almacenar los productos en un orden determinado.

public class Estanteria implements Serializable{

    private HashMap<Integer,Producto> productos;    // Almacena los productos con acceso eficiente por id
    private ArrayList<Integer> ordenProductos;      // Representa el orden que siguen los productos representados por su id
    private int contadorId;                         // Indica el siguiente id a usar al añadir un producto nuevo en la estanteria, se usa para garantizar que sea único

    /*
     * Función Creadora
    */
    public Estanteria() {
        this.productos = new HashMap<>();
        this.ordenProductos = new ArrayList<>();
        this.contadorId = 1;
    }
    //FUNCIONES NUEVAS PARA LA PERSISTENCIA
    @JsonCreator
    public Estanteria(
            @JsonProperty("productos") HashMap<Integer, Producto> productos,
            @JsonProperty("ordenProductos") ArrayList<Integer> ordenProductos,
            @JsonProperty("contadorId") int contadorId) {
        setProductos(productos);
        setOrdenProductos(ordenProductos);
        setContadorId(contadorId);
    }

    public ArrayList<Integer> getOrdenProductos() {//lo mismo que getIdProductos, la usa Json
        return this.ordenProductos;
    }
        public int getContadorId() {
        return contadorId;
    }

    @JsonSetter("productos")
    public void setProductos(HashMap<Integer, Producto> productos) {
        this.productos = productos;
    }

    @JsonSetter("ordenProductos")
    public void setOrdenProductos(ArrayList<Integer> ordenProductos) {
        this.ordenProductos = ordenProductos;
    }

    @JsonSetter("contadorId")
    public void setContadorId(int contadorId) {
        this.contadorId = contadorId;
    }
    //////////////////////////////////FIN FUNCIONES JSON
    
    // GETTERS //
    /*
     * Devuelve el producto con ese id, si existe o Null, en caso que no exista
    */
    public Producto getProducto(int Id) {
        return this.productos.get(Id);
    }

    /*
     * Devuelve el producto con ese nombre+marca o Null, en caso que no exista
    */
    public Producto getProducto(Producto p) {
        for(Producto prod : productos.values()) {
            if (prod.getNom().equals(p.getNom()) && prod.getMarca().equals(p.getMarca())) return prod;
        }
        return null;
    }

    /*
     * Devuelve el id del producto o -1 si el producto no esta en el sistema
    */
    public int getIdProducto(Producto prod) { 
        for(Map.Entry<Integer,Producto> producto : this.productos.entrySet()) {
            Producto p = producto.getValue();
            if (p.getNom().equals(prod.getNom()) && p.getMarca().equals(prod.getMarca())) return producto.getKey();
        }
        return -1;
    }

    /*
     * Devuelve los productos almacenados, (la usa la clase controladora del dominio para conocer su nombre y marca)
    */
    public HashMap<Integer,Producto> getProductos() {
        return this.productos;
    }

    /*
     * Devuelve el orden de los productos almacenados representado por sus ids
    */
    @JsonIgnore
    public ArrayList<Integer> getIdProductos() {
        return this.ordenProductos;
    }

    // SETTERS //
    /*
     * Añade un nuevo producto con ese nombre y marca a la estanteria
     *  Devuelve el id del producto añadido o -1 si el producto ya existia
    */
    public int altaProducto(Producto p) {
        if (getProducto(p) != null) return -1; 
        p.setId(this.contadorId);
        productos.put(this.contadorId, p);
        ordenProductos.add(this.contadorId);
        ++contadorId;
        return contadorId -1; 
    }

    /*
     * Modifica el producto pasado como parametro asignandole
     * el nuevo nombre y marca pasados como parametro
    */
    public boolean modificarProducto(Producto p, String nombre, String marca) {
        Producto prod = getProducto(p);
        if (prod != null) {
            if (prod.getNom().equals(nombre) && prod.getMarca().equals(marca)) {
                System.out.println("El nombre y marca nuevos son iguales a los anteriores");
                return false;
            }
            int id = prod.getid();
            System.out.println("El producto " + prod.getNom() + " " + prod.getMarca() + " ha cambiado a " + nombre + " " + marca + ".");
            prod.setNombre(nombre);
            prod.setMarca(marca);
            return true;
        }
        System.out.println("El producto " + prod.getNom() + " " + prod.getMarca() + "no esta en la Estanteria.");
        return false;
    }

    /*
     * Elimina el producto con nombre y marca de los parametros y 
     * devuelve el id si se ha eliminado o -1 si el producto no estaba
     * en la estanteria
    */
    public int eliminaProducto(Producto p) {
        Producto prod = getProducto(p);
        if (prod != null) {
            int id = prod.getid();
            productos.remove(id);
            ordenProductos.remove(Integer.valueOf(id));
            return id;
        }
        return -1;
    }

    /*
     * Asigna el orden recibido como parametro si este parametro es correcto, es decir,
     * esta compuesto por los mismos elementos del atributo ordenProductos de la clase.
    */
    public boolean aplicaOrden(ArrayList<Integer> orden) {
        // De esta forma nos aseguramos que la lista que se esta asignando tiene los mismos elementos que ordenProductos pero en otro orden
        if (orden.containsAll(this.ordenProductos) && this.ordenProductos.containsAll(orden)) {
            this.ordenProductos = new ArrayList<>(orden);
            return true;
        }
        return false;
    }

    public void vaciarEstanteria() {
        this.productos = new HashMap<>();
        this.ordenProductos = new ArrayList<>();
        this.contadorId = 1;
    }

    // Funciones de Entrada y Salida (I/O) //
    /*
     * Se encarga de mostrar por pantalla los productos de la estanteria
    */
    public String print() {
        
        int size = ordenProductos.size();
        
        String estanteria = "[ ";
        for (int i = 0; i < size; ++i) {
            Integer IdProd = ordenProductos.get(i);
            estanteria += productos.get(IdProd).printProducte();
            if (i != size - 1) {
                estanteria += " | ";
            }
        }
        estanteria += " ]\n"; 
        return estanteria;
    }
}

