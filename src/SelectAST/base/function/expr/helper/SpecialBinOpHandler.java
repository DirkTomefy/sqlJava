package SelectAST.base.function.expr.helper;

import SelectAST.base.function.expr.Expression;
import SelectAST.base.function.result.ParseSuccess;
import SelectAST.err.ParseNomException;

@FunctionalInterface
public interface SpecialBinOpHandler {
    ParseSuccess<Expression> handle(Expression left, String input) throws ParseNomException;    
}
