// copyright lololol
package lol.neoclient.gui;

import imgui.ImGui;
import imgui.assertion.ImAssertCallback;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import lol.neoclient.NeoClient;
import meteordevelopment.meteorclient.utils.render.FontUtils;

import static meteordevelopment.meteorclient.MeteorClient.mc;

import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.glfw.GLFW;


public class NeoImGui {
    static ImGuiImplGl3 imGuiGl = new ImGuiImplGl3();
    static ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();

    static {
        System.setProperty("imgui.library.path", System.getProperty("java.io.tmpdir"));
        System.setProperty("imgui.library.name", "imgui-java64.dll");
        InputStream stream = NeoImGui.class.getResourceAsStream("/assets/neoclient/imgui-java64.dll");
        try {
            byte[] data = stream.readAllBytes();
            // write DLL
            String path = System.getProperty("java.io.tmpdir") + "/imgui-java64.dll";
            java.nio.file.Files.write(java.nio.file.Paths.get(path), data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void init() {
        ImGui.createContext();

        try {
            var stream = FontUtils.class.getResourceAsStream("/assets/neoclient/fonts/Minecraftia.ttf");
            byte[] data = stream.readAllBytes();
            ImGui.getIO().getFonts().addFontFromMemoryTTF(data, 22);
        } catch (Exception e) {
            NeoClient.LOG.warn("Failed to load Minecraftia.ttf, using default ImGui font.");
        }

        // Set assertion callback
        ImGui.setAssertCallback(new ImAssertCallback() {
            @Override
            public void imAssertCallback(String assertion, int line, String file) {
                NeoClient.LOG.error("ImGui assertion failed: " + file + "(" + line + "): " + assertion);
            }
        });

        // Enable viewports
        //ImGui.getIO().addConfigFlags(ImGuiConfigFlags.ViewportsEnable);

        //ImGui.getStyle().setWindowRounding(6.f);

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
