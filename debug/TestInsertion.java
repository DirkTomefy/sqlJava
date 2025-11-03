package debug;

import java.util.HashSet;

import about.Domain;
import about.DomainAtom;
import about.Individual;
import about.Relation;
import about.function.RelationManager;
import err.DomainOutOfBonds;
import err.DomainSupportErr;

public class TestInsertion {
    public static void main(String[] args) throws DomainOutOfBonds, DomainSupportErr {
        Domain d1 = new Domain();
        HashSet<Object> possibleValue=new HashSet<>();
        possibleValue.add(1);
        possibleValue.add(0);
        d1.append(new DomainAtom(Integer.class, possibleValue, null));
        d1.append(new DomainAtom(String.class, null, 50));
        
        Relation r=new Relation();
        r.setName("Test");
        r.setDomaines(RelationManager.createDomains(d1));
        r.setFieldName(RelationManager.createFields("id"));

        r.insertNewInd(new Individual(new Object[]{1}));
        
    }
}
