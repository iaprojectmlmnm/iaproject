package ann;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OneHiddenLayer extends ANN{

	List<Neuron> hiddenLayer;
	
	int numHiddenNeurons = 10;
	

	public OneHiddenLayer(Map<Input, Output> trainingData, Map<Input, Output> testingData) {
		generator = new Random();
		this.trainingData = trainingData;
		this.testingData = testingData;
		
		// to be completed
	}
	
	
	public Output feed(Input in){
		// to be completed
		return null;
	}
	
	

	public Map<Integer,Double> train(int nbIterations) {
		// to be completed
		return null;
	}

	

}
