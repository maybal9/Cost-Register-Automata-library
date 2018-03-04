public class helpFunc<T> {

    public T[] deepCopy(T[] a){
        T[] ans = (T[]) new Object[a.length];
        for(int i=0; i<a.length; i++){
            ans[i] = a[i];
        }
        return ans;
    }
}
