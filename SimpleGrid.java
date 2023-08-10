// Written by: Paulameena Shultes
// Date: 08/10/2023
import HAL.*;
import HAL.Interfaces.SerializableModel;

import java.util.*;
import java.io.*;
import HAL.GridsAndAgents.AgentGrid2D;



public class SimpleGrid extends AgentGrid2D<Cell> implements SerializableModel{
    int xDim;
    int yDim;


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



    
}




