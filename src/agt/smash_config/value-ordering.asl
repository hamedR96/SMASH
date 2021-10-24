/*
 * Format of value-ordering rules:
 * vl::valueOrdering(order, value1, value2) :- context.
 * 
 * Where:
 * - "order": valueAsImportantAs, valueMoreImportantThan, valueLessImportant, valueMostImportant, valueLeastImportant, valueNotImportant
 * - "value1" and "value2": vbc, vbd, vuc, vun, vut, vst, vsa, vsi, vhe, vac, vpd, vpr, vfa, vsp, vss, vtr, vcr, vci, vhu
 * - "context" is a logical formula representing a contextual condition
 */

vl::valueOrdering(valueMostImportant, vbc, _) :- person(User) & (not userStatus(User, "OnDate") | not workingStatus(User, "InMeeting")).

vl::valueOrdering(valueMostImportant, vcr, _) :- person(User) & workingStatus(User, "InMeeting").

vl::valueOrdering(valueMostImportant, vhe, _) :- person(User) & userStatus(User, "OnDate").

vl::valueOrdering(valueMoreImportantThan, vac, vhe) :- person(User) & (workingStatus(User, "OnDuty") |
													(userStatus(User,"Alone") &	workingEvent(User, "Tomorrow"))).

vl::valueOrdering(valueLessImportant, vcr, vhe) :- person(User) & workingStatus(User, "OffDuty").
