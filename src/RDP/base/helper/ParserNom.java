package RDP.base.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import RDP.base.function.result.ParseError;
import RDP.base.function.result.ParseResult;
import RDP.base.function.result.ParseSuccess;
import RDP.base.helper.util.ParserNom0;
import RDP.err.ParseNomException;

public class ParserNom {
    // === TAG ===
    public static Function<String, ParseResult<String>> tag(String expected) {
        return input -> {
            if (input.startsWith(expected)) {
                return new ParseSuccess<>(input.substring(expected.length()), expected);
            }
            return new ParseError<>(ParseNomException.buildTagException(input, expected));
        };
    }

    // === TAGNOCASE ===
    public static Function<String, ParseResult<String>> tagNoCase(String expected) {
        return input -> {
            if (input.length() < expected.length()) {
                return new ParseError<>(new ParseNomException(input,
                        "Entrée trop courte : attendu \"" + expected + "\""));
            }

            String prefix = input.substring(0, expected.length());
            if (prefix.equalsIgnoreCase(expected)) {
                String remaining = input.substring(expected.length());
                return new ParseSuccess<>(remaining, prefix);
            } else {
                return new ParseError<>(new ParseNomException(input,
                        "Attendu (sans casse) \"" + expected + "\", trouvé \"" + prefix + "\""));
            }
        };
    }

    // === MULTISPACE1 ===
    public static Function<String, ParseResult<String>> multispace1() {
        return input -> {
            int i = 0;
            while (i < input.length() && Character.isWhitespace(input.charAt(i)))
                i++;
            if (i == 0)
                return new ParseError<>(new ParseNomException(input, "Expected at least one whitespace"));
            return new ParseSuccess<>(input.substring(i), input.substring(0, i));
        };
    }

    // === MULTISPACE0 ===
    public static Function<String, ParseResult<String>> multispace0() {
        return input -> {
            int i = 0;
            while (i < input.length() && Character.isWhitespace(input.charAt(i))) {
                i++;
            }
            String matched = input.substring(0, i);
            String remaining = input.substring(i);
            return new ParseSuccess<>(remaining, matched);
        };
    }

    // === DIGIT1 ===
    public static Function<String, ParseResult<String>> digit1() {
        return input -> {
            int i = 0;
            while (i < input.length() && Character.isDigit(input.charAt(i)))
                i++;
            if (i == 0)
                return new ParseError<>(new ParseNomException(input, "Expected at least one digit"));
            return new ParseSuccess<>(input.substring(i), input.substring(0, i));
        };
    }

    // === DIGIT0 ===
    public static Function<String, ParseResult<String>> digit0() {
        return input -> {
            int i = 0;
            while (i < input.length() && Character.isDigit(input.charAt(i)))
                i++;
            return new ParseSuccess<>(input.substring(i), input.substring(0, i));
        };
    }

    // === OPT ===
    public static <T> ParseResult<T> opt(Function<String, ParseResult<T>> parser, String input) {
        ParseResult<T> r = parser.apply(input);
        if (r instanceof ParseSuccess<T> s)
            return s;
        return new ParseSuccess<>(input, null);
    }

    // === DECIMAL1 ===
    public static Function<String, ParseResult<Double>> decimal1() {
        return input -> {
            ParseResult<String> intPartRes = ParserNom0.parseIntegerPart(input);
            if (intPartRes instanceof ParseError<String> e)
                return new ParseError<>(e.cause());

            ParseResult<String> fracPartRes = ParserNom0
                    .parseFractionPart(((ParseSuccess<String>) intPartRes).remaining());
            if (fracPartRes instanceof ParseError<String> e)
                return new ParseError<>(e.cause());

            return ParserNom0.combineIntegerAndFraction((ParseSuccess<String>) intPartRes,
                    (ParseSuccess<String>) fracPartRes);
        };

    }

    // === TAKEWHILE1 ===
    public static ParseResult<String> takeWhile1(Function<Character, Boolean> predicate, String input) {
        int i = 0;
        while (i < input.length() && predicate.apply(input.charAt(i)))
            i++;
        if (i == 0)
            return new ParseError<>(new ParseNomException(input, "Expected at least one matching character"));
        return new ParseSuccess<>(input.substring(i), input.substring(0, i));
    }

    public static ParseResult<String> tagName(String input) {
        ParseResult<String> firstRes = ParserNom0.parseFirstChar(input);
        if (firstRes instanceof ParseError<String> e)
            return e;

        ParseResult<String> restRes = ParserNom0.parseRestChars(((ParseSuccess<String>) firstRes).remaining());
        if (restRes instanceof ParseError<String> e)
            return e;

        return ParserNom0.combineFirstAndRestForTag((ParseSuccess<String>) firstRes, (ParseSuccess<String>) restRes);
    }

    public static Function<String, ParseResult<String>> tagString(char separator) {
        return input -> {
            ParseResult<String> openQuote = tag("" + separator).apply(input);
            if (openQuote instanceof ParseError<String> e)
                return e;

            String remaining = ((ParseSuccess<String>) openQuote).remaining();

            ParseResult<String> contentRes = takeWhile1(c -> c != separator, remaining);
            if (contentRes instanceof ParseError<String> e)
                return e;

            remaining = ((ParseSuccess<String>) contentRes).remaining();
            String content = ((ParseSuccess<String>) contentRes).matched();

            ParseResult<String> closeQuote = tag("" + separator).apply(remaining);
            if (closeQuote instanceof ParseError<String> e)
                return e;

            remaining = ((ParseSuccess<String>) closeQuote).remaining();

            return new ParseSuccess<>(remaining, content);
        };
    }

    @SafeVarargs
    public static <T> Function<String, ParseResult<T>> alt(Function<String, ParseResult<T>>... parsers) {
        return input -> {
            ParseNomException lastCause = null;

            for (Function<String, ParseResult<T>> parser : parsers) {
                ParseResult<T> result = parser.apply(input);
                if (result instanceof ParseSuccess<T>) {
                    return result;
                } else if (result instanceof ParseError<T> err) {
                    lastCause = err.cause();
                }
            }

            return new ParseError<>(lastCause != null ? lastCause : new ParseNomException(input, "No parser matched"));
        };
    }

    @SafeVarargs
    public static <T> Function<String, ParseResult<List<T>>> tuple(Function<String, ParseResult<T>>... parsers) {
        return input -> {
            List<T> results = new ArrayList<>();
            String rest = input;

            for (Function<String, ParseResult<T>> parser : parsers) {
                ParseResult<T> result = parser.apply(rest);

                if (!(result instanceof ParseSuccess<T> success)) {
                    return new ParseError<>(new ParseNomException(input, "tuple: parseur intermédiaire échoué"));
                }

                results.add(success.matched());
                rest = success.remaining();
            }

            return new ParseSuccess<>(rest, results);
        };
    }
}
