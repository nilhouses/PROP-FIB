package dominio.EjecutarAlgoritmo;
import dominio.Pair;

import java.util.*;

//Clase que busca el MST de un grafo añadiendo la mejor arista si esa no crea un ciclo en el grafo. Utiliza la clase WQuickUnion como Merge Find Set para una comprovacion de ciclos eficiente.
public class Kruskal {
    //ArrayList que contiene todas las aristas, su peso y sus vertices.
    private ArrayList <Pair<Float,Pair<Integer,Integer>>> relationgraph;

    //Retorna el arraylist Relationgraph. Utilitzat en tests.
    public ArrayList <Pair<Float,Pair<Integer,Integer>>> getRG()
    {
        return relationgraph;
    }


    //Creadora de Kruskal donde se traduce el Hashmap de los vertices y los pesos de las aristas a un arraylist.
    public Kruskal(HashMap<Pair<Integer, Integer>, Float> hmap)
    {
        relationgraph = new ArrayList <Pair<Float,Pair<Integer,Integer>>>();
        hmap.forEach((ids,similitud) -> {
            if(!relationgraph.contains( new Pair<>(similitud, new Pair<>(ids.getSecond(),ids.getFirst()))))
            {
                relationgraph.add(new Pair<>(similitud,new Pair<>(ids.getFirst(),ids.getSecond())));
            }
        });
    }

    //Calcula el Minimum Spanning Tree del grafo dado i retorna un arraylist ordenado de la mejor ordenación de productos.
    public ArrayList <Integer> MST(int n) {
        //Inicializa un objecto de WQUickUnion con la mida de productos a añadir.
        WQuickUnion uf = new WQuickUnion(n);

        //HashMap que contiene como clave un veritce i un arraylist de sus vecinos como elemento.
        HashMap<Integer,ArrayList<Integer>> curredge = new HashMap<Integer,ArrayList<Integer>>();

        //Sortea relationgraph en orden creciente de coste de aristas.
        relationgraph.sort(Comparator.comparing(p -> -p.getFirst()));
        //Invertimos el ArrayList porque queremos que sea decreciente.
        relationgraph.reversed();

        //Contador de numeros de aristas en el MST.
        final int[] numedge = {0};

        //Crea el Minimum Spanning Tree guardando las aristas de mayor peso en el Hashmap.
        relationgraph.forEach((element) -> {
            //Comprueba que la mida no sea maxima y que los vertices ya no esten conectados.
            if( numedge[0] < n && !uf.find(element.getSecond().getFirst(),element.getSecond().getSecond())) {
                boolean a = true;
                boolean b = true;

                //Comprueba que los vertices no se han añadido más de dos veces.
                if (curredge.containsKey(element.getSecond().getFirst()))
                {
                    if(curredge.get(element.getSecond().getFirst()).size() == 2)
                    {
                        a = false;
                    }
                }
                if (curredge.containsKey(element.getSecond().getSecond()))
                {
                    if(curredge.get(element.getSecond().getSecond()).size() == 2)
                    {
                        b = false;
                    }
                }

                //Añade la arista si es adecuada al MST.
                if((a && b))
                {
                    //Se ha añadido una arista, se incrementa el numero de aristas.
                    numedge[0]++;

                    //Añade los vertices al union para la proxima union find.
                    uf.union(element.getSecond().getFirst(),element.getSecond().getSecond());

                    //Si existe el veritce como key -> ya se ha añadido anteriormente, insertar vecino. Para el primer vertice.
                    if(curredge.containsKey(element.getSecond().getFirst()))
                    {
                        curredge.get(element.getSecond().getFirst()).add(element.getSecond().getSecond());
                    }
                    //Else, crear nuevo espacio e insertar key y vecino. Para el primer vertice.
                    else{
                        curredge.put(element.getSecond().getFirst(),new ArrayList<Integer> ());
                        curredge.get(element.getSecond().getFirst()).add(element.getSecond().getSecond());
                    }
                    //Si existe el veritce como key -> ya se ha añadido anteriormente, insertar vecino. Para el segundo vertice.
                    if(curredge.containsKey(element.getSecond().getSecond()))
                    {
                        curredge.get(element.getSecond().getSecond()).add(element.getSecond().getFirst());
                    }
                    //Else, crear nuevo espacio e insertar key y vecino. Para el segundo vertice.
                    else{
                        curredge.put(element.getSecond().getSecond(),new ArrayList<Integer> ());
                        curredge.get(element.getSecond().getSecond()).add(element.getSecond().getFirst());
                    }
                }
            }
        });
        //Encuentra uno de los dos vertices que estarian en la punta del Path para empezar a formar la estanteria ordenada.
        ArrayList <Integer> result = new ArrayList<Integer>();
        curredge.forEach((k,v) -> {
            if(v.size() == 1 && result.isEmpty())
            {
                result.add(k);
                result.add(v.getFirst());
            }
        });
        curredge.remove(result.getFirst());

        //Va insertando de los vertices para formar la estanteria ordenada.
        while(curredge.size() > 1)
        {
            int i = 0;
            if(Objects.equals(curredge.get(result.getLast()).get(i), result.get(result.size() - 2)))
            {
                i = 1;
            }
            Integer currnode = curredge.get(result.getLast()).get(i);
            curredge.remove(result.getLast());
            result.add(currnode);
        }
        return result;
    }

}