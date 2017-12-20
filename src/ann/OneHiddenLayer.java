package ann;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

@SuppressWarnings("unused")
public class OneHiddenLayer extends ANN{

	List<Neuron> hiddenLayer;
	
	int numHiddenNeurons = 20;
	

	public OneHiddenLayer(Map<Input, Output> trainingData, Map<Input, Output> testingData) {
		
		generator = new Random();
		this.trainingData = trainingData;
		this.testingData = testingData;
		inLayer = new ArrayList<InputNeuron>();
		outLayer =  new ArrayList<Neuron>();   
		hiddenLayer =  new ArrayList<Neuron>();   
		Sigmoid s = new Sigmoid();

		for(int i=0;i<(28*28);i++) 		
			inLayer.add(new InputNeuron(255));
		
		for (int i=0;i<10;i++) 		
			outLayer.add(new Neuron(s));
		
		for (int i=0;i<numHiddenNeurons;i++) 		
			hiddenLayer.add(new Neuron(s));


		
		for(Neuron n : outLayer) {
			for(Neuron m : hiddenLayer) 			
				n.addParent(m);		
		}
			
		for(Neuron n : hiddenLayer) {
			for(InputNeuron m : inLayer) 			
				n.addParent(m);		
		}

		
		for(Neuron n : hiddenLayer) {
			for(Neuron m : outLayer) 
				n.addChild(m);
		}
	}
	
	
	public Output feed(Input in){
		
		Iterator<Double> iterator = in.iterator();
		double  []result = new double[10];
		int index=0;
		
		while(iterator.hasNext()) {
			inLayer.get(index).feed(iterator.next());
			index++;
		}
		
		
		for (Neuron t : hiddenLayer)
			t.feed();								
		
		
		int cpt=0;
		for (Neuron t : outLayer){
			t.feed();								
			result[cpt]=t.getCurrentOutput();
			cpt++;		
		}

		return new Output(result);
	}
	
	

	public Map<Integer,Double> train(int nbIterations) {
		long startTime = System.nanoTime();    
		for(Neuron n : outLayer)
			n.initWeights();
		
		for(Neuron n : hiddenLayer)
			n.initWeights();
				
		Map<Integer,Double> results = new HashMap<Integer,Double>();
		
		for(int i=0;i<nbIterations;i++) {
		
			for (Map.Entry<Input, Output> d : trainingData.entrySet()) {
				
				feed(d.getKey());
				Iterator <Double> it = d.getValue().iterator();		
							
				for (Neuron n : outLayer)
					n.error = n.getCurrentOutput() * (1.0 - n.getCurrentOutput() ) * (it.next() - n.getCurrentOutput()) ;
					
				for (Neuron n : hiddenLayer) 				
					n.error = (n.getCurrentOutput() * (1.0 - n.getCurrentOutput())) * n.Somme ();
	
				
				for (Neuron n : outLayer) 
					n.updateWeights();
				
				for (Neuron n : hiddenLayer) 
					n.updateWeights();

			 }
						
			results.put(i,test(testingData,i));
	
		}
		
		//Converting nanoseconds to seconds
		double estimatedTime = (System.nanoTime() - startTime) / 1000000000.0; 
		System.out.println("The training of your hidden layer lasted for "+estimatedTime+" seconds");
		return results;
		
	}
	
}
