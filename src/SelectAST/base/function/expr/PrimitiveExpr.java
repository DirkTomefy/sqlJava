package SelectAST.base.function.expr;

import java.util.Vector;

import SelectAST.err.EvalErr;
import SelectAST.err.eval.AmbigousNameErr;
import SelectAST.err.eval.FieldNotFoundErr;
import SelectAST.err.eval.InvalidArgumentErr;
import SelectAST.err.eval.NullValueErr;
import SelectAST.err.eval.TypeMismatchErr;
import about.Individual;

public class PrimitiveExpr implements Expression {
    public PrimitiveKind type;
    private final Object value;

    public Object getValue() {
        return value;
    }

    public PrimitiveExpr(PrimitiveKind type, Object value) {
        this.type = type;
        this.value = value;
    }

    public static PrimitiveExpr number(Double valueDouble) {
        return new PrimitiveExpr(PrimitiveKind.NUMBER, valueDouble);
    }

    public static PrimitiveExpr id(String valueString) {
        return new PrimitiveExpr(PrimitiveKind.ID, valueString);
    }

    public static PrimitiveExpr string(String valueString) {
        return new PrimitiveExpr(PrimitiveKind.STRING, valueString);
    }

    @Override
    public String toString() {
        return type.toString() + "(" + value + ")";
    }

    public Object eval(Individual row, Vector<String> fieldName) throws EvalErr {
        return switch (type) {
            case ID -> evalId(row, fieldName);
            case NULLVALUE -> evalNullValue();
            case NUMBER -> evalNumber();
            case STRING -> evalString();
            default -> evalDefault();
        };
    }

    private Object evalId(Individual row, Vector<String> fieldName) throws EvalErr {
      
        if (fieldName == null || fieldName.isEmpty()) {
            throw new InvalidArgumentErr("ID", "field names cannot be null or empty");
        }

        if (!(value instanceof String)) {
            throw new TypeMismatchErr("String", value);
        }

        String idFieldName = (String) value;
        int index = fieldName.indexOf(idFieldName);

        if(fieldName.lastIndexOf(idFieldName)!=fieldName.indexOf(idFieldName)) throw new AmbigousNameErr(idFieldName);
        
        if (index == -1) {
            throw new FieldNotFoundErr(idFieldName, fieldName);
        }

        Object idValue = row.get(index);
        if (idValue == null) {
            throw new NullValueErr("field '" + idFieldName + "'");
        }

        return idValue;
    }

    private Object evalNumber() throws EvalErr {
        if (value instanceof Number) {
            return value;
        }
        throw new TypeMismatchErr("Number", value);
    }

    private Object evalString() throws EvalErr {
        if (value instanceof String) {
            return value;
        }
        throw new TypeMismatchErr("String", value);
    }

    private Object evalNullValue() {
        return null;
    }

    private Object evalDefault() throws EvalErr {
        if (value != null) {
            return value;
        }
        throw new NullValueErr("default primitive expression");
    }

    public static void main(String[] args) {
        try {
            // Création d'un Individual de test
            Individual testRow = new Individual();
            Vector<Object> rowValues = new Vector<>();
            rowValues.add(100); // index 0: user_id
            rowValues.add("John Doe"); // index 1: name
            rowValues.add(25.5); // index 2: age
            rowValues.add("Paris"); // index 3: city
            // Supposons que Individual a une méthode pour set les values
            testRow.setValues(rowValues);

            // Vector des noms de champs
            Vector<String> fieldNames = new Vector<>();
            fieldNames.add("user_id");
            fieldNames.add("name");
            fieldNames.add("age");
            fieldNames.add("city");

            System.out.println("=== TESTS PrimitiveExpr ===");

            // Test 1: ID avec champ existant
            System.out.println("\n1. Test ID avec champ existant:");
            PrimitiveExpr idExpr = PrimitiveExpr.id("user_id");
            Object result = idExpr.eval(testRow, fieldNames);
            System.out.println("ID 'user_id' = " + result);

            // Test 2: ID avec champ inexistant
            System.out.println("\n2. Test ID avec champ inexistant:");
            try {
                PrimitiveExpr idExpr2 = PrimitiveExpr.id("inexistant");
                result = idExpr2.eval(testRow, fieldNames);
                System.out.println("ID 'inexistant' = " + result);
            } catch (EvalErr e) {
                System.out.println("ERREUR attendue: " + e.getMessage());
            }

            // Test 3: NUMBER valide
            System.out.println("\n3. Test NUMBER valide:");
            PrimitiveExpr numExpr = PrimitiveExpr.number(42.5);
            result = numExpr.eval(testRow, fieldNames);
            System.out.println("NUMBER = " + result);

            // Test 4: STRING valide
            System.out.println("\n4. Test STRING valide:");
            PrimitiveExpr strExpr = new PrimitiveExpr(PrimitiveKind.STRING, "Hello World");
            result = strExpr.eval(testRow, fieldNames);
            System.out.println("STRING = " + result);

            // Test 5: NULLVALUE
            System.out.println("\n5. Test NULLVALUE:");
            PrimitiveExpr nullExpr = new PrimitiveExpr(PrimitiveKind.NULLVALUE, null);
            result = nullExpr.eval(testRow, fieldNames);
            System.out.println("NULLVALUE = " + result);

            // Test 6: NUMBER avec mauvais type
            System.out.println("\n6. Test NUMBER avec mauvais type:");
            try {
                PrimitiveExpr badNumExpr = new PrimitiveExpr(PrimitiveKind.NUMBER, "pas_un_nombre");
                result = badNumExpr.eval(testRow, fieldNames);
                System.out.println("NUMBER = " + result);
            } catch (EvalErr e) {
                System.out.println("ERREUR attendue: " + e.getMessage());
            }

            // Test 7: STRING avec mauvais type
            System.out.println("\n7. Test STRING avec mauvais type:");
            try {
                PrimitiveExpr badStrExpr = new PrimitiveExpr(PrimitiveKind.STRING, 123);
                result = badStrExpr.eval(testRow, fieldNames);
                System.out.println("STRING = " + result);
            } catch (EvalErr e) {
                System.out.println("ERREUR attendue: " + e.getMessage());
            }

            // Test 8: ID avec valeur non String
            System.out.println("\n8. Test ID avec valeur non String:");
            try {
                PrimitiveExpr badIdExpr = new PrimitiveExpr(PrimitiveKind.ID, 123);
                result = badIdExpr.eval(testRow, fieldNames);
                System.out.println("ID = " + result);
            } catch (EvalErr e) {
                System.out.println("ERREUR attendue: " + e.getMessage());
            }

            // Test 9: fieldNames null
            System.out.println("\n9. Test fieldNames null:");
            try {
                result = idExpr.eval(testRow, null);
                System.out.println("ID avec fieldNames null = " + result);
            } catch (EvalErr e) {
                System.out.println("ERREUR attendue: " + e.getMessage());
            }

            // Test 10: fieldNames vide
            System.out.println("\n10. Test fieldNames vide:");
            try {
                Vector<String> emptyFields = new Vector<>();
                result = idExpr.eval(testRow, emptyFields);
                System.out.println("ID avec fieldNames vide = " + result);
            } catch (EvalErr e) {
                System.out.println("ERREUR attendue: " + e.getMessage());
            }

            System.out.println("\n=== TOUS LES TESTS TERMINÉS ===");

        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
            e.printStackTrace();
        }
    }

}