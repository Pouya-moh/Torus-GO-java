/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import java.util.ArrayList;
import torus.Obstacle;
import torus.Robot2R;

/**
 *
 * @author Poo
 */
public class LocalPlanner {
    private Robot2R Robot;
    private Obstacle[] Obstacles;

    public LocalPlanner(Robot2R robot, ArrayList obstacles){
//        Robot = new Robot2R(robot.offsetX, robot.offsetY, robot.total_lenght);
        Robot = robot;
        Obstacles = new Obstacle[obstacles.size()];
        for (int i= 0; i < obstacles.size(); i++) {
            Obstacles[i] = (Obstacle) obstacles.get(i);
        }
    }

    public boolean configurationIsFree(Configuration c){
        Robot.setConfig(c.theta1, c.theta2);
        for (int i= 0; i < Obstacles.length; i++) {
            if ((Obstacles[i].inCollision(Robot.Link1)==true)||((Obstacles[i].inCollision(Robot.Link2)==true))) return false;
        }
        return true;
    }

    public boolean pathIsFree(Configuration[] path){
        for (int i = 0; i < path.length; i++) {
            if (!configurationIsFree(path[i])) return false;
        }
        return true;
    }

}
