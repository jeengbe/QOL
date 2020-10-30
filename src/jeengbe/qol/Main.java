package jeengbe.qol;

import java.util.Random;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
  public static Random rand;

  @Override
  public void onEnable() {
    rand = new Random();
    getServer().getPluginManager().registerEvents(this, this);
    getServer().getPluginManager().registerEvents(new CreeperChainListener(), this);
    getServer().getPluginManager().registerEvents(new KeepNamedListener(), this);
    getServer().getPluginManager().registerEvents(new ExplosionYieldListener(), this);
    getServer().getPluginManager().registerEvents(new DeathCompassListener(this), this);
    getServer().getPluginManager().registerEvents(new NoFeatherTrampleListener(), this);
    getServer().getPluginManager().registerEvents(new EndTotemListener(), this);
  }
}
