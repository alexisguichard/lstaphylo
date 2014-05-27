package Dataset;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Cluster.Cluster;
import Cluster.Term;

public class ConstructData {
	
	public static final int YEAR_START = 1991;
	public static final int YEAR_END = 1994;
	private static ArrayList<Term> termListInit = new ArrayList<Term>();
	private static ArrayList<Cluster> clusterListInit = new ArrayList<Cluster>();
	/**
	 * @param args
	 * @return 
	 * @throws IOException 
	 */
	public static ArrayList<Term> extractTerms(String fileTerms) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new FileReader(new File(fileTerms)));
		String line;
		int i = 1;
		while ((line = br.readLine()) != null) {
			String[] fields=line.split("\\s+");
			String plop = "";
			for(String s : fields){
				plop += s;
				
				
			}
		  termListInit.add(new Term(i,plop));
		  i++;
		}
		br.close();
		
		return termListInit;
		
	}
	

	public static ArrayList<Cluster> extractClusterOnePeriod2(String fileClustering, Integer year) throws IOException {
		
			BufferedReader br2 = new BufferedReader(new FileReader(new File(fileClustering)));
			String line2;
			br2.readLine();
			BufferedWriter bw = null;//the output sample
			bw = new BufferedWriter(new FileWriter(new File("input/finder"+year.toString())+".txt"));//the output sample
			while ((line2 = br2.readLine()) != null) {
				String[] fields=line2.split("\\s+");
				
				String[] str1= searchTerm(Integer.parseInt(fields[0]),termListInit).getValue().split("\\s+");
				String plop = "";
				for(String s : str1){
					plop += s;
					
					
				}
				
				bw.write(plop+" "+fields[1]);
				bw.newLine();
			
			}
			
			br2.close();	
			bw.close();
			return clusterListInit;
		}
	
	public static void constructFilesClustering(String[] listFiles) throws IOException {
		
		BufferedWriter bw = null;//the output sample
		bw = new BufferedWriter(new FileWriter(new File("input/textClustering.txt")));//the output sample
		Integer year = ConstructData.YEAR_START;
		for(String file : listFiles)
		{
			BufferedReader br2 = new BufferedReader(new FileReader(new File(file)));
			String line;
			while (( line = br2.readLine()) != null) {
				bw.write(year+" "+line);
				bw.newLine();
			}
			br2.close();
			year++;
		}
	
		bw.close();
	}
	
	 
	public static ArrayList<Cluster> extractClusterOnePeriod(String fileClustering, Integer year) throws IOException {
		
			clusterListInit = new ArrayList<Cluster>();
			BufferedReader br2 = new BufferedReader(new FileReader(new File(fileClustering)));
			String line2;
			while ((line2 = br2.readLine()) != null) {
				String[] fields=line2.split("\\s+");
				Cluster tmpCluster = new Cluster(Integer.parseInt(year+fields[0]), year);
				
				for(String s : fields){
					if(!s.equals(fields[0]))
					{
						Term t = searchTermByString(s,termListInit);
						tmpCluster.getListTerm().add(t);
					}
					
				}
						
				clusterListInit.add(tmpCluster);
					
			
			}
			
			br2.close();
			return clusterListInit;
		}
	
	public static Term searchTerm(Integer id, ArrayList<Term> terms){
		for(Term t : terms){
			
			if(t != null && t.getId().equals(id))
				return t;
		}
		return null;
	}
	
	public static Term searchTermByString(String s, ArrayList<Term> terms){
		for(Term t : terms){
			
			if(t != null && t.getValue().equals(s))
				return t;
		}
		return null;
	}
	
	public static Cluster searchCluster(Integer id, ArrayList<Cluster> cl){
		for(Cluster c : cl){
			if(c.getId().equals(id))
				return c;
		}
		return null;
	}

}
