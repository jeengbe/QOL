package jeengbe.qol;

import java.util.Random;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
  public static Random rand;
  public static Main   me;

  @Override
  public void onEnable() {
    rand = new Random();
    me   = this;
    getServer().getPluginManager().registerEvents(this, this);
    getServer().getPluginManager().registerEvents(new CreeperChain(), this);
    getServer().getPluginManager().registerEvents(new KeepNamed(), this);
    getServer().getPluginManager().registerEvents(new ExplosionYield(), this);
    getServer().getPluginManager().registerEvents(new DeathCompass(this), this);
    getServer().getPluginManager().registerEvents(new NoFeatherTrample(), this);
    getServer().getPluginManager().registerEvents(new EndTotem(), this);
    getServer().getPluginManager().registerEvents(new NoCampfireSpawnpoint(), this);
    getServer().getPluginManager().registerEvents(new NoBedGlide(), this);
    getServer().getPluginManager().registerEvents(new ShovelGong(), this);
    getServer().getPluginManager().registerEvents(new NoFriendSweep(), this);
    getServer().getPluginManager().registerEvents(new NetherSmokeGrenade(), this);
    getServer().getPluginManager().registerEvents(new TrickSkeleton(), this);
    getServer().getPluginManager().registerEvents(new AnvilSmash(), this);
    getServer().getPluginManager().registerEvents(new AnvilSpongeDry(), this);
    // getServer().getPluginManager().registerEvents(new VillagerDogs(), this);
    getServer().getPluginManager().registerEvents(new OcelotSilverfishAttack(), this);
    getServer().getPluginManager().registerEvents(new FreeDog(), this);
    getServer().getPluginManager().registerEvents(new ColorfulJoinQuit(), this);
    getServer().getPluginManager().registerEvents(new HerdFlee(), this);
    getServer().getPluginManager().registerEvents(new FleeDamager(), this);
  }
}
