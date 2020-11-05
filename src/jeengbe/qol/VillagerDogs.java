package jeengbe.qol;

import org.bukkit.craftbukkit.v1_16_R2.entity.CraftWolf;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;

import net.minecraft.server.v1_16_R2.EntityWolf;

/**
 * Villagers have a small chance of generating with a tamed dog
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/51wvce/villagers_should_rarely_spawn_with_tamed_wolves/}
 * <b>Broken:</b> Spawned dogs are rendered sitting, although they are technically
 * not.
 */
public class VillagerDogs implements Listener {
  @EventHandler
  public void onPopulate(ChunkPopulateEvent e) {
    for (Entity ent : e.getChunk().getEntities()) {
      if (!(ent instanceof Villager)) {
        continue;
      }
      if (Main.rand.nextFloat() > 1) {
        continue;
      }
      Villager vill = (Villager) ent;
      if (!vill.isAdult()) {
        continue;
      }

      Wolf wolf = (Wolf) vill.getWorld().spawnEntity(vill.getLocation(), EntityType.WOLF);
      EntityWolf nmsWolf = ((CraftWolf) wolf).getHandle();
      nmsWolf.setTamed(true);
      nmsWolf.setOwnerUUID(vill.getUniqueId());
      nmsWolf.setWillSit(false);
    }
  }
}
