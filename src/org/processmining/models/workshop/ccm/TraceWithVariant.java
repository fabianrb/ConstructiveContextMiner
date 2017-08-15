package org.processmining.models.workshop.ccm;

import java.util.Arrays;
import java.util.List;

import org.deckfour.xes.classification.XEventClass;

public class TraceWithVariant {
	private List<XEventClass> trace; 
	private String[] context;
	public TraceWithVariant(List<XEventClass> t, String[] c) {
		trace = t;
		context = c;
	}
	
	public List<XEventClass> getTrace() {
		return trace;
	}
	public String[] getContext() {
		return context;
	}
	
	public List<String> getContextAsList() {
		return Arrays.asList(context);
	}
	@Override
	public String toString() {
		return "Trace: " + trace.toString()+ " Contexto:" + Arrays.toString(context);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(context);
		result = prime * result + ((trace == null) ? 0 : trace.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other == null) return false;
	    if (other == this) return true;
	    if (!(other instanceof TraceWithVariant))return false;
	    TraceWithVariant otherCast = (TraceWithVariant) other;
	    return Arrays.equals(context, otherCast.getContext()) && trace.equals(otherCast.getTrace());
	}
	

}
