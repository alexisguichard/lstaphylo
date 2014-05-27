package Cluster;

import java.io.Serializable;

/**
 * This class represent term present in some corpus 
 * @author alexis
 *
 */
public class Term implements Comparable<Term>, Serializable{
	/**
	 * Id of term
	 */
	private Integer id;
	/**
	 * value of term : String
	 */
	private String value;
	/**
	 * Category of term : inherited, recombination, emergent
	 * in the special situation ( in specific cluster)
	 */
	private CategoryTerm category;
	
	/**
	 * Constructor of term with id and string value
	 * @param id
	 * @param value
	 */
	public Term(int id, String value){
		this.setId(id); this.setValue(value);this.category = CategoryTerm.EMERGENT;
	}

	public Term(String s) {
		setId(0);
		setValue(s);
	}

	/**
	 * Getter of id term
	 * @return Integer
	 * @see Term#id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Setter id
	 * @param id
	 * @see Term#id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Getter of string value of term
	 * @return String
	 * @see Term#value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Setter of string value
	 * @param value
	 * @see Term#value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Getter of category of this term 
	 * @return CategoryTerm enum of different category
	 */
	public CategoryTerm getCategory() {
		return category;
	}

	/**
	 * Setter of category
	 * @param category
	 * @see Term#category
	 */
	public void setCategory(CategoryTerm category) {
		this.category = category;
	}
	
	/**
	 * method toString for print this term
	 */
	public String toString(){
		return value;
	}

	/**
	 * Compare two terms
	 */
	public int compareTo(Term o) {
		// TODO Auto-generated method stub
		

		 if (this.getCategory().equals(CategoryTerm.EMERGENT))
	         return  1;
		 else if (o.getCategory().equals(CategoryTerm.EMERGENT))
	         return -1;
		 else if(this.getCategory().equals(CategoryTerm.RECOMBINATION))
			 return 1;
		 else if(o.getCategory().equals(CategoryTerm.RECOMBINATION))
			 return -1;
		return 0;
	}

}
