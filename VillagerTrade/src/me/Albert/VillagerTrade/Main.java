package me.Albert.VillagerTrade;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin implements Listener{
	private YamlConfiguration config;
	private File file = new File(this.getDataFolder() , "config.yml");;
	
	@Override
	public void onEnable() {
		org.bukkit.Bukkit.getConsoleSender().sendMessage("§a[VillagerTrade] Loaded");
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		this.saveDefaultConfig();
		 this.config = YamlConfiguration.loadConfiguration(this.file);
		 new Metrics(this);	
	};
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!new File(this.getDataFolder(),"config.yml").exists()) {
		saveResource("config.yml", true);
		} else
		this.config = YamlConfiguration.loadConfiguration(this.file);
		sender.sendMessage("§a[VillagerTrade] config reloaded");
		return true;
	}
	
	Villager v1;
	World w;
	Player p;
	@EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event){
		if (event.getRightClicked().getType() == EntityType.VILLAGER) {
                 v1=(Villager) event.getRightClicked();
                w = event.getPlayer().getWorld();
                p = event.getPlayer();
		}
	}
	
	@EventHandler
    public void onVillagerTrade(InventoryClickEvent event) {
        if ((event.getInventory() != null) && (event.getInventory().getType() == InventoryType.MERCHANT)) {
        	int a = this.config.getInt("chance");
        	int b=(int)(Math.random()*100);
        	if (event.getRawSlot() == 2 && v1.isDead() == false && a>=b) {
        		if (event.getAction().toString().contains("PICKUP") || event.getAction().toString().contains("MOVE_TO")) {
        		v1.remove();
                w.spawnEntity(v1.getLocation(), EntityType.VILLAGER);
                if (this.config.getBoolean("consolelogs") == true) {
                	Bukkit.getConsoleSender().sendMessage("§aVillager at X: " +  (int)v1.getLocation().getX() + " Y: "
                			+  (int)v1.getLocation().getY() + " Z: "+ (int)v1.getLocation().getZ()+ " changed his proffession");
                }
                getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
    				public void run() {
                p.closeInventory();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("message")));
        		}
                }, 10L);
            
        	}
          
        }
    }
	

}
}
