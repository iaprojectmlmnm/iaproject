package ann;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class Neuron {
	/** name of the neuron */
	String name;	
	/** List of parent neurons, i.e. the list of neurons that are used as input */
	List<Neuron> parents;
	/** list of child neurons,i.e. the list of neurons that use the output of this neuron */
	List<Neuron> children;
	/** activation function */
	Activation h;
	/** weight of the input neurons: it maps an input neuron to a weight */
	Map<Neuron,Double> w;
	/** value of the learning rate */
	final static double eta = 0.01;	
	/** current value of the output of the neuron */
	protected double out;
	/** current value of the error of the neuron */
	protected double error;
	/** random number generator */
	public static Random generator;

	
	
	/** 
	 * returns the current value of the error for that neuron.
	 * @return
	 */
	public double getError(){return error;}
	
	/**
	 * Constructor
	 * @param h is an activation function (linear, sigmoid, tanh)
	 */
	public Neuron(Activation h){
		if (generator==null)
			generator = new Random();
		this.h=h;
		parents = new ArrayList<>();
		children = new ArrayList<>();	
		w = new HashMap<Neuron,Double>();
	}
	
	public void addParent(Neuron parent){
		parents.add(parent);
	}
	
	public void addChild(Neuron child){
		children.add(child);
	}
	
	/**
	 * Initializes randomly the weights of the incoming edges 
	 */
	public void initWeights(){
			for(Neuron n : parents)
				w.put(n,ThreadLocalRandom.current().nextDouble(-0.5, 0.5)); //nextDouble renvoi une valeur entre 0 et 1

	}
	
	
	
	/**
	 * Computes the output of a neuron that is either in the hidden layer or in the output layer. 
	 * (there are no arguments as the neuron is not an inputNeuron)
	 */
	public void feed(){
		
		double res=0;
		for (Neuron n : parents) {
			res+=(n.getCurrentOutput()*w.get(n));	
		}
		
		out=h.activate(res);

}
	
	
	
	/**
	 * back propagation for a neuron in the output layer 
	 * @param val is the correct value.
	 */
	public void backPropagate(double target){
		error=target-out;	
	}
	
	/**
	 * returns the current ouput (it should be called once the output has been computed, 
	 * i.e. after calling feed)
	 * @return the current value of the ouput
	 */
	public double getCurrentOutput(){
		return out;
	}
	
	/** returns the name of the neuron *
	 * 
	 */
	public String toString(){
		return name + " out: " + out ;
	}
	
	
	/** Updates the weight of a neuron's parents *
	 * @author Nick
	 */
	public boolean updateWeights() {

		for (Entry<Neuron, Double> e : w.entrySet()){
			  
		    double v=e.getValue()+(eta*error*e.getKey().getCurrentOutput());
		    e.setValue(v);
		}
		
		return true;
	}
	
	
	public double Somme () { //Tous les neurones qui ont pour entrée la sortie du neuron n
		
		double res=0.;

		for (Neuron n : children) {
			
			res+=  n.w.get(this) * n.error;
		}
		
		return res;
	}
	
	

	

}
