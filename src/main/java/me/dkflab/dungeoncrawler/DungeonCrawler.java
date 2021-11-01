package me.dkflab.dungeoncrawler;

import me.dkflab.dungeoncrawler.commands.ClassCommand;
import me.dkflab.dungeoncrawler.commands.DungeonCommand;
import me.dkflab.dungeoncrawler.commands.ShopCommand;
import me.dkflab.dungeoncrawler.commands.UpgradeCommand;
import me.dkflab.dungeoncrawler.listeners.BlockBreak;
import me.dkflab.dungeoncrawler.listeners.ClickListener;
import me.dkflab.dungeoncrawler.listeners.EntityDamage;
import me.dkflab.dungeoncrawler.listeners.InventoryListener;
import me.dkflab.dungeoncrawler.managers.*;
import me.dkflab.dungeoncrawler.objects.Dungeon;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class DungeonCrawler extends JavaPlugin {

    public KitsManager kits;
    public AbilityManager abilityManager;
    public GUIManager gui;
    public MatchmakingManager mm;
    public DungeonManager dungeonManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerListeners();
        registerCommands();
        kits = new KitsManager(this);
        abilityManager = new AbilityManager(this);
        gui = new GUIManager(this);
        mm = new MatchmakingManager(this);
        dungeonManager = new DungeonManager(this);
        RecipeManager.init();

        BukkitRunnable run = new BukkitRunnable() {
            @Override
            public void run() {
                if (dungeonManager.getActiveDungeons() == null) {
                    return;
                }
                for (Dungeon d : dungeonManager.getActiveDungeons()) {
                    dungeonManager.loop(d);
                }
            }
        };
        run.runTaskTimer(this,20,20);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ClickListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(this),this);
        getServer().getPluginManager().registerEvents(new BlockBreak(this),this);
        getServer().getPluginManager().registerEvents(new EntityDamage(this),this);
    }

    private void registerCommands() {
        getCommand("class").setExecutor(new ClassCommand(this));
        getCommand("dungeon").setExecutor(new DungeonCommand(this));
        getCommand("upgrade").setExecutor(new UpgradeCommand(this));
        getCommand("shop").setExecutor(new ShopCommand(this));
    }

    public GUIManager getGUI() {
        return this.gui;
    }

    public MatchmakingManager getMM() {
        return mm;
    }
}
