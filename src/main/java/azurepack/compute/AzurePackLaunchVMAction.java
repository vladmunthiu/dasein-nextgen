package azurepack.compute;

import core.CloudProvider;
import core.actions.Func1;
import core.model.VirtualMachine;

/**
 * Created by vmunthiu on 2/17/2016.
 */
public class AzurePackLaunchVMAction implements Func1<VirtualMachine, VirtualMachine> {
    private CloudProvider cloudProvider;

    public AzurePackLaunchVMAction(CloudProvider cloudProvider) {
        this.cloudProvider = cloudProvider;
    }
    @Override
    public VirtualMachine call(VirtualMachine target) throws Exception {
        return null;
    }
}
