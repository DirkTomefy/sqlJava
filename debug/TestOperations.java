package debug;
import about.Domain;
import about.DomainAtom;
import about.Relation;
import about.function.RelationManager;

public class TestOperations {
    public static void main(String[] args) {
        /*
         * DOMAINES DE BASE
         */
        Domain INTEGER = new Domain();
        INTEGER.append(new DomainAtom(Integer.class, null, null));

        Domain VARCHAR = new Domain();
        VARCHAR.append(new DomainAtom(String.class, null, 50));

        Domain BOOLEAN = new Domain();
        BOOLEAN.append(new DomainAtom(Boolean.class, null, null));

        /*
         * RELATIONS POUR UNION (mêmes domaines)
         */
        Relation etudiants_Info = new Relation();
        etudiants_Info.setName("ETUDIANTS_INFO");
        etudiants_Info.setFieldName(RelationManager.createFields("id", "nom", "prenom"));
        etudiants_Info.setDomaines(RelationManager.createDomains(INTEGER, VARCHAR, VARCHAR));
        etudiants_Info.setIndividus(RelationManager.createIndividuals(
                new Object[] { 1, "Dupont", "Jean" },
                new Object[] { 2, "Martin", "Marie" },
                new Object[] { 3, "Bernard", "Pierre" }));

        Relation etudiants_Math = new Relation();
        etudiants_Math.setName("ETUDIANTS_MATH");
        etudiants_Math.setFieldName(RelationManager.createFields("id", "nom", "prenom"));
        etudiants_Math.setDomaines(RelationManager.createDomains(INTEGER, VARCHAR, VARCHAR));
        etudiants_Math.setIndividus(RelationManager.createIndividuals(
                new Object[] { 2, "Martin", "Marie" },
                new Object[] { 4, "Dubois", "Sophie" },
                new Object[] { 5, "Moreau", "Luc" }));

        /*
         * RELATIONS POUR DIFFÉRENCE
         */
        Relation produits_Stock = new Relation();
        produits_Stock.setName("PRODUITS_STOCK");
        produits_Stock.setFieldName(RelationManager.createFields("code", "libelle", "quantite"));
        produits_Stock.setDomaines(RelationManager.createDomains(INTEGER, VARCHAR, INTEGER));
        produits_Stock.setIndividus(RelationManager.createIndividuals(
                new Object[] { 101, "Ordinateur", 5 },
                new Object[] { 102, "Souris", 10 },
                new Object[] { 103, "Clavier", 8 },
                new Object[] { 104, "Ecran", 3 }));

        Relation produits_Vendus = new Relation();
        produits_Vendus.setName("PRODUITS_VENDUS");
        produits_Vendus.setFieldName(RelationManager.createFields("code", "libelle", "quantite"));
        produits_Vendus.setDomaines(RelationManager.createDomains(INTEGER, VARCHAR, INTEGER));
        produits_Vendus.setIndividus(RelationManager.createIndividuals(
                new Object[] { 102, "Souris", 10 },
                new Object[] { 103, "Clavier", 8 },
                new Object[] { 105, "Imprimante", 2 }));

        /*
         * RELATIONS POUR PRODUIT CARTÉSIEN (domaines différents)
         */
        Relation clients = new Relation();
        clients.setName("CLIENTS");
        clients.setFieldName(RelationManager.createFields("client_id", "nom_client"));
        clients.setDomaines(RelationManager.createDomains(INTEGER, VARCHAR));
        clients.setIndividus(RelationManager.createIndividuals(
                new Object[] { 1, "Entreprise A" },
                new Object[] { 2, "Entreprise B" }));

        Relation produits = new Relation();
        produits.setName("PRODUITS");
        produits.setFieldName(RelationManager.createFields("produit_id", "nom_produit"));
        produits.setDomaines(RelationManager.createDomains( VARCHAR,INTEGER));
        produits.setIndividus(RelationManager.createIndividuals(
                new Object[] {  "Service Cloud",101 },
                new Object[] {  "Maintenance" ,102 }));

        try {
            // TEST UNION
            System.out.println("1. UNION des étudiants:");
            Relation unionEtudiants = Relation.union(etudiants_Info, etudiants_Math);
            System.out.println(unionEtudiants.toSimpleString());
            System.out.println(unionEtudiants.toString());
            System.out.println("Résultat attendu: 5 individus (sans doublons)\n");

            // TEST DIFFÉRENCE
            System.out.println("2. DIFFÉRENCE (produits en stock - produits vendus):");
            Relation differenceProduits = Relation.difference(produits_Stock, produits_Vendus);
            System.out.println(differenceProduits.toSimpleString());
            System.out.println(differenceProduits.toString());
            System.out.println("Résultat attendu: 2 individus (101, 104)\n");

            // TEST PRODUIT CARTÉSIEN
            System.out.println("3. PRODUIT CARTÉSIEN clients × produits:");
            Relation produitCartesien = Relation.union(clients, produits);
            System.out.println(produitCartesien.toSimpleString());
            System.out.println(produitCartesien.toString());
            System.out.println("Résultat attendu: 4 individus (2 clients × 2 produits)\n");

        } catch (Exception e) {
            System.out.println("Erreur lors des tests: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthodes utilitaires pour créer les données plus facilement
  
}