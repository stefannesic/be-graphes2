package org.insa.algo.carpooling;

import org.insa.algo.AbstractSolution;
import org.insa.graph.Path;

public class CarPoolingSolution extends AbstractSolution {
	
	// optimal solution
	private Path path1;
	
	private Path path2;

    protected CarPoolingSolution(CarPoolingData data, Status status) {
        super(data, status);
    }

    public CarPoolingSolution(CarPoolingData data, Status status, Path path1, Path path2) {
        super(data, status);
        this.path1 = path1;
        this.path2 = path2;
    }
    
    /**
     * @return The path to first origin of this solution, if any.
     */
    public Path getPath1() {
        return path1;
    }
    
    /**
     * @return The path to second origin of this solution, if any.
     */
    public Path getPath2() {
        return path2;
    }
}
