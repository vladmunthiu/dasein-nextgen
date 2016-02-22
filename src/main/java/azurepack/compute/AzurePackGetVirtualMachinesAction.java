package azurepack.compute;

import azurepack.compute.model.WAPVirtualMachineModel;
import azurepack.compute.model.WAPVirtualMachinesModel;
import core.CloudProvider;
import core.actions.Func;
import core.model.VirtualMachine;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.dasein.cloud.utils.requester.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static core.requests.Get.get;

/**
 * Created by vmunthiu on 2/16/2016.
 */
public class AzurePackGetVirtualMachinesAction implements Func<Iterable<VirtualMachine>> {
    private CloudProvider cloudProvider;

    public AzurePackGetVirtualMachinesAction(CloudProvider cloudProvider) {
        this.cloudProvider = cloudProvider;
    }
    @Override
    public Iterable<VirtualMachine> call() throws Exception {
        String uri = String.format("%s/%s/services/systemcenter/vmm/VirtualMachines?$expand=VirtualDiskDrives", this.cloudProvider.getEndpoint(), this.cloudProvider.getAccountNumber());
        return get(uri, getMapper(), WAPVirtualMachinesModel.class);
    }

    private ObjectMapper<WAPVirtualMachinesModel, Iterable<VirtualMachine>> getMapper(){
        final List<VirtualMachine> virtualMachines = new ArrayList<VirtualMachine>();
        final String regionId = this.cloudProvider.getRegionId();
        return new ObjectMapper<WAPVirtualMachinesModel, Iterable<VirtualMachine>>() {
            @Override
            public Iterable<VirtualMachine> mapFrom(WAPVirtualMachinesModel wapVirtualMachinesModel) {
                CollectionUtils.forAllDo(wapVirtualMachinesModel.getVirtualMachines(), new Closure() {
                    @Override
                    public void execute(Object input) {
                        WAPVirtualMachineModel virtualMachineModel = (WAPVirtualMachineModel) input;
                        if (regionId.equalsIgnoreCase(virtualMachineModel.getCloudId())) {
                            virtualMachines.add(virtualMachineFrom(virtualMachineModel));
                        }
                    }
                });
                return virtualMachines;
            }
        };
    }

    private VirtualMachine virtualMachineFrom(WAPVirtualMachineModel virtualMachineModel) {
        VirtualMachine virtualMachine = new VirtualMachine(virtualMachineModel.getId());
        return virtualMachine;
    }
}
