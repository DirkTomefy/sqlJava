package RDP.base.function.expr.helper;

import RDP.base.function.expr.Expression;
import RDP.base.function.result.ParseSuccess;
import RDP.err.ParseNomException;

@FunctionalInterface
public interface SpecialBinOpHandler {
    ParseSuccess<Expression> handle(Expression left, String input) throws ParseNomException;    
}
