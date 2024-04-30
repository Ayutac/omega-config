package draylar.omegaconfiggui.api.screen;

import draylar.omegaconfig.api.Config;
import draylar.omegaconfiggui.mixin.modmenu.ModMenuAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public class OmegaModMenu {

    public static <T extends Config> void injectScreen(T config, OmegaScreenFactory<Screen> factory) {
        // they will suspect nothing
        ModMenuAccessor.getConfigScreenFactories().put(config.getModid(), factory::get);
    }
}
