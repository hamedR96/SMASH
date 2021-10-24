
//PERSON RULES

workingStatus(X, "OffDuty"):- not workingStatus(X, "OnDuty").

userStatus(X,"Alone"):- not userStatus(X,"Hangingout") & not userStatus(X,"OnDate").

///PHONE RULES

deviceStatus(X,"Off"):- not (deviceStatus(X,"Ringing") | deviceStatus(X,"Standby") | deviceStatus(X,"ReceivingCall")
										|  deviceStatus(X,"Busy") |  deviceStatus(X,"Calling") | deviceStatus(X,"Voicemail")| 
										deviceStatus(X,"Mute") | deviceStatus(X,"Unmute") |  deviceStatus(X,"Playing")). 

deviceMode(X,"Standard") :- not deviceMode(X,"Voicemail") | not deviceMode(X,"Airplane").

///SOFA RULES

sittingOn(X,Y)[useInPlanner]:- sensing(Y,X) &  type(Y, "Furniture").

personLocation(X,Y):- sensing(Z,X) & deviceLocation(Z,Y).

standUp(X):- not sittingtOn(X,Y).

//Other Rules inside(User, home)[useInPlanner] :- .

deviceNeighbor(X,Y)[useInPlanner]:- deviceLocation(X,Z) & deviceLocation(Y,Z). 
watchingTV(User,TV)[useInPlanner] :- sittingOn(User,Furniture) & deviceNeighbor(Furniture,TV) & deviceStatus(TV,"Playing").