package jeengbe.qol;

import org.bukkit.Material;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.PlayerInventory;

/**
 * Shearing a dog will set it free
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/58bqps/a_dog_can_be_let_go_by_shearing_off_its_collar/}
 */
public class FreeDog implements Listener {
  @EventHandler
  public void onClick(PlayerInteractAtEntityEvent e) {
    if (!(e.getRightClicked() instanceof Wolf))
      return;
    Wolf wolf = (Wolf) e.getRightClicked();
    if (!wolf.isTamed())
      return;
    if (!wolf.getOwner().getUniqueId().equals(e.getPlayer().getUniqueId()))
      return;
    PlayerInventory inv = e.getPlayer().getInventory();
    if (inv.getItemInMainHand().getType() != Material.SHEARS)
      return;
    wolf.setTamed(false);
    wolf.setOwner(null);
    wolf.setSitting(false);
    e.getPlayer().swingMainHand();
  }
}
