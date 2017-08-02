package org.processmining.models.workshop.ccm;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.processmining.plugins.properties.processmodel.Property;
import org.processmining.processtree.Block;
import org.processmining.processtree.Edge;
import org.processmining.processtree.Expression;
import org.processmining.processtree.Node;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.Variable;
import org.processmining.processtree.impl.AbstractBlock;

public class ConfigurableNode implements Node{

	@Override
	public UUID getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getIndependentProperty(Class<? extends Property<?>> property)
			throws InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getIndependentProperty(Property<?> property) throws InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIndependentProperty(Class<? extends Property<?>> property, Object value)
			throws InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setIndependentProperty(Property<?> property, Object value)
			throws InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getDependentProperty(Class<? extends Property<?>> property)
			throws InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getDependentProperty(Property<?> property) throws InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDependentProperty(Class<? extends Property<?>> property, Object value)
			throws InstantiationException, IllegalAccessException {
		// TODO Auto-generat	ed method stub
		
	}

	@Override
	public void setDependentProperty(Property<?> property, Object value)
			throws InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeIndependentProperty(Class<? extends Property<?>> property)
			throws InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeIndependentProperty(Property<?> property) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDependentProperty(Class<? extends Property<?>> property)
			throws InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDependentProperty(Property<?> property) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractMap<Property<?>, Object> getIndependentProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractMap<Property<?>, Object> getDependentProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Variable> getReadVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addReadVariable(Variable var) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeReadVariable(Variable var) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<Variable> getRemovableReadVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addRemovableReadVariable(Variable var) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeRemovableReadVariable(Variable var) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<Variable> getReadVariablesRecursive() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Variable> getWrittenVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addWriteVariable(Variable var) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeWriteVariable(Variable var) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<Variable> getRemovableWrittenVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addRemovableWriteVariable(Variable var) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeRemovableWriteVariable(Variable var) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<Variable> getWrittenVariablesRecursive() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProcessTree(ProcessTree tree) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProcessTree getProcessTree() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Edge addParent(UUID id, Block parent, Expression expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Edge addParent(Block parent, Expression expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Edge addParent(Block parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Block> getParents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeIncomingEdge(Edge edge) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int numParents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Edge> getIncomingEdges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addIncomingEdge(Edge edge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isRoot() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toStringShort() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}

}
