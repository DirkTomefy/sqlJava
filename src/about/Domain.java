package about;

import java.util.HashSet;
import java.util.Vector;

public class Domain {
    HashSet<DomainAtom> supports;
    HashSet<Object> allowedValue;
    public Domain() {
        this.supports=new HashSet<>();
    }   

    public Domain(HashSet<DomainAtom> supports) {
        this.supports = supports;
    }

    public void append(DomainAtom values) {
        this.supports.add(values);
    }
    public Domain toNolimit(){
        HashSet<DomainAtom> nolimit=new HashSet<>();
        for (DomainAtom object : supports) {
            nolimit.add(object.toNolimit());
        }
        return new Domain(nolimit);
    }
    public HashSet<DomainAtom> toNoListlimit(){
        HashSet<DomainAtom> nolimit=new HashSet<>();
        for (DomainAtom object : supports) {
            nolimit.add(object.toNolimit());
        }
        return nolimit;
    }
    public static Domain createNewDomain(Domain d1, Domain d2) {
        HashSet<DomainAtom> newSupports = new HashSet<>();
        newSupports.addAll(d1.supports);
        newSupports.addAll(d2.supports);
        return new Domain(newSupports);
    }

    public static Vector<Domain> createNewDomain(Vector<Domain> d1, Vector<Domain> d2) {
        Vector<Domain> result = new Vector<>();

        if (d1 == null && d2 == null)
            return result;
        if (d1 == null)
            return new Vector<>(d2);
        if (d2 == null)
            return new Vector<>(d1);

        int maxSize = Math.max(d1.size(), d2.size());

        for (int i = 0; i < maxSize; i++) {
            Domain domain1 = i < d1.size() ? d1.get(i) : null;
            Domain domain2 = i < d2.size() ? d2.get(i) : null;
            Domain combinedDomain = createNewDomain(domain1, domain2);
            result.add(combinedDomain);
        }

        return result;
    }

    public boolean isSupportable(Object a) {
        boolean value = false;
        for (DomainAtom domainAtom : supports) {
            if (domainAtom.isSupportable(a)) 
            {
                if(allowedValue!=null){
                    if(allowedValue.contains(a)){
                        return true;
                    }
                }else{
                    return true;
                }
            }
        }
        return value;
    }
    @Override
    public String toString() {
        if (supports == null || supports.isEmpty()) {
            return "Domain{EMPTY}";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Domain{");
        
        int count = 0;
        for (DomainAtom atom : supports) {
            if (count > 0) sb.append(", ");
            
            if (atom != null) {
                // Type principal
                sb.append(atom.getDef().getSimpleName());
                
                // Limite si présente
                if (atom.getLimit() != null) {
                    sb.append("(").append(atom.getLimit()).append(")");
                }
                
                // Valeurs autorisées si présentes
                // if (atom.getAllowedValue() != null && !atom.getAllowedValue().isEmpty()) {
                //     sb.append("[");
                //     int valueCount = 0;
                //     for (Object value : atom.getAllowedValue() ) {
                //         if (valueCount > 0) sb.append(",");
                //         sb.append(value);
                //         valueCount++;
                //         if (valueCount >= 3) { // Limite l'affichage à 3 valeurs
                //             sb.append(",...");
                //             break;
                //         }
                //     }
                //     sb.append("]");
                // }
            } else {
                sb.append("NULL");
            }
            count++;
        }
        sb.append("}");
        
        return sb.toString();
    }
    
}
