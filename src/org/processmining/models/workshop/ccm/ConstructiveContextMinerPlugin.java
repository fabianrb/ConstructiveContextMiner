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
import org.deckfour.xes.info.impl.XLogInfoImpl;
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
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.log.utils.XLogBuilder;
import org.processmining.models.connections.petrinets.EvClassLogPetrinetConnection;
import org.processmining.models.connections.petrinets.behavioral.FinalMarkingConnection;
import org.processmining.models.connections.petrinets.behavioral.InitialMarkingConnection;
import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.models.workshop.sjjleemans.ProcessTree.model.EventClass;
import org.processmining.plugins.astar.petrinet.PetrinetReplayerWithILP;
import org.processmining.plugins.connectionfactories.logpetrinet.TransEvClassMapping;
import org.processmining.plugins.petrinet.replayer.PNLogReplayer;
import org.processmining.plugins.petrinet.replayer.algorithms.IPNReplayAlgorithm;
import org.processmining.plugins.petrinet.replayer.algorithms.IPNReplayParameter;
import org.processmining.plugins.petrinet.replayer.algorithms.costbasedcomplete.CostBasedCompleteParam;
import org.processmining.plugins.petrinet.replayer.ui.PNReplayerUI;
import org.processmining.plugins.petrinet.replayer.util.LogAutomatonNode;
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
	public ProcessTree helloWorld(PluginContext context, XLog log) {
		return doCCM(context, log);
	}

	//Creates an initial tree as a sequence with the specified list of events 
	private ProcessTree createInitialSequentialTree(List<XEventClass>events){
		ProcessTree tree = new ProcessTreeImpl();
		Node root = new AbstractBlock.Seq("");
		root.setProcessTree(tree);
		tree.addNode(root);
		tree.setRoot(root);
		for (XEventClass e:events){
			Node childnode = new AbstractTask.Manual(e.getId());
			childnode.setProcessTree(tree);
			tree.addNode(childnode);
			tree.addEdge(((Block)root).addChild(childnode));
		}
		return tree;
	}
	public ProcessTree doCCM(PluginContext context, XLog log) {
		XEventClassifier usedClassifier = XLogInfoImpl.STANDARD_CLASSIFIER;
		//check trace ocurrence
		HashMap<List<XEventClass>, Integer> count = new HashMap<List<XEventClass>, Integer>();
		XLogInfo info = XLogInfoFactory.createLogInfo(log, usedClassifier);
		for (XTrace trace : log) {
			List<XEventClass> comingtrace = new ArrayList<XEventClass>();
			for (XEvent event : trace) {
				comingtrace.add(info.getEventClasses().getClassOf(event));
			}
			if (count.containsKey(comingtrace)) {
				count.put(comingtrace, count.get(comingtrace) + 1);
			} else {
				count.put(comingtrace, 1);
			}
			
		}
		//sort traces by ocurrence
		Map<List<XEventClass>, Integer> orderedCount = sortByValue(count);
		
		
		//print the sorted log at console
		for (Map.Entry<List<XEventClass>, Integer> entry : orderedCount.entrySet()) {
			System.out.println(entry.getKey().toString() + entry.getValue());
		}
		
		//create initial sequence tree with initial trace in the ordered log
		Iterator<List<XEventClass>> iterator = orderedCount.keySet().iterator();
		ProcessTree tree = null;
		if(iterator.hasNext()){
			tree = createInitialSequentialTree(iterator.next());
		}
		else{
			System.out.println("Log vacio");
			return null;
		}
		
		//needed to convert ProcessTree to PetriNet
		PluginPN pt2pn = new PluginPN();
		
		//iterate with the rest through the rest of the log
		while(iterator.hasNext()){
			//convertir arbol actual
			Petrinet petri = null;
			try {
				petri = (Petrinet)pt2pn.convert(context, tree)[0];
			} catch (NotYetImplementedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (InvalidProcessTreeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			nextIteration(context, tree, petri, iterator.next());
		}
		return tree;

	}

	private void nextIteration(PluginContext context, ProcessTree tree, Petrinet petri,
			List<XEventClass> trace) {
		//create unique trace log
		XLogBuilder builder = XLogBuilder.newInstance().startLog("newlog").addTrace("newtrace");
		for(XEventClass event:trace){
			//TODO ver como deshardcodear esto
			builder.addEvent(event.getId().substring(0, event.getId().length()-1));
		}
		XLog logOneTrace = builder.build();
		
		//Check alignments between single log and current model  
		SyncReplayResult rep = replayLog(context,logOneTrace,petri);
		Iterator<Object> itTask = rep.getNodeInstance().iterator();
		Iterator<StepTypes> itType = rep.getStepTypes().iterator();
		while(itTask.hasNext()){
			StepTypes type = itType.next();
			Object a = itTask.next();
			switch (type){
				case LMGOOD : System.out.print("Sync move --> "); Transition t = (Transition)a; System.out.println(t.getLabel()); break; //Transition					case LMNOGOOD : System.out.println("False sync move"); break;//--
				case L : System.out.print("Log move --> "); XEventClass tl = (XEventClass) a; System.out.println(tl.getId()); break;//Log Automaton Node
				case MINVI : System.out.println("Invisible step"); break;//Transition
				case MREAL : System.out.print("Model move --> "); Transition tm = (Transition)a; System.out.println(tm.getLabel()); break;//Transition
				case LMREPLACED : System.out.println("Replaced step"); break;//--
				case LMSWAPPED: System.out.println("Swapped step"); break;//--
				default: break;
			}
		}
		
		
		
	}

	//Replay single
	private SyncReplayResult replayLog(PluginContext context, XLog logOneTrace,
			Petrinet petri) {
		//create mapping between single log and petrinet
		TransEvClassMapping maptest = org.processmining.plugins.compliance.align.PNLogReplayer.getEventClassMapping(context, petri, logOneTrace, XLogInfoImpl.STANDARD_CLASSIFIER);
		
		//trace(log) alignment replayer
		PNLogReplayer replayer = new PNLogReplayer();
		
		//parameter build
		XLogInfo logInfo = XLogInfoFactory.createLogInfo(logOneTrace);
		CostBasedCompleteParam parameter = new CostBasedCompleteParam(logInfo.getEventClasses().getClasses(),
				maptest.getDummyEventClass(), petri.getTransitions(), 2, 5);
		parameter.getMapEvClass2Cost().remove(maptest.getDummyEventClass());
		parameter.getMapEvClass2Cost().put(maptest.getDummyEventClass(), 1);
		parameter.setGUIMode(false);
		parameter.setCreateConn(false);
		parameter.setMaxNumOfStates(200000);
		
		//getting petrinet initial and final marking 
		Marking iniMark=null;
		Marking finalMark =  null;
		try {
			iniMark = context.getConnectionManager()
					.getFirstConnection(InitialMarkingConnection.class, context, petri)
					.getObjectWithRole(InitialMarkingConnection.MARKING);
			finalMark = context.getConnectionManager()
					.getFirstConnection(FinalMarkingConnection.class, context, petri)
					.getObjectWithRole(FinalMarkingConnection.MARKING);
		} catch (ConnectionCannotBeObtained e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		parameter.setInitialMarking(iniMark);
		parameter.setFinalMarkings(new Marking[] {finalMark});

		PetrinetReplayerWithILP replWithoutILP = new PetrinetReplayerWithILP();
		PNRepResult alignments = null;
		try {
			alignments = replayer.replayLog(context, petri, logOneTrace, maptest, replWithoutILP, parameter);
		} catch (AStarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return alignments.iterator().next();
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
