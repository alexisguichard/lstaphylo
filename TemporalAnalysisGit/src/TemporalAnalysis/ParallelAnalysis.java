package TemporalAnalysis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import Cluster.Cluster;
import ConstructTree.Tree;

import java.util.Arrays;

import javax.sound.sampled.Line;

public class ParallelAnalysis {
	public ParallelAnalysis(){	}
	
	public ArrayList<JavaPairRDD<Cluster, Tuple2<Cluster, Double>>> startParallelAnalysis(Tree tree, double seuil, Integer year, JavaPairRDD<Integer, Cluster> clusters ){
		ArrayList<JavaPairRDD<Cluster, Tuple2<Cluster, Double>>> test = new ArrayList<JavaPairRDD<Cluster, Tuple2<Cluster, Double>>>();
		
		JavaRDD<Cluster> clusterY2 = clusters.values().filter(t -> t.getYear().equals(year+1)).cache();
		 
		 
		 for( Cluster cluster : clusters.values().filter(t -> t.getYear().equals(year)).cache().collect()){
			 JavaPairRDD<Cluster, Tuple2<Cluster, Double>> simCluster = clusterY2
						.mapToPair(cl ->  new Tuple2<Cluster, Tuple2<Cluster,Double>>(cluster
								,clusterSimilarity(cluster, cl)))
						.filter(c -> c._2()._2 > 0.0 ).reduceByKey((a,b) -> maxSimilarity(a, b));;
			 test.add(simCluster);
			 
			 
		 }
		 
		 ArrayList<JavaPairRDD<Cluster, Tuple2<Cluster, Double>>>  result = new ArrayList<JavaPairRDD<Cluster, Tuple2<Cluster, Double>>> ();
		 
		
		 return result;
		 /*
		   
		  
			 if(result != null && result.size() > 0){
						
						// on recherche la meilleure
						tree.addNode( result.get(0)._1(), result.get(0)._2()._1());
						
						// on ajoute notre nouveau noeud à l'arbre : id titre clusterParent clusterChild
						
					}
					*/
			 
	
	}
	

	private Tuple2<Cluster, Double> maxSimilarity(Tuple2<Cluster, Double> a,
		Tuple2<Cluster, Double> b) {
	// TODO Auto-generated method stub
		if(a._2() < b._2() && b._2() > 0.1)
			return b;
		
	return a;
}
	

	public  double clusterSimilarityTest( Cluster c, Cluster c2){
		double simJacc = JaccardSimilarity.simJaccard(c, c2);
		c2.setSimilarity(simJacc);
		return simJacc;
		
	}
	

	public  Tuple2<Cluster, Double> clusterSimilarity(Cluster c, Cluster c2){
		double simJacc = JaccardSimilarity.simJaccard(c, c2);
		c2.setSimilarity(simJacc);
		return new Tuple2<Cluster, Double>(c2, simJacc);
		
	}

	
}
