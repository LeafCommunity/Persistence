package community.leaf.examples.persistence;

import community.leaf.persistence.Persistent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.UUID;

import static community.leaf.examples.persistence.ExamplePersistencePlugin.key;

public class HitListener implements Listener
{
	private final ExamplePersistencePlugin plugin;
	
	public HitListener(ExamplePersistencePlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onHitEntity(EntityDamageByEntityEvent event)
	{
		Entity hurt = event.getEntity();
		if (hurt instanceof Player) { return; }
		
		Entity damager = event.getDamager();
		if (!(damager instanceof Player)) { return; }
		
		Player player = (Player) damager;
		PersistentDataContainer data = Persistent.container(hurt);
		
		HitCounter counter = data.getOrDefault(key("hit_counter"), HitCounter.TYPE, HitCounter.NONE).hit(player);
		data.set(key("hit_counter"), HitCounter.TYPE, counter);
		
		player.sendMessage(hurt.getName() + " hit " + counter.hits() + " time(s)!");
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event)
	{
		Entity dead = event.getEntity();
		if (dead instanceof Player) { return; }
		
		@NullOr HitCounter counter = Persistent.container(dead).get(key("hit_counter"), HitCounter.TYPE);
		if (counter == null) { return; }
		
		@NullOr UUID killerUuid = counter.lastHit();
		if (killerUuid == null) { return; }
		
		@NullOr Player killer = plugin.getServer().getPlayer(killerUuid);
		if (killer == null || !killer.isOnline()) { return; }
		
		killer.sendMessage("You dealt the final blow! " + dead.getName() + " was killed after " + counter.hits() + " hit(s).");
	}
}
