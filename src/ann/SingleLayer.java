package ann;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
public class SingleLayer extends ANN{
	
	
	

	public SingleLayer(Map<Input, Output> trainingData, Map<Input, Output> testingData) {
		generator = new Random();
		this.trainingData = trainingData;
		this.testingData = testingData;
		
		// to be completed
	
	}
	
	public Output feed(Input in){ //Calcule la valeur de sortie du RESEAU de neuronne à partir des données d'entrées dans Input in
		
		Double res=0;
		
		while (in.hasNext()) {
		
			res+=next();
			
		}
		
		h.activate(res);
		Output out(res);
		
		return res;
	}
	
	
	public Map<Integer,Double> train(int nbIterations) {
		// to be completed
		return null;
	}
	

}
