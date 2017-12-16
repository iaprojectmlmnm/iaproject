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
		inLayer = new ArrayList<InputNeuron>(); //Neuronnes d'entrées de mon réseau
		outLayer =  new ArrayList<Neuron>();  		  //Neuronnes de sorties de mon réseau
		hiddenLayer =  new ArrayList<Neuron>();       //Neuronnes cachés de mon réseau
		// Neuronnes d'entrées aux neuronnes cachés, neuronnes cachés aux neuronnes de sortie
		
		//remplir les neurones d'entrées

		for(int i=0;i<(28*28);i++) {
			
			inLayer.add(new InputNeuron(255)); //255 pour normalize, car un pixel est situé entre 0 et 255
		}
		
		
		//remplir les neuronnes de sorties
		
		for (int i=0;i<10;i++) {
			
			outLayer.add(new Neuron(new Sigmoid()));

		}
		
		for (int i=0;i<numHiddenNeurons;i++) {
			
			hiddenLayer.add(new Neuron(new Sigmoid()));

		}
		
		
		//Connecter chaque neurone de sortie à tout les neurones cachés
		//DEBUT
		
		for(Neuron n : outLayer) {

			for(Neuron m : hiddenLayer) {
			
				n.addParent(m);
			}
			
		}
		
		//Connecter chaque neurone caché à tout les neurones d'entrée
		//DEBUT
		
		for(Neuron n : hiddenLayer) {

			for(InputNeuron m : inLayer) {
			
				n.addParent(m);
			}
			
		}
		//FIN


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
