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
		inLayer = new ArrayList<InputNeuron>(); //Neuronnes d'entrées de mon réseau
		outLayer =  new ArrayList<Neuron>();  		  //Neuronnes de sorties de mon réseau

		
		//Connecter les 28*28 neurones d'entrées dans les 10 neurones de sorties
		
		//remplir les neurones d'entrées

		for(int i=0;i<(28*28);i++) {
			
			inLayer.add(new InputNeuron(255)); //255 pour normalize, car un pixel est situé entre 0 et 255
		}
		
		//remplir les neuronnes de sorties
		
		for (int i=0;i<10;i++) {
			
			outLayer.add(new Neuron(new Sigmoid()));

		}

		//Connecter chaque neurone de sortie à tout les neurones d'entrées
		//DEBUT
		
		for(Neuron n : outLayer) {

			for(InputNeuron m : inLayer) {
			
				n.addParent(m);
			}
			
		}
		
		//FIN
		
		//Inutile de connecter chaque neurone d'entrée aux neurones de sorties

			
	}
	
	public Output feed(Input in){ //Calcule la valeur de sortie du RESEAU de neuronne à partir des données d'entrées dans Input in
		
		Iterator<Double> iterator = in.iterator();
		double  []tab = new double[10];
		int index=0;
		while(iterator.hasNext()) {
			inLayer.get(index).feed(iterator.next()); //feed de la classe InputNeuron qui applique out= Val/Normalize
			index++;
		}
		int i=0;
		for (Neuron t : outLayer){
			t.feed();								//Feed de Neuron (neuron de sortie) fait appel aux parents, qui ont été nourris précédement
			tab[i]=t.getCurrentOutput();
			i++;		
		}

		Output t = new Output(tab); 
		return t;
	}
	
	
	public Map<Integer,Double> train(int nbIterations) { //Renvoi une map qui contient : NumItération -> Nombre erreur
							     						// Le nombre d'erreur est renvoyé par la méthode test() déjà définie
		Map<Integer,Double> results = new HashMap<Integer,Double>();

		for(Neuron n : outLayer){
			n.initWeights();
		}
				
		
		for(int i=0;i<nbIterations;i++) {
		
			for (Map.Entry<Input, Output> d : trainingData.entrySet()) { //Pour tous mes input de mes trainingData
				
				feed(d.getKey());
				Iterator <Double> it = d.getValue().iterator();
				Iterator <Neuron> myNeurons = outLayer.iterator();
							
					while (myNeurons.hasNext()) {
						
						Neuron n = myNeurons.next();
						double VraiSortie = it.next();
						n.backPropagate(VraiSortie); //effectue erreur = vraiSortie - maSortie <=> (Yk - Sk) sur dévloppez.net
						n.updateWeights();

					}
					

								
				 }
					

			results.put(i,test(testingData,i));
			//d.getKey();
			//g.getValue();
			
		}
		
		
		return results;		
	}
	
	
	

}
