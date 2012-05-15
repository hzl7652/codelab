package sort;

public class ZTraverse {

	final static int[][] sort1 = {
		{0 ,2 ,5 ,9 ,13,17},
		{1 ,4 ,8 ,12,16,20},
		{3 ,7 ,11,15,19,22},
		{6 ,10,14,18,21,23}
	};
	final static int[][] sort11 = {
		{0 ,2 ,3 ,9 ,10,17},
		{1 ,4 ,8 ,11,16,28},
		{5 ,7 ,12,15,19,22},
		{6 ,13,14,20,21,23}
	};
	final static int[][] sort2 = {
		{0 ,2 ,5 ,9 },
		{1 ,4 ,8 ,13},
		{3 ,7 ,12,17},
		{6 ,11,16,21},
		{10,15,20,24},
		{14,19,23,26},
		{18,22,25,27}
	};
	final static int[][] sort21 = {
		{0 ,2 ,3 ,9 },
		{1 ,4 ,8 ,10},
		{5 ,7 ,11,17},
		{6 ,12,16,18},
		{13,15,19,24},
		{14,20,23,25},
		{21,22,26,27}
	};
	
	static String  sort(int[][] sort){
		StringBuffer sb = new StringBuffer();
		
		int colNums = sort[0].length;
		int rowNums = sort.length;
		
		int maxLength = colNums + rowNums -1;
		
		for(int i=0;i<maxLength; i++){
			
			// left-down to right-top
			int col = i>(rowNums-1) ? (i-rowNums+1) :0;
			int row = i>(rowNums-1) ? (rowNums-1) :i;
			for(int index =0;index < (i+1) ;index++){
				int r = row - index;
				int c = col + index;
				if(r < 0 || c >(colNums-1)){
					break;
				}else{
					sb.append(sort[r][c]).append(",");
				}
			}
		}
		return sb.toString();
	}
	static String  sortN(int[][] sort){
		StringBuffer sb = new StringBuffer();
		
		int colNums = sort[0].length;
		int rowNums = sort.length;
		
		int maxLength = colNums + rowNums -1;
		
		for(int i=0;i<maxLength; i++){
			if(i%2 == 1){
				// left-down to right-top
				int col = i>(rowNums-1) ? (i-rowNums+1) :0;
				int row = i>(rowNums-1) ? (rowNums-1) :i;
				for(int index =0;index < (i+1) ;index++){
					int r = row - index;
					int c = col + index;
					if(r < 0 || c >(colNums-1)){
						break;
					}else{
						sb.append(sort[r][c]).append(",");
					}
				}
			}else{
				// right-top to left-down
				int row = i>(colNums-1) ? (i-colNums+1) :0;
				int col = i>(colNums-1) ? (colNums-1) :i;
				for(int index = 0;index < (i+1) ; index++){
					int r = row + index;
					int c = col - index;
					if(c < 0 || r >(rowNums -1)){
						break;
					}else{
						sb.append(sort[r][c]).append(",");
					}
				}
			}
		}
		return sb.toString();
	}
	public static void main(String[] args) {
		System.out.println(sort(sort1));
		System.out.println(sortN(sort11));
		System.out.println(sort(sort2));
		System.out.println(sortN(sort21));
	}
}
