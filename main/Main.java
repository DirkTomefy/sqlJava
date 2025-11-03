package main;

import java.util.Date;
import java.util.Vector;
import about.Domain;
import about.DomainAtom;
import about.Individual;
import about.Relation;

public class Main {
    public static void main(String[] args) {
        // Création des domaines de base
        Domain VARCHAR = new Domain();
        VARCHAR.append(new DomainAtom(String.class, null, 255)); // String avec limite 255
        
        Domain INTEGER = new Domain();
        INTEGER.append(new DomainAtom(Integer.class, null, null)); // Integer sans limite
        
        Domain DATE = new Domain();
        DATE.append(new DomainAtom(Date.class, null, null)); // Date sans limite
        
        Domain BOOLEAN = new Domain();
        BOOLEAN.append(new DomainAtom(Boolean.class, null, null)); // Boolean
        
        // Domaines avec valeurs spécifiques
        Domain GENDER = new Domain();
        DomainAtom genderAtom = new DomainAtom (String.class, null, 10);
        genderAtom.appendAllowedValue("M");
        genderAtom.appendAllowedValue("F");
        genderAtom.appendAllowedValue("OTHER");
        GENDER.append(genderAtom);
        
        Domain STATUS = new Domain();
        DomainAtom statusAtom = new DomainAtom (String.class, null, 20);
        statusAtom.appendAllowedValue("ACTIVE");
        statusAtom.appendAllowedValue("INACTIVE");
        statusAtom.appendAllowedValue("PENDING");
        statusAtom.appendAllowedValue("DELETED");
        STATUS.append(statusAtom);
        
        Domain PRIORITY = new Domain();
        DomainAtom priorityAtom = new DomainAtom (Integer.class, null, null);
        priorityAtom.appendAllowedValue(1);
        priorityAtom.appendAllowedValue(2);
        priorityAtom.appendAllowedValue(3);
        priorityAtom.appendAllowedValue(4);
        priorityAtom.appendAllowedValue(5);
        PRIORITY.append(priorityAtom);

        /*
         * CRÉATION DE RELATIONS (TABLES)
         */
        
        // Relation USERS
        Relation users = new Relation();
        users.setName("USERS");
        
        Vector<String> userFields = new Vector<>();
        userFields.add("id");
        userFields.add("username");
        userFields.add("email");
        userFields.add("birth_date");
        userFields.add("gender");
        userFields.add("status");
        users.setFieldName(userFields);
        
        Vector<Domain> userDomains = new Vector<>();
        userDomains.add(INTEGER);    // id
        userDomains.add(VARCHAR);    // username
        userDomains.add(VARCHAR);    // email
        userDomains.add(DATE);       // birth_date
        userDomains.add(GENDER);     // gender
        userDomains.add(STATUS);     // status
        users.setDomaines(userDomains);
        
        // Ajout d'individus (lignes) à USERS
        Vector<Individual> userIndividuals = new Vector<>();
        userIndividuals.add(new Individual(new Object[]{1, "john_doe", "john@example.com", new Date(), "M", "ACTIVE"}));
        userIndividuals.add(new Individual(new Object[]{2, "jane_smith", "jane@example.com", new Date(), "F", "ACTIVE"}));
        userIndividuals.add(new Individual(new Object[]{3, "bob_wilson", "bob@example.com", new Date(), "M", "INACTIVE"}));
        users.setIndividus(userIndividuals);
        
        // Relation PRODUCTS
        Relation products = new Relation();
        products.setName("PRODUCTS");
        
        Vector<String> productFields = new Vector<>();
        productFields.add("product_id");
        productFields.add("name");
        productFields.add("price");
        productFields.add("in_stock");
        productFields.add("priority");
        products.setFieldName(productFields);
        
        Vector<Domain> productDomains = new Vector<>();
        productDomains.add(INTEGER);    // product_id
        productDomains.add(VARCHAR);    // name
        productDomains.add(INTEGER);    // price
        productDomains.add(BOOLEAN);    // in_stock
        productDomains.add(PRIORITY);   // priority
        products.setDomaines(productDomains);
        
        // Ajout d'individus à PRODUCTS
        Vector<Individual> productIndividuals = new Vector<>();
        productIndividuals.add(new Individual(new Object[]{101, "Laptop", 999, true, 1}));
        productIndividuals.add(new Individual(new Object[]{102, "Mouse", 25, false, 3}));
        productIndividuals.add(new Individual(new Object[]{103, "Keyboard", 75, true, 2}));
        products.setIndividus(productIndividuals);
        
        // Relation ORDERS
        Relation orders = new Relation();
        orders.setName("ORDERS");
        
        Vector<String> orderFields = new Vector<>();
        orderFields.add("order_id");
        orderFields.add("user_id");
        orderFields.add("product_id");
        orderFields.add("quantity");
        orderFields.add("order_date");
        orderFields.add("status");
        orders.setFieldName(orderFields);
        
        Vector<Domain> orderDomains = new Vector<>();
        orderDomains.add(INTEGER);    // order_id
        orderDomains.add(INTEGER);    // user_id
        orderDomains.add(INTEGER);    // product_id
        orderDomains.add(INTEGER);    // quantity
        orderDomains.add(DATE);       // order_date
        orderDomains.add(STATUS);     // status
        orders.setDomaines(orderDomains);
        
        // Ajout d'individus à ORDERS
        Vector<Individual> orderIndividuals = new Vector<>();
        orderIndividuals.add(new Individual(new Object[]{1001, 1, 101, 1, new Date(), "ACTIVE"}));
        orderIndividuals.add(new Individual(new Object[]{1002, 2, 103, 2, new Date(), "PENDING"}));
        orderIndividuals.add(new Individual(new Object[]{1003, 1, 102, 1, new Date(), "DELETED"}));
        orders.setIndividus(orderIndividuals);

        /*
         * TESTS DE VALIDATION
         */
        System.out.println("=== TESTS DE VALIDATION ===");
        
        // Test validation des domaines
        System.out.println("Email valide: " + VARCHAR.isSupportable("test@example.com"));
        System.out.println("Email trop long: " + VARCHAR.isSupportable("a".repeat(256)));
        System.out.println("Genre valide: " + GENDER.isSupportable("M"));
        System.out.println("Genre invalide: " + GENDER.isSupportable("X"));
        System.out.println("Priorité valide: " + PRIORITY.isSupportable(3));
        System.out.println("Priorité invalide: " + PRIORITY.isSupportable(6));
        
        // Test validation des individus
        Individual validUser = new Individual(new Object[]{4, "new_user", "new@example.com", new Date(), "F", "PENDING"});
        Individual invalidUser = new Individual(new Object[]{5, "user", "invalid_email", new Date(), "X", "UNKNOWN"});
        
        // System.out.println("Utilisateur valide: " + users.isValidIndividual(validUser));
        // System.out.println("Utilisateur invalide: " + users.isValidIndividual(invalidUser));

        /*
         * AFFICHAGE DES RELATIONS
         */
        System.out.println("\n=== RELATIONS CRÉÉES ===");
        System.out.println("Relation: " + users.getName() + " avec " + users.getIndividus().size() + " individus");
        System.out.println("Relation: " + products.getName() + " avec " + products.getIndividus().size() + " individus");
        System.out.println("Relation: " + orders.getName() + " avec " + orders.getIndividus().size() + " individus");
        
        // // Test d'opérations relationnelles
        // try {
        //     Relation activeUsers = Relation.selection(users, "status", "ACTIVE");
        //     System.out.println("\nUtilisateurs actifs: " + activeUsers.getIndividus().size());
        // } catch (Exception e) {
        //     System.out.println("Erreur lors de la sélection: " + e.getMessage());
        // }
    }
}