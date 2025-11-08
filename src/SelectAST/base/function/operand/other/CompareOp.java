package SelectAST.base.function.operand.other;

import SelectAST.base.function.expr.Expression;
import SelectAST.base.function.operand.BinaryOp;

public enum CompareOp implements BinaryOp {
    Eq,
    Neq,
    Lt,
    Lte,
    Gt,
    Gte,
    Is,
    IsNot;

    @Override
    public double evalBySelf(Expression left, Expression rigth) {
        throw new UnsupportedOperationException("Unimplemented method 'evalBySelf'");
    }

    public static CompareOp[] allCompareOp() {
        return new CompareOp[] { CompareOp.Eq, CompareOp.Neq, CompareOp.Lt, CompareOp.Lte, CompareOp.Gt,
                CompareOp.Gte, CompareOp.Is, CompareOp.IsNot };
    }

}
