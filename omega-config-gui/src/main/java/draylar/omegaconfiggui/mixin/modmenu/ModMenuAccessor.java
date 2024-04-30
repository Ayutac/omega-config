package draylar.omegaconfiggui.mixin.modmenu;

import com.terraformersmc.modmenu.ModMenu;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(ModMenu.class)
public interface ModMenuAccessor {

    @Accessor(remap = false)
    static Map<String, ConfigScreenFactory<?>> getConfigScreenFactories() {
        throw new UnsupportedOperationException();
    }

}
