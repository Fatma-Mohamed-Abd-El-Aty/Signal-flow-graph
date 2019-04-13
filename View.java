import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import org.jgraph.JGraph;

public class View extends JFrame implements ActionListener{
	
	int nNodes,nBranches;
	JLabel l1,l2;
	JTextField tf1,tf2;
	JButton b;
	public View() {
		//JGraph g=new JGraph();
		l1=new JLabel("number of nodes");
		l2=new JLabel("number of branches");
		tf1=new JTextField();
		tf2=new JTextField();
		b=new JButton("Ok");
		setSize(500,500);
		setVisible(true);
		setTitle("Signal Flow Graph");
		getContentPane().setLayout(null);
		l1.setBounds(50, 30, 100, 30);
		getContentPane().add(l1);
		tf1.setBounds(200, 30, 100, 30);
		getContentPane().add(tf1);
		l2.setBounds(50, 100, 120, 30);
		getContentPane().add(l2);
		tf2.setBounds(200, 100, 100, 30);
		getContentPane().add(tf2);
		b.setBounds(170,160, 60, 30);
		b.addActionListener(this);
		getContentPane().add(b);
		
		
		
	}
	public static void main(String[] args) {
		View v=new View();

	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		nNodes=Integer.parseInt(tf1.getText());
		nBranches=Integer.parseInt(tf2.getText());
		int[] startNodes =new int[nBranches+1];
		int[] endNodes =new int[nBranches+1];
		int[] gains =new int[nBranches+1];
		
		remove(l1);
		remove(l2);
		remove(tf1);
		remove(tf2);
		remove(b);
		setSize(500,30*(nBranches+4));
		repaint();
		
		JPanel p=new JPanel();
		p.setLayout(null);
		p.setBounds(0, 25, 500, 25*(nBranches+1));
		//p.setPreferredSize(new Dimension(500,400));
		//JScrollPane scroll=new JScrollPane(p);
		JComboBox[] startCombo=new JComboBox[nBranches+1];
		JComboBox[] endCombo=new JComboBox[nBranches+1];
		JTextField[] gainText=new JTextField[nBranches+1];
	    
		JLabel start=new JLabel("Start Node");
		JLabel end=new JLabel("End Node");
		JLabel gain=new JLabel("Gain");
		start.setBounds(140, 5, 100, 15);
		end.setBounds(250, 5, 100, 15);
		gain.setBounds(360, 5, 50, 15);
		add(start);
		add(end);
		add(gain);
		add(p); 
		String[] nodes=new String[nNodes+1];
		for(int i=1;i<=nNodes;i++) {
			nodes[i]=""+i;
		}
		
		for(int i=1;i<=nBranches;i++) {
			JLabel l=new JLabel("Branch "+i );
			l.setBounds(30,25*i , 100, 20);
			p.add(l);
			startCombo[i]=new JComboBox(nodes);
			startCombo[i].setBounds(140, 25*i, 90, 20);
			p.add(startCombo[i]);
			endCombo[i]=new JComboBox(nodes);
			endCombo[i].setBounds(250, 25*i, 90, 20);
			p.add(endCombo[i]);
			gainText[i]=new JTextField();
			gainText[i].setBounds(360, 25*i, 50, 20);
			p.add(gainText[i]);
		}
		JButton ok=new JButton("Ok");
		ok.setBounds(220, 25*(nBranches+2), 60,30);
		add(ok);
		repaint();
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				gains[0]=1;
				for(int i=1;i<=nBranches;i++) {
					startNodes[i]=Integer.parseInt((String) startCombo[i].getItemAt(startCombo[i].getSelectedIndex()));
					endNodes[i]=Integer.parseInt((String) endCombo[i].getItemAt(endCombo[i].getSelectedIndex()));
					gains[i]=Integer.parseInt(gainText[i].getText());
				}
				Main main=new Main(nNodes,nBranches,startNodes,endNodes,gains);
				double tf=main.calculateTF();
				Graph g=new Graph(nNodes,nBranches,startNodes,endNodes,gains,tf,main.no_paths,main.no_loops,main.paths,main.loops);
				
			}
			
		});
		
	}

}
