package FusionTumorModel;

import HAL.lib.CommandLine;
import HAL.lib.CommandLine.*;


public class ConfigureModel {
    @Command(name = "2D ABM Fusion Model", 
        mixinStandardHelpOptions = true, 
        showDefaultValues = true, 
        description = "an on-lattice ABM of tumor evolution in presence and absence of fusion.")

    //Make a DUMMY Model to save default values that can be overwritten by configuration args/strings
    Fusion dummy = new Fusion();
    
    // ------------------------- General Model Properties -------------------------
    @Option(names = {"-s", "--seed"}, description= "Random number seed.")
    int seed = dummy.seed;
    @Option(names = {"-xDim", "--xDim"}, description = "x-dimension of lattice/grid.")
    int xDim = dummy.xDim;
    @Option(names = {"-yDim", "--yDim"},description = "y-dimension of lattice/grid.")
    int yDim = dummy.yDim;
    @Option(names = {"-n", "--nReplicates"}, description = "number of replicates.")
    int nReplicates = dummy.nReplicates;
    @Option(names = { "-w0", "--initialWidth"}, description="Initial population width") 
    int initWidth = dummy.initWidth;
    @Option(names = { "-g0", "--initialGeometry"}, description="Initial population structure geometry") 
    String initGeom = dummy.initGeom;
    @Option(names = { "-d0", "--initialDensity"}, description="Initial population density") 
    double initDensity = dummy.initDensity;
    @Option(names = { "-r0", "--initRadius"}, description="Initial population radius") 
    double initRadius = dummy.initRadius;
    @Option(names = { "-pM", "--pMutant"}, description="Initial mutant proportion") 
    double initMutProp = dummy.initMutProp;
    @Option(names = { "-dt", "--dt"}, description="Time step in days.") 
    double timeStep = dummy.timeStep;
    @Option(names = { "-nTSteps", "--nTSteps"}, description="Number of time steps to run for.")
    int maxTime = dummy.maxTime;
    @Option(names = { "-f", "--fusionOnOff"}, description="Boolean Fusion On or Off.")
    boolean fusion_present = dummy.fusion_present;
    @Option(names = { "-mT", "--modelType"}, description="Model Type e.g. 'death-resistance-fuse model' = 'drf'.")
    String modelType = dummy.modelType;

    // ------------------------- Cell Properties -------------------------
    @Option(names = { "-parDieProb", "--parDieProb"}, description="Probability of parental cell death")
    double parDieProb = dummy.parDieProb;
    @Option(names = { "-resDieProb", "--resDieProb"}, description="Probability of resistant cell death")
    double resDieProb = dummy.resDieProb;
    @Option(names = { "-fusDieProb", "--fusDieProb"}, description="Probability of fused cell death")
    double fusDieProb = dummy.fusDieProb;

    @Option(names = { "-parDivProb", "--parDivProb"}, description="Probability of parental cell replication")
    double parDivProb = dummy.parDivProb;
    @Option(names = { "-resDivProb", "--resDivProb"}, description="Probability of resistant cell replication")
    double resDivProb = dummy.resDivProb;
    @Option(names = { "-fusDivProb", "--fusDivProb"}, description="Probability of fused cell replicaion")
    double fusDivProb = dummy.fusDivProb;

    @Option(names = { "-parDivProb", "--parDivProb"}, description="Probability of parental cell mutation")
    double parMutProb = dummy.parMutProb;
    @Option(names = { "-resDivProb", "--resDivProb"}, description="Probability of resistant cell mutation")
    double resMutProb = dummy.resMutProb;
    @Option(names = { "-fusDivProb", "--fusDivProb"}, description="Probability of fused cell mutation")
    double fusMutProb = dummy.fusMutProb;
   
    @Option(names = { "-fusProb", "--fusProb"}, description="Probability of cell fusion.")
    double fusionRate = dummy.fusionRate;

    // // ------------------------- Output - Text -------------------------
    // @Option(names = { "--outDir"}, description="Directory which to save output files to.") 
    // String outDir ;
    // @Option(names = { "--imageOutDir"}, description="Directory which to save images to.") 
    // String imageOutDir;

    // // ------------------------- Output - Visualisation -------------------------
    // @Option(names = { "--imageFrequency"}, description="Frequency at which an image of the tumour is saved. Negative number turns it off.") 
    // int imageFrequency;
    // @Option(names = { "-visualiseB", "--visualiseB"}, description="Whether or not to show visualization.")
    // Boolean visualiseB;
    // @Option(names = { "--saveModelState"}, description="Whether or not to save the model object at the end of the simulation.") 
    // Boolean saveModelState ;
    // @Option(names = { "--saveFinalDiffImg"}, description="Save final image of diffusion grid.") 
    // Boolean saveFinalDiffImg;
    // @Option(names = { "--saveFinalDiffGrid"}, description="Save final image of diffusion grid.") 
    // Boolean saveFinalDiffGrid;
    // @Option(names = { "--saveFinalPopGrid"}, description="Save final population grid") 
    // Boolean saveFinalPopGrid;

     public static void main(String[ ] args) {
        int exitCode = new CommandLine(new ConfigureModel()).execute(args);
        System.exit(exitCode);
    }





}