package about.domains;

import about.DomainAtom;

public class VARCHAR implements DomainAtom {
    Integer limit;

    @Override
    public boolean isSupportable(Object value) {
        if (value instanceof String s) {
            if (limit == null) {
                return true;
            } else {
                return s.length() <= limit;
            }
        } else {
            return false;
        }
    }

}
