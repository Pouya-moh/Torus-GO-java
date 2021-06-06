/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package torus;

/**
 *
 * @author Poo
 */
public final class LocalPlanner {
//    public Configuration source;
//    public Configuration destination;
    public boolean pathExist;
    private Configuration[] path;
    public int distance;


    public LocalPlanner(Configuration source, Configuration destination){
        pathExist(source, destination);
    }

    public LocalPlanner(){
        distance=0;
    }

    public Configuration[] pathBetween(Configuration c1, Configuration c2){

        int t=0;
        int t1=0;
        int t2=0;

        int length = (int) Math.round(this.calcDistBetween(c1,c2));
        Configuration ret[] = new Configuration [length];

            for (int i=1;i<length;i++){
                Configuration c = null;
                //THETA1
                if (Math.abs(c2.configuration[0]-c1.configuration[0])>180){t=(int) (Math.max(c2.configuration[0], c1.configuration[0]) - 360);} else {t=(int) Math.max(c2.configuration[0], c1.configuration[0]);}
                if (c2.configuration[0]>c1.configuration[0]){t1=(int) Math.round(((i*c1.configuration[0])+(length-i)*t)/length);}
                if (c2.configuration[0]<c1.configuration[0]){t1=(int) Math.round(((i*t)+(length-i)*c2.configuration[0])/length);}
                //THETA2
                if (Math.abs(c2.configuration[1]-c1.configuration[1])>180){t=(int) (Math.max(c2.configuration[1], c1.configuration[1]) - 360);} else {t=(int) Math.max(c2.configuration[1], c1.configuration[1]);}
                if (c2.configuration[1]>c1.configuration[1]){t2=(int) Math.round(((i*c1.configuration[1])+(length-i)*t)/length);}
                if (c2.configuration[1]<c1.configuration[1]){t2=(int) Math.round(((i*t)+(length-i)*c2.configuration[1])/length);}
                //Saving!
                ret[i]=new Configuration(2);
                ret[i].setValueAt(0, t1);
                ret[i].setValueAt(1, t2);
            }
        ret[0]=c2;
        path=ret;
        return ret;
    }

    public double calcDistBetween(Configuration c1, Configuration c2){
        double ret=0;
        double t1,t2;
            if (Math.abs(c2.configuration[0]-c1.configuration[0])>180) {t1=Math.abs((Math.max(c2.configuration[0], c1.configuration[0])-360)-(Math.min(c2.configuration[0], c1.configuration[0])));} else {t1=Math.abs(c2.configuration[0]-c1.configuration[0]);}
            if (Math.abs(c2.configuration[1]-c1.configuration[1])>180) {t2=Math.abs((Math.max(c2.configuration[1], c1.configuration[1])-360)-(Math.min(c2.configuration[1], c1.configuration[1])));} else {t2=Math.abs(c2.configuration[1]-c1.configuration[1]);}

            ret=Math.sqrt(Math.pow(t1, 2)+Math.pow(t2, 2));


        return ret;
    }



    public boolean pathExist(Configuration c1, Configuration c2){
        Configuration _path[] = pathBetween(c1, c2);

        for (int i=0; i<_path.length; i++){
//            tmp.setConfiguration(_path[i]);
//            if (ws_tmp.inCollision()){pathExist=false; return false;}
            Configuration tmp;
            tmp = _path[i];
            Main.gui.Robot.setConfig((int)tmp.configuration[0], (int)tmp.configuration[1]);
        }
        pathExist=true;
        return true;
    }

    public Configuration[] getPath() {
        return path;
    }

//    public Configuration findQnew(Configuration Qold, Configuration Qrand, int threshold){
//        Configuration Qnew=null;
//        LocalPlanner lp = new LocalPlanner();
//        double dist = lp.calcDistBetween(Qold, Qrand);
//        if (dist>threshold){
//            int x = (int) (((dist - threshold) * Qold.X + threshold * Qrand.X) / dist);
//            int y = (int) (((dist - threshold) * Qold.Y + threshold * Qrand.Y) / dist);
//            int t1 = (int) (((dist - threshold) * Qold.Theta1 + threshold * Qrand.Theta1) / dist);
//            int t2 = (int) (((dist - threshold) * Qold.Theta2 + threshold * Qrand.Theta2) / dist);
//            int t3 = (int) (((dist - threshold) * Qold.Theta3 + threshold * Qrand.Theta3) / dist);
//            int t4 = (int) (((dist - threshold) * Qold.Theta4 + threshold * Qrand.Theta4) / dist);
//            int t5 = (int) (((dist - threshold) * Qold.Theta5 + threshold * Qrand.Theta5) / dist);
//            int t6 = (int) (((dist - threshold) * Qold.Theta6 + threshold * Qrand.Theta6) / dist);
//            int t7 = (int) (((dist - threshold) * Qold.Theta7 + threshold * Qrand.Theta7) / dist);
//            Qnew = new Configuration(x, y, t1, t2, t3, t4, t5, t6, t7);
//        } else Qnew = Qrand;
//        return Qnew;
//    }





}
