package jeengbe.qol;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Named items do not despawn natuarlly on death
 */
public class KeepNamed implements Listener {
  @EventHandler
  public void onDeath(PlayerDeathEvent e) {
    List<ItemStack> live = new ArrayList<>();
    for (ItemStack item : e.getDrops()) {
      if (item.hasItemMeta()) {
        ItemMeta meta = item.getItemMeta();
        if (meta.hasDisplayName()) {
          live.add(item);
        }
      }
    }
    e.getDrops().removeAll(live);

    for (ItemStack item : live) {
      Item i = e.getEntity().getLocation().getWorld().dropItemNaturally(e.getEntity().getLocation(), item);
      i.setTicksLived(-32768);
    }
  }
}
