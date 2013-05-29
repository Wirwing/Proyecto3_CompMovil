package com.fmat.proyecto3.utils.memento;

/**
 * Memento Interface to Originator
 * 
 * This interface allows the originator to restore its state
 */
public interface PreviousSortToOriginator {

	/**
	 * 
	 * @return desde
	 */
	public int getFrom();
	
	/**
	 * 
	 * @return hasta
	 */
	public int getTo();
	
}
