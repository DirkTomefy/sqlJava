package SelectAST.base.function.expr;

import java.util.Vector;
import java.util.function.Function;

import SelectAST.base.function.expr.helper.FactorHelper;
import SelectAST.base.function.operand.BinaryOp;
import SelectAST.base.function.operand.other.ArithmeticOp;
import SelectAST.base.function.operand.other.CompareOp;
import SelectAST.base.function.operand.other.LogicalOp;
import SelectAST.base.function.result.ParseError;
import SelectAST.base.function.result.ParseResult;
import SelectAST.base.function.result.ParseSuccess;
import SelectAST.err.EvalErr;
import SelectAST.err.ParseNomException;
import SelectAST.token.Token;
import SelectAST.token.TokenKind;
import SelectAST.token.Tokenizer;
import about.Individual;

public interface Expression {
    public static final Function<String, ParseResult<Expression>> parseExpression = input -> {
        try {
            return parseExprLevel(0, input);

        } catch (ParseNomException e) {
            return new ParseError<>(e);
        }
    };
    static final BinaryOp[][] LEVELS = {
            { LogicalOp.OR, LogicalOp.AND },
            CompareOp.allCompareOp(),
            { ArithmeticOp.ADD, ArithmeticOp.MIN },
            { ArithmeticOp.MUL, ArithmeticOp.DIV }
    };

    


    public abstract Object eval(Individual row, Vector<String> fieldName) throws EvalErr;

    public default boolean evalToBoolean(Individual row, Vector<String> fieldName) throws EvalErr {
        return ObjectIntoBoolean(eval(row, fieldName));
    }

    public static boolean ObjectIntoBoolean(Object e) {
        if (e == null)
            return false;
        if (e instanceof Boolean b)
            return b;
        if (e instanceof Number n)
            return n.doubleValue() != 0.0;
        if (e instanceof String s) {
            String str = s.trim().toLowerCase();
            return str.equals("true") || str.equals("1") || str.equals("yes")
                    || str.equals("on") || str.equals("t") || str.equals("y");
        }
        return true;
    }

    public static double booleanIntoDouble(boolean b) {
        return b ? 1.0 : 0.0;
    }

    public static boolean containsOp(BinaryOp[] list, BinaryOp value) {
        for (BinaryOp item : list) {
            if (value.equals(item))
                return true;
        }
        return false;
    }

    // ===================== Parseur générique de niveau =======================
    public static ParseSuccess<Expression> parseExprLevel(int level, String input) throws ParseNomException {
        if (level >= LEVELS.length) {
            return parseFactor0(input);
        }

        ParseSuccess<Expression> result = parseExprLevel(level + 1, input);
        input = result.remaining();
        Expression current = result.matched();

        while (true) {
            if (Tokenizer.codonStop(input))
                break;

            String oldInput = input;
            ParseResult<Token> nextRes = Tokenizer.scanToken(input);
            if (!(nextRes instanceof ParseSuccess<Token> next))
                break;

            Token token = next.matched();
            input = next.remaining();

            if (token.status == TokenKind.BINOP) {
                BinaryOp op = (BinaryOp) token.getValue();

                if (containsOp(LEVELS[level], op)) {
                    ParseSuccess<Expression> rhs = parseExprLevel(level + 1, input).unwrap();
                    input = rhs.remaining();
                    current = new BinaryExpr(current, op, rhs.matched());
                    continue;
                } else {
                    input = oldInput;
                }
            } else {
                input = oldInput;
            }

            break;
        }

        return new ParseSuccess<>(input, current);
    }

    // ========================= Parseur de facteur ==========================
    public static ParseSuccess<Expression> parseFactor0(String input) throws ParseNomException {
        ParseSuccess<Token> tSuccess = Tokenizer.scanToken(input).unwrap();
        Token t = tSuccess.matched();
        String rest = tSuccess.remaining();

        return switch (t.status) {
            case NUMBER -> FactorHelper.handleNumber(t, rest);
            case OTHER -> FactorHelper.handleOther(t, rest, input);
            case ID -> FactorHelper.handleId(t, rest);
            case PREFIXEDOP -> FactorHelper.handlePrefixedOp(t, rest);
            case BINOP -> FactorHelper.handleBinOp(t, rest, input);
            case NULLVALUE -> new ParseSuccess<>(rest, new PrimitiveExpr(PrimitiveKind.NULLVALUE, null));
            case STRING -> FactorHelper.handleString(t, rest);
            default -> throw ParseNomException.buildTokenWrongPlace(t, input);
        };
    }

   
    public static void main(String[] args) throws ParseNomException {
        String input = "-(1)=0+(-1)";
        var res = parseExpression.apply(input).unwrap();
        System.out.println(res.matched());
        System.out.println("Remaining : " + res.remaining());
    }
}
