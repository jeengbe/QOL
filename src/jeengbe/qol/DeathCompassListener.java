package jeengbe.qol;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.CompassMeta;

/**
 * Craftable compass pointing to the crafter's death location
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/97qpbj/crafting_a_totem_with_a_compass_makes_it_point_to/}
 */
public class DeathCompassListener implements Listener {
  private HashMap<UUID, Location> deathLocations     = new HashMap<>();

  public static Integer           COMPASS_MODEL_DATA = 1;
  private ItemStack               compassItem;

  public DeathCompassListener(Main main) {
    compassItem = new ItemStack(Material.COMPASS);
    CompassMeta compassMeta = (CompassMeta) compassItem.getItemMeta();
    compassMeta.setCustomModelData(COMPASS_MODEL_DATA);
    compassMeta.setLocalizedName("jeengbe.qol.death_compass_compass");
    compassMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    compassMeta.addEnchant(Enchantment.DURABILITY, 1, true);
    compassItem.setItemMeta(compassMeta);

    ShapelessRecipe compassRecipe = new ShapelessRecipe(new NamespacedKey(main, "death_compass_compass"), compassItem);
    compassRecipe.addIngredient(1, Material.COMPASS);
    compassRecipe.addIngredient(1, Material.TOTEM_OF_UNDYING);
    Bukkit.addRecipe(compassRecipe);
  }

  @EventHandler
  public void onCraftCompassPrepare(PrepareItemCraftEvent e) {
    if (e.getRecipe() == null)
      return;
    if (e.getRecipe().getResult() == null)
      return;
    if (e.getRecipe().getResult().equals(compassItem)) {
      if (!deathLocations.containsKey(e.getViewers().get(0).getUniqueId())) {
        e.getInventory().setResult(null);
      } else {
        ItemStack compass = e.getRecipe().getResult();
        CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
        compassMeta.setLodestoneTracked(false);
        compassMeta.setLodestone(deathLocations.get(e.getViewers().get(0).getUniqueId()));
        compass.setItemMeta(compassMeta);
        e.getInventory().setResult(compass);
      }
    }
  }

  @EventHandler
  public void onCraftCompass(CraftItemEvent e) {
    if (e.getRecipe() == null)
      return;
    if (e.getRecipe().getResult() == null)
      return;
    if (e.getRecipe().getResult().equals(compassItem)) {
      if (deathLocations.containsKey(e.getWhoClicked().getUniqueId())) {
        ItemStack compass = e.getCurrentItem();
        CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
        compassMeta.setLodestoneTracked(false);
        compassMeta.setLodestone(deathLocations.get(e.getWhoClicked().getUniqueId()));
        compass.setItemMeta(compassMeta);
        e.setCurrentItem(compass);
      }
    }
  }

  @EventHandler
  public void onDeath(PlayerDeathEvent e) {
    deathLocations.put(e.getEntity().getUniqueId(), e.getEntity().getLocation());
  }
}
