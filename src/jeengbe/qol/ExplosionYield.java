package jeengbe.qol;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * Explosions yield 100% of destroyed blocks
 */
public class ExplosionYield implements Listener {
  @EventHandler
  public void onExplode(EntityExplodeEvent e) {
    e.setYield(1.0f);
  }
}
