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

import cartago.manual.syntax.Atom;

/**
 * Internal action responsible to run the planner and getting as result a plan
 * if possible each time it is called
 */
public class runPlanner extends DefaultInternalAction {

	private HashBiMap<String, String> dictFunctors;
	private HashBiMap<String, String> dictTerms;
	private HashMap<String, Integer> dictFunctorsArity;

	private void addFunctorAndTermsFromBeliefToDictionaries(Literal belief) {
		if (belief.getNS().toString().equals("pl") && belief.getFunctor().toString().equals("actionConditions")) {
			String key = belief.getTerm(0).toString();
			String val = functorToPddlFormat(Literal.parseLiteral(belief.getTerm(0).toString()));
			dictFunctors.forcePut(key, val);
			dictFunctorsArity.put(val, ListTermImpl.parseList(belief.getTerm(1).toString()).size());
		} else {
			String key = (belief.getNS().toString().equals("default") ? "" : belief.getNS() + "::")
					+ belief.getFunctor();
			String val = functorToPddlFormat(belief);
			dictFunctors.forcePut(key, val);
			dictFunctorsArity.put(val, belief.getArity());
		}

		for (Term t : belief.getTermsArray()) {
			if (t.toString().startsWith("_")) {
				dictTerms.forcePut("_", "_");
			} else if (t.isGround()) {
				dictTerms.forcePut(t.toString(), stringToPddlFormat(t.toString()));
			}
		}
	}

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {

		String search = "astar(lmcut())";// "astar(blind(transform=no_transform(), cache_estimates=true))";
		String domainPath = "domain.pddl";
		String problemPath = "problem.pddl";
		String domainName = "smashdm";
		String problemName = "smashpb";
		String solutionName = "sas_plan";
		
		String mainPath = "lib/downward-linux/";
		String python = "python3";
		String operSys = System.getProperty("os.name").toLowerCase();

		if (operSys.contains("win")) {
			mainPath = "lib/downward-win/";
			python = "py";
		} else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix")) {
			mainPath = "lib/downward-linux/";
			python = "python3";
		}

		String execLine = python + " " + mainPath + "fast-downward.py " + mainPath + domainPath + " " + mainPath
				+ problemPath + " --search " + search;

		//////////////////////////////////////////////////////////////////
		// VOCABULARY: Functors and Terms
		//////////////////////////////////////////////////////////////////

		this.dictFunctors = HashBiMap.create();
		this.dictTerms = HashBiMap.create();
		this.dictFunctorsArity = new HashMap<>();

		ts.getAg().getBB().forEach(belief -> {
			if (belief.isRule() && belief.hasAnnot(Literal.parseLiteral("useInPlanner"))) {
				belief.logicalConsequence(ts.getAg(), un).forEachRemaining(context -> {
					Literal consequence = (Literal) belief.capply(context);
					this.addFunctorAndTermsFromBeliefToDictionaries(consequence);
				});
			} else {
				this.addFunctorAndTermsFromBeliefToDictionaries(belief);
			}
		});

		this.printDebug(ts, un,
				"=== Planner will use this Dictionary of Functors (predicates) ~~{" + dictFunctors.toString() + "}~~");
		this.printDebug(ts, un,
				"=== Planner will use this Dictionary of Terms (subjects, objects) ~~{" + dictTerms.toString() + "}~~");

		//////////////////////////////////////////////////////////////////
		// PDDL DOMAIN: Predicates, Axioms, Actions
		//////////////////////////////////////////////////////////////////

		// PDDL Predicates (from Belief Base beliefs of agent)
		StringBuilder predicates = new StringBuilder();
		predicates.append("\n\t(:predicates\n");
		dictFunctorsArity.forEach((key, val) -> {
			predicates.append("\t\t(" + key);
			for (int i = 0; i < val; i++) {
				predicates.append(" ?term" + (i + 1));
			}
			predicates.append(")\n");
		});
		predicates.append("\t)\n\n");

		// PDDL Axioms (from Rules of agent)
		/*
		 * StringBuilder axioms = new StringBuilder();
		 * ts.getAg().getBB().forEach((belief) -> { if(belief.isRule() &&
		 * belief.getNS().toString().equals("axiom")) { String axiom ="";
		 * HashSet<String> vars = new HashSet<>(); String terms = ""; String cond = "";
		 * 
		 * for(Term term : belief.getTermsArray()) {
		 * if(term.toString().matches("[A-Z](.*)")) { vars.add("?" +
		 * term.toString().substring(0, 1).toLowerCase() +
		 * term.toString().substring(1)); terms += "?"; } terms +=
		 * term.toString().substring(0, 1).toLowerCase() + term.toString().substring(1)
		 * + " "; }
		 * 
		 * for(String term : belief.toString().split(":- ")[1].split("[\\(\\)&| ,]")) {
		 * if(term.toString().matches("[A-Z](.*)")) { vars.add("?" +
		 * term.toString().substring(0, 1).toLowerCase() +
		 * term.toString().substring(1)); } }
		 * 
		 * cond = belief.toString().split(":- ")[1]; cond = cond.replaceAll("::", "-");
		 * cond = Pattern.compile("\\(([A-Z])").matcher(cond).replaceAll(match ->
		 * {return "(?" + match.group(1).toLowerCase();}); cond =
		 * Pattern.compile(",([A-Z])").matcher(cond).replaceAll(match -> ",?" +
		 * match.group(1).toLowerCase()); cond =
		 * Pattern.compile("([^ \\(]+?)\\(([^\\)]+?)\\)").matcher(cond).replaceAll(match
		 * -> "[" + match.group(1) + " " + match.group(2) + "]"); cond =
		 * cond.replaceAll(",", " "); cond = cond.replaceAll("not \\(", "(not "); cond =
		 * recursiveMatchParenthesis(cond); cond = cond.replaceAll("[\\{\\[]", "(");
		 * cond = cond.replaceAll("[\\}\\]]", ")");
		 * 
		 * axiom += "\t(:axiom" + "\n"; axiom += "\t\t" + ":vars (" +
		 * vars.stream().collect(Collectors.joining(" ")) + ")" + "\n"; //axiom += ";; "
		 * + belief.toString().split(":- ")[1] + "\n"; axiom += "\t\t" + ":context " +
		 * cond + "\n"; axiom += "\t\t" + ":implies (" + (belief.getNS().toString() ==
		 * "default" ? "" : belief.getNS() + "-") + belief.getFunctor() + " " +
		 * terms.trim() + ")" + "\n"; axiom += "\t)\n\n";
		 * 
		 * axioms.append(axiom); } });
		 */

		// PDDL Actions (from Plans of agent)
		StringBuilder actions = new StringBuilder();
		ts.getAg().getBB().forEach((l) -> {
			if (l.getNS().toString().equals("pl") && l.getFunctor().equals("actionConditions")) {
				actions.append("\t(:action " + l.getTerm(0).toString() + "\n");
				actions.append("\t\t:parameters ("
						+ l.getTerm(1).toString().replace("[", "?").replace("]", "").replace(",", " ?").toLowerCase()
						+ ")\n");
				actions.append("\t\t:precondition " + this.conditionJasonToPddl(l.getTerm(2).toString()) + "\n");
				actions.append("\t\t:effect " + this.conditionJasonToPddl(l.getTerm(3).toString()) + "\n");
				actions.append("\t)\n\n");
			}
		});

		// Build Domain
		StringBuilder domain = new StringBuilder();
		domain.append("(define (domain " + domainName + ")\n\n");
		domain.append("\t(:requirements\n");
		domain.append("\t\t:strips\n");
		// domain.append("\t\t:domain-axioms\n");
		domain.append("\t)\n\n");
		domain.append(predicates);
		domain.append(actions);
		// domain.append(axioms);
		domain.append(")");

		// Write Domain
		File domainFile = new File(mainPath + domainPath);
		domainFile.createNewFile();
		FileWriter domainWriter = new FileWriter(domainFile, false);
		domainWriter.write(domain.toString());
		domainWriter.close();

		//////////////////////////////////////////////////////////////////
		// PDDL PROBLEM: Objects, Init, Goal
		//////////////////////////////////////////////////////////////////

		// PDDL Objects (from Belief Base baliefs of agent)
		StringBuilder objects = new StringBuilder();
		objects.append("\n\t(:objects\n");
		dictTerms.forEach((key, val) -> {
			objects.append("\t\t" + val + "\n");
		});
		objects.append("\t)\n\n");

		// PDDL Init (from Belief Base beliefs of agent)
		StringBuilder init = new StringBuilder();
		init.append("\n\t(:init\n");
		ts.getAg().getBB().forEach((belief) -> {
			String ns = belief.getNS().toString();
			if (belief.isGround() && !ns.equals("vl") && !ns.equals("pl") && !ns.equals("al") && !ns.equals("kqml")
					&& !ns.equals("planner")) {
				init.append("\t\t(" + this.functorToPddlFormat(belief));
				for (Term t : belief.getTermsArray()) {
					init.append(" " + this.stringToPddlFormat(t.toString()));
				}
				init.append(")\n");
			} else if (belief.isRule() && belief.hasAnnot(Literal.parseLiteral("useInPlanner"))) {
				belief.logicalConsequence(ts.getAg(), un).forEachRemaining(context -> {
					Literal consequence = (Literal) belief.capply(context);
					init.append("\t\t(" + this.functorToPddlFormat(consequence));
					for (Term t : consequence.getTermsArray()) {
						init.append(" " + this.stringToPddlFormat(t.toString()));
					}
					init.append(")\n");
				});
			}
		});
		init.append("\t)\n\n");

		// PDDL Goal (from output of Goal-Reasoning layer)
		HashMap<String, String> goalAchieveConditions = new HashMap<>();
		ts.getAg().getBB().forEach((l) -> {
			if (l.getNS().toString().equals("gl") && l.getFunctor().equals("goalAchieveCondition")) {
				goalAchieveConditions.put(l.getTerm(0).toString() + l.getTerm(1).toString(), l.getTerm(2).toString());
			}
		});

		StringBuilder goal = new StringBuilder();
		goal.append("\n\t(:goal\n");
		goal.append("\t\t" + conditionJasonToPddl(args[1].toString()) + "\n");
		goal.append("\t)\n\n");

		// Build Problem
		StringBuilder problem = new StringBuilder();
		problem.append("(define (problem " + problemName + ")\n\n");
		problem.append("\t(:domain " + domainName + ")\n\n");
		problem.append(objects);
		problem.append(init);
		problem.append(goal);
		problem.append(")");

		File problemFile = new File(mainPath + problemPath);
		problemFile.createNewFile();
		FileWriter problemWriter = new FileWriter(problemFile, false);
		problemWriter.write(problem.toString());
		problemWriter.close();

		////////////////////////////////////////////////////////////////
		// Execute Planner
		////////////////////////////////////////////////////////////////

		String plan = this.executePlanner(ts, un, execLine, solutionName);
		if (plan.isEmpty()) {
			this.printDebug(ts, un, "=== Planning did not found a Plan (tip: check the Planner errors)");
			return true;
		}

		////////////////////////////////////////////////////////////////
		// Add Plan to Library
		////////////////////////////////////////////////////////////////

		String[] planActions = plan.split("[\\(\\)]");
		ArrayList<String> planSteps = new ArrayList<String>();

		this.printDebug(ts, un, "=== Planner found a Plan; see below its steps (if Plan is not empty):");
		for (String s : planActions) {
			if (!s.isEmpty()) {
				this.printDebug(ts, un, "=== Plan step ~~{" + s + "}~~");
				String[] actionItems = s.split(" ");
				planSteps.add(dictFunctors.inverse().get(actionItems[0]) + "(" + dictTerms.inverse().get(actionItems[1])
						+ "," + dictTerms.inverse().get(actionItems[2]) + ")");
			}
		}

		String planBody = planSteps.stream().collect(Collectors.joining(";\n"));
		String randomLabel = "iiilabel_" + String.format("%06d", (int) (Math.random() * (999999 + 1)));
		Plan jasonPlan = Plan
				.parse("@" + randomLabel + "\n+!" + args[0] + " : " + randomLabel + " <- \n " + planBody + ".");

		// Add Plan to Agent Library in order to be executed by Acting Layer
		ts.getAg().getPL().add(jasonPlan);
		this.printDebug(ts, un, "=== Planning will send to Acting Layer the Plan ~~{" + jasonPlan + "}~~");

		// Send Plan to Acting Layer
		try {
			ts.getAg().delBel(Literal.parseLiteral("al::planToAct(_)"));
			ts.getAg().addBel(Literal.parseLiteral("al::planToAct(" + args[0] + "," + randomLabel + ")"));
		} catch (RevisionFailedException e) {
			e.printStackTrace();
		}

		return true; // everything ok, so returns true // un.unifies(args[1],
						// Literal.parseLiteral(replan+""));
	}

	private String executePlanner(TransitionSystem ts, Unifier un, String execLine, String solutionName)
			throws Exception {
		String string = null;
		Process p = Runtime.getRuntime().exec(execLine);
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		// read the output from the command
		this.printDebugPlanner(ts, un, "=== Messages of Planner below (if any):");
		while ((string = stdInput.readLine()) != null) {
			this.printDebugPlanner(ts, un, "=== Msg: " + string);
		}

		// read any errors from the attempted command
		this.printDebugPlanner(ts, un, "=== Errors of Planner below (if any):");
		boolean hasError = false;
		while ((string = stdError.readLine()) != null) {
			this.printDebugPlanner(ts, un, "=== Error: " + string);
			hasError = true;
		}

		// Get solution back
		try {
			BufferedReader reader = new BufferedReader(new FileReader(solutionName));
			String line = reader.readLine();
			String plan = "";
			this.printDebugPlanner(ts, un, "=== Solution of Planner below (if any):");
			while (!hasError && line != null && !line.startsWith(";")) {
				this.printDebugPlanner(ts, un, "=== Solution: " + line);
				plan += line;
				line = reader.readLine();
			}
			reader.close();
			return plan;
		} catch (FileNotFoundException e) {
			return "";
		}
	}

	/**
	 * Makes an expression ground by replacing variables with terms, e.g. "at(tv,
	 * Room)" can be grounded to "at(tv, bedroom)" or "at(tv, livingroom)"
	 * 
	 * @param ts    Transition System
	 * @param un    Unifier
	 * @param l     Literal to be grounded (make variables become terms)
	 * @param index The index of the literal we want to ground, e.g. index = 2 means
	 *              we ground the variable "Room" in "at(tv, Room)"
	 * @return Returns the literal grounded, e.g. if Room in "at(tv, Room)" is
	 *         solved to "bedroom", then it returns "at(tv, bedroom)"
	 */
	private String makeExpressionGround(TransitionSystem ts, Unifier un, Literal l, int index) {
		if (l.getTerm(index).isGround()) {
			return l.getTerm(index).toString();
		} else if (l.getTerm(index).isVar()) {
			return l.logicalConsequence(ts.getAg(), un).next().get(l.getTerm(index).toString()).toString();
		} else {
			return l.getTerm(index).capply(un).toString();
		}
	}

	private String conditionJasonToPddl(String input) {
		input = input.replaceAll("::", "--");
		input = Pattern.compile("([\\(,])([A-Z])").matcher(input)
				.replaceAll(match -> match.group(1) + "?" + match.group(2).toLowerCase()); // all variables (starting
																							// with an uppercase, e.g.
																							// "Var") become "?var"
		input = input.replaceAll(",", " ");
		input = Pattern.compile("([^ \\(]+?)\\(([^\\)]+?)\\)").matcher(input).replaceAll(
				match -> "[" + stringToPddlFormat(match.group(1)) + " " + stringToPddlFormat(match.group(2)) + "]"); // all
																														// "predicate(subject,object)"
																														// become
																														// "[predicate
																														// subject,object]"
		input = input.replaceAll("not \\(", "(not ");
		input = recursiveMatchParenthesis(input); // all infix formulas become prefix formulas, e.g. "(X & Y)" become
													// "(and X Y)", and "(X | Y)" become "(or X Y)"
		input = input.replaceAll("[\\{\\[]", "(");
		input = input.replaceAll("[\\}\\]]", ")");
		return input;
	}

	private String inToPrefix(String s) {
		if (s.contains("&")) {
			s = s.replace(" &", "");
			return "and " + s;
		} else if (s.contains("|")) {
			s = s.replace(" |", "");
			return "or " + s;
		} else {
			return s;
		}
	}

	private String recursiveMatchParenthesis(String s) {
		return Pattern.compile("(.*)\\(([^\\(\\)]*)\\)(.*)").matcher(s)
				.replaceAll(match -> this.recursiveMatchParenthesis(
						match.group(1) + "{" + this.inToPrefix(match.group(2)) + "}" + match.group(3)));
	}

	private String stringToPddlFormat(String s) {
		// return Pattern.compile("([^A-Za-z0-9
		// _-])"/*"([\\(\\)\\[\\]\\./,\"])"*/).matcher(s).replaceAll(match -> "_-" +
		// Character.codePointAt(match.group(1), 0) + "-_");
		return s.replace("\"", "_q1q_").replace("/", "_q2q_").replace(".", "_q7q_").replace(",", "_q8q_")
				.replace("(", "_q3q_").replace(")", "_q4q_").replace("[", "_q5q_").replace("]", "_q6q_").toLowerCase();
	}

	private String functorToPddlFormat(Literal l) {
		return (l.getNS().toString().equals("default") ? "" : l.getNS() + "--").toLowerCase()
				+ this.stringToPddlFormat(l.getFunctor());
	}

	/**
	 * Prints a debug message if the debug mode is active
	 * 
	 * @param ts  Transition System
	 * @param un  Unifier
	 * @param msg The debug message to be printed if debug is active
	 */
	private void printDebug(TransitionSystem ts, Unifier un, String msg) {
		if (this.isDebugActive(ts, un)) {
			ts.getAg().getLogger().info("iaPL " + msg);
		}
	}

	private void printDebugPlanner(TransitionSystem ts, Unifier un, String msg) {
		if (this.isDebugPlannerActive(ts, un)) {
			ts.getAg().getLogger().info("iaPLANNER " + msg);
		}
	}

	/**
	 * Returns true if debug mode is active
	 * 
	 * @param ts Transition System
	 * @param un Unifier
	 */
	private boolean isDebugActive(TransitionSystem ts, Unifier un) {
		return ts.getAg().believes(Literal.parseLiteral("pl::debug(true)"), un);
	}

	private boolean isDebugPlannerActive(TransitionSystem ts, Unifier un) {
		return ts.getAg().believes(Literal.parseLiteral("planner::debug(true)"), un);
	}
}