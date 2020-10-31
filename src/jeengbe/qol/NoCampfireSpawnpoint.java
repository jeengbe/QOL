package jeengbe.qol;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Sleeping near a campfire doesn't change the player's bed spawn location
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/hv6tk2/place_your_bed_near_a_campfire_and_say_no_to/}
 * <b>Bug: </b>It force-sets the player's bed spawn location, causing them to respawn
 * there although the bed may be destroyed.
 */
public class NoCampfireSpawnpoint implements Listener {
  private Material[]              BEDS         = new Material[] { Material.WHITE_BED, Material.ORANGE_BED, Material.PURPLE_BED, Material.LIGHT_BLUE_BED, Material.YELLOW_BED, Material.LIME_BED, Material.PINK_BED, Material.GRAY_BED, Material.LIGHT_GRAY_BED, Material.CYAN_BED, Material.PURPLE_BED, Material.BLUE_BED, Material.BROWN_BED, Material.GREEN_BED, Material.RED_BED, Material.BLACK_BED };
  private HashMap<UUID, Location> bedLocations = new HashMap<>();

  @EventHandler
  public void onSleep(PlayerInteractEvent e) {
    if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
      return;
    if (!Arrays.stream(BEDS).anyMatch(e.getClickedBlock().getType()::equals))
      return;
    if (e.getPlayer().getBedSpawnLocation() != null) {
      bedLocations.put(e.getPlayer().getUniqueId(), e.getPlayer().getBedSpawnLocation().clone());
    }
  }

  @EventHandler
  public void onAwake(PlayerBedLeaveEvent e) {
    if (!isCampfireNearby(e.getBed().getLocation()))
      return;
    e.setSpawnLocation(false);
    if (bedLocations.containsKey(e.getPlayer().getUniqueId())) {
      e.getPlayer().setBedSpawnLocation(bedLocations.get(e.getPlayer().getUniqueId()), true);
    }
  }

  private Boolean isCampfireNearby(Location loc) {
    int d = 3;
    for (int x = -d; x < d; x++) {
      for (int y = -d; y < d; y++) {
        for (int z = -d; z < d; z++) {
          if (loc.clone().add(x, y, z).getBlock().getType() == Material.CAMPFIRE)
            return true;
        }
      }
    }
    return false;
  }
}
