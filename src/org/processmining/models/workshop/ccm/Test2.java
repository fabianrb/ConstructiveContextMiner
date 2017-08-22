package org.processmining.models.workshop.ccm;

import java.util.HashSet;
import java.util.Iterator;

import nl.tue.astar.AStarException;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XEvent;
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
		parameterLabels = {}, 
		returnLabels = { "TestModel" }, 
		returnTypes = {ProcessTree.class}, 
		userAccessible = true, 
		help = "This is a new test"
	)
public class Test2 {
	@UITopiaVariant(
		affiliation = "Universidad de Chile", 
		author = "Fabian Rojas Blum", 
		email = "fabian.rojas.blum@gmail.com"
	)
	@PluginVariant(variantLabel = "prueba", requiredParameterLabels = {})
	public ProcessTree helloWorld(PluginContext context) {
		
		ProcessTree tree = new ProcessTreeUpdatable();
		Block or1 = new AbstractBlock.Or("");
		tree.addNode(or1);
		tree.setRoot(or1);
		
		Node n765 = new AbstractTask.Manual("765+complete");
		tree.addNode(n765);
		tree.addEdge(or1.addChild(n765));
		
		Block or2 = new AbstractBlock.Or("");
		tree.addNode(or2);
		tree.addEdge(or1.addChild(or2));
		
		Node n770 = new AbstractTask.Manual("770+complete");
		tree.addNode(n770);
		tree.addEdge(or2.addChild(n770));
		
		Block or3 = new AbstractBlock.Or("");
		tree.addNode(or3);
		tree.addEdge(or2.addChild(or3));
		
		Node n540 = new AbstractTask.Manual("540+complete");
		tree.addNode(n540);
		tree.addEdge(or3.addChild(n540));
		
		Block seq1 = new AbstractBlock.Seq("");
		tree.addNode(seq1);
		tree.addEdge(or3.addChild(seq1));
		
		Node n630 = new AbstractTask.Manual("630+complete");
		tree.addNode(n630);
		tree.addEdge(seq1.addChild(n630));
		
		Block xor1 = new AbstractBlock.Xor("");
		tree.addNode(xor1);
		tree.addEdge(seq1.addChild(xor1));
		
		Node theta1 = new AbstractTask.Automatic("");
		tree.addNode(theta1);
		tree.addEdge(xor1.addChild(theta1));
		
		Node n730 = new AbstractTask.Manual("730+complete");
		tree.addNode(n730);
		tree.addEdge(xor1.addChild(n730));
		
		
				
		return tree;

	}
}
