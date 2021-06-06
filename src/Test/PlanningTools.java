/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import torus.Robot2R;

/**
 *
 * @author pouya
 */
public class PlanningTools {

    public PlanningTools(){

    }

    public static Configuration[] getDirectPath(Configuration _s, Configuration _g){
        Configuration s = new Configuration(_s.theta1, _s.theta2);
        Configuration g = new Configuration(_g.theta1, _g.theta2);
        int d = (int) calculateDistance(s,g);
        Configuration[] path = new Configuration[d];

        int dx = (Math.abs(s.theta1-g.theta1));
        int dy = (Math.abs(s.theta2-g.theta2));

        if ((dx <= 180) && (dy <= 180)) {
            for (int i = 0; i < d; i++) {
                int dx_ = (i * g.theta1 + (d - i) * s.theta1) / d;
                int dy_ = (i * g.theta2 + (d - i) * s.theta2) / d;
                Configuration temp = new Configuration(dx_, dy_);
                path[i] = temp;
            }
        }

        if((dx>180)&&(dy<=180)){
            if (s.theta1>g.theta1){
                s.theta1 = s.theta1-359;
            }else {
                g.theta1 = g.theta1-359;
            }

            for(int i = 0; i < d; i++){
                int dx_ = (i*g.theta1+(d-i)*s.theta1)/d;
                int dy_ = (i*g.theta2+(d-i)*s.theta2)/d;
                if (dx_<0) dx_=dx_+359;
                Configuration temp = new Configuration(dx_, dy_);
                path[i] = temp;
            }
        }

        if((dy>180)&&(dx<=180)){
            if(s.theta2>g.theta2){
                s.theta2 = s.theta2-359;
            }else {
                g.theta2 = g.theta2-359;
            }

            for (int i = 0; i < d; i++) {
                int dx_ = (i*g.theta1+(d-i)*s.theta1)/d;
                int dy_ = (i*g.theta2+(d-i)*s.theta2)/d;
                if(dy_<0) dy_=dy_+359;
                Configuration temp = new Configuration(dx_, dy_);
                path[i] = temp;
            }
        }

        if((dx>180)&&(dy>180)){
            if (s.theta1>g.theta1){
                s.theta1 = s.theta1 - 359;
            } else {
                g.theta1 = g.theta1 - 359;
            }

            if (s.theta2>g.theta2){
                s.theta2 = s.theta2 - 359;
            } else {
                g.theta2 = g.theta2 - 359;
            }

            for (int i = 0; i < d; i++) {
                int dx_ = (i*g.theta1+(d-i)*s.theta1)/d;
                int dy_ = (i*g.theta2+(d-i)*s.theta2)/d;
                if (dx_<0) dx_ = dx_+359;
                if (dy_<0) dy_ = dy_+359;
                Configuration temp = new Configuration(dx_, dy_);
                path[i] = temp;
            }
        }


        return path;

    }

    public static double calculateDistance(Configuration s, Configuration g){
        return Math.sqrt(Math.pow(angleDistance(s.theta1, g.theta1),2)+Math.pow(angleDistance(s.theta2, g.theta2), 2));
    }

    public static int angleDistance(int t1, int t2){
        int angDist = (Math.abs(t1 - t2)) % 360;
        if (angDist > 180) {
            angDist = 360 - angDist;
        }
        return angDist;
    }


//    public static boolean direcFreePathExists(Configuration q_near, Configuration q_new){
//        Configuration[] localPath = new Configuration[(int)(calculateDistance(q_near, q_new))];
//        localPath = getDirectPath(q_near, q_new);
//        for (int i= 0; i < localPath.length; i++) {
//            if (/* localPath[i].isInCollision */true){
//                return false;
//            }
//        }
//        return true;
//    }



}
