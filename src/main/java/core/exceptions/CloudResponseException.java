package core.exceptions;

import org.apache.http.client.ClientProtocolException;

/**
 * Created by vmunthiu on 10/12/2015.
 */
public class CloudResponseException extends ClientProtocolException {
    private CloudErrorType errorType;
    private int            httpCode;
    private String         providerCode;

    public CloudResponseException(CloudErrorType cloudErrorType, int httpCode, String providerCode, String message){
        super(message);
        this.errorType = cloudErrorType;
        this.httpCode = httpCode;
        this.providerCode = providerCode;
    }

    public CloudErrorType getErrorType() {
        return errorType;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getProviderCode() {
        return providerCode;
    }
}
