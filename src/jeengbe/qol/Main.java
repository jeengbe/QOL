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
    getServer().getPluginManager().registerEvents(new CreeperChain(), this);
    getServer().getPluginManager().registerEvents(new KeepNamed(), this);
    getServer().getPluginManager().registerEvents(new ExplosionYield(), this);
    getServer().getPluginManager().registerEvents(new DeathCompass(this), this);
    getServer().getPluginManager().registerEvents(new NoFeatherTrample(), this);
    getServer().getPluginManager().registerEvents(new EndTotem(), this);
    getServer().getPluginManager().registerEvents(new NoCampfireSpawnpoint(), this);
    getServer().getPluginManager().registerEvents(new NoBedGlide(), this);
  }
}
