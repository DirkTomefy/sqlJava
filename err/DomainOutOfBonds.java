package err;

import about.Individual;
import about.Relation;

public class DomainOutOfBonds extends RelationalErr {
    public DomainOutOfBonds() {

    }

    public DomainOutOfBonds(String message) {
        super(message);
    }

    public DomainOutOfBonds(Throwable cause) {
        super(cause);
    }

    public DomainOutOfBonds(Individual ind, Relation rel) {
        super("Cannot add individual of fields_size=" + ind.getValues().size()
                + " in relation(" + rel.getName() + ") with domains_size " + rel.getDomaines().size());
    }
}
