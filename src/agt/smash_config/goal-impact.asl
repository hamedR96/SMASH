/*
 * Format of goal impact rules:
 * gl::goalImpact(goal, impact, value) :- context.
 * 
 * Where:
 * - "goal": a goal usually represented by a predicate goalName(Var1, Var2)
 * - "impact": positive, neutral, negative
 * - "value": vbc, vbd, vuc, vun, vut, vst, vsa, vsi, vhe, vac, vpd, vpr, vfa, vsp, vss, vtr, vcr, vci, vhu
 * - "context" is a logical formula representing a contextual condition
 */

gl::goalImpact(play(TV, Media), negative, vcr) :- person(User) & workingStatus(User, "OnDuty").

gl::goalImpact(watch(User, Media), negative, vcr) :- person(User) & workingStatus(User, "OnDuty").

gl::goalImpact(notifyUser(Phone,User), negative, vcr) :-person(User) & (userStatus(User,"OnDate") | workingStatus(User, "InMeeting")).

gl::goalImpact(mute(TV), negative, vhe) :- person(User) & type(Phone, "Phone") & deviceOwner(Phone,User) & deviceStatus(Phone, "Standby") & not gl::goalActivation(notifyUser(Phone, User), active, _). 											
											
//other aspect of the context