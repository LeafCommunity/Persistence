package community.leaf.persistence;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;

public interface PersistentPluginData extends PersistentDataContainer
{
	static PersistentPluginData of(Plugin plugin, PersistentDataContainer container)
	{
		return new PersistentPluginDataImpl(plugin, container);
	}
	
	static PersistentPluginData of(Plugin plugin, PersistentDataHolder holder)
	{
		return of(plugin, Objects.requireNonNull(holder, "holder").getPersistentDataContainer());
	}
	
	Plugin plugin();
	
	default <T, Z> void set(String key, PersistentDataType<T, Z> type, Z value)
	{
		set(new NamespacedKey(plugin(), key), type, value);
	}
	
	default <T, Z> boolean has(String key, PersistentDataType<T, Z> type)
	{
		return has(new NamespacedKey(plugin(), key), type);
	}
	
	default <T, Z> @NullOr Z get(String key, PersistentDataType<T, Z> type)
	{
		return get(new NamespacedKey(plugin(), key), type);
	}
	
	default <T, Z> Z getOrDefault(String key, PersistentDataType<T, Z> type, Z defaultValue)
	{
		return getOrDefault(new NamespacedKey(plugin(), key), type, defaultValue);
	}
	
	default void remove(String key)
	{
		remove(new NamespacedKey(plugin(), key));
	}
}
