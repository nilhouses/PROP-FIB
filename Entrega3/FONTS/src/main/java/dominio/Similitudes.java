package dominio;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;

import dominio.excepciones.*;

public class Similitudes implements Serializable {

    private HashMap<Pair<Integer, Integer>, Float> hmap;      // Atributo existente en la primera entrega
    private HashMap<String, Float> jsonHmap;                  // Nuevo atributo que se encarga de la traduccion con JSON
    /*
     * Somos conscientes que podriamos haber unificado estos atributos para no tener la informacion duplicada y usar solo el jsonHmap, ya que 
     * a partir de este se puede extraer la informacion, pero hemos decidido dejarlo asi (ya que ya funcionaba) y porque somos un grupo reducido 
     * y hemos decidido emplear nuestro tiempo en programar otras partes del proyecto 
    */

    // Constructor
    public Similitudes() {
        hmap = new HashMap<>();
        jsonHmap = new HashMap<>();
    }

    //FUNCIONES NUEVAS PARA LA PERSISTENCIA
    @JsonCreator
    public Similitudes(@JsonProperty("jsonHmap") HashMap<String, Float> jsonHmap) {
        this.jsonHmap = jsonHmap; 
        this.hmap = new HashMap<>(); 
    
        for (Map.Entry<String, Float> entry : jsonHmap.entrySet()) {
            String key = entry.getKey();                                // Ex: "1,2"
            String[] parts = key.split(",");                     // Separamos los valores
            Integer first = Integer.parseInt(parts[0]);               // Convertimos el primero a Integer
            Integer second = Integer.parseInt(parts[1]);             // Convertimos el segundo a Integer
            Pair<Integer, Integer> pair = new Pair<>(first, second);// Creamos el Pair
            this.hmap.put(pair, entry.getValue());                 // Actualizamos el hashmap original
        }
    }
    //consultora de json
    public HashMap<String, Float> getJsonHmap() {
        return jsonHmap;
    }
    //////////////////////////////////FIN FUNCIONES JSON

    //Funcion que se encarga de añadir un grado de similitud entre 2 productos
    public boolean anadirSimilitud(Integer prod1, Integer prod2, float sim) throws SimilitudExisteException{
        Pair<Integer, Integer> p = new Pair<>(prod1, prod2);
        if (hmap.get(p) != null)
        {
            throw new SimilitudExisteException("El producto ya existe.");
        }     
        hmap.put(p, sim);
        p = new Pair<>(prod2, prod1);
        if (hmap.get(p) != null)
        {
            throw new SimilitudExisteException("El producto ya existe.");
        }
        hmap.put(p, sim);
        //hashmap json
        jsonHmap.put(prod1 + "," + prod2, sim);
        jsonHmap.put(prod2 + "," + prod1, sim);
        return true;
    }

    //Funcion que se encarga de eliminarlos grados de similitud de 1 producto
    public boolean eliminarSimilitud(ArrayList<Integer> prods, Integer prod1) {
        for (Integer i = 0; i < prods.size(); ++i) {
            Integer prod2 = prods.get(i);
            if (prod1 != prod2) {
                try{
                    eliminarSimilitud(prod1, prod2);
                }
                catch (NoExisteSimilitudException ex){
                    System.out.println("No hay ninguna similitud entre estos productos, el sistema tiene errores de almacenamiento de informacion");
                }
            }
        }
        return true;
    }

    //Funcion que se encarga de eliminar un grado de similitud entre 2 productos
    private boolean eliminarSimilitud(Integer prod1, Integer prod2) throws NoExisteSimilitudException{
        Pair<Integer, Integer> p = new Pair<>(prod1, prod2);
        Float nice = hmap.remove(p);
        if (nice == null)
        {
            throw new NoExisteSimilitudException("La similitud de entre productos no existe.");
        }
        p = new Pair<>(prod2, prod1);
        nice = hmap.remove(p);
        if (nice == null)
        {
            throw new NoExisteSimilitudException("La similitud de entre productos no existe.");
        }
        //hashmap json
        jsonHmap.remove(prod1 + "," + prod2);
        jsonHmap.remove(prod2 + "," + prod1);
        //cambios
        return true;
    }

    //Funcion que se encarga de modificar el grado de similitud entre 2 productos
    public boolean modificarSimilitud(Integer prod1, Integer prod2, float sim) throws NoExisteSimilitudException {
        Pair<Integer, Integer> p = new Pair<>(prod1, prod2);
        if (hmap.get(p) == null)
        {
            throw new NoExisteSimilitudException("La similitud de entre productos no existe.");
        }
        hmap.put(p, sim);
        p = new Pair<>(prod2, prod1);
        if (hmap.get(p) == null)
        {
            throw new NoExisteSimilitudException("La similitud de entre productos no existe.");
        }
        hmap.put(p, sim);
        //hashmap json
        jsonHmap.put(prod1 + "," + prod2, sim);
        jsonHmap.put(prod2 + "," + prod1, sim);
        //cambios
        return true;
    }

    @JsonIgnore //Funcion que se encarga de devolver el grado de similitud entre 2 productos
    public Float getSimilitud(Integer prod1, Integer prod2) throws NoExisteSimilitudException, ValorSimilitudNoIgualException, MismoProductoException {
        if (prod1 != prod2) {
            Pair<Integer, Integer> p = new Pair<>(prod1, prod2);
            Float sim1 = hmap.get(p);
            p = new Pair<>(prod2, prod1);
            Float sim2 = hmap.get(p);
            if(sim1 == null){
                throw new NoExisteSimilitudException("La similitud de entre productos no existe.");
            }
            if(sim2 == null){
                throw new NoExisteSimilitudException("La similitud de entre productos no existe.");
            }
            if(sim1.equals(sim2)){
                return sim1;
            } else{
                throw new  ValorSimilitudNoIgualException("El valor de la similitud no es el mismo. Error sistema.");
            }
        }else{
            throw new  MismoProductoException("Han insertado el mismo producto.");
        }
    }

    @JsonIgnore //Funcion que devuelve el atributo que almacena todas las similitudes
    public HashMap<Pair<Integer, Integer>, Float> getHmap() {
        return this.hmap;
    }

    //Funcion que se encarga de devolver todos los grados de similitud entre ciertos productos (ArrayList prods) con el producto indicado (parametro prod1) 
    public Vector<Pair<Integer, Float>> getSimilitudes(ArrayList<Integer> prods, Integer prod1) {
        Vector<Pair<Integer, Float>> todas = new Vector<>();

        for (Integer i = 0; i < prods.size(); i++) {
            Integer prod2 = prods.get(i);
            try {
                Pair<Integer, Float> p = new Pair<>(prod2, getSimilitud(prod1, prod2));
                todas.add(p);
            }
            catch (NoExisteSimilitudException | ValorSimilitudNoIgualException | MismoProductoException ex){
                System.out.println("Error con el acceso a similitudes, son del mismo producto, no existe");
            }
        }
        return todas;
    }

    //Funcion que se encarga de eliminar todas las similitudes de un usuario, se usa cuando el usuario vacia la estanteria
    public void vaciarEstanteria() {
        hmap = new HashMap<>();
        jsonHmap = new HashMap<>();
    }
}
