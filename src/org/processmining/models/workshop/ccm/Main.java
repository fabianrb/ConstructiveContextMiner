package org.processmining.models.workshop.ccm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;



public class Main {
	public static void main(String[] args) {
		

		String name = "amisoftPart";

		
		
		String[][] traces = {
				{"a","b1","b2","c","d2","f"},
				{"a","c","b","e"},
				{"a", "b1", "d2", "b2", "c", "f"},
				{"a", "b1", "d", "b2", "c", "e"},
				{"a", "c", "b", "f"},
				{"a", "b", "c", "d", "f", "g"},
				{"a", "b1", "b2", "c", "d2", "e"},
				{"a", "b", "d", "c", "f", "g"},
				{"a","b","d","c","e","g"},				
				{"a", "b", "c", "f","g"},
				{"a", "b", "c","d","e","g"},
				{"a", "d", "c","b","f","g"},
				{"a", "c", "d","b","f","g"},
				{"a", "c", "b","e","g"},
				{"a", "d", "b","c","f","g"},
				{"a", "d", "b","c","e","g"},
				{"a", "c", "b","f","g"}
		};

		
		
				/*String[][] traces = {
					{"Assign Estimated Tasks (a)", "Req. Analysis (b)", "R.E with Use Cases (c)", "Customer Approval (e)", "Negotiate Changes (f)", "Req. Analysis (b)", "R.E with Scenarios (d)", "Customer Approval (e)", "Establish Req. Base Line (g)"},
					{"Assign Estimated Tasks (a)", "Req. Analysis (b)", "R.E with Scenarios (d)", "Customer Approval (e)", "Establish Req. Base Line (g)"},
					{"Assign Estimated Tasks (a)", "Req. Analysis (b)", "R.E with Use Cases (c)", "Customer Approval (e)", "Establish Req. Base Line (g)"}
				};
				*/
		
		int [] frequency = {50,120,60,45,80,38,20,26,12,8,6,4,2,1,1,1,1};
		
		
		//String[][] traces ={{"Initial Meeting","Req.Analysis","R.E. with use cases","Customer Approval","Negotiate Changes","Req.Analysis","R.E. with use cases","Customer Approval","Establish Req. base line"}, {"Initial Meeting", "Req.Analysis","R.E. with scenarios", "Customer Approval","Establish Req. base line"}, 
		//		{"Initial Meeting", "Req.Analysis","R.E. with use cases",  "Customer Approval", "Negotiate Changes","Req.Analysis","R.E. with use cases","Customer Approval","Negotiate Changes","Req.Analysis","R.E. with scenarios","Customer Approval","Establish Req. base line"}};
		
		

		
		
		
		
		
		
		
		int a = 17;
		while(a > 0){
		int num = a;
		try {
			File file = new File("/home/fblum/Escritorio/Logs/Test"+a+".xes");
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +  
					" <!-- This file has been generated with the OpenXES library. It conforms -->\n" +  
					" <!-- to the XML serialization of the XES standard for log storage and -->\n" +  
					" <!-- management. -->\n" +  
					" <!-- XES standard version: 1.0 -->\n" +  
					" <!-- OpenXES library version: 1.0RC7 -->\n" +  
					" <!-- OpenXES is available from http://www.openxes.org/ -->\n" +  
					" <log xes.version=\"1.0\" xes.features=\"nested-attributes\" openxes.version=\"1.0RC7\" xmlns=\"http://www.xes-standard.org/\">\n" +  
					" 	<extension name=\"Lifecycle\" prefix=\"lifecycle\" uri=\"http://www.xes-standard.org/lifecycle.xesext\"/>\n" +  
					" 	<extension name=\"Organizational\" prefix=\"org\" uri=\"http://www.xes-standard.org/org.xesext\"/>\n" +  
					" 	<extension name=\"Time\" prefix=\"time\" uri=\"http://www.xes-standard.org/time.xesext\"/>\n" +  
					" 	<extension name=\"Concept\" prefix=\"concept\" uri=\"http://www.xes-standard.org/concept.xesext\"/>\n" +  
					" 	<extension name=\"Semantic\" prefix=\"semantic\" uri=\"http://www.xes-standard.org/semantic.xesext\"/>\n" +  
					" 	<global scope=\"trace\">\n" +  
					" 		<string key=\"concept:name\" value=\"__INVALID__\"/>\n" +  
					" 	</global>\n" +  
					" 	<global scope=\"event\">\n" +  
					" 		<string key=\"concept:name\" value=\"__INVALID__\"/>\n" +  
					" 		<string key=\"lifecycle:transition\" value=\"complete\"/>\n" +  
					" 	</global>\n" +  
					" 	<classifier name=\"MXML Legacy Classifier\" keys=\"concept:name lifecycle:transition\"/>\n" +  
					" 	<classifier name=\"Event Name\" keys=\"concept:name\"/>\n" +  
					" 	<classifier name=\"Resource\" keys=\"org:resource\"/>\n" +  
					" 	<string key=\"source\" value=\"Rapid Synthesizer\"/>\n" +  
					" 	<string key=\"concept:name\" value=\""+ name +".mxml\"/>\n" +  
					" 	<string key=\"lifecycle:model\" value=\"standard\"/> \n");
			
			int max = traces.length;
			if (num>0){
				max = num;
			}
			
			for(int i=0;i<max;i++){
				for(int x=0;x<frequency[i];x++){
					
				output.write("<trace>");
				output.write("<string key=\"concept:name\" value=\"Case"+(i+1)+"\"/>");
				for(int j=0;j<traces[i].length;j++){
					output.write("<event>");	
					output.write("<string key=\"concept:name\" value=\""+traces[i][j]+"\"/>");
					output.write("</event>");
				}
				output.write("</trace>");
				}
			}




			output.write("</log>");
			output.close();
			a--;

		} catch ( IOException e ) {
			e.printStackTrace();
		}
		}
	}
}