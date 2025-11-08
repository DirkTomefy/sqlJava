package SelectAST.base.function.expr;

import java.util.Vector;

import SelectAST.err.EvalErr;
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

    @Override
    public String toString() {
        return type.toString() + "(" + value + ")";
    }

    @Override
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
        validateFieldNameNotEmpty(fieldName, "ID");
        validateValueIsString();
        
        String idFieldName = (String) value;
        int index = findFieldIndex(fieldName, idFieldName);
        Object idValue = row.get(index);
        if (idValue == null) {
            throw new EvalErr("La valeur du champ '" + idFieldName + "' est null");
        }
        
        return idValue;
    }

    private Object evalNullValue() {
        return null;
    }

    private Object evalNumber() throws EvalErr {
        if (value instanceof Number) {
            return value;
        }
        throw new EvalErr("La valeur pour NUMBER doit être un nombre, mais c'est: " + 
                         (value != null ? value.getClass().getSimpleName() : "null"));
    }

    private Object evalString() throws EvalErr {
        if (value instanceof String) {
            return value;
        }
        throw new EvalErr("La valeur pour STRING doit être une chaîne de caractères, mais c'est: " + 
                         (value != null ? value.getClass().getSimpleName() : "null"));
    }

    private Object evalDefault() throws EvalErr {
        if (value != null) {
            return value;
        }
        throw new EvalErr("La valeur pour le type " + type + " est null");
    }

    private void validateFieldNameNotEmpty(Vector<String> fieldName, String typeName) throws EvalErr {
        if (fieldName == null || fieldName.isEmpty()) {
            throw new EvalErr("Aucun champ spécifié pour le type " + typeName);
        }
    }

    private void validateValueIsString() throws EvalErr {
        if (!(value instanceof String)) {
            throw new EvalErr("La valeur pour ID doit être une chaîne de caractères");
        }
    }

    private int findFieldIndex(Vector<String> fieldName, String fieldToFind) throws EvalErr {
        int index = fieldName.indexOf(fieldToFind);
        if (index == -1) {
            throw new EvalErr("Le champ '" + fieldToFind + "' n'existe pas dans la liste des champs");
        }
        return index;
    }
}