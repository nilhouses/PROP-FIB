import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.beans.Transient;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import dominio.Similitudes;
import dominio.Producto;
import dominio.Pair;

public class TestSimilitudes {
    
    @Test
    public void TestCreadoraYGetMapSimilitudes() {
        Similitudes s = new Similitudes();
        HashMap<Pair<Integer, Integer>, Float> aux = new HashMap<>();
        assertEquals(s.getHmap(), aux);
    }
    
    @Test 
    public void TestAnadirSimilitud() {
        Similitudes s = new Similitudes();
        HashMap<Integer, Producto> prods = new HashMap<>();
        prods.put(1, new Producto(1, "Patatas", "Lays"));
        prods.put(2, new Producto(2, "Patatas", "Campesinas"));
        prods.put(2, new Producto(3, "Patatas", "Pringles"));

        String inp = "0.1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(inp.getBytes());
        System.setIn(in);

        assertTrue("Comprueba que la funcion retorne true", s.anadirSimilitud(2, 3, 0.2f));

        assertNotEquals("Comprueba que no se haya anado otro valor",0.1f, s.getSimilitud(2, 3), 0.0001f);

        assertEquals("Comprueba que se ha anadido el valor esperado", 0.2f, s.getSimilitud(2, 3), 0.0001f);
    }

    @Test
    public void TestModificarSimilitud() {
        Similitudes s = new Similitudes();
        s.anadirSimilitud(1, 2, 0.1f);
        
        assertTrue("Comprueba que la funcion retorne true", s.modificarSimilitud(1, 2, 0.2f));

        assertEquals("Comprueba que el valor modificado se ha modificado correctamente en una similitud", 0.2f, s.getSimilitud(1, 2), 0.0001f);
        assertEquals("Comprueba que el valor modificado se ha modificado correctamente en la otra similitud", 0.2f, s.getSimilitud(2, 1), 0.0001f);
    }

    @Test
    public void TestEliminarSimilitud() {
        Similitudes s = new Similitudes();
        s.anadirSimilitud(1, 2, 0.1f);
        ArrayList<Integer> prods = new ArrayList<>();
        prods.add(1);
        prods.add(2);

        assertTrue("Comprueba que la funcion retorne true", s.eliminarSimilitud(prods, 1));
        
        assertEquals("Comprueba que al haber eliminado la similitud, al consultarla devuelve -1.0", -1.0f, s.getSimilitud(1, 2), 0.0001f);
    }

    @Test 
    public void TestGetSimilitud() {
        Similitudes s = new Similitudes();
        s.anadirSimilitud(1, 2, 0.1f);

        assertEquals("Comprueba que el valor retornado sea igual al de la similitud anadida", 0.1f, s.getSimilitud(1, 2), 0.0001f);
    }

    @Test 
    public void TestGetSimilitudes() {
        Similitudes s = new Similitudes();
        s.anadirSimilitud(1, 2, 0.2f);
        s.anadirSimilitud(1, 3, 0.3f);

        ArrayList<Integer> prods = new ArrayList<>();
        prods.add(1);
        prods.add(2);
        prods.add(3);

        Vector<Pair<Integer, Float>> vec = s.getSimilitudes(prods, 1);
        assertEquals("Comprueba que el tamano del vector sea correcto", 2, vec.size());
        assertTrue("Comprueba el primer elemento del vector", vec.contains(new Pair<>(2, 0.2f)));
        assertTrue("Comprueba el segundo elemento del vector", vec.contains(new Pair<>(3, 0.3f)));

    }

    @Test 
    public void TestVaciarEstanteria() {
        Similitudes s = new Similitudes();
        s.anadirSimilitud(1, 2, 0.2f);
        s.vaciarEstanteria();
        HashMap<Pair<Integer, Integer>, Float> h = new HashMap<>();
        assertEquals("Comprueba que el HashMap acabe vacio", s.getHmap(), h);
    }
    
}
