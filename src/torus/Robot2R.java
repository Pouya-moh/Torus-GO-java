/*
 Implimentation of a 2r robot.
 */

package torus;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 *
 * @author Pouya Mohammadi
 */
public class Robot2R {
    private Point2D Base;
    private Point2D Joint;
    private Point2D EndEffector;

    public Line2D Link1;
    public Line2D Link2;
    public Ellipse2D visJoint, visBase, visEndEffector;
    public int offsetX,offsetY;
    public int total_lenght;
    public int rad_vis;
    private int max_total_length;
    private int min_link_length;

    public Robot2R(int ofx, int ofy, int total){
        offsetX=ofx;
        offsetY=ofy;
        total_lenght=total;
        max_total_length=179;
        min_link_length=1;
        rad_vis=7;
        
        Base = new Point2D.Double(offsetX, offsetY);
        Joint = new Point2D.Double(offsetX+(total_lenght/2), offsetY);
        EndEffector = new Point2D.Double(offsetX+total_lenght, offsetY);

        Link1 = new Line2D.Double(Base, Joint);
        Link2 = new Line2D.Double(Joint, EndEffector);

        visJoint = new Ellipse2D.Double(Joint.getX()-rad_vis/2, Joint.getY()-rad_vis/2, rad_vis, rad_vis);
        visBase = new Ellipse2D.Double(Base.getX()-rad_vis/2, Base.getY()-rad_vis/2, rad_vis, rad_vis);
        visEndEffector = new Ellipse2D.Double(EndEffector.getX()-rad_vis/2, EndEffector.getY()-rad_vis/2, rad_vis, rad_vis);
    }

    public double getLinkLength_1(){
        return Base.distance(Joint);
    }

    public double getLinkLength_2(){
        return Joint.distance(EndEffector);
    }

    public void setConfig(int theta1, int theta2){
         double x=getLinkLength_1()*Math.cos(Math.toRadians(-theta1));
         double y=getLinkLength_1()*Math.sin(Math.toRadians(-theta1));

         double x2=getLinkLength_1()*Math.cos(Math.toRadians(-theta1))+getLinkLength_2()*Math.cos(Math.toRadians(-theta1-theta2));
         double y2=getLinkLength_1()*Math.sin(Math.toRadians(-theta1))+getLinkLength_2()*Math.sin(Math.toRadians(-theta1-theta2));
         
         Joint.setLocation(x+offsetX, y+offsetY);
         EndEffector.setLocation(x2+offsetX, y2+offsetY);

         Link1.setLine(Base, Joint);
         Link2.setLine(Joint, EndEffector);

         visJoint.setFrame(Joint.getX()-rad_vis/2, Joint.getY()-rad_vis/2, rad_vis, rad_vis);
         visEndEffector.setFrame(EndEffector.getX()-rad_vis/2, EndEffector.getY()-rad_vis/2, rad_vis, rad_vis);

    }



    public void setTotal_lenght(int tl) {
        if (tl>max_total_length) {
            this.total_lenght=max_total_length;
            }
        else this.total_lenght = tl;

        this.setConfig(0, 0);
        this.Joint.setLocation(offsetX+(total_lenght/2), offsetY);
        this.EndEffector.setLocation(offsetX+total_lenght, offsetY);
    }

    public void setJoint_Position(int p){
        int pose=total_lenght/2;
        if (p<min_link_length) {
            pose=min_link_length;
            }
        else pose=p;


        this.setConfig(0, 0);
        this.Joint.setLocation(offsetX+pose, offsetY);
    }



}
