package ann;

import java.util.Iterator;

public class Input implements Iterable<Double>{
	
	int[][] data;
	
	public Input(int size){
		data = new int[size][size];
	}
	
	public Input(int numRows, int numCols) {
		data = new int[numRows][numCols];
	}
	
	public void setValue(int row, int col, int val) {
		data[row][col] = val;
	}

	
	public double getValue(int row, int col){
		return data[row][col];
	}
	
	public String toString(){
		String res="";
		for (int i=0;i<data.length;i++){
			res+= "{ ";
			for (int j=0;j<data.length-1;j++)
				res+= data[i][j] + ",";
			res+= data[i][data.length-1]+"}";
		}
		return res;
	}

	@Override
	public Iterator<Double> iterator() {
		return new ItData();
	}
	
	private class ItData implements Iterator<Double>{
		private int positionX;
		private int positionY;
		
		public ItData(){
			positionX=0;
			positionY=0;
		}
		
		@Override
		public boolean hasNext() {
			return (positionY< data.length-1 || (positionY== data.length && positionX < data.length-1) );
		}

		@Override
		public Double next() {
			Double val = new Double(data[positionX][positionY]);
			if (positionX==data.length-1){
				positionX=0;
				positionY++;
			}
			else
				positionX++;
			return val;
			
		}
		
	}
	
}
