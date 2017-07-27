package org.processmining.models.workshop.ccm;

import java.util.HashSet;
import java.util.Iterator;

import nl.tue.astar.AStarException;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.log.utils.XLogBuilder;
import org.processmining.models.connections.petrinets.EvClassLogPetrinetConnection;
import org.processmining.models.connections.petrinets.PNRepResultAllRequiredParamConnection;
import org.processmining.models.connections.petrinets.behavioral.FinalMarkingConnection;
import org.processmining.models.connections.petrinets.behavioral.InitialMarkingConnection;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.astar.petrinet.PetrinetReplayerWithILP;
import org.processmining.plugins.astar.petrinet.PetrinetReplayerWithoutILP;
import org.processmining.plugins.connectionfactories.logpetrinet.EvClassLogPetrinetConnectionFactoryUI;
import org.processmining.plugins.connectionfactories.logpetrinet.TransEvClassMapping;
import org.processmining.plugins.petrinet.replayer.PNLogReplayer;
import org.processmining.plugins.petrinet.replayer.algorithms.IPNReplayAlgorithm;
import org.processmining.plugins.petrinet.replayer.algorithms.IPNReplayParameter;
import org.processmining.plugins.petrinet.replayer.algorithms.costbasedcomplete.CostBasedCompleteParam;
import org.processmining.plugins.petrinet.replayer.ui.PNReplayerUI;
import org.processmining.plugins.petrinet.replayresult.PNRepResult;
import org.processmining.plugins.petrinet.replayresult.StepTypes;
import org.processmining.plugins.replayer.replayresult.SyncReplayResult;
import org.processmining.processtree.Originator;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.impl.AbstractBlock;
import org.processmining.processtree.impl.AbstractTask;
import org.processmining.processtree.impl.ProcessTreeImpl;
import org.processmining.processtree.Node;
import org.processmining.processtree.Block;
import org.processmining.ptconversions.plugins.PluginPN;
import org.processmining.ptconversions.pn.ProcessTree2Petrinet.InvalidProcessTreeException;
import org.processmining.ptconversions.pn.ProcessTree2Petrinet.NotYetImplementedException;

@Plugin(
		name = "Test2", 
		parameterLabels = {"Log"}, 
		returnLabels = { "TestModel" }, 
		returnTypes = {TestModel.class}, 
		userAccessible = true, 
		help = "This is a new test"
	)
public class Test2 {
	@UITopiaVariant(
		affiliation = "Universidad de Chile", 
		author = "Fabian Rojas Blum", 
		email = "fabian.rojas.blum@gmail.com"
	)
	@PluginVariant(variantLabel = "Mine a Workshop Model, default", requiredParameterLabels = { 0})
	public TestModel helloWorld(PluginContext context, XLog log) {
		/*try {
			PNRepResult repResult = context.tryToFindOrConstructFirstObject(PNRepResult.class,
					PNRepResultAllRequiredParamConnection.class, PNRepResultAllRequiredParamConnection.PNREPRESULT, net,
					log);
			System.out.println("hecho");
		} catch (ConnectionCannotBeObtained e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("no hecho");

		}*/
		ProcessTree tree = new ProcessTreeImpl();
		Node n = new AbstractBlock.Seq("");
		n.setProcessTree(tree);
		tree.addNode(n);
		tree.setRoot(n);
		Node n2 = new AbstractTask.Manual("a");
		n2.setProcessTree(tree);
		tree.addNode(n2);
		tree.addEdge(((Block)n).addChild(n2));
		n2 = new AbstractTask.Manual("b");
		n2.setProcessTree(tree);
		tree.addNode(n2);
		tree.addEdge(((Block)n).addChild(n2));
		n2 = new AbstractTask.Manual("c");
		n2.setProcessTree(tree);
		tree.addNode(n2);
		tree.addEdge(((Block)n).addChild(n2));
		Petrinet net = null;
		PluginPN pt2pn = new PluginPN();
		try {
			net = (Petrinet)pt2pn.convert(context, tree)[0];
		} catch (NotYetImplementedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (InvalidProcessTreeException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		
		//probando una segunda net 
		Petrinet net2 = null;
		PluginPN pt2pn2 = new PluginPN();
		try {
			net2 = (Petrinet)pt2pn2.convert(context, tree)[0];
		} catch (NotYetImplementedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (InvalidProcessTreeException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		TransEvClassMapping maptest = org.processmining.plugins.compliance.align.PNLogReplayer.getEventClassMapping(context, net, log, XLogInfoImpl.NAME_CLASSIFIER);

		
		
		TransEvClassMapping map1 = null;
		try {
			map1 = context.tryToFindOrConstructFirstObject(TransEvClassMapping.class, EvClassLogPetrinetConnection.class, EvClassLogPetrinetConnection.TRANS2EVCLASSMAPPING, net, log);
			System.out.println("mapping");

		} catch (ConnectionCannotBeObtained e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("nomapping");

		}
		
		PNLogReplayer replayer = new PNLogReplayer();
		
		//parameter build
		XLogInfo logInfo = XLogInfoFactory.createLogInfo(log);
		CostBasedCompleteParam parameter = new CostBasedCompleteParam(logInfo.getEventClasses().getClasses(),
				map1.getDummyEventClass(), net.getTransitions(), 2, 5);
		parameter.getMapEvClass2Cost().remove(map1.getDummyEventClass());
		parameter.getMapEvClass2Cost().put(map1.getDummyEventClass(), 1);
		
		
		Marking iniMark=null;
		try {
			iniMark = context.getConnectionManager()
					.getFirstConnection(InitialMarkingConnection.class, context, net)
					.getObjectWithRole(InitialMarkingConnection.MARKING);
		} catch (ConnectionCannotBeObtained e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Marking finalMark =  null;
		try {
			finalMark = context.getConnectionManager()
					.getFirstConnection(FinalMarkingConnection.class, context, net)
					.getObjectWithRole(FinalMarkingConnection.MARKING);
		} catch (ConnectionCannotBeObtained e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		parameter.setGUIMode(false);
		parameter.setCreateConn(false);
		parameter.setInitialMarking(iniMark);
		parameter.setFinalMarkings(new Marking[] {finalMark});
		parameter.setMaxNumOfStates(200000);

		
		
		PetrinetReplayerWithILP replWithoutILP = new PetrinetReplayerWithILP();
		PNRepResult alignments = null;
		try {
			alignments = replayer.replayLog(context, net, log, maptest, replWithoutILP, parameter);
			System.out.println("lo hizo");
		} catch (AStarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("no lo hizo");

		}
		
		System.out.println("Cantidad de alineamientos: " + alignments.size());
		int i=1;
		for (SyncReplayResult rep : alignments) {
			System.out.println("Alineamiento " + i);
			i++;

			Iterator<Object> itTask = rep.getNodeInstance().iterator();
			Iterator<StepTypes> itType = rep.getStepTypes().iterator();
			while(itTask.hasNext()){
				StepTypes type = itType.next();
				Object a = itTask.next();
				switch (type){
					case LMGOOD : System.out.print("Sync move --> "); Transition t = (Transition)a; System.out.println(t.getLabel()); break; //Transition
					case LMNOGOOD : System.out.println("False sync move"); break;//--
					case L : System.out.print("Log move --> "); XEventClass tl = (XEventClass) a; System.out.println(tl.getId()); break;//Log Automaton Node
					case MINVI : System.out.println("Invisible step"); break;//Transition
					case MREAL : System.out.print("Model move --> "); Transition tm = (Transition)a; System.out.println(tm.getLabel()); break;//Transition
					case LMREPLACED : System.out.println("Replaced step"); break;//--
					case LMSWAPPED: System.out.println("Swapped step"); break;//--
					default: break;
				}
			}
		}
		
/*
		EvClassLogPetrinetConnection conn = null;
		try {
			conn = context.getConnectionManager().getFirstConnection(EvClassLogPetrinetConnection.class, context, net,
					log);
			System.out.println("funciono");

		} catch (ConnectionCannotBeObtained e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("no funciono");

		}*/
		return new TestModel();
	}
}
