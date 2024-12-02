// copyright lololol
package lol.neoclient;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;
import lol.neoclient.gui.NeoGui;
import lol.neoclient.gui.NeoImGui;
import lol.neoclient.gui.NeoTab;

import static meteordevelopment.meteorclient.MeteorClient.mc;

import java.util.Vector; // thread safe

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeoClient extends MeteorAddon {

    public class PacketInfo {
        public String name;
        public NetworkSide side;
        public long time;
        public Packet<?> packet;
    };

    public static final Logger LOG = LoggerFactory.getLogger("Neo Client");
    public static NeoGui gui = new NeoGui();
    public static Vector<PacketInfo> packets = new Vector<>();

    @Override
    public void onInitialize() {
        name = "Neo";
        authors = new String[] {"Alpyne"};

        NeoImGui.init();

        // Dispose on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NeoImGui.shutdown();
        }));

        LOG.info("Initialized Neo Client");

        MeteorClient.EVENT_BUS.subscribe(this);
    }

    @Override
    public String getPackage() {
        return "lol.neoclient";
    }

    @EventHandler
    private void onRender(Render2DEvent event) {
        if (Utils.isLoading()) return;

        NeoImGui.beginFrame();

        // Do GUI
        if (mc.currentScreen instanceof NeoTab.NeoScreen)
        {
            gui.draw();
        }

        NeoImGui.render();
    }

    static String getPacketName(Packet<?> packet) {
        var id = packet.getPacketId().id();
        if (id.getNamespace() == Identifier.DEFAULT_NAMESPACE) {
            // Don't include namespace if it's just "minecraft:"
            return id.getPath();
        } else {
            return id.toString();
        }
    }

    void addPacket(Packet<?> packet, boolean fromClient) {
        final long time = java.lang.System.currentTimeMillis();

        NetworkSide side = packet.getPacketId().side();
        assert fromClient ? (side == NetworkSide.SERVERBOUND) : (side == NetworkSide.CLIENTBOUND);

        PacketInfo info = new PacketInfo();
        info.name = getPacketName(packet);
        info.side = side;
        info.time = time;
        info.packet = packet;

        packets.add(info);
    }

    @EventHandler
    void onReceivePacket(PacketEvent.Receive event) {
        addPacket(event.packet, false);
    }

    @EventHandler
    void onSendPacket(PacketEvent.Sent event) {
        addPacket(event.packet, true);
    }
}
