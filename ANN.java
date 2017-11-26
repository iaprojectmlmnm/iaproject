package ann;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Abstract class for an artificial neural network
 * @author stephane
 *
 */
public abstract class ANN {
	
	/** output layer of the neural network */
	List<Neuron> outLayer;
	/**	input layer of the neural network, the InputNeurons are just artefacts to feed the data */
	List<InputNeuron> inLayer; 
	/** trainingData contains the training data, as pairs (data, label) */
	Map<Input, Output> trainingData;
	/** testingData contains the testing data, as pairs (data, label) */
	Map<Input, Output> testingData;
	/** random generator */
	Random generator;
	
	/**
	 * method that trains the neural network for a certain number of iterations (no convergence test is used).
	 * @param numIterations is the number of iterations, i.e. the number of times the algorithm will update using
	 *  the entire training data
	 * @return returns the dynamics of the error: it contains a map that associate one iteration number and the
	 * number of mistakes done on the testing set. 
	 * 
	 */
	public abstract Map<Integer,Double> train(int numIterations);

	/**
	 * Methods that computes the output of the neural network given the input
	 * @param in input data of the neural network
	 * @return the output of the neural network (it is stored in an object output that contains a vector
	 *  of the values of each neurons in the output layer
	 */
	public abstract Output feed(Input in);
	
	/**
	 * Methods that computes the number of mistakes of the neural network on the testing data: for each example in the training data, 
	 * the method compares the output of the network with the label of the data and counts the number of mistakes.
	 * @param data map that contains a set of data (usually used with the testing data)
	 * @param it the number of iterations when this test is called
	 *  (it is only used to print the result on the console)
	 * @return the number of mistakes made on the data set.
	 */
	public double test(Map<Input,Output> data, int it) {
		double err = 0;
		int count =0, numMistakes=0;
		for (Map.Entry<Input, Output> d : data.entrySet()) {
			count++;
			Input in = d.getKey();
			Output out = d.getValue();
			Output result = feed(in);
			if (!out.equals(result))
				numMistakes++;
			Iterator<Neuron> itNeurons = outLayer.iterator();
			Iterator<Double> itData = out.iterator();
			while (itNeurons.hasNext()) {
				Neuron n = itNeurons.next();
				double t = itData.next();
				err += (n.getCurrentOutput() - t) * (n.getCurrentOutput() - t);
			}
		}
		System.out.println("["  + it +  "] err= " + Math.sqrt(err) + " number of mistakes " + numMistakes + " / " + count + "("+ (numMistakes*1.0 / count)+")");
		return numMistakes*1.0/data.size();
	}

	
}
