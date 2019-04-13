
import java.util.Arrays;
import java.util.Scanner;

public class Main {
	static int no_nodes ;
	static String[] nodes ;
	static int inputNode ;
	static int outputNode ;
	static int no_branches ;
	static int no_nonTouching ;
	static int no_initialbranches ;
	static int[] FirstNode ;
    static int[] SecondNode ;
    static int[] gains ;
    static int[][] paths ;
    static int no_paths = 0 ;
    static int no_loops = 0 ;
    static int len = 0 ;
    static int[][] path ;
    static int[][] loop ;
    static int[][] loops ;
	static boolean exist = false;
	static boolean exist1 = false;
	static int size = 0;
	static int counter = 0;
	static int counter1 = 0;
	static boolean check = false;
	static boolean flage = false;
	static int lsize = 0;
	static int llen = 0 ;
	static int[][] nonTouching ;
	static int[][] nonTouchingLoops ;
	static int[][] gnonTouchingLoops1 ;
	static int[][] gnonTouchingLoops2 ;
	static int [] loopgain;
	static int [] pathgain;
	static int [] nonTouchgain;
	static int no_index = 0;
	static double TF =0;
	static int f = 0;
	static int end = 0 ;
    public Main(int nNodes,int nBranches,int[] startNodes,int[] endNodes,int[] gains) {
			no_nodes=nNodes;
			no_branches=nBranches;
			FirstNode=startNodes;
			SecondNode=endNodes;
			this.gains=gains;
			inputNode=1;
			outputNode=nNodes;
			build();
		}
    
    
	public void build() {
		path = new int[no_nodes*no_nodes][no_nodes*no_nodes];
		for(int i = 0 ; i <= no_branches ; i++){
		 if(FirstNode[i] == inputNode){
			 counter = 0;
		     size = 0;
		     findPath(i);
		     len ++;
		}
		}
		paths = new int[no_nodes*no_nodes][no_nodes*no_nodes];
		for(int i = 0 ; i < len ; i++){
			for(int j = 0 ; ((j<no_nodes)) ; j++){
				if((path[i][j]!=0)){
				if(path[i][j]==outputNode){
					for(int l =0 ; !(l > j) ; l++){
						paths[no_paths][l] = path[i][l];
					}
					no_paths++;
				}
				}
		}
		}
		loop = new int[no_nodes*no_nodes][no_nodes*no_nodes];
		for(int i = 0 ; i <= no_branches ; i++){
		 if(!(FirstNode[i] < SecondNode[i])){
			 end = FirstNode[i] ;
			 f = 0;
			 counter = 0;
			 counter1 = 0;
			 flage = false ;
			 lsize = 0;
		     findLoops(i);
			 llen ++;
		}
		
		}
		loops =new int[no_nodes*no_nodes][no_nodes*no_nodes];
		for(int i = 0 ; i < llen ; i++){
			for(int j = 1 ; ((j<=no_branches)&&(loop[i][j]!=0)) ; j++){
				if(loop[i][j]==loop[i][0]){
					if(!contain(loop[i])){
					for(int l =0 ; !(l > j) ; l++){
						loops[no_loops][l] = loop[i][l];
					}
					no_loops++;
				}
				}
			}
		}
		calculateloopgain ();
		calculatepathgain ();
		calculateTF();
	}
	public double calculateTF(){
		double deltak = 0;
		double sum = 0 ;
		for(int i = 0 ; i < no_paths ; i++){
			deltak = 0;
			for(int j = 0 ; j < no_loops ; j++){
				if(!ptouch(i,j)){
					deltak = deltak + loopgain[j];
				}
			}
			deltak = 1- deltak ;
			sum = sum+(pathgain[i]*deltak);
		}
		double delta = calculatedelta();
		TF = (double)(sum / delta);
		return TF;
	}
	public static double calculatedelta(){
		double delta = 0 ;
		int gain = 0 ;
		for(int j = 0 ; j < no_loops ; j ++){
			gain = gain + loopgain[j];
		}
		delta = 1-gain;
		for(int i = 2 ; find(i) ; i++){
			gain = 0 ;
			int[] index = findno(i);
			for(int k = 0 ; k < no_index ; k++){
				gain = 1;
				for(int l = 0 ; l < i ; l++){
					gain = gain * loopgain[(gnonTouchingLoops1[index[k]][l])-1];
				}
			}
			delta = delta +(Math.pow(-1, i))*gain ;
		}
		return delta;
	}
	public static boolean find (int i){
		boolean find = false ;
		nonTouchingLoops(i);
		for(int j = 0 ; j < no_nonTouching ; j++){
			if(gnonTouchingLoops1[j][i-1]!=0){
				find = true ;
			}
		}
		return find;
	}
	public static int[] findno (int i){
		int[] index = new int[no_nonTouching];
		no_index = 0;
		nonTouchingLoops(i);
		for(int j = 0 ; j < no_nonTouching ; j++){
			if(gnonTouchingLoops1[j][i-1]!=0){
				index[no_index] = j ;
				no_index++;
			}
		}
		return index;
	}
	
	public static void calculateloopgain (){
		loopgain = new int[no_loops];
		for(int i = 0 ; i < no_loops ; i++){
			loopgain[i] = 1;
			for(int j = 0 ; (j < no_nodes*no_nodes); j++){
				if((loops[i][j] != 0)){
				int k = j+1 ;
					for(int n = 0 ; n <= no_branches ; n++){
						if(loops[i][j] == FirstNode[n]){
							if(loops[i][k] == SecondNode[n]){
								loopgain[i] = loopgain[i]*gains[n];
						}
					}
					}
				}
			}
		}
	}
	
	public static void calculatepathgain (){
		pathgain = new int[no_paths];
		for(int i = 0 ; i < no_paths ; i++){
			pathgain[i] = 1;
			for(int j = 0 ; (j < no_nodes*no_nodes) ; j++){
				if((paths[i][j] != 0)){
				int k = j+1 ;
					for(int n = 0 ; n <= no_branches ; n++){
						if(paths[i][j] == FirstNode[n]){
							if(paths[i][k] == SecondNode[n]){
								pathgain[i] = pathgain[i]*gains[n];
							}
						}
					}
				}
				}
		}
	}
	public static boolean ptouch (int i , int j){
		boolean touch = false; 
		for(int l = 0 ; ((l<no_nodes*no_nodes)&&(paths[i][l] != 0)) ; l++){
			for(int m = 0 ; ((m<no_nodes*no_nodes)&&(loops[j][m] !=0)) ; m++){
				if(paths[i][l] == loops[j][m]){
					touch = true ;
				}
			}
		}
		return touch;
		
	}
	
	public static int[][] nonTouchingLoops(int i){
		boolean touch = false ;
		gnonTouchingLoops1 = new int[no_loops*no_loops][no_loops*no_loops];
		if(i==2){
			nonTouchingLoops();
			gnonTouchingLoops1 = nonTouchingLoops;	
		}
		else{
			gnonTouchingLoops2 = new int[no_loops*no_loops][no_loops*no_loops];
			gnonTouchingLoops2 = nonTouchingLoops(i-1);
			for(int j = 0 ; gnonTouchingLoops2[j][0] != 0 ; j++){
				for(int l = 0 ; l < no_loops ; l++){
					for(int k = 0 ; k < i-1 ; k++){
						touch = Touch (l , gnonTouchingLoops2[j][k]-1);
						
					}
					if(touch){
						continue ;
					}
					gnonTouchingLoops1[j][i-1] = l ; 
				}
			}
		}
		return gnonTouchingLoops1;
		
	}
	public static void nonTouchingLoops(){
		int s = 0 ;
		nonTouching = new int[no_loops*no_loops][no_loops*no_loops];
		for(int i = 0 ; i < no_loops ; i++){
			for(int j = i+1 ; j < no_loops ; j++ ){
				check = false ;
				for(int l = 0 ; ((l<no_nodes*no_nodes)&&(loops[i][l] != 0)) ; l++){
					for(int m = 0 ; ((m<no_nodes*no_nodes)&&(loops[j][m] !=0)) ; m++){
						if(loops[i][l] == loops[j][m]){
							check = true ;
						}
					}
				}
				if(!check){
					nonTouching[s][0] = i+1 ;
					nonTouching[s][1] = j+1;
					s++;
			}
		}
	}   
		nonTouchingLoops = new int[no_loops*no_loops][2];
		no_nonTouching = 0;
		for(int i = 0 ; i < s ; i++){
			if(nonTouching[i][1] != 0){
				nonTouchingLoops[no_nonTouching] = nonTouching[i];
				no_nonTouching++;
			}
		}
	}
	public static boolean Touch(int i , int j){
		check = false ;
		for(int l = 0 ; ((l<no_nodes*no_nodes)&&(loops[i][l] != 0)) ; l++){
			for(int m = 0 ; ((m<no_nodes*no_nodes)&&(loops[j][m] !=0)) ; m++){
				if(loops[i][l] == loops[j][m]){
					check = true ;
				}
			}
		}
		return check;
	}
	
	private static void findPath(int i){
		int z ;
		boolean exist = false;
		if(SecondNode[i]==outputNode){
			path[len][size] = FirstNode[i];
			size ++;
			path[len][size] = outputNode;
		}
		else if((SecondNode[i] > FirstNode[i])){
			path[len][size] = FirstNode[i];
			size++;
			for(int j = 0 ; j <= no_branches ; j++ ){
				if(FirstNode[j] == SecondNode[i]){
					if(counter > 0){
						exist = false ;
						f = 0;
						for(z = 0 ; z < size; z++){
							if(path[len][z]== FirstNode[j]){
								exist = true;
								f = z ;
						}
						}	
						if(exist){
						len++;
						}
						for(int l = 0 ; ((exist)&&(l < f)); l++){
							path[len][l]=path[len-1][l];
							size = l+1;
						}
						
						}
					counter ++;
					findPath(j);
				}
			}
			}
		}
	
	private static void findLoops(int i){
		for(int k = 1 ; k < lsize ; k++){
			if(SecondNode[i] == loop[llen][k]){
				flage = true ;
			}
		}
		if(!flage){
		int z ;
		if(SecondNode[i]== end ){
			exist = false ;
			loop[llen][lsize] = FirstNode[i];
			lsize++;
			loop[llen][lsize] = end;
			lsize ++;
		}
		else if((SecondNode[i] > FirstNode[i])){
			loop[llen][lsize] = FirstNode[i];
			lsize++;
			for(int j = 0 ; j <= no_branches ; j++ ){
				if(FirstNode[j] == SecondNode[i]){
					if(counter > 0){
						exist = false ;
						f = 0;
						for(z = 0 ; z < lsize; z++){
							if(loop[llen][z]== FirstNode[j]){
								exist = true; 
								f = z;
						}
						}	
						if(exist){
						llen++;
						}
						for(int l = 0 ; (exist && (l < f)); l++){
							loop[llen][l]=loop[llen-1][l];
							lsize = l+1; 
							}
						}
					counter ++;
					findLoops(j);
					flage = false;
				}
		    }
		}
		else if((SecondNode[i] < FirstNode[i])){
				loop[llen][lsize] = FirstNode[i];
				lsize++;
				for(int j = 0 ; j <= no_branches ; j++ ){
					if(FirstNode[j] == SecondNode[i]){
						if(counter1 > 0){
							exist1 = false ;
							f = 0 ;
							for(z = 0 ; z < lsize; z++){
								if(loop[llen][z]== FirstNode[j]){
									exist1 = true; 
									f = z;
							}
							}	
							if(exist1){
							llen++;
							}
							for(int l = 0 ; (exist1 &&(l < f)); l++){
								loop[llen][l]=loop[llen-1][l];
								lsize = l+1; 
								}
							}
						counter1 ++;
						findLoops(j);
			            flage = false ;
					}
			}
		}
	}
		}
	public static int[] shift (int[] newloop ){
		int temp = newloop[0];
		int l = 0;
		for(l = 0; l < newloop.length-1 ; l++){
			newloop[l] = newloop[l+1];
		}
		newloop[l] = temp ;	
		return newloop;
	}
	public static boolean contain (int [] newloop){
		        int j = 0;
		        int l = 0;
		        int z = 0 ;
		        for(j = 0 ; newloop[j+2] != 0 ; j++);
		        int[] newloop1 = new int [j+1];
		        for(z = 0 ;z < j+1 ; z++){
		        	newloop1[z] = newloop[z]; 
		        }
		        if(!(z<3)){
				for(int n = 1 ; n < no_nodes ; n++){
				newloop1 = shift (newloop1);
				for(int m = 0 ; m < no_loops ; m++ ){
					for(l = 0 ; loops[m][l+2] != 0 ; l++);
			        int[] newloop2 = new int [l+1];
			        for(int r = 0 ;r < l+1 ; r++){
			        	newloop2[r] = loops[m][r]; 
			        }
					if(equal(newloop1,newloop2)){
						return true;
					}
				}
			}
		        }
		return false;
	}
	public static boolean equal (int [] arr1 , int [] arr2){
		int n = arr1.length;
		int m = arr2.length;
		if(n != m){
			return false;
		}
		Arrays.sort(arr1);
		Arrays.sort(arr2);
		for(int i =0 ; i < n ; i++){
			if(arr1[i] != arr2[i]){
				return false;
			}
		}
		return true;
	}

    }
