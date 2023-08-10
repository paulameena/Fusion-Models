
import HAL.GridsAndAgents.AgentSQ2Dunstackable;
import java.lang.String;



public class Cell extends AgentSQ2Dunstackable<SimpleGrid>{
    //cell characteristics
    static String cellType;
    double replicationRate;
    double deathRate;
    //double mutationRate; 
    //double resistanceRate;

    //CONSTRUCTOR NOT NEEDED ACCORDING TO HAL DOCUMENTATION?
    
    // public Cell(String cellType, double replicationRate, double deathRate){
    //     this.cellType = cellType;
    //     this.replicationRate = replicationRate;
    //     this.deathRate = deathRate;
    // }

    public String getCellType() {
        return cellType;
    }

    public double getReplicationRate() {
        return replicationRate;
    }

    public double getDeathRate() {
        return deathRate;
    }

    public void setReplicationRate(double newReplicationRate) {
        this.replicationRate = newReplicationRate;
    }

    public void setDeathRate(double newDeathRate) {
        this.deathRate = newDeathRate;
    }
  

}
