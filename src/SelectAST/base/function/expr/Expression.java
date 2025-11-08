package SelectAST.base.function.expr;

import java.util.function.Function;

import SelectAST.base.function.expr.helper.FactorHelper;
import SelectAST.base.function.operand.BinaryOp;
import SelectAST.base.function.operand.other.ArithmeticOp;
import SelectAST.base.function.operand.other.CompareOp;
import SelectAST.base.function.operand.other.LogicalOp;
import SelectAST.base.function.result.ParseError;
import SelectAST.base.function.result.ParseResult;
import SelectAST.base.function.result.ParseSuccess;
import SelectAST.err.ParseNomException;
import SelectAST.token.Token;
import SelectAST.token.TokenKind;
import SelectAST.token.Tokenizer;

public interface Expression {

    public static final Function<String, ParseResult<Expression>> level0 = Expression::parseLogical;

    public static boolean contains(BinaryOp[] list, BinaryOp value) {
        for (BinaryOp item : list) {
            if (value.equals(item))
                return true;
        }
        return false;
    }

    public static ParseSuccess<Expression> parse_expr_level(
            String input,
            Function<String, ParseResult<Expression>> lower_parser,
            BinaryOp[] ops) throws ParseNomException {
        ParseSuccess<Expression> result = lower_parser.apply(input).unwrap();
        input = result.remaining();
        Expression current = result.matched();
        while (true) {
            if (Tokenizer.codonStop(input))
                break;

            String oldInput = input;
            ParseResult<Token> nextRes = Tokenizer.scanToken(input);
            if (!(nextRes instanceof ParseSuccess<Token> next)) {
                break;
            }

            Token token = next.matched();
            input = next.remaining();

            if (token.status == TokenKind.BINOP) {
                BinaryOp op = (BinaryOp) token.getValue();

                if (contains(ops, op)) {
                    ParseSuccess<Expression> rhs = lower_parser.apply(input).unwrap();
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

    public static ParseResult<Expression> parseLogical(String input) {
        BinaryOp[] ops = { LogicalOp.OR, LogicalOp.AND };
        try {
            return parse_expr_level(input, Expression::parseCompare, ops);
        } catch (ParseNomException e) {
            return new ParseError<>(e);
        }
    }

    public static ParseResult<Expression> parseCompare(String input) {
        BinaryOp[] ops = CompareOp.allCompareOp();
        try {
            return parse_expr_level(input, Expression::parseAtom, ops);
        } catch (ParseNomException e) {
            return new ParseError<>(e);
        }
    }

    public static ParseResult<Expression> parseAtom(String input) {
        BinaryOp[] ops = { ArithmeticOp.ADD, ArithmeticOp.MIN };
        try {
            return parse_expr_level(input, Expression::parseTerm, ops);
        } catch (ParseNomException e) {
            return new ParseError<>(e);
        }
    }

    public static ParseResult<Expression> parseTerm(String input) {
        BinaryOp[] ops = { ArithmeticOp.MUL, ArithmeticOp.DIV };
        try {
            return parse_expr_level(input, Expression.parseFactorFn(), ops);
        } catch (ParseNomException e) {
            return new ParseError<>(e);
        }
    }

    public static ParseSuccess<Expression> parseFactor0(String input) throws ParseNomException {
        ParseSuccess<Token> tSuccess = Tokenizer.scanToken(input).unwrap();
        Token t = tSuccess.matched();

        String rest = tSuccess.remaining();

        return switch (t.status) {
            case NUMBER -> {
                yield FactorHelper.handleNumber(t, rest);
            }
            case OTHER -> {
                yield FactorHelper.handleOther(t, rest, input);
            }
            case ID -> {
                yield FactorHelper.handleId(t, rest);
            }
            case PREFIXEDOP -> {
                yield FactorHelper.handlePrefixedOp(t, rest);
            }
            case BINOP -> {
                yield FactorHelper.handleBinOp(t, rest, input);
            }
            case NULLVALUE -> {
                yield new ParseSuccess<>(rest, new PrimitiveExpr(PrimitiveKind.NULLVALUE, null));
            }
            default -> throw ParseNomException.buildTokenWrongPlace(t, input);
        };
    }

    public static Function<String, ParseResult<Expression>> parseFactorFn() {
        return input -> {
            try {
                return parseFactor0(input);
            } catch (ParseNomException e) {
                return new ParseError<>(e);
            }
        };
    }

    public abstract Double eval();

    public static void main(String[] args) throws ParseNomException {
        String input = "1 is not 1";
        var res = parseLogical(input).unwrap();
        System.out.println(res.matched());
        System.out.println("Remaining : " + res.remaining());
        System.out.println("Input : " + input);
        // System.out.println("error : " + res.getnull+1*3 and 1Message());
        // System.out.println(res.matched().eval());
    }
}
