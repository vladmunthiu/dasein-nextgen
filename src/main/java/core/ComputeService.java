package core;

import com.google.inject.Inject;
import core.interfaces.VirtualMachineSupport;

/**
 * Created by vmunthiu on 2/17/2016.
 */
public class ComputeService {
    private VirtualMachineSupport virtualMachineSupport;

    @Inject
    public ComputeService(VirtualMachineSupport virtualMachineSupport) {
        this.virtualMachineSupport = virtualMachineSupport;
    }

    public VirtualMachineSupport getVirtualMachineSupport() {
        return virtualMachineSupport;
    }
}
