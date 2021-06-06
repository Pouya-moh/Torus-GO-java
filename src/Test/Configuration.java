/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

/**
 *
 * @author pouya
 */
public class Configuration {
    public int theta1;
    public int theta2;

    /**
     * Creates a configuration of (0,0)
     */
    public Configuration(){
        theta1 = 0;
        theta2 = 0;
    }

    public Configuration(int q1, int q2){
        theta1 = q1;
        theta2 = q2;
    }

    public Configuration(boolean RandomConfiguration){
        theta1 = (int) (Math.random() * ((359) + 1));
        theta2 = (int) (Math.random() * ((359) + 1));
    }

    public void print(){
        System.out.println(this.theta1+" , "+this.theta2);
    }

    public void print(String s){
        System.out.println(s+" = "+this.theta1+" , "+this.theta2);
    }

    @Override
    public String toString() {
        return "Configuration{" + "theta1=" + theta1 + "theta2=" + theta2 + '}';
    }

    


}
