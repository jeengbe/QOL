package jeengbe.qol;

import org.bukkit.craftbukkit.v1_16_R2.entity.CraftOcelot;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ocelot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import net.minecraft.server.v1_16_R2.EntityOcelot;
import net.minecraft.server.v1_16_R2.EntitySilverfish;
import net.minecraft.server.v1_16_R2.PathfinderGoalNearestAttackableTarget;

/**
 * Ocelots attack Silverfish
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/5e25gg/cats_should_attack_silverfish/}
 */
public class OcelotSilverfishAttack implements Listener {
  @EventHandler
  public void onLoad(ChunkLoadEvent e) {
    for (Entity ent : e.getChunk().getEntities()) {
      if (!(ent instanceof Ocelot)) {
        continue;
      }
      applyAI((Ocelot) ent);
    }
  }

  @EventHandler
  public void onSpawn(CreatureSpawnEvent e) {
    if (!(e.getEntity() instanceof Ocelot))
      return;
    applyAI((Ocelot) e.getEntity());
  }

  private void applyAI(Ocelot ocelot) {
    EntityOcelot nmsOcelot = ((CraftOcelot) ocelot).getHandle();
    nmsOcelot.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<>(nmsOcelot, EntitySilverfish.class, 10, false, false, null));
  }
}
