package org.processmining.models.workshop.ccm;

import org.processmining.processtree.Node;

public class Alignment {
private Node model;
private Node log;
private AlignType type;

	public Alignment (Node model, Node log, AlignType type){
		this.model = model;
		this.log = log;
		this.type = type;
	}
	
	public Node getLog() {
		return log;
	}
	public Node getModel() {
		return model;
	}
	public AlignType getType() {
		return type;
	}
	public Node getValue(){
		if(type==AlignType.log)
			return log;
		return model;
		
	}
}
