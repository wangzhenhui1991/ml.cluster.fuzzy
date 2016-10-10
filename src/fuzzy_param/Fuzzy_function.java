package fuzzy_param;

public class Fuzzy_function {
	/**
	 * 随机生成a-b的随机数(double),默认a为下限，b为上限
	 * @param a
	 * @param b
	 * @return
	 */
	public static double getRandomNumber(double a, double b) {
	    if (b < a)
	        return getRandomNumber(b, a);
	    double a_d=a,b_d=b;
	    double result=a_d +  ((1 + b - a) * Math.random());
	    if(result>b_d || result<a_d)
	    	return getRandomNumber(b, a);
	    
	    return convert_floor(result);
	}
	
	/**
	 * floor 执行向下舍入，即它总是将数值向下舍入为最接近的整数；
	 */
	public static double convert_floor(double a){
		int result=10;
		for (int i=1;i<Fuzzy_parametre.getAfter_dot();i++)
		{
			result=result*10;
		}
		return Math.floor(a * result) / result;
	}
	/**
	 * ceil 向上舍入，即它总是将数值向上舍入为最接近的整数
	 */
	public static double convert_ceil(double a){
		int result=10;
		for (int i=1;i<Fuzzy_parametre.getAfter_dot();i++)
		{
			result=result*10;
		}
		return Math.ceil(a * result) / result;
	}
	/**
	 * 以精度0.01进行四舍五入
	 * @param a
	 * @return
	 */
	public static double floorconvert(double a){
		double result;
		double x = a - Math.floor(a*100)/100;
		if(x<0.005)
		{
			result=convert_floor(a);
		}
		else{ 
			if(x>0.005){
				result=convert_ceil(a);
			}
			else{
				result=convert_floor(a);
			}
			
		}
		return result;
		
	}
}
