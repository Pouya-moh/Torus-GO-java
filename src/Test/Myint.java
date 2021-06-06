/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;



/**
 *
 * @author Poo
 */
public class Myint {
    private int value;
    private static final int MAX_VALUE = 359;
    private static final int MIN_VALUE = 0;

    public Myint(){
        this.value=0;
    }

    public Myint(int value){
        this.value=value;
    }

    public void inc(){
        if (this.value==359) {
            this.value = 0;
        }else this.value++;
    }

    public void dec(){
        if (this.value==0){
            this.value=359;
        }else this.value--;
    }
    public int value(){
        return this.value;
    }

}
