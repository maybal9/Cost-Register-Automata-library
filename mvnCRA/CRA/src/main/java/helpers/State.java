package helpers;

public class State<T> {
    private T state;

    public State(T s){
        this.state = s;
    }

    public State(State<T> state) {
        this.state = state.getState();
    }


    public T getState() {
        return state;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof State && ((State)other).state.equals(this.getState()));
    }
}
