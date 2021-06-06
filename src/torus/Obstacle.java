/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package torus;

import java.awt.geom.Line2D;

/**
 *
 * @author Poo
 */
public abstract class Obstacle {

    public abstract boolean inCollision(Line2D l);
}
