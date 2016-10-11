package fuzzy;

import java.util.LinkedList;
import java.util.List;

import fuzzy_param.Fuzzy_function;
import fuzzy_param.Fuzzy_parametre;
import outil.Center;
import outil.Point;

public class Fuzzy {
		
		Fuzzy_data fuzzy_data;
		private double[][] matrix;
		private static int run_count=0;
		private List<Point>[] cluster=null;
		
		
		public List<Point>[] getCluster() {
			return cluster;
		}
		public void setCluster(List<Point>[] cluster) {
			this.cluster = cluster;
		}
		public Fuzzy_data getFuzzy_data() {
			return fuzzy_data;
		}
		public void setFuzzy_data(Fuzzy_data fuzzy_data) {
			this.fuzzy_data = fuzzy_data;
		}
		public double[][] getMatrix() {
			return matrix;
		}
		public void setMatrix(double[][] matrix) {
			this.matrix = matrix;
		}
		
		public Fuzzy() {
			fuzzy_data=new Fuzzy_data(Fuzzy_parametre.getFuzziness(),Fuzzy_parametre.getNum_cluster(), Fuzzy_parametre.getEssaie_number(), Fuzzy_parametre.getValmin_x(),Fuzzy_parametre.getValmax_x(),Fuzzy_parametre.getValmin_y(), Fuzzy_parametre.getValmax_y());
			matrix=new double[Fuzzy_parametre.getEssaie_number()][Fuzzy_parametre.getNum_cluster()];
		}
		/**
		 * 初始化-隶属度矩阵
		 */
		public void init()
		{
			for(int i=0;i<Fuzzy_parametre.getEssaie_number();i++){
				for(int j=0;j<Fuzzy_parametre.getNum_cluster();j++){
					matrix[i][j]=euclidien_distance(fuzzy_data.getData_point(i).getX(),fuzzy_data.getData_point(i).getY(),fuzzy_data.getCluster_point(j).getX() ,fuzzy_data.getCluster_point(j).getY());
				}
			}
			
		}
		/**
		 * 计算欧氏距离
		 * @param x1
		 * @param y1
		 * @param x2
		 * @param y2
		 * @return
		 */
		public double euclidien_distance(double x1,double y1,double x2,double y2){
			return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
		}
		/**
		 * 计算隶属度矩阵
		 */
		public void membership_matrix()
		{
			for(int i=0;i<Fuzzy_parametre.getEssaie_number();i++){
//				System.out.print(i+":");
		    	double result=0.0;
				for(int j=0;j<Fuzzy_parametre.getNum_cluster();j++){
					result=result+Math.pow(1/matrix[i][j],1/(fuzzy_data.getFuzziness()-1));
				}
				for(int j=0;j<Fuzzy_parametre.getNum_cluster();j++){
					matrix[i][j]=Fuzzy_function.floorconvert(Math.pow(1/matrix[i][j],1/(fuzzy_data.getFuzziness()-1))/result);
//					System.out.print("\t"+matrix[i][j]);
				}
//				System.out.println();
				
			}
		}
		
		public void membership_matrix_pcm(){
			for(int i=0;i<Fuzzy_parametre.getEssaie_number();i++){
				System.out.print(i+":");
		    	double result=0.0,d=0.0,eta=0.0;
				for(int j=0;j<Fuzzy_parametre.getNum_cluster();j++){
					double u = Math.pow(matrix[i][j],fuzzy_data.getFuzziness());
					result=result+u;
					d=d+u*Math.pow(matrix[i][j],2);
					eta=d/result;
				}
				for(int j=0;j<Fuzzy_parametre.getNum_cluster();j++){
					double tmp=Math.pow(eta,1/(fuzzy_data.getFuzziness()-1));
					matrix[i][j]=Fuzzy_function.floorconvert(tmp/(Math.pow(matrix[i][j],2/(fuzzy_data.getFuzziness()-1))+tmp));
					System.out.print("\t"+matrix[i][j]);
				}
				System.out.println();
				
			}
		}
		/**
		 * 根据隶属度矩阵，计算新的聚类中心
		 */
		public List<Center> new_cendroid()
		{
			List<Center> center_point=new LinkedList<>();
			for(int j=0;j<Fuzzy_parametre.getNum_cluster();j++){
//				System.out.print(j+":\t");
				double result=0.0,x=0.0,y=0.0;
				
				for(int i=0;i<Fuzzy_parametre.getEssaie_number();i++){
					double u = Math.pow(matrix[i][j],fuzzy_data.getFuzziness());
					result=result+u;
					
					x=x+u*fuzzy_data.getData_point(i).getX();
					y=y+u*fuzzy_data.getData_point(i).getY();
//					System.out.print(matrix[i][j]+"\t");
				}
//				System.out.println();
			//calculate the new cluster center
		    double c_x=x/result;
		    double c_y=y/result;
			center_point.add(new Center(c_x,c_y));
//			System.out.println(j+"\t"+" ∑(U(ij)^m*X(i))="+x+"\t∑(U(ij)^m)"+result+"\tc_x="+ c_x+"\tc_Y="+c_y);
		}
			return center_point;
						
		}
		public void cendroid_converge(){
			boolean cond=true;
			long startTime=System.currentTimeMillis();
			int iterations=0;
			System.out.println(Thread.currentThread().getName()+"\tis clustering...");
			loops:
			while(cond){
				init();
				//计算隶属度矩阵
				membership_matrix();
				//重新计算类中心
				List<Center> new_center=new_cendroid();
				//判断是否满足结束条件
				for(int j=0;j<Fuzzy_parametre.getNum_cluster();j++){
					Double x1=new_center.get(j).getX();
					Double y1=new_center.get(j).getY();
					if(x1.isNaN() || y1.isNaN()){
						break loops;
					}
					if(Math.abs(new_center.get(j).getX()-fuzzy_data.getCluster_point(j).getX())<Fuzzy_parametre.getEpsilon() && Math.abs(new_center.get(j).getY()-fuzzy_data.getCluster_point(j).getY())<Fuzzy_parametre.getEpsilon()){
						cond=false;
					}
					else{
						cond=true;
					}
				}
				
					fuzzy_data.setCluster_point(new_center);
					iterations++;
				
			}
			long endTime=System.currentTimeMillis();
			System.out.println("\nSystem runing:\t"+(endTime-startTime)+"ms");
			System.out.println("iterations:\t"+iterations);
			System.out.println("run count:\t"+(++run_count));
			
		}
		/**
		 * 将每个样本点(根据隶属度 (最大值))划分到所属的类中心(链表)中
		 * 
		 */
		public List<Point>[] cluster()
		{
			if(cluster!=null) return cluster;
			this.cluster=new List[Fuzzy_parametre.getNum_cluster()];
			for(int i=0;i<Fuzzy_parametre.getNum_cluster();i++){
				cluster[i]=new LinkedList<>();
			}
			for(int i=0;i<Fuzzy_parametre.getEssaie_number();i++){
				int max=0;
				double value=0;
				for(int j=0;j<Fuzzy_parametre.getNum_cluster();j++){
					if(matrix[i][j]>value){
						max=j;
						value=matrix[i][j];
					}
				}
				cluster[max].add(fuzzy_data.getData_point(i));
			}
			return cluster;
		}

	
		

			
}
