package dominio;

public class Pair<K, V> {
    private K first;
    private V second;

    public Pair() {
        this.first = null;
        this.second = null;
    }

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K setFirst(K first) {
        this.first = first;
        return this.first;
    }

    public V setSecond(V second) {
        this.second = second;
        return this.second;
    }

    public K getFirst() {
        return this.first;
    }

    public V getSecond() {
        return this.second;
    }

    public void printPair() {
        System.out.print("(");
        if (this.first == null) System.out.print(" ");
        else System.out.print(this.first);
        System.out.print(",");
        if (this.second == null) System.out.print(" ");
        else System.out.print(this.second);
        System.out.println(")");
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return first.equals(pair.first) && second.equals(pair.second);
    }

    public int hashCode() {
        return 31 * first.hashCode() + second.hashCode();
    }
}