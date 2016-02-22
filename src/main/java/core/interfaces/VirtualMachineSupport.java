package core.interfaces;

import com.sun.istack.internal.NotNull;
import core.model.VirtualMachine;
import org.apache.commons.collections.Predicate;

/**
 * Created by vmunthiu on 10/12/2015.
 */
public interface VirtualMachineSupport {
    public VirtualMachine launch(@NotNull VirtualMachine virtualMachine) throws Exception;
    public Iterable<VirtualMachine> getVirtualMachines() throws Exception;
    public Iterable<VirtualMachine> getVirtualMachines(Predicate predicateMatch) throws Exception;
    public VirtualMachine getVirtualMachineById(String virtualMachineProviderId) throws Exception;
}
