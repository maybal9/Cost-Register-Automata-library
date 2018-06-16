package helpers;

public class Pair<K,V> {

    protected K key;
    protected V value;

    public Pair(){
        this.key = null;
        this.value = null;
    }

    public Pair(K k, V v){
        this.key = k;
        this.value = v;
    }

    public Pair(Pair<K,V> p){
        this.key = p.getKey();
        this.value = p.getValue();
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public boolean equals(Pair other) {
        return (this.key == other.getKey() && this.value == other.getValue());
    }

}
