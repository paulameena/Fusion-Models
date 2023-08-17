
import HAL.GridsAndAgents.AgentSQ2Dunstackable;
import java.lang.String;
import HAL.Gui.GridWindow;
import HAL.GridsAndAgents.AgentGrid2D;
import HAL.Rand;
import HAL.Util;



public class Cell extends AgentSQ2Dunstackable<Fusion>{
    //cell characteristics
     String cellType; //options are p, r, or f
    
    //making these cell-type specific (instead of model-specific) so unique phenotypic behaviors can be adopted later
    //double mutationRate; 
    double divRate;
    double resistanceRate;
    double deathRate;
    

    //CONSTRUCTOR NOT NEEDED ACCORDING TO HAL DOCUMENTATION?

    // public Cell(String cellType, double replicationRate, double deathRate){
    //     this.cellType = cellType;
    //     this.replicationRate = replicationRate;
    //     this.deathRate = deathRate;
    // }

     /*------------------------------------------
               HOUSEKEEPING FUNCTIONS
     ------------------------------------------*/
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

    public double getResistanceRate() {
        return this.resistanceRate;
    }

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

    public void Step() {
        if (G.rng.Double() < this.deathRate) {
            Dispose();
            return;
        }
        if (G.rng.Double() < this.divRate) {
            int nOptions = G.MapEmptyHood(G.divHood, Xsq(), Ysq());
            if(nOptions>0) {
                int iDaughter = G.divHood[G.rng.Int(nOptions)];
                Cell daughter = G.NewAgentSQ(iDaughter);

                if( this.resistanceRate > 0){
                    if (G.rng.Double() < resistanceRate) {
                        daughter.cellType = "r";
                        daughter.deathRate = G.dieProbs[1];
                        daughter.divRate = G.divProbs[1];
                        daughter.resistanceRate = G.resistanceRates[1];
                        return;
                    }
                }
                // else inherit the properties of this cell
                //TODO: update later to  introduce more heterogeneity with division, even within cell types
                daughter.cellType = this.cellType;
                daughter.deathRate = this.deathRate;
                daughter.resistanceRate = this.resistanceRate;
                daughter.divRate = this.divRate;
            }
        }
         //otherwise nothing happens and the function returns
            return;
    }
  

}
