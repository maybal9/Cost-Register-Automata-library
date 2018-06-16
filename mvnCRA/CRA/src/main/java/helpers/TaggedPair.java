package helpers;

public class TaggedPair<K,V,T> extends Pair<K,V>{
    private T tag;

    public TaggedPair(Pair<K,V> otherPair ,T t){
        super(otherPair);
        this.tag = t;
    }

    public TaggedPair(K k, V v, T t){
        super(k,v);
        this.tag = t;
    }

    public T getTag(){
        return this.tag;
    }

    public Pair<K,V> getPair(){
        return new Pair<K,V>(super.getKey(),super.getValue());
    }


}
