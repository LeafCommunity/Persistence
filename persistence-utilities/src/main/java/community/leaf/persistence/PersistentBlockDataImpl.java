/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/Persistence>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.persistence;

import community.leaf.persistence.keys.Namespaced;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Objects;

final class PersistentBlockDataImpl extends ProxiedPersistentDataContainer implements PersistentBlockData
{
	private final Namespaced namespace;
	private final Block block;
	private final NamespacedKey key;
	
	private @NullOr PersistentDataContainer container;
	
	PersistentBlockDataImpl(Namespaced namespace, Block block)
	{
		this.namespace = Objects.requireNonNull(namespace, "namespace");
		this.block = block;
		this.key = namespace.key(PersistentBlockData.chunkKey(block));
	}
	
	@Override
	public Namespaced namespace() { return namespace; }
	
	@Override
	public Block getBlock() { return block; }
	
	@Override
	public NamespacedKey getBlockKey() { return key; }
	
	private PersistentDataContainer chunkDataContainer()
	{
		return block.getChunk().getPersistentDataContainer();
	}
	
	@Override
	protected PersistentDataContainer container()
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
