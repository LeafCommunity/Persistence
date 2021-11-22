package community.leaf.examples.persistence;

import community.leaf.persistence.PersistentBlockData;
import community.leaf.persistence.PersistentDataTypes;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import pl.tlinkowski.annotation.basic.NullOr;

import java.util.UUID;

public class BlockPlaceListener implements Listener
{
	private final ExamplePersistencePlugin plugin;
	
	public BlockPlaceListener(ExamplePersistencePlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		PersistentBlockData data = PersistentBlockData.of(plugin, event.getBlockPlaced());
		data.set("placed_by", PersistentDataTypes.UUID, event.getPlayer().getUniqueId());
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event)
	{
		PersistentBlockData.of(plugin, event.getBlock()).remove("placed_by");
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onClickOnBlock(PlayerInteractEvent event)
	{
		if (event.getHand() != EquipmentSlot.HAND) { return; }
		
		@NullOr Block block = event.getClickedBlock();
		if (block == null || block.getType() == Material.AIR) { return; }
		
		PersistentBlockData data = PersistentBlockData.of(plugin, block);
		
		@NullOr UUID placedBy = data.get("placed_by", PersistentDataTypes.UUID);
		if (placedBy == null) { return; }
		
		event.getPlayer().sendMessage(
			block.getType() + " placed by: " + plugin.getServer().getOfflinePlayer(placedBy).getName()
		);
	}
}
