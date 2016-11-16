/**
 * 
 */
package com.rayzr522.particlegui;

import org.bukkit.Material;
import org.bukkit.Particle;

/**
 * @author Rayzr
 *
 */
public class ParticleData {

    private Particle particle;
    private Material display;
    private short    dataValue;
    private String   name;

    public ParticleData(Particle particle, Material display, int dataValue, String name) {
        super();
        this.particle = particle;
        this.display = display;
        this.dataValue = (short) dataValue;
        this.name = name;
    }

    /**
     * @return the particle
     */
    public Particle getParticle() {
        return particle;
    }

    /**
     * @return the display
     */
    public Material getDisplay() {
        return display;
    }

    /**
     * @return the dataValue
     */
    public short getDataValue() {
        return dataValue;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

}
