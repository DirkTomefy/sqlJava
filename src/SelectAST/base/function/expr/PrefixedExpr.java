package SelectAST.base.function.expr;

import SelectAST.base.function.operand.PrefixedOp;

public class PrefixedExpr implements Expression {
    private PrefixedOp op;
    private Expression expr;

    public PrefixedExpr(PrefixedOp op, Expression expr) {
        this.op = op;
        this.expr = expr;
    }

    @Override
    public Double eval() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eval'");
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

}
