package DrawTree;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;

import javax.swing.JPanel;

import ConstructTree.Branch;
import ConstructTree.Tree;
import Dataset.ConstructData;

public class PanelPhyloTree extends JPanel {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1103065127951579183L;
	public final static int WIDTH_CELL = 100;
	public final static int HEIGHT_CELL = 50;
	private static Tree tree;
	/*final BufferedImage ima = 
            new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);*/
		
	
	public PanelPhyloTree(){
		
		/*
		 try {
	            // types possibles : jpeg, png, gif
	            ImageIO.write(ima, "png", new File("test.png"));
	        } catch(IOException e) {
	            System.err.println("Erreur lors de l'écriture de l'image.");
	        }*/
		
	}
	

	private void saveTree() {
		// TODO Auto-generated method stub
		try {
			tree.save();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void startConstructTree() {
		// TODO Auto-generated method stub
		
		tree.calculPosition(null, 50,10);
		this.setPreferredSize(new Dimension(tree.getWidth(),(PanelPhyloTree.HEIGHT_CELL+110)*20));
		saveTree();
		repaint();
		revalidate();
		
		
	}
	
	public static Tree getTree() {
		return tree;
	}

	public void setTree(Tree tree) {
		PanelPhyloTree.tree = tree;
		startConstructTree();
	}

/*
	public BufferedImage getIma() {
		return ima;
	}
*/	
	public void drawSeparateYear(Graphics g){
		int y = 0; 
		for(Integer year=ConstructData.YEAR_START;year<=ConstructData.YEAR_END;year++){
			 g.drawString(year.toString(),0, y + PanelPhyloTree.HEIGHT_CELL);
			 g.drawLine(0, y,tree.getWidth(), y);
			 y+=PanelPhyloTree.HEIGHT_CELL+80;
		}
			
		
	}
	
	
	public void paint(Graphics g){
		drawSeparateYear(g);
		for(Branch b : tree.getBranchs()){
			b.draw(g);
		}
		repaint();
		revalidate();
		
	}
	
}
