// copyright lololol
package lol.neoclient.gui;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import imgui.ImColor;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiDataType;
import imgui.flag.ImGuiHoveredFlags;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import imgui.type.ImDouble;
import imgui.type.ImInt;
import lol.neoclient.NeoClient;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import net.minecraft.client.MinecraftClient;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.screens.settings.BlockListSettingScreen;
import meteordevelopment.meteorclient.gui.screens.settings.EntityTypeListSettingScreen;
import meteordevelopment.meteorclient.gui.screens.settings.ItemListSettingScreen;
import meteordevelopment.meteorclient.gui.themes.meteor.MeteorGuiTheme;
import meteordevelopment.meteorclient.settings.BlockListSetting;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.EntityTypeListSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.ItemListSetting;
import meteordevelopment.meteorclient.systems.modules.Category;

public class NeoGui {
    ImBoolean showDemoWindow = new ImBoolean(false);
    ImBoolean showClientbound = new ImBoolean(true);
    ImBoolean showServerbound = new ImBoolean(true);
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    String longestModuleName = null;
    Set<String> openModules = new HashSet<>();

    // HACK
    public static float moduleOpenTime = 0;

    // Colors
    //-----------------------------------------------------------------------------

    public static int color(Color c) {
        return (c.r << 0) + (c.g << 8) + (c.b << 16) + (c.a << 24);
    }

    public static int bgColor() {
        MeteorGuiTheme theme = (MeteorGuiTheme)GuiThemes.get();
        return color(theme.accentColor.get());
    }

    public static float relativeLuminance(Color c) {
        float r = c.r <= 10.f ? (c.r / 3294f) : (float)Math.pow(c.r/269.f + 0.0513f, 2.4f);
        float g = c.g <= 10.f ? (c.g / 3294f) : (float)Math.pow(c.g/269.f + 0.0513f, 2.4f);
        float b = c.b <= 10.f ? (c.b / 3294f) : (float)Math.pow(c.b/269.f + 0.0513f, 2.4f);
        return 0.2126f * r + 0.7152f * g + 0.0722f * b;
    }

    public static float contrastRatio(float a, float b) {
        float l1 = Math.max(a, b);
        float l2 = Math.min(a, b);
        return (l1 + 0.05f) / (l2 + 0.05f);
    }

    public static float contrastRatio(Color a, Color b) {
        return contrastRatio(relativeLuminance(a), relativeLuminance(b));
    }

    public int textColor() {
        Color bg = ((MeteorGuiTheme)GuiThemes.get()).accentColor.get();
        float relativeLuminance = relativeLuminance(bg);
        float contrastRatioWhite = contrastRatio(relativeLuminance(Color.WHITE), relativeLuminance);
        float contrastRatioBlack = contrastRatio(relativeLuminance(Color.BLACK), relativeLuminance);
        if (contrastRatioWhite < contrastRatioBlack) {
            return 0xff000000; // black
        } else {
            return 0xffffffff; // white
        }
    }

    // Widgets
    //-----------------------------------------------------------------------------

    public void hotkey(Keybind key) {
		boolean waitingforkey = false;
		if (!waitingforkey) {
			if(ImGui.button(key.toString()))
				waitingforkey = true;
		}
		else if (waitingforkey) {
			ImGui.button("...");
		}
	}

    //-----------------------------------------------------------------------------

    public boolean beginModulePopup(Module module) {
        // Open if it should be
        if (openModules.contains(module.name)) {
            if (!ImGui.isPopupOpen(module.name))
                ImGui.openPopup(module.name);
            else
                openModules.remove(module.name);
        }

        // Open if right clicked last item
        if (ImGui.isMouseReleased(ImGuiMouseButton.Right) && ImGui.isItemHovered(ImGuiHoveredFlags.AllowWhenBlockedByPopup)) {
            ImGui.openPopup(module.name);
        }

        if (ImGui.isPopupOpen(module.name)) {
            ImVec2 pos = ImGui.getCursorScreenPos();
            ImGui.setNextWindowPos(pos.x, pos.y, ImGuiCond.Appearing);
            return ImGui.beginPopup(module.name, ImGuiWindowFlags.AlwaysAutoResize | ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoSavedSettings);
        }
        return false;
    }

    public void draw() {
        // Modules
        int borderColor = ImGui.getColorU32(ImGuiCol.Border);
        int pop = 3;
        ImGui.pushStyleColor(ImGuiCol.TitleBg, bgColor());
        ImGui.pushStyleColor(ImGuiCol.TitleBgActive, bgColor());
        ImGui.pushStyleColor(ImGuiCol.Border, 0);
        {
            // Figure out window width
            if (longestModuleName == null) {
                for (Module module : Modules.get().getAll()) {
                    if (longestModuleName == null || module.title.length() > longestModuleName.length())
                        longestModuleName = module.title;
                }
            }

            int x = 100;
            for (Category category : Modules.loopCategories()) {
                // Category window
                ImGui.setNextWindowPos(x, 300, ImGuiCond.FirstUseEver);
                ImGui.setNextWindowBgAlpha(0.75f);
                ImGui.setNextWindowSize(200, -1);
                ImGui.pushStyleColor(ImGuiCol.Text, textColor());
                if (ImGui.begin(category.name)) {
                    ImGui.popStyleColor(); // Text
                    
                    // Module
                    for (Module module : Modules.get().getGroup(category)) {
                        // Menu style
                        ImGui.pushStyleColor(ImGuiCol.PopupBg, ImGui.getColorU32(ImGuiCol.MenuBarBg));
                        ImGui.pushStyleColor(ImGuiCol.Border, borderColor);

                        // Selectable
                        boolean wasActive = module.isActive();
                        int headerColor = ImGui.getColorU32(ImGuiCol.Header);
                        ImGui.pushStyleColor(ImGuiCol.Header, wasActive ? bgColor() : ImGui.getColorU32(ImGuiCol.HeaderHovered));
                        ImGui.pushStyleColor(ImGuiCol.HeaderActive, bgColor());
                        ImGui.pushStyleColor(ImGuiCol.HeaderHovered, wasActive ? ImGui.getColorU32(ImGuiCol.HeaderActive) : headerColor);
                        if (wasActive)
                            ImGui.pushStyleColor(ImGuiCol.Text, textColor());

                        //TODO: Show keybind like with a menu item when active
                        if (wasActive
                            ? ImGui.selectable(module.title, wasActive)
                            : ImGui.menuItem(module.title, module.keybind.isSet() ? module.keybind.toString() : null))
                            module.toggle();
                        ImGui.popStyleColor(wasActive ? 4 : 3);

                        // Tooltip
                        if (ImGui.isItemHovered()) {
                            ImGui.beginTooltip();

                            ImGui.text(module.title);
                            ImGui.separator();

                            ImGui.pushStyleColor(ImGuiCol.Text, color(new Color(200, 200, 200)));
                            ImGui.pushTextWrapPos(ImGui.getCursorPosX() + 350);
                            ImGui.text(module.description);
                            ImGui.popTextWrapPos();
                            ImGui.popStyleColor();

                            ImGui.endTooltip();
                        }

                        // Module menu
                        if (beginModulePopup(module)) {
                            //ImGui.setCursorPosX(ImGui.getWindowWidth() - 36);
                            ImBoolean active = new ImBoolean(module.isActive());
                            if (ImGui.checkbox("##active", active))
                                module.toggle();
                            ImGui.sameLine();
                            ImGui.text(module.title);
                            ImGui.separator();
                            
                            ImGui.pushStyleColor(ImGuiCol.Text, color(new Color(200, 200, 200)));
                            ImGui.pushTextWrapPos(ImGui.getCursorPosX() + 350);
                            ImGui.textWrapped(module.description);
                            ImGui.popTextWrapPos();
                            ImGui.popStyleColor();

                            ImGui.spacing();
                            ImGui.spacing();
                            ImGui.text("Key");
                            ImGui.sameLine();
                            hotkey(module.keybind);
                            ImGui.spacing();
                            ImGui.spacing();

                            // FIXME: clusterfuck
                            for (var group : module.settings.groups) {
                                boolean open = true;
                                if (group.name != "General" || module.settings.groups.size() > 1)
                                    open = ImGui.collapsingHeader(group.name, ImGuiTreeNodeFlags.DefaultOpen);
                                if (open) {
                                    for (var setting : group) {
                                        if (!setting.isVisible())
                                            continue;

                                        if (setting.getClass() == DoubleSetting.class) {
                                            // Doubles
                                            DoubleSetting set = (DoubleSetting)setting;
                                            ImDouble d = new ImDouble(set.get());
                                            if (!set.noSlider) {
                                                if (ImGui.sliderScalar(setting.title, ImGuiDataType.Double, d, set.sliderMin, set.sliderMax))
                                                    set.set(d.get());
                                            } else {
                                                if (ImGui.dragScalar(setting.title, ImGuiDataType.Double, d, 0.1f, set.sliderMin, set.sliderMax))
                                                    set.set(d.get());
                                            }
                                        } else if (setting.getClass() == IntSetting.class) {
                                            // Ints
                                            IntSetting set = (IntSetting)setting;
                                            ImInt d = new ImInt(set.get());
                                            if (!set.noSlider) {
                                                if (ImGui.sliderScalar(setting.title, ImGuiDataType.S32, d, set.sliderMin, set.sliderMax))
                                                    set.set(d.get());
                                            } else {
                                                if (ImGui.dragScalar(setting.title, ImGuiDataType.S32, d, 1, set.sliderMin, set.sliderMax))
                                                    set.set(d.get());
                                            }
                                        } else if (setting.getClass() == BoolSetting.class) {
                                            // Bools
                                            BoolSetting set = (BoolSetting)setting;
                                            ImBoolean b = new ImBoolean(set.get());
                                            if (ImGui.checkbox(setting.title, b))
                                                set.set(b.get());
                                        } else if (setting.getClass() == EnumSetting.class) {
                                            // Enums
                                            var set = (EnumSetting<?>)setting;
                                            try {
                                                var fields = set.getClass().getDeclaredField("suggestions");
                                                fields.setAccessible(true);
                                                var suggestions = (java.util.List<String>)fields.get(set);
                                                if (ImGui.beginCombo(setting.title, set.get().toString())) {
                                                    for (String suggestion : suggestions) {
                                                        boolean selected = set.get().toString().equals(suggestion);
                                                        if (ImGui.selectable(suggestion, selected))
                                                            set.parse(suggestion);
                                                        if (selected)
                                                            ImGui.setItemDefaultFocus();
                                                    }
                                                    
                                                    ImGui.endCombo();
                                                }
                                            } catch (Exception e) {
                                                // fuck you
                                            }
                                        } else if (setting.getClass() == ColorSetting.class) {
                                            ColorSetting set = (ColorSetting)setting;
                                            SettingColor color = (SettingColor)set.get();
                                            float[] rgba = {color.r, color.g, color.b, color.a};
                                            if (ImGui.colorButton(setting.title, rgba))
                                                set.set(new SettingColor(rgba[0], rgba[1], rgba[2], rgba[3]));
                                            ImGui.sameLine();
                                            ImGui.text(setting.title);
                                        } else if (setting.getClass() == BlockListSetting.class) {
                                            BlockListSetting set = (BlockListSetting)setting;
                                            ImGui.text(setting.title); ImGui.sameLine();
                                            if (ImGui.button("Choose Blocks")) {
                                                MinecraftClient.getInstance().setScreen(new BlockListSettingScreen(GuiThemes.get(), set));
                                                openModules.add(module.name);
                                            }
                                        } else if (setting.getClass() == EntityTypeListSetting.class) {
                                            EntityTypeListSetting set = (EntityTypeListSetting)setting;
                                            ImGui.text(setting.title); ImGui.sameLine();
                                            if (ImGui.button("Choose Entities")) {
                                                MinecraftClient.getInstance().setScreen(new EntityTypeListSettingScreen(GuiThemes.get(), set));
                                                openModules.add(module.name);
                                            }
                                        } else if (setting.getClass() == ItemListSetting.class) {
                                            ItemListSetting set = (ItemListSetting)setting;
                                            ImGui.text(setting.title); ImGui.sameLine();
                                            if (ImGui.button("Choose Items")) {
                                                MinecraftClient.getInstance().setScreen(new ItemListSettingScreen(GuiThemes.get(), set));
                                                openModules.add(module.name);
                                            }
                                        } else {
                                            ImGui.text(setting.getClass().getSimpleName() + " " + setting.title);
                                        }
                                    }
                                }
                            }
                            ImGui.endPopup();
                        }

                        if (module.keybind.isSet()) {
                        }

                        ImGui.popStyleColor(2);
                    }
                    x += ImGui.getWindowWidth() + 5;
                }
                ImGui.end();
            }
        }
        ImGui.popStyleColor(pop);

        //ImGui.showDemoWindow();

        /*if (ImGui.begin("Debug Render", ImGuiWindowFlags.AlwaysAutoResize)) {
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
        ImGui.end();*/

        /*ImGui.setNextWindowContentSize(0, NeoClient.packets.size() * ImGui.getTextLineHeight() + 100);
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

                // TODO: Use a scroll view for le clipping
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
                    ImGui.textUnformatted(dateFormat.format(new Date(info.time)));

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
        ImGui.end();*/

        if (showDemoWindow.get())
            ImGui.showDemoWindow();
    }
}
