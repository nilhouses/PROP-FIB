package dominio.EjecutarAlgoritmo;

import java.util.HashMap;

class WQuickUnion {
    private int[] id; // id[i] representa el padre del elemento al indice i.
    private int[] size; //size[i] representa el numero de nodos que contiene el elemento a ese indice.

    //Hashmap que contiene como key el identificador del producto y como valor su posicion en id y size.
    private HashMap<Integer,Integer> pointto;

    //Contiene la primera posición libre en id y size.
    private int count = 0;

    //-----------------------------------------------------------------------------------------------

    //Constructora que inicializa id, size y pointto con la cantidad de productos hay.
    public WQuickUnion(int n) {
        id = new int[n];
        size = new int[n];
        pointto = new HashMap<Integer,Integer>();
        for (int i = 0; i < n; i++) {
            id[i] = i;
            size[i] = 1;
        }
    }

    //Si existen los dos nodos indicados devuleve si estan conectados. Esta funcion comprueba si al añadir los dos nodos se crearia un ciclo en el grafo comparando los dos padres que devuelve la funcion root. Si son el mismo hay ciclo.
    public boolean find(int p, int q) {
        if(pointto.containsKey(p) && pointto.containsKey(q))
        {
            return root(pointto.get(p)) == root(pointto.get(q));
        }
        return false;
    }

    //Funcion que une los dos nodos indicados para que se indique que estan conectados en la siguiente llamada de find.
    public void union(int p, int q) {

        if(!pointto.containsKey(p))
        {
            pointto.put(p,count);
            count++;
        }
        if(!pointto.containsKey(q))
        {
            pointto.put(q,count);
            count++;
        }
        p = pointto.get(p);
        q = pointto.get(q);

        int i = root(p);
        int j = root(q);
        if (size[i] < size[j]) {
            id[i] = j;
            size[j] += size[i];
        } else {
            id[j] = i;
            size[i] += size[j];
        }

    }

    //Funcion que encuentra el padre del nodo indicado.
    private int root(int p) {
        while(p != id[p]) {
            id[p] = id[id[p]]; //Compresion del camino. Hace que el arbol sea lo mas plano possible.
            p = id[p];
        }
        return p;
    }
}
