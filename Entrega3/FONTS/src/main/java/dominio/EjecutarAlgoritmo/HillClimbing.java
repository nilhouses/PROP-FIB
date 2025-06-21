package dominio.EjecutarAlgoritmo;

import dominio.Pair;

import java.util.*;

public class HillClimbing {

    //Relationgraph es un arraylist donde se contienen las aristas con sus pesos y los       identificadores de los vertices.
    private HashMap<Integer,Integer> graphencrypt;
    private HashMap<Integer,Integer> graphdecrypt;

    //Contiene los costes de las aristas donde los vertices son el valor de fila y columna.
    private Float[][] adjMatrix;

    //Es el valor de indice para la traduccion de los productos al ejecutar la creadora de Hill Climbing. Incrementa por 1 cada vez que se inserta un nuevo producto.Indica en que posicion esta ese producto dentro de la matriz.
    private int count = 0;

    //Es el numero de productos que hay para ordenar.
    private int n;

    //La funcion heuristica que calcula el coste total de la solucion dada. Devuelve el coste encontrado para que sea comparado.
    private Float Heuristic(int [] point) {
        Float cost = adjMatrix[point[0]][point[n - 1]];
        for(int i = 0; i < n; i++) {
            cost = cost + adjMatrix[point[i%n]][point[(i + 1)%n]];
        }
        return cost;
    }

    //Generadora de los vecinos de la solucion dada. Devuelve una LinkedList con todas las possibles nuevas combinaciones con solo cambiar dos nodos de posicion.
    private LinkedList<int []> NeighboringSolutions(int []  currPoint) {
        int i, j;
        LinkedList<int []> neighborhood = new LinkedList<int[]>();
        int rep = calcrep();
        for(i = 0; i < rep; i++)
        {
            neighborhood.add(Arrays.copyOf(currPoint, currPoint.length));
        }
        int pos = 1;
        for(i = 0; i < n; i++) {
            for(j = i + 1; j < n; j++)
            {
                SwapOperator(neighborhood.get(pos),i, j%n);
                pos++;
            }
        }
        return neighborhood;
    }

    //Funcion que la mida de la LinkedList dependiendo de la cantidad de productos a ordenar. La mida sigue una funcion de (n - 1) + (n - 2) + ... + (n - n + 1) + 1. Se hace +1 para conservar la solucion inicial.
    private int calcrep (){
        int cnt = 1;
        for(int i = n - 1; i > 0; i--)
        {
            cnt += i;
        }
        return cnt;
    }

    //Operacion simple para hacer el swap. Intercanvia dos elementos en un vector.
    private void SwapOperator(int[] currPoint, int i, int j) {
        int temp = currPoint[i];
        currPoint[i] = currPoint[j];
        currPoint[j] = temp;
    }

    //Generadora de la solucion inicial. Crea aleatoriamente una secuencia de productos sin repetir ninguno.
    private int[] InitialSolutionGenerator() {
        int [] vecCities = new int[n];
        ArrayList<Integer> availablenum = new ArrayList<Integer>();
        for(int i = 0; i < n; i++)
        {
            availablenum.add(i);
        }
        Collections.shuffle(availablenum);
        for(int i = 0; i < n; i++)
        {
            vecCities[i] = availablenum.get(i);
        }
        return vecCities;
    }

    //Traduce el resultado de valores entre [0..n-1] a sus valores originales. La traduccion es necessaria para assegurar el funcionamiento de la matriz sin restringir que los operadores sean entre 0 y n-1.
    private ArrayList <Integer> translateResult(int[] bstPoint)
    {
        ArrayList <Integer> result = new ArrayList <Integer>();
        for(int i = 0; i < n; i++)
        {
            result.add(graphdecrypt.get(bstPoint[i]));
        }
        return result;
    }

    //Creadora de la classe Hill Climbing. Assigna un valor a los identificadores de productos que se le da y escribe el coste de la arista en la matriz entre el par de productos que es el valor de similitudes.
    public HillClimbing(HashMap<Pair<Integer, Integer>, Float> hmap, int numVert)
    {
        n = numVert;
        graphencrypt = new HashMap<Integer,Integer>();
        graphdecrypt = new HashMap<Integer,Integer>();
        adjMatrix = new Float[n][n];
        hmap.forEach((ids,similitud) -> {
            if(!graphencrypt.containsKey(ids.getFirst())) {
                graphencrypt.put(ids.getFirst(), count);
                graphdecrypt.put(count, ids.getFirst());
                count++;
            }
            if(!graphencrypt.containsKey(ids.getSecond()))
            {
                graphencrypt.put(ids.getSecond(),count);
                graphdecrypt.put(count,ids.getSecond());
                count++;
            }
            adjMatrix[graphencrypt.get(ids.getFirst())][graphencrypt.get(ids.getSecond())] = similitud;
        });
    }

    //Funcion que ordena la estanteria.
    public ArrayList <Integer> ExHillClimbing() {
        boolean local; // Indica si se ha encontrado un maximo local y ya no se puede mejorar la solucion encontrada.
        //La lista de vecinos.
        LinkedList<int []> nghbrs = null;
        //El mejor becino encontrado.
        int [] bstNghbrPoint = null;
        //La mejor ordenacion encontrada.
        int [] bstPoint = null;
        //El mejor coste encontrado.
        Float bstcst = 0.0F;
        local = false;
        //La solucion inicial es la mejor solucion encontrada hasta este momento.
        bstPoint = InitialSolutionGenerator();

        //Busqueda para encontrar la mejor solucion de sus vecinos.
        while(!local) {
            //Se crea la lista de vecinos del bstPoint.
            nghbrs = NeighboringSolutions(bstPoint);
            //Se selecciona el primer vecino.
            bstNghbrPoint = nghbrs.pop();
            //Float que guarda el coste del mejor vecino.
            Float bstNghbrCst = Heuristic(bstNghbrPoint);
            //Buclo que busca el mejor vecino.
            while(!nghbrs.isEmpty()) {
                //Calcula el coste del siguiente vecino.
                int [] pointX = nghbrs.pop();
                Float pntXCst = Heuristic(pointX);

                //Comprara el mejor vecino con el nuevo y se queda con el de coste mas alto.
                if(pntXCst > bstNghbrCst) {
                    bstNghbrPoint = pointX;
                    bstNghbrCst = pntXCst;
                }

            }

            //Compara el mejor vecino con la mejor solucion encontrada hasta el momento. Si el mejor vecino tiene coste superior a la major solucion no es maximo local y se sigue iterando, else es bstPoint es maximo y se para la ejecucion.
            if(Heuristic(bstNghbrPoint) > Heuristic(bstPoint)) {
                bstPoint = bstNghbrPoint;
                bstcst = bstNghbrCst;
            } else {
                local = true;
            }
        }
        //Se traduce el bstPoint para que devuelva la ordenacion con los identificadores adecuados y los retorna.
        return translateResult(bstPoint);
    }
}