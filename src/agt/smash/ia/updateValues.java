// Internal action code for project smash_ag

package smash.ia;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;
import jason.bb.BeliefBase;

/**
 * Internal action responsible to update the order of importance of values each time it is called
 */
public class updateValues extends DefaultInternalAction {
	
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
    	this.resetValues(ts, un);
    	this.applyValueOrderingRules(ts, un);
		return true; //un.unifies(new Atom("a(5)"), args[0]); // everything ok, so returns true
    }
    
    /**
     * Resets the default importance of values (by using the "vl::defaultValueJustAbove" beliefs),
     * i.e. the order of values will be set to the default one
     * @param ts Transition System
     * @param un Unifier
     */
    private void resetValues(TransitionSystem ts, Unifier un) {
    	// The deletion of all current beliefs "vl::isValueJustAbove" (that shows previous order of importance of values)
    	Literal vjaTemplate = Literal.parseLiteral("vl::isValueJustAbove(_, _)");
    	Iterator<Literal> listToDel = ts.getAg().getBB().getCandidateBeliefs(vjaTemplate, un);
    	if(listToDel != null) {
    		listToDel.forEachRemaining((vja) -> {
	    		try {
					ts.getAg().delBel(vja);
				} catch (RevisionFailedException e) {
					e.printStackTrace();
				}
        	});
    	}
    	
    	// The addition of beliefs "vl::isValueJustAbove" based upon "vl::defaultValueJustAbove" beliefs
    	Literal dvjaTemplate = Literal.parseLiteral("vl::defaultValueJustAbove(_, _)");
    	Iterator<Literal> listToAdd = ts.getAg().getBB().getCandidateBeliefs(dvjaTemplate, un);
    	if(listToAdd != null) {
    		listToAdd.forEachRemaining((dvja) -> {
    			Literal vjaNew = Literal.parseLiteral("vl::isValueJustAbove(" + dvja.getTerm(0).toString() + ", " + dvja.getTerm(1).toString() + ")");
    			try {
    				ts.getAg().addBel(vjaNew);
    			} catch (RevisionFailedException e) {
    				e.printStackTrace();
    			}
    		});
    	}
    }
    
    /**
     * Applies all valid value ordering rule in the order they are presented, consequently updating the order of importance of values
     * @param ts Transition System
     * @param un Unifier
     */
    private void applyValueOrderingRules(TransitionSystem ts, Unifier un) {
    	// Going through all the belief base, gets only the "valueOrdering" beliefs
    	ts.getAg().getBB().forEach((vo) -> {
    		if(vo.getFunctor().equals("valueOrdering") && ts.getAg().believes(vo, un)) {
    			
    			// Code executed only if the value ordering rule is "valueMostImportant", "valueLeastImportant" or "valueNotImportant"
    			// This types of rules have only one value to be used, contrary to "valueMoreImportantThan",
    			// "valueLessImportantThan", and "valueAsImportantAs" that compare two values
    			if(vo.getTerm(0).toString().equals("valueMostImportant")
    					|| vo.getTerm(0).toString().equals("valueLeastImportant")
    					|| vo.getTerm(0).toString().equals("valueNotImportant")/*vr.getArity() == 2*/) {
	    			Term val = vo.getTerm(1);
	    			
	    			// In each case, after testing if there is anything to be done (i.e. the value needs to be reordered),
	    			// it will first delete the value from the current position by using "reconnectTwoListsOfValues",
	    			// and then if the rule is "most" or "least" it will add the value back in the position near to the begin/end
	    			if(vo.getTerm(0).toString().equals("valueMostImportant") &&
	    					!ts.getAg().believes(Literal.parseLiteral("vl::isValueMostImportant(" + val + ")"), un)) {
	    				this.printDebug(ts, un, "=== Value-ordering active, type Most ~~{" + vo + "}~~");
	    				
	    				reconnectTwoListsOfValues(ts, un, val);
	    				
	    				Set<Term> termAbove = new HashSet<>();
	    				termAbove.add(Literal.parseLiteral("vbegin"));
	    				
	    				Set<Term>termBelow = new HashSet<>();
	    				Literal vbTemplate = Literal.parseLiteral("vl::isValueJustAbove(vbegin, _)");
	    				Iterator<Literal> itBelow = ts.getAg().getBB().getCandidateBeliefs(vbTemplate, un);
	    				if(itBelow != null) {
	    					itBelow.forEachRemaining((l) -> {
	    						if(l.getTerm(0).equals(Literal.parseLiteral("vbegin"))) {
	    							termBelow.add(l.getTerm(1));
	    						}
	    					});
	    				}
	    				
	    				insertBetweenTwoListsOfValues(ts, un, termAbove, termBelow, val);
	    			} else if(vo.getTerm(0).toString().equals("valueLeastImportant") &&
	    					!ts.getAg().believes(Literal.parseLiteral("vl::isValueLeastImportant(" + val + ")"), un)) {
	    				this.printDebug(ts, un, "=== Value-ordering active, type Least ~~{" + vo + "}~~");
	    				
	    				reconnectTwoListsOfValues(ts, un, val);
	    				
	    				Set<Term>termAbove = new HashSet<>();
	    				Literal vaTemplate = Literal.parseLiteral("vl::isValueJustAbove(_, vend)");
	    				Iterator<Literal> itAbove = ts.getAg().getBB().getCandidateBeliefs(vaTemplate, un);
	    				if(itAbove != null) {
	    					itAbove.forEachRemaining((l) -> {
	    						if(l.getTerm(1).equals(Literal.parseLiteral("vend"))) {
	    							termAbove.add(l.getTerm(0));
	    						}
	    					});
	    				}
	    				
	    				Set<Term> termBelow = new HashSet<>();
	    				termBelow.add(Literal.parseLiteral("vend"));
	    				
	    				insertBetweenTwoListsOfValues(ts, un, termAbove, termBelow, val);
	    			} else if(vo.getTerm(0).toString().equals("valueNotImportant") &&
	    					!ts.getAg().believes(Literal.parseLiteral("vl::isValueNotImportant(" + val + ")"), un)) {
	    				this.printDebug(ts, un, "=== Value-ordering active, type Not ~~{" + vo + "}~~");
	    				
	    				reconnectTwoListsOfValues(ts, un, val);
	    			}
    			} else /*if(vr.getArity() == 3)*/ {
	    			Term val1 = vo.getTerm(1);
	    			Term val2 = vo.getTerm(2);
	    			
	    			// In each case, after testing if there is anything to be done (i.e. the "val1" needs to be reordered),
	    			// it will first delete the "val1" from the current position by using "reconnectTwoListsOfValues", and then
	    			// it will add the "val1" back in a position near to the "val2" (just before, after, or at the same posiiton)
	    			// by using "insertBetweenTwoListsOfValues" function
	    			if(vo.getTerm(0).toString().equals("valueMoreImportantThan") &&
	    					!ts.getAg().believes(Literal.parseLiteral("vl::isValueMoreImportantThan(" + val1 + ", " + val2 + ")"), un)) {
	    				this.printDebug(ts, un, "=== Value-ordering active, type More ~~{" + vo + "}~~");
	    				
	    				reconnectTwoListsOfValues(ts, un, val1);
	    				
	    				Set<Term>termAbove = new HashSet<>();
	    				Literal vaTemplate = Literal.parseLiteral("vl::isValueJustAbove(_, " + val2 + ")");
	    				Iterator<Literal> itAbove = ts.getAg().getBB().getCandidateBeliefs(vaTemplate, un);
	    				if(itAbove != null) {
	    					itAbove.forEachRemaining((l) -> {
	    						if(l.getTerm(1).equals(val2)) {
	    							termAbove.add(l.getTerm(0));
	    						}
	    					});
	    				}
	    				
	    				Set<Term>termBelow = new HashSet<>();
	    				Term ta = termAbove.iterator().next();
	    				Literal veTemplate = Literal.parseLiteral("vl::isValueJustAbove(" + ta + ", _)");
	    				Iterator<Literal> itEqual = ts.getAg().getBB().getCandidateBeliefs(veTemplate, un);
	    				if(itEqual != null) {
	    					itEqual.forEachRemaining((l) -> {
	    						if(l.getTerm(0).equals(ta)) {
	    							termBelow.add(l.getTerm(1));
	    						}
	    					});
	    				}
	    				
	    				insertBetweenTwoListsOfValues(ts, un, termAbove, termBelow, val1);
	    			} else if(vo.getTerm(0).toString().equals("valueLessImportantThan") &&
	    					!ts.getAg().believes(Literal.parseLiteral("vl::isValueLessImportantThan(" + val1 + ", " + val2 + ")"), un)) {
	    				this.printDebug(ts, un, "=== Value-ordering active, type Less ~~{" + vo + "}~~");
	    				
	    				reconnectTwoListsOfValues(ts, un, val1);
	    				
	    				Set<Term>termBelow = new HashSet<>();
	    				Literal vbTemplate = Literal.parseLiteral("vl::isValueJustAbove(" + val2 + ", _)");
	    				Iterator<Literal> itBelow = ts.getAg().getBB().getCandidateBeliefs(vbTemplate, un);
	    				if(itBelow != null) {
	    					itBelow.forEachRemaining((l) -> {
	    						if(l.getTerm(0).equals(val2)) {
	    							termBelow.add(l.getTerm(1));
	    						}
	    					});
	    				}
	    				
	    				Set<Term>termAbove = new HashSet<>();
	    				Term tb = termBelow.iterator().next();
	    				Literal veTemplate = Literal.parseLiteral("vl::isValueJustAbove(_, " + tb + ")");
	    				Iterator<Literal> itEqual = ts.getAg().getBB().getCandidateBeliefs(veTemplate, un);
	    				if(itEqual != null) {
	    					itEqual.forEachRemaining((l) -> {
	    						if(l.getTerm(1).equals(tb)) {
	    							termAbove.add(l.getTerm(0));
	    						}
	    					});
	    				}
	    				
	    				insertBetweenTwoListsOfValues(ts, un, termAbove, termBelow, val1);
	    			} else if(vo.getTerm(0).toString().equals("valueAsImportantAs") &&
	    					!ts.getAg().believes(Literal.parseLiteral("vl::isValueAsImportantAs(" + val1 + ", " + val2 + ")"), un)) {
	    				this.printDebug(ts, un, "=== Value-ordering active, type As (Equal) ~~{" + vo + "}~~");
	    				
	    				reconnectTwoListsOfValues(ts, un, val1);
	    				
	    				Set<Term>termAbove = new HashSet<>();
	    				Literal vaTemplate = Literal.parseLiteral("vl::isValueJustAbove(_, " + val2 + ")");
	    				Iterator<Literal> itAbove = ts.getAg().getBB().getCandidateBeliefs(vaTemplate, un);
	    				if(itAbove != null) {
	    					itAbove.forEachRemaining((l) -> {
	    						if(l.getTerm(1).equals(val2)) {
	    							termAbove.add(l.getTerm(0));
	    						}
	    					});
	    				}
	    				
	    				Set<Term>termBelow = new HashSet<>();
	    				Literal vbTemplate = Literal.parseLiteral("vl::isValueJustAbove(" + val2 + ", _)");
	    				Iterator<Literal> itBelow = ts.getAg().getBB().getCandidateBeliefs(vbTemplate, un);
	    				if(itBelow != null) {
	    					itBelow.forEachRemaining((l) -> {
	    						if(l.getTerm(0).equals(val2)) {
	    							termBelow.add(l.getTerm(1));
	    						}
	    					});
	    				}
	    				
	    				insertBetweenTwoListsOfValues(ts, un, termAbove, termBelow, val1);
	    			}
	    		}
    		}
    	});
    }
    
    /**
     * Deletes the "middleVal" from its current position in the order of importance of values (it may be added back by other functions)
     * @param ts Transition System
     * @param un Unifier
     * @param middleVal The value to be deleted from the current list "order of importance of values"
     */
    private void reconnectTwoListsOfValues(TransitionSystem ts, Unifier un, Term middleVal) {
		Set<Literal> litAbove = new HashSet<>();
		Set<Term> termAbove = new HashSet<>();
		
		// Find all values above middleVal
		Literal vaTemplate = Literal.parseLiteral("vl::isValueJustAbove(_, " + middleVal + ")");
		Iterator<Literal> itAbove = ts.getAg().getBB().getCandidateBeliefs(vaTemplate, un);
		if(itAbove != null) {
			itAbove.forEachRemaining((l) -> {
				if(l.getTerm(1).equals(middleVal)) {
					litAbove.add(l);
					termAbove.add(l.getTerm(0));
				}
			});
		}
		
		// Find all values below middleVal
		Set<Literal> litBelow = new HashSet<>();
		Set<Term> termBelow = new HashSet<>();
		Literal vbTemplate = Literal.parseLiteral("vl::isValueJustAbove(" + middleVal + ", _)");
		Iterator<Literal> itBelow = ts.getAg().getBB().getCandidateBeliefs(vbTemplate, un);
		if(itBelow != null) {
			itBelow.forEachRemaining((l) -> {
				if(l.getTerm(0).equals(middleVal)) {
					litBelow.add(l);
					termBelow.add(l.getTerm(1));
				}
			});
		}
		
		// Delete all beliefs about the middleVal importance
    	for(Literal l : litAbove) {
    		try {
				ts.getAg().delBel(l);
			} catch (RevisionFailedException e) {
				e.printStackTrace();
			}
    	}
    	for(Literal l : litBelow) {
    		try {
				ts.getAg().delBel(l);
			} catch (RevisionFailedException e) {
				e.printStackTrace();
			}
    	}
    	
    	// If there is no value that was equal to middleVal, it proceeds to connect the values above and below middleVal,
    	// otherwise it means there is at least one value that is still in the same position middleVal was, so there is
    	// no need to reconnect the list of values
    	if(termAbove.iterator().hasNext()) {
			Literal veTemplate = Literal.parseLiteral("vl::isValueJustAbove(" + termAbove.iterator().next() + ", _)");
			if(!ts.getAg().believes(veTemplate, un)){
				for(Term ta : termAbove) {
					for(Term tb : termBelow) {
						Literal t = Literal.parseLiteral("vl::isValueJustAbove(" + ta + ", " + tb + ")");
						try {
							ts.getAg().addBel(t);
						} catch (RevisionFailedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
    }
    
    /**
     * Adds middleVal to a new position in the list order of importance of values
     * @param ts Transition System
     * @param un Unifier
     * @param termAbove The values just above the new position for middleVal
     * @param termBelow The values jsut below the new position for middleVal
     * @param middleVal The value to be added between the values just above and just below the new position
     */
    private void insertBetweenTwoListsOfValues(TransitionSystem ts, Unifier un, Set<Term> termAbove, Set<Term> termBelow, Term middleVal) {
    	// Adds new beliefs to set all values just above the middleVal, updating to its new position of importance
    	for(Term t : termAbove) {
    		Literal vTemplate = Literal.parseLiteral("vl::isValueJustAbove(" + t + ", " + middleVal + ")");
    		try {
				ts.getAg().addBel(vTemplate);
			} catch (RevisionFailedException e) {
				e.printStackTrace();
			}
    	}
    	
    	// Adds new beliefs to set all values just below the middleVal, updating to its new position of importance
    	for(Term t : termBelow) {
    		Literal vTemplate = Literal.parseLiteral("vl::isValueJustAbove(" + middleVal + ", " + t + ")");
    		try {
    			ts.getAg().addBel(vTemplate);
			} catch (RevisionFailedException e) {
				e.printStackTrace();
			}
    	}
    	
    	// Deletes old beliefs about the values just above and just below, since now there is middleVal in the middle of them
    	for(Term ta : termAbove) {
    		for(Term tb : termBelow) {
    			Literal vTemplate = Literal.parseLiteral("vl::isValueJustAbove(" + ta + ", " + tb + ")");
    			try {
        			ts.getAg().delBel(vTemplate);
    			} catch (RevisionFailedException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    }
    
    /**
     * Prints a debug message if the debug mode is active
     * 
     * @param ts Transition System
     * @param un Unifier
     * @param msg The debug message to be printed if debug is active
     */
    private void printDebug(TransitionSystem ts, Unifier un, String msg) {
    	if(this.isDebugActive(ts, un)) {
    		ts.getAg().getLogger().info("iaVL " + msg);
    	}
    }
    
    /**
     * Returns true if debug mode is active
     * @param ts Transition System
     * @param un Unifier
     */
    private boolean isDebugActive(TransitionSystem ts, Unifier un) {
    	return ts.getAg().believes(Literal.parseLiteral("vl::debug(true)"), un);
    }
}
