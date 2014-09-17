package j13;

public class RandomCA {
	private static final int MAX_CELLS = 32;
	
	private int rule;
	private int[] cells;
	
	public RandomCA(){
		rule = 30;
		cells = new int[MAX_CELLS];
		seedCA((int) (System.currentTimeMillis() * 0x5DEECE66DL));
	}
	
	public void seedCA(int seed){
		for(int i=MAX_CELLS-1;i>=0;i--){
			cells[i] = seed & 1;
			seed>>=1;
		}
	}
	
	private void updateCA(){
		for(int i=0,c=cells[0],a=cells[MAX_CELLS-1];i<MAX_CELLS;i++){
			int b=(i+1==MAX_CELLS)?c:cells[i+1];
			int o=a<<2|cells[i]<<1|b;
			a=cells[i];
			cells[i]=rule>>o&1;
		}
	}
	
	public int next(int bits) {
		// max 32... for now
		int out =0;
		updateCA();
		for(int i=0;i<bits;i++)
			out = out<<1|cells[i];
		return out;
	}
	
	public int nextInt() {		
		return next(32);
	}
	
	public boolean nextBoolean(){
		return next(1) == 0;
	}
	
	public float nextFloat(){
		return next(24) / (float) (1<<24);
	}
	
	public int nextInt(int n){
		// i... really need something better
		int next = next(32);
		if(next < 0) next=~next+1;
		return next % n;
	}
}