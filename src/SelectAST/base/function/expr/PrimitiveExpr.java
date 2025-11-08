package SelectAST.base.function.expr;

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

    @Override
    public Double eval() {
        return (Double) value;
    }

    public static PrimitiveExpr number(Double valueDouble) {
        return new PrimitiveExpr(PrimitiveKind.NUMBER, valueDouble);
    }

    public static PrimitiveExpr id(String valueString){
        return new PrimitiveExpr(PrimitiveKind.ID, valueString);
    }  
    @Override
    public String toString(){
        return type.toString()+"("+value+")";
    }
}