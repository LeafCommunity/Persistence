package community.leaf.persistence;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;

final class PersistentBlockDataImpl extends ProxiedPersistentDataContainer implements PersistentBlockData
{
	private final Plugin plugin;
	private final Block block;
	private final NamespacedKey key;
	
	private @NullOr PersistentDataContainer container;
	
	PersistentBlockDataImpl(Plugin plugin, Block block)
	{
		this.plugin = plugin;
		this.block = block;
		this.key = new NamespacedKey(plugin, PersistentBlockData.chunkKey(block));
	}
	
	@Override
	public Plugin plugin() { return plugin; }
	
	@Override
	public Block getBlock() { return block; }
	
	@Override
	public NamespacedKey getBlockKey() { return key; }
	
	private PersistentDataContainer chunkDataContainer()
	{
		return block.getChunk().getPersistentDataContainer();
	}
	
	@Override
	public PersistentDataContainer container()
	{
		if (container != null) { return container; }
		
		PersistentDataContainer chunkData = chunkDataContainer();
		@NullOr PersistentDataContainer blockData = chunkData.get(key, PersistentDataTypes.TAG_CONTAINER);
		
		return container = (blockData != null) ? blockData : chunkData.getAdapterContext().newPersistentDataContainer();
	}
	
	@Override
	public void removeAll()
	{
		chunkDataContainer().remove(key);
		container = null;
	}
	
	@Override
	public boolean exists()
	{
		return chunkDataContainer().has(key, PersistentDataTypes.TAG_CONTAINER);
	}
	
	private void update()
	{
		if (container == null || container.isEmpty()) { removeAll(); }
		else { chunkDataContainer().set(key, PersistentDataTypes.TAG_CONTAINER, container); }
	}
	
	@Override
	public <T, Z> void set(NamespacedKey key, PersistentDataType<T, Z> type, Z value)
	{
		super.set(key, type, value);
		update();
	}
	
	@Override
	public void remove(NamespacedKey key)
	{
		super.remove(key);
		update();
	}
}
