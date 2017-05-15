package org.processmining.models.workshop.ccm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;

public class ConstructiveContextMinerPlugin {
	@Plugin(name = "Constructive Context Miner", parameterLabels = { "Log" }, returnLabels = { "Hello world string" }, returnTypes = { String.class }, userAccessible = true, help = "Produces a representation of a Configurable Process Tree as a String")
	@UITopiaVariant(affiliation = "Universidad de Chile", author = "Fabian Rojas Blum", email = "fabian.rojas.blum@gmail.com")
	public String helloWorld(UIPluginContext context, XLog log) {
		return doCCM(context, log);
	}

	public String doCCM(UIPluginContext context, XLog log) {
		// ProcessTree2Petrinet.convert(tree);
		
		
		//check trace ocurrence
		HashMap<List<String>, Integer> count = new HashMap<List<String>, Integer>();

		for (XTrace trace : log) {
			List<String> comingtrace = new ArrayList<String>();
			for (XEvent event : trace) {
				comingtrace.add(event.getAttributes().get("concept:name")
						.toString());
			}

			if (count.keySet().contains(comingtrace)) {
				count.put(comingtrace, count.get(comingtrace) + 1);
			} else {
				count.put(comingtrace, 1);
			}

		}
		//sort traces by ocurrence
		Map<List<String>, Integer> orderedCount = sortByValue(count);
		
		
		//print the sorted log at console
		for (Map.Entry<List<String>, Integer> entry : orderedCount.entrySet()) {
			System.out.println(entry.getKey().toString() + entry.getValue());
		}
		
		
		return "Hola mundo";
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

}
