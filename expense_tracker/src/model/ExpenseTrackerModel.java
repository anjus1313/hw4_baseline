package model;
/**
 * The ExpenseTrackerModel class is a model class for the expense tracker app that performs
 * updates in the database of transactions. It registers listeners and notifies them when a
 * state change occurs.
 *
 * @see model.Transaction
 * @see model.ExpenseTrackerModelListener
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class ExpenseTrackerModel {

  //encapsulation - data integrity
  private List<Transaction> transactions;
  private List<Integer> matchedFilterIndices;

  // List to store registered listeners
  protected Set<ExpenseTrackerModelListener> listeners;

  // This is applying the Observer design pattern.                          
  // Specifically, this is the Observable class. 

    /**
     * Contructor method
     * Declares the new array lists to contain transactions and filter indices.
     * Declares new hash set to contain all the observers (listeners) of the Model class.
      */
  public ExpenseTrackerModel() {
    transactions = new ArrayList<Transaction>();
    matchedFilterIndices = new ArrayList<Integer>();
    listeners = new HashSet<>();
  }

    /**
     * Adds a valid Transaction to the list of transactions.
     * It also clears any previous filtering.
     * @param t The transaction to be added
     */
  public void addTransaction(Transaction t) {
    // Perform input validation to guarantee that all transactions added are non-null.
    if (t == null) {
      throw new IllegalArgumentException("The new transaction must be non-null.");
    }
    transactions.add(t);
    // The previous filter is no longer valid.
    matchedFilterIndices.clear();
    stateChanged();
  }

  /**
   * Removes a Transaction from the list of transactions.
   * It also clears any previous filtering.
   * @param t The transaction to be removed
   */
  public void removeTransaction(Transaction t) {
    transactions.remove(t);
    // The previous filter is no longer valid.
    matchedFilterIndices.clear();
    stateChanged();
  }

  /**
   * Returns the list of transactions as an unmodifiable list.
   * @return List of transactions
   */
  public List<Transaction> getTransactions() {
    //encapsulation - data integrity
    return Collections.unmodifiableList(new ArrayList<>(transactions));
  }

    /**
     * Validates the rows to be filtered/highlighted and stores them in a new array List - newMatchedFilterIndices
     * @param newMatchedFilterIndices The list of rows to be filtered/highlighted
     */
  public void setMatchedFilterIndices(List<Integer> newMatchedFilterIndices) {
      // Perform input validation
      if (newMatchedFilterIndices == null) {
	  throw new IllegalArgumentException("The matched filter indices list must be non-null.");
      }
      for (Integer matchedFilterIndex : newMatchedFilterIndices) {
	  if ((matchedFilterIndex < 0) || (matchedFilterIndex > this.transactions.size() - 1)) {
	      throw new IllegalArgumentException("Each matched filter index must be between 0 (inclusive) and the number of transactions (exclusive).");
	  }
      }
      // For encapsulation, copy in the input list 
      this.matchedFilterIndices.clear();
      this.matchedFilterIndices.addAll(newMatchedFilterIndices);
      stateChanged();
  }

    /**
     * Returns a copy of the list of rows to be filtered/highlighted
     * @return An arrayList of rows to be filtered/highlighted
     */
  public List<Integer> getMatchedFilterIndices() {
      // For encapsulation, copy out the output list
      List<Integer> copyOfMatchedFilterIndices = new ArrayList<Integer>();
      copyOfMatchedFilterIndices.addAll(this.matchedFilterIndices);
      return copyOfMatchedFilterIndices;
  }

  /**
   * Registers the given ExpenseTrackerModelListener for
   * state change events.
   *
   * @param listener The ExpenseTrackerModelListener to be registered
   * @return If the listener is non-null and not already registered,
   *         returns true. If not, returns false.
   */   
  public boolean register(ExpenseTrackerModelListener listener) {
      // For the Observable class, this is one of the methods.
      if (listener!=null && !containsListener(listener)){
          listeners.add(listener);
          return true;
      }
      return false;
  }

  /**
   * Unregisters the given ExpenseTrackerModelListener for
   * state change events.
   * @param listener The ExpenseTrackerModelListener to be unregistered
   * @return If the listener is non-null and is already registered,
   *         returns true. If not, returns false.
   */
  public boolean unregister(ExpenseTrackerModelListener listener) {
      // For the Observable class, this is one of the methods.
      if (listener!=null && containsListener(listener)){
          listeners.remove(listener);
          return true;
      }
      return false;
  }

  /**
   * Returns the number of registered listeners
   * @return Number of registered listeners
   */
  public int numberOfListeners() {
      // For testing, this is one of the methods.
      return listeners.size();
  }

  /**
   * Checks if the given ExpenseTrackerModelListener is registered
   * @param listener The ExpenseTrackerModelListener to be checked for registration
   * @return If the ExpenseTrackerModelListener is registered, returns true.
   *         If not, returns false.
   */
  public boolean containsListener(ExpenseTrackerModelListener listener) {
      // For testing, this is one of the methods.
      return listeners.contains(listener);
  }

  /**
   * Updates all the registered ExpenseTrackerModelListener in case any state change occurs.
   */
  protected void stateChanged() {
      // For the Observable class, this is one of the methods.
      for (ExpenseTrackerModelListener listener : listeners) {
          listener.update(this);
      }
  }
}
