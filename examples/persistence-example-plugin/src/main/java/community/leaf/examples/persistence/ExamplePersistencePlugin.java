package community.leaf.examples.persistence;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class ExamplePersistencePlugin extends JavaPlugin
{
    private static ExamplePersistencePlugin instance()
    {
        return JavaPlugin.getPlugin(ExamplePersistencePlugin.class);
    }
    
    public static NamespacedKey key(String key)
    {
        return new NamespacedKey(instance(), key);
    }
    
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        getServer().getPluginManager().registerEvents(new HitListener(this), this);
    }
}
