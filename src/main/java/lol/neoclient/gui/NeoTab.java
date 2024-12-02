// copyright lololol
package lol.neoclient.gui;

import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import meteordevelopment.meteorclient.pathing.PathManagers;
import meteordevelopment.meteorclient.utils.PreInit;
import net.minecraft.client.gui.screen.Screen;

public class NeoTab extends Tab {
    public NeoTab() {
        super("Neo");
    }

    @PreInit(dependencies = PathManagers.class)
    public static void init() {
        Tabs.add(new NeoTab());
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return new NeoScreen(theme, this);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof NeoScreen;
    }

    // Dummy screen for ImGui
    public static class NeoScreen extends TabScreen {
        public NeoScreen(GuiTheme theme, Tab tab) {
            super(theme, tab);
        }

        @Override
        public void initWidgets() {}
    }
}
