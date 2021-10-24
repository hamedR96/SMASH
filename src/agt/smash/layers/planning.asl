{ include("smash_config/action-model.asl") }
{ include("smash_config/goal-conditions.asl") }

+gl::state(Goal, Status, Source)[source(_)]
	<-
	if(pl::debug(true)) {.print("PL === Just updated the State for Goal ~~{", Goal, "}~~, with Status ~~{", Status, "}~~, and Source ~~{", Source, "}~~");}
	.

-gl::state(Goal, Status, Source)[source(_)]
	<-
	if(pl::debug(true)) {.print("PL === Just deleted the State for Goal ~~{", Goal, "}~~  with Status ~~{", Status, "}~~  and Source ~~{", Source, "}~~");}
	.

+pl::goalToPlan(Goal, Status)
	<-
	-pl::goalToPlan(Goal, Status);
	?pl::goalAchieveCondition(Goal, Status, AchieveCondition);
	if(pl::debug(true)) {.print("PL === Planning Layer received Goal ~~{", Goal, "}~~  and Status ~~{", Status, "}~~  and Achieve Condition ~~{", AchieveCondition, "}~~");}
	.relevant_plans({+!Goal}, LP);
	if (.empty(LP)) {
		if(pl::debug(true)) {.print("PL === Run planner for Goal ~~{", Goal, "}~~  and Achieve Condition ~~{", AchieveCondition, "}~~");}
    	smash.ia.runPlanner(Goal, AchieveCondition);
    } else {
    	if(pl::debug(true)) {.print("PL === For Goal ~~{", Goal, "}~~ execute a plan already available in agent library ~~{", LP, "}~~");}
    	.add_plan({-!Goal <- if(pl::debug(true)){.print("PL === The plan already available failed, so now run the planner for Goal ~~{", Goal, "}~~  and Achieve Condition ~~{", AchieveCondition, "}~~")}; smash.ia.runPlanner(Goal, AchieveCondition);});
    	!Goal; // Execute goal, if it fails then JaCaMo will run the new plan added just above
    	-gl::state(Goal, Status, _);  // Goal executed with success, now delete old state
    }
	.
