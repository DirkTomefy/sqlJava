package RDP.err.parsing;

import RDP.err.ParseNomException;

public class TokenNotFound extends ParseNomException {
    public TokenNotFound(String input){
     super(input,"Token not found");
    }
}
