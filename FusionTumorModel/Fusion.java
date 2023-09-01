package FusionTumorModel;

// Written by: Paulameena Shultes
//**ADAPTED FROM HAL DOCUMENTATION AND GITHUB.COM/ESHANKING/HAL_DOSE_RESPONSE.GIT**
// Date: 08/10/2023

import HAL.Interfaces.SerializableModel;
import HAL.Util;
//import java.util.*;
//import java.io.*;
import HAL.GridsAndAgents.AgentGrid2D;
import HAL.Tools.FileIO;

//import FusionProject.Spatial.AgentBasedModels.Models.FusionModelEtienne.FusionModel2DHeterogeneityContactInhibition.*;

import java.time.LocalDateTime;
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


public class Fusion extends AgentGrid2D<Cell>{
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

    // @Override
    // public void SetupConstructors() {
    //     _PassAgentConstructor(Cell.class);
    // }
    
    /*------------------------------------------
                MODEL PARAMETERS
     ------------------------------------------*/

    int xDim= 100;
    int yDim = 100;
    int[] divHood = HAL.Util.MooreHood(false); // 8 neighbors for division
    int[] fusHood = HAL.Util.MooreHood(false); //8 neighbors for fusion
    Rand rng=new Rand(); 
    int seed = 0; //option for reproducibility
    //double replicationRate = 0.2; // birth probability
    //double deathRate = 0.01; // death probability
    //double fusionRate = 0.1; //fusion probability
    double initResProb = 0; // mutation probability for initial seeding, for now set to 0 to imitate Andriy's paper

    //int[] divHood = Util.MooreHood(false); // 8 neighbors-- doesn't make sense for fusion 
    public double initRadius = 10;
    public double initMutProp=0.1;
    String initGeom = "circle"; // "circle" or "square" inital population; tumor assumptions favor circle
    int initWidth = 100;
    double initDensity = 0.01;
    String modelType = "drf";
    boolean fusion_present = false;

    
    /*------------------------------------------
                MODEL TRACKING
     ------------------------------------------*/

    Hashtable<String, Integer> cellTypeCounts = new Hashtable<>();
    int fusionctr = 0;
    int[] TypeRepresentants;
    int clonalRichness = 0;
    int timeIndex = 0;
    boolean saveModelState = false;
    FileIO cellTypeCountLogFile = null;
    String cellTypeCountLogFileName = "./data/cellTypeCountLog_" +  LocalDateTime.now().toString() + ".csv";


    /*------------------------------------------
                CELL PARAMETERS
     ------------------------------------------*/

     //genotype things, as defined by @ebaratch
    int allele_num = 20;
    int[] wildtype_genotype = new int[allele_num];
    int max_score;


    //TODO: might want to change these to be functions/distributions, to create more variability between the cell types/introduce more heterogeneity
    //index = 0 is parental, index = 1 is resistant, index = 2 will be fused
    double parDieProb = 0.01; // parental death probability
    double resDieProb = 0.01; // resistant death probability
    double fusDieProb = 0.01; // fused death probability


    //TODO:can make these for phenotypic mutations (for conferring resistance) later
    double parMutProb = 0.01; // parental mutation probability
    // double resMutProb = 0; // resistant mutation probability
    double fusMutProb = 0.01; // fused mutation probability

    double parDivProb = 0.2; // parental replication probability
    double resDivProb = 0.1; // resistant replication probability
    double fusDivProb = 0.1; // fused replication probability

    double fusionRate = 0.0001; //TODO: change this to be specific to different cell types e.g. only parental-res. can fuse!

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

/*-------------------------------------------------------------
         HOUSEKEEPING FUNCTIONS: SAVE AND LOAD PARAMS, etc.
     -----------------------------------------------------------*/
     //NOTE: ADAPTED FROM HAL DOCUMENTATION AND BIRTHDEATH.JAVA FROM HAL DOCS
    
    public void SetSeed(int seed) {
        this.rng = new Rand(seed);
    }

    public void SetDieProbs(double parProb, double resProb, double fusProb) {
        this.parDieProb = parProb;
        this.resDieProb = resProb;
        this.fusDieProb = fusProb;
    }
    public void SetMutProbs(double parProb, double resProb, double fusProb) {
        this.parMutProb = parProb;
        // this.resMutProb = resProb;
        this.fusMutProb = fusProb;
    }

    public void SetDivProbs(double parProb, double resProb, double fusProb) {
        this.parDivProb = parProb;
        this.resDivProb = resProb;
        this.fusDivProb = fusProb;
    }

    public void SetFusionRate(double fusRate) {
        this.fusionRate = fusRate;
    }

    public void SetInitModelConds(double r, int xDim, int yDim, double mutProp, String initGeom, 
        int initWidth, double initDensity, String modelType, boolean fusion_on) {
            this.initRadius = r;
            this.initMutProp = mutProp;
            this.initGeom = initGeom;
            this.initWidth = initWidth;
            this.initDensity = initDensity;
            this.modelType = modelType;
            this.fusion_present = fusion_on;
        }
    
    public void SetSimulationParams(int maxTime, double timeStep, int nReplicates) {
        this.maxTime = maxTime;
        this.nReplicates = nReplicates;
        this.timeStep= timeStep;
    }
/*---------------------------------------------------------------------
               FUSION MODEL UTILITY FUNCTIONS-- ADAPTED FROM @EBARATCH
     -------------------------------------------------------------------*/
    //Function Converting a bit array into vector into the numerical value in base 2
    //source: @ebaratch
    int ConvertBinary(int[] BinaryVector){
        int total=0;
        for (int i=0;i<BinaryVector.length;i++){
            total=total+BinaryVector[i]*(int)Math.pow(2,i);
        }
        return total;
    }
    
     //Function computing Shanon Diversity Index of the population
    //source: @ebaratch
    private double ComputeShannon(){
        double Shanon=0;
        int GenoInt=0;
        int TotalCell=0;
        double sumProp=0;

        for (Cell c:this){
            TotalCell=TotalCell+1;
            GenoInt=ConvertBinary(c.genotype);
            TypeRepresentants[GenoInt]=TypeRepresentants[GenoInt]+1;
        }
        for (int i=0;i<max_score;i++){
            if(TypeRepresentants[i]>0){
                Shanon=Shanon-(TypeRepresentants[i]*1.0/TotalCell)*Math.log(TypeRepresentants[i]*1.0/TotalCell);
                System.out.println("checkProp="+i+(float)TypeRepresentants[i]/TotalCell);
                sumProp=sumProp+(float)TypeRepresentants[i]/TotalCell;
            }
            TypeRepresentants[i]=0;
        }
        System.out.println("checkPop="+TotalCell+","+sumProp);
        return Shanon;
    }

     //Function computing Total number of cancer cells
     //source: @ebaratch
     double ComputeTotal(){
        int TotalCell=0;

        for (Cell c:this){
            TotalCell=TotalCell+1;
        }

        System.out.println("checkPop="+TotalCell);
        return TotalCell;
    }

    //Function computing the amount of mutations in a genome vector
    //source: @ebaratch
    int Score(int[] BinaryVector){
        int total=0;
        for (int i=0;i<BinaryVector.length;i++){
            if (BinaryVector[i]>0){
                total=total+BinaryVector[i];
            }
        }
        return total;
    }

    //Function computing the richness of the population
    //source: @ebaratch
    double ComputeRichness(){
        int Richness=0;
        int GenoInt=0;


        for (Cell c:this){
            GenoInt=ConvertBinary(c.genotype);
            TypeRepresentants[GenoInt]=TypeRepresentants[GenoInt]+1;
        }
        for (int i=0;i<max_score;i++){
            if(TypeRepresentants[i]>0){
                Richness=Richness+1;
            }
            TypeRepresentants[i]=0;
        }

        return Richness;
    }
    /*------------------------------------------
                KEY MODEL FUNCTIONS
     ------------------------------------------*/
     //NOTE: ADAPTED FROM HAL DOCUMENTATION AND BIRTHDEATH.JAVA FROM HAL DOCS
     //SOME FUNCTIONS WERE DEFINED BY @EBARATCH BUT INHERITANCE DIDN'T WORK SO I COPIED THEM HERE

  

    private void turnFusionOff() {
         if (!this.fusion_present) { //make sure to set fusion rate to 0 if fusion is toggled off for this run
        this.fusDivProb = 0;
    }
    }

    
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
        this.turnFusionOff(); //see if need to turn fusion off

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
                // if (rng.Double() < initResProb) {
                //     c.cellType = "r";
                //     c.divRate = resDivProb;
                //     c.deathRate = resDieProb;
                //     //c.resistanceRate = resMutProb; //already resistant! so should be 0
                // } else {
                c.cellType = "p";
                c.divRate = parDivProb;
                c.deathRate = parDieProb;
                c.genotype = wildtype_genotype;
                    //c.resistanceRate = parMutProb;
                //}
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




