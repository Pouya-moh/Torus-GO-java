/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Poo
 */
public class RiccardoTree <N>{
    private RiccardoTree parent;
    private HashSet<RiccardoTree> children;
    private N objectNode;

    private RiccardoTree(RiccardoTree parent, N name) {
        this.parent = parent;
        this.objectNode = name;
        children=new HashSet<RiccardoTree>();
    }

    public RiccardoTree(N o) {
        this.objectNode = o;
        children=new HashSet<RiccardoTree>();
    }

    public HashSet<RiccardoTree> getChildren() {
        return children;
    }

    public N getObjectNode() {
        return objectNode;
    }

    public RiccardoTree<N> getParent() {
        return parent;
    }

    public RiccardoTree addChild(N name){
        RiccardoTree n=new RiccardoTree(this,name);
        if(children.add(n))return n;
        return null;

    }

    public Iterator<RiccardoTree> childrenIterator(){
        return children.iterator();
    }

    

    public boolean isRoot(){
        return parent==null;
    }

    public boolean isLeaf(){
        return children.isEmpty();
    }

    public RiccardoTree addChild(N nodeInTheTree, N newNode){
        if(objectNode.equals(nodeInTheTree)){
            return addChild(newNode);
        }
        else if(isLeaf())return null;

        Iterator<RiccardoTree> childrenIterator = childrenIterator();
        while(childrenIterator.hasNext()){
            RiccardoTree next = childrenIterator.next();
            RiccardoTree addedchild = next.addChild(nodeInTheTree,newNode);
            if(addedchild !=null) return addedchild;
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RiccardoTree<N> other = (RiccardoTree<N>) obj;
        if (this.objectNode != other.objectNode && (this.objectNode == null || !this.objectNode.equals(other.objectNode))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.objectNode != null ? this.objectNode.hashCode() : 0);
        return hash;
    }

    public RiccardoTree getNode(N node){
        if(objectNode.equals(node)){
            return this;
        }
        else if(isLeaf())return null;

        Iterator<RiccardoTree> childrenIterator = childrenIterator();
        while(childrenIterator.hasNext()){
            RiccardoTree next = childrenIterator.next();
            RiccardoTree ret=next.getNode(node);
            if(ret!=null)return ret;

        }
        return null;
    }

    
    public HashSet<RiccardoTree<N>> getAllNodes(){
        HashSet ret= new HashSet();
        ret.add(this);

        Iterator<RiccardoTree> childrenIterator = childrenIterator();
        while(childrenIterator.hasNext()){
            RiccardoTree next = childrenIterator.next();
            ret.addAll(next.getAllNodes());

        }
        return ret;
    }


}

