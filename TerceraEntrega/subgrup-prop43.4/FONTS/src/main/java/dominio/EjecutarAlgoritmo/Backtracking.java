package dominio.EjecutarAlgoritmo;
import dominio.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Comparator;


//Esta clase se encarga de ordenar los productos de la estantería (representados por su id) según sus similitudes.

public class Backtracking {

    private HashMap<Integer, ArrayList<Pair<Integer, Float>>> grafo; // Representa el grafo completo de similitudes entre productos (los ids son nodos y las relaciones entre productos aristas con peso)
    private ArrayList<Integer> solucion;                             // Almacena el ciclo con mayor peso encontrado hasta el momento, para representar la solución óptima
    private float maxSumaPeso;                                       // Almacena el valr del ciclo mencionado anteriormente
    
    /*
     * Función Creadora, se encarga de ejecutar el algoritmo.
     *  PARaMETRO -> similitudes entre los productos
    */
    public Backtracking(HashMap<Pair<Integer, Integer>, Float> similitudes) {
        // Inicialización de atributos
        this.grafo = new HashMap<>();
        this.solucion = new ArrayList<>();
        this.maxSumaPeso = 0.0f;
        asignaRelaciones(similitudes);
        /*
         * Inicio el backtracking desde el primer nodo disponible arbitrariamente, ya que por la geometría circular de la estantería,
         * el ciclo con mayor peso se puede empezar desde cualquier nodo.
         */
        Map.Entry<Integer, ArrayList<Pair<Integer, Float>>> primeraEntrada = grafo.entrySet().iterator().next();
        Integer primerProducto = primeraEntrada.getKey();
        // Asigno a solucion el ciclo Hamiltoniano del grafo (cubre todos los vértices) y tiene la suma de pesos mas grande posible
        HashSet<Integer> visitados = new HashSet<>();
        ArrayList<Integer> soltemp = new ArrayList<>();
        backtracking(primerProducto, primerProducto, soltemp, visitados, 0);
    }

    /*
     * Función "Traductora",  traduce la estructura de datos de similitudes a la que se usa en algoritmo, almacenada en el atributo grafo.
     *  PARaMETRO -> similitudes entre los productos
    */
    private void asignaRelaciones(HashMap<Pair<Integer, Integer>, Float> similitudes) {
        // Uso un conjunto para no tener duplicados
        Set<String> addedEdges = new HashSet<>();

        for (Map.Entry<Pair<Integer, Integer>, Float> arista : similitudes.entrySet()) {
            Pair<Integer, Integer> key = arista.getKey();
            Integer p1 = key.getFirst();
            Integer p2 = key.getSecond();
            Float gradoRel = arista.getValue();

            // 2 identificadores para la misma arista, porque el tipo string es mas facil de comparar que el timpo Pair<>
            String edgeKey1 = p1 + "-" + p2;
            String edgeKey2 = p2 + "-" + p1;

            // Añadir aristas
            if (!addedEdges.contains(edgeKey1) && !addedEdges.contains(edgeKey2)) {

                grafo.putIfAbsent(p1, new ArrayList<>());
                grafo.putIfAbsent(p2, new ArrayList<>());
                grafo.get(p1).add(new Pair<>(p2, gradoRel));
                grafo.get(p2).add(new Pair<>(p1, gradoRel));
                addedEdges.add(edgeKey1);
                addedEdges.add(edgeKey2);
            }
        }
    }

    /*
     * Función Recursiva que se encarga de ejecutar el algoritmo.
     *  PARaMETROS
     *          nodo       -> Nodo que se recorre en esta llamada
     *          nodoI      -> Nodo desde el que se inicia el algoritmo
     *          soltemp    -> Solucion que se esta consiguiendo
     *          visitados  -> Conjunto que me indica si un nodo esta visitado o no
     *          pesoActual -> Peso que representa la solucion actual
    */
    private void backtracking(Integer nodo, Integer nodoI, ArrayList<Integer> soltemp, HashSet<Integer> visitados, float pesoActual) {
        //Aquí descartamos nodos ya visitados y descartamos una solucion parcial en el caso que la suma de los pesos restantes no pueda llegar al máximo actual 
        if ((visitados.contains(nodo) && !nodo.equals(nodoI)) || (grafo.size() - visitados.size()) < maxSumaPeso - pesoActual) return;
        // Añado el nodo si no vuelve a ser el primero
        if (!visitados.contains(nodo) && (!nodo.equals(nodoI) || soltemp.size() == 0)) {
            soltemp.add(nodo);
            visitados.add(nodo);
        }
        // Si he completado un ciclo que incluye todos los nodos, verifico si la solución actual es mejor que la anterior
        if (soltemp.size() > 1 && visitados.size() == grafo.size() && nodo.equals(nodoI)) {
            if (pesoActual > maxSumaPeso) { // Nueva candidata a solución
                maxSumaPeso = pesoActual;
                this.solucion.clear();
                this.solucion.addAll(new ArrayList<>(soltemp)); // Añado una copia del atributo de la clase
            }

        } else { //Ciclo no completado
            for (Pair<Integer, Float> vecino : grafo.get(nodo)) {
                Integer siguienteNodo = vecino.getFirst();
                Float pesoArista = vecino.getSecond();
                if (!visitados.contains(siguienteNodo) || (siguienteNodo.equals(nodoI)&&soltemp.size()==grafo.size())) {
                    backtracking(siguienteNodo, nodoI, soltemp, visitados, pesoActual + pesoArista);
                }
            }
        }
        // Tiro atras para poder contemplar las otras combinaciones
        if (soltemp.size()>1 && !nodo.equals(nodoI)) {
            soltemp.remove(soltemp.size() - 1); // Elimino el último nodo añadido
            visitados.remove(nodo);             // Y lo considero no visitado sólo si no es el nodo inicial
        }
    }

    /*
     * Función consultora, devuelve la ArrayList que representan los productos en la estantería ordenados
    */
    public ArrayList<Integer> getSol() {
        return this.solucion;
    }
}
