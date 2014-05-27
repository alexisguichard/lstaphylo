package ConstructTree;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import Cluster.Term;
import DrawTree.Drawing;
import DrawTree.PanelPhyloTree;

public class Branch extends Drawing{
	
	private ArrayList<Node> roots;
	private HashSet<Node> nodes;
	private String title;
	
	public Branch(){
		roots = new ArrayList<Node>();
		nodes = new HashSet<Node>();
		title = "";
	}
	
	
	public ArrayList<Node> getRoots() {
		return roots;
	}
	public void setRoots(ArrayList<Node> roots) {
		this.roots = roots;
	}
	public HashSet<Node> getNodes() {
		return nodes;
	}
	public void setNodes(HashSet<Node> nodes) {
		this.nodes = nodes;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void findTitle(){
		ArrayList<Term> terms = new ArrayList<Term>();
		for( Node root : nodes){
			terms.addAll(root.getCluster().getListTerm());
		}
		
		findFrequenceAllItemSet(terms);
	}
	
	private void findFrequenceAllItemSet(ArrayList<Term> list){     
		HashSet<Term> uniqueSet = new HashSet<Term>(list);
		HashMap<Term, Integer>  listOcc = new HashMap<Term, Integer>();
		
		for (Term temp : uniqueSet) {
			int tmp = Collections.frequency(list, temp);
			listOcc.put(temp, tmp);
		    
		 }
		// Ajout des entrées de la map à une liste
		 List<Entry<Term, Integer>> entries = new ArrayList<Entry<Term, Integer>>(listOcc.entrySet());
		 
		  // Tri de la liste sur la valeur de l'entrée
		  Collections.sort(entries, new Comparator<Entry<Term, Integer>>() {
		    public int compare(final Entry<Term, Integer> e1, final Entry<Term, Integer> e2) {
		      return e1.getValue().compareTo(e2.getValue());
		    }

			@Override
			public Comparator<Entry<Term, Integer>> reversed() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<Entry<Term, Integer>> thenComparing(
					Comparator<? super Entry<Term, Integer>> other) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U> Comparator<Entry<Term, Integer>> thenComparing(
					Function<? super Entry<Term, Integer>, ? extends U> keyExtractor,
					Comparator<? super U> keyComparator) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <U extends Comparable<? super U>> Comparator<Entry<Term, Integer>> thenComparing(
					Function<? super Entry<Term, Integer>, ? extends U> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<Entry<Term, Integer>> thenComparingInt(
					ToIntFunction<? super Entry<Term, Integer>> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<Entry<Term, Integer>> thenComparingLong(
					ToLongFunction<? super Entry<Term, Integer>> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Comparator<Entry<Term, Integer>> thenComparingDouble(
					ToDoubleFunction<? super Entry<Term, Integer>> keyExtractor) {
				// TODO Auto-generated method stub
				return null;
			}
		

		  });
		setTitle(entries.get(entries.size()-1).getKey().getValue()+" - "+entries.get(entries.size()-2).getKey().getValue());
		
	}
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		g.drawString(getTitle(),getX(),10);
		for(Node n : getRoots()){
			g.setColor(Color.red);
			n.draw(g);
		}
		
	}
	@Override
	public int calculPosition( Drawing d, int minX, int y) {
		// TODO Auto-generated method stub
		findTitle();
		int minWidth = 0;
		
		Collections.sort(roots);
		for( Node root : roots){
			// Calcul de la position
			setHeight(getHeight()+root.getHeight());
			minWidth = root.calculPosition( root, minX, y);
			minX = root.getX()+PanelPhyloTree.WIDTH_CELL+200;
			
			// Indexation des termes
			Set<Term> listAntecedentTerm = new HashSet<Term>();
			root.findContent(root,listAntecedentTerm);
			
		}
		setX(roots.get(0).getX());
		setWidth(minWidth+300);
		
		return getX();
		
	}

	
	public String toString(){
		return getTitle();
	}


	public void save() throws IOException {
		// TODO Auto-generated method stub
		BufferedWriter bw = null;//the output sample
		bw = new BufferedWriter(new FileWriter(new File("output/node.csv"), true));//the output sample
		
		for(Node n : nodes){
			bw.write(n.getIdentifier()+"	"+n.getTitle()+"	"+n.getContents());
			bw.newLine();
		}
		
		bw.close();
		
		for( Node root : roots){
			root.save();
		}
	}

}
