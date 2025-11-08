package SelectAST.base.function.expr;

import SelectAST.base.function.operand.BinaryOp;

public class BinaryExpr implements Expression {
    private final Expression left;
    private final BinaryOp op;
    private final Expression right;

    public BinaryExpr(Expression left, BinaryOp op, Expression right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public Double eval() {
        return op.evalBySelf(left,right);
       
    }

    @Override
    public String toString() {
        return "(" + left + " " + op + " " + right + ")";
    }
}
