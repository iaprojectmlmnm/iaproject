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
			
	}
	
	public Output feed(Input in){ //Calcule la valeur de sortie du RESEAU de neuronne à partir des données d'entrées dans Input in
		
		Double res=0;
		
		while (in.hasNext()) {
		
			res+=next();
			
		}
		
		h.activate(res);
		Output out(res);
		
		return out;
	}
	
	
	public Map<Integer,Double> train(int nbIterations) { //Renvoi une map qui contient : NumItération -> Nombre erreur
							     // Le nombre d'erreur est renvoyé par la méthode test déjà définie
		
		Map<Integer,Double> results = new Map<Integer,Double>();
		
		for(int i=0;i<nbIterations;i++) {
		
			for (Map.Entry<Input, Output> d : trainingData.entrySet()) { //Pour tous mes input de mes trainingData
				
				
				results.put(i,test(trainingData,i));
				//d.getKey();
				//g.getValue();
				
			}
			
		}
		
		return results;
	}
	

}
