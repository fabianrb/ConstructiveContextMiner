package org.processmining.models.workshop.ccm;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;

public class ConstructiveContextMinerPlugin {
		@Plugin(
			name = "Constructive Context Miner", 
			parameterLabels = {"Log"}, 
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
		public String helloWorld(UIPluginContext context, XLog log) {
			return doCCM(context, log);
		}
	
	public String doCCM (UIPluginContext context, XLog log){
		//ProcessTree2Petrinet.convert(tree);
		return "Hola mundo";
	}
}
