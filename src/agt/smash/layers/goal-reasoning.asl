{ include("smash_config/goal-activation.asl") }
{ include("smash_config/goal-impact.asl") }

/*
 * OBS: Every goal that is given by the user must start by "gl::" in order to be processed by the goal-reasoning layer
 */
+!gl::GivenGoal
	<-
	GivenGoal = Goal[source(_)];
	if(gl::debug(true)) {.print("GL === Agent received and will process the Goal ~~{", Goal, "}~~");}
	+gl::state(Goal, waiting, user);
	smash.ia.updateGoals;
	.

+!smash::GivenGoal
	<-
	!!gl::GivenGoal
	.