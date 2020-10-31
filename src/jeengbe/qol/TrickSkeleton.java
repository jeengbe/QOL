package jeengbe.qol;

import org.bukkit.craftbukkit.v1_16_R2.entity.CraftSkeleton;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import net.minecraft.server.v1_16_R2.EntitySkeletonAbstract;
import net.minecraft.server.v1_16_R2.PathfinderGoalNearestAttackableTarget;

/**
 * Shooting a skeleton whilst invisible angers the skeleton at the next closest
 * skeleton
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/4y8dyb/shooting_a_skeleton_while_invisible_makes_him/}
 */
public class TrickSkeleton implements Listener {
  @EventHandler
  public void onShoot(EntityDamageByEntityEvent e) {
    if (!(e.getDamager() instanceof Arrow))
      return;
    Arrow arrow = (Arrow) e.getDamager();

    if (e.getEntity().getType() != EntityType.SKELETON)
      return;
    Skeleton skeleton = (Skeleton) e.getEntity();

    if (!(arrow.getShooter() instanceof Player))
      return;

    Player player = (Player) arrow.getShooter();
    if (!player.hasPotionEffect(PotionEffectType.INVISIBILITY))
      return;

    EntitySkeletonAbstract skel1 = ((CraftSkeleton) skeleton).getHandle();
    PathfinderGoalNearestAttackableTarget<EntitySkeletonAbstract> ai = new PathfinderGoalNearestAttackableTarget<>(skel1, EntitySkeletonAbstract.class, 0, true, false, null);
    ai.a();
    ai.c();
  }
}
