package jeengbe.qol;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.PlayerInventory;

/**
 * Shovels make a GONG-Sound when hitting an entity with it
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/4no4l2/shovels_make_a_gong_sound_when_striking_a_player/}
 */
public class ShovelGong implements Listener {
  private Material[] SHOVELS = new Material[] { Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL };

  @EventHandler
  public void onShovel(EntityDamageByEntityEvent e) {
    if (e.getCause() != DamageCause.ENTITY_ATTACK)
      return;
    if (!(e.getDamager() instanceof Player))
      return;
    Player player = (Player) e.getDamager();
    PlayerInventory inv = player.getInventory();
    if (inv.getItemInMainHand() == null)
      return;
    if (!Arrays.stream(SHOVELS).anyMatch(inv.getItemInMainHand().getType()::equals))
      return;
    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.BLOCK_BELL_USE, SoundCategory.PLAYERS, 1f, 1f);
  }
}
