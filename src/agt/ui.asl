// Agent assistant in project smash_ag

{ include("smash/include.asl") }
{ include("context/rules.asl") }	


+receivedMsg(Msg) : true
	<-
	.print(Msg);
	-receivedMsg(Msg);
	.

+updateSMASH: true <- smash.ia.updateGoals. 						
/////////////////////
// a note for next generations: we might define a Articact that represent the digital twin of people.

+addPerson(X,Y,Z): true <- .print(X, " that is ",Y," and ", Z," is now added to the system");
									 +person(X);
									 -userStatus(X,_);
									 +userStatus(X,Y);
									 -workingStatus(X,_);
									 +workingStatus(X, Z);
									// smash.ia.updateGoals;
									.	
+addEvent(X,Y,Z): true  <- .print(X, " has a ",Y," ", Z);
									 +hasWorkingEvent(X, Z);
								//	 smash.ia.updateGoals;
									.				
									
+addPhone(A,B,C,D,E): true  <- .print("Phone ",A, " that belongs to ",B," and is Located in ", C," with Status of ",D, " is added to the system");
									makeArtifact(A,"devices.ArtPhone",[A,B,C,D,E],Id);
        						    focus(Id);
									 .

+addFurniture(A,B,C,D,E): true  <- .print("Furniture ",A, " that is Located in ", B, " is added to the system");
									makeArtifact(A,"devices.ArtSofa",[A,B,C,D,E],Id);
        						    focus(Id);
									.
+addFurniture(A,B): true  <- .print("Furniture ",A, " that is Located in ", B, " is added to the system");
									makeArtifact(A,"devices.ArtSofa",[A,B],Id);
        						    focus(Id);
									.

+addTV(A,B,C,D,E): true  <- .print("TV ",A, " that belongs to ",B," is Located in ", C, " with Status of ",D, " is added to the system");
									makeArtifact(A,"devices.ArtTV",[A,B,C,D,E],Id);
        						    focus(Id)
        						.
+addTV(A,B,C,D): true  <- .print("TV ",A, " that belongs to ",B," is Located in ", C, " with Status of ",D, " is added to the system");
									makeArtifact(A,"devices.ArtTV",[A,B,C,D],Id);
        						    focus(Id)
        						.
///

+scenario : true  <-    .print("The main Scenario is Launched!");						
						+addPerson("Max","Hangingout","OffDuty");
						+addPerson("Sarah","Hangingout","OffDuty");					
						+addEvent("Max","Meeting","Tomorrow");
						+addPhone("maxPhone","Max","LivingRoom","Standby","Standard");
						+addFurniture("maxSofa","LivingRoom"/*,"tcp://192.168.1.21:3883", "homein/#", "location-abstraction"*/);
						+addTV("maxTV","Max","LivingRoom","Off"/*,"http://192.168.1.21:8080"*/);
  				    	
  				    	.print("\nMax asks to watch Canal+\n");
  				    	!!gl::watch("Max","Canal+");
  				    	.wait(3000);
  				    	
  				    	.print("\nMax sits down on sofa\n");
  				    	sittingDown("Max");
  						.wait(3000);
  						
  						.print("\nJames from work is calling\n");
  						receivingCall("James", "Work");
  						.wait(5000);
  						
  						.print("\nMom from family is calling\n");
  						receivingCall("Mom", "Family");
  						.wait(3000);
  						
  						.print("\nMax answers the call\n");
  						answeringCall;
  						.wait(3000);
  						
  						.print("\nMax rejects call\n");
  						rejectingCall;
						.

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
