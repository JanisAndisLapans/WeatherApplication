package Util;

// Utility generic class to be used as a tuple for two elements

public class Pair<T1, T2> {
	T1 val1;
	T2 val2;
	
	public Pair(T1 val1, T2 val2) {
		this.val1 = val1;
		this.val2 = val2;
	}

	public T1 getVal1() {
		return val1;
	}

	public void setVal1(T1 val1) {
		this.val1 = val1;
	}

	public T2 getVal2() {
		return val2;
	}

	public void setVal2(T2 val2) {
		this.val2 = val2;
	}
	
}
