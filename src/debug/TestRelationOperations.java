package debug;

import about.Domain;
import about.DomainEnum;
import about.Individual;
import about.Relation;
import about.domains.*;
import java.util.HashSet;
import java.util.Vector;

public class TestRelationOperations {
    
    public static void main(String[] args) {
        try {
            testProjection();
            testSelection();
            testUnion();
            testIntersection();
            testProduitCartesien();
            System.out.println("Tous les tests ont réussi !");
        } catch (Exception e) {
            System.out.println("Erreur lors des tests: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void testProjection() throws Exception {
        System.out.println("=== Test Projection ===");
        
        Relation employes = createEmployesRelation();
        System.out.println("Relation originale: " + employes.getName());
        System.out.println("Nombre d'individus: " + employes.getIndividus().size());
        
        Relation projection = employes.projection(new String[]{"nom", "salaire"});
        System.out.println("Projection (nom, salaire): " + projection.getName());
        
        // Vérification
        assert projection.getFieldName().size() == 2;
        assert projection.getFieldName().contains("nom");
        assert projection.getFieldName().contains("salaire");
        assert projection.getIndividus().size() == 3;
        
        System.out.println("✓ Test Projection réussi");
    }
    
    public static void testSelection() throws Exception {
        System.out.println("\n=== Test Selection ===");
        
        Relation employes = createEmployesRelation();
        
        // Sélection: salaire > 3000
        String selectionQuery="salaire > 3000";
        Relation selection = employes.selection(selectionQuery);
        System.out.println("Sélection ("+selectionQuery+"): " + selection.getName());
        
        // Vérification
        assert selection.getIndividus().size() == 2; // Alice et Charlie
        
        System.out.println("✓ Test Selection réussi");
    }
    
    public static void testUnion() throws Exception {
        System.out.println("\n=== Test Union ===");
        
        Relation relation1 = createRelation1();
        Relation relation2 = createRelation2();
        
        Relation union = Relation.union(relation1,relation2);
        System.out.println("Union: " + union.getName());
        
        // Vérification
        assert union.getIndividus().size() == 5; // 3 de relation1 + 2 de relation2
        
        System.out.println(""+union);
        System.out.println("✓ Test Union réussi");
    }
    
    public static void testIntersection() throws Exception {
        System.out.println("\n=== Test Intersection ===");
        
        Relation relation1 = createRelation1();
        Relation relation2 = createRelation3(); // Relation avec individus communs
        
        Relation intersection = Relation.intersection(relation1,relation2);
        System.out.println("Intersection: " + intersection.getName());
        
        // Vérification
        assert intersection.getIndividus().size() == 1; // Un individu commun
        System.out.println(""+intersection);
        System.out.println("✓ Test Intersection réussi");
    }
    
    public static void testProduitCartesien() throws Exception {
        System.out.println("\n=== Test Produit Cartésien ===");
        
        Relation relationA = createSmallRelationA();
        Relation relationB = createSmallRelationB();
        
        Relation produit = Relation.produitCartesien(relationA,relationB);
        System.out.println("Produit Cartésien: " + produit.getName());
        
        // Vérification
        assert produit.getFieldName().size() == 4; // 2 champs de A + 2 champs de B
        assert produit.getIndividus().size() == 6; // 2 individus A × 3 individus B
        
        System.out.println("✓ Test Produit Cartésien réussi");
    }
    
    // Méthodes utilitaires pour créer des relations de test
    
    private static Relation createEmployesRelation() {
        Relation employes = new Relation();
        employes.setName("Employes");
        
        // Configuration des noms de champs
        Vector<String> fieldNames = new Vector<>();
        fieldNames.add("nom");
        fieldNames.add("age");
        fieldNames.add("salaire");
        fieldNames.add("departement");
        employes.setFieldName(fieldNames);
        
        // Configuration des domaines
        Vector<Domain> domaines = new Vector<>();
        
        // Domaine pour nom: VARCHAR(50)
        Domain domaineNom = new Domain();
        VARCHAR varcharDomain = new VARCHAR();
        varcharDomain.setLimit(50);
        domaineNom.getSupports().add(varcharDomain);
        domaines.add(domaineNom);
        
        // Domaine pour age: NUMBER(18, 65)
        Domain domaineAge = new Domain();
        NUMBER ageDomain = new NUMBER();
        ageDomain.setMin(18);
        ageDomain.setMax(65);
        domaineAge.getSupports().add(ageDomain);
        domaines.add(domaineAge);
        
        // Domaine pour salaire: NUMBER(2000, 10000)
        Domain domaineSalaire = new Domain();
        NUMBER salaireDomain = new NUMBER();
        salaireDomain.setMin(2000);
        salaireDomain.setMax(10000);
        domaineSalaire.getSupports().add(salaireDomain);
        domaines.add(domaineSalaire);
        
        // Domaine pour département: ENUM
        Domain domaineDept = new Domain();
        HashSet<Object> depts = new HashSet<>();
        depts.add("IT");
        depts.add("RH");
        depts.add("Finance");
        domaineDept.getSupports().add(new DomainEnum(depts));
        domaines.add(domaineDept);
        
        employes.setDomaines(domaines);
        
        // Création des individus avec les bons types
        Vector<Individual> individus = new Vector<>();
        
        Individual indiv1 = new Individual();
        // Utilisation des bons types selon les domaines
        indiv1.getValues().add("Alice");        // VARCHAR
        indiv1.getValues().add(30);             // NUMBER (age)
        indiv1.getValues().add(4000.0);         // NUMBER (salaire)
        indiv1.getValues().add("IT");           // ENUM
        individus.add(indiv1);
        
        Individual indiv2 = new Individual();
        indiv2.getValues().add("Bob");
        indiv2.getValues().add(25);
        indiv2.getValues().add(2800.0);
        indiv2.getValues().add("RH");
        individus.add(indiv2);
        
        Individual indiv3 = new Individual();
        indiv3.getValues().add("Charlie");
        indiv3.getValues().add(35);
        indiv3.getValues().add(5000.0);
        indiv3.getValues().add("Finance");
        individus.add(indiv3);
        
        employes.setIndividus(individus);
        
        return employes;
    }
    
    private static Relation createRelation1() {
        Relation relation = new Relation();
        relation.setName("Relation1");
        
        Vector<String> fieldNames = new Vector<>();
        fieldNames.add("id");
        fieldNames.add("valeur");
        relation.setFieldName(fieldNames);
        
        Vector<Domain> domaines = new Vector<>();
        
        // Domaine pour id: NUMBER(1, 10)
        Domain domaineId = new Domain();
        NUMBER idDomain = new NUMBER();
        idDomain.setMin(1);
        idDomain.setMax(10);
        domaineId.getSupports().add(idDomain);
        domaines.add(domaineId);
        
        // Domaine pour valeur: VARCHAR(20)
        Domain domaineValeur = new Domain();
        VARCHAR varcharDomain = new VARCHAR();
        varcharDomain.setLimit(20);
        domaineValeur.getSupports().add(varcharDomain);
        domaines.add(domaineValeur);
        
        relation.setDomaines(domaines);
        
        Vector<Individual> individus = new Vector<>();
        
        Individual indiv1 = new Individual();
        indiv1.getValues().add(1);      // NUMBER
        indiv1.getValues().add("A");    // VARCHAR
        individus.add(indiv1);
        
        Individual indiv2 = new Individual();
        indiv2.getValues().add(2);
        indiv2.getValues().add("B");
        individus.add(indiv2);
        
        Individual indiv3 = new Individual();
        indiv3.getValues().add(3);
        indiv3.getValues().add("C");
        individus.add(indiv3);
        
        relation.setIndividus(individus);
        
        return relation;
    }
    
    private static Relation createRelation2() {
        Relation relation = new Relation();
        relation.setName("Relation2");
        
        Vector<String> fieldNames = new Vector<>();
        fieldNames.add("id");
        fieldNames.add("valeur");
        relation.setFieldName(fieldNames);
        
        Vector<Domain> domaines = new Vector<>();
        
        Domain domaineId = new Domain();
        NUMBER idDomain = new NUMBER();
        idDomain.setMin(1);
        idDomain.setMax(10);
        domaineId.getSupports().add(idDomain);
        domaines.add(domaineId);
        
        Domain domaineValeur = new Domain();
        VARCHAR varcharDomain = new VARCHAR();
        varcharDomain.setLimit(20);
        domaineValeur.getSupports().add(varcharDomain);
        domaines.add(domaineValeur);
        
        relation.setDomaines(domaines);
        
        Vector<Individual> individus = new Vector<>();
        
        Individual indiv1 = new Individual();
        indiv1.getValues().add(4);      // NUMBER
        indiv1.getValues().add("D");    // VARCHAR
        individus.add(indiv1);
        
        Individual indiv2 = new Individual();
        indiv2.getValues().add(5);
        indiv2.getValues().add("E");
        individus.add(indiv2);
        
        relation.setIndividus(individus);
        return relation;
    }
    
    private static Relation createRelation3() {
        Relation relation = new Relation();
        relation.setName("Relation3");
        
        Vector<String> fieldNames = new Vector<>();
        fieldNames.add("id");
        fieldNames.add("valeur");
        relation.setFieldName(fieldNames);
        
        Vector<Domain> domaines = new Vector<>();
        
        Domain domaineId = new Domain();
        NUMBER idDomain = new NUMBER();
        idDomain.setMin(1);
        idDomain.setMax(10);
        domaineId.getSupports().add(idDomain);
        domaines.add(domaineId);
        
        Domain domaineValeur = new Domain();
        VARCHAR varcharDomain = new VARCHAR();
        varcharDomain.setLimit(20);
        domaineValeur.getSupports().add(varcharDomain);
        domaines.add(domaineValeur);
        
        relation.setDomaines(domaines);
        
        Vector<Individual> individus = new Vector<>();
        
        Individual indiv1 = new Individual();
        indiv1.getValues().add(2);      // Commun avec Relation1
        indiv1.getValues().add("B");
        individus.add(indiv1);
        
        Individual indiv2 = new Individual();
        indiv2.getValues().add(6);
        indiv2.getValues().add("F");
        individus.add(indiv2);
        
        relation.setIndividus(individus);
        
        return relation;
    }
    
    private static Relation createSmallRelationA() {
        Relation relation = new Relation();
        relation.setName("A");
        
        Vector<String> fieldNames = new Vector<>();
        fieldNames.add("idA");
        fieldNames.add("nomA");
        relation.setFieldName(fieldNames);
        
        Vector<Domain> domaines = new Vector<>();
        
        // Domaine pour idA: NUMBER
        Domain domaineIdA = new Domain();
        domaineIdA.getSupports().add(new NUMBER());
        domaines.add(domaineIdA);
        
        // Domaine pour nomA: VARCHAR
        Domain domaineNomA = new Domain();
        domaineNomA.getSupports().add(new VARCHAR());
        domaines.add(domaineNomA);
        
        relation.setDomaines(domaines);
        
        Vector<Individual> individus = new Vector<>();
        
        Individual indiv1 = new Individual();
        indiv1.getValues().add(1);      // NUMBER
        indiv1.getValues().add("X");    // VARCHAR
        individus.add(indiv1);
        
        Individual indiv2 = new Individual();
        indiv2.getValues().add(2);
        indiv2.getValues().add("Y");
        individus.add(indiv2);
        
        relation.setIndividus(individus);
        
        return relation;
    }
    
    private static Relation createSmallRelationB() {
        Relation relation = new Relation();
        relation.setName("B");
        
        Vector<String> fieldNames = new Vector<>();
        fieldNames.add("idB");
        fieldNames.add("nomB");
        relation.setFieldName(fieldNames);
        
        Vector<Domain> domaines = new Vector<>();
        
        // Domaine pour idB: NUMBER
        Domain domaineIdB = new Domain();
        domaineIdB.getSupports().add(new NUMBER());
        domaines.add(domaineIdB);
        
        // Domaine pour nomB: VARCHAR
        Domain domaineNomB = new Domain();
        domaineNomB.getSupports().add(new VARCHAR());
        domaines.add(domaineNomB);
        
        relation.setDomaines(domaines);
        
        Vector<Individual> individus = new Vector<>();
        
        Individual indiv1 = new Individual();
        indiv1.getValues().add(10);     // NUMBER
        indiv1.getValues().add("P");    // VARCHAR
        individus.add(indiv1);
        
        Individual indiv2 = new Individual();
        indiv2.getValues().add(20);
        indiv2.getValues().add("Q");
        individus.add(indiv2);
        
        Individual indiv3 = new Individual();
        indiv3.getValues().add(30);
        indiv3.getValues().add("R");
        individus.add(indiv3);
        
        relation.setIndividus(individus);
        
        return relation;
    }
}