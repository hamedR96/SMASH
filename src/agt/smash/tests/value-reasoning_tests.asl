!vl::tests.

+!vl::tests : not vl::tests(true).

+!vl::tests : vl::tests(true) <-
	.print("Tests VL are ON");
	
	// Testing relative importance operations
	
	?isValueAsImportantAs(vbc, vun);
	
	?isValueAsImportantAs(vbc, vbc);
	
	?isValueMoreImportantThan(vbc, vpd);
	
	?isValueLessImportantThan(vbd, vbc);
	
	?isValueMostImportant(vac);
	
	//?isValueLeastImportant(vpd);
	
	?isValueAsImportantAs(vun, vbc);
	
	?isValueAsImportantAs(vss, vut);
	
	?isValueAsImportantAs(vtr, vss);
	
	?isValueMoreImportantThan(vhe, vss);
	
	?isValueAsImportantAs(vhe, vhe);
	
	?isValueAsImportantAs(vsi, vpd);
	
	?isValueLessImportantThan(vpd, vun);
	
	?isValueMoreImportantThan(vhe, vfa);
	
	?isValueLessImportantThan(vst, vhe);
	
	?isValueAsImportantAs(vsa, vfa);

	// Testing calculation of importance of values using value-ordering rules
	
	?importanceOfValues(IVb);
	if(vl::debug(true)) {.print("BEGIN ", IVb);}
	
	
	+workStatus(iaco, holidays);
	?importanceOfValues(IVe);
	
	+go(iaco, meeting);
	+go(boss, meeting);
	
	+day(meeting, tomorrow);
	
	?importanceOfValues(IVe);
	if(vl::debug(true)) {.print("_END_ ", IVe);}
	
	.
