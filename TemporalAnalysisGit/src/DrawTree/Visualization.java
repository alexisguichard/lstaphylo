package DrawTree;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import TemporalAnalysis.ClusterDetection;

import ConstructTree.Branch;
import ConstructTree.Tree;

public class Visualization extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4217640590060703216L;
	final static JPanel panelPhylo= new PanelPhyloTree();
	static JTree treeBranch = null; 
	
	public static void startVisual(Tree tree)  {

		final int SLIDE_MIN = 1;
	     final int SLIDE_MAX = 8;
	     final int SLIDE_INIT = 6;    //initial frames per second
		JFrame frame = new JFrame("Evolution du paysage scientifique - Arbre phylomemetique");
		
		
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( new Integer( 1), new JLabel("0.1") );
		labelTable.put( new Integer( 2 ), new JLabel("0.2") );
		labelTable.put( new Integer( 3 ), new JLabel("0.3") );
		labelTable.put( new Integer( 4 ), new JLabel("0.4") );
		labelTable.put( new Integer( 5 ), new JLabel("0.5") );
		labelTable.put( new Integer( 6 ), new JLabel("0.6") );
		labelTable.put( new Integer( 7 ), new JLabel("0.7") );
		labelTable.put( new Integer( 8 ), new JLabel("0.8") );
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		///////
		// Construct tree
		///////
		((PanelPhyloTree) panelPhylo).setTree( tree);
		JScrollPane scrollPane = new JScrollPane(panelPhylo);
		
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Phylomememic Tree");
		createNodes(top);
		treeBranch = new JTree(top);
		treeBranch.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		treeBranch.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent e) {
				//Returns the last path element of the selection.
				//This method is useful only when the selection model allows a single selection.
				    DefaultMutableTreeNode node = (DefaultMutableTreeNode)
				    		treeBranch.getLastSelectedPathComponent();

				    if (node == null)
				    //Nothing is selected.     
				    return;

				    Object nodeInfo = node.getUserObject();
				    if (node.isLeaf()) {
				        Branch b = (Branch)nodeInfo;
				        ((PanelPhyloTree) panelPhylo).setTree(new Tree(b));;
				    }
				}
		});
		
		JPanel panel = new JPanel(new BorderLayout());
		
		 //Create the label of slider 
        JLabel sliderLabel = new JLabel("Seuil : ", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        //Create the slider.
        JSlider sliderSeuil = new JSlider(JSlider.HORIZONTAL,
        		SLIDE_MIN, SLIDE_MAX, SLIDE_INIT);
        sliderSeuil.setLabelTable( labelTable );
        sliderSeuil.setMajorTickSpacing(8);
        sliderSeuil.setMinorTickSpacing(1);
        sliderSeuil.setPaintTicks(true);
        sliderSeuil.setPaintLabels(true);
        sliderSeuil.setBorder(
                BorderFactory.createEmptyBorder(0,0,10,0));
        Font font = new Font("Serif", Font.ITALIC, 15);
        sliderSeuil.setFont(font);
        sliderSeuil.addChangeListener(new ChangeListener(){


			public void stateChanged(ChangeEvent e) {
		        JSlider source = (JSlider)e.getSource();
		        if (!source.getValueIsAdjusting()) {
		            double fps = (double)((int)source.getValue())/10;
		            Tree tree = new Tree();
		    		try {
						ClusterDetection.temporalAnalysis(tree, fps);
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    		
		    		((PanelPhyloTree) panelPhylo).setTree(tree);
		    		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Phylomememic Tree");
		    		createNodes(top);
		    		((DefaultTreeModel) treeBranch.getModel()).setRoot(top);
		    		((DefaultTreeModel) treeBranch.getModel()).reload();
		    		
		        }
		    }
        	
        });
        JPanel panelSlider = new JPanel(new BorderLayout());
        panelSlider.add(sliderLabel, BorderLayout.NORTH);
        panelSlider.add(sliderSeuil, BorderLayout.SOUTH);
        
        /////
        // JTree for branch of the tree
        ////
        
       
        
        
        JScrollPane treeView = new JScrollPane(treeBranch);
        
        panel.add(treeView, BorderLayout.WEST);
        panel.add(panelSlider, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
		frame.getContentPane().add(panel);
		frame.setPreferredSize(new Dimension(1500,1000));
		frame.pack();
		frame.repaint();
		frame.setVisible(true);
	}

	private static void createNodes(DefaultMutableTreeNode top) {
		// TODO Auto-generated method stub
		
		DefaultMutableTreeNode category = null;
	    DefaultMutableTreeNode branchs = null;
	    
	    category = new DefaultMutableTreeNode("Branchs");
	    top.add(category);
	    
	    //original Tutorial
	    for(Branch b : PanelPhyloTree.getTree().getBranchs())
	    {
	    	branchs = new DefaultMutableTreeNode(b);
	    	 category.add(branchs);
	    }
	   
		
	}
	

}
