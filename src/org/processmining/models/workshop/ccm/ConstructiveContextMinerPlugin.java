package org.processmining.models.workshop.ccm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.tue.astar.AStarException;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XTraceImpl;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.connections.ConnectionManager;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.log.utils.XLogBuilder;
import org.processmining.models.connections.petrinets.EvClassLogPetrinetConnection;
import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.plugins.astar.petrinet.PetrinetReplayerWithILP;
import org.processmining.plugins.connectionfactories.logpetrinet.TransEvClassMapping;
import org.processmining.plugins.petrinet.replayer.algorithms.IPNReplayAlgorithm;
import org.processmining.plugins.petrinet.replayer.algorithms.IPNReplayParameter;
import org.processmining.plugins.petrinet.replayer.algorithms.costbasedcomplete.CostBasedCompleteParam;
import org.processmining.plugins.petrinet.replayer.ui.PNReplayerUI;
import org.processmining.plugins.petrinet.replayresult.PNRepResult;
import org.processmining.plugins.petrinet.replayresult.StepTypes;
import org.processmining.plugins.replayer.replayresult.SyncReplayResult;
import org.processmining.processtree.Block;
import org.processmining.processtree.Node;
import org.processmining.processtree.Originator;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.impl.AbstractBlock;
import org.processmining.processtree.impl.AbstractTask;
import org.processmining.processtree.impl.ProcessTreeImpl;
import org.processmining.ptconversions.plugins.PluginPN;
import org.processmining.ptconversions.pn.ProcessTree2Petrinet.InvalidProcessTreeException;
import org.processmining.ptconversions.pn.ProcessTree2Petrinet.NotYetImplementedException;

public class ConstructiveContextMinerPlugin {
	@Plugin(name = "Constructive Context Miner", parameterLabels = { "Log" }, returnLabels = { "Hello world string"}, returnTypes = { ProcessTree.class }, userAccessible = true, help = "Produces a representation of a Configurable Process Tree as a String")
	@UITopiaVariant(affiliation = "Universidad de Chile", author = "Fabian Rojas Blum", email = "fabian.rojas.blum@gmail.com")
	public ProcessTree helloWorld(UIPluginContext context, XLog log) {
		return doCCM(context, log);
	}

	public ProcessTree doCCM(UIPluginContext context, XLog log) {
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
		
		//create initial sequence tree with initial trace in the ordered log
		ProcessTree tree = new ProcessTreeImpl();
		Node root = new AbstractBlock.Seq("");
		root.setProcessTree(tree);
		tree.addNode(root);
		tree.setRoot(root);

		Iterator it = orderedCount.entrySet().iterator();
		
		if(it.hasNext()){
			Entry<List<String>,Integer> entry = (Entry<List<String>, Integer>) it.next();
			for(String name:entry.getKey()){
				Node childnode = new AbstractTask.Manual(name, new HashSet<Originator>());
				childnode.setProcessTree(tree);
				tree.addNode(childnode);
				tree.addEdge(((Block)root).addChild(childnode));
			}
		}else{
			System.out.println("Log vacio");
			return null;
		}
		
		//iterate the rest of the log
		PluginPN pt2pn = new PluginPN();

		while(it.hasNext()){
			Entry<List<String>,Integer> entry = (Entry<List<String>, Integer>) it.next();
			List<String>trace = entry.getKey();
			Petrinet petri=null;
			try {
				petri = (Petrinet)pt2pn.convert(context, tree)[0];
			} catch (NotYetImplementedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidProcessTreeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//Create log with unique trace
			XLogBuilder builder = XLogBuilder.newInstance().startLog("newlog").addTrace("newtrace");
			for(String event:trace){
				builder.addEvent(event);
			}
			XLog logOneTrace = builder.build();
			
			//replay
			
			EvClassLogPetrinetConnection conn = null;
			try {
				conn = context.getConnectionManager().getFirstConnection(EvClassLogPetrinetConnection.class, context, petri,
						logOneTrace);
			} catch (ConnectionCannotBeObtained e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			/*
			 * TODO ESTO Aún no funciona
			 * TransEvClassMapping mapping = (TransEvClassMapping) conn.getObjectWithRole(EvClassLogPetrinetConnection.TRANS2EVCLASSMAPPING);
			Collection<Transition> transCol = petri.getTransitions();
			Collection<XEventClass> evClassCol;
			XEventClassifier classifier = mapping.getEventClassifier();
			XLogInfo summary = XLogInfoFactory.createLogInfo(log, classifier);
			XEventClasses eventClassesName = summary.getEventClasses();
			evClassCol = new HashSet<XEventClass>(eventClassesName.getClasses());
			evClassCol.add(mapping.getDummyEventClass());
			CostBasedCompleteParam params = new CostBasedCompleteParam(evClassCol, mapping.getDummyEventClass(),transCol);
			PetrinetReplayerWithILP replayer = new PetrinetReplayerWithILP();
			*/
			PNReplayerUI pnReplayerUI = new PNReplayerUI();
			Object[] resultConfiguration = pnReplayerUI.getConfiguration(context, petri, logOneTrace);
			if (resultConfiguration == null) {
				context.getFutureResult(0).cancel(true);
				return null;
			}
			
			//Compute the alignments
			IPNReplayAlgorithm selectedAlg = (IPNReplayAlgorithm) resultConfiguration[PNReplayerUI.ALGORITHM];
			PNRepResult alignments = null;

			
			
			try {
				alignments = selectedAlg.replayLog(context, petri, logOneTrace, (TransEvClassMapping) resultConfiguration[PNReplayerUI.MAPPING], 
						(IPNReplayParameter) resultConfiguration[PNReplayerUI.PARAMETERS]);
			} catch (AStarException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//print aligments with current trace
			System.out.println("Cantidad de alineamientos: " + alignments.size());
			int i=1;
			for (SyncReplayResult rep : alignments) {
				System.out.println("Alineamiento " + i);
				i++;

				Iterator<Object> itTask = rep.getNodeInstance().iterator();
				Iterator<StepTypes> itType = rep.getStepTypes().iterator();
				while(itTask.hasNext()){
					StepTypes type = itType.next();
					itTask.next();
					switch (type){
						case LMGOOD : System.out.println("Sync move"); break; //Transition
						case LMNOGOOD : System.out.println("False sync move"); break;//--
						case L : System.out.println("Log move"); break;//Log Automaton Node
						case MINVI : System.out.println("Invisible step"); break;//Transition
						case MREAL : System.out.println("Model move"); break;//Transition
						case LMREPLACED : System.out.println("Replaced step"); break;//--
						case LMSWAPPED: System.out.println("Swapped step"); break;//--
						default: break;
					}
				}
			}
		}
		
		return tree;
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
