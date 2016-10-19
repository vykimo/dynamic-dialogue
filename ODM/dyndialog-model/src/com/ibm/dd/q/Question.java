package com.ibm.dd.q;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


@XmlType(name="Question")
@XmlAccessorType(XmlAccessType.FIELD)
public class Question implements Comparable{
	// unique id 
	private String label;
	/**
	 * Type of question could be y/n, or rank based (unique selection of multiple choice,
	 * , date, decimal, integer, short answer or long answer.
	 */
	private String type;
	private List<Option> options = new ArrayList<Option>();
	
	public Question(){}
	

	public String toString(){
		String s=getLabel();
	   return s;	
	}
	
	/**
	 * The options instead of being provided as a list of option
	 * they are given as string in different possible formats:
	 * [{name:Yes,selected:false},{name:No,selected:true}]
	 * [{name:Yes},{name:No}]
	 * [Yes,No]
	 * null 
	 * ""
	 * 
	 * @param options
	 */
	public  void  parseOptions(String options){
		if (options == null) return;
		if (options.length()<=2) return;
		List<Option> l = new ArrayList<Option>();	
		if (options.contains("name:")) {
			for (int j=0;j<options.length();j++) {
				if (options.charAt(j)== 'n' && options.charAt(j+1)== 'a' && options.charAt(j+2)== 'm' && options.charAt(j+4)== ':') {
					Option opt=processOption(options,j);
				     l.add(opt);
				}
			}	
		} else{
			String[] newOptions=options.replace('[',' ').replace(']', ' ').trim().split(",");
			for (String o: newOptions){
				Option opt=new Option(o);
				l.add(opt);
			}
		}
		this.setOptions(l);
	}
	
	public void parseOptionsAsCSV(String options) {
		String[] ops = options.split(",");
		List<Option> l = new ArrayList<Option>();	
		for (String o: ops){
			Option opt=new Option(o);
			l.add(opt);
		}
		this.setOptions(l);
	}

	public void addOptionToQuestion(String opt){
		Option option=new Option(opt);
		this.getOptions().add(option);
	}
	
	private static Option processOption(String options, int j) {
		Option opt=new Option();
		int i=j+5,k=i;
		boolean found = false;
		while (! found && k<options.length()) {
			if (options.charAt(k) == ',' || options.charAt(k) == '}') found =true;
			else k++;
		}
		if (found) {
		   opt.setName(options.substring(i, k).trim());
		}
		// search for boolean value, k should be on , from ,selected:false
		found = false;
		while (! found && k<options.length()) {
			if (options.charAt(k) == ':') found =true;
			else k++;
		} 
		if (found) {
			if(options.charAt(k+1) == 't') {
				opt.setSelected(true);
			} else {
				opt.setSelected(false);
			}
		} else {
			opt.setSelected(false);
		}
	return opt;
    }
	

	// ---------------------------------------------------------------------------------
	// ------------- Getters --- setters 
	// ---------------------------------------------------------------------------------

	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public List<Option> getOptions() {
		if (options == null) options= new ArrayList<Option>();
		return options;
	}


	public void setOptions(List<Option> options) {
		this.options = options;
	}


	@Override
	public int compareTo(Object o) {
		if (o instanceof Question) {
			Question other =(Question)o;
			if (this.label != null && other.getLabel() !=null)
				return (this.label.compareTo(other.getLabel()));
			else return -1;
		}
		return 1;
	}

  public boolean equals(Object o) {
	  if (o instanceof Question) {
		  Question other =(Question)o;
			return (this.label == other.getLabel()); 
	  }
	  return false;
  }

}
