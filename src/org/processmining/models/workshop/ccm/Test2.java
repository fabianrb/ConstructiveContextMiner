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
		
		ProcessTree tree = new ProcessTreeImpl();
		Node n = new AbstractBlock.Seq("");
		n.setProcessTree(tree);
		tree.addNode(n);
		tree.setRoot(n);
		Node n1 = new AbstractTask.Manual("a+");
		n1.setProcessTree(tree);
		tree.addNode(n1);
		tree.addEdge(((Block)n).addChild(n1));
		Node n2 = new AbstractTask.Manual("b+");
		n2.setProcessTree(tree);
		tree.addNode(n2);
		tree.addEdge(((Block)n).addChild(n2));
		Node n3 = new AbstractTask.Manual("c+");
		n3.setProcessTree(tree);
		tree.addNode(n3);
		tree.addEdge(((Block)n).addChild(n3));
		return tree;

	}
}
