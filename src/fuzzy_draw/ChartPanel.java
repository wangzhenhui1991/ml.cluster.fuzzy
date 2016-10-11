package fuzzy_draw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JPanel;

import outil.Point;
import fuzzy.Fuzzy;
import fuzzy_param.Fuzzy_function;
import fuzzy_param.Fuzzy_parametre;

public class ChartPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6718768834848360555L;
	SwingWorkerFuzzy worker;
	Fuzzy fuzzy;
	
	static int count =1;
	public ChartPanel(SwingWorkerFuzzy worker){
		this.worker=worker;
	}
	   @Override
	   public void paintComponent(Graphics g) {

	        super.paintComponent(g);
	        
	        if(worker!=null && worker.isDone()){
		        try {
					fuzzy=worker.get();
					if(fuzzy!=null)
						System.out.println("get the cluster!");
						doDrawing(g);
//				        drawingAnything(g);
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
//	        doDrawing(g);

	    }
	   
	private void doDrawing(Graphics g) {
		
    	Graphics2D g2d = (Graphics2D) g;

    	
		List<Point>[] clust =this.fuzzy.getCluster();
   		for(int j=0;j<Fuzzy_parametre.getNum_cluster();j++){
   			g2d.setColor(new Color((int)Fuzzy_function.getRandomNumber(0, 255),(int)Fuzzy_function.getRandomNumber(0, 255), (int)Fuzzy_function.getRandomNumber(0, 255)));
   			
   			for(int i=0;i<clust[j].size();i++){
   				
   				Ellipse2D.Double circle =new Ellipse2D.Double(
   						((clust[j].get(i).getX()-Fuzzy_parametre.getValmin_x())*Fuzzy_parametre.getDraw_surface_width())/(Fuzzy_parametre.getValmax_x()-Fuzzy_parametre.getValmin_x())-2.5,
   						Fuzzy_parametre.getDraw_surface_height()-((clust[j].get(i).getY()-Fuzzy_parametre.getValmin_y())*Fuzzy_parametre.getDraw_surface_height())/(Fuzzy_parametre.getValmax_y()-Fuzzy_parametre.getValmin_y())-2.5, 
   						5, 
   						5);
   		        g2d.fill(circle);
   			}
   			
   			Ellipse2D.Double circle =new Ellipse2D.Double(
   					((fuzzy.getFuzzy_data().getCluster_point(j).getX()-Fuzzy_parametre.getValmin_x())*Fuzzy_parametre.getDraw_surface_width())/(Fuzzy_parametre.getValmax_x()-Fuzzy_parametre.getValmin_x())-7.5,
   					Fuzzy_parametre.getDraw_surface_height()-((fuzzy.getFuzzy_data().getCluster_point(j).getY()-Fuzzy_parametre.getValmin_y())*Fuzzy_parametre.getDraw_surface_height())/(Fuzzy_parametre.getValmax_y()-Fuzzy_parametre.getValmin_y())-7.5, 
   					15, 
   					15);
   			
		        g2d.fill(circle);
   		}
        
    }
	private void drawingAnything(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(new Color(125,125,125));
		g2d.fill(new Ellipse2D.Double(0,0,Fuzzy_parametre.getDraw_surface_width(),Fuzzy_parametre.getDraw_surface_height()));
	}

}
