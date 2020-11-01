package jeengbe.qol;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.bukkit.craftbukkit.v1_16_R2.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import net.minecraft.server.v1_16_R2.EntityCreature;
import net.minecraft.server.v1_16_R2.NavigationAbstract;
import net.minecraft.server.v1_16_R2.PathEntity;
import net.minecraft.server.v1_16_R2.PathfinderGoal;
import net.minecraft.server.v1_16_R2.PathfinderGoalPanic;
import net.minecraft.server.v1_16_R2.PathfinderGoalWrapped;
import net.minecraft.server.v1_16_R2.RandomPositionGenerator;
import net.minecraft.server.v1_16_R2.Vec3D;

/**
 * Animals flee from their last damager
 */
public class FleeDamager implements Listener {
  private EntityType[] FLEE = new EntityType[] { EntityType.CHICKEN, EntityType.COW, EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.HORSE, EntityType.LLAMA, EntityType.TRADER_LLAMA, EntityType.PARROT, EntityType.PIG, EntityType.SHEEP, EntityType.STRIDER, EntityType.WANDERING_TRADER };

  @EventHandler
  public void onLoad(ChunkLoadEvent e) {
    List<Entity> patch = new ArrayList<>();
    for (Entity ent : e.getChunk().getEntities()) {
      if (!Arrays.stream(FLEE).anyMatch(entityType -> ent.getType() == entityType)) {
        continue;
      }
      patch.add(ent);
    }

    patch.forEach(ent -> patchAI(ent));
  }

  @EventHandler
  public void onSpawn(CreatureSpawnEvent e) {
    if (!Arrays.stream(FLEE).anyMatch(entityType -> e.getEntity().getType() == entityType))
      return;
    patchAI(e.getEntity());
  }

  private void patchAI(Entity entity) {
    EntityCreature nmsEntity = (EntityCreature) ((CraftLivingEntity) entity).getHandle();
    PathfinderGoalWrapped panicGoalWrapper = nmsEntity.goalSelector.getTasks().stream().filter(goal -> goal.getGoal().getClass().getName().equals(PathfinderGoalPanic.class.getName()) || goal.getGoal().getClass().getName().equals(PathfinderGoalAvoidDamager.class.getName())).findFirst().orElseGet(() -> null);
    if (panicGoalWrapper == null)
      return;
    PathfinderGoal panicGoal = panicGoalWrapper.getGoal();

    Field fieldSpeed;
    try {
      fieldSpeed = panicGoal.getClass().getDeclaredField("b");
      fieldSpeed.setAccessible(true);
      nmsEntity.goalSelector.a(panicGoal);
      nmsEntity.goalSelector.a(panicGoalWrapper.h(), new PathfinderGoalAvoidDamager(nmsEntity, 5F, (Double) fieldSpeed.get(panicGoal)));
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}

class PathfinderGoalAvoidDamager extends PathfinderGoal {

  protected EntityCreature     entity;
  protected float              avoidDistance;
  // »b« for Reflection
  protected double             b;
  protected PathEntity         pathEntity;
  protected NavigationAbstract navigationAbstract;
  private int                  timeToReaclcPath;

  public PathfinderGoalAvoidDamager(EntityCreature entity, float avoidDistance, double avoidSpeed) {
    this.entity             = entity;
    this.avoidDistance      = avoidDistance;
    this.b                  = avoidSpeed;
    this.navigationAbstract = entity.getNavigation();
    a(EnumSet.of(PathfinderGoal.Type.TARGET));
  }

  @Override
  public boolean a() {
    if (this.entity.getLastDamager() == null)
      return false;
    if (this.entity.h(this.entity.getLastDamager()) > this.avoidDistance * this.avoidDistance)
      return false;

    Vec3D target;
    int c = 0;
    do {
      target = RandomPositionGenerator.c(this.entity, 16, 7, this.entity.getLastDamager().getPositionVector());
      if (target == null)
        return false;
      if (c++ > 100)
        throw new RuntimeException("c > 100");
    } while (this.entity.getLastDamager().h(target.x, target.y, target.z) < this.entity.h(this.entity.getLastDamager()));

    this.pathEntity = this.navigationAbstract.a(target.x, target.y, target.z, 0);
    return (this.pathEntity != null);
  }

  @Override
  public boolean b() {
    return this.entity.getLastDamager() != null && !this.navigationAbstract.m() && this.entity.h(this.entity.getLastDamager()) < this.avoidDistance * this.avoidDistance;
  }

  @Override
  public void c() {
    this.timeToReaclcPath = 0;
    this.navigationAbstract.a(this.pathEntity, this.b);
  }

  @Override
  public void d() {
    this.pathEntity = null;
  }

  @Override
  public void e() {
    if (this.entity.getLastDamager() != null) {
      if (--this.timeToReaclcPath <= 0) {
        this.timeToReaclcPath = 10;
        Vec3D target = RandomPositionGenerator.c(this.entity, 16, 7, this.entity.getLastDamager().getPositionVector());
        if (target == null)
          return;
        if (this.entity.getLastDamager().h(target.x, target.y, target.z) < this.entity.h(this.entity.getLastDamager()))
          return;
        this.pathEntity = this.navigationAbstract.a(target.x, target.y, target.z, 0);
      }
    }
  }
}
