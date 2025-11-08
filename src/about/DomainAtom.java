package about;


public abstract class DomainAtom {

    boolean canBenull=false;
    public abstract boolean isSupportable(Object value);
    public void setCanBenull(boolean canBenull) {
        this.canBenull = canBenull;
    }
}
