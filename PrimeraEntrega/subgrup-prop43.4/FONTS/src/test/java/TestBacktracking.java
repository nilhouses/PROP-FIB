import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;

import dominio.Pair;
import dominio.EjecutarAlgoritmo.Backtracking;

/*
    En este fichero queremos ver si con ciertas similitudes entre enteros y una lista de estos 
    enteros podemos reordenar correctamente esta lista con la clase Backtracking.

    Es necesario recalcar que dado que la estanteria es circular, nuestra implementación siempre
    devuelve una lista con el nodo con menor ID en primera posicón, ya que da igual por donde se
    empiece el ciclo a encontrar.
    Podemos observar, entonces, que devolver [1,3,2] seria equivalente a devolver [2,1,3],
    ya que se usan las mismas aristas (o similitudes) entre los mismos productos
*/

public class TestBacktracking {

    @Test
    public void TestBacktrackingBasico() {
        /*  TEST 1
         * En este test usamos tan solo 3 nodos, para comprovar el correcto funcionamiento del 
         * algoritmo de backtracking implementado en un ejemplo muy basico.
         *
        El grafo es el siguiente:
            Nodo 1 -> [2, 0.2] [3, 0.3]
            Nodo 2 -> [1, 0.2] [3, 0.1]
            Nodo 3 -> [1, 0.3] [2, 0.1]
        es decir,
                1
                /\
               /  \
             3/____\2
        */

        //parametro de creadora
        HashMap<Pair<Integer, Integer>, Float> similitudes = new HashMap<>();

        //añadimos la información al parametro
        similitudes.put(new Pair<>(1,2),0.2f);
        similitudes.put(new Pair<>(1,3),0.3f);
        similitudes.put(new Pair<>(2,1),0.2f);
        similitudes.put(new Pair<>(2,3),0.1f);
        similitudes.put(new Pair<>(3,1),0.3f);
        similitudes.put(new Pair<>(3,2),0.1f);
        /*
         * En este caso la estanteria debe ser [1,2,3], ya que los pesos de sus aristas tienen  como
         * suma de pesos (0.2+0.1+0.3) = 0.6, este caso es muy poco útil ya que solo hay 1 ciclo, y se
         * puede recorrer como [1,2,3] o [1,3,2] en la forma que hemos implementado el algoritmo.
        */
        ArrayList<Integer> solucionDeseada = new ArrayList<>(Arrays.asList(1,2,3));
        //creamos instancia del algoritmo y obtenemos la solucion para poder compararla
        Backtracking b = new Backtracking(similitudes);
        ArrayList<Integer> solucion = b.getSol();
        assertTrue(solucion.equals(solucionDeseada));
    }

    @Test
    public void TestBacktrackingSimple() {
        //TEST 2
        /*
         * En este test usamos 4 nodos, para comprovar el correcto funcionamiento del algoritmo implementado 
         * con un caso sencillo, en el que forzamos que el ciclo no sea [1,2,3,4] para comprobar que se 
         * ejecuta correctamente el algoritmo cuando se encuentra otro ciclo con mayor suma de pesos que 
         * el primer ciclo encontrado.
         * 
        El grafo es el siguiente:
            Nodo 1 -> [2, 0.2] [3, 0.9] [4, 0.1]
            Nodo 2 -> [1, 0.2] [3, 0.1] [4, 0.9]
            Nodo 3 -> [1, 0.9] [2, 0.1] [4, 0.9]
            Nodo 4 -> [1, 0.1] [2, 0.9] [3, 0.9]
        Es decir,
          1 _______2
            |\   /|
            | \ / |
            |  X  |
            | / \ |
            |/   \|
           3|_____|4

        */

        //parametro de creadora
        HashMap<Pair<Integer, Integer>, Float> similitudes = new HashMap<>();

        //añadimos la información al parametro
        similitudes.put(new Pair<>(1,2),0.2f);
        similitudes.put(new Pair<>(1,3),0.9f);
        similitudes.put(new Pair<>(1,4),0.1f);

        similitudes.put(new Pair<>(2,1),0.2f);
        similitudes.put(new Pair<>(2,3),0.1f);
        similitudes.put(new Pair<>(2,4),0.9f);

        similitudes.put(new Pair<>(3,1),0.9f);
        similitudes.put(new Pair<>(3,2),0.1f);
        similitudes.put(new Pair<>(3,4),0.9f);

        similitudes.put(new Pair<>(4,1),0.1f);
        similitudes.put(new Pair<>(4,2),0.9f);
        similitudes.put(new Pair<>(4,3),0.9f);

        /*
         * En este caso la estanteria debe ser [1,2,4,3], ya que los pesos de sus aristas
         * tienen suma (0.2+0.9+0.9+0.9) = 2.9, y es la primera vez que el algoritmo llega
         * a encontrar el ciclo hamiltoniano de este peso (2.9f)
        */

        //añadimos la información al parametro
        ArrayList<Integer> solucionDeseada = new ArrayList<>(Arrays.asList(1,2,4,3));
        //creamos instancia del algoritmo y obtenemos la solucion para poder compararla
        Backtracking b = new Backtracking(similitudes);
        ArrayList<Integer> solucion = b.getSol();
        assertTrue(solucion.equals(solucionDeseada));
    }

    @Test
    public void TestBacktrackingExtra() {
        //TEST 3
        /*
         * En este test usamos 5 nodos, para comprovar el correcto funcionamiento del algoritmo
         * de backtracking implementado con un caso en el que forzamos que el ciclo sea [1,3,2,4,5],
         * el el primer ciclo con pesomaximo encontrado.
         * 
        El grafo es el siguiente:
            Nodo 1 -> [2, 0.0] [3, 1.0] [4, 0.0] [5, 0.9]
            Nodo 2 -> [1, 0.0] [3, 0.9] [4, 0.9] [5, 0.0]
            Nodo 3 -> [1, 1.0] [2, 0.9] [4, 0.0] [5, 0.0]
            Nodo 4 -> [1, 1.0] [2, 0.9] [3, 0.0] [5, 0.9]
            Nodo 5 -> [1, 1.9] [2, 0.0] [3, 0.0] [4, 0.9]
        Es decir, (si solo dibujamos las aristas con pesos > 0),
                 1 _______3________
                  /       \        |
                 /         \       |
               5 \         / 2     |
                  \       /        |
                   \     /         |
                    \   /          |
                     \ /___________|
                      4
         */

        //parametro de creadora
        HashMap<Pair<Integer, Integer>, Float> similitudes = new HashMap<>();

        //añadimos la información al parametro
        similitudes.put(new Pair<>(1,2),0.0f);
        similitudes.put(new Pair<>(1,3),1.0f);
        similitudes.put(new Pair<>(1,4),0.0f);
        similitudes.put(new Pair<>(1,5),0.9f);

        similitudes.put(new Pair<>(2,1),0.0f);
        similitudes.put(new Pair<>(2,3),0.9f);
        similitudes.put(new Pair<>(2,4),0.9f);
        similitudes.put(new Pair<>(2,5),0.0f);

        similitudes.put(new Pair<>(3,1),1.0f);
        similitudes.put(new Pair<>(3,2),0.9f);
        similitudes.put(new Pair<>(3,4),0.0f);
        similitudes.put(new Pair<>(3,5),0.0f);

        similitudes.put(new Pair<>(4,1),0.0f);
        similitudes.put(new Pair<>(4,2),0.9f);
        similitudes.put(new Pair<>(4,3),0.0f);
        similitudes.put(new Pair<>(4,5),0.9f);

        similitudes.put(new Pair<>(5,1),0.9f);
        similitudes.put(new Pair<>(5,2),0.0f);
        similitudes.put(new Pair<>(5,3),0.0f);
        similitudes.put(new Pair<>(5,4),0.9f);

        /*
         * En este caso la estanteria debe ser [1,3,2,4,5], ya que los pesos de sus aristas
         * tienen suma (0.2+0.9+0.9+0.9) = 2.9, y es la primera vez que el algoritmo llega
         * a encontrar el ciclo hamiltoniano de este peso (2.9f)
        */

        //añadimos la información al parametro
        ArrayList<Integer> solucionDeseada = new ArrayList<>(Arrays.asList(1,3,2,4,5));
        //creamos instancia del algoritmo y obtenemos la solucion para poder compararla
        Backtracking b = new Backtracking(similitudes);
        ArrayList<Integer> solucion = b.getSol();
        assertTrue(solucion.equals(solucionDeseada));
    }

    @Test
    public void TestBacktrackingComplejo() {
        //TEST 4
        /*
         * En este test usamos 10 nodos para comprobar el correcto funcionamiento del algoritmo
         * de backtracking implementado con un caso en el que se fuerza que el ciclo hamiltoniano
         * con mayor peso encontrado sea [1, 3, 5, 2, 10, 8, 4, 9, 7, 6],
         * con peso 1.0+0.9+0.9+0.7+0.9+0.8+0.9+0.5+0.5+0.7=7.9
         * 
        El grafo es el siguiente:
          Nodo 1 -> [3, 1.0] [5, 0.8] [7, 0.7] [2, 0.5] [4, 0.6] [6, 0.7] [8, 0.4] [9, 0.5] [10, 0.6]
          Nodo 2 -> [1, 0.5] [4, 0.6] [6, 0.4] [8, 0.3] [3, 0.8] [5, 0.9] [7, 0.4] [9, 0.2] [10, 0.7]
          Nodo 3 -> [1, 1.0] [5, 0.9] [7, 0.2] [9, 0.3] [2, 0.8] [4, 0.7] [6, 0.6] [8, 0.5] [10, 0.4]
          Nodo 4 -> [2, 0.6] [6, 0.5] [8, 0.8] [10, 0.7] [1, 0.5] [3, 0.7] [5, 0.4] [7, 0.6] [9, 0.9]
          Nodo 5 -> [1, 0.8] [3, 0.9] [7, 0.4] [9, 0.1] [2, 0.9] [4, 0.4] [6, 0.7] [8, 0.6] [10, 0.5]
          Nodo 6 -> [2, 0.4] [4, 0.5] [8, 0.2] [10, 0.6] [1, 0.7] [3, 0.6] [5, 0.7] [7, 0.5] [9, 0.3]
          Nodo 7 -> [1, 0.7] [3, 0.2] [5, 0.4] [9, 0.5] [2, 0.4] [4, 0.6] [6, 0.5] [8, 0.6] [10, 0.3]
          Nodo 8 -> [2, 0.3] [4, 0.8] [6, 0.2] [10, 0.9] [1, 0.7] [3, 0.5] [5, 0.6] [7, 0.6] [9, 0.7]
          Nodo 9 -> [3, 0.3] [5, 0.1] [7, 0.5] [10, 0.6] [2, 0.2] [4, 0.9] [6, 0.3] [8, 0.7] [1, 0.6]
          Nodo 10 -> [4, 0.7] [6, 0.6] [8, 0.9] [9, 0.6] [1, 0.5] [3, 0.4] [5, 0.5] [7, 0.3] [2, 0.7]
        */

        //parametro de creadora
        HashMap<Pair<Integer, Integer>, Float> similitudes = new HashMap<>();

        //añadimos la información al parametro
        similitudes.put(new Pair<>(1, 3), 1.0f);
        similitudes.put(new Pair<>(1, 5), 0.8f);
        similitudes.put(new Pair<>(1, 7), 0.7f);
        similitudes.put(new Pair<>(1, 2), 0.5f);
        similitudes.put(new Pair<>(1, 4), 0.6f);
        similitudes.put(new Pair<>(1, 6), 0.7f);
        similitudes.put(new Pair<>(1, 8), 0.4f);
        similitudes.put(new Pair<>(1, 9), 0.5f);
        similitudes.put(new Pair<>(1, 10), 0.6f);
        
    
        similitudes.put(new Pair<>(2, 1), 0.5f);
        similitudes.put(new Pair<>(2, 4), 0.6f);
        similitudes.put(new Pair<>(2, 6), 0.4f);
        similitudes.put(new Pair<>(2, 8), 0.3f);
        similitudes.put(new Pair<>(2, 3), 0.8f);
        similitudes.put(new Pair<>(2, 5), 0.9f);
        similitudes.put(new Pair<>(2, 7), 0.4f);
        similitudes.put(new Pair<>(2, 9), 0.2f);
        similitudes.put(new Pair<>(2, 10), 0.7f);
    
        similitudes.put(new Pair<>(3, 1), 1.0f);
        similitudes.put(new Pair<>(3, 5), 0.9f);
        similitudes.put(new Pair<>(3, 7), 0.2f);
        similitudes.put(new Pair<>(3, 9), 0.3f);
        similitudes.put(new Pair<>(3, 2), 0.8f);
        similitudes.put(new Pair<>(3, 4), 0.7f);
        similitudes.put(new Pair<>(3, 6), 0.6f);
        similitudes.put(new Pair<>(3, 8), 0.5f);
        similitudes.put(new Pair<>(3, 10), 0.4f);
    
        similitudes.put(new Pair<>(4, 2), 0.6f);
        similitudes.put(new Pair<>(4, 6), 0.5f);
        similitudes.put(new Pair<>(4, 8), 0.8f);
        similitudes.put(new Pair<>(4, 10), 0.7f);
        similitudes.put(new Pair<>(4, 1), 0.5f);
        similitudes.put(new Pair<>(4, 3), 0.7f);
        similitudes.put(new Pair<>(4, 5), 0.4f);
        similitudes.put(new Pair<>(4, 7), 0.6f);
        similitudes.put(new Pair<>(4, 9), 0.9f);
    
        similitudes.put(new Pair<>(5, 1), 0.8f);
        similitudes.put(new Pair<>(5, 3), 0.9f);
        similitudes.put(new Pair<>(5, 7), 0.4f);
        similitudes.put(new Pair<>(5, 9), 0.1f);
        similitudes.put(new Pair<>(5, 2), 0.9f);
        similitudes.put(new Pair<>(5, 4), 0.4f);
        similitudes.put(new Pair<>(5, 6), 0.7f);
        similitudes.put(new Pair<>(5, 8), 0.6f);
        similitudes.put(new Pair<>(5, 10), 0.5f);
    
        similitudes.put(new Pair<>(6, 2), 0.4f);
        similitudes.put(new Pair<>(6, 4), 0.5f);
        similitudes.put(new Pair<>(6, 8), 0.2f);
        similitudes.put(new Pair<>(6, 10), 0.6f);
        similitudes.put(new Pair<>(6, 1), 0.7f);
        similitudes.put(new Pair<>(6, 3), 0.6f);
        similitudes.put(new Pair<>(6, 5), 0.7f);
        similitudes.put(new Pair<>(6, 7), 0.5f);
        similitudes.put(new Pair<>(6, 9), 0.3f);
    
        similitudes.put(new Pair<>(7, 1), 0.7f);
        similitudes.put(new Pair<>(7, 3), 0.2f);
        similitudes.put(new Pair<>(7, 5), 0.4f);
        similitudes.put(new Pair<>(7, 9), 0.5f);
        similitudes.put(new Pair<>(7, 2), 0.4f);
        similitudes.put(new Pair<>(7, 4), 0.6f);
        similitudes.put(new Pair<>(7, 6), 0.5f);
        similitudes.put(new Pair<>(7, 8), 0.6f);
        similitudes.put(new Pair<>(7, 10), 0.3f);
    
        similitudes.put(new Pair<>(8, 2), 0.3f);
        similitudes.put(new Pair<>(8, 4), 0.8f);
        similitudes.put(new Pair<>(8, 6), 0.2f);
        similitudes.put(new Pair<>(8, 10), 0.9f);
        similitudes.put(new Pair<>(8, 1), 0.7f);
        similitudes.put(new Pair<>(8, 3), 0.5f);
        similitudes.put(new Pair<>(8, 5), 0.6f);
        similitudes.put(new Pair<>(8, 7), 0.6f);
        similitudes.put(new Pair<>(8, 9), 0.7f);
    
        similitudes.put(new Pair<>(9, 3), 0.3f);
        similitudes.put(new Pair<>(9, 5), 0.1f);
        similitudes.put(new Pair<>(9, 7), 0.5f);
        similitudes.put(new Pair<>(9, 10), 0.6f);
        similitudes.put(new Pair<>(9, 2), 0.2f);
        similitudes.put(new Pair<>(9, 4), 0.9f);
        similitudes.put(new Pair<>(9, 6), 0.3f);
        similitudes.put(new Pair<>(9, 8), 0.7f);
        similitudes.put(new Pair<>(9, 1), 0.6f);
    
        similitudes.put(new Pair<>(10, 4), 0.7f);
        similitudes.put(new Pair<>(10, 6), 0.6f);
        similitudes.put(new Pair<>(10, 8), 0.9f);
        similitudes.put(new Pair<>(10, 9), 0.6f);
        similitudes.put(new Pair<>(10, 1), 0.5f);
        similitudes.put(new Pair<>(10, 3), 0.4f);
        similitudes.put(new Pair<>(10, 5), 0.5f);
        similitudes.put(new Pair<>(10, 7), 0.3f);
        similitudes.put(new Pair<>(10, 2), 0.7f);

        //añadimos la información al parametro
        ArrayList<Integer> solucionDeseada = new ArrayList<>(Arrays.asList(1, 3, 5, 2, 10, 8, 4, 9, 7, 6));
        //creamos instancia del algoritmo y obtenemos la solucion para poder compararla
        Backtracking b = new Backtracking(similitudes);
        ArrayList<Integer> solucion = b.getSol();
        assertTrue(solucion.equals(solucionDeseada));
    }
}

