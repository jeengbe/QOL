package jeengbe.qol;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Feather Falling prevents trampling Farm Land
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/9drs9h/feather_falling_should_prevent_farmland_from/}
 */
public class NoFeatherTrampleListener implements Listener {
  @EventHandler
  public void onTrample(PlayerInteractEvent e) {
    if (e.getAction() != Action.PHYSICAL)
      return;
    if (e.getClickedBlock().getType() != Material.FARMLAND)
      return;
    Player p = e.getPlayer();
    ItemStack boots = p.getInventory().getBoots();
    if (boots == null)
      return;
    ItemMeta bootsMeta = boots.getItemMeta();
    if (!bootsMeta.hasEnchant(Enchantment.PROTECTION_FALL))
      return;
    e.setCancelled(true);
  }
}
