package ann;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Class that reads the MNIST data and run simulation from a user interface
 * 
 * @author stephane
 *
 */
public class MnistData extends Application {

	/**
	 * data is the training data from the MNIST repository
	 */
	Map<Input,Output> data;
	/** testSet is the testing data from the MNIST repository */
	Map<Input,Output> testSet;
	/** numIterations is the number of iterations run for training the  neural network*/
	static int numIterations = 10;
	/** ann is the neural network */
	ANN ann;

	/**
	 * Read two data files from the MNIST repository: one file contains the training instances, the other the label of the training instances
	 * 
	 * @param dataFilePath  path of the data file containing the training data
	 * @param labelFilePath label name of the data file containing the label of the training data
	 * @return a map that associates to each data its associated label
	 * @throws IOException
	 */
	public static Map<Input,Output> readData(String dataFilePath, String labelFilePath) throws IOException {
		
		Map<Input,Output> data = new HashMap<>();
		DataInputStream dataInputStream = new DataInputStream(
				new BufferedInputStream(new FileInputStream(dataFilePath)));
		int magicNumber = dataInputStream.readInt();
		int numberOfItems = dataInputStream.readInt();
		int nRows = dataInputStream.readInt();
		int nCols = dataInputStream.readInt();

		System.out.println("magic number is    : " + magicNumber);
		System.out.println("number of items is : " + numberOfItems);
		System.out.println("number of rows is  : " + nRows);
		System.out.println("number of cols is  : " + nCols);

		DataInputStream labelInputStream = new DataInputStream(
				new BufferedInputStream(new FileInputStream(labelFilePath)));
		int labelMagicNumber = labelInputStream.readInt();
		int numberOfLabels = labelInputStream.readInt();

		System.out.println("labels magic number is: " + labelMagicNumber);
		System.out.println("number of labels is: " + numberOfLabels);

		assert numberOfItems == numberOfLabels;

		for (int i = 0; i < numberOfItems; i++) {
			Input d = new Input(nRows, nCols);
			Output t = new Output(labelInputStream.readUnsignedByte());

			for (int r = 0; r < nRows; r++) {
				for (int c = 0; c < nCols; c++) {
					d.setValue(c, r, dataInputStream.readUnsignedByte());
				}
			}
			data.put(d, t);
			
		}
		dataInputStream.close();
		labelInputStream.close();
		return data;
	}

	/**
	 * launch the simulator: when ran with no argument, the program simply trains a single layer neural network and stops; ran with any argument, it lauches a user interface.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length==0){
			try {
				Map<Input,Output> data = readData("train-images-idx3-ubyte", "train-labels-idx1-ubyte");
				Map<Input,Output> testSet = readData("t10k-images-idx3-ubyte", "t10k-labels-idx1-ubyte"); ;
				SingleLayer ann = new SingleLayer(data,testSet);
        		ann.train(numIterations);
        		System.out.println("done training");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			launch();
		}
	}

	/**
	 * Method that controls the user interface.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// size of one cell in the UI
		int size=5;
		try {
			System.out.println("Reading Data");
			data = readData("train-images-idx3-ubyte", "train-labels-idx1-ubyte");
			testSet = readData("t10k-images-idx3-ubyte", "t10k-labels-idx1-ubyte");
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*---- setting up the user interface --------------------------------*/
		primaryStage.setTitle("Artificial Neural Network");
		Group root = new Group();
		Scene scene = new Scene(root, 1350, 580, Color.LIGHTBLUE);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		/*~~ Label for writing error message in the UI ~~*/
        Label err = new Label("");	
		err.setTextFill(Color.RED);
        root.getChildren().add(err);

		/*~~ Label for  ~~*/
        Label label = new Label("");
        label.setAlignment(Pos.CENTER);
    	label.setLayoutX(80);
    	label.setLayoutY(390);

        /*~~Button for seeing a training example ~~~~~~~~~~~~*/
		Button btn = new Button();
        btn.setText("visualise a random training example");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
            	Input[] t = new Input[data.size()];
            	int val = (int) Math.round(Math.random()*data.size());
            	data.keySet().toArray(t);
            	Input in = t[val];
            	Output out = data.get(in);
            	label.setText("Data #" + val + " label: "+ out);
            	label.setVisible(true);
            	
            	int vx = 80;
            	int vy= 420;
                for (int i = 0; i < 28; i++) {
        			for (int j = 0; j < 28; j++) {
        				Rectangle pix = new Rectangle(vx+i*size, vy+j*size, size, size);
        				int gray = 255-(int) in.getValue(i,j);
        				pix.setFill(Color.rgb(gray, gray,gray));
        				root.getChildren().add(pix);
        			}
        		}
            }
        });
        
        /*~~ graph to show the dynamics of the number of mistakes over the iterations~~~~*/
        ObservableList<XYChart.Series<Integer, Double>> answer = FXCollections.observableArrayList();
        NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();
	    LineChart<Integer,Double> lineChart = new LineChart(xAxis, yAxis);
	    lineChart.setData(answer);
	    lineChart.setTitle("Evolution of the percentage of mistakes");
	    root.getChildren().add(lineChart);
        
        /*~~ Button to start training a single layer neural network ~~~*/
        Button blearn = new Button();
        
        blearn.setText("Train Single Layer Network");
        blearn.setOnAction(new EventHandler<ActionEvent>() {
        	
        	public void handle(ActionEvent event){
        		err.setText("");
        		ann = new SingleLayer(data,testSet);
        		Map<Integer,Double> res = ann.train(numIterations);
        		Series<Integer, Double> single = new Series<Integer, Double>();
        		single.setName("single layer");
        		for (Map.Entry<Integer,Double> entry : res.entrySet())
        			single.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        		answer.add(single);     		
        	}
        });
        
        /*~~ Button to start training a neural network with one hiddden layer ~~~*/
        /*~~ the textfield is used to specify the number of units in the hidden layer */
        Button blearn2 = new Button();
        blearn2.setText("Train Network with one hidden layer");
        Label l = new Label("Number of hidden neurons");
        l.setAlignment(Pos.BASELINE_LEFT);
        TextField askNumNeurons = new TextField (); 
        askNumNeurons.setOnAction(new EventHandler<ActionEvent>() {
        	
        	public void handle(ActionEvent event){
                String text = askNumNeurons.getText();
                int numHN = Integer.parseInt(text);
                blearn2.setText("Train Network with one hidden layer ("+numHN+" neurons)");
        	}	
        	});
        
        blearn2.setOnAction(new EventHandler<ActionEvent>() {
        	
        	public void handle(ActionEvent event){
        		err.setText("");
        		// construct the neural net
        		ann = new OneHiddenLayer(data,testSet);
        		((OneHiddenLayer) ann).numHiddenNeurons = Integer.parseInt(askNumNeurons.getText());
        		// launch training
        		Map<Integer,Double> res = ann.train(numIterations);
        		System.out.println("done training");
        		// graph the error
        		Series<Integer, Double> s = new Series<Integer, Double>();
        		s.setName("one hidden layer");
        		
        		for (Map.Entry<Integer,Double> entry : res.entrySet()){
        			s.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        		}
        		answer.add(s);  
        	}
        });

        /*~~ Button for visualizing the weights of the output layer 
         * This is mainly done for single layer, but can be used for other nets 
         * (but the number of neurons of the last layer should be less than 28x28
         */
        Button bvis = new Button();
        
        bvis.setText("visualise  weights of ouput units");
        bvis.setOnAction(new EventHandler<ActionEvent>() {
        	
        	public void handle(ActionEvent event){
        		if (ann!=null){
				for (int res = 0; res <= 9; res++) {
					Neuron n = ann.outLayer.get(res);
					// find max value of weights
					double max = Double.NEGATIVE_INFINITY, min = Double.POSITIVE_INFINITY;
					for (Neuron ne: n.parents){
						if (n.w.get(ne) > max) max = n.w.get(ne);
						if (n.w.get(ne) < min) min = n.w.get(ne);		
					}		
					int vx=0,vy=0;
					int startX = 350, delta=140;
					switch(res){
					case 0:	vx=startX;vy=420;	break;
					case 1:	vx=startX;vy=280;	break;
					case 2:	vx=startX+delta;vy=280;	break;
					case 3:	vx=startX+2*delta;vy=280;	break;
					case 4:	vx=startX;vy=140;	break;
					case 5:	vx=startX+delta;vy=140;	break;
					case 6:	vx=startX+2*delta;vy=140;	break;
					case 7:	vx=startX;vy=0;	break;
					case 8:	vx=startX+delta;vy=0;	break;
					case 9:	vx=startX+2*delta;vy=0;	break;
					}
					int i=0,j=0;
					for (Neuron ne: n.parents){
						Rectangle pix = new Rectangle(vx + i * size, vy+ j * size, size, size);
						int val = (int) (255 * (-min + n.w.get(ne)) / (-min + max));
						pix.setFill(Color.rgb(val, val, val));
						root.getChildren().add(pix);
						if (i==27){
							i = 0;
							j=j+1;
						}
						else
							i++;
					}					
				}
			}
        	else{
        		err.setText("Train a network first!");
        	}
        	}
		});
        
        /*~~ setting the location of the elements in the UI ~~*/
        btn.setLayoutX(10);
        btn.setLayoutY(10);
        blearn.setLayoutX(10);
        blearn.setLayoutY(40);
        bvis.setLayoutX(10);
        bvis.setLayoutY(70);
        l.setLayoutX(10);
        l.setLayoutY(104);
        askNumNeurons.setLayoutX(180);
        askNumNeurons.setLayoutY(100);
        askNumNeurons.setPrefColumnCount(3);
	    blearn2.setLayoutX(10);
        blearn2.setLayoutY(130);        
        err.relocate(10, 160);
        lineChart.setLayoutX(840);
	    lineChart.setLayoutY(10);    
        
        /*~~ adding the elements in the UI ~~*/
        root.getChildren().add(btn);   
        root.getChildren().add(blearn); 
        root.getChildren().add(blearn2); 
        root.getChildren().add(bvis); 
        root.getChildren().add(label);
        root.getChildren().add(l);
        root.getChildren().add(askNumNeurons);
	}
	
}
