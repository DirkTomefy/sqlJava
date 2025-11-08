package SelectAST.base.function.result;

import SelectAST.err.ParseNomException;

public record ParseError<T>(ParseNomException cause) implements ParseResult<T> {

    @Override
    public ParseSuccess<T> unwrap() throws ParseNomException{
        throw this.cause;
    }

    @Override
    public String getInput() {
        return cause.getInput();
    }


    public String getMessage(){
        return cause.getMessage();
    }
}
