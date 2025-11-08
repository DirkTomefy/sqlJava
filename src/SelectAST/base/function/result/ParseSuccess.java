package SelectAST.base.function.result;

import SelectAST.err.ParseNomException;

public record ParseSuccess<T>(String remaining,T matched ) implements ParseResult<T> {
     @Override
    public ParseSuccess<T> unwrap() throws ParseNomException{
        return this;
    }

     @Override
    public String getInput() {
        return this.remaining;
    }
}
