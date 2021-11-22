/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/Persistence>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.persistence;

import community.leaf.persistence.keys.Namespaced;
import community.leaf.persistence.keys.PluginNamespace;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public interface PersistentBlockData extends PersistentNamespaceData
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
	
	static PersistentBlockData of(Namespaced namespace, Block block)
	{
		return new PersistentBlockDataImpl(namespace, block);
	}
	
	static PersistentBlockData of(Plugin plugin, Block block)
	{
		return of(PluginNamespace.of(plugin), block);
	}
	
	Block getBlock();
	
	NamespacedKey getBlockKey();
	
	void removeAll();
	
	boolean exists();
}
