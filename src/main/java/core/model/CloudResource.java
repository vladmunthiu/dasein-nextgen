package core.model;

/**
 * Created by vmunthiu on 12/1/2015.
 */
public abstract class CloudResource {
    private String providerId;

    public CloudResource(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderId() {
        return providerId;
    }
}
