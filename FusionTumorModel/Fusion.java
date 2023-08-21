package Fusion;

// Written by: Paulameena Shultes
//**ADAPTED FROM HAL DOCUMENTATION AND GITHUB.COM/ESHANKING/HAL_DOSE_RESPONSE.GIT**
// Date: 08/10/2023
import HAL.Interfaces.SerializableModel;
import HAL.Util;
//import java.util.*;
//import java.io.*;
import HAL.GridsAndAgents.AgentGrid2D;
import HAL.Tools.FileIO;

//import java.util.Dictionary;
import java.util.Hashtable;

//import HAL.Gui.GridWindow;
//import HAL.Gui.UIGrid;
import HAL.Rand;
//import HAL.GridsAndAgents.PDEGrid2D;
//import HAL.GridsAndAgents.AgentSQ2Dunstackable;



/*Fusion


HAL is used to define the grid and the cells, and to set up the simulation parameters.
The grid is initialized with a set of cells, and the cells are initialized with their characteristics.


Note: the way the HAL library is defined; agents are not constructed by you but by the grid. No agent constructors are needed.
 
*/


public class Fusion extends AgentGrid2D<Cell> implements SerializableModel{
   /*------------------------------------------
                 CONSTRUCTORS
     ------------------------------------------*/

    public Fusion(int x, int y) {
        super(x, y, Cell.class);
        this.xDim = x;
        this.yDim = y;
    }

    public Fusion(){
        super(100,100,Cell.class);
        this.xDim = 100;
        this.yDim = 100;
    }

    @Override
    public void SetupConstructors() {
        _PassAgentConstructor(Cell.class);
    }
    
    /*------------------------------------------
                MODEL PARAMETERS
     ------------------------------------------*/

    int xDim= 100;
    int yDim = 100;
    int[] divHood = HAL.Util.MooreHood(false); // 8 neighbors for division
    int[] fusHood = HAL.Util.MooreHood(false); //8 neighbors for fusion
    Rand rng=new Rand(); 
    //double replicationRate = 0.2; // birth probability
    //double deathRate = 0.01; // death probability
    //double fusionRate = 0.1; //fusion probability
    double initResProb = 0.1; // mutation probability for initial seeding

    //int[] divHood = Util.MooreHood(false); // 8 neighbors-- doesn't make sense for fusion 
    public double initRadius = 10;
    public double initMutProp=0.1;
    String initGeom = "circle"; // "circle" or "square" inital population; tumor assumptions favor circle
    int initWidth = 100;
    double initDEnsity = 0.01;
    String modelType = "drf";

    Hashtable<String, Integer> cellTypeCounts = new Hashtable<>();

    /*------------------------------------------
                CELL PARAMETERS
     ------------------------------------------*/

    //TODO: might want to change these to be functions/distributions, to create more variability between the cell types/introduce more heterogeneity
    //index = 0 is parental, index = 1 is resistant, index = 2 will be fused
    double[] dieProbs = new double[]{0.1,0.1}; // death probability
    double[] resistanceRates = new double[]{0.0001,0}; // mutation rate to resistant from parental/sensitive; if already sensitive, does nothing
    double[] divProbs = new double[]{0.1,0.2}; // probability of division for each cell type
    double[] fusionRate = new double[]{0.0001, 0.0001};

   /*------------------------------------------
                SIMULATION PARAMETERS
     ------------------------------------------*/

    int maxTime = 10000; // max time for simulation
    double timeStep = 1; // time step for simulation
    public int nReplicates = 1; // number of replicates

/*------------------------------------------
                IO PARAMETERS
     ------------------------------------------*/

     FileIO cellCountLogFile = null;
     String cellCountLogFileName = "./data/cellCountLog.csv";



    /*------------------------------------------
                MODEL FUNCTIONS
     ------------------------------------------*/
     //NOTE: ADAPTED FROM HAL DOCUMENTATION AND BIRTHDEATH.JAVA FROM HAL DOCS

    public void UpdateCellCounts(Cell c) {
        this.cellTypeCounts.replace(c.cellType, this.cellTypeCounts.get(c.cellType) + 1);
    }

    public void UpdateCellDeath(Cell c) {
        this.cellTypeCounts.replace(c.cellType, this.cellTypeCounts.get(c.cellType) - 1);
    }



    public void InitializeTumor(double radius, String shape){
        int[] coords;
        int nCoords;
        //initialize cell counts to 0
        this.cellTypeCounts.put("f", 0);
        this.cellTypeCounts.put("r",0);
        this.cellTypeCounts.put("p", 0);
        if (shape.equals("circle")) {
            coords= Util.CircleHood(true,radius);
            nCoords= MapHood(coords,xDim/2,yDim/2);
            }
        else {
            coords= Util.RectangleHood(true,(int) radius, (int) radius);
            nCoords= MapHood(coords,xDim/2,yDim/2);
            }
        for (int i = 0; i < nCoords ; i++) {
                Cell c = NewAgentSQ(coords[i]);
                if (rng.Double() < initResProb) {
                    c.cellType = "r";
                    c.divRate = divProbs[1];
                    c.deathRate = dieProbs[1];
                    c.resistanceRate = 0; //already resistant!
                } else {
                    c.cellType = "p";
                    c.divRate = divProbs[0];
                    c.deathRate = dieProbs[0];
                    c.resistanceRate = resistanceRates[0];
                }
        UpdateCellCounts(c);
        }
    }

    public void Step() {
        for (Cell c : this) {
            c.Step(this.modelType);
        }
        CleanAgents(); // remove dead agents
        ShuffleAgents(rng); //change iteration order so not acting on cells in chronological order
    }
   
    // public void Draw(GridWindow vis){
    //     for (int i = 0; i < vis.length; i++) {
    //         Cell c = GetAgent(i);
    //         vis.SetPix(i, c == null ? BLACK : c.color);
    //     }
    // }








    
}




