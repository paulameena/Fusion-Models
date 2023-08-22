package FusionTumorModel;

import java.io.File;
import java.time.LocalDateTime;
import HAL.lib.CommandLine;


/* Adapted from Eshan's DoseResponseModel.java file */

public class RunModel implements Runnable{
    
    Fusion model = new Fusion();
    String outFName;
    String popGridFName;
    //String outDir = "./data/fusionModel";



    new File(outDir). mkdirs(); //make output directory if it doesn't exist


    public void run() {
        //configure model based on user inputs OR defaults
        ConfigureModel configs =new ConfigureModel();
        model.SetDieProbs(configs.parDieProb, configs.resDieProb, configs.fusDieProb);
        model.SetDivProbs(configs.parDivProb, configs.resDivProb, configs.fusDivProb);
        model.SetMutProbs(configs.parMutProb, configs.resMutProb, configs.fusMutProb);
        model.SetFusionRate(configs.fusionRate);
        model.SetInitModelConds(configs.initRadius, configs.xDim, configs.yDim, configs.initMutProp, configs.initGeom, 
            configs.initWidth, configs.initDensity, configs.modelType, configs.fusion_present);
        model.SetSimulationParams(configs.maxTime, configs.timeStep, configs.nReplicates);
        model.SetSeed(configs.seed);
        
    }
}
