package jeengbe.qol;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.util.Vector;

/**
 * Entering a bed resets a player's velocity
 */
public class NoBedGlide implements Listener {
  @EventHandler
  public void onSleep(PlayerBedEnterEvent e) {
    e.getPlayer().setVelocity(new Vector());
  }
}
