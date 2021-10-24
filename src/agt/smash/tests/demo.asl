// Examples of rules:
insidePlace(HumanUser, homeSpace) :- onObject(HumanUser, sofaObject) & not (inRoom(HumanUser, livingRoom) | not inRoom(HumanUser, livingRoom)).
inside(User, home)[useInPlanner] :- .member(User, [iaco, hamed, john, maria]).
status(X)[useInPlanner] :- .member(X, ["On", "Off", "Playing", "Mute"]).

!demo::demo.

+!demo::demo : not demo::demo(true).

+!demo::demo : demo::demo(true) & Delay = 10000 <-
	.print("DEMO is ON");
	
	// some terms that should come from ontology (context) -- necessary for the planner
	+term("ai_movie");
	+term(tv);
	+term(phone);
	+term("standby");
	+term("active");
	+term(silence);
	+term(on);
	+term(off);
	+term(mom);
	+term(boss);
	+term(work);
	+term(family);
	+term("15");
	+term("20");
	+term("25");
	
	// some predicates that should come from ontology (context) -- necessary for the planner
	+isMemberOf(a,a);
	+isPlaying(a,a);
	+calling(a,a);
	+ringing(a,a);
	
	//------------------------------//
	
	.print("\n\n# User went outside, heater is active and tv is off...\n");
	-+at(iaco, outside);
	+gl::state(warmUpHome("heaterObj", "25"), active, self);
	//-+inMode("heaterObj", "active");
	-+hasTemp("heaterObj", "15");
	-+innerTemp(home, "25");
	-+hasStatus(tv, off);
	smash.ia.updateGoals;
	.wait(Delay);
	//.wait(999999);
	
	//------------------------------//
	
	.my_name(Ag);
	.print("\n\n# Work meeting scheduled for tomorrow...\n");
	-+scheduledFor(workMeeting, tomorrow); // can be "tomorrow" or something else, like "nextWeek"
	-+goTo(iaco, workMeeting);
	smash.ia.updateGoals;
	.wait(Delay);
	
	//------------------------------//
	
	.print("\n\n# User is at home...\n");
	-+at(iaco, home);
	-+inMode(heater, standby);
	smash.ia.updateGoals;
	.wait(Delay);
	//.wait(999999999);
	//------------------------------//
	
	.print("\n\n# User asks movie just after turning tv on...\n");
	+on(iaco, sofa);
	-+hasStatus(tv, on);
	-+hasTemp(heaterObj, temp20c);
	!gl::watch(iaco, "ai_movie");
	.wait(Delay);
	
	//------------------------------//
	
	.print("\n\n# User receives a call from boss...\n");
	-+isPlaying(tv, ai_movie);
	-calling(_, _);
	+calling(boss, iaco);
	smash.ia.updateGoals;
	.wait(Delay);
	
	//------------------------------//
	
	.


