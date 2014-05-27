package ConstructTree;

import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Cluster.Cluster;
import DrawTree.Drawing;
import DrawTree.PanelPhyloTree;

public class Tree extends Drawing {

	private ArrayList<Branch> branchs;

	// Constructors
	public Tree() {
		this.setBranchs(new ArrayList<Branch>());
	}

	// Properties

	public Tree(Branch b) {
		// TODO Auto-generated constructor stub
		this.setBranchs(new ArrayList<Branch>());
		branchs.add(b);
	}

	public ArrayList<Branch> getBranchs() {
		return branchs;
	}

	public void setBranchs(ArrayList<Branch> branchs) {
		this.branchs = branchs;
	}

	// Public interface

	public Node addNode( Cluster parent, Cluster current) {
		Node nodeChild = searchNodeOfCluster(current);
		if(nodeChild == null){
			nodeChild = new Node(current.getId(), current);
		}
		Node nodeParent = searchNodeOfCluster(parent);
		if(nodeParent == null){
			nodeParent = new Node(parent.getId(), parent);
		}
		this.setChild(nodeChild, nodeParent);
		return nodeChild;
	}
	
	// Private
	private void setChild(Node children, Node nodeParent) {
		Branch b = searchBranch(nodeParent);
		if(b ==  null)
			b = searchBranch(children);
		if (b != null) {

			 //b = searchBranch(children);
			Branch b2 = detectBranch( children, b);
			while(b2 != null){
				Branch newB = new Branch();
				newB.setRoots(b.getRoots());
				newB.getRoots().addAll(b2.getRoots());
				newB.setNodes(b.getNodes());
				newB.getNodes().addAll(b2.getNodes());
				b = newB;
				branchs.remove(b2);
				
				b2 = detectBranch( children, b);
			}
			 b2 = detectBranch( nodeParent, b);
			while(b2 != null){
				Branch newB = new Branch();
				newB.setRoots(b.getRoots());
				newB.getRoots().addAll(b2.getRoots());
				newB.setNodes(b.getNodes());
				newB.getNodes().addAll(b2.getNodes());
				b = newB;
				branchs.remove(b2);
				
				b2 = detectBranch( nodeParent, b);
			}
			nodeParent.addChild(children);
			b.getNodes().add(children);
		}
		else // parent pas encore créé donc c'est une racine
		{

			nodeParent.addChild(children);
			Branch branch = new Branch();
			branch.getRoots().add(nodeParent);
			branch.getNodes().add(nodeParent);
			branch.getNodes().add(children);
			branchs.add(branch);
		}
	}



	private Branch searchBranch(Node nodeParent) {
		for(Branch b : branchs){
			if(b.getNodes().contains(nodeParent))
				return b;
		}
		return null;
	}

	private Node searchNodeOfCluster(Cluster c){
		for(Branch b : branchs){
			for(Node n : b.getNodes())
				if(n.getCluster().equals(c))
					return n;
		}
		return null;
	}


	private Branch detectBranch( Node parent, Branch b2) {
		// TODO Auto-generated method stub
		for(Branch b : branchs){
			if(b.getNodes().contains(parent
					)&& !b.equals(b2))
			{
				
				return b;
			}
		}
		return null;
	}


	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		for(Branch b : branchs){
			b.draw(g);
		}

	}

	@Override
	public int calculPosition( Drawing d, int minX, int y) {
		// TODO Auto-generated method stub
		
		for(Branch b : branchs){
			setHeight(getHeight()+b.getHeight());
			b.calculPosition(this, minX, y);
			minX = b.getWidth();
		}
		setX(minX);
		setWidth(minX+PanelPhyloTree.WIDTH_CELL+200);
		
		return getX();

	}

	public void save() throws IOException {
		// TODO Auto-generated method stub
		BufferedWriter bw = null;//the output sample
		bw = new BufferedWriter(new FileWriter(new File("output/edges.csv")));
		bw.write("source	target");
		bw.newLine();
		bw.close();
		bw = new BufferedWriter(new FileWriter(new File("output/node.csv")));
		bw.write("id	title	contents");
		bw.newLine();
		bw.close();
		for(Branch b : branchs){
			b.save();
		}
	}



}
