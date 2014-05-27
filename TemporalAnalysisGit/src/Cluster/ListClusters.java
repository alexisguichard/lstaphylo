package Cluster;
import java.util.ArrayList;

/**
 * Cluster list for one period (all cluster for one year)
 * @author alexis
 *
 */
public class ListClusters {
	/**
	 * Period of all cluster
	 */
	private Integer year;
	/**
	 * List of cluster for this period
	 */
	private ArrayList<Cluster> listCluster = new ArrayList<Cluster>();
	
	/**
	 * Constructor one params for precise year
	 * @param year
	 * @see ListClusters#year
	 */
	public ListClusters(Integer year){
		this.setYear(year);
	}

	/**
	 * Getter  for year
	 * @return year Integer
	 * @see ListClusters#year
	 */
	public Integer getYear() {
		return year;
	}
	/**
	 * Setter of year
	 * @param year
	 * @see ListClusters#year
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	/**
	 *  Getter of all cluster for this period
	 * @return listCluster
	 * @see ListClusters#listCluster
	 */
	public ArrayList<Cluster> getListCluster() {
		return listCluster;
	}

	/**
	 * Setter for this list of cluster 
	 * @param listCluster
	 * @see ListClusters#listCluster
	 */
	public void setListCluster(ArrayList<Cluster> listCluster) {
		this.listCluster = listCluster;
	}

}
