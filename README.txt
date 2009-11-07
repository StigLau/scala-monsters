h1. Intro
Scala Monsters is a based on an old PC game, Beast, but does not intend to recreate all its functionality

The game map consists of a grid with randomly placed boxes, monsters and players. 
The boxes can be pushed, and several can be pushed at once, but they cannot be dragged. Hence, if a box ends up by a wall, it probably cannot be used again.
If a player or monster is in the way of your player pushing the boxes, he will act as a wall and stop the progression of the boxes.
However, if there is a wall behind the monster/player, it will be squished.
Game ends when all opponents/monsters are squished.

The game is envisioned to be hub'n'spoke, where the server manages the games legal state, and the clients are concerned with providing the GUI for displaying the game board, moving pieces, or providing headless monsters.

h1. Requirements
Scala Monsters requires Java and Maven 2 installed

== Running the game ==
From a command line;  
mvn scala:run -DmainClass=no.lau.domain.UI


 -Stig,


Read more:
http://en.wikipedia.org/wiki/Beast_(video_game)
http://www.identicalsoftware.com/ogs/2000/beasts.html
http://twistedmatrix.com/users/acapnotic/wares/code/beasts/


h2. Prioritized todo list:
 * Edit interface of Mortal in some way - if you have tickListener and are killable, the ticklistener should be removed, and you should yourself be removed from the board. Today, your ghost can still be hanging around on the board. 
 * Handle crashes - IE multiple monsters wishing to move to the same space in time
 * implement exploding static walls
 * The game can be extended to be a american football game where one user controls multiple monsters which battle a oppositioning team, bent on mutual distruction with obstacles lying around the field.
 * Capture the flag or similar
 * Monster who can push/squish
 * Property of the game how many cascading moves a monster can create
 * Start to create monster AI

h2. Known bugs
 * Cannot push multiple blocks. Have to implement a pusher "Strength" to indicate the number of available pushes. Although this is not a requirement of Beasts, it could be interesting to implement at a later stage for a different kind of game.
