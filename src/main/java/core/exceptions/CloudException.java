package core.exceptions;

import com.sun.istack.internal.Nullable;
import com.sun.javafx.beans.annotations.NonNull;

/**
 * Created by vmunthiu on 10/12/2015.
 */
public class CloudException extends Exception{
    private static final long serialVersionUID = -1975104091752615199L;

    private CloudErrorType errorType;
    private int            httpCode;
    private String         providerCode;

    /**
     * Constructs an unlabeled exception.
     */
    public CloudException() {
        super();
    }

    /**
     * Constructs a cloud exception with a specific error message.
     * @param msg the message for the error that occurred
     */
    public CloudException(@NonNull String msg) {
        super(msg);
    }

    /**
     * Constructs a cloud exception in response to a specific cause.
     * @param cause the error that caused this exception to be thrown
     */
    public CloudException(@NonNull Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a cloud exception with a specific error message and cause.
     * @param msg the message for the error that occurred
     * @param cause the error that caused this exception to be thrown
     */
    public CloudException(@NonNull String msg, @NonNull Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs a cloud exception with cloud provider data added in
     * @param type cloud error type
     * @param httpCode the HTTP error code
     * @param providerCode the provider-specific error code
     * @param msg the error message
     */
    public CloudException(@NonNull CloudErrorType type, int httpCode, @Nullable String providerCode, @NonNull String msg) {
        super(msg);
        this.errorType = type;
        this.httpCode = httpCode;
        this.providerCode = providerCode;
    }

    /**
     * Constructs a cloud exception with cloud provider data added in
     * @param type cloud error type
     * @param httpCode the HTTP error code
     * @param providerCode the provider-specific error code
     * @param msg the error message
     * @param cause the error that caused this exception to be thrown
     */
    public CloudException(@NonNull CloudErrorType type, int httpCode, @Nullable String providerCode, @NonNull String msg, @NonNull Throwable cause) {
        super(msg, cause);
        this.errorType = type;
        this.httpCode = httpCode;
        this.providerCode = providerCode;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public @NonNull CloudErrorType getErrorType() {
        return (errorType == null ? CloudErrorType.GENERAL : errorType);
    }

    public @NonNull String getProviderCode() {
        return (providerCode == null ? "" : providerCode);
    }
}
