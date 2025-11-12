package RDP.token;

import java.util.function.Function;

import RDP.base.function.operand.PrefixedOp;
import RDP.base.function.operand.other.ArithmeticOp;
import RDP.base.function.operand.other.CompareOp;
import RDP.base.function.operand.other.LogicalOp;
import RDP.base.function.result.ParseError;
import RDP.base.function.result.ParseResult;
import RDP.base.function.result.ParseSuccess;
import RDP.base.helper.ParserNom;
import RDP.err.ParseNomException;

public class Tokenizer {
    private static final String[] PrivatizedToken = { "or", "and", "null", "is" };

    private static boolean isPrivatized(String s) {
        for (String t : PrivatizedToken) {
            if (t.equalsIgnoreCase(s))
                return true;

        }
        return false;
    }

    public static ParseResult<Token> mapToBinOpToken(ParseSuccess<String> success, String input) {
        String matched = success.matched();
        ArithmeticOp op;
        switch (matched) {
            case "+" -> op = ArithmeticOp.ADD;
            case "-" -> op = ArithmeticOp.MIN;
            case "*" -> op = ArithmeticOp.MUL;
            case "/" -> op = ArithmeticOp.DIV;
            default -> {
                return new ParseError<>(new ParseNomException(input, "Unknown operator: " + matched));
            }
        }
        return new ParseSuccess<>(success.remaining(), Token.binop(op));
    }

    public static ParseResult<Token> mapToCompareOpToken(ParseSuccess<String> success, String input) {
        String matched = success.matched();
        CompareOp op;
        switch (matched) {
            case "=" -> op = CompareOp.Eq;
            case "!=" -> op = CompareOp.Neq;
            case ">=" -> op = CompareOp.Gte;
            case ">" -> op = CompareOp.Gt;
            case "<=" -> op = CompareOp.Lte;
            case "<" -> op = CompareOp.Lt;
            default -> {
                if ("is".equalsIgnoreCase(matched)) {
                    op = CompareOp.Is;
                } else {
                    return new ParseError<>(new ParseNomException(input, "Unknown operator: " + matched));
                }

            }
        }
        return new ParseSuccess<>(success.remaining(), Token.binop(op));
    }

    public static ParseResult<Token> mapToLogicalOpToken(ParseSuccess<String> success, String input) {
        String matched = success.matched();
        LogicalOp op;
        if (matched.equalsIgnoreCase("and")) {
            op = LogicalOp.AND;
        } else if (matched.equalsIgnoreCase("or")) {
            op = LogicalOp.OR;
        } else {
            return new ParseError<>(new ParseNomException(input, "Unknown operator: " + matched));
        }
        return new ParseSuccess<>(success.remaining(), Token.binop(op));
    }

    public static Function<String, ParseResult<Token>> tagArithmOp() {
        return input -> {
            Function<String, ParseResult<String>> parser = ParserNom.alt(
                    ParserNom.tag("+"),
                    ParserNom.tag("-"),
                    ParserNom.tag("*"),
                    ParserNom.tag("/"));

            ParseResult<String> result = parser.apply(input);

            if (result instanceof ParseError<String> err) {
                return new ParseError<>(err.cause());
            }

            ParseSuccess<String> success = (ParseSuccess<String>) result;

            return mapToBinOpToken(success, input);
        };
    }

    public static Function<String, ParseResult<Token>> tagCompareOp() {
        return input -> {
            Function<String, ParseResult<String>> parser = ParserNom.alt(
                    ParserNom.tag("<="),
                    ParserNom.tag(">="),
                    ParserNom.tag("!="),
                    ParserNom.tag("<"),
                    ParserNom.tag(">"),
                    ParserNom.tag("="),
                    ParserNom.tagNoCase("is") // is utilisé mais non isNot !!
            );

            ParseResult<String> result = parser.apply(input);

            if (result instanceof ParseError<String> err) {
                return new ParseError<>(err.cause());
            }

            ParseSuccess<String> success = (ParseSuccess<String>) result;

            return mapToCompareOpToken(success, input);
        };
    }

    public static Function<String, ParseResult<Token>> tagLogicalOp() {
        return input -> {
            Function<String, ParseResult<String>> parser = ParserNom.alt(
                    ParserNom.tagNoCase("and"),
                    ParserNom.tagNoCase("or"));
            ParseResult<String> result = parser.apply(input);

            if (result instanceof ParseError<String> err) {
                return new ParseError<>(err.cause());
            }

            ParseSuccess<String> success = (ParseSuccess<String>) result;

            return mapToLogicalOpToken(success, input);
        };
    }

    public static Function<String, ParseResult<Token>> tagPrefixedOp() {
        return input -> {
            Function<String, ParseResult<String>> parser = ParserNom.alt(
                    ParserNom.tag("!"));

            ParseResult<String> result = parser.apply(input);

            if (result instanceof ParseError<String> err) {
                return new ParseError<>(err.cause());
            }

            ParseSuccess<String> success = (ParseSuccess<String>) result;

            return new ParseSuccess<Token>(success.remaining(), new Token(TokenKind.PREFIXEDOP, PrefixedOp.NOT));
        };
    }

    public static Function<String, ParseResult<Token>> tagString() {
        return input -> {
            Function<String, ParseResult<String>> parser = ParserNom.alt(
                    ParserNom.tagString('"'),
                    ParserNom.tagString('\''));

            ParseResult<String> result = parser.apply(input);

            if (result instanceof ParseError<String> err) {
                return new ParseError<>(err.cause());
            }

            ParseSuccess<String> success = (ParseSuccess<String>) result;
            return new ParseSuccess<Token>(success.remaining(), new Token(TokenKind.STRING, success.matched()));
        };
    }

    // TODO create new function tagString for '' and ""
    public static Function<String, ParseResult<Token>> tagNumber() {
        return input -> {
            Function<String, ParseResult<Double>> numberParser = ParserNom.decimal1();
            ParseResult<Double> result = numberParser.apply(input);

            if (result instanceof ParseError<Double> err) {
                return new ParseError<>(err.cause());
            }
            ParseSuccess<Double> success = (ParseSuccess<Double>) result;
            return new ParseSuccess<>(success.remaining(), Token.number(success.matched()));
        };
    }

    public static Function<String, ParseResult<Token>> tagOtherKeyWord() {
        return input -> {
            Function<String, ParseResult<String>> parser = ParserNom.alt(
                    ParserNom.tag("("),
                    ParserNom.tag(")"));

            ParseResult<String> result = parser.apply(input);

            if (result instanceof ParseError<String> err) {
                return new ParseError<>(err.cause());
            }

            ParseSuccess<String> success = (ParseSuccess<String>) result;
            return new ParseSuccess<>(success.remaining(), Token.other(success.matched()));
        };
    }

    public static Function<String, ParseResult<Token>> tagId() {
        return input -> {
            ParseResult<String> result = ParserNom.tagName(input);
            if (result instanceof ParseError<String> err)
                return new ParseError<>(err.cause());

            ParseSuccess<String> success = (ParseSuccess<String>) result;
            if (isPrivatized(success.matched()))
                return new ParseError<>(new ParseNomException(input,
                        "can not use '" + success.matched() + "' as an id because it's a privatized token"));
            return new ParseSuccess<Token>(success.remaining(), Token.id(success.matched()));
        };
    }

    public static Function<String, ParseResult<Token>> tagNullValue() {
        return input -> {
            ParseResult<String> result = ParserNom.tagNoCase("null").apply(input);
            if (result instanceof ParseError<String> err)
                return new ParseError<>(err.cause());

            ParseSuccess<String> success = (ParseSuccess<String>) result;
            return new ParseSuccess<Token>(success.remaining(), Token.nullvalue());
        };
    }

    public static ParseResult<Token> mapManyKeyWordToToken(String mkw, String inputSuccess, String inputErr) {
        return switch (mkw.trim().toLowerCase()) {
            case "isnot" -> {
                yield new ParseSuccess<>(inputSuccess, Token.binop(CompareOp.IsNot));
            }
            default -> {
                yield new ParseError<>(new ParseNomException(inputErr, "Keyword expected"));
            }
        };
    }

    public static Function<String, ParseResult<Token>> tagManyKeyWords() {
        return input -> {
            var isNot = ParserNom.tuple(
                    ParserNom.tagNoCase("is"),
                    ParserNom.multispace1(),
                    entry -> {
                        return ParserNom.tagName(entry);
                    });

            var combinedParser = ParserNom.alt(isNot);

            var result = combinedParser.apply(input).map(list -> String.join("", list));
            if (result instanceof ParseSuccess<String> success) {
                return mapManyKeyWordToToken(success.matched().replace(" ", ""), success.getInput(), input);
            } else {
                return new ParseError<>(new ParseNomException(input, "Keyword expected"));
            }
        };
    }

    public static ParseResult<Token> scanToken(String input) {
        input = input.trim();
        Function<String, ParseResult<Token>> parser = ParserNom.alt(
                tagString(),
                tagManyKeyWords(),
                tagId(),
                tagNumber(),
                tagNullValue(),
                tagOtherKeyWord(),
                tagLogicalOp(),
                tagCompareOp(),
                tagArithmOp(),
                tagPrefixedOp());
        
        return parser.apply(input);
    }

    public static boolean codonStop(String input) {
        return input.trim().startsWith(")") || input.trim().isEmpty();
    }

    public static void main(String[] args) throws ParseNomException {
        var t= scanToken("<").unwrap();
        System.out.println(""+t.matched());
    }
}
