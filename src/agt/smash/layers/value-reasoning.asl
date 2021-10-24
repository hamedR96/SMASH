
{ include("smash_config/values.asl") }
{ include("smash_config/value-ordering.asl") }

importanceOfValues(List) :- smash.ia.updateValues &          vl::isValueMostImportant(Val1)   & .findall(Val, vl::isValueAsImportantAs(Val1, Val), ListEq1) & .union([Val1], ListEq1, SetEq1) & (.length(SetEq1) > 1 & ValEq1 = [SetEq1] | ValEq1 = [Val1]) & .concat([]   , ValEq1, List1)
	& (vl::isValueLeastImportant(Val1) & List = List1 | vl::isValueJustAbove(Val1, Val2) & .findall(Val, vl::isValueAsImportantAs(Val2, Val), ListEq2) & .union([Val2], ListEq2, SetEq2) & (.length(SetEq2) > 1 & ValEq2 = [SetEq2] | ValEq2 = [Val2]) & .concat(List1, ValEq2, List2))
	& (vl::isValueLeastImportant(Val2) & List = List2 | vl::isValueJustAbove(Val2, Val3) & .findall(Val, vl::isValueAsImportantAs(Val3, Val), ListEq3) & .union([Val3], ListEq3, SetEq3) & (.length(SetEq3) > 1 & ValEq3 = [SetEq3] | ValEq3 = [Val3]) & .concat(List2, ValEq3, List3))
	& (vl::isValueLeastImportant(Val3) & List = List3 | vl::isValueJustAbove(Val3, Val4) & .findall(Val, vl::isValueAsImportantAs(Val4, Val), ListEq4) & .union([Val4], ListEq4, SetEq4) & (.length(SetEq4) > 1 & ValEq4 = [SetEq4] | ValEq4 = [Val4]) & .concat(List3, ValEq4, List4))
	& (vl::isValueLeastImportant(Val4) & List = List4 | vl::isValueJustAbove(Val4, Val5) & .findall(Val, vl::isValueAsImportantAs(Val5, Val), ListEq5) & .union([Val5], ListEq5, SetEq5) & (.length(SetEq5) > 1 & ValEq5 = [SetEq5] | ValEq5 = [Val5]) & .concat(List4, ValEq5, List5))
	& (vl::isValueLeastImportant(Val5) & List = List5 | vl::isValueJustAbove(Val5, Val6) & .findall(Val, vl::isValueAsImportantAs(Val6, Val), ListEq6) & .union([Val6], ListEq6, SetEq6) & (.length(SetEq6) > 1 & ValEq6 = [SetEq6] | ValEq6 = [Val6]) & .concat(List5, ValEq6, List6))
	& (vl::isValueLeastImportant(Val6) & List = List6 | vl::isValueJustAbove(Val6, Val7) & .findall(Val, vl::isValueAsImportantAs(Val7, Val), ListEq7) & .union([Val7], ListEq7, SetEq7) & (.length(SetEq7) > 1 & ValEq7 = [SetEq7] | ValEq7 = [Val7]) & .concat(List6, ValEq7, List7))
	& (vl::isValueLeastImportant(Val7) & List = List7 | vl::isValueJustAbove(Val7, Val8) & .findall(Val, vl::isValueAsImportantAs(Val8, Val), ListEq8) & .union([Val8], ListEq8, SetEq8) & (.length(SetEq8) > 1 & ValEq8 = [SetEq8] | ValEq8 = [Val8]) & .concat(List7, ValEq8, List8))
	& (vl::isValueLeastImportant(Val8) & List = List8 | vl::isValueJustAbove(Val8, Val9) & .findall(Val, vl::isValueAsImportantAs(Val9, Val), ListEq9) & .union([Val9], ListEq9, SetEq9) & (.length(SetEq9) > 1 & ValEq9 = [SetEq9] | ValEq9 = [Val9]) & .concat(List8, ValEq9, List9))
	& (vl::isValueLeastImportant(Val9) & List = List9 | vl::isValueJustAbove(Val9, Val0) & .findall(Val, vl::isValueAsImportantAs(Val0, Val), ListEq0) & .union([Val0], ListEq0, SetEq0) & (.length(SetEq0) > 1 & ValEq0 = [SetEq0] | ValEq0 = [Val0]) & .concat(List9, ValEq0, List0))
	& (vl::isValueLeastImportant(Val0) & List = List0 | vl::isValueJustAbove(Val0, ValA) & .findall(Val, vl::isValueAsImportantAs(ValA, Val), ListEqA) & .union([ValA], ListEqA, SetEqA) & (.length(SetEqA) > 1 & ValEqA = [SetEqA] | ValEqA = [ValA]) & .concat(List0, ValEqA, ListA))
	& (vl::isValueLeastImportant(ValA) & List = ListA | vl::isValueJustAbove(ValA, ValB) & .findall(Val, vl::isValueAsImportantAs(ValB, Val), ListEqB) & .union([ValB], ListEqB, SetEqB) & (.length(SetEqB) > 1 & ValEqB = [SetEqB] | ValEqB = [ValB]) & .concat(ListA, ValEqB, ListB))
	& (vl::isValueLeastImportant(ValB) & List = ListB | vl::isValueJustAbove(ValB, ValC) & .findall(Val, vl::isValueAsImportantAs(ValC, Val), ListEqC) & .union([ValC], ListEqC, SetEqC) & (.length(SetEqC) > 1 & ValEqC = [SetEqC] | ValEqC = [ValC]) & .concat(ListB, ValEqC, ListC))
	& (vl::isValueLeastImportant(ValC) & List = ListC | vl::isValueJustAbove(ValC, ValD) & .findall(Val, vl::isValueAsImportantAs(ValD, Val), ListEqD) & .union([ValD], ListEqD, SetEqD) & (.length(SetEqD) > 1 & ValEqD = [SetEqD] | ValEqD = [ValD]) & .concat(ListC, ValEqD, ListD))
	& (vl::isValueLeastImportant(ValD) & List = ListD | vl::isValueJustAbove(ValD, ValE) & .findall(Val, vl::isValueAsImportantAs(ValE, Val), ListEqE) & .union([ValE], ListEqE, SetEqE) & (.length(SetEqE) > 1 & ValEqE = [SetEqE] | ValEqE = [ValE]) & .concat(ListD, ValEqE, ListE))
	& (vl::isValueLeastImportant(ValE) & List = ListE | vl::isValueJustAbove(ValE, ValF) & .findall(Val, vl::isValueAsImportantAs(ValF, Val), ListEqF) & .union([ValF], ListEqF, SetEqF) & (.length(SetEqF) > 1 & ValEqF = [SetEqF] | ValEqF = [ValF]) & .concat(ListE, ValEqF, ListF))
	& (vl::isValueLeastImportant(ValF) & List = ListF | vl::isValueJustAbove(ValF, ValG) & .findall(Val, vl::isValueAsImportantAs(ValG, Val), ListEqG) & .union([ValG], ListEqG, SetEqG) & (.length(SetEqG) > 1 & ValEqG = [SetEqG] | ValEqG = [ValG]) & .concat(ListF, ValEqG, ListG))
	& (vl::isValueLeastImportant(ValG) & List = ListG | vl::isValueJustAbove(ValG, ValH) & .findall(Val, vl::isValueAsImportantAs(ValH, Val), ListEqH) & .union([ValH], ListEqH, SetEqH) & (.length(SetEqH) > 1 & ValEqH = [SetEqH] | ValEqH = [ValH]) & .concat(ListG, ValEqH, ListH))
	& (vl::isValueLeastImportant(ValH) & List = ListH | vl::isValueJustAbove(ValH, ValI) & .findall(Val, vl::isValueAsImportantAs(ValI, Val), ListEqI) & .union([ValI], ListEqI, SetEqI) & (.length(SetEqI) > 1 & ValEqI = [SetEqI] | ValEqI = [ValI]) & .concat(ListH, ValEqI, ListI))
	& (vl::isValueLeastImportant(ValI) & List = ListI)
	.

isValueMoreImportantThan(Val1, Val2) :- smash.ia.updateValues & vl::isValueMoreImportantThan(Val1, Val2).
vl::isValueMoreImportantThan(Val1, Val2) :- vl::isValueJustAbove(Val1, Val2)
	| (vl::isValueJustAbove(Val1, ValX) & vl::isValueMoreImportantThan(ValX, Val2))
	.

isValueLessImportantThan(Val1, Val2) :- smash.ia.updateValues & vl::isValueLessImportantThan(Val1, Val2).
vl::isValueLessImportantThan(Val1, Val2) :- vl::isValueMoreImportantThan(Val2, Val1).

isValueAsImportantAs(Val1, Val2) :- smash.ia.updateValues & vl::isValueAsImportantAs(Val1, Val2).
vl::isValueAsImportantAs(Val1, Val2) :-
	( vl::isValueJustAbove(Val1, ValBelow) & vl::isValueJustAbove(Val2, ValBelow) & vl::isValueJustAbove(ValTop, Val1) & vl::isValueJustAbove(ValTop, Val2)
	| vl::isValueJustAbove(Val1, ValBelow) & vl::isValueJustAbove(Val2, ValBelow) & vl::isValueMostImportant(Val1)     & vl::isValueMostImportant(Val2)
	| vl::isValueJustAbove(ValTop, Val1)   & vl::isValueJustAbove(ValTop, Val2)   & vl::isValueLeastImportant(Val1)    & vl::isValueLeastImportant(Val2)
	).

isValueMostImportant(Val) :- smash.ia.updateValues & vl::isValueMostImportant(Val). 
vl::isValueMostImportant(Val) :- vl::isValueJustAbove(vbegin, Val).

isValueLeastImportant(Val) :- smash.ia.updateValues & vl::isValueLeastImportant(Val).
vl::isValueLeastImportant(Val) :- vl::isValueJustAbove(Val, vend).

isValueNotImportant(Val) :- smash.ia.updateValues & vl::isValueNotImportant(Val).
vl::isValueNotImportant(Val) :- not vl::isValueJustAbove(Val, _) & not vl::isValueJustAbove(_, Val).

isValueImportant(Val) :- smash.ia.updateValues & vl::isValueImportant(Val).
vl::isValueImportant(Val) :- not vl::isValueNotImportant(Val).
