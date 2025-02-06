package dominio;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.Iterator;

public class Similitudes {

    private HashMap<Pair<Integer, Integer>, Float> hmap;

    public Similitudes() {
        hmap = new HashMap<>();
    }

    public boolean anadirSimilitud(Integer prod1, Integer prod2, float sim) {
        Pair<Integer,Integer> p = new Pair<>(prod1, prod2);

        if (hmap.get(p) != null) return false;     //modificar más adelante
        hmap.put(p, sim);
        p = new Pair<>(prod2, prod1);
        if (hmap.get(p) != null) return false;  
        hmap.put(p, sim);
        return true;
    }
    
    public boolean eliminarSimilitud(ArrayList<Integer> prods, Integer prod1) {
        for (Integer i = 0; i < prods.size(); ++i) {
            Integer prod2 = prods.get(i);
            if (prod1 != prod2) {
                boolean nice = eliminarSimilitud(prod1, prod2);
                if (!nice) return false;
            }
        }
        return true;
    }

    private boolean eliminarSimilitud(Integer prod1, Integer prod2) {
        Pair<Integer,Integer> p = new Pair<>(prod1, prod2);
        System.out.println(hmap.size());
        Float nice = hmap.remove(p);
        if (nice == null) return false;
        p = new Pair<>(prod2, prod1);
        nice = hmap.remove(p);
        if (nice == null) return false;
        return true;
    }

    public boolean modificarSimilitud(Integer prod1, Integer prod2, float sim) {
        Pair<Integer,Integer> p = new Pair<>(prod1, prod2);
        if (hmap.get(p) == null) return false;
        hmap.put(p, sim);
        p = new Pair<>(prod2, prod1);
        if (hmap.get(p) == null) return false;
        hmap.put(p, sim);
        return true;
    }

    public float getSimilitud(Integer prod1, Integer prod2) {
        if (prod1 != prod2) {
            Pair<Integer,Integer> p = new Pair<>(prod1, prod2);
            Float sim = hmap.get(p);
            if (sim != null) return sim;
            else {
                p = new Pair<>(prod2, prod1);
                sim = hmap.get(p);
                if (sim != null) return sim;
            }
        }
        return -1.0f;
    }

    public HashMap<Pair<Integer, Integer>, Float> getHmap() {
        return this.hmap;
    }

    public Vector<Pair<Integer, Float>> getSimilitudes(ArrayList<Integer> prods, Integer prod1) {
        Vector<Pair<Integer, Float>> todas =  new Vector<>();

        for (Integer i = 0; i < prods.size(); i++) {
            Integer prod2 = prods.get(i);
            Float sim = getSimilitud(prod1, prod2);
            if (sim != -1.0f) {
                Pair<Integer,Float> p = new Pair<>(prod2, sim);
                todas.add(p);
            }
        }
        return todas;
    }

    public void vaciarEstanteria() {
        hmap = new HashMap<>();
    }
}