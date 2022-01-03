package community.leaf.examples.persistence;

import community.leaf.persistence.Persistent;
import community.leaf.persistence.PersistentNamespaceData;
import community.leaf.persistence.json.JsonPersistentDataContainer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.UUID;

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
        PersistentNamespaceData data = Persistent.namespace(plugin).data(hurt);
        
        HitCounter counter = data.getOrDefault("hit_counter", HitCounter.TYPE, HitCounter.NONE).hit(player);
        data.set("hit_counter", HitCounter.TYPE, counter);
        
        player.sendMessage("Ouch! " + hurt.getName() + " hits: " + ChatColor.RED + counter.hits());
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event)
    {
        Entity dead = event.getEntity();
        if (dead instanceof Player) { return; }
        
        @NullOr HitCounter counter = Persistent.namespace(plugin).data(dead).get("hit_counter", HitCounter.TYPE);
        if (counter == null) { return; }
        
        @NullOr UUID killerUuid = counter.lastHit();
        if (killerUuid == null) { return; }
        
        @NullOr Player killer = plugin.getServer().getPlayer(killerUuid);
        if (killer == null || !killer.isOnline()) { return; }
        
        killer.sendMessage(
            "You dealt the final blow! " + ChatColor.DARK_RED + dead.getName() +
            " died after " + counter.hits() + " hit(s)."
        );
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEntityClick(PlayerInteractEntityEvent event)
    {
        if (event.getHand() != EquipmentSlot.HAND) { return; }
        
        Player player = event.getPlayer();
        Entity clicked = event.getRightClicked();
        
        @NullOr HitCounter counter = Persistent.namespace(plugin).data(clicked).get("hit_counter", HitCounter.TYPE);
        if (counter == null) { return; }
        
        String json = JsonPersistentDataContainer.of(counter).toPrettyJsonString();
        
        player.sendMessage(ChatColor.GRAY + clicked.getName() + "'s hit counter:");
        player.sendMessage(ChatColor.GOLD + json);
    }
}
