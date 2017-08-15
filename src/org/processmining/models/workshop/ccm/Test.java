package org.processmining.models.workshop.ccm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
	public static void main (String[]a){
		String [] a1 = {"a","b"};
		String [] a2 = {"a","b"};
		System.out.println(Arrays.equals(a1, a2));
		List<String> b1 = new ArrayList<>();
		b1.add("a");
		b1.add("b");
		List<String> b2 = new ArrayList<>();
		b2.add("a");
		b2.add("b");
		System.out.println(b1.equals(b2));
		
	}
}
