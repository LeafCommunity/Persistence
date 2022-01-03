package community.leaf.examples.persistence;

import community.leaf.persistence.Persistent;
import community.leaf.persistence.PersistentDataTypes;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.UUID;

import static community.leaf.examples.persistence.ExamplePersistencePlugin.key;

public class HitCounter implements Persistent<PersistentDataContainer, HitCounter>
{
    public static final HitCounter NONE = new HitCounter(null, 0);
    
    public static final PersistentDataType<PersistentDataContainer, HitCounter> TYPE =
        Persistent.dataType(
            PersistentDataContainer.class,
            HitCounter.class,
            (counter, context) ->
            {
                PersistentDataContainer container = context.newPersistentDataContainer();
                
                @NullOr UUID lastHit = counter.lastHit();
                if (lastHit != null) { container.set(key("last_hit"), PersistentDataTypes.UUID, lastHit); }
                
                container.set(key("hits"), PersistentDataTypes.INTEGER, counter.hits());
                
                return container;
            },
            (container, context) -> new HitCounter(
                container.get(key("last_hit"), PersistentDataTypes.UUID),
                container.get(key("hits"), PersistentDataTypes.INTEGER)
            )
        );
    
    private final @NullOr UUID lastHit;
    private final int hits;
    
    public HitCounter(@NullOr UUID lastHit, int hits)
    {
        this.lastHit = lastHit;
        this.hits = hits;
    }
    
    public @NullOr UUID lastHit() { return lastHit; }
    
    public int hits() { return hits; }
    
    @Override
    public PersistentDataType<PersistentDataContainer, HitCounter> persistentDataType()
    {
        return TYPE;
    }
    
    public HitCounter hit(Player player)
    {
        return new HitCounter(player.getUniqueId(), hits + 1);
    }
}
