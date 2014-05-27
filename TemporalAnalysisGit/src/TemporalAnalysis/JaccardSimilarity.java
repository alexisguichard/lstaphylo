package TemporalAnalysis;

import java.util.ArrayList;
import java.util.HashSet;

import Cluster.Cluster;
import Cluster.Term;

public class JaccardSimilarity {
	public static double simJaccard(Cluster c1, Cluster c2){
		ArrayList<Cluster> c = new ArrayList<Cluster>();
		c.add(c2);
		return (float)simJaccard(c1,c);
	}
	
	
	public static double simJaccard(Cluster c1, ArrayList<Cluster> c2){
		HashSet<String> h1 = new HashSet<String>();
		HashSet<String> h2 = new HashSet<String>();
		
		for(Term t : c1.getListTerm())
			h1.add(t.getValue());
		for(Cluster c : c2)
			for(Term t : c.getListTerm())
			h2.add(t.getValue());
		/*
		h1.addAll(c1.getListTerm());
		for(Cluster c : c2){
			h2.addAll(c.getListTerm());
		}*/
		
		int sizeh1 = h1.size();
		//Retains all elements in h3 that are contained in h2 ie intersection
		h1.retainAll(h2);
		h2.removeAll(h1);
		
		//Union 
		int union = sizeh1 + h2.size();
		int intersection = h1.size();
		
		return (double)intersection/union;
	}
	
}
