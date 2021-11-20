/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/Persistence>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.persistence;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Persistent<T, Z extends Persistent<T, Z>>
{
	@SuppressWarnings("NullableProblems")
	static <T, Z> PersistentDataType<T, Z> dataType(
		Class<T> primitiveType,
		Class<Z> complexType,
		BiFunction<Z, PersistentDataAdapterContext, T> toPrimitive,
		BiFunction<T, PersistentDataAdapterContext, Z> fromPrimitive
	) {
		return new PersistentDataType<>()
		{
			@Override
			public Class<T> getPrimitiveType() { return primitiveType; }
			
			@Override
			public Class<Z> getComplexType() { return complexType; }
			
			@Override
			public T toPrimitive(Z complex, PersistentDataAdapterContext context) { return toPrimitive.apply(complex, context); }
			
			@Override
			public Z fromPrimitive(T primitive, PersistentDataAdapterContext context) { return fromPrimitive.apply(primitive, context); }
		};
	}
	
	static <T, Z> PersistentDataType<T, Z> dataType(
		Class<T> primitiveType,
		Class<Z> complexType,
		Function<Z, T> toPrimitive,
		Function<T, Z> fromPrimitive
	) {
		return dataType(
			primitiveType,
			complexType,
			(complex, ignored) -> toPrimitive.apply(complex),
			(primitive, ignored) -> fromPrimitive.apply(primitive)
		);
	}
	
	static PersistentDataContainer container(PersistentDataHolder holder)
	{
		return holder.getPersistentDataContainer();
	}
	
	PersistentDataType<T, Z> persistentDataType();
	
	@SuppressWarnings("unchecked")
	default T toPersistentData(PersistentDataAdapterContext context)
	{
		return persistentDataType().toPrimitive((Z) this, context);
	}
}
