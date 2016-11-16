/**
 * 
 */
package com.rayzr522.particlegui;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

/**
 * @author Rayzr
 *
 */
public class ParticleListener implements Listener {

    private ParticleGUI plugin;

    public ParticleListener(ParticleGUI plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getFrom().toVector().equals(e.getTo().toVector())) {
            return;
        }
        if (!e.getPlayer().hasPermission("ParticleGUI.use")) {
            return;
        }
        List<Particle> particles = plugin.getParticles(e.getPlayer());
        for (Particle particle : particles) {
            showParticle(e.getFrom(), e.getTo(), particle);
        }
    }

    public void showParticle(Location from, Location to, Particle particle) {
        // This long line just moves the position back by 0.2 blocks so it
        // isn't in your face while you move
        Vector vec = from.toVector().subtract(to.toVector().subtract(from.toVector()).normalize().multiply(0.2));
        from.getWorld().spawnParticle(particle, vec.getX(), vec.getY() + 1, vec.getZ(), 5, 0.1, 0.1, 0.1, 0);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        plugin.initPlayer(e.getPlayer());
    }

}
