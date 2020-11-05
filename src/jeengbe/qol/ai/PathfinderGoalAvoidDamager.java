package jeengbe.qol.ai;

import java.util.EnumSet;

import net.minecraft.server.v1_16_R2.EntityCreature;
import net.minecraft.server.v1_16_R2.NavigationAbstract;
import net.minecraft.server.v1_16_R2.PathEntity;
import net.minecraft.server.v1_16_R2.PathfinderGoal;
import net.minecraft.server.v1_16_R2.RandomPositionGenerator;
import net.minecraft.server.v1_16_R2.Vec3D;

public class PathfinderGoalAvoidDamager extends PathfinderGoal {

  protected EntityCreature     entity;
  protected float              avoidDistance;
  // »b« for Reflection
  protected double             b;
  protected PathEntity         pathEntity;
  protected NavigationAbstract navigationAbstract;
  protected int                timeToReaclcPath;

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
      if (c++ > 1000)
        throw new RuntimeException("c > 1000");
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