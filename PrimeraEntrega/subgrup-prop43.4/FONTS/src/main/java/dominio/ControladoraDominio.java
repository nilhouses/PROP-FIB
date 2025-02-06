package dominio;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import dominio.EjecutarAlgoritmo.Backtracking;
import dominio.EjecutarAlgoritmo.HillClimbing;
import dominio.EjecutarAlgoritmo.Kruskal;

import java.util.Scanner;

public class ControladoraDominio {

    private Estanteria e;
    private Similitudes s;

    public ControladoraDominio() {
        e = new Estanteria();
        s = new Similitudes();
    }

    public Integer anadirProductoEstanteria(Producto p) {
        
        Integer prod1 = e.altaProducto(p);
        if (prod1 == -1) {System.out.println("Este producto ya existe");
            return -1;}
        System.out.println("El producto se ha insertado correctamente");
        return prod1;
    }

    public boolean anadirSimilitudProducto(Integer prod1, Integer prod2, Float sim) {
        s.anadirSimilitud(prod1, prod2, sim);
        s.anadirSimilitud(prod2, prod1, sim);
        return true;
    }

    public HashMap<Integer,Producto> getProds() {
        return e.getProductos();
    }

    public boolean eliminarProductoEstanteria(Producto p) {
        
        Integer prod1 = e.eliminaProducto(p);
        if (prod1 == -1) {System.out.println("El producto no existe");  return false;}
        if (!s.eliminarSimilitud(e.getIdProductos(), prod1)) {System.out.println("Han habido errores"); return false;}
        System.out.println("El producto se ha eliminado correctamente");
        return true;
    }

    public boolean ordenarEstanteria(Integer num) {

        ArrayList<Integer> aux = e.getIdProductos();
        if (aux.size() == 0) {System.out.println("No hay productos en la estanteria a ordenar"); return true;}
        if (aux.size() == 1) {System.out.println("Solo hay un producto en la estanteria");e.print(); return true;}
        HashMap<Pair<Integer, Integer>, Float> hmap = s.getHmap();
        if (aux.size() > 1) {
            if (aux.size()*(aux.size()-1) != hmap.size()) {System.out.println("Error con relaciones y productos"); return false;}
        }
        
        if (num == 1) {
            Backtracking b = new Backtracking(s.getHmap());
            ArrayList<Integer> sol = b.getSol();
            if (e.aplicaOrden(sol)){
                System.out.println("Estanteria ordenada, aqui tiene el nuevo orden:");
                e.print();
                return true;
            }
            else {System.out.println("Ha habido algun error en la ordenacion"); return false;}
        }
        else if (num == 2) {
            Kruskal k = new Kruskal(s.getHmap());
            ArrayList<Integer> sol = k.MST(e.getIdProductos().size() + 1);
            if (e.aplicaOrden(sol)){
                System.out.println("Estanteria ordenada, aqui tiene el nuevo orden:");
                e.print();
                return true;
            }
            else {System.out.println("Ha habido algun error en la ordenacion"); return false;}
        }
        else if (num == 3) {
            HillClimbing hc = new HillClimbing(s.getHmap(), e.getIdProductos().size());
            ArrayList<Integer> sol = hc.ExHillClimbing();
            if (e.aplicaOrden(sol)){
                System.out.println("Estanteria ordenada, aqui tiene el nuevo orden:");
                e.print();
                return true;
            }
            else {System.out.println("Ha habido algun error en la ordenacion"); return false;}
        }
        return false;
    }
    
    public boolean modificarSimilitudProductos(Producto p1, Producto p2, Float sim) {
        Integer prod1ID = e.getIdProducto(p1);
        Integer prod2ID = e.getIdProducto(p2);

        s.modificarSimilitud(prod1ID, prod2ID, sim);
        s.modificarSimilitud(prod2ID, prod1ID, sim);
        return true;
    }

    public Float getSimilitudProductos(Producto p1, Producto p2) {

        Pair<Boolean, Boolean> p = validarProductos(p1,p2);
        
        if (p.equals(new Pair<>(true, false))) {System.out.println("El producto " + p2.getNom() + " " + p2.getMarca() + " no existe en la estanteria"); return -1.0f;}
        if (p.equals(new Pair<>(false, true))) {System.out.println("El producto " + p1.getNom() + " " + p2.getMarca() + " no existe en la estanteria"); return -1.0f;}
        if (p.equals(new Pair<>(false, false))) {System.out.println("Ninguno de los dos productos existe en la estanteria"); return -1.0f;}

        Integer prod1 = e.getIdProducto(p1);
        Integer prod2 = e.getIdProducto(p2);

        Float f = s.getSimilitud(prod1, prod2);
        return f;
    }

    public boolean getSimilitudesProducto(Producto p) {     //acabar
        int prod1ID = e.getIdProducto(p);
        if (prod1ID == -1) {System.out.println("El producto no existe");  return false;}

        Iterator<Map.Entry<Integer, Producto>> it = e.getProductos().entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry<Integer, Producto> e = it.next();

            Integer prod2ID = e.getKey();

            Producto prod2 = e.getValue();
            String nombreP2 = prod2.getNom();
            String marcaP2 = prod2.getMarca();

            if (prod2ID != prod1ID) {
                Float f = s.getSimilitud(prod1ID, prod2ID);
                if (f != -1) {
                    System.out.println("La similitud entre " + p.getNom() + " " + p.getMarca() + " y " + nombreP2 + " " + marcaP2 + " es: " + f);
                }
            }
        }

        return true;
    }

    public boolean intercambiarProductos(Producto p1, Producto p2) {

        Pair<Boolean, Boolean> p = validarProductos(p1,p2);
        
        if (p.equals(new Pair<>(true, false))) {System.out.println("El producto " + p2.getNom() + " " + p2.getMarca() + " no existe en la estanteria"); return false;}
        if (p.equals(new Pair<>(false, true))) {System.out.println("El producto " + p1.getNom() + " " + p1.getMarca() + " no existe en la estanteria"); return false;}
        if (p.equals(new Pair<>(false, false))) {System.out.println("Ninguno de los dos productos existe en la estanteria"); return false;}
        
        ArrayList<Integer> orden = e.getIdProductos();
        Integer prod1ID = existeProducto(p1);
        Integer prod2ID = existeProducto(p2);

        if (prod1ID.equals(prod2ID)) {System.out.println("Los productos introducidos son el mismo"); return false;}

        for(int i = 0; i < orden.size(); ++i) {
            if (orden.get(i).equals(prod1ID)) orden.set(i,prod2ID);
            else if (orden.get(i).equals(prod2ID)) orden.set(i, prod1ID);
        }

        e.aplicaOrden(orden);
        return true;
    }

    public Pair<Boolean, Boolean> validarProductos(Producto p1, Producto p2) {
        int prod1 = e.getIdProducto(p1);
        int prod2 = e.getIdProducto(p2);

        if (prod1 != -1) {
            if (prod2 != -1) return new Pair<>(true, true);
            return new Pair<>(true, false);
        }
        if (prod2 != -1) return new Pair<>(false, true);
        return new Pair<>(false, false);
    }

    public void print() {
        e.print();
    }

    public Integer existeProducto(Producto p) {
        Integer prod1 = e.getIdProducto(p);
        if (prod1 == -1) return -1;
        return prod1;
    }

    public boolean vaciarEstanteria() {
        e.vaciarEstanteria();
        s.vaciarEstanteria();
        System.out.println("La estanteria se ha vaciado exitosamente");
        return true;
    }
}