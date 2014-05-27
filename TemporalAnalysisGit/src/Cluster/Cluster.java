package Cluster;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import TemporalAnalysis.JaccardSimilarity;
import TemporalAnalysis.ParallelAnalysis;
import scala.Tuple2;


/**
 * Cluster represent list of term in commun in somebody documents in a precise year
 * 
 * @author alexis
 *
 */
public class Cluster implements Comparable<Cluster>, Serializable {
	
	/**
	 * Cluster ID
	 * 
	 */
	private Integer id;
	/**
	 * Cluster List of Term
	 */
	private Set<Term> listTerm = new HashSet<Term>();
	/**
	 * Cluster year
	 */
	private Integer year;
	/**
	 * Similarity of cluster with parent
	 */
	private Double similarity;
	
	/**
	 * Constructor with 2 params 
	 * @param id
	 * @param year2
	 */
	public Cluster(int id, Integer year2){
		this.setId(id); setYear(year2); 
	}

	public Cluster() { setSimilarity(0.0);
	}

	public Cluster(String line, Integer year) {
		// TODO Auto-generated constructor stub
		String[] words = line.split(" ");
		setId( Integer.parseInt(words[1]));
		for(int i=2; i< words.length;i++){
			listTerm.add(new Term(words[i]));
		}
		setSimilarity(0.0);
		setYear(year);
	}

	/**
	 * ID getter
	 * @return id
	 * @see Cluster#id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * ID setter
	 * @param id
	 * @see Cluster#id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Getter of ListTerm
	 * @return listTerm
	 * @see Cluster#listTerm
	 */
	public Set<Term> getListTerm() {
		return listTerm;
	}

	/**
	 * Setter listTerm
	 * @param listTerm
	 * @see Cluster#listTerm
	 */
	public void setListTerm(HashSet<Term> listTerm) {
		this.listTerm = listTerm;
	}

	/**
	 * Getter of year
	 * @return year
	 * @see Cluster#year
	 */
	public Integer getYear() {
		return year;
	}

	/**
	 * setter of year
	 * @param year
	 * @see Cluster#year
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 * Sette of similarity to cluster
	 * @param double1
	 * @see Cluster#similarity
	 */
	public void setSimilarity(Double double1) {
		// TODO Auto-generated method stub
		this.similarity = (double) Math.round(double1*100)/100;
	}
	
	/** 
	 * Getter of similarity
	 * @return similarity
	 * @see Cluster#similarity
	 */
	public double getSimilarity(){
		return similarity;
	}

	/**
	 * Compare two clusters
	 */
	public int compareTo(Cluster o) {

		 if (this.getSimilarity() > o.getSimilarity())
	         return  1;
		 else if (this.getSimilarity() < o.getSimilarity())
	         return -1;
		return 0;
	}

}
