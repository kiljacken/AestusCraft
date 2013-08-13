package dk.kiljacken.aestuscraft.api.heat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

public class HeatNetworkImpl implements IHeatNetwork {
    private List<IHeatMachine> m_Machines;
    private List<IHeatConductor> m_Conductors;
    private int m_CurrentIndex = 0;

    public HeatNetworkImpl()
    {
        m_Machines = new ArrayList<>();
        m_Conductors = new ArrayList<>();
    }

    @Override
    public List<IHeatMachine> getConnectedMachines()
    {
        return m_Machines;
    }

    @Override
    public List<IHeatConductor> getConnectedConductors()
    {
        return m_Conductors;
    }

    @Override
    public void merge(IHeatNetwork network)
    {
        if (network == null || network == this)
        {
            return;
        }

        IHeatNetwork newNetwork = new HeatNetworkImpl();
        newNetwork.getConnectedConductors().addAll(getConnectedConductors());
        newNetwork.getConnectedConductors().addAll(network.getConnectedConductors());
        newNetwork.refresh();
    }

    @Override
    public void split(IHeatConductor conductor)
    {
        // Remove the conductor from this network
        m_Conductors.remove(conductor);

        // Clear lists to make avoid dead references
        m_Machines.clear();

        // Initialize flood fill queue
        Queue<IHeatConductor> floodQueue = new LinkedList<>();

        // Loop until the conductors in this network has been removed
        while (!m_Conductors.isEmpty())
        {
            // Get a conductor from this network and remove it. We get the last
            // one as it's best for performance
            floodQueue.add(m_Conductors.remove(m_Conductors.size() - 1));

            // Create a new network
            IHeatNetwork network = new HeatNetworkImpl();

            // Loop through all conductors connected to this one and add them to
            // the network
            while (!floodQueue.isEmpty())
            {
                IHeatConductor floodConductor = floodQueue.poll();

                // Add the conductor to the new network
                network.getConnectedConductors().add(floodConductor);
                floodConductor.setNetwork(network);

                // Add adjacent conductors to the queue if they're not already
                // in the new network
                for (IHeatConductor connectedConductor : floodConductor.getConnectedConductors())
                {
                    if (connectedConductor != conductor && !network.getConnectedConductors().contains(connectedConductor))
                    {
                        floodQueue.add(connectedConductor);
                    }
                }
            }

            network.refresh();
        }
    }

    // TODO: Split network refresh into conductor culling/parenting and machine polling
    @Override
    public void refresh()
    {
        Set<IHeatConductor> conductorSet = new HashSet<>(getConnectedConductors());
        getConnectedConductors().clear();
        getConnectedConductors().addAll(conductorSet);

        int size = getConnectedConductors().size() * 4;
        Set<IHeatMachine> machines = new HashSet<>(size);

        Iterator<IHeatConductor> iter = getConnectedConductors().iterator();

        while (iter.hasNext())
        {
            IHeatConductor conductor = iter.next();
            TileEntity conductorTile = (TileEntity) conductor;

            if (conductor == null || conductorTile.isInvalid())
            {
                iter.remove();
            }
            else
            {
                conductor.setNetwork(this);

                machines.addAll(conductor.getConnectedMachines());
            }
        }

        getConnectedMachines().clear();
        getConnectedMachines().addAll(machines);

        for (IHeatMachine machine : getConnectedMachines())
        {
            machine.setNetwork(this);
        }
    }

    @Override
    public float supplyHeat(float amount)
    {
        if (amount < 0.0f)
        {
            return 0.0f;
        }

        Collection<IHeatConsumer> consumers = (Collection<IHeatConsumer>) (Object) Collections2.filter(getConnectedMachines(),
                Predicates.instanceOf(IHeatConsumer.class));
        float consumed = supplyHeat(amount, consumers, 0);

        return consumed;
    }

    private float supplyHeat(float amount, Collection<IHeatConsumer> consumers, int depth)
    {
        if (depth >= 4)
        {
            return 0.0f;
        }

        float consumed = 0.0f;
        for (IHeatConsumer consumer : consumers)
        {
            consumed += consumer.supplyHeat(amount / consumers.size());
        }

        if (consumed < amount)
        {
            consumed += supplyHeat(amount - consumed, consumers, depth + 1);
        }

        return consumed;
    }
}
