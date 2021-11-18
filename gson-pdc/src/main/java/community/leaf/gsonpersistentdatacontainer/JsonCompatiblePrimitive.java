/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/GsonPersistentDataContainer>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.gsonpersistentdatacontainer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

@SuppressWarnings("NullableProblems")
public abstract class JsonCompatiblePrimitive<T> implements JsonPrimitiveGetter<T>, JsonPrimitiveSetter<T>, PersistentDataType<T, T>
{
	static <T> JsonCompatiblePrimitive<T> of(Class<T> type, JsonPrimitiveGetter<T> getter, JsonPrimitiveSetter<T> setter)
	{
		Objects.requireNonNull(getter, "getter");
		Objects.requireNonNull(setter, "setter");
		
		return new JsonCompatiblePrimitive<>(type)
		{
			@Override
			public T getFromJson(JsonElement element) { return getter.getFromJson(element); }
			
			@Override
			public void setInJson(JsonObject object, String key, T primitive) { setter.setInJson(object, key, primitive); }
		};
	}
	
	private final Class<T> primitiveType;
	
	JsonCompatiblePrimitive(Class<T> primitiveType)
	{
		this.primitiveType = Objects.requireNonNull(primitiveType, "primitiveType");
	}
	
	@Override
	public Class<T> getPrimitiveType() { return primitiveType; }
	
	@Override
	public Class<T> getComplexType() { return primitiveType; }
	
	@Override
	public T toPrimitive(T complex, PersistentDataAdapterContext context) { return complex; }
	
	@Override
	public T fromPrimitive(T primitive, PersistentDataAdapterContext context) { return primitive; }
}
