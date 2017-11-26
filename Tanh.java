package ann;

public class Tanh implements Activation{

	public double activate(double v){
		return Math.tanh(v);
	}
}
