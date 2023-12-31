/* Define the characteristics for cells in my model
 * 
 * Written by:Paulameena Shultes @paulameena
 * date last updated: 8/31/2023
 * 
 * adapted from: @eshanking and @ebaratch HAL models
 */


package FusionTumorModel;

import HAL.GridsAndAgents.AgentSQ2Dunstackable;
//import Models.FusionModelEtienne.FusionModel2DHeterogeneityContactInhibition.FusionModelContactInhibition;
//import FusionProject.Spatial.AgentBasedModels.Models.FusionModelEtienne.FusionModel2DHeterogeneityContactInhibition.*;

import java.lang.String;
import java.io.PrintWriter;
import java.util.BitSet;
// import Framework.Gui.Window2DOpenGL;
import HAL.Util.*;
import HAL.Gui.*;
// import Framework.Util.*;
// import HAL.GridsAndAgents.AgentGrid2D;
// import HAL.Rand;
// import HAL.Util;




//note that @ebaratch cell type is spherical so that can adjust size of each cell-- fusion cells tend to be bigger than regular cells
// for now keeping with @eshanking's model and making all cells the same size; can adapt if makes results drastically different
public class Cell extends AgentSQ2Dunstackable<Fusion>{
    //cell characteristics
    String cellType; //options are p, r, or f
    int color; //color of cell in visualization
    boolean dead; //has the cell been exhausted yet
    int[] genotype; //genotype of cell (0 = wildtype, 1 = mutation)
    //double cell_radius; //radius of cell

    
    
    //making these cell-type specific (instead of model-specific) so unique phenotypic behaviors can be adopted later
    //double mutationRate; 
    double divRate;
    //double resistanceRate; //DIFFERENCE: resistanceRate is the probability of a cell becoming resistant upon division, referring to the phenotypic change
    double mutRate; //DIFFERENCE: mutRate is the probability of a cell becoming resistant upon division, referring to the genetic change
    double deathRate;
    double fusionRate;    

    //CONSTRUCTOR NOT NEEDED ACCORDING TO HAL DOCUMENTATION?

    // public Cell(String cellType, double replicationRate, double deathRate){
    //     this.cellType = cellType;
    //     this.replicationRate = replicationRate;
    //     this.deathRate = deathRate;
    // }

     /*------------------------------------------
               HOUSEKEEPING FUNCTIONS
     ------------------------------------------*/
     public int GetColor() {
        return color;
     }

     public void SetColor(int color) {
        this.color = color;
     }
     
     public boolean IsDead() {
        return dead;
     }

     public String getCellType() {
        return cellType;
    }

    public void setCellType(String cellType) {
        this.cellType = cellType;
    }
    public double getDivRate() {
        return this.divRate;
    }

    public double getDeathRate() {
        return this.deathRate;
    }

    // public double getResistanceRate() {
    //     return this.resistanceRate;
    // }

    public void setDivRate(double newDivRate) {
        this.divRate = newDivRate;
    }

    public void setDeathRate(double newDeathRate) {
        this.deathRate = newDeathRate;
    }

/*------------------------------------------
                KEY FUNCTIONS
     ------------------------------------------*/
     //NOTE: ADAPTED FROM HAL DOCUMENTATION AND BIRTHDEATH.JAVA FROM HAL DOCS
     //SOME FUNCTIONS WERE DEFINED IN @EBARATCH'S CODE BUT INHERITANCE DIDN'T WORK SO I COPIED THEM HERE

     //Genomes recombination function
     //SOURCE: @ebaratch
    public void Blendind(int[] Genotype1,int[] Genotype2){

        int tempBit=0;
        for (int i=0;i<G.allele_num;i++){
            if(G.rng.Double()<0.5) {
                tempBit = Genotype1[i];

                Genotype1[i] = Genotype2[i];
                Genotype2[i] = tempBit;
            }

        }

    }
    //Cell color calculation function
    //source: @ebaratch
    void CalcCellColor(){
        double binary=0;
        binary=G.ConvertBinary(this.genotype);
        // color= LongRainbowMap(1-1.0*(binary)/(Math.pow(2,G.allele_num)-1));

    }

    private boolean Fuse() {
         double fusProb = G.rng.Double(); // need to create
         int fusOptions = G.MapOccupiedHood(G.fusHood, Xsq(), Ysq()); //can only fuse if in contact with another cell
         //Q: How to check with cells in occupied-- parental, resistant? 
         // Maybe first iteration, we don't care? But realistically should be limited to res-res or parental-resistant 
         if (fusProb < this.fusionRate & fusOptions > 0){
            int iTarget = G.fusHood[G.rng.Int(fusOptions)];
            Cell target = G.AllAgents().get(iTarget); 
            int[] genotype1 = target.genotype;
            int[] genotype2= this.genotype;
            target.Die(); 
            //Q: is the index grabbed by the MapHood functions limited from 0-7 or does it span the whole grid?
            this.Die();
            Blendind(genotype1, genotype2); //create mixed genotype
            Cell fusedCell = G.NewAgentSQ(iTarget);  
            fusedCell.genotype = genotype2;
            fusedCell.cellType = "f";
            fusedCell.deathRate = G.fusDieProb;
            //fusedCell.resistanceRate = G.fusMutProb;
            fusedCell.divRate = G.fusDivProb;
            G.UpdateCellCounts(fusedCell);
            return true;
         }
         else {
            return false;
         }
    }

    //Function changing a random 0 into a 1 in the genome vector
    void Mutate(){

        int indexMut=G.rng.Int(G.allele_num);
        if (this.genotype[indexMut]==0){
            this.genotype[indexMut]=1;
        }

        // SetCellColor();
    }

    private boolean Die(){
        if (G.rng.Double() < this.deathRate) {
            G.UpdateCellDeath(this);
            Dispose();
            return true;
        }
        return false;
    }

    private boolean Replicate(){
        double divProb = G.rng.Double();
        int divOptions = G.MapEmptyHood(G.divHood, Xsq(), Ysq()); //can only divide if no cell there

        if (divProb < this.divRate & divOptions > 0) {
             int iDaughter = G.divHood[G.rng.Int(divOptions)];
             Cell daughter = G.NewAgentSQ(iDaughter);

                if( this.mutRate > 0){
                    if (G.rng.Double() < this.mutRate) {
                       daughter.genotype = this.genotype;
                    }
                }

                // if( this.resistanceRate > 0){
                //     if (G.rng.Double() < resistanceRate) {
                //         daughter.cellType = "r";
                //         daughter.deathRate = G.resDieProb;
                //         daughter.divRate = G.resDivProb;
                //         daughter.resistanceRate = G.resMutProb;
                //         G.UpdateCellCounts(daughter);
                //         return true;
                //     }
                // }
                // else inherit the properties of this cell
                //TODO: update later to  introduce more heterogeneity with division, even within cell types
                daughter.cellType = this.cellType;
                daughter.deathRate = this.deathRate;
                //daughter.resistanceRate = this.resistanceRate;
                daughter.divRate = this.divRate;
                G.UpdateCellCounts(daughter);
                return true;
        }
        return false;
    }

    /* STEP FUNCTION
     *  
     *  modelType = parameter to specify priority order e.g. DRF = dieReplicateFuse (in that order). Order may change evolutionary trajectory
     * 
     */
    public void Step(String modelType) { 

        if (modelType.equals("drf")) {
            boolean first = this.Die();

            if (!first) {
                boolean next = this.Replicate();
                if (!next) {
                    this.Fuse();
                    return;
                }
            }
        }
        else if (modelType.equals("dfr")) {
             boolean first = this.Die();

            if (!first) {
                boolean next = this.Fuse();
                if (!next) {
                    this.Replicate();
                    return;
                }
            }
        }
        else if (modelType.equals("rdf")) {
             boolean first = this.Replicate();

            if (!first) {
                boolean next = this.Die();
                if (!next) {
                    this.Fuse();
                    return;
                }
            }

        }
        else if (modelType.equals("rfd")) {
             boolean first = this.Replicate();

            if (!first) {
                boolean next = this.Fuse();
                if (!next) {
                    this.Die();
                    return;
                }
            }

        } 
        else {
         //otherwise nothing happens and the function returns
            return;
        }
    }

    public void Step() {
        this.Step("drf");
        return;
    }
  

}
