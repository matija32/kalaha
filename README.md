#Kalaha

#How to run it?
* To build, run <code>mvn clean install</code> from the command line
* To start the game, run <code>./mvnw spring-boot:run</code> from the command line
* To play the game, open <code>localhost:8000</code> in your browser

#Scope
This game demonstrates how to use Java8 + SpringBoot + ReactJS to make a simple webapp. 
The game is designed to be played by two players from the same browser window.

#Rules of the game

##Board Setup
Each of the two players has his six pits in front of him. To the right of the six pits,
each player has a larger pit, his Kalaha. In each of the six round pits are put six
stones when the game starts.


##Game Play
The player who begins with the first move picks up all the stones in anyone of his
own six pits, and sows the stones on to the right, one in each of the following
pits, including his own Kalaha. No stones are put in the opponents&#39; Kalaha. If the
player&#39;s last stone lands in his own Kalaha, he gets another turn. This can be
repeated several times before it&#39;s the other player&#39;s turn.

##Capturing Stones
During the game the pits are emptied on both sides. Always when the last stone
lands in an own empty pit, the player captures his own stone and all stones in the
opposite pit (the other players&#39; pit) and puts them in his own Kalaha.

##The Game Ends
The game is over as soon as one of the sides run out of stones. The player who
still has stones in his pits keeps them and puts them in his/hers Kalaha. Winner
of the game is the player who has the most stones in his Kalaha.

