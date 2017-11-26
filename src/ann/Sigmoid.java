package ann;

public class Sigmoid implements Activation{
	
	public double activate(double val){
		return 1.0/(1.0+Math.exp(-val));
	}
	
}
