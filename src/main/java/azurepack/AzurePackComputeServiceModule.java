package azurepack;

import azurepack.compute.AzurePackGetVirtualMachinesAction;
import azurepack.compute.AzurePackLaunchVMAction;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import core.CloudProvider;
import core.SupportFactory;
import core.interfaces.VirtualMachineSupport;
import core.requests.Get;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.dasein.cloud.utils.requester.streamprocessors.JsonStreamToObjectProcessor;
import org.dasein.cloud.utils.requester.streamprocessors.StreamProcessor;

/**
 * Created by vmunthiu on 2/17/2016.
 */
public class AzurePackComputeServiceModule extends AbstractModule {
    private CloudProvider cloudProvider;

    public AzurePackComputeServiceModule(CloudProvider cloudProvider) {
        this.cloudProvider = cloudProvider;
    }

    @Override
    protected void configure() {
        //bind here all support instances
        bind(VirtualMachineSupport.class).toInstance(getVirtualMachineSupportInstance());


        //bind here request instance classes
        Injector getInjector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(HttpClientBuilder.class).toInstance(cloudProvider.getHttpClientBuilder());
                bind(StreamProcessor.class).toInstance(new JsonStreamToObjectProcessor());
                RequestBuilder requestBuilder = RequestBuilder.get();
                addCommonHeaders(requestBuilder);
                bind(RequestBuilder.class).toInstance(requestBuilder);
            }
        });
        bind(Get.class).toInstance(getInjector.getInstance(Get.class));
    }

    private VirtualMachineSupport getVirtualMachineSupportInstance() {
        final SupportFactory<VirtualMachineSupport> virtualMachineSupport = new SupportFactory<VirtualMachineSupport>(VirtualMachineSupport.class);

        virtualMachineSupport.set("getVirtualMachines", new AzurePackGetVirtualMachinesAction(this.cloudProvider));
        virtualMachineSupport.set("launch", new AzurePackLaunchVMAction(this.cloudProvider));

        /* we can do something like this to proper bind actions to methid calls
        new Expectations() {
            { virtualMachineSupport.getVirtualMachines(); action = new AzurePackGetVirtualMachinesAction(cloudProvider); }
            { virtualMachineSupport.launch((VirtualMachine)any); action = new AzurePackLaunchVMAction(cloudProvider);}
        };
        */

        return virtualMachineSupport.getInstance();
    }

    private void addCommonHeaders(RequestBuilder requestBuilder) {
        requestBuilder.addHeader("x-ms-version", "2014-02-01");
        requestBuilder.addHeader("Accept", "application/json");
    }
}
