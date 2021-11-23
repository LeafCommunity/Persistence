/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/Persistence>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.persistence;

import community.leaf.persistence.keys.Namespaced;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.stream.Stream;

public interface PersistentChunkData extends PersistentNamespaceData
{
	static PersistentChunkData of(Namespaced namespace, Chunk chunk)
	{
		return new PersistentChunkDataImpl(namespace, chunk);
	}
	
	Chunk chunk();
	
	default Stream<NamespacedKey> blockDataKeys()
	{
		return keysInNamespace().filter(key ->
			PersistentBlockData.CHUNK_KEY_PATTERN.matcher(key.getKey()).matches()
		);
	}
	
	default Stream<PersistentBlockData> blockData()
	{
		return keysInNamespace()
			.map(key ->
			{
				Matcher matcher = PersistentBlockData.CHUNK_KEY_PATTERN.matcher(key.getKey());
				if (!matcher.matches()) { return null; }
				
				int x = Integer.parseInt(matcher.group("x"));
				int y = Integer.parseInt(matcher.group("y"));
				int z = Integer.parseInt(matcher.group("z"));
				
				return PersistentBlockData.of(namespace(), chunk().getBlock(x, y, z));
			})
			.filter(Objects::nonNull);
	}
}
