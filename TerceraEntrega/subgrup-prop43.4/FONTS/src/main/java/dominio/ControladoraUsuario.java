package dominio;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import dominio.EjecutarAlgoritmo.Backtracking;
import dominio.EjecutarAlgoritmo.HillClimbing;
import dominio.EjecutarAlgoritmo.Kruskal;

import java.util.Scanner;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import dominio.excepciones.*;

public class ControladoraUsuario implements Serializable {

    private String username;
    private String password;
    private Estanteria e;
    private Similitudes s;

    public ControladoraUsuario(String username, String password) { //Antiguamente Controladora del dominio
        this.username = username;
        this.password = password;
        e = new Estanteria();
        s = new Similitudes();
    }

    // FUNCIONES NUEVAS PARA LA PERSISTENCIA
    @JsonCreator
    public ControladoraUsuario(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("estanteria") Estanteria e,
            @JsonProperty("similitudes") Similitudes s) {
        this. username = username;
        this.password = password;
        this.e = e != null ? e : new Estanteria();
        this.s = s != null ? s : new Similitudes();
    }

    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }

    public Estanteria getEstanteria() {
        return e;
    }

    public Similitudes getSimilitudes() {
        return s;
    }

    @JsonSetter("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonSetter("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonSetter("estanteria")
    public void setEstanteria(Estanteria e) {
        this.e = e;
    }

    @JsonSetter("similitudes")
    public void setSimilitudes(Similitudes s) {
        this.s = s;
    }
    ////////////////////////////////// FIN FUNCIONES JSON

    //Funcion que nos permite anadir un producto a la estanteria
    public Integer anadirProductoEstanteria(Producto p) {

        Integer prod1 = e.altaProducto(p);
        if (prod1 == -1) {
            System.out.println("Este producto ya existe");
            return -1;
        }
        System.out.println("El producto se ha insertado correctamente");
        return prod1;
    }

    //Funcion que nos permite modificar el nombre y/o marca de un producto de la estanteria
    public boolean modificarProductoEstanteria(Producto p, String nombre, String marca) { 
        if (e.modificarProducto(p, nombre, marca))
            return true;
        else
            return false;
    }

    //Funcion que nos permite anadir un grado de similitud entre dos productos
    public boolean anadirSimilitudProducto(Integer prod1, Integer prod2, Float sim) {
        try {
            s.anadirSimilitud(prod1, prod2, sim);
        } catch (SimilitudExisteException ex) {

        }
        return true;
    }

    @JsonIgnore
    public HashMap<Integer, Producto> getProds() {
        return e.getProductos();
    }
    
    //Funcion que nos permite modificar el nombre y/o marca de un producto de la estanteria
    public boolean eliminarProductoEstanteria(Producto p) {

        Integer prod1 = e.eliminaProducto(p);
        if (prod1 == -1) {
            System.out.println("El producto no existe");
            return false;
        }
        if (!s.eliminarSimilitud(e.getIdProductos(), prod1)) {
            System.out.println("Han habido errores");
            return false;
        }
        System.out.println("El producto se ha eliminado correctamente");
        return true;
    }

    //Funcion que nos permite ordenar la estanteria segun un algoritmo seleccionado (num)
    public boolean ordenarEstanteria(Integer num) {

        ArrayList<Integer> aux = e.getIdProductos();
        if (aux.size() == 0) {
            System.out.println("No hay productos en la estanteria a ordenar");
            return true;
        }
        if (aux.size() == 1) {
            System.out.println("Solo hay un producto en la estanteria");
        }

        HashMap<Pair<Integer, Integer>, Float> hmap = s.getHmap();
        if (aux.size() > 1) {
            if (aux.size() * (aux.size() - 1) != hmap.size()) {
                System.out.println("Error con relaciones y productos");
                return false;
            }
        }

        if (num == 1) {
            Backtracking b = new Backtracking(s.getHmap());
            ArrayList<Integer> sol = b.getSol();
            if (e.aplicaOrden(sol)) {
                System.out.println("Estanteria ordenada");
                return true;
            } else {
                System.out.println("Ha habido algun error en la ordenacion");
                return false;
            }
        } else if (num == 2) {
            Kruskal k = new Kruskal(s.getHmap());
            ArrayList<Integer> sol = k.MST(e.getIdProductos().size() + 1);
            if (e.aplicaOrden(sol)) {
                System.out.println("Estanteria ordenada");
                return true;
            } else {
                System.out.println("Ha habido algun error en la ordenacion");
                return false;
            }
        } else if (num == 3) {
            HillClimbing hc = new HillClimbing(s.getHmap(), e.getIdProductos().size());
            ArrayList<Integer> sol = hc.ExHillClimbing();
            if (e.aplicaOrden(sol)) {
                System.out.println("Estanteria ordenada");
                return true;
            } else {
                System.out.println("Ha habido algun error en la ordenacion");
                return false;
            }
        }
        return false;
    }

    //Funcion que nos permite modificar el grado de similitud entre dos productos
    public boolean modificarSimilitudProductos(Producto p1, Producto p2, Float sim) {
        Integer prod1ID = e.getIdProducto(p1);
        Integer prod2ID = e.getIdProducto(p2);

        try
        {
            s.modificarSimilitud(prod1ID, prod2ID, sim);
        }
        catch(NoExisteSimilitudException ex)
        {

        }
        return true;
    }

    //Funcion que nos devuelve el grado de similitud entre dos productos
    public Float getSimilitudProductos(Producto p1, Producto p2) {

        try {
            validarProductos(p1, p2);
        } catch (NoExistenProductosException | ProductoNoExisteException ex) {
            // Product 1 doesn't exist.
            // Product 2 doesn't exist.
            // Both products don't exist.
        }
        Integer prod1 = e.getIdProducto(p1);
        Integer prod2 = e.getIdProducto(p2);
        try {
            return s.getSimilitud(prod1, prod2);
        } catch (NoExisteSimilitudException | ValorSimilitudNoIgualException | MismoProductoException ex) {

        }
        return -1.0f;
    }

    //Funcion que nos devuelve los grados de similitud de un producto
    public String getSimilitudesProducto(Producto p) { // acabar
        int prod1ID = 1;
        String print = "";

        prod1ID = e.getIdProducto(p);
        
        if (prod1ID == -1) {
            // Write proper exception handler
            System.out.println("El producto no existe");
        } else {

            Iterator<Map.Entry<Integer, Producto>> it = e.getProductos().entrySet().iterator();
            while (it.hasNext()) {
                HashMap.Entry<Integer, Producto> e = it.next();

                Integer prod2ID = e.getKey();

                Producto prod2 = e.getValue();
                String nombreP2 = prod2.getNom();
                String marcaP2 = prod2.getMarca();

                Float val = 0.0f;

                if (prod2ID != prod1ID) {
                    try {
                        val = s.getSimilitud(prod1ID, prod2ID);
                    } catch (NoExisteSimilitudException | ValorSimilitudNoIgualException | MismoProductoException ex) {
                        System.out.println(ex.getMessage());
                    }
                    print += "La similitud entre " + p.getNom() + " " + p.getMarca() + " y " + nombreP2 + " " + marcaP2 + " es: " + val + "\n";
                }
            }
            System.out.println(print);
        }
        return print;
    }

    //Funcion que nos permite intercambiar la posicion de los dos productos
    public boolean intercambiarProductos(Producto p1, Producto p2) {

        try {
            validarProductos(p1, p2);
        } catch (NoExistenProductosException | ProductoNoExisteException ex) {
            // Product 1 doesn't exist.
            // Product 2 doesn't exist.
            // Both products don't exist.
        }

        ArrayList<Integer> orden = e.getIdProductos();
        Integer prod1ID = existeProducto(p1);
        Integer prod2ID = existeProducto(p2);

        if (prod1ID.equals(prod2ID)) {
            System.out.println("Los productos introducidos son el mismo");
            return false;
        }

        for (int i = 0; i < orden.size(); ++i) {
            if (orden.get(i).equals(prod1ID))
                orden.set(i, prod2ID);
            else if (orden.get(i).equals(prod2ID))
                orden.set(i, prod1ID);
        }

        e.aplicaOrden(orden);
        return true;
    }

    //Funcion que nos devuelve los ids de dos productos (si existen en la estanteria)
    public void validarProductos(Producto p1, Producto p2)
            throws NoExistenProductosException, ProductoNoExisteException {
        int prod1 = e.getIdProducto(p1);
        int prod2 = e.getIdProducto(p2);

        if (prod1 == -1) {
            if (prod2 == -1) {
                throw new NoExistenProductosException("Los dos productos no existen.");
            }
            throw new ProductoNoExisteException("El producto " + p1.getNom() + " " + p1.getMarca() + " no existe.");
        }
        if (prod2 == -1) {
            throw new ProductoNoExisteException("El producto " + p2.getNom() + " " + p2.getMarca() + " no existe.");
        }
    }

    //Funcion que imprime la estanteria
    public String print() {
        return e.print();
    }

    //Funcion que nos devuelve un booleano indicando la existencia de un producto
    public Integer existeProducto(Producto p) {
        Integer prod1 = e.getIdProducto(p);
        if (prod1 == -1)
            return -1;
        return prod1;
    }


    //Funcion que nos vacia la estanteria 
    public boolean vaciarEstanteria() {
        e.vaciarEstanteria();
        s.vaciarEstanteria();
        System.out.println("La estanteria se ha vaciado exitosamente");
        return true;
    }
}