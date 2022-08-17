package zone.possum.emigration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import net.fabricmc.loader.api.FabricLoader;
import zone.possum.emigration.farmersdelight.FarmersDelightPlugin;

public class EmigrationPlugin implements EmiPlugin {
    public static final Logger LOGGER = LoggerFactory.getLogger("emigration");

    @Override
    public void register(EmiRegistry emi) {
        FabricLoader loader = FabricLoader.getInstance();
        if (loader.isModLoaded("farmersdelight"))
            new FarmersDelightPlugin().register(emi);
    }
}
