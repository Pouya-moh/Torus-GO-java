/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package torus;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/**
 *
 * @author Poo
 */
public class PolygonObstacle extends Obstacle{
    public int segmentNumber;
    public Path2D shape;
    public Point2D P1=null;
    public Point2D P2=null;
    public Point2D P3=null;
    public Point2D P4=null;
    public Point2D P5=null;
    public int r2=15;

    Ellipse2D c1,c2,c3,c4,c5;

    public PolygonObstacle(Point2D p1, Point2D p2, Point2D p3){
        P1=p1;
        P2=p2;
        P3=p3;
        segmentNumber=3;
        shape = new Path2D.Double();

        shape.moveTo(p1.getX(), p1.getY());
        shape.lineTo(p1.getX(), p1.getY());
        shape.lineTo(p2.getX(), p2.getY());
        shape.lineTo(p3.getX(), p3.getY());

        shape.closePath();

        c1 = new Ellipse2D.Double(P1.getX()-r2, P1.getY()-r2, r2*2, r2*2);
        c2 = new Ellipse2D.Double(P2.getX()-r2, P2.getY()-r2, r2*2, r2*2);
        c3 = new Ellipse2D.Double(P3.getX()-r2, P3.getY()-r2, r2*2, r2*2);
    }

    public PolygonObstacle(Point2D p1, Point2D p2, Point2D p3, Point2D p4){
        P1=p1;
        P2=p2;
        P3=p3;
        P4=p4;
        segmentNumber=4;
        shape = new Path2D.Double();

        shape.moveTo(p1.getX(), p1.getY());
        shape.lineTo(p1.getX(), p1.getY());
        shape.lineTo(p2.getX(), p2.getY());
        shape.lineTo(p3.getX(), p3.getY());
        shape.lineTo(p4.getX(), p4.getY());

        shape.closePath();

        c1 = new Ellipse2D.Double(P1.getX()-r2, P1.getY()-r2, r2*2, r2*2);
        c2 = new Ellipse2D.Double(P2.getX()-r2, P2.getY()-r2, r2*2, r2*2);
        c3 = new Ellipse2D.Double(P3.getX()-r2, P3.getY()-r2, r2*2, r2*2);
        c4 = new Ellipse2D.Double(P4.getX()-r2, P4.getY()-r2, r2*2, r2*2);
    }

    public PolygonObstacle(Point2D p1, Point2D p2, Point2D p3, Point2D p4, Point2D p5){
        P1=p1;
        P2=p2;
        P3=p3;
        P4=p4;
        P5=p5;
        segmentNumber=5;
        shape = new Path2D.Double();

        shape.moveTo(p1.getX(), p1.getY());
        shape.lineTo(p1.getX(), p1.getY());
        shape.lineTo(p2.getX(), p2.getY());
        shape.lineTo(p3.getX(), p3.getY());
        shape.lineTo(p4.getX(), p4.getY());
        shape.lineTo(p5.getX(), p5.getY());

        shape.closePath();

        c1 = new Ellipse2D.Double(P1.getX()-r2, P1.getY()-r2, r2*2, r2*2);
        c2 = new Ellipse2D.Double(P2.getX()-r2, P2.getY()-r2, r2*2, r2*2);
        c3 = new Ellipse2D.Double(P3.getX()-r2, P3.getY()-r2, r2*2, r2*2);
        c4 = new Ellipse2D.Double(P4.getX()-r2, P4.getY()-r2, r2*2, r2*2);
        c5 = new Ellipse2D.Double(P5.getX()-r2, P5.getY()-r2, r2*2, r2*2);
    }

    public Path2D getShape() {
        return shape;
    }

    public boolean inCollision(Line2D l){
//        if (link2.intersectsLine(tri_obs_1.getX(), tri_obs_1.getY(), tri_obs_2.getX(), tri_obs_2.getY())){test_image_buffer.setRGB(i-1, j-1, 0xff00ffff);}//continue;}
        if (segmentNumber==3){
            if (l.intersectsLine(P1.getX(), P1.getY(), P2.getX(), P2.getY())){return true;}
            if (l.intersectsLine(P2.getX(), P2.getY(), P3.getX(), P3.getY())){return true;}
            if (l.intersectsLine(P3.getX(), P3.getY(), P1.getX(), P1.getY())){return true;}
        }
        else if (segmentNumber==4){
            if (l.intersectsLine(P1.getX(), P1.getY(), P2.getX(), P2.getY())){return true;}
            if (l.intersectsLine(P2.getX(), P2.getY(), P3.getX(), P3.getY())){return true;}
            if (l.intersectsLine(P3.getX(), P3.getY(), P4.getX(), P4.getY())){return true;}
            if (l.intersectsLine(P4.getX(), P4.getY(), P1.getX(), P1.getY())){return true;}
        }
        else {
            if (l.intersectsLine(P1.getX(), P1.getY(), P2.getX(), P2.getY())){return true;}
            if (l.intersectsLine(P2.getX(), P2.getY(), P3.getX(), P3.getY())){return true;}
            if (l.intersectsLine(P3.getX(), P3.getY(), P4.getX(), P4.getY())){return true;}
            if (l.intersectsLine(P4.getX(), P4.getY(), P5.getX(), P5.getY())){return true;}
            if (l.intersectsLine(P5.getX(), P5.getY(), P1.getX(), P1.getY())){return true;}
        }
        return false;
    }


    public void setPoint(int i, Point2D p){
            switch (i){
                case 1: P1.setLocation(p); c1.setFrame(P1.getX()-r2, P1.getY()-r2, r2*2, r2*2); break;
                case 2: P2.setLocation(p); c2.setFrame(P2.getX()-r2, P2.getY()-r2, r2*2, r2*2); break;
                case 3: P3.setLocation(p); c3.setFrame(P3.getX()-r2, P3.getY()-r2, r2*2, r2*2); break;
                case 4: P4.setLocation(p); c4.setFrame(P4.getX()-r2, P4.getY()-r2, r2*2, r2*2); break;
                case 5: P5.setLocation(p); c5.setFrame(P5.getX()-r2, P5.getY()-r2, r2*2, r2*2); break;
            }

            if (segmentNumber==3){
                shape.reset();
                shape.moveTo(P1.getX(), P1.getY());
                shape.lineTo(P1.getX(), P1.getY());
                shape.lineTo(P2.getX(), P2.getY());
                shape.lineTo(P3.getX(), P3.getY());
                shape.closePath();
            }
            if (segmentNumber==4){
                shape.reset();
                shape.moveTo(P1.getX(), P1.getY());
                shape.lineTo(P1.getX(), P1.getY());
                shape.lineTo(P2.getX(), P2.getY());
                shape.lineTo(P3.getX(), P3.getY());
                shape.lineTo(P4.getX(), P4.getY());
                shape.closePath();
            }
            if (segmentNumber==5){
                shape.reset();
                shape.moveTo(P1.getX(), P1.getY());
                shape.lineTo(P1.getX(), P1.getY());
                shape.lineTo(P2.getX(), P2.getY());
                shape.lineTo(P3.getX(), P3.getY());
                shape.lineTo(P4.getX(), P4.getY());
                shape.lineTo(P5.getX(), P5.getY());
                shape.closePath();
            }
            
    }

    public void setPoint(int i, double x, double y){
        Point2D.Double p = new Point2D.Double(x, y);
            switch (i){
                case 1: P1.setLocation(p); c1.setFrame(P1.getX()-r2, P1.getY()-r2, r2*2, r2*2); break;
                case 2: P2.setLocation(p); c2.setFrame(P2.getX()-r2, P2.getY()-r2, r2*2, r2*2); break;
                case 3: P3.setLocation(p); c3.setFrame(P3.getX()-r2, P3.getY()-r2, r2*2, r2*2); break;
                case 4: P4.setLocation(p); c4.setFrame(P4.getX()-r2, P4.getY()-r2, r2*2, r2*2); break;
                case 5: P5.setLocation(p); c5.setFrame(P5.getX()-r2, P5.getY()-r2, r2*2, r2*2); break;
            }

            if (segmentNumber==3){
                shape.reset();
                shape.moveTo(P1.getX(), P1.getY());
                shape.lineTo(P1.getX(), P1.getY());
                shape.lineTo(P2.getX(), P2.getY());
                shape.lineTo(P3.getX(), P3.getY());
                shape.closePath();
            }
            if (segmentNumber==4){
                shape.reset();
                shape.moveTo(P1.getX(), P1.getY());
                shape.lineTo(P1.getX(), P1.getY());
                shape.lineTo(P2.getX(), P2.getY());
                shape.lineTo(P3.getX(), P3.getY());
                shape.lineTo(P4.getX(), P4.getY());
                shape.closePath();
            }
            if (segmentNumber==5){
                shape.reset();
                shape.moveTo(P1.getX(), P1.getY());
                shape.lineTo(P1.getX(), P1.getY());
                shape.lineTo(P2.getX(), P2.getY());
                shape.lineTo(P3.getX(), P3.getY());
                shape.lineTo(P4.getX(), P4.getY());
                shape.lineTo(P5.getX(), P5.getY());
                shape.closePath();
            }

    }

    /**
     * Incremental Move!
     * @param x
     * @param y
     */
    public void updatePoints(int i){

//        if(segmentNumber==3){
//            c1.setFrame(P1.getX()-r2, P1.getY()-r2, r2*2, r2*2);
//            c2.setFrame(P2.getX()-r2, P2.getY()-r2, r2*2, r2*2);
//            c3.setFrame(P3.getX()-r2, P3.getY()-r2, r2*2, r2*2);
//        }
//
//        if(segmentNumber==4){
//            c1.setFrame(P1.getX()-r2, P1.getY()-r2, r2*2, r2*2);
//            c2.setFrame(P2.getX()-r2, P2.getY()-r2, r2*2, r2*2);
//            c3.setFrame(P3.getX()-r2, P3.getY()-r2, r2*2, r2*2);
//            c4.setFrame(P4.getX()-r2, P4.getY()-r2, r2*2, r2*2);
//        }
//
//        if(segmentNumber==5){
//            c1.setFrame(P1.getX()-r2, P1.getY()-r2, r2*2, r2*2);
//            c2.setFrame(P2.getX()-r2, P2.getY()-r2, r2*2, r2*2);
//            c3.setFrame(P3.getX()-r2, P3.getY()-r2, r2*2, r2*2);
//            c4.setFrame(P4.getX()-r2, P4.getY()-r2, r2*2, r2*2);
//            c5.setFrame(P5.getX()-r2, P5.getY()-r2, r2*2, r2*2);
//        }
        

    }



}
