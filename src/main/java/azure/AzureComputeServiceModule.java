package azure;

import azure.compute.AzureGetVirtualMachineAction;
import com.google.inject.AbstractModule;
import core.CloudProvider;
import core.GenericSupport;
import core.interfaces.VirtualMachineSupport;

/**
 * Created by vmunthiu on 2/17/2016.
 */
public class AzureComputeServiceModule extends AbstractModule{
    private CloudProvider cloudProvider;

    public AzureComputeServiceModule(CloudProvider cloudProvider) {
        this.cloudProvider = cloudProvider;
    }
    @Override
    protected void configure() {
        GenericSupport<VirtualMachineSupport> virtualMachineSupport = new GenericSupport<VirtualMachineSupport>(VirtualMachineSupport.class);
        virtualMachineSupport.set("getVirtualMachines", new AzureGetVirtualMachineAction(this.cloudProvider));

        bind(VirtualMachineSupport.class).toInstance(virtualMachineSupport.getInstance());
    }
}
