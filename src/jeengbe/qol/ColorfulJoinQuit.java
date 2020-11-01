package jeengbe.qol;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Join and quit messages are colored green and red, respectively
 * {@link https://www.reddit.com/r/minecraftsuggestions/comments/5ehq0e/player_joined_the_game_in_green_colour_player/}
 */
public class ColorfulJoinQuit implements Listener {
  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    e.setJoinMessage(ChatColor.GREEN + e.getJoinMessage().substring(2));
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    e.setQuitMessage(ChatColor.RED + e.getQuitMessage().substring(2));
  }
}
