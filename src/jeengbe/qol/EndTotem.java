package jeengbe.qol;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.minecraft.server.v1_16_R2.EntityPlayer;
import net.minecraft.server.v1_16_R2.PacketPlayOutEntityStatus;

/**
 * Totem prevents falling into the void
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/815oon/if_you_fall_into_the_void_with_a_totem_in_your/}
 */
public class EndTotem implements Listener {
  @EventHandler
  public void onResurrect(EntityDamageEvent e) {
    Entity ent = e.getEntity();
    if (!ent.getWorld().getName().endsWith("_the_end"))
      return;
    if (e.getCause() != DamageCause.VOID)
      return;
    if (!(ent instanceof HumanEntity))
      return;
    HumanEntity hum = (HumanEntity) ent;
    if (hum.getHealth() - e.getFinalDamage() > 0)
      return;
    PlayerInventory inv = hum.getInventory();
    if (inv.getItemInMainHand().getType() != Material.TOTEM_OF_UNDYING && inv.getItemInOffHand().getType() != Material.TOTEM_OF_UNDYING)
      return;
    if (inv.getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING) {
      ItemStack totem = inv.getItemInMainHand();
      totem.setAmount(totem.getAmount() - 1);
      inv.setItemInMainHand(totem);
    } else if (inv.getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
      ItemStack totem = inv.getItemInOffHand();
      totem.setAmount(totem.getAmount() - 1);
      inv.setItemInOffHand(totem);
    } else
      return;

    Location tp = ent.getLocation();
    tp.setY(ent.getWorld().getMaxHeight());
    ent.teleport(tp, TeleportCause.ENDER_PEARL);
    e.setCancelled(true);

    if (ent instanceof Player) {
      EntityPlayer handle = ((CraftPlayer) hum).getHandle();
      PacketPlayOutEntityStatus statusPacket = new PacketPlayOutEntityStatus(handle, (byte) 35);
      handle.playerConnection.sendPacket(statusPacket);
    }
  }
}
