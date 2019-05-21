package me.Albert.VillagerTrade;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;
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
	
	private boolean cd = true;
	
	@EventHandler
    public void onTrade(VillagerReplenishTradeEvent e) {
		int a = this.config.getInt("chance");
    	int b=(int)(Math.random()*100);
    	if (a>=b && e.getEntity().isDead() == false && cd == true 
    		&& e.getEntity().getTicksLived() > config.getInt("ticks")) {
    		if (config.getBoolean("unlockall") == false) {
        	changeVillager(e.getEntity());
    		}
    		if (config.getBoolean("unlockall") == true && e.getEntity().getRecipeCount() >= 4) {
    			changeVillager(e.getEntity());
    		}
   	
    	}
    	
   	 
    }
	public void changeVillager(Entity v) {
		    cd = false;
	        v.remove();
	        if (config.getString("action").equalsIgnoreCase("change")) {
	        v.getWorld().spawnEntity(v.getLocation(),EntityType.VILLAGER);
	        }
	     if (this.config.getBoolean("consolelogs") == true) {
	    	 String action = config.getString("action").toString();
	    	if (action.equalsIgnoreCase("change")) {
	    		action = " changed his proffesion";
	    	}
	    	if (action.equalsIgnoreCase("remove")) {
	    		action = " has been removed";
	    	}
      	Bukkit.getConsoleSender().sendMessage("§aVillager at X: " +  (int)v.getLocation().getX() + " Y: "
      			+  (int)v.getLocation().getY() + " Z: "+ (int)v.getLocation().getZ()+ action);
      }
	     
	  for(Player p: Bukkit.getOnlinePlayers()) {
		  if (v.getWorld() == p.getWorld()) {
		 if (v.getLocation().distanceSquared(p.getLocation()) < 20) {
			 p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("message")));
		 }
	  }
	  }
	     getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				cd =true;
		}
      }, 10L);
		
	}
	

}
