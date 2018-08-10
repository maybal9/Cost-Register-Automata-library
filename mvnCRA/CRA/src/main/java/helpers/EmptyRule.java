package helpers;

public class EmptyRule<T> extends Rule<T> {

    private T eta;

    public EmptyRule(Integer regdest, T eta){
        super(regdest,eta);
        super.isEmptyRule = true;
        this.eta = eta;
    }

    public T getEta() {
        return eta;
    }
}
