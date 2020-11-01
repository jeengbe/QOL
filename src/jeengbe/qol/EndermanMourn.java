package jeengbe.qol;

import java.util.EnumSet;
import java.util.List;

import org.bukkit.craftbukkit.v1_16_R2.entity.CraftEnderman;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import net.minecraft.server.v1_16_R2.DragonControllerPhase;
import net.minecraft.server.v1_16_R2.EntityEnderDragon;
import net.minecraft.server.v1_16_R2.EntityEnderman;
import net.minecraft.server.v1_16_R2.EntityHuman;
import net.minecraft.server.v1_16_R2.IEntitySelector;
import net.minecraft.server.v1_16_R2.PathfinderGoal;
import net.minecraft.server.v1_16_R2.WorldServer;

/**
 * Endermen stare at the Ender Dragon during its death animation
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/6dmcpw/when_the_enderdragon_has_no_health_leftas_its/}
 */
public class EndermanMourn implements Listener {
  @EventHandler
  public void onLoad(ChunkLoadEvent e) {
    for (Entity ent : e.getChunk().getEntities()) {
      if (!(ent instanceof Enderman)) {
        continue;
      }
      applyAI((Enderman) ent);
    }
  }

  @EventHandler
  public void onSpawn(CreatureSpawnEvent e) {
    if (!(e.getEntity() instanceof Enderman))
      return;
    applyAI((Enderman) e.getEntity());
  }

  private void applyAI(Enderman enderman) {
    EntityEnderman nmsEnderman = ((CraftEnderman) enderman).getHandle();
    nmsEnderman.goalSelector.a(1, new PathfinderGoalEndermanMourn(nmsEnderman));
  }
}

class PathfinderGoalEndermanMourn extends PathfinderGoal {
  protected EntityEnderman    entity;
  protected EntityEnderDragon dragon;

  public PathfinderGoalEndermanMourn(EntityEnderman entity) {
    this.entity = entity;
    a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.JUMP));
  }

  @Override
  public boolean a() {
    List<EntityEnderDragon> dragons = ((WorldServer) this.entity.world).g();
    for (EntityEnderDragon dragon : dragons) {
      if (dragon.getDataWatcher().get(EntityEnderDragon.PHASE) == DragonControllerPhase.DYING.b()) {
        this.dragon = dragon;
        return true;
      }
    }
    return false;
  }

  @Override
  public void d() {
    this.entity.setAggressive(true);
    List<? extends EntityHuman> players = this.dragon.world.getPlayers();
    players.removeIf(ent -> !IEntitySelector.f.test(ent));
    if (players.size() > 0) {
      this.entity.setLastDamager(players.get(this.dragon.world.random.nextInt(players.size())));
    }
    this.dragon = null;
  }

  @Override
  public void e() {
    this.entity.getNavigation().o();
    this.entity.getControllerLook().a(dragon.locX(), dragon.locY(), dragon.locZ());
  }
}