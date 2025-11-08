package SelectAST.base.function.result;

import java.util.function.Function;

import SelectAST.err.ParseNomException;

public sealed interface ParseResult<T> permits ParseError, ParseSuccess {
    public ParseSuccess<T> unwrap() throws ParseNomException;
    public String getInput();

    public default <U> ParseResult<U> map(Function<T, U> mapper) {
        if (this instanceof ParseSuccess<T> success) {
            return new ParseSuccess<>(success.remaining(), mapper.apply(success.matched()));
        } else {
            ParseError<T> err = (ParseError<T>) this;
            return new ParseError<>(err.cause());
        }
    }
}
