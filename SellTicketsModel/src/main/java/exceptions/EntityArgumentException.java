package exceptions;

import java.util.ArrayList;

/**
 * Created by Sergiu on 3/25/2017.
 */
public class EntityArgumentException extends RepositoryException {
    public EntityArgumentException(String message) {
        super(message);
    }

    public EntityArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityArgumentException(Throwable cause) {
        super(cause);
    }

    protected EntityArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EntityArgumentException(String join, ArrayList<String> errList) {
        super(join, errList);
    }
}
