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
public class BackTrack<N> {
    private RiccardoTree<N> tree;
    private ArrayList<N> path;
    private N goal;
    private N init;

    public BackTrack(RiccardoTree<N> t, N INIT, N GOAL){
        tree = t;
        goal = GOAL;
        init = INIT;
    }

    public ArrayList<N> getPath(){
        ArrayList<N> ret = new ArrayList<N>();
        RiccardoTree<N> temp = tree.getNode(goal);
        boolean flag=true;
        while (flag) {
            ret.add(temp.getObjectNode());
            temp = temp.getParent();
            if ((temp == null) || (temp.getObjectNode().equals(init))) {
                flag = false;
                ret.add(init);
            }
        }

        return ret;
    }
}
