package ann;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
@SuppressWarnings("unused")
public class SingleLayer extends ANN{
	
	
	//Réseau de type feed-forward : on avance uniquement et pas de retour en arrière, inutile de connecter chaque neurones d'entrés à ceux de sortie

	public SingleLayer(Map<Input, Output> trainingData, Map<Input, Output> testingData) {
		generator = new Random();
		this.trainingData = trainingData;
		this.testingData = testingData;
		inLayer = new ArrayList<InputNeuron>(); 
		outLayer =  new ArrayList<Neuron>();  		  


		for(int i=0;i<(28*28);i++) 			
			inLayer.add(new InputNeuron(255)); //255 pour normalize, car un pixel est situé entre 0 et 255
		
		for (int i=0;i<10;i++) 		
			outLayer.add(new Neuron(new Sigmoid()));

		for(Neuron n : outLayer) {
			for(InputNeuron m : inLayer) 		
				n.addParent(m);	
		}
		
			
	}
	
	public Output feed(Input in){ 
		
		Iterator<Double> iterator = in.iterator();
		double  []tab = new double[10];
		int index=0;
		while(iterator.hasNext()) {
			inLayer.get(index).feed(iterator.next()); 
			index++;
		}
		int i=0;
		for (Neuron t : outLayer){
			t.feed();							
			tab[i]=t.getCurrentOutput();
			i++;		
		}

		Output t = new Output(tab); 
		return t;
	}
	
	
	public Map<Integer,Double> train(int nbIterations) { 
		
		long startTime = System.nanoTime();    

		Map<Integer,Double> results = new HashMap<Integer,Double>();

		for(Neuron n : outLayer)
			n.initWeights();
	
		for(int i=0;i<nbIterations;i++) {
		
			for (Map.Entry<Input, Output> d : trainingData.entrySet()) { 
				
				feed(d.getKey());
				Iterator <Double> it = d.getValue().iterator();
				Iterator <Neuron> myNeurons = outLayer.iterator();
							
					while (myNeurons.hasNext()) {
						
						Neuron n = myNeurons.next();
						double trueValue = it.next();
						n.backPropagate(trueValue); 
						n.updateWeights();
					}					
								
				 }					

			results.put(i,test(testingData,i));					
		}
		
		//Converting nanoseconds to seconds
		double estimatedTime = (System.nanoTime() - startTime) / 1000000000.0;
		System.out.println("The training of your single layer lasted for "+estimatedTime+" seconds");
		return results;		
	}
	
	
	

}
