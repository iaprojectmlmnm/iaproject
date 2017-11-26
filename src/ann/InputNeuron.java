package ann;

/**
 * Models a neuron in the input layer. This neuron does not learn anything (there are no weights!)
 * it is simply used to feed the data in the network.
 * @author stephane
 *
 */
public class InputNeuron extends Neuron {

	double normalise;
	
	public InputNeuron(){
		super(null);
	}
	
	public InputNeuron(double normalise){
		super(null);
		this.normalise = normalise; 
	}
	
	public void feed(double val){
		out=val/normalise;
	}
}
