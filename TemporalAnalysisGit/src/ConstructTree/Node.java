package ConstructTree;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import Cluster.CategoryTerm;
import Cluster.Cluster;
import Cluster.Term;
import Dataset.ConstructData;
import DrawTree.Drawing;
import DrawTree.PanelPhyloTree;
/**
 * Node of the tree contains title and content for description
 * @author guichard
 *
 */
public class Node extends Drawing implements Comparable<Node>{
	/**
	 * ID of the node construct as NUMCLUSTER-YEAR
	 */
	private Integer identifier;
	private double similarityUnion = 0.0;
	public Integer getIdentifier() {
		return identifier;
	}

	/**
	 * Title of the node : most popular terms and the year 
	 */
	private String title;
	/**
	 * Contents of the node : most popular term (new, recombination...)
	 */
	private HashSet<Term> contents;
	/**
	 * Children of the current node (this)
	 */
	private ArrayList<Node> childrens;
	
	private Cluster cluster;

	// Constructor
	/**
	 * Constructor of the node, with a title 
	 * @param identifier
	 * @see Node#title
	 */
	public Node(Integer id, Cluster cluster) {
		this.identifier = id;
		this.cluster = cluster;
		this.title = cluster.getListTerm().iterator().next()+"-"+cluster.getYear()+" Term : "+ cluster.getListTerm().size();
		childrens = new ArrayList<Node>();
		contents = new HashSet<Term>();
	}
	
	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	

	// Properties
	/**
	 * Get the title of the node
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
	}

	public HashSet<Term> getContents() {
		return contents;
	}

	public void setContents(HashSet<Term> contents) {
		this.contents = contents;
	}

	public ArrayList<Node> getChildrens() {
		return childrens;
	}

	public void setChildrens(ArrayList<Node> childrens) {
		this.childrens = childrens;
	}


	/**
	 * Add new children for this node
	 * @param children
	 */
	public void addChild(Node children) {
		this.childrens.add(children);
	}
	
	public void removeChild(Node children){
		this.childrens.remove(children);
	}
	
	public String toString(BufferedWriter bw){
		 try {
			bw.write("		<child id=\""+ this.getIdentifier()+"\" /><name>"+ this.getTitle()+"</name>");
			if(getChildrens().size() > 0) {
				 bw.write("		\n<childs>	\n");
				 for (Node child : getChildrens()) {
					 child.toString(bw);
				 }
				 bw.write("		\n</childs>\n");
			 }
			 bw.write("</child>");
			 bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
		return "ok";
	}
	

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
		boolean recomb = false;
		/*
		for(Term t : contents)
			if(t.getCategory().equals(CategoryTerm.EMERGENT)){
				g.setColor(Color.red);
				break;
			}
			else if(t.getCategory().equals(CategoryTerm.RECOMBINATION))
			{
				g.setColor(new Color(243, 214, 23));
				recomb = true;
				
			}
			else if(!g.getColor().equals(Color.red) && !recomb)
				g.setColor(Color.green);
			
				*/
		
		g.fillRect(getX(), getY(),getWidth(),20);
		
		g.setColor(Color.white);
		g.fillRect(getX(), getY()+20,getWidth(),(12*5));
		
		g.setColor(Color.black);
		g.drawString(getTitle(),getX()+15,getY()+15);
		int i = 30;
		ArrayList<Term> temp = new ArrayList<Term>();
		temp.addAll(contents);

		for(int k = 0; k<=5;k++){
			if(contents.size()-1 >k){
				//if(temp.get(k).getCategory().equals(CategoryTerm.EMERGENT))
					g.setColor(Color.red);
			/*	else if(temp.get(k).getCategory().equals(CategoryTerm.RECOMBINATION))
					g.setColor(new Color(243, 214, 23));
				*/g.drawString(temp.get(k).getValue(),getX()+15,getY()+i);
				g.setColor(Color.black);
				i +=10;
				}
		}
		
		for(Node child : getChildrens()){
			g.setColor(Color.white);
			child.draw(g);
			g.setColor(Color.black);
				g.drawString(Double.toString(this.getCluster().getSimilarity()),
						(getX()+(PanelPhyloTree.WIDTH_CELL/2)+ child.getX()+(PanelPhyloTree.WIDTH_CELL/2))/2
								,(getY()+PanelPhyloTree.HEIGHT_CELL+child.getY())/2 );
			 g.drawLine(getX()+(PanelPhyloTree.WIDTH_CELL/2), getY()+20+(12*5),
					 child.getX()+(PanelPhyloTree.WIDTH_CELL/2), child.getY());
		}
		
	}

	@Override
	public int calculPosition(Drawing d, int minX, int y) {
		// TODO Auto-generated method stub
		y = 30;
		int maxWidth = 0;
		for(Integer year=ConstructData.YEAR_START;year<=ConstructData.YEAR_END;year++){
		
			if(cluster.getYear().equals(year)){
				setY(y);
				for(Node n : getChildrens()){
					setHeight(n.getHeight());
					int tmp =0;
					tmp = n.calculPosition( this, minX, y);
					if(tmp> maxWidth) maxWidth = tmp;
					if( getChildrens().size() > 1)
						minX += widthString(n.getTitle())+150;
					
				}
				if(getChildrens().size() > 1 ){
					setX((getChildrens().get(getChildrens().size()-1).getX()+getChildrens().get(0).getX())/2);
					
					setHeight(getHeight()+PanelPhyloTree.HEIGHT_CELL);
				}
				else if(((Node) d).getChildrens().size() > 1 && ((Node) d).getChildrens().indexOf(this) != 0 ) 
					setX(((Node) d).getChildrens().get(((Node) d).getChildrens().indexOf(this)-1).getX()+widthString(((Node) d).getChildrens().get(((Node) d).getChildrens().indexOf(this)-1).getTitle())+150);
				else if (getX()==0){
					setX(minX);
					setHeight(PanelPhyloTree.HEIGHT_CELL);
				}
				else{
					setX((getX()+minX)/2);
				}
				setWidth(widthString(getTitle()));
				
				if(minX > maxWidth) maxWidth = minX;
			}
			y+=PanelPhyloTree.HEIGHT_CELL+80;
		}
		return maxWidth;
		
	}

	public void findContent(Node root, Set<Term> listAntecedentTerm) {
		// TODO Auto-generated method stub
		Set<Term> tmp = listAntecedentTerm;
		
		// Terme présent chez le pére 
		if(!(root == this)){
			tmp = root.getCluster().getListTerm();

			tmp.retainAll(this.getCluster().getListTerm());
			
			for(Term t : tmp)
				t.setCategory(CategoryTerm.INHERITED);
			contents.addAll(tmp);
		}
			
		
		// Term présent chez les antécédents
		tmp = listAntecedentTerm;
		tmp.removeAll(contents);
		tmp.retainAll(this.getCluster().getListTerm());
		for(Term t : tmp)
			t.setCategory(CategoryTerm.RECOMBINATION);
		contents.addAll(tmp);
		
		for(Term t : this.getCluster().getListTerm())
			if(!contents.contains(t))
				contents.add(t);
		
		for(Node child : this.getChildrens()){
			listAntecedentTerm.addAll(this.getCluster().getListTerm());
			child.findContent(this,listAntecedentTerm);
		}
		
	}
	

	public int compareTo(Node n) {
		// TODO Auto-generated method stub
		
		 if (this.getCluster().getYear() > n.getCluster().getYear())
	         return  1;
		 else if (this.getCluster().getYear() < n.getCluster().getYear())
	         return -1;
		return 0;
	}

	public double getSimilarityUnion() {
		return similarityUnion;
	}

	public void setSimilarityUnion(double similarityUnion) {
		this.similarityUnion = similarityUnion;
	}

	public void save() throws IOException {
		// TODO Auto-generated method stub
		
		for(Node n : childrens){
			BufferedWriter bw = null;//the output sample
			bw = new BufferedWriter(new FileWriter(new File("output/edges.csv"), true));//the output sample
			bw.write(getIdentifier()+"	"+n.getIdentifier());
			bw.newLine();
			bw.close();
			n.save();
		}
		
		
	}



}
