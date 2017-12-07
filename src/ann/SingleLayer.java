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
		List<InputNeuron> inLayer = new ArrayList<InputNeuron>(); //Neuronnes d'entrées de mon réseau
		List<Neuron> outLayer =  new ArrayList<Neuron>();  		  //Neuronnes de sorties de mon réseau

		
		//remplir les neurones d'entrées
		//remplir les neuronnes de sorties
		//Connecter les 28*28 neurones d'entrées dans les 10 neurones de sorties
		// 10 de sortie au 28*28 d'entrées
	
		for(int i=0;i<784;i++) {
			
			inLayer.add(new InputNeuron(255)); //255 pour normalize, car un pixel est situé entre 0 et 255
		}
		
		
		for (int i=0;i<10;i++) {
			
			outLayer.add(new Neuron(new Sigmoid()));
		}

		
		//Connecter chaque neurones de sorties à tout les neurones d'entrées
		//DEBUT
		List<Neuron> tmp = null;
		for(int i=0;i<inLayer.size();i++) 
			tmp.add(inLayer.get(i));				
		
		for(int i=0;i<10;i++)
			outLayer.get(i).parents=tmp;
		//FIN
		
		//Connecter chaque neurones d'entrées aux neurones de sorties
		//DEBUT
		for(int i=0;i<784;i++) {
			
			inLayer.get(i).children=outLayer;
		}		
		//FIN
			
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
