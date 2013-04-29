package pencilbox.kakuro;

import java.util.ArrayList;

/**
 * 
 */
public class HintTbl {
	static final int D1 = 1 << 1;
	static final int D2 = 1 << 2;
	static final int D3 = 1 << 3;
	static final int D4 = 1 << 4;
	static final int D5 = 1 << 5;
	static final int D6 = 1 << 6;
	static final int D7 = 1 << 7;
	static final int D8 = 1 << 8;
	static final int D9 = 1 << 9;
	static final int D_ALL = D1 + D2 + D3 + D4 + D5 + D6 + D7 + D8 + D9;
	static final int[] D_ARRAY = {D1 , D2 , D3 , D4 , D5 , D6 , D7 , D8 , D9};
	static final int MIN[] = {1, 3, 6, 10, 15, 21, 28, 36, 45 };
	static final int MAX[] = {9, 17, 24, 30, 35, 39, 42, 44, 45};
	//  9876543210

	static ArrayList<ArrayList<ArrayList<Integer>>> wk;
	static int[][][] pattern; // [Œ…”][‡Œv][index]
	static{
		wk = new ArrayList<ArrayList<ArrayList<Integer>>>(9);
		// ArrayList[”š”][‡Œv]
		for(int d=1; d<=9; d++){
			int min = MIN[d-1];
			int max = MAX[d-1];
			wk.add(d-1, new ArrayList<ArrayList<Integer>>(max-min+1));
			for(int i=min; i<=max; i++){
				wk.get(d-1).add(i-min, new ArrayList<Integer>());
			}
		}
		for(int d0=1; d0<=9; d0++){
			int sum0 = d0;
			int pat0 = 1<<d0;
			addPatInWk(0, sum0-MIN[0], pat0);			
			for(int d1=d0+1; d1<=9; d1++){
				int sum1 = sum0 + d1;
				int pat1 = pat0 + (1<<d1);
				addPatInWk(1, sum1-MIN[1], pat1);
				for(int d2=d1+1; d2<=9; d2++){
					int sum2 = sum1 + d2;
					int pat2 = pat1 + (1<<d2);
					addPatInWk(2, sum2-MIN[2], pat2);
					for(int d3=d2+1; d3<=9; d3++){
						int sum3 = sum2 + d3;
						int pat3 = pat2 + (1<<d3);
						addPatInWk(3, sum3-MIN[3], pat3);
						for(int d4=d3+1; d4<=9; d4++){
							int sum4 = sum3 + d4;
							int pat4 = pat3 + (1<<d4);
							addPatInWk(4, sum4-MIN[4], pat4);
							for(int d5=d4+1; d5<=9; d5++){
								int sum5 = sum4 + d5;
								int pat5 = pat4 + (1<<d5);
								addPatInWk(5, sum5-MIN[5], pat5);
								for(int d6=d5+1; d6<=9; d6++){
									int sum6 = sum5 + d6;
									int pat6 = pat5 + (1<<d6);
									addPatInWk(6, sum6-MIN[6], pat6);
									for(int d7=d6+1; d7<=9; d7++){
										int sum7 = sum6 + d7;
										int pat7 = pat6 + (1<<d7);
										addPatInWk(7, sum7-MIN[7], pat7);
										if(d7==9) continue;
										int d8=9;
										int sum8 = sum7 + d8;
										int pat8 = pat7 + (1<<d8);
										addPatInWk(8, sum8-MIN[8], pat8);
									}
								}
							}
						}
					}
				}
			}
		}
		pattern = new int[9][][];
		for(int d=0; d<=8; d++){
			ArrayList<ArrayList<Integer>> a2 = wk.get(d);
			int size2 = a2.size();
			int[][] p2 = pattern[d] = new int[size2][];
			for(int i2=0; i2<size2; i2++){
				ArrayList<Integer> a3 = a2.get(i2);
				int size3 = a3.size();
				int[] p3 = p2[i2] = new int[size3];
				for(int i3=0; i3<size3; i3++){
					p3[i3] = a3.get(i3).intValue();
				}
			}
		}
		wk = null;
//		printAllPatterns();
	}
	static void addPatInWk(int nn, int sum, int pat){
		wk.get(nn).get(sum).add(new Integer(pat));		
	}
	static int getRemainingDigit(int sum, int no, int used) {
		int allowedPattern = 0;
		int noIndex = no-1;
		int min = MIN[noIndex];
		int max = MAX[noIndex];
		if(sum < min) return 0;
		if(sum > max) return 0;
		int[] possible = pattern[no-1][sum-min];
		int size = possible.length;
		for(int i=0; i<size; i++){
			int pat = possible[i];
			if((pat & used) != 0) continue;
			allowedPattern |= pat;
		}
//		allowedPattern = HintTbl.D_ALL;
		return allowedPattern;
	}

//	private static void printAllPatterns() {
//		for(int d=0; d<=8; d++){
//			for(int s=0; s<pattern[d].length; s++){
//				System.out.print("["+(d+1)+","+(s+MIN[d])+"]: ");
//				for(int i=0; i<pattern[d][s].length; i++){
//					System.out.print(patternString(pattern[d][s][i])+",");
//				}
//				System.out.print("\n");
//			}
//		}
//	}
//	private static String patternString(int ptn) {
//		String str = "";
//		for (int l=1; l<=9; l++)
//			if ((ptn & (1<<l)) > 0) str += l;
//		return str;
//	}
}
