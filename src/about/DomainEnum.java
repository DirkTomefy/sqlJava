package about;

import java.util.HashSet;

public class DomainEnum extends DomainAtom {
    public DomainEnum(Class<?> def) {
        super(def);
    }

    HashSet<Object> allowedValue;

}
