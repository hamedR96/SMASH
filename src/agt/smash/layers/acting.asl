
+al::planToAct(PlanTerm, PlanLabel)
	<-
	-al::planToAct(PlanTerm, PlanLabel);
	if(al::debug(true)) {.print("AL === Acting Layer received the Plan ~~{", PlanTerm, "}~~");}
	if(smash.ia.actingCanExecute(PlanLabel)) {
		!!al::deletePlanFromLibrary(PlanLabel);
		+PlanLabel;
		!PlanTerm;
		if(al::debug(true)) {.print("AL === Acting successfully executed the Plan with Label ~~{", PlanLabel, "}~~ and Term ~~{", PlanTerm, "}~~");}
	} else {
		if(al::debug(true)) {.print("AL === Acting did NOT allow the execution of Plan with Label ~~{", PlanLabel, "}~~ and Term ~~{", PlanTerm, "}~~");}
	}
	-gl::state(PlanTerm, _, _);
	.

+!al::deletePlanFromLibrary(PlanLabel)
	<-
	.wait(1000);
	if(al::debug(true)) {.print("AL === Deleting the Plan with Label ~~{", PlanLabel, "}~~");}
	.remove_plan(PlanLabel);
	-PlanLabel;
	.
