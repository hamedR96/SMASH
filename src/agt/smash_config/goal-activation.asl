/*
 * Format of goal-activation rules:
 * gl::goalActivation(goal, status, source) :- context.&
 * 
 * Where:
 * - "goal": a goal usually represented by a predicate goalName(Var1, Var2)
 * - "status": active, inactive, dropped
 * - "source": user, self
 * - "context" is a logical formula representing a contextual condition
 */

gl::goalActivation(Goal, active, user) :- gl::state(Goal, waiting, user).

gl::goalActivation(turnOn(TV), active,self) :- type(TV, "TV") & deviceStatus(TV,"Off") & gl::state(watch(User, Media), active, _).

gl::goalActivation(play(TV,Media), active,self) :- sittingOn(User,Furniture) & deviceNeighbor(TV,Furniture) & gl::state(watch(User, Media), active, _).

gl::goalActivation(watch(User,Media), dropped,self) :- sittingOn(User,Furniture) & deviceNeighbor(TV,Furniture) & gl::goalActivation(play(TV,Media), active,self).
																								   
gl::goalActivation(mute(TV), active,self) :- gl::goalActivation(notifyUser(Phone, User), active, _) & deviceStatus(TV,"Playing").

gl::goalActivation(unmute(TV), active, self):- sittingOn(User, Furniture) &  deviceNeighbor(TV, Furniture) & deviceStatus(TV, "Mute") & deviceStatus(Phone, "Standby").

gl::goalActivation(notifyUser(Phone, User), active, self):- watchingTV(User, TV) & deviceStatus(Phone, "ReceivingCall") & deviceOwner(Phone, User) 
   &
  (
  	(isValueMoreImportantThan(vbc, vhe) & callerType(Y, "Family") & incomingCall(Phone,Y)) |
    (isValueMoreImportantThan(vcr, vhe) & callerType(X, "Work") & incomingCall(Phone,X))
    )   
  .

gl::goalActivation(voicemail(Phone, User), active, self):- watchingTV(User, TV) & deviceStatus(Phone, "ReceivingCall") & deviceOwner(Phone, User)  
     & 
  (
  (callerType(Z, "Work") & isValueMoreImportantThan(vhe, vcr)& incomingCall(Phone,Z)) | 
  (callerType(E, "Family") & isValueMoreImportantThan(vhe, vbc)& incomingCall(Phone,E))
  ) 
  .