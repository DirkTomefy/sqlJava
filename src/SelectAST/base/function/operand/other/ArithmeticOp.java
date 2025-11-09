package SelectAST.base.function.operand.other;

import java.util.Vector;

import SelectAST.base.function.expr.Expression;
import SelectAST.base.function.operand.BinaryOp;
import SelectAST.base.function.result.ParseResult;
import SelectAST.base.function.result.ParseSuccess;
import SelectAST.base.helper.ParserNom;
import SelectAST.err.EvalErr;
import SelectAST.err.eval.DivisionByZeroErr;
import about.Individual;

public enum ArithmeticOp implements BinaryOp {
    ADD,
    MIN,
    MUL,
    DIV;

    @Override
    public Object applyByCtx(Individual row, Vector<String> fieldName, Expression left, Expression right)
            throws EvalErr {
        Object leftValue = left.eval(row, fieldName);
        Object rightValue = right.eval(row, fieldName);

        double leftDouble = toDouble(leftValue);
        double rightDouble = toDouble(rightValue);

        return applyOperation(leftDouble, rightDouble);
    }

    private static double toDouble(Object value)  {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }else if (value instanceof String s){
            ParseResult<Double> a=ParserNom.decimal1().apply(s);
            if(a instanceof ParseSuccess<Double> success) return success.matched();
        }
        return makedefaultToDouble(value);
    }

    private static double makedefaultToDouble(Object value) {
        return Expression.booleanIntoDouble(Expression.ObjectIntoBoolean(value));
    }

    private double applyOperation(double left, double right) throws EvalErr {
        return switch (this) {
            case ADD -> left + right;
            case MIN -> left - right;
            case MUL -> left * right;
            case DIV -> {
                if (right == 0.0) {
                    throw new DivisionByZeroErr(left);
                }
                yield left / right;
            }
        };
    }
}
