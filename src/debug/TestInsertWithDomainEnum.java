package debug;

import about.Domain;
import about.DomainAtom;
import about.DomainEnum;
import about.Individual;
import about.Relation;
import err.DomainSupportErr;

import java.util.HashSet;
import java.util.Vector;

public class TestInsertWithDomainEnum {
    
    public static void main(String[] args) {
        try {
            testDomainEnumWithMixedTypes();
            testDomainEnumWithStringsOnly();
            testDomainEnumWithNumbersOnly();
            testDomainEnumWithRejections();
            System.out.println("✓ Tous les tests ont réussi !");
        } catch (Exception e) {
            System.out.println("✗ Erreur lors des tests: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void testDomainEnumWithMixedTypes() throws Exception {
        System.out.println("=== Test DomainEnum avec types mixtes ===");
        
        HashSet<Object> mixedValues = new HashSet<>();
        mixedValues.add("admin");
        mixedValues.add("user");
        mixedValues.add(1);      // Integer
        mixedValues.add(2.5);    // Double
        mixedValues.add(100L);   // Long
        
        DomainEnum mixedDomain = new DomainEnum(mixedValues);
        Relation relation = createSimpleRelation("MixedTypes", mixedDomain);
        
        // Test d'insertion de valeurs valides
        Individual ind1 = new Individual();
        ind1.getValues().add("admin");
        relation.insertNewInd(ind1);
        
        Individual ind2 = new Individual();
        ind2.getValues().add(1);
        relation.insertNewInd(ind2);
        
        Individual ind3 = new Individual();
        ind3.getValues().add(2.5);
        relation.insertNewInd(ind3);
        
        // Test d'insertion de valeur NON valide (doit être rejetée)
        try {
            Individual ind4 = new Individual();
            ind4.getValues().add("guest"); // "guest" n'est pas dans les valeurs autorisées
            relation.insertNewInd(ind4);
            System.out.println("✗ ERREUR: 'guest' aurait dû être rejeté !");
        } catch (DomainSupportErr e) {
            System.out.println("✓ 'guest' correctement rejeté: " + e.getMessage());
        }
        
        System.out.println("✓ DomainEnum mixte - Insertions valides: " + relation.getIndividus().size());
    }
    
    public static void testDomainEnumWithStringsOnly() throws Exception {
        System.out.println("\n=== Test DomainEnum avec strings seulement ===");
        
        HashSet<Object> stringValues = new HashSet<>();
        stringValues.add("red");
        stringValues.add("green");
        stringValues.add("blue");
        
        DomainEnum stringDomain = new DomainEnum(stringValues);
        Relation relation = createSimpleRelation("Colors", stringDomain);
        
        // Insertions valides
        Individual ind1 = new Individual();
        ind1.getValues().add("red");
        relation.insertNewInd(ind1);
        
        Individual ind2 = new Individual();
        ind2.getValues().add("blue");
        relation.insertNewInd(ind2);
        
        // Test d'insertion invalide - nombre au lieu de string
        try {
            Individual ind3 = new Individual();
            ind3.getValues().add(123); // Nombre au lieu de string
            relation.insertNewInd(ind3);
            System.out.println("✗ ERREUR: 123 (nombre) aurait dû être rejeté !");
        } catch (DomainSupportErr e) {
            System.out.println("✓ 123 correctement rejeté: " + e.getMessage());
        }
        
        // Test d'insertion invalide - string non autorisée
        try {
            Individual ind4 = new Individual();
            ind4.getValues().add("yellow"); // "yellow" n'est pas dans la liste
            relation.insertNewInd(ind4);
            System.out.println("✗ ERREUR: 'yellow' aurait dû être rejeté !");
        } catch (DomainSupportErr e) {
            System.out.println("✓ 'yellow' correctement rejeté: " + e.getMessage());
        }
        
        System.out.println("✓ DomainEnum strings - Insertions valides: " + relation.getIndividus().size());
    }
    
    public static void testDomainEnumWithNumbersOnly() throws Exception {
        System.out.println("\n=== Test DomainEnum avec nombres seulement ===");
        
        HashSet<Object> numberValues = new HashSet<>();
        numberValues.add(1);
        numberValues.add(2);
        numberValues.add(3);
        numberValues.add(10.5);
        numberValues.add(20.75);
        
        DomainEnum numberDomain = new DomainEnum(numberValues);
        Relation relation = createSimpleRelation("Numbers", numberDomain);
        
        // Insertions valides
        Individual ind1 = new Individual();
        ind1.getValues().add(1);
        relation.insertNewInd(ind1);
        
        Individual ind2 = new Individual();
        ind2.getValues().add(10.5);
        relation.insertNewInd(ind2);
        
        // Test d'insertion invalide - nombre non autorisé
        try {
            Individual ind3 = new Individual();
            ind3.getValues().add(4); // 4 n'est pas dans la liste
            relation.insertNewInd(ind3);
            System.out.println("✗ ERREUR: 4 aurait dû être rejeté !");
        } catch (DomainSupportErr e) {
            System.out.println("✓ 4 correctement rejeté: " + e.getMessage());
        }
        
        // Test d'insertion invalide - string au lieu de nombre
        try {
            Individual ind4 = new Individual();
            ind4.getValues().add("five"); // String au lieu de nombre
            relation.insertNewInd(ind4);
            System.out.println("✗ ERREUR: 'five' aurait dû être rejeté !");
        } catch (DomainSupportErr e) {
            System.out.println("✓ 'five' correctement rejeté: " + e.getMessage());
        }
        
        System.out.println("✓ DomainEnum nombres - Insertions valides: " + relation.getIndividus().size());
    }
    
    public static void testDomainEnumWithRejections() throws Exception {
        System.out.println("\n=== Test rejets spécifiques ===");
        
        HashSet<Object> specificValues = new HashSet<>();
        specificValues.add("active");
        specificValues.add("inactive");
        specificValues.add(0);
        specificValues.add(1);
        specificValues.add(true);
        specificValues.add(false);
        
        DomainEnum specificDomain = new DomainEnum(specificValues);
        Relation relation = createSimpleRelation("Specific", specificDomain);
        
        // Insertions valides
        Individual ind1 = new Individual();
        ind1.getValues().add("active");
        relation.insertNewInd(ind1);
        
        Individual ind2 = new Individual();
        ind2.getValues().add(true);
        relation.insertNewInd(ind2);
        
        Individual ind3 = new Individual();
        ind3.getValues().add(0);
        relation.insertNewInd(ind3);
        
        // Test de rejets variés
        Object[] invalidValues = {"pending", 2, 1.5, "TRUE", null, new Object()};
        
        for (Object invalidValue : invalidValues) {
            try {
                Individual ind = new Individual();
                ind.getValues().add(invalidValue);
                relation.insertNewInd(ind);
                System.out.println("✗ ERREUR: " + invalidValue + " aurait dû être rejeté !");
            } catch (DomainSupportErr e) {
                System.out.println("✓ " + invalidValue + " correctement rejeté");
            }
        }
        
        System.out.println("✓ Rejets spécifiques - Insertions valides: " + relation.getIndividus().size());
    }
    
    // Méthode utilitaire pour créer une relation simple
    private static Relation createSimpleRelation(String name, DomainAtom domainAtom) {
        Domain domain = new Domain();
        domain.getSupports().add(domainAtom);
        
        Relation relation = new Relation();
        relation.setName(name);
        
        Vector<String> fieldNames = new Vector<>();
        fieldNames.add("value");
        relation.setFieldName(fieldNames);
        
        Vector<Domain> domaines = new Vector<>();
        domaines.add(domain);
        relation.setDomaines(domaines);
        relation.setIndividus(new Vector<>());
        
        return relation;
    }
}