// Internal action code for project smash_ag

package smash.ia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jason.RevisionFailedException;
import jason.asSemantics.*;
import jason.asSyntax.*;

/**
 * Internal action responsible to update (activate, inactivate, drop, create new...) goals each time it is called
 */
public class updateGoals extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
    	
    	// For debug purposes only
		this.printDebug(ts, un, "=== Before Goal-reasoning, the Goal State beliefs are as follows (if empty, then there is none):");
    	ts.getAg().getBB().forEach((state) -> {
    		if(state.getNS().equals(new Atom("gl")) && state.getFunctor().equals("state") && ts.getAg().believes(state, un)) {
    			this.printDebug(ts, un, "=== Goal State ~~{" + state + "}~~");
    		}
    	});
    	
    	// Calls the update function (the receiving of a new goal at the update function is performed at the agent level)
    	this.update(ts, un);

    	// Calls the select function
    	this.select(ts, un);
    	
    	// Calls the sort function
    	ArrayList<Literal> updatedGoals = this.sort(ts, un);
    	
    	// Sends goals to planning layer using beliefs and plans
    	for(Literal l : updatedGoals) {
    		try {
    			ts.getAg().delBel(Literal.parseLiteral("pl::goalToPlan(_)"));
    			ts.getAg().addBel(Literal.parseLiteral("pl::goalToPlan(" + l.getTerm(0) + "," + l.getTerm(1) + ")"));
    		} catch (RevisionFailedException e) {
    			e.printStackTrace();
    		}
    	}
    	
		// For debug purposes only
		this.printDebug(ts, un, "=== Goal-reasoning ended with a total of " + updatedGoals.size() + " goals updated in Goal State beliefs, the full list of Goals is ~~{" + updatedGoals + "}~~");
		this.printDebug(ts, un, "=== After Goal-reasoning, the Goal State beliefs are as follows (if empty, then there is none):");
    	ts.getAg().getBB().forEach((state) -> {
    		if(state.getNS().equals(new Atom("gl")) && state.getFunctor().equals("state") && ts.getAg().believes(state, un)) {
    			this.printDebug(ts, un, "=== Goal State ~~{" + state + "}~~");
    		}
    	});
    	
    	return true;
    }
    
    /**
     * All new given goals from whose source is equal to "user" by definition
     * get the state "waiting" and are added to beliefs of the agent, and
     * the information received by the planning and acting layers also update
     * the status of goals with "success" or "fail"
     * 
     * @param ts Transition System
     * @param un Unifier
     * @param givenGoal The goal given by the user to be achieved by the agent
     */
    private void update(TransitionSystem ts, Unifier un) throws Exception {
    	// fully take into account return from planning...
    	// fully take into account return from acting...
    	
    	// It deletes the dropped goals, but now dropped goals are directly deleted, so this code is useless
    	// It is kept in case the architecture changes
    	/* Literal gsTemplate = Literal.parseLiteral("gl::state(_, _, _)");
		Iterator<Literal> itGs = ts.getAg().getBB().getCandidateBeliefs(gsTemplate, un);
		if(itGs != null) {
			itGs.forEachRemaining((l) -> {
				if(ts.getAg().believes(l, un) && l.getTerm(1).toString().equals("dropped")) {
					this.printDebug(ts, un, "=== Deleted the Dropped Goal ~~{" + l + "}~~");
					try {
						ts.getAg().delBel(l);
					} catch (RevisionFailedException e) {
						e.printStackTrace();
					}
				}
			});
		}*/
    }
    
    /**
     * All "gl::goalActivation" whose condition is satisfied are applied,
     * i.e. the body of these rules are used to update the statuses of goals
     * (setting statuses to "active", "inactive"), and having by
     * definition the source equal to "self" since they are autonomously updated goals
     * 
     * @param ts Transition System
     * @param un Unifier
     */
    private void select(TransitionSystem ts, Unifier un) {
    	Literal gaTemplate = Literal.parseLiteral("gl::goalActivation(_, _, _)");
		Iterator<Literal> itGa = ts.getAg().getBB().getCandidateBeliefs(gaTemplate, un);
		if(itGa != null) {
			itGa.forEachRemaining((l) -> {
				if(ts.getAg().believes(l, un)) {
					String glStateGoal = this.makeExpressionGround(ts, un, l, 0);
					String glStateStatus = this.makeExpressionGround(ts, un, l, 1);
					String glStateSource = this.makeExpressionGround(ts, un, l, 2);
					
					Literal newAutoGoal = Literal.parseLiteral("gl::state("+glStateGoal+","+glStateStatus+","+glStateSource+")");
					Literal glStateGoalAnyStatusAnySource = Literal.parseLiteral("gl::state(" + glStateGoal + ", _, _)");
					if(ts.getAg().believes(newAutoGoal, un)) {
						this.printDebug(ts, un, "=== Goal-reasoning will not self-activate a goal because found there is already one for Goal ~~{" + newAutoGoal + "}~~");
					} else {
						try {
							ts.getAg().delBel(glStateGoalAnyStatusAnySource);
							ts.getAg().addBel(newAutoGoal);
						} catch (RevisionFailedException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
    }
    
    /**
     * All goals statuses whose source is equal to "self" are filtered,
     * based upon the "gl::goalImpact" rules that are active in the current context,
     * and then the goals statuses that have at least one negative impact over an
     * existing value are deleted the beliefs of the agent, anf finally as a last step
     * all newly created or updated goal statuses are ordered accordingly to the order
     * of importance of values, added to "orderedGoals" and returned by the function
     * to be sent to the planning layer afterwards
     * 
     * @param ts Transition System
     * @param un Unifier
     * @return The totally ordered st of goals to be sent to the planning layer
     */
    private ArrayList<Literal> sort(TransitionSystem ts, Unifier un) {
    	Literal gsTemplate = Literal.parseLiteral("gl::state(_, _, _)");
		Iterator<Literal> itGs = ts.getAg().getBB().getCandidateBeliefs(gsTemplate, un);
		if(itGs != null) {
			itGs.forEachRemaining((gs) -> {
				if(ts.getAg().believes(gs, un) && gs.getTerm(2).toString().equals("self")) {
					this.printDebug(ts, un, "=== Goal-reasoning will test the impact of a self-activated Goal (source 'self') ~~{" + gs + "}~~");
					
					Literal giTemplate = Literal.parseLiteral("gl::goalImpact(_, _, _)");
					Iterator<Literal> itGi = ts.getAg().getBB().getCandidateBeliefs(giTemplate, un);
					if(itGi != null) {
						itGi.forEachRemaining((gi) -> {
							if(ts.getAg().believes(gi, un)) {
								String stateGoal = this.makeExpressionGround(ts, un, gs, 0);
								String giGoal = this.makeExpressionGround(ts, un, gi, 0);
								
								if(gi.getTerm(0).capply(un).toString().equals(gs.getTerm(0).capply(un).toString()) && gs.getTerm(1).toString().equals("active") && gi.getTerm(1).toString().equals("negative")) {
									this.printDebug(ts, un, "=== Goal-reasoning found a self-activated Goal with Negative Impact (-) ~~{" + gi + "}~~");
									//Literal newDroppedGoal = Literal.parseLiteral("gl::state(" + giGoal + ", dropped, self)");
									Literal stateGoalAnyStatusAnySource = Literal.parseLiteral("gl::state(" + stateGoal + ", _, _)");
									try {
										ts.getAg().delBel(stateGoalAnyStatusAnySource);
										//ts.getAg().addBel(newDroppedGoal);
									} catch (RevisionFailedException e) {
										e.printStackTrace();
									}
								}
							}
						});
					}
				}
			});
		}
		
		HashSet<Literal> goalsToOrder = new HashSet<>();
		itGs = ts.getAg().getBB().getCandidateBeliefs(gsTemplate, un);
		if(itGs != null) {
			itGs.forEachRemaining((gs) -> {
				if(ts.getAg().believes(gs, un) && (gs.getTerm(1).toString().equals("active") || gs.getTerm(1).toString().equals("inactive"))) {
					goalsToOrder.add(gs);
					this.printDebug(ts, un, "=== Goal-reasoning will order the Goals ~~{" + gs + "}~~");
				}
			});
		}
		
		Literal voTemplate = Literal.parseLiteral("importanceOfValues(_)");
		Literal voBelief = ts.getAg().getBB().getCandidateBeliefs(voTemplate, un).next();
		Term voList = voBelief.logicalConsequence(ts.getAg(), un).next().get(voBelief.getTerm(0).toString());
		List<String> voArray = Arrays.asList(voList.toString().replace("[", "").replace("]", "").replace(" ", "").split(","));
		this.printDebug(ts, un, "=== Goal-reasoning will order Goals using this order of Values ~~{" + voArray + "}~~");
		
		ArrayList<Literal> orderedGoals = new ArrayList<>();
		for(String value : voArray) {
			for(Literal goal : goalsToOrder) {
				Literal gi = Literal.parseLiteral("gl::goalImpact(" + goal.getTerm(0) + ", positive, " + value + ")");
				if(ts.getAg().believes(gi, un)) {
					orderedGoals.add(goal);
				};
			}
		}
		goalsToOrder.removeAll(orderedGoals);
		
		for(String value : voArray) {
			for(Literal goal : goalsToOrder) {
				Literal gi = Literal.parseLiteral("gl::goalImpact(" + goal.getTerm(0) + ", neutral, " + value + ")");
				if(ts.getAg().believes(gi, un)) {
					orderedGoals.add(goal);
				};
			}
		}
		goalsToOrder.removeAll(orderedGoals);
		
		for(Literal goal : goalsToOrder) {
			orderedGoals.add(goal);
		}
		goalsToOrder.removeAll(orderedGoals);
		
		return orderedGoals;
    }
    
    /**
     * Makes an expression ground by replacing variables with terms,
     * e.g. "at(tv, Room)" can be grounded to "at(tv, bedroom)" or "at(tv, livingroom)"
     * 
     * @param ts Transition System
     * @param un Unifier
     * @param l Literal to be grounded (make variables become terms)
     * @param index The index of the literal we want to ground, e.g. index = 2 means we ground the variable "Room" in "at(tv, Room)"
     * @return Returns the literal grounded, e.g. if Room in "at(tv, Room)" is solved to "bedroom", then it returns "at(tv, bedroom)"
     */
    private String makeExpressionGround(TransitionSystem ts, Unifier un, Literal l, int index) {
    	if(l.getTerm(index).isGround()) {
			return l.getTerm(index).toString();
		} else if (l.getTerm(index).isVar()) {
			return l.logicalConsequence(ts.getAg(), un).next().get(l.getTerm(index).toString()).toString();
		} else {
			return l.getTerm(index).capply(un).toString();
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
    		ts.getAg().getLogger().info("iaGL " + msg);
    	}
    }
    
    /**
     * Returns true if debug mode is active
     * @param ts Transition System
     * @param un Unifier
     */
    private boolean isDebugActive(TransitionSystem ts, Unifier un) {
    	return ts.getAg().believes(Literal.parseLiteral("gl::debug(true)"), un);
    }
}
