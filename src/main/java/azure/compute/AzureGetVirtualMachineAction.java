package azure.compute;

import core.CloudProvider;
import core.actions.Func;
import core.model.VirtualMachine;

import static core.requests.Get.get;

/**
 * Created by vmunthiu on 2/16/2016.
 */
public class AzureGetVirtualMachineAction implements Func<VirtualMachine> {
    private CloudProvider cloudProvider;

    public AzureGetVirtualMachineAction(CloudProvider cloudProvider) {
        this.cloudProvider = cloudProvider;
    }
    @Override
    public VirtualMachine call() throws Exception {
        return get("", VirtualMachine.class);
    }
}
