package debug;

import java.util.Vector;

import about.Individual;
import about.Relation;

public class TestSelection {
    public static void main(String[] args) {
        testSelection();
    }
    public static void testSelection() {
        try {
            System.out.println("=== TEST DE LA FONCTION SELECTION ===");

            // Création des noms de champs
            Vector<String> fieldNames = new Vector<>();
            fieldNames.add("nom");
            fieldNames.add("age");
            fieldNames.add("ville");

            // Création des individus
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

            // Création de la relation
            Relation relation = new Relation("Personnes", fieldNames, new Vector<>(), individus);

            System.out.println("Relation originale: " + relation.getIndividus().size() + " individus");
            for (Individual ind : relation.getIndividus()) {
                System.out.println("  " + ind.getValues());
            }

            // Test 1: Sélection par âge
            System.out.println("\n1. Sélection age > 28:");
            Relation result1 = relation.selection("age > 28");
            System.out.println("Résultat: " + result1.getIndividus().size() + " individus");
            for (Individual ind : result1.getIndividus()) {
                System.out.println("  " + ind.getValues());
            }

            // Test 2: Sélection par ville
            System.out.println("\n2. Sélection ville = 'Paris':");
            Relation result2 = relation.selection("ville = \"Paris\"");
            System.out.println("Résultat: " + result2.getIndividus().size() + " individus");
            for (Individual ind : result2.getIndividus()) {
                System.out.println("  " + ind.getValues());
            }

            // Test 3: Sélection combinée
            System.out.println("\n3. Sélection age <= 30 AND ville != 'Paris':");
            Relation result3 = relation.selection("age <= 30 AND ville != 'Paris'");
            System.out.println("Résultat: " + result3.getIndividus().size() + " individus");
            for (Individual ind : result3.getIndividus()) {
                System.out.println("  " + ind.getValues());
            }

            // Test 4: Sélection avec condition toujours vraie
            System.out.println("\n4. Sélection 1 = 1 (tous les individus):");
            Relation result4 = relation.selection("1 = 1");
            System.out.println("Résultat: " + result4.getIndividus().size() + " individus");

            // Test 5: Sélection avec condition toujours fausse
            System.out.println("\n5. Sélection 1 = 0 (aucun individu):");
            Relation result5 = relation.selection("1 = 0");
            System.out.println("Résultat: " + result5.getIndividus().size() + " individus");

            System.out.println("\n=== TESTS TERMINÉS ===");

        } catch (Exception e) {
            System.err.println("Erreur pendant le test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
