package jeengbe.qol.ai;

import java.util.List;

import net.minecraft.server.v1_16_R2.EntityCreature;
import net.minecraft.server.v1_16_R2.EntityCreeper;
import net.minecraft.server.v1_16_R2.PathfinderTargetCondition;
import net.minecraft.server.v1_16_R2.RandomPositionGenerator;
import net.minecraft.server.v1_16_R2.Vec3D;

public class PathfinderGoalFleeCreeper extends PathfinderGoalAvoidDamager {
  protected final PathfinderTargetCondition targetCondition;
  protected EntityCreeper                   exploding;

  public PathfinderGoalFleeCreeper(EntityCreature entity, float avoidDistance, double avoidSpeed) {
    super(entity, avoidDistance, avoidSpeed);
    targetCondition = (new PathfinderTargetCondition()).a(avoidDistance).a(ent -> ent instanceof EntityCreeper && ((EntityCreeper) ent).fuseTicks > 0);
  }

  @Override
  public boolean a() {
    // Is creeper explodig nearby
    List<EntityCreeper> creepers = this.entity.world.a(EntityCreeper.class, this.targetCondition, this.entity, this.entity.getBoundingBox().grow(this.avoidDistance));
    if (creepers.size() == 0)
      return false;
    this.exploding = creepers.get(0);

    Vec3D target;
    int c = 0;
    do {
      target = RandomPositionGenerator.c(this.entity, 16, 7, this.exploding.getPositionVector());
      if (target == null)
        return false;
      if (c++ > 1000)
        throw new RuntimeException("c > 1000");
    } while (this.exploding.h(target.x, target.y, target.z) < this.entity.h(this.exploding));

    this.pathEntity = this.navigationAbstract.a(target.x, target.y, target.z, 0);
    return (this.pathEntity != null);
  }

  @Override
  public boolean b() {
    return this.exploding != null && this.exploding.isAlive() && !this.navigationAbstract.m() && this.entity.h(this.exploding) < this.avoidDistance * this.avoidDistance;
  }

  @Override
  public void c() {
    this.timeToReaclcPath = 0;
    this.navigationAbstract.a(this.pathEntity, this.b);
  }

  @Override
  public void d() {
    this.pathEntity = null;
    this.exploding  = null;
  }

  @Override
  public void e() {
    if (this.exploding != null && this.exploding.isAlive()) {
      if (--this.timeToReaclcPath <= 0) {
        this.timeToReaclcPath = 10;
        Vec3D target = RandomPositionGenerator.c(this.entity, 16, 7, this.exploding.getPositionVector());
        if (target == null)
          return;
        if (this.exploding.h(target.x, target.y, target.z) < this.entity.h(this.exploding))
          return;
        this.pathEntity = this.navigationAbstract.a(target.x, target.y, target.z, 0);
      }
    }
  }
}
