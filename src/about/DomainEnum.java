package about;

import java.util.HashSet;

public class DomainEnum implements DomainAtom {
    HashSet<Object> allowedValue;

    public DomainEnum(HashSet<Object> allowedValue) {
        this.allowedValue = allowedValue;
    }

    @Override
    public boolean isSupportable(Object value) {
        return this.allowedValue.contains(value);
    }
}
