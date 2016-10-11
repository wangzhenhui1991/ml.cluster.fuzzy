package fuzzy_draw;

import java.util.List;
import java.util.Random;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import fuzzy.Fuzzy;
import outil.Point;

public class SwingWorkerFuzzy extends SwingWorker<Fuzzy, String> {

	ChartPanel chart;
	JProgressBar progressBar;

	Fuzzy fuzzy;
	/***
	 * 
	 */
	public SwingWorkerFuzzy(){
		chart=null;
	}
	@Override
	protected Fuzzy doInBackground() throws Exception {
		System.out.println(Thread.currentThread().getName()+"\t is doing In Background.");
    	this.fuzzy=new Fuzzy();
    	fuzzy.cendroid_converge();
		List<Point>[] clust =fuzzy.cluster();
		
		

		return fuzzy;
	}
	
	@Override
	protected void process(List<String> chunks) {
		super.process(chunks);
		
		System.out.println(Thread.currentThread().getName()+chunks.get(chunks.size()-1));
	}
	
	@Override
	protected void done() {
		super.done();
		System.out.println(Thread.currentThread().getName()+"it's done.");
		chart.paint(chart.getGraphics());
	}
	public ChartPanel getChart() {
		return chart;
	}
	public void setChart(ChartPanel chart) {
		this.chart = chart;
	}
	
	public JProgressBar getProgressBar() {
		return progressBar;
	}
	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}
	
}
