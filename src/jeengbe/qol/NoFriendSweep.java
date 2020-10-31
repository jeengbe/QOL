package jeengbe.qol;

import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

/**
 * Swipe-Attacking your own tamed animal does not damage it
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/4c0jyf/hey_mojang_can_you_please_make_it_so_that_the/}
 */
public class NoFriendSweep implements Listener {
  @EventHandler
  public void onSweep(EntityDamageByEntityEvent e) {
    if (e.getCause() != DamageCause.ENTITY_SWEEP_ATTACK)
      return;
    if (!(e.getDamager() instanceof Player))
      return;

    if (!(e.getEntity() instanceof Tameable))
      return;
    Tameable tam = (Tameable) e.getEntity();
    if (!tam.isTamed())
      return;
    if (!tam.getOwner().getUniqueId().equals(e.getDamager().getUniqueId()))
      return;
    e.setCancelled(true);
  }
}
