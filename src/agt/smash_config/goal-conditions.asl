/*
 * Format of goal achieve condition:
 * pl::goalAchieveCondition(goal, status, context).
 * 
 * Where:
 * - "goal": a goal usually represented by a predicate goalName(Var1, Var2)
 * - "status": active, inactive, dropped
 * - "context" is a logical formula representing a contextual condition
 */

pl::goalAchieveCondition(turnOn(TV), active,  deviceStatus(TV,"Standby")).

pl::goalAchieveCondition(turnOff(TV), active,  deviceStatus(TV,"Off")).

pl::goalAchieveCondition(play(TV,Media), active, deviceStatus(TV,"Playing")).

pl::goalAchieveCondition(watch(User,Media), active, deviceStatus(TV,"Playing")).

pl::goalAchieveCondition(notifyUser(Phone,User), active, deviceStatus(Phone,"Ringing")). 

pl::goalAchieveCondition(mute(TV), active, deviceStatus(TV,"Mute")). 

pl::goalAchieveCondition(unmute(TV), active, deviceStatus(TV,"Playing")). 

pl::goalAchieveCondition(voicemail(Phone,User), active, deviceStatus(Phone,"Voicemail")). 
