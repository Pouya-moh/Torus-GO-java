/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package torus;

import java.awt.AWTException;
import javax.swing.UIManager;

/**
 *
 * @author Pouya Mohammadi.
 * @version Alpha Release.
 */
public class Main {

    public static GUI gui;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws AWTException {
        // uncomment to generate final dist
        //trying to set the application title

        try {
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
        } catch (Exception e) {
            System.out.println("Unable to set Look and Feel!");
        }

        System.setProperty("apple.laf.useScreenMenuBar", "true");

        // set the name of the application menu item
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "2R C-Space");


//    // set the look and feel
//    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //trying to set the application title
        gui = new GUI();
        gui.setVisible(true);

    }
}
