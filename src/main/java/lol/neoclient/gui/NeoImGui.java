// copyright lololol
package lol.neoclient.gui;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import lol.neoclient.NeoClient;
import meteordevelopment.meteorclient.utils.render.FontUtils;

import static meteordevelopment.meteorclient.MeteorClient.mc;

import org.lwjgl.glfw.GLFW;


public class NeoImGui {
    static ImGuiImplGl3 imGuiGl = new ImGuiImplGl3();
    static ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();

    public static void init() {
        ImGui.createContext();

        try {
            var stream = FontUtils.class.getResourceAsStream("/assets/neoclient/fonts/Minecraftia.ttf");
            byte[] data = stream.readAllBytes();
            ImGui.getIO().getFonts().addFontFromMemoryTTF(data, 22);
        } catch (Exception e) {
            NeoClient.LOG.warn("Failed to load Minecraftia.ttf, using default ImGui font.");
        }

        // Enable viewports
        ImGui.getIO().addConfigFlags(ImGuiConfigFlags.ViewportsEnable);

        // Attach to window and OpenGL
        imGuiGlfw.init(mc.getWindow().getHandle(), true);
        imGuiGl.init();
    }

    public static void beginFrame() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    public static void render() {
        // Render ImGui
        ImGui.render();
        imGuiGl.renderDrawData(ImGui.getDrawData());

        // Render extra windows
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
    }

    public static void shutdown() {
        imGuiGl.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }
}
