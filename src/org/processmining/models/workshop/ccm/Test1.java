package org.processmining.models.workshop.ccm;

import java.util.Iterator;

import nl.tue.astar.AStarException;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.plugins.connectionfactories.logpetrinet.TransEvClassMapping;
import org.processmining.plugins.petrinet.replayer.algorithms.IPNReplayAlgorithm;
import org.processmining.plugins.petrinet.replayer.algorithms.IPNReplayParameter;
import org.processmining.plugins.petrinet.replayer.ui.PNReplayerUI;
import org.processmining.plugins.petrinet.replayresult.PNRepResult;
import org.processmining.plugins.petrinet.replayresult.StepTypes;
import org.processmining.plugins.replayer.replayresult.SyncReplayResult;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.impl.AbstractBlock;
import org.processmining.processtree.impl.ProcessTreeImpl;
import org.processmining.processtree.Node;

public class Test1 {
	@Plugin(
		name = "Test1", 
		parameterLabels = {"Log", "Model"}, 
		returnLabels = { "Hello world string" }, 
		returnTypes = { String.class }, 
		userAccessible = true, 
		help = "Produces a representation of a Configurable Process Tree as a String"
	)
	@UITopiaVariant(
		affiliation = "Universidad de Chile", 
		author = "Fabian Rojas Blum", 
		email = "fabian.rojas.blum@gmail.com"
	)
	public String helloWorld(UIPluginContext context, XLog log, Petrinet net) {
		return doCCM(context, log, net);
	}

public String doCCM (UIPluginContext context, XLog log, Petrinet net){
	//ProcessTree2Petrinet.convert(tree);
	PNReplayerUI pnReplayerUI = new PNReplayerUI();
	Object[] resultConfiguration = pnReplayerUI.getConfiguration(context, net, log);
	if (resultConfiguration == null) {
		context.getFutureResult(0).cancel(true);
		return null;
	}
	
	//Compute the alignments
	IPNReplayAlgorithm selectedAlg = (IPNReplayAlgorithm) resultConfiguration[PNReplayerUI.ALGORITHM];
	PNRepResult alignments = null;
	
	try {
		alignments = selectedAlg.replayLog(context, net, log, (TransEvClassMapping) resultConfiguration[PNReplayerUI.MAPPING], 
				(IPNReplayParameter) resultConfiguration[PNReplayerUI.PARAMETERS]);
	} catch (AStarException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	System.out.println("Cantidad de alineamientos: " + alignments.size());
	int i=1;
	
	ProcessTree tree = new ProcessTreeImpl();
	Node n = new AbstractBlock.Seq("");
	n.setProcessTree(tree);
	tree.addNode(n);
	tree.setRoot(n);
	
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
			
			/*
			//If it is a log move, just skip
			if(type == StepTypes.L){
				itTask.next();//Skip the task
			}
			
			else{ //It is a PetriNet Transition
				Transition trans = ((Transition) itTask.next());
				//t.add(trans);
			}*/
		}
	}
		
	//return checkMultiETCAlign1(context,log, net, sett, alignments);
	
	
	return "Hola mundo";
}
}
