package SelectAST.base.function.operand.other;

import java.util.Vector;

import SelectAST.base.function.expr.Expression;
import SelectAST.base.function.operand.BinaryOp;
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
    public boolean applyByBool(boolean left, boolean right) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'applyByBool'");
    }

    @Override
    public Object applyByCtx(Individual row, Vector<String> fieldName, Expression left, Expression right) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'applyByCtx'");
    }

 
}
