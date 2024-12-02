// copyright lololol
package lol.neoclient.gui;

import java.text.SimpleDateFormat;

import imgui.ImGui;
import imgui.flag.ImGuiTableColumnFlags;
import imgui.flag.ImGuiTableFlags;
import imgui.type.ImBoolean;
import lol.neoclient.NeoClient;
import lol.neoclient.NeoClient.PacketInfo;
import lol.neoclient.DebugRendererSettings;
import net.minecraft.network.NetworkSide;

public class NeoGui {
    ImBoolean showDemoWindow = new ImBoolean(false);
    ImBoolean showClientbound = new ImBoolean(true);
    ImBoolean showServerbound = new ImBoolean(true);
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    public void draw() {

        if (ImGui.begin("Debug")) {
            ImGui.checkbox("Enable Debug Renderers", DebugRendererSettings.enabled);
            ImGui.separator();
            ImGui.beginDisabled(!DebugRendererSettings.enabled.get());
            ImGui.checkbox("Pathfinding", DebugRendererSettings.showPathfindingDebugRenderer);
            ImGui.checkbox("Water", DebugRendererSettings.showWaterDebugRenderer);
            ImGui.checkbox("Heightmap", DebugRendererSettings.showHeightmapDebugRenderer);
            ImGui.checkbox("Collision", DebugRendererSettings.showCollisionDebugRenderer);
            ImGui.checkbox("Supporting Block", DebugRendererSettings.showSupportingBlockDebugRenderer);
            ImGui.checkbox("Neighbor Update", DebugRendererSettings.showNeighborUpdateDebugRenderer);
            ImGui.checkbox("Structure", DebugRendererSettings.showStructureDebugRenderer);
            ImGui.checkbox("Sky Light", DebugRendererSettings.showSkyLightDebugRenderer);
            ImGui.checkbox("World Gen Attempt", DebugRendererSettings.showWorldGenAttemptDebugRenderer);
            ImGui.checkbox("Block Outline", DebugRendererSettings.showBlockOutlineDebugRenderer);
            ImGui.checkbox("Chunk Loading", DebugRendererSettings.showChunkLoadingDebugRenderer);
            ImGui.checkbox("Village", DebugRendererSettings.showVillageDebugRenderer);
            ImGui.checkbox("Village Section", DebugRendererSettings.showVillageSectionsDebugRenderer);
            ImGui.checkbox("Bees", DebugRendererSettings.showBeeDebugRenderer);
            ImGui.checkbox("Raid Center", DebugRendererSettings.showRaidCenterDebugRenderer);
            ImGui.checkbox("Goal Selector", DebugRendererSettings.showGoalSelectorDebugRenderer);
            ImGui.checkbox("Game Event", DebugRendererSettings.showGameEventDebugRenderer);
            ImGui.checkbox("Light", DebugRendererSettings.showLightDebugRenderer);
            ImGui.checkbox("Breeze", DebugRendererSettings.showBreezeDebugRenderer);
            ImGui.endDisabled();
        }
        ImGui.end();

        ImGui.setNextWindowContentSize(0, NeoClient.packets.size() * ImGui.getTextLineHeight() + 100);
        if (ImGui.begin("Neo")) {
            ImGui.checkbox("Client", showServerbound);
            ImGui.sameLine();
            ImGui.checkbox("Server", showClientbound);
            ImGui.sameLine();
            if (ImGui.button("Clear")) {
                NeoClient.packets.clear();
            }
            ImGui.sameLine();
            ImGui.checkbox("Demo Window", showDemoWindow);

            int flags = ImGuiTableFlags.Borders | ImGuiTableFlags.RowBg | ImGuiTableFlags.Resizable;
            if (ImGui.beginTable("packets", 4, flags)) {
                
                ImGui.tableSetupColumn("Time", ImGuiTableColumnFlags.WidthFixed, 100);
                ImGui.tableSetupColumn("From", ImGuiTableColumnFlags.WidthFixed, 50);
                ImGui.tableSetupColumn("Type", ImGuiTableColumnFlags.WidthFixed, 250);
                ImGui.tableSetupColumn("Details");
                ImGui.tableHeadersRow();

                int i = 0;
                for (i = 0; i < NeoClient.packets.size(); i++) {
                    // TODO: Only show packets that are scrolled on screen, otherwise just move cursor
                    if (i > 600) //ImGui.getCursorPosY() >= ImGui.getScrollY() + ImGui.getContentRegionMaxY())
                        break;

                    PacketInfo info = NeoClient.packets.get(i);

                    // Filter packets
                    if (info.side == NetworkSide.SERVERBOUND && !showServerbound.get())
                        continue;
                    if (info.side == NetworkSide.CLIENTBOUND && !showClientbound.get())
                        continue;

                    ImGui.tableNextRow();

                    ImGui.tableNextColumn();
                    //ImGui.textUnformatted(dateFormat.format(new Date(info.time)));
                    ImGui.textUnformatted("" + ImGui.getCursorPosY() + " / ");

                    ImGui.tableNextColumn();
                    ImGui.textUnformatted(info.side == NetworkSide.SERVERBOUND ? "Client" : "Server");

                    ImGui.tableNextColumn();
                    ImGui.textUnformatted(info.name);

                    ImGui.tableNextColumn();
                    ImGui.textUnformatted(info.packet.toString());

                    ImGui.getTextLineHeight();
                }

                ImGui.endTable();
            }

        
        }
        ImGui.end();

        if (showDemoWindow.get())
            ImGui.showDemoWindow();
    }
}
