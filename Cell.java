import HAL.*;
import HAL.GridsAndAgents.Agent2DBase;
import HAL.GridsAndAgents.AgentSQ2Dunstackable;

import java.util.*;
import java.io.*;
import java.lang.String;



public class Cell extends AgentSQ2Dunstackable<SimpleGrid>{
    //cell characteristics
    String cellType;
    double replicationRate;
    double deathRate;
    //double mutationRate; 
    //double resistanceRate;

    public Cell(String cellType, double replicationRate, double deathRate){
        this.cellType = cellType;
        this.replicationRate = replicationRate;
        this.deathRate = deathRate;
    }

    public String getCellType() {
        return cellType;
    }

    public double getReplicationRate() {
        return replicationRate;
    }

    public double getDeathRate() {
        return deathRate;
    }
  

}
