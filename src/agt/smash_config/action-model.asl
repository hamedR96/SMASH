/*
 * Format of action impact rules: al::actionImpact(action, impact, value) :- context.
 *   - "action": an action usually represented by a predicate actionName(Var1, Var2)
 *   - "impact": positive, neutral, negative
 *   - "value": vbc, vbd, vuc, vun, vut, vst, vsa, vsi, vhe, vac, vpd, vpr, vfa, vsp, vss, vtr, vcr, vci, vhu
 *   - "context" is a logical formula representing a contextual condition
 * 
 * ---
 * 
 * Format of action conditions: pl::actionConditions(name, parameters, pre, post).
 *   - "name": the name of the action
 *   - "parameters": an array containing the necessary variables, e.g. [Var1, Var2, Var3]
 *   - "pre" is a logical formula representing a contextual precondition
 *   - "post" is a formula representing the effect of the action in context (beliefs to add and delete [precedeed with "not" operator])
 */

//al::actionImpact(turnOnTv(TV), negative, vhe) :- deviceStatus(TV,"On") | (not sittingOn(User,Furniture) & deviceNeighbor(TV,Furniture)).
pl::actionConditions(setTVStatus,
		[TV, Status],
		not deviceStatus(TV, Status) & type(TV,"TV"),
		deviceStatus(TV, Status) ).

//al::actionImpact(setMode(heaterObj, standby), negative, vhe) :- innerTemp(home, "15").
pl::actionConditions(setPhoneStatus,
		[Phone, Status],
		not deviceStatus(Phone, Status) &  type(Phone,"Phone") ,
		deviceStatus(Phone, Status) ).
