package community.leaf.persistence;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

final class PersistentPluginDataImpl extends ProxiedPersistentDataContainer implements PersistentPluginData
{
	private final Plugin plugin;
	private final PersistentDataContainer container;
	
	PersistentPluginDataImpl(Plugin plugin, PersistentDataContainer container)
	{
		this.plugin = Objects.requireNonNull(plugin, "plugin");
		this.container = Objects.requireNonNull(container, "container");
	}
	
	@Override
	public Plugin plugin() { return plugin; }
	
	@Override
	protected PersistentDataContainer container() { return container; }
}
