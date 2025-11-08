package SelectAST.err.eval;

import SelectAST.err.EvalErr;

public class NullValueErr extends EvalErr {
    public NullValueErr(String context) {
        super("Null value not allowed for: " + context);
    }
}

