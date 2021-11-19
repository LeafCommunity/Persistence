/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/Persistence>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.persistence.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.persistence.PersistentDataContainer;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SuppressWarnings("NullableProblems")
public class PersistentJsonType
{
	private PersistentJsonType() { throw new UnsupportedOperationException(); }
	
	public static final JsonCompatiblePrimitive<Byte> BYTE =
		JsonCompatiblePrimitive.of(Byte.class, JsonElement::getAsByte, JsonPrimitiveSetter.number());
	
	public static final JsonCompatiblePrimitive<Short> SHORT =
		JsonCompatiblePrimitive.of(Short.class, JsonElement::getAsShort, JsonPrimitiveSetter.number());
	
	public static final JsonCompatiblePrimitive<Integer> INTEGER =
		JsonCompatiblePrimitive.of(Integer.class, JsonElement::getAsInt, JsonPrimitiveSetter.number());
	
	public static final JsonCompatiblePrimitive<Long> LONG =
		JsonCompatiblePrimitive.of(Long.class, JsonElement::getAsLong, JsonPrimitiveSetter.number());
	
	public static final JsonCompatiblePrimitive<Float> FLOAT =
		JsonCompatiblePrimitive.of(Float.class, JsonElement::getAsFloat, JsonPrimitiveSetter.number());
	
	public static final JsonCompatiblePrimitive<Double> DOUBLE =
		JsonCompatiblePrimitive.of(Double.class, JsonElement::getAsDouble, JsonPrimitiveSetter.number());
	
	public static final JsonCompatiblePrimitive<String> STRING =
		JsonCompatiblePrimitive.of(String.class, JsonElement::getAsString, JsonObject::addProperty);
	
	public static final JsonCompatiblePrimitive<byte[]> BYTE_ARRAY =
		new JsonCompatiblePrimitive<>(byte[].class)
		{
			@Override
			public byte[] getFromJson(JsonElement element)
			{
				JsonArray array = element.getAsJsonArray();
				byte[] bytes = new byte[array.size()];
				for (int i = 0; i < array.size(); i++) { bytes[i] = array.get(i).getAsByte(); }
				return bytes;
			}
			
			@Override
			public void setInJson(JsonObject object, String key, byte[] primitive)
			{
				JsonArray array = new JsonArray();
				for (byte b : primitive) { array.add(b); }
				object.add(key, array);
			}
		};
	
	public static final JsonCompatiblePrimitive<int[]> INTEGER_ARRAY =
		new JsonCompatiblePrimitive<>(int[].class)
		{
			@Override
			public int[] getFromJson(JsonElement element)
			{
				JsonArray array = element.getAsJsonArray();
				int[] integers = new int[array.size()];
				for (int i = 0; i < array.size(); i++) { integers[i] = array.get(i).getAsInt(); }
				return integers;
			}
			
			@Override
			public void setInJson(JsonObject object, String key, int[] primitive)
			{
				JsonArray array = new JsonArray();
				for (int i : primitive) { array.add(i); }
				object.add(key, array);
			}
		};
	
	public static final JsonCompatiblePrimitive<long[]> LONG_ARRAY =
		new JsonCompatiblePrimitive<>(long[].class)
		{
			@Override
			public long[] getFromJson(JsonElement element)
			{
				JsonArray array = element.getAsJsonArray();
				long[] longs = new long[array.size()];
				for (int i = 0; i < array.size(); i++) { longs[i] = array.get(i).getAsLong(); }
				return longs;
			}
			
			@Override
			public void setInJson(JsonObject object, String key, long[] primitive)
			{
				JsonArray array = new JsonArray();
				for (long l : primitive) { array.add(l); }
				object.add(key, array);
			}
		};
	
	public static final JsonCompatiblePrimitive<PersistentDataContainer> TAG_CONTAINER =
		JsonCompatiblePrimitive.of(
			PersistentDataContainer.class,
			(element) -> new JsonPersistentDataContainer(element.getAsJsonObject()),
			(object, key, primitive) -> object.add(key, ((JsonPersistentDataContainer) primitive).json())
		);
	
	public static final JsonCompatiblePrimitive<PersistentDataContainer[]> TAG_CONTAINER_ARRAY =
		new JsonCompatiblePrimitive<>(PersistentDataContainer[].class)
		{
			@Override
			public PersistentDataContainer[] getFromJson(JsonElement element)
			{
				List<PersistentDataContainer> containers = new ArrayList<>();
				
				for (JsonElement item : element.getAsJsonArray())
				{
					containers.add(PersistentJsonType.TAG_CONTAINER.getFromJson(item));
				}
				
				return containers.toArray(PersistentDataContainer[]::new);
			}
			
			@Override
			public void setInJson(JsonObject object, String key, PersistentDataContainer[] primitive)
			{
				JsonArray array = new JsonArray();
				
				// TODO: accept original PDC too?
				Arrays.stream(primitive)
					.map(container -> (JsonPersistentDataContainer) container)
					.map(JsonPersistentDataContainer::json)
					.forEach(array::add);
				
				object.add(key, array);
			}
		};
	
	public static final Map<Class<?>, JsonCompatiblePrimitive<?>> TYPES;
	
	static
	{
		Map<Class<?>, JsonCompatiblePrimitive<?>> compatible = new LinkedHashMap<>();
		
		Arrays.stream(PersistentJsonType.class.getDeclaredFields())
			.filter(field -> Modifier.isStatic(field.getModifiers()))
			.filter(field -> JsonCompatiblePrimitive.class.isAssignableFrom(field.getType()))
			.flatMap(field -> {
				try { return Stream.of((JsonCompatiblePrimitive<?>) field.get(null)); }
				catch (IllegalAccessException e) { return Stream.empty(); }
			})
			.forEach(primitive -> compatible.put(primitive.getPrimitiveType(), primitive));
		
		TYPES = Map.copyOf(compatible);
	}
}
