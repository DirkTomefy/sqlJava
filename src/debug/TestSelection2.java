package debug;

import java.util.Vector;

import SelectAST.err.EvalErr;
import SelectAST.err.ParseNomException;
import about.Domain;
import about.Individual;
import about.Relation;
import about.domains.NUMBER;
import about.domains.VARCHAR;
import err.DomainOutOfBonds;
import err.DomainSupportErr;

public class TestSelection2 {
    public static void main(String[] args) {
        Vector<Domain> domains = new Vector<>();
        domains.add((new VARCHAR(false, 200)).intoDomain());
        domains.add((new NUMBER(false, 1, 100)).intoDomain());
        domains.add((new VARCHAR(false, 200)).intoDomain());

        Vector<String> fieldNames = new Vector<>();
        fieldNames.add("nom");
        fieldNames.add("age");
        fieldNames.add("ville");

        Vector<Individual> individus = new Vector<>();

        // Individu 1
        Vector<Object> values1 = new Vector<>();
        values1.add("Alice");
        values1.add(25);
        values1.add("Paris");
        individus.add(new Individual(values1));

        // Individu 2
        Vector<Object> values2 = new Vector<>();
        values2.add("Bob");
        values2.add(30);
        values2.add("Lyon");
        individus.add(new Individual(values2));

        // Individu 3 (doublon de Bob pour tester l'élimination des doublons)
        Vector<Object> values3 = new Vector<>();
        values3.add("Bob");
        values3.add(30);
        values3.add("Lyon");
        individus.add(new Individual(values3));

        // Individu 4
        Vector<Object> values4 = new Vector<>();
        values4.add("Charlie");
        values4.add(35);
        values4.add("Paris");
        individus.add(new Individual(values4));

        // Individu 5
        Vector<Object> values5 = new Vector<>();
        values5.add("Diana");
        values5.add(28);
        values5.add("Marseille");
        individus.add(new Individual(values5));

        // Individu 6
        Vector<Object> values6 = new Vector<>();
        values6.add("Marseille");
        values6.add(18);
        values6.add("Marseille");
        individus.add(new Individual(values6));

        Relation relation = new Relation("Personnes", fieldNames, domains, individus);
        System.out.println("" + relation.toString());
        try {
            String condition="(nom=ville) AND (nom='Marseille' or 'Marseille'=ville) and (((age=18 and 4=1*3+1) and age<=18)) and -1=0.0+(-1)";
           //String condition="!(nom=ville)";
            System.out.println("" + relation.selection(
                    condition));
        } catch (ParseNomException e) {
            e.printStackTrace();
        } catch (EvalErr e) {
            e.printStackTrace();
        }
        try {
            Vector<Object> newIndValue = new Vector<>();
            newIndValue.add("Marseille");
            newIndValue.add(10);
            newIndValue.add("Marseille");
            relation.insertNewInd(newIndValue);
           // System.out.println(""+relation);
        } catch (DomainOutOfBonds | DomainSupportErr e) {
            e.printStackTrace();
        }
    }
}
