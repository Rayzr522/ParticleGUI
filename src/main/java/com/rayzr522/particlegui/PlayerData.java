/**
 * 
 */
package com.rayzr522.particlegui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Particle;

/**
 * @author Rayzr
 *
 */
public class PlayerData {

    private List<Particle> particles = new ArrayList<Particle>();

    public PlayerData() {

    }

    /**
     * @param particle the particle to add
     */
    public boolean addParticle(Particle particle) {
        if (particles.size() >= 5) {
            return false;
        }
        return particles.add(particle);
    }

    /**
     * @param particle the particle to set
     */
    public void setParticle(Particle particle) {
        particles.clear();
        particles.add(particle);
    }

    /**
     * @return The particles list
     */
    public List<Particle> getParticles() {
        return particles;
    }

    /**
     * 
     */
    public void clearParticles() {
        particles.clear();
    }

}
