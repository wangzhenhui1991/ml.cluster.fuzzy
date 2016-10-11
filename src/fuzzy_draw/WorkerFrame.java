package fuzzy_draw;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker.StateValue;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import fuzzy_param.Fuzzy_parametre;


public class WorkerFrame extends JFrame {

	private static final long serialVersionUID = 8834292378948012300L;
	SwingWorkerFuzzy worker=new SwingWorkerFuzzy();
	ChartPanel surface;
	JProgressBar progressBar;
	JButton btnStart;
	JButton btnEnd;
	
	public WorkerFrame() {
		
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					createGUI();
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	public void createGUI(){
	       setTitle("Simple Java 2D example");
	       setPreferredSize(new Dimension(Fuzzy_parametre.getDraw_surface_width(), Fuzzy_parametre.getDraw_surface_height()));
	       pack();
	       setLocationRelativeTo(null);
	       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       addCompoentToPane(this.getContentPane());
	}
	public  void addCompoentToPane(Container pane){
			
		pane.setLayout(new BorderLayout(1,5));
		
			JPanel fr=new JPanel();
			fr.setLayout(new BorderLayout(1,5));
	       btnStart=new JButton("start");
	       btnEnd=new JButton("end");
	       progressBar=new JProgressBar();
	       progressBar.setVisible(false);
	       
	       fr.add(btnStart,BorderLayout.WEST);
	       fr.add(btnEnd,BorderLayout.EAST);
	       fr.add(progressBar,BorderLayout.CENTER);
	       pane.add(fr,BorderLayout.NORTH);
	       
	       surface=new ChartPanel(worker);
	       surface.setBorder(new TitledBorder("Cluster"));
	       surface.setLayout(new BorderLayout(1,5));

	       pane.add(surface,BorderLayout.CENTER);
		       
	       btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(worker==null){
					worker=new SwingWorkerFuzzy();
				}
			       worker.execute();
			       worker.addPropertyChangeListener(new PropertyChangeListener() {
					
					@Override
				    public void propertyChange(PropertyChangeEvent evt) {
					      if ("progress".equals(evt.getPropertyName())) {
					        progressBar.setValue((Integer) evt.getNewValue());
					      }
					    }
				});
			       worker.addPropertyChangeListener(new PropertyChangeListener() {
				    public void propertyChange(PropertyChangeEvent evt) {
				      if ("state".equals(evt.getPropertyName())) {
				        StateValue state = (StateValue) evt.getNewValue();
				        if (StateValue.STARTED.equals(state)) {
				          btnStart.setEnabled(false);
				          btnEnd.setEnabled(true);
					       progressBar.setVisible(true);
				        } else if (StateValue.DONE.equals(state)) {
				          worker = null;
				          btnStart.setEnabled(true);
				          btnEnd.setEnabled(false);
					       progressBar.setVisible(false);
				        }
				      }
				    }
				  });
			       worker.setChart(surface);

			}
		});

	}
	

}
