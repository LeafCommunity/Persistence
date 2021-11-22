package community.leaf.persistence;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public interface PersistentBlockData extends PersistentPluginData
{
	static String chunkKey(int x, int y, int z)
	{
		int chunkX = x & 0xF;
		int chunkZ = z & 0xF;
		return "block/x" + chunkX + "/y" + y + "/z" + chunkZ;
	}
	
	static String chunkKey(Block block)
	{
		return chunkKey(block.getX(), block.getY(), block.getZ());
	}
	
	static PersistentBlockData of(Plugin plugin, Block block)
	{
		return new PersistentBlockDataImpl(plugin, block);
	}
	
	Block getBlock();
	
	NamespacedKey getBlockKey();
	
	void removeAll();
	
	boolean exists();
}
