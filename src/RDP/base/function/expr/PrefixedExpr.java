package RDP.base.function.expr;

import java.util.Vector;

import RDP.base.function.operand.PrefixedOp;
import RDP.err.EvalErr;
import about.Individual;

public class PrefixedExpr implements Expression {
    private PrefixedOp op;
    private Expression expr;

    public PrefixedExpr(PrefixedOp op, Expression expr) {
        this.op = op;
        this.expr = expr;
    }

  

    @Override
    public String toString() {
        return op + "(" + expr.toString() + ")";
    }

    public PrefixedOp getOp() {
        return op;
    }

    public void setOp(PrefixedOp op) {
        this.op = op;
    }

    public Expression getExpr() {
        return expr;
    }

    public void setExpr(Expression expr) {
        this.expr = expr;
    }



    @Override
    public Object eval(Individual row, Vector<String> fieldName) throws EvalErr {
        Object value = expr.eval(row, fieldName);
        
        System.out.println(""+expr.toString());

        return switch (op) {
            case NOT -> evalNot(value);
            case NEG -> evalNeg(value);
        };
    }

    private Object evalNot(Object value) throws EvalErr {
        boolean boolValue = Expression.ObjectIntoBoolean(value);
        return !boolValue;
    }

    private Object evalNeg(Object value) throws EvalErr {
        double doubleValue;
        
        if (value instanceof Number) {
            doubleValue = ((Number) value).doubleValue();
        } else {
            boolean boolValue = Expression.ObjectIntoBoolean(value);
            doubleValue = Expression.booleanIntoDouble(boolValue);
        }
        
        return -doubleValue;
    }

    public static void main(String[] args) {
        try {
            // Création d'un Individual de test (simulé)
            Individual testRow = new Individual();
            Vector<String> fieldNames = new Vector<>();
            
            System.out.println("=== TESTS PrefixedExpr ===");

            // Test 1: NOT avec booléen true
            System.out.println("\n1. Test NOT avec booléen true:");
            PrimitiveExpr trueExpr = new PrimitiveExpr(PrimitiveKind.NUMBER, 1);
            PrefixedExpr notTrue = new PrefixedExpr(PrefixedOp.NOT, trueExpr);
            Object result = notTrue.eval(testRow, fieldNames);
            System.out.println("NOT(true) = " + result + " (type: " + result.getClass().getSimpleName() + ")");

            // Test 2: NOT avec booléen false
            System.out.println("\n2. Test NOT avec booléen false:");
            PrimitiveExpr falseExpr = new PrimitiveExpr(PrimitiveKind.NUMBER, 0 );
            PrefixedExpr notFalse = new PrefixedExpr(PrefixedOp.NOT, falseExpr);
            result = notFalse.eval(testRow, fieldNames);
            System.out.println("NOT(false) = " + result + " (type: " + result.getClass().getSimpleName() + ")");

            // Test 3: NOT avec nombre positif
            System.out.println("\n3. Test NOT avec nombre positif:");
            PrimitiveExpr numExpr = new PrimitiveExpr(PrimitiveKind.NUMBER, 5.0);
            PrefixedExpr notNum = new PrefixedExpr(PrefixedOp.NOT, numExpr);
            result = notNum.eval(testRow, fieldNames);
            System.out.println("NOT(5.0) = " + result + " (type: " + result.getClass().getSimpleName() + ")");

            // Test 4: NOT avec zéro
            System.out.println("\n4. Test NOT avec zéro:");
            PrimitiveExpr zeroExpr = new PrimitiveExpr(PrimitiveKind.NUMBER, 0.0);
            PrefixedExpr notZero = new PrefixedExpr(PrefixedOp.NOT, zeroExpr);
            result = notZero.eval(testRow, fieldNames);
            System.out.println("NOT(0.0) = " + result + " (type: " + result.getClass().getSimpleName() + ")");

            // Test 5: NOT avec string "true"
            System.out.println("\n5. Test NOT avec string 'true':");
            PrimitiveExpr strTrueExpr = new PrimitiveExpr(PrimitiveKind.STRING, "true");
            PrefixedExpr notStrTrue = new PrefixedExpr(PrefixedOp.NOT, strTrueExpr);
            result = notStrTrue.eval(testRow, fieldNames);
            System.out.println("NOT(\"true\") = " + result + " (type: " + result.getClass().getSimpleName() + ")");

            // Test 6: NOT avec string "false"
            System.out.println("\n6. Test NOT avec string 'false':");
            PrimitiveExpr strFalseExpr = new PrimitiveExpr(PrimitiveKind.STRING, "false");
            PrefixedExpr notStrFalse = new PrefixedExpr(PrefixedOp.NOT, strFalseExpr);
            result = notStrFalse.eval(testRow, fieldNames);
            System.out.println("NOT(\"false\") = " + result + " (type: " + result.getClass().getSimpleName() + ")");

            // Test 7: NOT avec null
            System.out.println("\n7. Test NOT avec null:");
            PrimitiveExpr nullExpr = new PrimitiveExpr(PrimitiveKind.NULLVALUE, null);
            PrefixedExpr notNull = new PrefixedExpr(PrefixedOp.NOT, nullExpr);
            result = notNull.eval(testRow, fieldNames);
            System.out.println("NOT(null) = " + result + " (type: " + result.getClass().getSimpleName() + ")");

            // Test 8: NEG avec nombre positif
            System.out.println("\n8. Test NEG avec nombre positif:");
            PrimitiveExpr posNumExpr = new PrimitiveExpr(PrimitiveKind.NUMBER, 10.5);
            PrefixedExpr negPos = new PrefixedExpr(PrefixedOp.NEG, posNumExpr);
            result = negPos.eval(testRow, fieldNames);
            System.out.println("NEG(10.5) = " + result + " (type: " + result.getClass().getSimpleName() + ")");

            // Test 9: NEG avec nombre négatif
            System.out.println("\n9. Test NEG avec nombre négatif:");
            PrimitiveExpr negNumExpr = new PrimitiveExpr(PrimitiveKind.NUMBER, -7.2);
            PrefixedExpr negNeg = new PrefixedExpr(PrefixedOp.NEG, negNumExpr);
            result = negNeg.eval(testRow, fieldNames);
            System.out.println("NEG(-7.2) = " + result + " (type: " + result.getClass().getSimpleName() + ")");

            // Test 10: NEG avec booléen true
            System.out.println("\n10. Test NEG avec booléen true:");
            PrefixedExpr negTrue = new PrefixedExpr(PrefixedOp.NEG, trueExpr);
            result = negTrue.eval(testRow, fieldNames);
            System.out.println("NEG(true) = " + result + " (type: " + result.getClass().getSimpleName() + ")");

            // Test 11: NEG avec booléen false
            System.out.println("\n11. Test NEG avec booléen false:");
            PrefixedExpr negFalse = new PrefixedExpr(PrefixedOp.NEG, falseExpr);
            result = negFalse.eval(testRow, fieldNames);
            System.out.println("NEG(false) = " + result + " (type: " + result.getClass().getSimpleName() + ")");

            // Test 12: NEG avec string "1"
            System.out.println("\n12. Test NEG avec string '1':");
            PrimitiveExpr strOneExpr = new PrimitiveExpr(PrimitiveKind.STRING, "1");
            PrefixedExpr negStrOne = new PrefixedExpr(PrefixedOp.NEG, strOneExpr);
            result = negStrOne.eval(testRow, fieldNames);
            System.out.println("NEG(\"1\") = " + result + " (type: " + result.getClass().getSimpleName() + ")");

            // Test 13: NEG avec string "0"
            System.out.println("\n13. Test NEG avec string '0':");
            PrimitiveExpr strZeroExpr = new PrimitiveExpr(PrimitiveKind.STRING, "0");
            PrefixedExpr negStrZero = new PrefixedExpr(PrefixedOp.NEG, strZeroExpr);
            result = negStrZero.eval(testRow, fieldNames);
            System.out.println("NEG(\"0\") = " + result + " (type: " + result.getClass().getSimpleName() + ")");

            // Test 14: NEG avec null
            System.out.println("\n14. Test NEG avec null:");
            PrefixedExpr negNull = new PrefixedExpr(PrefixedOp.NEG, nullExpr);
            result = negNull.eval(testRow, fieldNames);
            System.out.println("NEG(null) = " + result + " (type: " + result.getClass().getSimpleName() + ")");

            // Test 15: NEG avec entier
            System.out.println("\n15. Test NEG avec entier:");
            PrimitiveExpr intExpr = new PrimitiveExpr(PrimitiveKind.NUMBER, 42);
            PrefixedExpr negInt = new PrefixedExpr(PrefixedOp.NEG, intExpr);
            result = negInt.eval(testRow, fieldNames);
            System.out.println("NEG(42) = " + result + " (type: " + result.getClass().getSimpleName() + ")");

            System.out.println("\n=== TOUS LES TESTS TERMINÉS ===");

        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
