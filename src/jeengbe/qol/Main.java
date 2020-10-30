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
  }
}
