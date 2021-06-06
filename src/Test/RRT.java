/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import torus.Robot2R;

/**
 *
 * @author Poo
 */
public final class RRT {
    private double biasWeight;// = 0.25;
    private double threshold;// = 50;
    private double delta;// = 20;
    private int iterationNumber;// = 50000;
    private Configuration init;
    private Configuration goal;
    private LocalPlanner lp;
    private ArrayList<Configuration> path;
    public boolean findGoal = false;
    public NodeSet<Configuration> nodeSET;
    public RiccardoTree<Configuration> new_tree;

    public RRT(){
        
    }

    public RRT(Configuration init_conf, Configuration goal_conf, Robot2R _robot, ArrayList _obstacles, double _biasWeight, double _threshold, double _delta, int _iterationNumber){

        biasWeight = _biasWeight;
        threshold = _threshold;
        delta = _delta;
        iterationNumber = _iterationNumber;

        init = new Configuration(init_conf.theta1, init_conf.theta2);
        goal = new Configuration(goal_conf.theta1, goal_conf.theta2);
//        tree = new ListenableUndirectedGraph(DefaultEdge.class);
        new_tree = new RiccardoTree<Configuration>(init);
//        tree.addVertex(init);
//        System.out.println("Tree initialized at the init as root...");
        lp = new LocalPlanner(_robot, _obstacles);
//        System.out.println("Local Planner object instanciated...");
        boolean TerminationConfition = false;


        int counter = 0;

        while (!TerminationConfition) {
            counter++;
            Configuration q_Rand, q_Near, q_New;
            double tmp = Math.random();

            if (tmp<biasWeight) {
                q_Rand = goal;
            } else {
                q_Rand = new Configuration(true);
            }

            q_Near = findNearest(q_Rand);
            q_New = findQ_New(q_Near, q_Rand, delta);
            boolean q_New_Added = false;

            if (lp.configurationIsFree(q_New)){
                Configuration[] localPath = new Configuration[(int)PlanningTools.calculateDistance(q_Near, q_New)];
                localPath = PlanningTools.getDirectPath(q_Near, q_New);
                if(lp.pathIsFree(localPath)){
//                    tree.addVertex(q_New);
//                    tree.addEdge(q_Near, q_New);
                    new_tree.addChild(q_Near, q_New);
                    q_New_Added = true;
                } else q_New_Added = false;
            }

            if ((PlanningTools.calculateDistance(q_New, goal)<=threshold)&&(q_New_Added)){
                Configuration[] localPath = new Configuration[(int)PlanningTools.calculateDistance(q_New, goal)];
                localPath = PlanningTools.getDirectPath(q_New, goal);
                if(lp.pathIsFree(localPath)){
//                    tree.addVertex(q_New);
//                    tree.addVertex(goal);
//                    tree.addEdge(q_New, goal);
                    new_tree.addChild(q_New, goal);
                    findGoal = true;
//                    System.out.println("GOAAAAAL!!!");
                    nodeSET = new NodeSet<Configuration>(new_tree);
                }
            }

            if ((counter>=iterationNumber)||(findGoal)) TerminationConfition = true;

        }
        if (!findGoal) JOptionPane.showMessageDialog(null, "Path not found :(", "Nooooooo!", JOptionPane.WARNING_MESSAGE); //System.out.println("Path not found :(");
        else //path_on_graph = new DijkstraShortestPath(tree, init, goal);
//            bfs = new BreadthFirstIterator(tree, goal);
        {
            path = new ArrayList<Configuration>();
            path = new BackTrack<Configuration>(new_tree, init, goal).getPath();
        }
//        System.out.println("Tree size = TO BE CALCULATED");
    }



    public Configuration findNearest(Configuration c){
        Configuration nearest;
        nodeSET = new NodeSet<Configuration>(new_tree);
        Iterator<RiccardoTree<Configuration>> it = nodeSET.getSet().iterator();
        nearest =  it.next().getObjectNode();

        while (it.hasNext()) {
            Configuration candidate =  it.next().getObjectNode();
            if (PlanningTools.calculateDistance(candidate,c)<=PlanningTools.calculateDistance(nearest,c)){
                nearest=candidate;
            }
        }

        return nearest;
    }

    public static Configuration findQ_New(Configuration q_Near, Configuration q_Rand, double delta){
        Configuration q_new = new Configuration();
        Configuration q_near = new Configuration(q_Near.theta1, q_Near.theta2);
        Configuration q_rand = new Configuration(q_Rand.theta1, q_Rand.theta2);

        double distance = PlanningTools.calculateDistance(q_near, q_rand);

        if (Math.abs(q_near.theta1 - q_rand.theta1) > 180) {
            if (q_rand.theta1 > q_near.theta1) {
                q_rand.theta1 = q_rand.theta1 - 359;
            } else {
                q_near.theta1 = q_near.theta1 - 359;
            }
        }
        if (Math.abs(q_near.theta2 - q_rand.theta2) > 180) {
            if (q_rand.theta2 > q_near.theta2) {
                q_rand.theta2 = q_rand.theta2 - 359;
            } else {
                q_near.theta2 = q_near.theta2 - 359;
            }
        }

        double ratio = (distance/delta)-1;

        q_new.theta1 = (int) ((((double) q_near.theta1) * (ratio) + q_rand.theta1) / (ratio + 1));
        q_new.theta2 = (int) ((((double) q_near.theta2) * (ratio) + q_rand.theta2) / (ratio + 1));

        if (q_new.theta1<0) q_new.theta1=q_new.theta1+359;
        if (q_new.theta2<0) q_new.theta2=q_new.theta2+359;

        return q_new;

    }

    public RiccardoTree<Configuration> getNew_tree() {
        return new_tree;
    }

    public ArrayList<Configuration> getPath() {
        return path;
    }

    
    


}
