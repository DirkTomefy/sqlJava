package SelectAST.err.parsing;

import SelectAST.err.ParseNomException;

public class TokenNotFound extends ParseNomException {
    public TokenNotFound(String input){
     super(input,"Token not found");
    }
}
