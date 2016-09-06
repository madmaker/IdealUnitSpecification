package ru.idealplm.utils.specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import ru.idealplm.utils.specification.Specification.FormField;

public class BLFormat {
	
	public boolean exceedsLimit = false;
	private ArrayList<String> docFormat = new ArrayList<String>();

	static class DocFormatComparator implements Comparator<String>{

		@Override
		public int compare(String s0, String s1) {
			int arg0len, arg1len;

			arg0len = s0.length();
			arg1len = s1.length();
			
			if(Integer.valueOf(s0.charAt(1))==Integer.valueOf(s1.charAt(1))){
				if(arg0len > arg1len) return 1;
				if(arg0len < arg1len) return -1;
				if(Integer.valueOf(s0.charAt(3)) > Integer.valueOf(s1.charAt(3))) return 1;
				if(Integer.valueOf(s0.charAt(3)) < Integer.valueOf(s1.charAt(3))) return -1;
			} else {
				return (Integer.valueOf(s0.charAt(1)) > Integer.valueOf(s1.charAt(1))) ? -1 : 1 ;
			}
			return 0;
		}
		
	}
	
	public BLFormat(String format) {
		if(!format.equals("*)")){
			if(format.length() > SpecificationSettings.columnLengths.get(FormField.FORMAT)-1) exceedsLimit = true;
			for(String f : format.split(",")){
				if(!containsFormat(f.trim())) docFormat.add(f.trim());
			}
		} else {
			docFormat.add(format);
		}
	}
	
	public ArrayList<String> toStringList(){
		return docFormat;
	}
	
	public boolean containsFormat(String format) {
		boolean result = docFormat.contains(format);
		return result;
	}
	
	@Override
	public String toString() {
		Collections.sort(docFormat, new DocFormatComparator());
		Iterator<String> it = docFormat.iterator();
		StringBuilder sb = new StringBuilder();
		if(exceedsLimit && it.hasNext()) sb.append("*) " + it.next() + (it.hasNext()?", ":""));
		while(it.hasNext()){
			sb.append(it.next() + (it.hasNext()?", ":""));
		}
		return sb.toString().trim();
	}
}
