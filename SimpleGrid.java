// Written by: Paulameena Shultes
//**ADAPTED FROM HAL DOCUMENTATION AND GITHUB.COM/ESHANKING/HAL_DOSE_RESPONSE.GIT**
// Date: 08/10/2023
import HAL.*;
import HAL.Interfaces.SerializableModel;

import java.util.*;
import java.io.*;
import HAL.GridsAndAgents.AgentGrid2D;
import HAL.Tools.FileIO;
import HAL.Gui.UIGrid;

/*SIMPLE GRID


HAL is used to define the grid and the cells, and to set up the simulation parameters.
The grid is initialized with a set of cells, and the cells are initialized with their characteristics.
The grid is then passed to the simulation class, where the simulation is run.

Note: the way the HAL library is defined; agents are not constructed by you but by the grid. No agent constructors are needed.
 
*/


public class SimpleGrid extends AgentGrid2D<Cell> implements SerializableModel{
    /*------------------------------------------
                GRID CHARACTERISTICS
     ------------------------------------------*/

    int xDim;
    int yDim;


   /*------------------------------------------
                 CONSTRUCTORS
     ------------------------------------------*/


    public SimpleGrid(int x, int y) {
        super(x, y, Cell.class);
        this.xDim = x;
        this.yDim = y;
    }

    public SimpleGrid(){
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
    int[] divHood = Util.VonNeumannHood(false); // 4 neighbors
    //int[] divHood = Util.MooreHood(false); // 8 neighbors-- doesn't make sense for fusion 
    public double initRadius = 10;
    public double initMutProp=0.1;
    String initGeom = "circle"; // "circle" or "square" inital population; tumor assumptions favor circle
    int initWidth = 100;
    double initDEnsity = 0.01;

    /*------------------------------------------
                CELL PARAMETERS
     ------------------------------------------*/

    double dieProb = 0.1; // death probability
    double resistanceRate = 0.0001; // mutation rate to resistant from parental/sensitive
    double timeStep = 1; // time step for simulation
    
    //TODO: might want to change this to be a function that samples from a distribution with different centers, to create more variability between the cell types/introduce more heterogeneity
    double[] divProbs = new double[]{0.1,0.2}; // probability of division for each cell type, first = parent, second = resistant

   /*------------------------------------------
                SIMULATION PARAMETERS
     ------------------------------------------*/

    int maxTime = 10000; // max time for simulation
    public int nReplicates = 1; // number of replicates

/*------------------------------------------
                IO PARAMETERS
     ------------------------------------------*/

     FileIO cellCountLogFile = null;
     String cellCountLo







    
}




