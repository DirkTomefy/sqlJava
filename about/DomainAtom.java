package about;

import java.util.HashSet;
import java.util.Set;

public class DomainAtom {
    private Class<?> def;
    // private Set<Object> allowedValue;
    private Integer limit;

    // constructors
    public DomainAtom(Class<?> def) {
        this.def = def;
    }

    public DomainAtom(Class<?> def, Integer limit) {
        this.def = def;

        this.limit = limit;
    }


    // getters and setters
    public Class<?> getDef() {
        return def;
    }

    public void setDef(Class<?> def) {
        this.def = def;
    }

    public Integer getLimit() {
        return limit;
    }

  
    public boolean isSupportable(Object value) {        
        if (limit != null && value instanceof String) {
            String str = (String) value;
            if (str.length() > limit) {
                return false;
            }
        }

        if (Number.class.isAssignableFrom(value.getClass())) {
            Number a = (Number) value;
            if (a.intValue() > limit)
                return false;
        }

        if (!def.isAssignableFrom(value.getClass())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object value) {
        if (!(value instanceof DomainAtom)) {
            return false;
        } else {
            DomainAtom da = (DomainAtom) value;
            if (this.def.equals(da.def)) {
                return true;
            } else {
                return false;
            }
        }

    }

    public DomainAtom toNolimit(){
        return new DomainAtom(def , null);
    }
}
