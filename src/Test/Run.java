/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import java.util.ArrayList;

/**
 *
 * @author Poo
 */
public class Run {
    public static void main(String[] args) {


        Configuration c0,c1,c2,c3,c4,c5,c6,c7,c8,c9;


        c0 = new Configuration(0,0);
        c1 = new Configuration(0,1);
        c2 = new Configuration(0,2);
        c3 = new Configuration(0,3);
        c4 = new Configuration(0,4);
        c5 = new Configuration(0,5);
        c6 = new Configuration(0,6);
        c7 = new Configuration(0,7);
        c8 = new Configuration(0,8);
        c9 = new Configuration(0,9);

        RiccardoTree<Configuration> tree = new RiccardoTree(c0);
        
        tree.addChild(c1);
        tree.addChild(c1, c2);
        tree.addChild(c2, c3);
        tree.addChild(c2, c4);
        tree.addChild(c2, c5);
        tree.addChild(c3, c6);
        tree.addChild(c3, c7);
        tree.addChild(c6, c8);

        tree.addChild(c9);

        ArrayList<Configuration> l = new BackTrack<Configuration>(tree, c0, c7).getPath();

        System.out.println(l);


    }
}
