
import HAL.GridsAndAgents.AgentSQ2Dunstackable;
import java.lang.String;



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
        if (G.rng.Double() < G.replicationRate) {
            int nOptions = G.MapEmptyHood(G.VonNeumannHood, Xsq(), Ysq());
            if(nOptions>0) {
                G.NewAgentSQ(G.VonNeumannHood[G.rng.Int(nOptions)]).;
            }
        }
    }
  

}
