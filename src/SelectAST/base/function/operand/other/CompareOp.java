package SelectAST.base.function.operand.other;

import java.util.Vector;

import SelectAST.base.function.expr.Expression;
import SelectAST.base.function.expr.PrimitiveExpr;
import SelectAST.base.function.expr.PrimitiveKind;
import SelectAST.base.function.operand.BinaryOp;
import SelectAST.err.EvalErr;
import SelectAST.err.eval.InvalidArgumentErr;
import about.Individual;

public enum CompareOp implements BinaryOp {
    Eq,
    Neq,
    Lt,
    Lte,
    Gt,
    Gte,
    Is,
    IsNot;

    public static CompareOp[] allCompareOp() {
        return new CompareOp[] { CompareOp.Eq, CompareOp.Neq, CompareOp.Lt, CompareOp.Lte, CompareOp.Gt,
                CompareOp.Gte, CompareOp.Is, CompareOp.IsNot };
    }

    @Override
    public Object applyByCtx(Individual row, Vector<String> fieldName, Expression left, Expression right) throws EvalErr {
        return switch (this) {
            case Is, IsNot -> evalIsComparison(row, fieldName, left, right);
            default -> evalStandardComparison(row, fieldName, left, right);
        };
    }

    private Object evalIsComparison(Individual row, Vector<String> fieldName, Expression left, Expression right) throws EvalErr {
        if (!(right instanceof PrimitiveExpr)) {
            throw new InvalidArgumentErr("IS/IS NOT", "right operand must be a primitive null value");
        }
        
        PrimitiveExpr primitiveRight = (PrimitiveExpr) right;
        if (primitiveRight.type != PrimitiveKind.NULLVALUE) {
            throw new InvalidArgumentErr("IS/IS NOT", "right operand must be a null value");
        }
        
        Object leftValue = left.eval(row, fieldName);
        boolean isNull = (leftValue == null);

        return (this == Is) ? isNull : !isNull;
    }

    private Object evalStandardComparison(Individual row, Vector<String> fieldName, Expression left, Expression right) throws EvalErr {
        Object leftValue = left.eval(row, fieldName);
        Object rightValue = right.eval(row, fieldName);

        if (leftValue == null || rightValue == null) {
            return false;
        }

        double leftDouble = toDouble(leftValue);
        double rightDouble = toDouble(rightValue);

        return switch (this) {
            case Eq -> leftDouble == rightDouble;
            case Neq -> leftDouble != rightDouble;
            case Lt -> leftDouble < rightDouble;
            case Lte -> leftDouble <= rightDouble;
            case Gt -> leftDouble > rightDouble;
            case Gte -> leftDouble >= rightDouble;
            default -> throw new InvalidArgumentErr(this.toString(), "comparison not implemented");
        };
    }



    private double toDouble(Object value) throws EvalErr {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        boolean boolValue = Expression.ObjectIntoBoolean(value);
        return Expression.booleanIntoDouble(boolValue);
    }
}