package ann;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

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
		
		Iterator<Double> iterator = in.iterator();
		double  []tab = new double[10];
		int index=0;
		while(iterator.hasNext()) {
			inLayer.get(index).feed(iterator.next()); //feed de la classe InputNeuron qui applique out= Val/Normalize
			index++;
		}
		
		
		int j=0;
		for (Neuron t : hiddenLayer){
			t.feed();								//Feed de Neuron (neuron de sortie) fait appel aux parents, qui ont été nourris précédement
			tab[j]=t.getCurrentOutput();
			j++;		
			//System.out.println(t.getCurrentOutput());
		}
		
		
		int i=0;
		for (Neuron t : outLayer){
			t.feed();								//Feed de Neuron (neuron de sortie) fait appel aux parents, qui ont été nourris précédement
			tab[i]=t.getCurrentOutput();
			i++;		
			//System.out.println(t.getCurrentOutput());
		}

		Output t = new Output(tab); 

		//System.out.println(t);
		return t;
	}
	
	

	public Map<Integer,Double> train(int nbIterations) {
		
		for(Neuron n : outLayer){
			n.initWeights();
		}
		
		for(Neuron n : hiddenLayer){
			n.initWeights();
		}
				
		Map<Integer,Double> results = new HashMap<Integer,Double>();
		
		for(int i=0;i<nbIterations;i++) {
		
			for (Map.Entry<Input, Output> d : trainingData.entrySet()) { //Pour tous mes input de mes trainingData
				
				feed(d.getKey());
				Output vraiValeure = d.getValue();
				Iterator <Double> it = vraiValeure.iterator();
				
				double di=0.;
							
				for (Neuron n : outLayer) {
					
					di = n.getCurrentOutput() * (1 - n.getCurrentOutput() ) * (it.next() - n.getCurrentOutput()) ;
					n.di = di;
				}

					
					double dj;
					for (Neuron n : hiddenLayer) {
						
						dj = n.getCurrentOutput() * (1 - n.getCurrentOutput() ) * Somme (n);
						n.di = dj;
					}
				

				
				 }
				
			
			for (Neuron n : outLayer)
				n.UpdateWeightsHidden();
			
			for (Neuron n : hiddenLayer)
				n.UpdateWeightsHidden();
			
			results.put(i,test(trainingData,i));

			
		}
		
		return results;
		
		
	}
	
	
	
	public double Somme (Neuron pere) { //Tous les neurones qui ont pour entrée la sortie du neuron n
		
		double res=0.;
		
		for (Neuron n : outLayer) {
			
			res+= n.w.get(pere) * n.error;
		}
		
		pere.error=res;
		
		return res;
	}
	

}
