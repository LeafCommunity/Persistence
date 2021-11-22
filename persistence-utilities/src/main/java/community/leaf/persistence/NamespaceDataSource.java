/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/Persistence>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.persistence;

import community.leaf.persistence.keys.Namespaced;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;

@FunctionalInterface
public interface NamespaceDataSource
{
	Namespaced namespace();
	
	default PersistentNamespaceData data(PersistentDataHolder holder)
	{
		return PersistentNamespaceData.of(namespace(), holder.getPersistentDataContainer());
	}
	
	default PersistentNamespaceData data(PersistentDataContainer container)
	{
		return PersistentNamespaceData.of(namespace(), container);
	}
	
	default PersistentBlockData data(Block block)
	{
		return PersistentBlockData.of(namespace(), block);
	}
}
