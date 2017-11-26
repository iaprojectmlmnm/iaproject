package ann;

import java.util.Iterator;

public class Output implements Iterable<Double> {
	double[] value;

	public Output(int val) {
		value = new double[10];
		value[val] = 1;
	}
	
	public Output(double[] val){
		value = val;
	}

	public double[] getVal() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Output) {
			Output o = (Output) obj;
			int tindex = 0;
			double max = value[0];
			for (int i = 1; i < value.length; i++) {
				if (max < value[i]) {
					max = value[i];
					tindex = i;
				}
			}
			int oindex = 0;
			max = o.value[0];
			for (int i = 1; i < o.value.length; i++) {
				if (max < o.value[i]) {
					max = o.value[i];
					oindex = i;
				}
			}
			return oindex == tindex;
		} else
			return false;
	}

	private class ItData implements Iterator<Double> {
		private int position;

		public ItData() {
			position = 0;
		}

		@Override
		public boolean hasNext() {
			return (position < value.length - 1);
		}

		@Override
		public Double next() {
			Double val = new Double(value[position]);
			position++;
			return val;
		}

	}

	public String toString() {
		int index = 0;
		double max=value[0];
		// look for the max
		for (int i=1;i<value.length;i++){
			if (value[i] > max){
				max=value[i];
				index=i;
			}
		}
		return index + "(" + max + ")";
	}

	@Override
	public Iterator<Double> iterator() {
		return new ItData();
	}
}
