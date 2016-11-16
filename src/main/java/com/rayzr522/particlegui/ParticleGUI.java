/**
 * 
 */
package com.rayzr522.particlegui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.java.JavaPlugin;

import com.perceivedev.perceivecore.gui.Gui;
import com.perceivedev.perceivecore.gui.components.panes.AnchorPane;
import com.perceivedev.perceivecore.gui.components.panes.FlowPane;
import com.perceivedev.perceivecore.gui.components.simple.DisplayColor;
import com.perceivedev.perceivecore.gui.components.simple.SimpleButton;
import com.perceivedev.perceivecore.gui.components.simple.StandardDisplayTypes;
import com.perceivedev.perceivecore.util.ItemFactory;
import com.perceivedev.perceivecore.util.TextUtils;
import com.perceivedev.perceivecore.util.Unicode;

/**
 * @author Rayzr
 *
 */
public class ParticleGUI extends JavaPlugin {

    private static List<ParticleData> particleTypes = new ArrayList<ParticleData>();

    private static void addParticle(Particle particle, Material display, int dataValue, String name) {
        particleTypes.add(new ParticleData(particle, display, dataValue, name));
    }

    static {
        addParticle(Particle.CLOUD, Material.WOOL, 0, "Cloud");
        addParticle(Particle.FLAME, Material.FLINT_AND_STEEL, 0, "Fire");
        addParticle(Particle.DRIP_LAVA, Material.LAVA_BUCKET, 0, "Lava Drip");
        addParticle(Particle.DRIP_WATER, Material.WATER_BUCKET, 0, "Water Drip");
        addParticle(Particle.HEART, Material.APPLE, 0, "Hearts");
        addParticle(Particle.CRIT, Material.IRON_SWORD, 0, "Crit");
        addParticle(Particle.CRIT_MAGIC, Material.DIAMOND_SWORD, 0, "Crit Magic");
        addParticle(Particle.DRAGON_BREATH, Material.DRAGON_EGG, 0, "Dragon Breath");
        addParticle(Particle.REDSTONE, Material.REDSTONE, 0, "Redstone");
        addParticle(Particle.ENCHANTMENT_TABLE, Material.BOOK, 0, "Magic Letters");
        addParticle(Particle.SLIME, Material.SLIME_BALL, 0, "Slime");
        addParticle(Particle.END_ROD, Material.END_ROD, 0, "End Rod");
        addParticle(Particle.PORTAL, Material.ENDER_CHEST, 0, "Portal");
        addParticle(Particle.NOTE, Material.NOTE_BLOCK, 0, "Music");
        addParticle(Particle.FOOTSTEP, Material.STAINED_CLAY, 9, "Footstep");
        addParticle(Particle.WATER_SPLASH, Material.RAW_FISH, 0, "Splash");
        addParticle(Particle.WATER_BUBBLE, Material.WATER_BUCKET, 0, "Bubble");
        addParticle(Particle.SMOKE_NORMAL, Material.WEB, 0, "Smoke Normal");
        addParticle(Particle.SMOKE_LARGE, Material.WEB, 0, "Smoke Large");
        addParticle(Particle.VILLAGER_HAPPY, Material.INK_SACK, 15, "Villager Happy");
        addParticle(Particle.VILLAGER_ANGRY, Material.FIREBALL, 0, "Villager Angry");
        addParticle(Particle.EXPLOSION_NORMAL, Material.TNT, 0, "Explosion Small");
        addParticle(Particle.EXPLOSION_LARGE, Material.TNT, 0, "Explosion Normal");
    }

    private HashMap<UUID, PlayerData> players = new HashMap<>();

    private Logger                    logger;
    private Gui                       gui;

    private String                    prefix;

    @SuppressWarnings("unused")
    private ParticleListener          listener;

    @Override
    public void onEnable() {
        logger = getLogger();

        prefix = "&8&l" + Unicode.DOUBLE_ANGLE_LEFT + "&4Particle&6&lGUI&8&l" + Unicode.DOUBLE_ANGLE_RIGHT;

        initGUI();

        getCommand("particles").setExecutor(new ParticleCommand(this));

        for (Player player : Bukkit.getOnlinePlayers()) {
            initPlayer(player);
        }

        logger.info(versionText() + " enabled");

    }

    /**
     * Initializes the GUI
     */
    private void initGUI() {

        int bottom = particleTypes.size() / 9 + 1;
        gui = new Gui(prefix, bottom + 1);

        listener = new ParticleListener(this);

        FlowPane particlesPane = new FlowPane(9, 5);

        int i = 0;
        for (ParticleData data : particleTypes) {

            SimpleButton button = new SimpleButton(e -> {

                if (e.getClickType() == ClickType.RIGHT) {
                    addParticle(e.getPlayer(), data.getParticle());
                    msg(e.getPlayer(), ChatColor.GOLD + "Particle added");
                } else {
                    setParticle(e.getPlayer(), data.getParticle());
                    msg(e.getPlayer(), ChatColor.GOLD + "Particle selected");
                }

            });

            final int I = i + 1;
            button.setDisplayType(c -> ItemFactory.builder(data.getDisplay()).setDurability(data.getDataValue()).setAmount(I));
            button.setName("&cParticle: &6" + data.getName());
            button.setLore("&7&oLeft click: select", "&7&oRight click: add");
            button.setCloseOnClick(false);

            i++;

        }

        AnchorPane controlsPane = new AnchorPane(9, 1);

        SimpleButton clear = new SimpleButton("&cClear", e -> {
            get(e.getPlayer()).clearParticles();
            msg(e.getPlayer(), ChatColor.GREEN + "Particles removed!");
        });
        clear.setDisplayType(StandardDisplayTypes.FLAT);
        clear.setColor(DisplayColor.RED);

        controlsPane.addComponent(clear, 8, 0);

        ((AnchorPane) gui.getRootPane()).addComponent(particlesPane, 0, 0);
        ((AnchorPane) gui.getRootPane()).addComponent(controlsPane, 0, 5);

    }

    @Override
    public void onDisable() {

        logger.info(versionText() + " disabled");
    }

    /**
     * @return The version text of this plugin
     */
    public String versionText() {
        return getName() + " v" + getDescription().getName();
    }

    public void msg(Player player, String msg) {
        player.sendMessage(TextUtils.colorize(prefix + "&r " + msg));
    }

    public PlayerData get(Player player) {
        return players.containsKey(player.getUniqueId()) ? players.get(player.getUniqueId()) : players.put(player.getUniqueId(), new PlayerData());
    }

    public List<Particle> getParticles(Player player) {
        return get(player).getParticles();
    }

    public void setParticle(Player player, Particle particle) {
        get(player).setParticle(particle);
    }

    public void addParticle(Player player, Particle particle) {
        if (!players.get(player.getUniqueId()).addParticle(particle)) {
            msg(player, ChatColor.RED + "You are already at the max number of particles!");
        }
    }

    /**
     * @param player
     */
    public void initPlayer(Player player) {
        players.put(player.getUniqueId(), new PlayerData());
    }

    /**
     * @param p the player to open the GUI for
     */
    public void openGUI(Player p) {
        p.openInventory(gui.getInventory());
    }

}
