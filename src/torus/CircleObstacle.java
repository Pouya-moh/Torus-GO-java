/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package torus;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 *
 * @author Poo
 */
public class CircleObstacle extends Obstacle{
    public Ellipse2D shape;
    public Point2D center;
    public int radius;
    public int r2=15;
    public Ellipse2D cc;

    public CircleObstacle(Point2D c, int r){
        shape = new Ellipse2D.Double();
        radius=r;
        center=c;
        shape.setFrame(center.getX()-radius, center.getY()-radius, radius*2, radius*2);
        cc=new Ellipse2D.Double(center.getX()-r2, center.getY()-r2, r2*2, r2*2);
    }

    public void setCenter(Point2D center) {
        this.center = center;
        shape.setFrame(center.getX()-radius, center.getY()-radius, radius*2, radius*2);
        cc.setFrame(center.getX()+radius-r2, center.getY()-r2, r2*2, r2*2);
    }

    public void setRadius(int radius) {
        if (radius>=15){
        this.radius = radius;
        shape.setFrame(center.getX()-radius, center.getY()-radius, radius*2, radius*2);
        cc.setFrame(center.getX()+radius-r2, center.getY()-r2, r2*2, r2*2);
        }
    }

    public Ellipse2D getShape() {
        return shape;
    }

    public boolean inCollision(Line2D l) {

        if (this.shape.contains(l.getP1())||this.shape.contains(l.getP2()))
            return true;



        double a = l.getP1().distance(center);
        double b = l.getP2().distance(center);
        double c = l.getP1().distance(l.getP2());
        double theta1 = Math.acos((Math.pow(b,2)+Math.pow(c,2)-Math.pow(a,2))/(2*b*c));
        double theta2 = Math.acos((Math.pow(a,2)+Math.pow(c,2)-Math.pow(b,2))/(2*a*c));
//            System.out.println(((Math.pow(b,2)+Math.pow(c,2)-Math.pow(a,2))/(2*b*c)));

//        if ((!this.getShape().contains(l.getX1(), l.getY1()))&&(!this.getShape().contains(l.getX2(), l.getY2())))
//             return false;
         if (isInside(l.getP2())) {
            return true;
        }

        if (isInside(l.getP1())) {
            return true;
        }

        if (Math.toDegrees(theta1) > 90 || Math.toDegrees(theta2) > 90) {
            return false;
        } else if (l.ptLineDist(center) < radius) {
            return true;
        } else {
            return false;
        }
    }

    
    private boolean isInside(Point2D p){
        if(Math.pow(p.getX()-center.getX(),2)+Math.pow(p.getY()-center.getY(), 2)<Math.pow(radius, 2)){
            return true;
        }
        return false;
    }

}
