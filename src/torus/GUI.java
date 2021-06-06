package torus;

import Test.RiccardoTree;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.swing.ImageIcon;
import javax.vecmath.Point3d;
import Test.Configuration;
import Test.NodeSet;
import Test.PlanningTools;
import Test.RRT;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Pouya Mohammadi
 * Created on Aug 24, 2011, 7:34:13 PM
 */
public class GUI extends javax.swing.JFrame {


    public Shape3D torus;
    public Appearance app;
    public Texture tex;
    public SimpleUniverse universe;
    public Scene t1;
    public Canvas3D canvas;
    public BranchGroup root;
    public TextureLoader texLoader;
    public int torus_flag;
    public int lineType=3;
    public BasicStroke Line[] = new BasicStroke[7];
    public int resolution=1;
    public Test.Configuration init;
    public Test.Configuration goal;
    public RRT rrt;
    public Test.Configuration[] rrtPath;
    public ArrayList<Test.Configuration> rrtPathAsArrayList;
    public AffineTransform translationMatrix;
    public Point oldMousePose;
    public int x_red_robot_old=2;
    public int y_red_robot_old=2;
//    public int[] old_colors_cSpace;
    public int[] old_colors_torus_cSpace;
//    boolean someone_is_glued_gloal = false;


    Robot2R Robot;
    Robot r_mouse;
    CircleObstacle circle1,circle2,circle3;
    PolygonObstacle rectangle;
    PolygonObstacle triangle;
    BufferedImage c_space,c_space_torus,gradient,c_space_clear, red_dot_robot;
    boolean cf1,cf2,cf3,tf1,tf2,tf3,rf1,rf2,rf3,rf4;
    private final BufferStrategy strategy;
    /** Creates new form GUI */
    public GUI() throws AWTException {
        initComponents();
        jLabel6.setText("<html> <div align=\"center\">  <i>2R C-Space (Alpha Release)</i><br><br><br> Designed and developed by <i><b>Pouya Mohammadi</b></i>   <br> <br><br> This software is created only for educational purposes <br>   Feel free to share it under this condition <br><br>Special Edition for<br>Pro.<b>Giuseppe Oriolo</b><br><br><br> If you have any feedback, suggestions or bug-report<br> do not hesitate to send an email to:");
//        jPanel1.setDoubleBuffered(true);
        this.setLayout(null);
        try {
            gradient = ImageIO.read((GUI.class.getClassLoader().getResource("torus/gradient.jpg")));
        } catch (IOException e) {
        }
        c_space_torus=new BufferedImage(360, 360, BufferedImage.TYPE_INT_RGB);
        //testing
        c_space      =new BufferedImage(360, 360, BufferedImage.TYPE_INT_RGB);
        //testing
        c_space_clear=new BufferedImage(360, 360, BufferedImage.TYPE_INT_RGB);
        for(int i=1;i<=360;i++){
                for(int j=1;j<=360;j++){
                    c_space_clear.setRGB(i-1, j-1, 0xffffff);
            }
        }

//        old_colors_cSpace = new int[9];
        old_colors_torus_cSpace = new int[9];
        for (int i= 0; i < 9; i++) {
//            old_colors_cSpace[i]=0xFFFFFF;
            old_colors_torus_cSpace[i]=0xFFFFFF;
        }

        //Setting RED_DOT_ROBOT! :D
        red_dot_robot = new BufferedImage(7, 7, BufferedImage.TYPE_INT_RGB);
        for(int i=0;i<7;i++){
                for(int j=0;j<7;j++){
                    red_dot_robot.setRGB(i, j, 0xff0000);
            }
        }
        ImageIcon img_red_dot_robot = new ImageIcon(red_dot_robot);
        jLabel_Red_Dot_Robot.setIcon(img_red_dot_robot);

        translationMatrix = new AffineTransform();


        r_mouse = new Robot();

        jSlider2.setPaintTicks(true);
        jSlider2.setMajorTickSpacing(90);
        jSlider2.setMinorTickSpacing(45);
        jSlider2.setPaintLabels(true);

        jSlider3.setPaintTicks(true);
        jSlider3.setMajorTickSpacing(90);
        jSlider3.setMinorTickSpacing(45);
        jSlider3.setPaintLabels(true);

        jSlider5.setPaintTicks(true);
        jSlider5.setMajorTickSpacing(42);
        jSlider5.setPaintLabels(true);

        jSlider4.setPaintTicks(true);
        jSlider4.setMajorTickSpacing(21);
        jSlider4.setPaintLabels(true);

        jSlider6.setPaintTicks(true);
        jSlider6.setMajorTickSpacing(1);
        jSlider6.setPaintLabels(true);

        Robot=new Robot2R(jPanel1.getHeight()/2, jPanel1.getWidth()/2, 100);

        Line[6]=new BasicStroke(7);
        Line[5]=new BasicStroke(6);
        Line[4]=new BasicStroke(5);
        Line[3]=new BasicStroke(4);
        Line[2]=new BasicStroke(3);
        Line[1]=new BasicStroke(2);
        Line[0]=new BasicStroke(1);


        circle1 = new CircleObstacle(new Point2D.Double(120,135), 30);
        circle2 = new CircleObstacle(new Point2D.Double(115,240), 50);
        circle3 = new CircleObstacle(new Point2D.Double(270,265), 70);
        triangle = new PolygonObstacle(new Point2D.Double(54, 53), new Point2D.Double(174, 41), new Point2D.Double(118, 90));
        rectangle = new PolygonObstacle(new Point2D.Double(193,61), new Point2D.Double(314,61), new Point2D.Double(268,160), new Point2D.Double(193,131));
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        calculateC_Space();
        makeTorus();
        updateTorus();
    }

    @Override
        public void paint (Graphics g) {
        super.paintComponents(g);

        Graphics2D g2 = (Graphics2D) jPanel1.getGraphics();
        // The following is the original calculation of cspace. The next block is for jpl version. switch comments to have normal behaviour
        
        if (jCheckBoxQuad.isSelected()){
            g2.setStroke(Line[0]);
            g2.setColor(Color.magenta);
            g2.draw(rectangle.shape);
            g2.fill(rectangle.shape);
            if (rf1) g2.draw(rectangle.c1);
            if (rf2) g2.draw(rectangle.c2);
            if (rf3) g2.draw(rectangle.c3);
            if (rf4) g2.draw(rectangle.c4);
        }

        if (jCheckBoxCirc1.isSelected()){
            g2.setStroke(Line[0]);
            g2.setColor(Color.yellow);
            g2.draw(circle1.shape);
            g2.fill(circle1.shape);
            if (cf1) g2.draw(circle1.cc);
        }

        if (jCheckBoxCirc2.isSelected()){
            g2.setStroke(Line[0]);
            g2.setColor(Color.gray);
            g2.draw(circle2.shape);
            g2.fill(circle2.shape);
            if (cf2) g2.draw(circle2.cc);
        }

        if (jCheckBoxCirc3.isSelected()){
            g2.setStroke(Line[0]);
            g2.setColor(Color.gray);
            g2.draw(circle3.shape);
            g2.fill(circle3.shape);
            if (cf3) g2.draw(circle3.cc);
        }

        if (jCheckBoxTri.isSelected()){
            g2.setStroke(Line[0]);
            g2.setColor(Color.cyan);
            g2.draw(triangle.shape);
            g2.fill(triangle.shape);
            if (tf1) g2.draw(triangle.c1);
            if (tf2) g2.draw(triangle.c2);
            if (tf3) g2.draw(triangle.c3);
        }
        /*
        if (jCheckBoxQuad.isSelected()){
            g2.setStroke(Line[0]);
            g2.setColor(Color.GRAY);
            g2.draw(rectangle.shape);
            g2.fill(rectangle.shape);
            if (rf1) g2.draw(rectangle.c1);
            if (rf2) g2.draw(rectangle.c2);
            if (rf3) g2.draw(rectangle.c3);
            if (rf4) g2.draw(rectangle.c4);
        }

        if (jCheckBoxCirc1.isSelected()){
            g2.setStroke(Line[0]);
            g2.setColor(Color.cyan);
            g2.draw(circle1.shape);
            g2.fill(circle1.shape);
            if (cf1) g2.draw(circle1.cc);
        }

        if (jCheckBoxCirc2.isSelected()){
            g2.setStroke(Line[0]);
            g2.setColor(Color.magenta);
            g2.draw(circle2.shape);
            g2.fill(circle2.shape);
            if (cf2) g2.draw(circle2.cc);
        }

        if (jCheckBoxCirc3.isSelected()){
            g2.setStroke(Line[0]);
            g2.setColor(Color.yellow);
            g2.draw(circle3.shape);
            g2.fill(circle3.shape);
            if (cf3) g2.draw(circle3.cc);
        }

        if (jCheckBoxTri.isSelected()){
            g2.setStroke(Line[0]);
            g2.setColor(Color.GRAY);
            g2.draw(triangle.shape);
            g2.fill(triangle.shape);
            if (tf1) g2.draw(triangle.c1);
            if (tf2) g2.draw(triangle.c2);
            if (tf3) g2.draw(triangle.c3);
        }
         */
        g2.setColor(Color.black);
        g2.setStroke(Line[lineType]);
        g2.draw(Robot.Link1);
        g2.draw(Robot.Link2);
        g2.draw(Robot.visBase);

        g2.setColor(Color.GRAY);
        g2.draw(Robot.visEndEffector);
        g2.draw(Robot.visJoint);

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSlider2 = new javax.swing.JSlider();
        jSlider3 = new javax.swing.JSlider();
        jSlider4 = new javax.swing.JSlider();
        jSlider5 = new javax.swing.JSlider();
        jSlider6 = new javax.swing.JSlider();
        jPanel4 = new javax.swing.JPanel();
        jCheckBoxTri = new javax.swing.JCheckBox();
        jCheckBoxQuad = new javax.swing.JCheckBox();
        jCheckBoxCirc1 = new javax.swing.JCheckBox();
        jCheckBoxCirc2 = new javax.swing.JCheckBox();
        jCheckBoxCirc3 = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        c_space_auto_update = new javax.swing.JCheckBox();
        jCheckBoxShowOnTorus = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jSlider1 = new javax.swing.JSlider();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel_Red_Dot_Robot = new javax.swing.JLabel();
        jLabel_c_space = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("2R C-Space");
        setMinimumSize(new java.awt.Dimension(842, 800));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(-16777216,true)));
        jPanel1.setPreferredSize(new java.awt.Dimension(360, 360));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jPanel1MouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel1MouseClicked(evt);
            }
        });
        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jPanel1MouseMoved(evt);
            }
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel1MouseDragged(evt);
            }
        });
        jPanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPanel1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jPanel1KeyReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 358, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 358, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(-16777216,true)));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Joint 1");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel2.setText("Joint 2");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        jLabel3.setText("Link Ratio");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        jLabel4.setText("Robot Length");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, -1, -1));

        jLabel5.setText("Line Weight");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));

        jSlider2.setMaximum(360);
        jSlider2.setPaintTicks(true);
        jSlider2.setValue(0);
        jSlider2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider2StateChanged(evt);
            }
        });
        jPanel3.add(jSlider2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 25, 230, -1));

        jSlider3.setMaximum(360);
        jSlider3.setPaintTicks(true);
        jSlider3.setValue(0);
        jSlider3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider3StateChanged(evt);
            }
        });
        jPanel3.add(jSlider3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 75, 230, -1));

        jSlider4.setMinimum(2);
        jSlider4.setPaintTicks(true);
        jSlider4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider4StateChanged(evt);
            }
        });
        jPanel3.add(jSlider4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 125, 230, -1));

        jSlider5.setMaximum(182);
        jSlider5.setMinimum(2);
        jSlider5.setPaintTicks(true);
        jSlider5.setValue(100);
        jSlider5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider5StateChanged(evt);
            }
        });
        jPanel3.add(jSlider5, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 175, 230, -1));

        jSlider6.setMaximum(7);
        jSlider6.setMinimum(1);
        jSlider6.setPaintTicks(true);
        jSlider6.setSnapToTicks(true);
        jSlider6.setValue(3);
        jSlider6.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider6StateChanged(evt);
            }
        });
        jPanel3.add(jSlider6, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 225, 230, -1));

        jTabbedPane1.addTab("Robot", jPanel3);

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jCheckBoxTri.setText("Triangle Obstacle");
        jCheckBoxTri.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxTriStateChanged(evt);
            }
        });
        jPanel4.add(jCheckBoxTri, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, -1, -1));

        jCheckBoxQuad.setText("Rectangle Obstacle");
        jCheckBoxQuad.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxQuadStateChanged(evt);
            }
        });
        jPanel4.add(jCheckBoxQuad, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        jCheckBoxCirc1.setText("Circular Obstacle 1");
        jCheckBoxCirc1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxCirc1StateChanged(evt);
            }
        });
        jPanel4.add(jCheckBoxCirc1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 130, -1, -1));

        jCheckBoxCirc2.setText("Circular Obstacle 2");
        jCheckBoxCirc2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxCirc2StateChanged(evt);
            }
        });
        jPanel4.add(jCheckBoxCirc2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, -1, -1));

        jCheckBoxCirc3.setText("Circular Obstacle 3");
        jCheckBoxCirc3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBoxCirc3StateChanged(evt);
            }
        });
        jPanel4.add(jCheckBoxCirc3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, -1, -1));

        jTabbedPane1.addTab("Obstacles", jPanel4);

        jCheckBox1.setSelected(true);
        jCheckBox1.setLabel("Show Path in C-Space");

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("High Resolution");
        jCheckBox2.setToolTipText("<html>\nDetermines if the configuratio space is calculated with high resolution of not.\n<br>If you have slow computer maybe it's better to keep this unchecked.\n<br>Please metion that this setting, has no effect on the accuracy of RRT.");
        jCheckBox2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox2StateChanged(evt);
            }
        });

        c_space_auto_update.setSelected(true);
        c_space_auto_update.setText("Auto update the c-space");
        c_space_auto_update.setToolTipText("<html>\nCheck this if you want to update the configuration space as you change the worksapce\n<br>i.e. moveing the obstacles or changing the the robot size and link length.");
        c_space_auto_update.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                c_space_auto_updateStateChanged(evt);
            }
        });

        jCheckBoxShowOnTorus.setSelected(true);
        jCheckBoxShowOnTorus.setText("Show Robot Configuration on Torus");

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(46, 46, 46)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jCheckBoxShowOnTorus)
                    .add(c_space_auto_update)
                    .add(jCheckBox2)
                    .add(jCheckBox1))
                .addContainerGap(170, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(34, 34, 34)
                .add(jCheckBox1)
                .add(27, 27, 27)
                .add(jCheckBox2)
                .add(27, 27, 27)
                .add(c_space_auto_update)
                .add(30, 30, 30)
                .add(jCheckBoxShowOnTorus)
                .addContainerGap(120, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Settings", jPanel7);

        jPanel6.setToolTipText("<html>\n<p>Modified RRT\n<ol>\n<li><b>Goal Bias:</b> With this probability, in each iteration, Q<sub>goal</sub> is considered as Q<sub>rand</sub>.</li>\n<li><b>Threshold:</b> If the distance between Q<sub>goal</sub> and Q<sub>new</sub> is less than threshold, RRT tries to connect them.</li>\n<li><b>Delta:</b> The amount of extension from Q<sub>near</sub> toward Q<sub>rand</sub> to achieve Q<sub>new</sub>.</li>\n</ol>\n</html> ");
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setText("Init");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, -1, -1));

        jButton2.setText("Goal");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, -1, -1));
        jPanel6.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 60, -1));
        jPanel6.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 60, -1));
        jPanel6.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, 60, -1));
        jPanel6.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 50, 60, -1));

        jButton4.setText("Find Path!");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 230, 110, -1));

        jSlider1.setValue(1);
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });
        jPanel6.add(jSlider1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 370, -1));

        jLabel7.setText("q2");
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 20, 30));

        jLabel8.setText("q1");
        jPanel6.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 26, 20, 20));

        jLabel9.setText("q1");
        jPanel6.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 20, 30));

        jLabel10.setText("q2");
        jPanel6.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 20, 30));

        jLabel11.setText("Goal Bias");
        jPanel6.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, -1, 30));

        jLabel12.setText("Threshold");
        jPanel6.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 130, -1, 30));

        jLabel13.setText("Delta");
        jPanel6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, -1, 30));

        jLabel14.setText("Iterations");
        jPanel6.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, -1, 30));

        jTextField5.setText("0.25");
        jPanel6.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 150, -1));

        jTextField6.setText("50");
        jPanel6.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 150, -1));

        jTextField7.setText("20");
        jPanel6.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, 150, -1));

        jTextField8.setText("25000");
        jPanel6.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 190, 150, -1));

        jButton3.setText("Clear Path");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, 110, -1));

        jTabbedPane1.addTab("RRT", jPanel6);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("<html> <div align=\"center\">  <i>2R C-Space (Alpha Release)</i><br><br><br> Designed and developed by <i><b>Pouya Mohammadi</b></i>   <br> <br><br> This software is created only for educational purposes <br>   Feel free to share it under this condition <br><br>Special Edition for<br>Pro.<b> Jean-Paul Laumond</b><br><br><br> If you have any feedback, suggestions or bug-report<br> do not hesitate to send an email to:");
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/torus/Untitled2.png"))); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jLabel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5Layout.createSequentialGroup()
                        .add(jLabel15)
                        .add(98, 98, 98))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 255, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel15)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jLabel6.getAccessibleContext().setAccessibleName("<html> <div align=\"center\">  <i>2R C-Space (Alpha Release)</i><br><br><br> Designed and developed by <i><b>Pouya Mohammadi</b></i>   <br> <br><br> This software is created only for educational purposes <br>   Feel free to share it under this condition <br><br>Special Edition for<br>Pro.<b>Giuseppe Oriolo</b><br><br><br> If you have any feedback, suggestions or bug-report<br> do not hesitate to send an email to:");

        jTabbedPane1.addTab("About", jPanel5);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, 420, 360));

        jLabel_Red_Dot_Robot.setDoubleBuffered(true);
        getContentPane().add(jLabel_Red_Dot_Robot, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 397, 7, 7));

        jLabel_c_space.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(-16777216,true)));
        jLabel_c_space.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        jLabel_c_space.setDoubleBuffered(true);
        jLabel_c_space.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel_c_spaceMouseClicked(evt);
            }
        });
        jLabel_c_space.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jLabel_c_spaceMouseDragged(evt);
            }
        });
        getContentPane().add(jLabel_c_space, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 360, 360));

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(-16777216,true)));

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 418, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 358, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 400, 420, 360));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel_c_spaceMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_c_spaceMouseDragged

        Point pose=evt.getPoint();

        if(pose.x==360) {r_mouse.mouseMove(this.getX()+jLabel_c_space.getX()+1,this.getY()+jLabel_c_space.getY()+22+pose.y);}
        if(pose.x==0)   {r_mouse.mouseMove(this.getX()+jLabel_c_space.getX()+359,this.getY()+jLabel_c_space.getY()+22+pose.y);}
        if(pose.y==360) {r_mouse.mouseMove(this.getX()+jLabel_c_space.getX()+pose.x,this.getY()+jLabel_c_space.getY()+22+1);}
        if(pose.y==0)   {r_mouse.mouseMove(this.getX()+jLabel_c_space.getX()+pose.x,this.getY()+jLabel_c_space.getY()+359+22);}


//        if (jCheckBox1.isSelected()&&pose.x!=360&&pose.x!=359&&pose.x!=0&&pose.x!=1&&pose.y!=360&&pose.y!=359&&pose.y!=0&&pose.y!=1){
//            c_space.setRGB(pose.x, pose.y, 0);
//            c_space.setRGB(pose.x-1, pose.y, 0);
//            c_space.setRGB(pose.x+1, pose.y, 0);
//            c_space.setRGB(pose.x, pose.y-1, 0);
//            c_space.setRGB(pose.x, pose.y+1, 0);
//
//            c_space.setRGB(pose.x-1, pose.y-1, 0);
//            c_space.setRGB(pose.x+1, pose.y+1, 0);
//            c_space.setRGB(pose.x+1, pose.y-1, 0);
//            c_space.setRGB(pose.x-1, pose.y+1, 0);
//
//            c_space_torus.setRGB(pose.x, pose.y, 0);
//            c_space_torus.setRGB(pose.x-1, pose.y, 0);
//            c_space_torus.setRGB(pose.x+1, pose.y, 0);
//            c_space_torus.setRGB(pose.x, pose.y-1, 0);
//            c_space_torus.setRGB(pose.x, pose.y+1, 0);
//
//            c_space_torus.setRGB(pose.x-1, pose.y-1, 0);
//            c_space_torus.setRGB(pose.x+1, pose.y+1, 0);
//            c_space_torus.setRGB(pose.x+1, pose.y-1, 0);
//            c_space_torus.setRGB(pose.x-1, pose.y+1, 0);
//        }
        
        jSlider3.setValue(pose.y);
        jSlider2.setValue(pose.x);
        Robot.setConfig(pose.x, pose.y);
        
        repaint();
        updateTorus();
    }//GEN-LAST:event_jLabel_c_spaceMouseDragged

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged

        Point2D pose = new Point2D.Double(evt.getX(), evt.getY());
        boolean someone_is_glued_local = false;

//        if (!evt.isShiftDown()){
                if ((!someone_is_glued_local)&&(jCheckBoxCirc1.isSelected())){
                    if (circle1.cc.contains(pose)){circle1.setRadius((int)(pose.getX()-circle1.center.getX()));someone_is_glued_local=true;}
                    if (circle1.getShape().contains(pose)){circle1.setCenter(pose);someone_is_glued_local=true;}
                }

                if ((!someone_is_glued_local)&&(jCheckBoxCirc2.isSelected())){
                    if (circle2.cc.contains(pose)){circle2.setRadius((int)(pose.getX()-circle2.center.getX()));someone_is_glued_local=true;}
                    if (circle2.getShape().contains(pose)){circle2.setCenter(pose);someone_is_glued_local=true;}
                }

                if ((!someone_is_glued_local)&&(jCheckBoxCirc3.isSelected())){
                    if (circle3.cc.contains(pose)){circle3.setRadius((int)(pose.getX()-circle3.center.getX()));someone_is_glued_local=true;}
                    if (circle3.getShape().contains(pose)){circle3.setCenter(pose);someone_is_glued_local=true;}
                }

                if((!someone_is_glued_local)&&(jCheckBoxTri.isSelected())){
                    if (triangle.c1.contains(pose)){triangle.setPoint(1,pose);someone_is_glued_local=true;}
                    if (triangle.c2.contains(pose)){triangle.setPoint(2,pose);someone_is_glued_local=true;}
                    if (triangle.c3.contains(pose)){triangle.setPoint(3,pose);someone_is_glued_local=true;}

                    if ((triangle.getShape().contains(pose))&&(!someone_is_glued_local)) {
                        translationMatrix.setToTranslation(pose.getX()-oldMousePose.x, pose.getY()-oldMousePose.y);
                        triangle.getShape().transform(translationMatrix);

                        triangle.setPoint(1,triangle.P1.getX()+pose.getX()-oldMousePose.x, triangle.P1.getY()+pose.getY()-oldMousePose.y);
                        triangle.setPoint(2,triangle.P2.getX()+pose.getX()-oldMousePose.x, triangle.P2.getY()+pose.getY()-oldMousePose.y);
                        triangle.setPoint(3,triangle.P3.getX()+pose.getX()-oldMousePose.x, triangle.P3.getY()+pose.getY()-oldMousePose.y);

                        oldMousePose.x = evt.getX();
                        oldMousePose.y = evt.getY();

                        someone_is_glued_local=true;
                    }
                }

                if ((!someone_is_glued_local)&&(jCheckBoxQuad.isSelected())){
                    if (rectangle.c1.contains(pose)){rectangle.setPoint(1,pose);someone_is_glued_local=true;}
                    if (rectangle.c2.contains(pose)){rectangle.setPoint(2,pose);someone_is_glued_local=true;}
                    if (rectangle.c3.contains(pose)){rectangle.setPoint(3,pose);someone_is_glued_local=true;}
                    if (rectangle.c4.contains(pose)){rectangle.setPoint(4,pose);someone_is_glued_local=true;}

                    if (rectangle.getShape().contains(pose)&&(!someone_is_glued_local)) {
                        translationMatrix.setToTranslation(pose.getX()-oldMousePose.x, pose.getY()-oldMousePose.y);
                        rectangle.getShape().transform(translationMatrix);

                        rectangle.setPoint(1,rectangle.P1.getX()+pose.getX()-oldMousePose.x, rectangle.P1.getY()+pose.getY()-oldMousePose.y);
                        rectangle.setPoint(2,rectangle.P2.getX()+pose.getX()-oldMousePose.x, rectangle.P2.getY()+pose.getY()-oldMousePose.y);
                        rectangle.setPoint(3,rectangle.P3.getX()+pose.getX()-oldMousePose.x, rectangle.P3.getY()+pose.getY()-oldMousePose.y);
                        rectangle.setPoint(4,rectangle.P4.getX()+pose.getX()-oldMousePose.x, rectangle.P4.getY()+pose.getY()-oldMousePose.y);

                        oldMousePose.x = evt.getX();
                        oldMousePose.y = evt.getY();

                        someone_is_glued_local=true;
                    }
                }
            repaint();
            if ((c_space_auto_update.isSelected())&&(someone_is_glued_local)){
                calculateC_Space();
                updateTorus();
            }
//        }else{ //setting robot pose
        if ((!someone_is_glued_local)&&(evt.getButton()==1)){
            someone_is_glued_local=true;
            Point pose_inv = new Point(evt.getX()-180, evt.getY()-180);
            double c2 = (Math.pow(pose_inv.getX(),2)+Math.pow(pose_inv.getY(),2)-Math.pow(Robot.getLinkLength_1(),2)-Math.pow(Robot.getLinkLength_2(), 2))/(2*Robot.getLinkLength_1()*Robot.getLinkLength_2());
            double s2;
            s2 = (Math.sqrt(1-Math.pow(c2, 2)));
            double q2 =  Math.atan2(s2, c2);
            double q1 =  (Math.atan2(pose_inv.getY(), pose_inv.getX())-Math.atan2(Robot.getLinkLength_2()*s2, Robot.getLinkLength_1()+Robot.getLinkLength_2()*c2));
            q1=-Math.toDegrees(q1);
            q2=-Math.toDegrees(q2);
            Robot.setConfig((int)q1, (int)q2);
            if (q1<0) q1=359+q1;
            if (q2<0) q2=359+q2;
            jSlider2.setValue((int)q1);
            jSlider3.setValue((int)q2);
            repaint();
        }
        if ((!someone_is_glued_local)&&(evt.getButton()==3)){
            someone_is_glued_local=true;
            Point pose_inv = new Point(evt.getX()-180, evt.getY()-180);
            double c2 = (Math.pow(pose_inv.getX(),2)+Math.pow(pose_inv.getY(),2)-Math.pow(Robot.getLinkLength_1(),2)-Math.pow(Robot.getLinkLength_2(), 2))/(2*Robot.getLinkLength_1()*Robot.getLinkLength_2());
            double s2;
            s2=-(Math.sqrt(1-Math.pow(c2, 2)));
            double q2 =  Math.atan2(s2, c2);
            double q1 =  (Math.atan2(pose_inv.getY(), pose_inv.getX())-Math.atan2(Robot.getLinkLength_2()*s2, Robot.getLinkLength_1()+Robot.getLinkLength_2()*c2));
            q1=-Math.toDegrees(q1);
            q2=-Math.toDegrees(q2);
            Robot.setConfig((int)q1, (int)q2);
            if (q1<0) q1=359+q1;
            if (q2<0) q2=359+q2;
            jSlider2.setValue((int)q1);
            jSlider3.setValue((int)q2);
            repaint();
        }
        


        
        
    }//GEN-LAST:event_jPanel1MouseDragged

    private void jPanel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseMoved
        Point2D pose = new Point2D.Double(evt.getX(), evt.getY());
        if (circle1.getShape().contains(pose)){cf1=true; tf2=false; tf3=false; rf1=false; rf2=false; rf3=false; rf4=false; tf1=false; cf2=false; cf3=false;}
        if (circle2.getShape().contains(pose)){cf2=true; tf2=false; tf3=false; rf1=false; rf2=false; rf3=false; rf4=false; tf1=false; cf1=false; cf3=false;}
        if (circle3.getShape().contains(pose)){cf3=true; tf2=false; tf3=false; rf1=false; rf2=false; rf3=false; rf4=false; tf1=false; cf2=false; cf1=false;}

        if (triangle.c1.contains(pose)){tf1=true; tf2=false; tf3=false; rf1=false; rf2=false; rf3=false; rf4=false; cf1=false; cf2=false; cf3=false;}
        if (triangle.c2.contains(pose)){tf2=true; tf1=false; tf3=false; rf1=false; rf2=false; rf3=false; rf4=false; cf1=false; cf2=false; cf3=false;}
        if (triangle.c3.contains(pose)){tf3=true; tf2=false; tf1=false; rf1=false; rf2=false; rf3=false; rf4=false; cf1=false; cf2=false; cf3=false;}

        if (rectangle.c1.contains(pose)){rf1=true; tf2=false; tf3=false; tf1=false; rf2=false; rf3=false; rf4=false; cf1=false; cf2=false; cf3=false;}
        if (rectangle.c2.contains(pose)){rf2=true; tf2=false; tf3=false; rf1=false; tf1=false; rf3=false; rf4=false; cf1=false; cf2=false; cf3=false;}
        if (rectangle.c3.contains(pose)){rf3=true; tf2=false; tf3=false; rf1=false; rf2=false; tf1=false; rf4=false; cf1=false; cf2=false; cf3=false;}
        if (rectangle.c4.contains(pose)){rf4=true; tf2=false; tf3=false; rf1=false; rf2=false; rf3=false; tf1=false; cf1=false; cf2=false; cf3=false;}
        repaint();
    }//GEN-LAST:event_jPanel1MouseMoved

    private void jSlider2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider2StateChanged
        Robot.setConfig(jSlider2.getValue(), jSlider3.getValue());
        setRedDotRobotPose(jSlider2.getValue(), jSlider3.getValue());
        if(jCheckBoxShowOnTorus.isSelected())showRobotAsRedDot(jSlider2.getValue(), jSlider3.getValue());
        repaint();
    }//GEN-LAST:event_jSlider2StateChanged

    private void jSlider3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider3StateChanged
        Robot.setConfig(jSlider2.getValue(), jSlider3.getValue());
        setRedDotRobotPose(jSlider2.getValue(), jSlider3.getValue());
        if(jCheckBoxShowOnTorus.isSelected())showRobotAsRedDot(jSlider2.getValue(), jSlider3.getValue());
        repaint();
    }//GEN-LAST:event_jSlider3StateChanged

    private void jSlider5StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider5StateChanged
        Robot.setTotal_lenght(jSlider5.getValue());
        repaint();
        if (c_space_auto_update.isSelected()){
            calculateC_Space();
            updateTorus();
        }
        jSlider4.setMaximum(jSlider5.getValue()-2);
        jSlider4.setValue(jSlider4.getMaximum()/2);
    }//GEN-LAST:event_jSlider5StateChanged

    private void jSlider6StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider6StateChanged
        lineType=jSlider6.getValue()-1;
        repaint();
    }//GEN-LAST:event_jSlider6StateChanged

    private void jSlider4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider4StateChanged
        Robot.setJoint_Position(jSlider4.getValue());
        repaint();
        if (c_space_auto_update.isSelected()){
            calculateC_Space();
            updateTorus();
        }
    }//GEN-LAST:event_jSlider4StateChanged

    private void jCheckBoxTriStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxTriStateChanged
        if (c_space_auto_update.isSelected()){
            repaint();
            calculateC_Space();
            updateTorus();
        }
    }//GEN-LAST:event_jCheckBoxTriStateChanged

    private void jCheckBoxQuadStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxQuadStateChanged
        if (c_space_auto_update.isSelected()){
            repaint();
            calculateC_Space();
            updateTorus();
        }
    }//GEN-LAST:event_jCheckBoxQuadStateChanged

    private void jCheckBoxCirc1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxCirc1StateChanged
        if (c_space_auto_update.isSelected()){
            repaint();
            calculateC_Space();
            updateTorus();
        }
    }//GEN-LAST:event_jCheckBoxCirc1StateChanged

    private void jLabel_c_spaceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_c_spaceMouseClicked
        Point pose = new Point(evt.getX(), evt.getY());
        jSlider3.setValue(pose.y);
        jSlider2.setValue(pose.x);
        Robot.setConfig(pose.x, pose.y);
//        System.out.println(evt.getX()+" , "+evt.getY());
        repaint();
    }//GEN-LAST:event_jLabel_c_spaceMouseClicked

    private void jCheckBoxCirc2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxCirc2StateChanged
        if (c_space_auto_update.isSelected()){
            repaint();
            calculateC_Space();
            updateTorus();
        }
    }//GEN-LAST:event_jCheckBoxCirc2StateChanged

    private void jCheckBoxCirc3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBoxCirc3StateChanged
        if (c_space_auto_update.isSelected()){
            repaint();
            calculateC_Space();
            updateTorus();
        }
    }//GEN-LAST:event_jCheckBoxCirc3StateChanged

    private void c_space_auto_updateStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_c_space_auto_updateStateChanged
        if (c_space_auto_update.isSelected()){
            calculateC_Space();
            updateTorus();
        }else {
            ImageIcon icon = new ImageIcon();
            icon.setImage(c_space_clear);
            jLabel_c_space.setIcon(icon);
            updateTorus();
        }
    }//GEN-LAST:event_c_space_auto_updateStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jTextField1.setText(String.valueOf(jSlider2.getValue()));
        jTextField2.setText(String.valueOf(jSlider3.getValue()));
        init = new Test.Configuration(Integer.valueOf(jTextField1.getText()), Integer.valueOf(jTextField2.getText()));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTextField3.setText(String.valueOf(jSlider2.getValue()));
        jTextField4.setText(String.valueOf(jSlider3.getValue()));
        goal = new Test.Configuration(Integer.valueOf(jTextField3.getText()), Integer.valueOf(jTextField4.getText()));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        ArrayList a = new ArrayList();
        if (jCheckBoxCirc1.isSelected()) a.add(circle1);
        if (jCheckBoxCirc2.isSelected()) a.add(circle2);
        if (jCheckBoxCirc3.isSelected()) a.add(circle3);
        if (jCheckBoxQuad.isSelected()) a.add(rectangle);
        if (jCheckBoxTri.isSelected()) a.add(triangle);

        double bias_ = Double.valueOf(jTextField5.getText());
        double threshold_ = Double.valueOf(jTextField6.getText());
        double delta_ = Double.valueOf(jTextField7.getText());
        int iterations_ = Integer.valueOf(jTextField8.getText());

        long t1_ = System.currentTimeMillis();
        rrt = new RRT(init, goal, Robot, a, bias_, threshold_, delta_, iterations_);
        long t2_ = System.currentTimeMillis();

        if (rrt.findGoal) {
            jCheckBox1.setSelected(true);
            plotTree(rrt.nodeSET);
            showQiQf(init, goal);

            rrtPathAsArrayList = new ArrayList<Test.Configuration>();
            ArrayList<Test.Configuration> path_conf_nodes = rrt.getPath();

            for (int i = 0; i < path_conf_nodes.size()-1; i++) {
                Test.Configuration parentNode = path_conf_nodes.get(i);
                Test.Configuration thisNode = path_conf_nodes.get(i+1);
                Test.Configuration[] localPath = PlanningTools.getDirectPath(parentNode,thisNode);
                rrtPathAsArrayList.addAll(Arrays.asList(localPath));
            }

            jSlider1.setMaximum(rrtPathAsArrayList.size());
            jSlider1.setMinimum(1);
            jSlider1.setValue(1);

            repaint();
            updateTorus();

            String msg = "Number of nodes in the RRT tree: "+rrt.new_tree.getAllNodes().size()+".\n";
            msg = msg+"Number of nodes in the path tree: "+path_conf_nodes.size()+".\n";
            msg = msg+"Time elapsed to find the path: "+(t2_-t1_)+" milliseconds.";
            JOptionPane.showMessageDialog(null,msg ,"Path found :D", JOptionPane.INFORMATION_MESSAGE);
        }
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        if(rrt.findGoal){
            int TH1 = (rrtPathAsArrayList.get(Math.abs(rrtPathAsArrayList.size()-jSlider1.getValue())).theta1);
            int TH2 = (rrtPathAsArrayList.get(Math.abs(rrtPathAsArrayList.size()-jSlider1.getValue())).theta2);
//            Robot.setConfig(rrtPathAsArrayList.get(jSlider1.getValue()).theta1, rrtPathAsArrayList.get(jSlider1.getValue()).theta2);
//            setRedDotRobotPose(rrtPathAsArrayList.get(jSlider1.getValue()).theta1, rrtPathAsArrayList.get(jSlider1.getValue()).theta2);
            Robot.setConfig(TH1,TH2);
            setRedDotRobotPose(TH1,TH2);
            showRobotAsRedDot(TH1, TH2);
            repaint();
//            System.out.println(Math.abs(rrtPathAsArrayList.size()-jSlider1.getValue()));
        }
    }//GEN-LAST:event_jSlider1StateChanged

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
        oldMousePose = evt.getPoint();
//        someone_is_glued = false;
    }//GEN-LAST:event_jPanel1MousePressed

    private void jPanel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseReleased
//        someone_is_glued = false;
    }//GEN-LAST:event_jPanel1MouseReleased

    private void jCheckBox2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCheckBox2StateChanged
        if (jCheckBox2.isSelected()) resolution =1; else resolution =2;
        calculateC_Space();
        repaint();
        updateTorus();
    }//GEN-LAST:event_jCheckBox2StateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        calculateC_Space();
        repaint();
        updateTorus();
        jTextField1.setText("0");
        jTextField2.setText("0");
        jTextField3.setText("0");
        jTextField4.setText("0");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jPanel1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel1KeyPressed
//        if (evt.){
//            shiftPressed = true;
//            System.out.println("SHIFt!");
//        }
    }//GEN-LAST:event_jPanel1KeyPressed

    private void jPanel1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPanel1KeyReleased
//        shiftPressed = false;
//        someone_is_glued_local=false;
    }//GEN-LAST:event_jPanel1KeyReleased

    private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseClicked

        if ((evt.getButton()==1)){
            Point pose_inv = new Point(evt.getX()-180, evt.getY()-180);
            double c2 = (Math.pow(pose_inv.getX(),2)+Math.pow(pose_inv.getY(),2)-Math.pow(Robot.getLinkLength_1(),2)-Math.pow(Robot.getLinkLength_2(), 2))/(2*Robot.getLinkLength_1()*Robot.getLinkLength_2());
            double s2;
            s2 = (Math.sqrt(1-Math.pow(c2, 2)));
            double q2 =  Math.atan2(s2, c2);
            double q1 =  (Math.atan2(pose_inv.getY(), pose_inv.getX())-Math.atan2(Robot.getLinkLength_2()*s2, Robot.getLinkLength_1()+Robot.getLinkLength_2()*c2));
            q1=-Math.toDegrees(q1);
            q2=-Math.toDegrees(q2);
            Robot.setConfig((int)q1, (int)q2);
            if (q1<0) q1=359+q1;
            if (q2<0) q2=359+q2;
            jSlider2.setValue((int)q1);
            jSlider3.setValue((int)q2);
            repaint();
        }
        if ((evt.getButton()==3)){

            Point pose_inv = new Point(evt.getX()-180, evt.getY()-180);
            double c2 = (Math.pow(pose_inv.getX(),2)+Math.pow(pose_inv.getY(),2)-Math.pow(Robot.getLinkLength_1(),2)-Math.pow(Robot.getLinkLength_2(), 2))/(2*Robot.getLinkLength_1()*Robot.getLinkLength_2());
            double s2;
            s2=-(Math.sqrt(1-Math.pow(c2, 2)));
            double q2 =  Math.atan2(s2, c2);
            double q1 =  (Math.atan2(pose_inv.getY(), pose_inv.getX())-Math.atan2(Robot.getLinkLength_2()*s2, Robot.getLinkLength_1()+Robot.getLinkLength_2()*c2));
            q1=-Math.toDegrees(q1);
            q2=-Math.toDegrees(q2);
            Robot.setConfig((int)q1, (int)q2);
            if (q1<0) q1=359+q1;
            if (q2<0) q2=359+q2;
            jSlider2.setValue((int)q1);
            jSlider3.setValue((int)q2);
            repaint();
        }
    }//GEN-LAST:event_jPanel1MouseClicked

    //ORIGINAL CALCULATE C_SPACE
//    private void calculateC_Space(){
//
//        c_space=new BufferedImage(360, 360, BufferedImage.TYPE_INT_RGB);
//          if (resolution==1){
//            for(int i=1;i<=360;i++){
//                for(int j=1;j<=360;j++){
//                    c_space.setRGB(i-1, j-1, 0xffffff);
//                    c_space_torus.setRGB(i-1, j-1, gradient.getRGB(i-1, j-1));
//                    Robot.setConfig(i, j);
//
//                        if(jCheckBoxTri.isSelected()){
//                            if (triangle.inCollision(Robot.Link2)){c_space.setRGB(i-1, j-1, 0x00ffff);c_space_torus.setRGB(i-1, j-1, 0x00ffff);}
//                            if (triangle.inCollision(Robot.Link1)){c_space.setRGB(i-1, j-1, 0x00ffff);c_space_torus.setRGB(i-1, j-1, 0x00ffff);}
//                        }
//
//                        if(jCheckBoxQuad.isSelected()){
//                            if (rectangle.inCollision(Robot.Link2)){c_space.setRGB(i-1, j-1, 0xff00ff&c_space.getRGB(i-1, j-1));c_space_torus.setRGB(i-1, j-1, 0xff00ff&c_space.getRGB(i-1, j-1));}
//                            if (rectangle.inCollision(Robot.Link1)){c_space.setRGB(i-1, j-1, 0xff00ff&c_space.getRGB(i-1, j-1));c_space_torus.setRGB(i-1, j-1, 0xff00ff&c_space.getRGB(i-1, j-1));}
//                        }
//
//                        if(jCheckBoxCirc1.isSelected()){
//                            if (circle1.inCollision(Robot.Link2)){c_space.setRGB(i-1, j-1, 0xffff00&c_space.getRGB(i-1, j-1));c_space_torus.setRGB(i-1, j-1, 0xffff00&c_space.getRGB(i-1, j-1));}
//                            if (circle1.inCollision(Robot.Link1)){c_space.setRGB(i-1, j-1, 0xffff00&c_space.getRGB(i-1, j-1));c_space_torus.setRGB(i-1, j-1, 0xffff00&c_space.getRGB(i-1, j-1));}
//                        }
//
//                        if(jCheckBoxCirc2.isSelected()){
//                            if (circle2.inCollision(Robot.Link2)){c_space.setRGB(i-1, j-1, 0x797979);c_space_torus.setRGB(i-1, j-1, 0x797979);}
//                            if (circle2.inCollision(Robot.Link1)){c_space.setRGB(i-1, j-1, 0x797979);c_space_torus.setRGB(i-1, j-1, 0x797979);}
//                        }
//
//                        if(jCheckBoxCirc3.isSelected()){
//                            if (circle3.inCollision(Robot.Link2)){c_space.setRGB(i-1, j-1, 0x797979);c_space_torus.setRGB(i-1, j-1, 0x797979);}
//                            if (circle3.inCollision(Robot.Link1)){c_space.setRGB(i-1, j-1, 0x797979);c_space_torus.setRGB(i-1, j-1, 0x797979);}
//                        }
//                }
//            }
//        }
//
//        ImageIcon icon = new ImageIcon();
//        icon.setImage(c_space);
//        jLabel_c_space.setIcon(icon);
//    }
    private void calculateC_Space(){

//        c_space=new BufferedImage(360, 360, BufferedImage.TYPE_INT_RGB);

            for(int i=1;i<=360;i=i+resolution){
                for(int j=1;j<=360;j=j+resolution){
                    c_space.setRGB(i-1, j-1, 0xffffff);
                    c_space_torus.setRGB(i-1, j-1, gradient.getRGB(i-1, j-1));
                    Robot.setConfig(i, j);

                    // The following is the original calculation of cspace. The next block is for jpl version. switch comments to have normal behaviour
                        
                        if(jCheckBoxTri.isSelected()){
                            if (triangle.inCollision(Robot.Link2)){c_space.setRGB(i-1, j-1, 0x00ffff);c_space_torus.setRGB(i-1, j-1, 0x00ffff);}
                            if (triangle.inCollision(Robot.Link1)){c_space.setRGB(i-1, j-1, 0x00ffff);c_space_torus.setRGB(i-1, j-1, 0x00ffff);}
                        }

                        if(jCheckBoxQuad.isSelected()){
                            if (rectangle.inCollision(Robot.Link2)){c_space.setRGB(i-1, j-1, 0xff00ff&c_space.getRGB(i-1, j-1));c_space_torus.setRGB(i-1, j-1, 0xff00ff&c_space.getRGB(i-1, j-1));}
                            if (rectangle.inCollision(Robot.Link1)){c_space.setRGB(i-1, j-1, 0xff00ff&c_space.getRGB(i-1, j-1));c_space_torus.setRGB(i-1, j-1, 0xff00ff&c_space.getRGB(i-1, j-1));}
                        }

                        if(jCheckBoxCirc1.isSelected()){
                            if (circle1.inCollision(Robot.Link2)){c_space.setRGB(i-1, j-1, 0xffff00&c_space.getRGB(i-1, j-1));c_space_torus.setRGB(i-1, j-1, 0xffff00&c_space.getRGB(i-1, j-1));}
                            if (circle1.inCollision(Robot.Link1)){c_space.setRGB(i-1, j-1, 0xffff00&c_space.getRGB(i-1, j-1));c_space_torus.setRGB(i-1, j-1, 0xffff00&c_space.getRGB(i-1, j-1));}
                        }

                        if(jCheckBoxCirc2.isSelected()){
                            if (circle2.inCollision(Robot.Link2)){c_space.setRGB(i-1, j-1, 0x797979);c_space_torus.setRGB(i-1, j-1, 0x797979);}
                            if (circle2.inCollision(Robot.Link1)){c_space.setRGB(i-1, j-1, 0x797979);c_space_torus.setRGB(i-1, j-1, 0x797979);}
                        }

                        if(jCheckBoxCirc3.isSelected()){
                            if (circle3.inCollision(Robot.Link2)){c_space.setRGB(i-1, j-1, 0x797979);c_space_torus.setRGB(i-1, j-1, 0x797979);}
                            if (circle3.inCollision(Robot.Link1)){c_space.setRGB(i-1, j-1, 0x797979);c_space_torus.setRGB(i-1, j-1, 0x797979);}
                        }
                        /*
                        if(jCheckBoxCirc1.isSelected()){
                            if (circle1.inCollision(Robot.Link2)){c_space.setRGB(i-1, j-1, 0x00ffff);c_space_torus.setRGB(i-1, j-1, 0x00ffff);}
                            if (circle1.inCollision(Robot.Link1)){c_space.setRGB(i-1, j-1, 0x00ffff);c_space_torus.setRGB(i-1, j-1, 0x00ffff);}
                        }

                        if(jCheckBoxCirc2.isSelected()){
                            if (circle2.inCollision(Robot.Link2)){c_space.setRGB(i-1, j-1, 0xff00ff&c_space.getRGB(i-1, j-1));c_space_torus.setRGB(i-1, j-1, 0xff00ff&c_space.getRGB(i-1, j-1));}
                            if (circle2.inCollision(Robot.Link1)){c_space.setRGB(i-1, j-1, 0xff00ff&c_space.getRGB(i-1, j-1));c_space_torus.setRGB(i-1, j-1, 0xff00ff&c_space.getRGB(i-1, j-1));}
                        }

                        if(jCheckBoxCirc3.isSelected()){
                            if (circle3.inCollision(Robot.Link2)){c_space.setRGB(i-1, j-1, 0xffff00&c_space.getRGB(i-1, j-1));c_space_torus.setRGB(i-1, j-1, 0xffff00&c_space.getRGB(i-1, j-1));}
                            if (circle3.inCollision(Robot.Link1)){c_space.setRGB(i-1, j-1, 0xffff00&c_space.getRGB(i-1, j-1));c_space_torus.setRGB(i-1, j-1, 0xffff00&c_space.getRGB(i-1, j-1));}
                        }

                        if(jCheckBoxTri.isSelected()){
                            if (triangle.inCollision(Robot.Link2)){c_space.setRGB(i-1, j-1, 0x797979);c_space_torus.setRGB(i-1, j-1, 0x797979);}
                            if (triangle.inCollision(Robot.Link1)){c_space.setRGB(i-1, j-1, 0x797979);c_space_torus.setRGB(i-1, j-1, 0x797979);}
                        }

                        if(jCheckBoxQuad.isSelected()){
                            if (rectangle.inCollision(Robot.Link2)){c_space.setRGB(i-1, j-1, 0x797979);c_space_torus.setRGB(i-1, j-1, 0x797979);}
                            if (rectangle.inCollision(Robot.Link1)){c_space.setRGB(i-1, j-1, 0x797979);c_space_torus.setRGB(i-1, j-1, 0x797979);}
                        }
                    */
                    if(resolution!=1){
                        if ((i<360)&&(j<360)) c_space.setRGB(i, j, c_space.getRGB(i-1, j-1));
                        if (i<360) c_space.setRGB(i, j-1, c_space.getRGB(i-1, j-1));
                        if (j<360) c_space.setRGB(i-1, j, c_space.getRGB(i-1, j-1));

                        if ((i<360)&&(j<360)) c_space_torus.setRGB(i, j, c_space_torus.getRGB(i-1, j-1));
                        if (i<360) c_space_torus.setRGB(i, j-1, c_space_torus.getRGB(i-1, j-1));
                        if (j<360) c_space_torus.setRGB(i-1, j, c_space_torus.getRGB(i-1, j-1));
                    }
                }
            }

        ImageIcon icon = new ImageIcon();
        icon.setImage(c_space);
        jLabel_c_space.setIcon(icon);
    }

    private void makeTorus(){
        try {

            torus_flag=1;
            canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
            t1 = getSceneFromFile();
            app = new Appearance();
            app.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
            texLoader = new TextureLoader(c_space,new Container());
            tex = texLoader.getTexture();
            app.setTexture(tex);
            root = t1.getSceneGroup();
            root.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
            torus = (Shape3D) root.getChild(0);
            torus.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
            torus.setAppearance(app);
            universe = new SimpleUniverse(canvas);
            universe.getViewingPlatform().setNominalViewingTransform();
            universe.addBranchGraph(root);
            universe.getViewingPlatform().setNominalViewingTransform();
            ViewingPlatform viewingPlatform = universe.getViewingPlatform();
            OrbitBehavior orbit = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ALL | OrbitBehavior.STOP_ZOOM);
            BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
            orbit.setSchedulingBounds(bounds);
            viewingPlatform.setNominalViewingTransform();
            viewingPlatform.setViewPlatformBehavior(orbit);
            canvas.setSize(jPanel2.getSize().width, jPanel2.getSize().height);
            jPanel2.add(canvas);
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Scene getSceneFromFile() throws IOException {
        ObjectFile file = new ObjectFile(ObjectFile.RESIZE);
        return file.load((GUI.class.getClassLoader().getResource("torus/torus2.obj")));

    }

    public void updateTorus(){
        if (torus_flag==1){
            if (c_space_auto_update.isSelected()) tex = new TextureLoader(c_space_torus,new Container()).getTexture();
            if (!c_space_auto_update.isSelected()) tex = new TextureLoader(gradient,new Container()).getTexture();
            app.setTexture(tex);
            root = t1.getSceneGroup();
            torus = (Shape3D) root.getChild(0);
            torus.setAppearance(app);
        }
    }

    public void plotPath(ArrayList<Test.Configuration> _path){
        for (int i = 0; i < _path.size()-1; i++) {
            plotLocalPath(_path.get(i), _path.get(i+1));
        }
    }

    public void plotTree(NodeSet<Configuration> n_s){
        Iterator<RiccardoTree<Configuration>> iterator = n_s.getSet().iterator();
        while (iterator.hasNext()) {
            RiccardoTree<Configuration> next = iterator.next();
            if(next.isRoot()) continue;
            Test.Configuration parentNode = next.getParent().getObjectNode();
            Test.Configuration thisNode = next.getObjectNode();
            plotLocalPath(thisNode, parentNode);
        }
    }

    public void plotLocalPath(Test.Configuration _init, Test.Configuration _goal){
        Test.Configuration[] localPath = PlanningTools.getDirectPath(_init, _goal);
        for (int j = 0; j < localPath.length; j++) {
            plotConfiguration(localPath[j]);
        }
    }

    public void plotConfiguration(Test.Configuration c){

        Point pose = new Point(c.theta1, c.theta2);
            if (jCheckBox1.isSelected() && pose.x < 359 && pose.x > 1 && pose.y < 359 && pose.y > 1) {
                c_space.setRGB(pose.x, pose.y, 0);
//                c_space.setRGB(pose.x - 1, pose.y, 0);
//                c_space.setRGB(pose.x + 1, pose.y, 0);
//                c_space.setRGB(pose.x, pose.y - 1, 0);
//                c_space.setRGB(pose.x, pose.y + 1, 0);

//                c_space.setRGB(pose.x - 1, pose.y - 1, 0);
//                c_space.setRGB(pose.x + 1, pose.y + 1, 0);
//                c_space.setRGB(pose.x + 1, pose.y - 1, 0);
//                c_space.setRGB(pose.x - 1, pose.y + 1, 0);

                c_space_torus.setRGB(pose.x, pose.y, 0);
                c_space_torus.setRGB(pose.x - 1, pose.y, 0);
                c_space_torus.setRGB(pose.x + 1, pose.y, 0);
                c_space_torus.setRGB(pose.x, pose.y - 1, 0);
                c_space_torus.setRGB(pose.x, pose.y + 1, 0);

//                c_space_torus.setRGB(pose.x - 1, pose.y - 1, 0);
//                c_space_torus.setRGB(pose.x + 1, pose.y + 1, 0);
//                c_space_torus.setRGB(pose.x + 1, pose.y - 1, 0);
//                c_space_torus.setRGB(pose.x - 1, pose.y + 1, 0);

            }
//        repaint();
//        updateTorus();
    }

    public void setRedDotRobotPose(int x, int y){
        int newX = x+jLabel_c_space.getX();
        int newY = y+jLabel_c_space.getY();
        jLabel_Red_Dot_Robot.setLocation(newX-2, newY-2);
        
    }

    public void showRobotAsRedDot(int x, int y){

//        System.out.println(x+" , "+y);
        if(x < 359 && x > 1 && y < 359 && y > 1){
            //BRINGING THE OLD POSE TO IT'S OLD COLOR

            c_space_torus.setRGB(x_red_robot_old, y_red_robot_old, old_colors_torus_cSpace[0]);
            c_space_torus.setRGB(x_red_robot_old - 1, y_red_robot_old, old_colors_torus_cSpace[1]);
            c_space_torus.setRGB(x_red_robot_old + 1, y_red_robot_old, old_colors_torus_cSpace[2]);
            c_space_torus.setRGB(x_red_robot_old, y_red_robot_old - 1, old_colors_torus_cSpace[3]);
            c_space_torus.setRGB(x_red_robot_old, y_red_robot_old + 1, old_colors_torus_cSpace[4]);

            c_space_torus.setRGB(x_red_robot_old - 1, y_red_robot_old - 1, old_colors_torus_cSpace[5]);
            c_space_torus.setRGB(x_red_robot_old + 1, y_red_robot_old + 1, old_colors_torus_cSpace[6]);
            c_space_torus.setRGB(x_red_robot_old + 1, y_red_robot_old - 1, old_colors_torus_cSpace[7]);
            c_space_torus.setRGB(x_red_robot_old - 1, y_red_robot_old + 1, old_colors_torus_cSpace[8]);


            x_red_robot_old = x;
            y_red_robot_old = y;


            //SETTING THE NEW COLORS
            old_colors_torus_cSpace[0]=c_space_torus.getRGB(x, y);
            c_space_torus.setRGB(x, y, 0xFF0000);

            old_colors_torus_cSpace[1]=c_space_torus.getRGB(x-1, y);
            c_space_torus.setRGB(x - 1, y, 0xFF0000);

            old_colors_torus_cSpace[2]=c_space_torus.getRGB(x+1, y);
            c_space_torus.setRGB(x + 1, y, 0xFF0000);

            old_colors_torus_cSpace[3]=c_space_torus.getRGB(x, y-1);
            c_space_torus.setRGB(x, y - 1, 0xFF0000);

            old_colors_torus_cSpace[4]=c_space_torus.getRGB(x, y+1);
            c_space_torus.setRGB(x, y + 1, 0xFF0000);

            old_colors_torus_cSpace[5]=c_space_torus.getRGB(x-1, y-1);
            c_space_torus.setRGB(x - 1, y - 1, 0xFF0000);

            old_colors_torus_cSpace[6]=c_space_torus.getRGB(x+1, y+1);
            c_space_torus.setRGB(x + 1, y + 1, 0xFF0000);

            old_colors_torus_cSpace[7]=c_space_torus.getRGB(x+1, y-1);
            c_space_torus.setRGB(x + 1, y - 1, 0xFF0000);

            old_colors_torus_cSpace[8]=c_space_torus.getRGB(x-1, y+1);
            c_space_torus.setRGB(x - 1, y + 1, 0xFF0000);

            updateTorus();

        }
    }
    

    public void showQiQf(Test.Configuration QI, Test.Configuration QG){
        Point pose = new Point(QI.theta1, QI.theta2);
                c_space.setRGB(pose.x, pose.y, 0x0000FF);
                c_space.setRGB(pose.x - 1, pose.y, 0x0000FF);
                c_space.setRGB(pose.x + 1, pose.y, 0x0000FF);
                c_space.setRGB(pose.x, pose.y - 1, 0x0000FF);
                c_space.setRGB(pose.x, pose.y + 1, 0x0000FF);

                c_space.setRGB(pose.x - 1, pose.y - 1, 0x0000FF);
                c_space.setRGB(pose.x + 1, pose.y + 1, 0x0000FF);
                c_space.setRGB(pose.x + 1, pose.y - 1, 0x0000FF);
                c_space.setRGB(pose.x - 1, pose.y + 1, 0x0000FF);

        pose.setLocation(QG.theta1, QG.theta2);
                c_space.setRGB(pose.x, pose.y, 0x00FF00);
                c_space.setRGB(pose.x - 1, pose.y, 0x00FF00);
                c_space.setRGB(pose.x + 1, pose.y, 0x00FF00);
                c_space.setRGB(pose.x, pose.y - 1, 0x00FF00);
                c_space.setRGB(pose.x, pose.y + 1, 0x00FF00);

                c_space.setRGB(pose.x - 1, pose.y - 1, 0x00FF00);
                c_space.setRGB(pose.x + 1, pose.y + 1, 0x00FF00);
                c_space.setRGB(pose.x + 1, pose.y - 1, 0x00FF00);
                c_space.setRGB(pose.x - 1, pose.y + 1, 0x00FF00);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox c_space_auto_update;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBoxCirc1;
    private javax.swing.JCheckBox jCheckBoxCirc2;
    private javax.swing.JCheckBox jCheckBoxCirc3;
    private javax.swing.JCheckBox jCheckBoxQuad;
    private javax.swing.JCheckBox jCheckBoxShowOnTorus;
    private javax.swing.JCheckBox jCheckBoxTri;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_Red_Dot_Robot;
    private javax.swing.JLabel jLabel_c_space;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    private javax.swing.JSlider jSlider3;
    private javax.swing.JSlider jSlider4;
    private javax.swing.JSlider jSlider5;
    private javax.swing.JSlider jSlider6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables

}
