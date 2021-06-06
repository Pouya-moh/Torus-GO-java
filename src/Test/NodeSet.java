/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import java.util.HashSet;

/**
 *
 * @author Poo
 */
public class NodeSet<N> {
    HashSet<RiccardoTree<N>> set;
    public NodeSet(RiccardoTree<N> root){
        set= root.getAllNodes();
    }

    public HashSet<RiccardoTree<N>> getSet() {
        return set;
    }

}
