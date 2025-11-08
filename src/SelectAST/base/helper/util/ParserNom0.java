package SelectAST.base.helper.util;

import SelectAST.base.function.result.ParseError;
import SelectAST.base.function.result.ParseResult;
import SelectAST.base.function.result.ParseSuccess;
import SelectAST.base.helper.ParserNom;
import SelectAST.err.ParseNomException;

public class ParserNom0 {
    public static ParseResult<String> parseIntegerPart(String input) {
        return ParserNom.digit1().apply(input);
    }

    public static ParseResult<String> parseFractionPart(String input) {
        return ParserNom.opt(inp -> {
            if (!inp.startsWith("."))
                return new ParseError<>(new ParseNomException(input,"No dot"));
            ParseResult<String> digits = ParserNom.digit1().apply(inp.substring(1));
            if (digits instanceof ParseError<String> e)
                return e;
            String frac = "." + ((ParseSuccess<String>) digits).matched();
            return new ParseSuccess<>(((ParseSuccess<String>) digits).remaining(), frac);
        }, input);
    }

    public static ParseResult<Double> combineIntegerAndFraction(ParseSuccess<String> intPart,
            ParseSuccess<String> fracPart) {
        return combineFirstAndRest(intPart, fracPart, Double::parseDouble);
    }

    public static boolean isIdentStart(char c) {
        return Character.isLetter(c) || c == '_';
    }

    public static boolean isIdentChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    public static ParseResult<String> parseFirstChar(String input) {
        return ParserNom.takeWhile1(ParserNom0::isIdentStart, input);
    }

    public static ParseResult<String> parseRestChars(String input) {
        return ParserNom.opt(inp -> ParserNom.takeWhile1(ParserNom0::isIdentChar, inp), input);
    }

    private static <T, R> ParseResult<R> combineFirstAndRest(
            ParseSuccess<T> first,
            ParseSuccess<T> rest,
            java.util.function.Function<String, R> mapper) {
        String firstStr = first.matched() != null ? first.matched().toString() : "";
        String restStr = rest.matched() != null ? rest.matched().toString() : "";
        String combined = firstStr + restStr;
        R value;
        try {
            value = mapper.apply(combined);
        } catch (Exception e) {
            return new ParseError<>(new ParseNomException(rest.remaining(), "Failed to map combined value"));
        }
        return new ParseSuccess<>(rest.remaining(), value);
    }

    public static ParseResult<String> combineFirstAndRestForTag(ParseSuccess<String> first, ParseSuccess<String> rest) {
    return combineFirstAndRest(first, rest, s -> s); 
}

}
