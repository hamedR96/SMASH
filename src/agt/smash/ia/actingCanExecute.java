package smash.ia;

import jason.RevisionFailedException;
import jason.asSemantics.*;
import jason.asSyntax.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.HashBiMap;

import cartago.manual.syntax.ListTerm;

/**
 * Internal action responsible to run the planner and getting as result a plan if possible each time it is called
 */
public class actingCanExecute extends DefaultInternalAction {

	@Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
		
		Plan plan = ts.getAg().getPL().get(args[0].toString());
		
		PlanBody planbody = plan.getBody();
		while(planbody != null) {
		   	Literal aiTemplate = Literal.parseLiteral("al::actionImpact(_, _, _)");
			Iterator<Literal> itAi = ts.getAg().getBB().getCandidateBeliefs(aiTemplate, un);
			final Term pb = planbody.getHead().getBodyTerm();
			if(itAi != null) {
				itAi.forEachRemaining((l) -> {
					Literal isValImportant = Literal.parseLiteral("vl::isValueImportant(" + l.getTerm(2).toString() + ")");
					if(ts.getAg().believes(l, un)
							&& l.getTerm(0).capply(un).toString().equals(pb.capply(un).toString())
							&& l.getTerm(1).toString().equals("negative")
							&& ts.getAg().believes(isValImportant, un)
							) {
						printDebug(ts, un, "=== Action with Negative Impact (-) found ~~{"+l+"}~~  so remove the Plan with Label ~~{"+plan.getLabel()+"}~~");
						ts.getAg().getPL().remove(plan.getLabel());
					}
				});
			}
			
			planbody = planbody.getBodyNext();
		}
		
		if(ts.getAg().getPL().get(plan.getLabel()) == null) {
			return false;
		} else {
			return true;
		}
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
    		ts.getAg().getLogger().info("iaAL " + msg);
    	}
    }
    
    /**
     * Returns true if debug mode is active
     * @param ts Transition System
     * @param un Unifier
     */
    private boolean isDebugActive(TransitionSystem ts, Unifier un) {
    	return ts.getAg().believes(Literal.parseLiteral("al::debug(true)"), un);
    }
}