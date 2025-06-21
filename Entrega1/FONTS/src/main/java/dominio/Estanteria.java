package dominio;
import dominio.Producto;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Esta clase se encarga de almacenar los productos en un orden determinado.

public class Estanteria {

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
    public void print() {
        int size = ordenProductos.size();

        System.out.print("[ ");
        for (int i = 0; i < size; ++i) {
            Integer IdProd = ordenProductos.get(i);
            productos.get(IdProd).printProducte();
            if (i != size - 1) {
                System.out.print(" | ");
            }
        }
        System.out.println(" ]");
    }
}


