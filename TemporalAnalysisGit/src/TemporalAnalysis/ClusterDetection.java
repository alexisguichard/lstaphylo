package TemporalAnalysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;
import Cluster.Cluster;
import ConstructTree.Tree;
import Dataset.ConstructData;
import DrawTree.Visualization;

public class ClusterDetection {
	//private static HashMap<Integer, ListClusters> clusterListPerPeriod = new HashMap<Integer, ListClusters>();
	private static JavaSparkContext sc = null;
	static ArrayList<JavaPairRDD<Integer, Cluster>> listClusterByYear = new ArrayList<JavaPairRDD<Integer, Cluster>>() ;
	static JavaPairRDD<Integer, Cluster> clusters;
	private static JavaPairRDD<Cluster, Tuple2<Cluster, Double>> simCluster;
	
	public static void main(String[] args) throws IOException {
		SparkConf conf = new SparkConf()
        .setMaster("local[15]")
        .setAppName("Temporal Analysis")
        .set("spark.executor.memory", "1g"); 
		conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
		conf.set("spark.eventLog.enabled", "false");
        sc = new JavaSparkContext(conf);

		Tree tree = new Tree();
		temporalAnalysis(tree, 0.6);
		Visualization.startVisual(tree);
		/*for(Integer year=ConstructData.YEAR_START;year<=ConstructData.YEAR_END;year++){
		String file = "input/Fulltestset.tab."+year.toString()+".clustering";
		ConstructData.extractTerms("input/embryoList1.txt");
		ConstructData.extractClusterOnePeriod2(file, year);
		}*/
	}

	public static void temporalAnalysis(Tree tree, double seuil) throws IOException{
	
		
		// List of termes
		//ConstructData.extractTerms("input/embryoList1.txt");
		JavaRDD<String> file = sc.textFile("input/textClustering.txt").cache();
		JavaRDD<String> lines = file.flatMap(line -> Arrays.asList(line));
		clusters = lines.mapToPair(line -> new Tuple2<Integer, Cluster>(Integer.parseInt(line.split(" ")[0]),new Cluster(line, Integer.parseInt(line.split(" ")[0])))).cache();
		ArrayList<Integer> listYears = new ArrayList<Integer>();
		for(Integer y = ConstructData.YEAR_START; y<ConstructData.YEAR_END; y++){
			listYears.add(y);
		}
		
		JavaRDD<Integer> years = sc.parallelize(listYears);
		
		ArrayList<JavaPairRDD<Cluster, Tuple2<Cluster, Double>>> result = new ArrayList<JavaPairRDD<Cluster, Tuple2<Cluster, Double>>>();
		years.cache().foreach(y -> result.addAll( new ParallelAnalysis().startParallelAnalysis(tree, seuil, y, clusters )));
		
		JavaPairRDD<Cluster, Tuple2<Cluster, Double>> test = null;
		for(JavaPairRDD<Cluster, Tuple2<Cluster, Double>> r : result){
			if(test.equals(null))
				test = r;
			else
				test.union(r).cache();
		}
		//new ParallelAnalysis().startParallelAnalysis(tree, seuil, 1991, clusters );
		//new ParallelAnalysis().startParallelAnalysis(tree, seuil, 1992 , clusters);
		//new ParallelAnalysis().startParallelAnalysis(tree, seuil, 1993 , clusters);
		
		
	}
	
	
	/*

	public static void parallelAnalysis(Tree tree, double seuil, Iterable<Cluster> c1, Iterable<Cluster> c2){
			
			ArrayList<Cluster> list = new ArrayList<Cluster>();
			
			c2.forEach((c) ->  list.add(c));
			JavaRDD<Cluster> test = sc.parallelize(list);
		
			for(Cluster clusterCurrent : c1){
				// T
					JavaRDD<Cluster> clusters = test.map(cl -> clusterSimilarity(clusterCurrent, cl)).cache();
					ArrayList<Cluster> simCluster = new ArrayList<Cluster>();
					
					for(Cluster cluster : c2){
					
						double simJacc = JaccardSimilarity.simJaccard(clusterCurrent, cluster);
	
						if(simJacc > 0.0){
							cluster.setSimilarity(simJacc);
							simCluster.add(cluster);
							
						}
						
					}
					
					// calcul de la similarité jaccard pour tous les clusters de la période effectuée
					// objectif de l'algo : trouver si des unions de cluster sont possible
					
					
					ArrayList<Cluster> goodSim = new ArrayList<Cluster>();
					if(simCluster.size() > 1 ){
						
						
						// Test UNION avec le cluster avec le plus gros score contre tous les autres
						
						Collections.sort(simCluster);
						ArrayList<Cluster> resultSim = new ArrayList<Cluster>();
						resultSim = calculUnionSimilarity(clusterCurrent,simCluster.get(simCluster.size()-1), 
								simCluster, simCluster.get(simCluster.size()-1).getSimilarity(), resultSim);
						if(goodSim.size() == 0 
								|| 
							(resultSim.size() > 0 && JaccardSimilarity.simJaccard(clusterCurrent, goodSim) < JaccardSimilarity.simJaccard(clusterCurrent, resultSim)))
							goodSim = resultSim;
					}
					
					// Pas d'union de cluster, mais une similarité a été trouvé
					if(goodSim.size() == 0 && simCluster.size() > 0){
						// on recherche la meilleure
						Cluster tmpCl = null;
						ArrayList<Cluster> nodes = new ArrayList<Cluster>();
						for(Cluster c : simCluster){
							if(c.getSimilarity() >= seuil){
								if(tmpCl == null || c.getSimilarity()  > tmpCl.getSimilarity()){
									tmpCl = c;
									nodes = new ArrayList<Cluster>();
									nodes.add(c);
								}
								else if(  c.getSimilarity() == tmpCl.getSimilarity())
									nodes.add(c);
							}
						}
						for(Cluster c : nodes)
						{
							clusterCurrent.setSimilarity(c.getSimilarity());
							tree.addNode( clusterCurrent, c);
						}
						// on ajoute notre nouveau noeud à l'arbre : id titre clusterParent clusterChild
						
					}
					// UNION
					else if (goodSim.size() > 0){
						for(Cluster c : goodSim){
							double sim = JaccardSimilarity.simJaccard(clusterCurrent, goodSim);
							if(sim >= seuil){
								clusterCurrent.setSimilarity(sim);
								tree.addNode( clusterCurrent, c);
							}
							
						}
						
					}
						
						
					
					
					
				
			}
			
			
			
		}*/
	
	
	
	public static ArrayList<Cluster> calculUnionSimilarity(Cluster cCurrent, Cluster c, ArrayList<Cluster> listCluster,double seuil, ArrayList<Cluster> resultSim ){
		
		int index = 0;
		while(index < listCluster.size())
		{
			for(int i = index+1 ; i < listCluster.size(); i++)
			{
				ArrayList<Cluster> tmpList = new ArrayList<Cluster>();
				tmpList.add(listCluster.get(index));
				tmpList.add(listCluster.get(i));
				double simJacc = JaccardSimilarity.simJaccard(cCurrent, tmpList);
				
				if(simJacc> seuil && simJacc > JaccardSimilarity.simJaccard(cCurrent, resultSim)){
					resultSim = tmpList;
					seuil = simJacc;
				}
			}
			index++;
		}
			
		
		return resultSim;
	}
	/**
	public static void save(){

		//ConstructData.extractTerms("input/termFilm.txt");
		// listes de cluster pour toutes les périodes
		for(Integer year=ConstructData.YEAR_START;year<=ConstructData.YEAR_END;year++){
			ListClusters lc = new ListClusters(year);
			String filet = "input/test"+year.toString()+".txt";
			ArrayList<Cluster> clusterList = ConstructData.extractClusterOnePeriod(filet, year);
					
			lc.setListCluster(clusterList);
			clusterListPerPeriod.put(year, lc);
			
			
			
		}
		
		
	
	
	**/

}
