/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package torus;

/**
 *
 * @author Pouya Mohammadi
 */
public class Configuration {
    public double[] configuration;
    private int confSize;


    public Configuration(int size){
        confSize = size;
        configuration = new double[confSize];
    }

    public Configuration(int size, double defaultValue){
        confSize = size;
        configuration = new double[confSize];

        for (int i = 0; i < size; i++) {
            configuration[i] = defaultValue;
        }
    }

    public Configuration(int size, int minRandValue, int maxRandValue){
        confSize = size;
        configuration = new double[confSize];
        //int x =1+(int)(Math.random()*(499));
        if (minRandValue == 0 && maxRandValue == 1) {
            for (int i = 0; i < size; i++) {
                configuration[i] = Math.random();
            }
        } else {
            for (int i = 0; i < size; i++) {
                configuration[i] = minRandValue + (int) (Math.random() * ((maxRandValue - minRandValue) + 1));
            }
        }
    }

    public boolean setValueAt(int index, double value){
        if (index>confSize) return false;

        configuration[index] = value;
        return true;
    }

    public int getSize(){
        return this.confSize;
    }

    public double getValueAt(int index){
        if (index>confSize) return 1000000;

        return configuration[index];
    }

    public double[] getDof() {
        return configuration;
    }

    public void printlnConf(){
        for (int i = 0; i < this.confSize; i++) {
            System.out.println(this.configuration[i]);
        }
    }

}
