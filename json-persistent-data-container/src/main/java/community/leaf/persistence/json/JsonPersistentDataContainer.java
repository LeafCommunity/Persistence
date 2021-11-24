/*
 * Copyright © 2021, RezzedUp <https://github.com/LeafCommunity/Persistence>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package community.leaf.persistence.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import community.leaf.persistence.Persistent;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("NullableProblems")
public class JsonPersistentDataContainer implements PersistentDataContainer
{
	private static final Gson PRETTY_PRINTER = new GsonBuilder().setPrettyPrinting().create();
	
	private static final JsonParser PARSER = new JsonParser();
	
	@SuppressWarnings("unchecked")
	public static JsonPersistentDataContainer of(PersistentDataContainer container)
	{
		Objects.requireNonNull(container, "container");
		if (container instanceof JsonPersistentDataContainer) { return (JsonPersistentDataContainer) container; }
		
		JsonPersistentDataContainer json = new JsonPersistentDataContainer();
		
		conversion: for (NamespacedKey key : container.getKeys())
		{
			for (JsonCompatiblePrimitive<?> type : PersistentJsonType.TYPES.values())
			{
				if (container.has(key, type))
				{
					@NullOr Object value = container.get(key, type);
					
					if (value != null)
					{
						json.set(key, (PersistentDataType<Object, Object>) type, value);
						continue conversion;
					}
				}
			}
		}
		
		return json;
	}
	
	public static <Z> JsonPersistentDataContainer of(PersistentDataType<PersistentDataContainer, Z> type, Z complex)
	{
		Objects.requireNonNull(type, "type");
		Objects.requireNonNull(complex, "complex");
		
		return of(type.toPrimitive(complex, JsonPersistentDataContainer::new));
	}
	
	public static <Z extends Persistent<PersistentDataContainer, Z>> JsonPersistentDataContainer of(Z complex)
	{
		Objects.requireNonNull(complex, "complex");
		return of(complex.persistentDataType(), complex);
	}
	
	public static JsonPersistentDataContainer fromJsonString(String json)
	{
		Objects.requireNonNull(json, "json");
		return new JsonPersistentDataContainer(PARSER.parse(json).getAsJsonObject());
	}
	
	public static <Z> Z fromJsonString(PersistentDataType<PersistentDataContainer, Z> type, String json)
	{
		Objects.requireNonNull(type, "type");
		Objects.requireNonNull(json, "json");
		
		return type.fromPrimitive(fromJsonString(json), JsonPersistentDataContainer::new);
	}
	
	private final JsonObject json;
	
	public JsonPersistentDataContainer(JsonObject json)
	{
		this.json = Objects.requireNonNull(json, "json");
	}
	
	public JsonPersistentDataContainer()
	{
		this(new JsonObject());
	}
	
	public JsonObject json() { return json; }
	
	public String toJsonString() { return json.getAsString(); }
	
	public String toPrettyJsonString() { return PRETTY_PRINTER.toJson(json); }
	
	@SuppressWarnings("unchecked")
	private <T> JsonCompatiblePrimitive<T> primitive(PersistentDataType<T, ?> type)
	{
		@NullOr JsonCompatiblePrimitive<T> primitive =
			(JsonCompatiblePrimitive<T>) PersistentJsonType.TYPES.get(type.getPrimitiveType());
		
		if (primitive == null)
		{
			throw new IllegalArgumentException("Unsupported primitive type: " + type.getPrimitiveType());
		}
		
		return primitive;
	}
	
	
	@Override
	public <T, Z> void set(NamespacedKey key, PersistentDataType<T, Z> type, Z value)
	{
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(type, "type");
		Objects.requireNonNull(value, "value");
		
		JsonCompatiblePrimitive<T> primitive = primitive(type);
		T converted = type.toPrimitive(value, getAdapterContext());
		primitive.setInJson(json, key.toString(), converted);
	}
	
	@Override
	public <T, Z> boolean has(NamespacedKey key, PersistentDataType<T, Z> type)
	{
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(type, "type");
		
		JsonCompatiblePrimitive<T> primitive = primitive(type);
		@NullOr JsonElement existing = json.get(key.toString());
		return existing != null && primitive.isInstance(existing);
	}
	
	@Override
	public @NullOr <T, Z> Z get(NamespacedKey key, PersistentDataType<T, Z> type)
	{
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(type, "type");
		
		JsonCompatiblePrimitive<T> primitive = primitive(type);
		
		@NullOr JsonElement element = json.get(key.toString());
		if (element == null) { return null; }
		
		if (!primitive.isInstance(element))
		{
			throw new IllegalArgumentException(
				"Existing element is incompatible with requested type " +
				primitive.getPrimitiveType().getSimpleName() + ": " + element
			);
		}
		
		return type.fromPrimitive(primitive.getFromJson(element), getAdapterContext());
	}
	
	@Override
	public <T, Z> Z getOrDefault(NamespacedKey key, PersistentDataType<T, Z> type, Z defaultValue)
	{
		@NullOr Z z = get(key, type);
		return (z != null) ? z : defaultValue;
	}
	
	@Override
	public Set<NamespacedKey> getKeys()
	{
		return json.entrySet().stream()
			.map(Map.Entry::getKey)
			.map(key -> {
				String[] parts = key.split(":", 2);
				return (parts.length != 2) ? null : new NamespacedKey(parts[0], parts[1]);
			})
		   	.filter(Objects::nonNull)
			.collect(Collectors.toSet());
	}
	
	@Override
	public void remove(NamespacedKey key)
	{
		json.remove(Objects.requireNonNull(key, "key").toString());
	}
	
	@Override
	public boolean isEmpty()
	{
		return json.size() == 0;
	}
	
	@Override
	public PersistentDataAdapterContext getAdapterContext()
	{
		return JsonPersistentDataContainer::new;
	}
}
