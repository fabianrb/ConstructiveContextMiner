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
		Block seq1 = new AbstractBlock.Seq("");
		tree.addNode(seq1);
		tree.setRoot(seq1);
		
		Node a = new AbstractTask.Manual("a");
		tree.addNode(a);
		tree.addEdge(seq1.addChild(a));
		
		
		Block xor1 = new AbstractBlock.Xor("");
		tree.addNode(xor1);
		tree.addEdge(seq1.addChild(xor1));
		
		
		Block and1 = new AbstractBlock.And("");
		tree.addNode(and1);
		tree.addEdge(xor1.addChild(and1));
		
		Node c = new AbstractTask.Manual("c");
		tree.addNode(c);
		tree.addEdge(and1.addChild(c));

		Node d = new AbstractTask.Manual("d");
		tree.addNode(d);
		tree.addEdge(and1.addChild(d));
		
		Node b = new AbstractTask.Manual("b");
		tree.addNode(b);
		tree.addEdge(and1.addChild(b));
		
		Block seq2 = new AbstractBlock.Seq("");
		tree.addNode(seq2);
		tree.addEdge(xor1.addChild(seq2));
		
		Node b1 = new AbstractTask.Manual("b1");
		tree.addNode(b1);
		tree.addEdge(seq2.addChild(b1));

		
		Block xor3 = new AbstractBlock.Xor("");
		tree.addNode(xor3);
		tree.addEdge(seq2.addChild(xor3));		
		
		
		Node d2 = new AbstractTask.Manual("d2");
		tree.addNode(d2);
		tree.addEdge(xor3.addChild(d2));
		

		Node d_ = new AbstractTask.Manual("d");
		tree.addNode(d_);
		tree.addEdge(xor3.addChild(d_));

		
		Node b2 = new AbstractTask.Manual("b2");
		tree.addNode(b2);
		tree.addEdge(seq2.addChild(b2));
		
		Node c_ = new AbstractTask.Manual("c+complete");
		tree.addNode(c_);
		tree.addEdge(seq2.addChild(c_));
		
		Node d2_ = new AbstractTask.Manual("d2");
		tree.addNode(d2_);
		tree.addEdge(seq2.addChild(d2_));
		
		Block xor2 = new AbstractBlock.Xor("");
		tree.addNode(xor2);
		tree.addEdge(seq1.addChild(xor2));
		
		Node e = new AbstractTask.Manual("e");
		tree.addNode(e);
		tree.addEdge(xor2.addChild(e));
		
		
		Block seq3 = new AbstractBlock.Seq("");
		tree.addNode(seq3);
		tree.addEdge(xor2.addChild(seq3));
		
		Node f = new AbstractTask.Manual("f");
		tree.addNode(f);
		tree.addEdge(seq3.addChild(f));
		
		Node g = new AbstractTask.Manual("g");
		tree.addNode(g);
		tree.addEdge(seq3.addChild(g));
		
		
		
		
		return tree;

	}
}
