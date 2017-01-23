Design Choices
=============

I apologize for submitting this design file a few hours late. I was under the impression it was part of the Analysis part. My bad!

**High-level design goals of BunnyBreakout:**
In BunnyBreakout, I sought to have one controller class that starts the program, creates the user,  displays his status, and presents different views (levels, splash screen, and messages): *BunnyBreakout*. From there, it became clear I needed more classes:

 - A *User* class. Contains the user's points, lives, and current level.
 - A *MessageView* class. Presents a message (either the splash screen or a different one). To retain flexibility, I created a *Message* class, to encapsulate different messages. To shorten code, I created a *CustomText* class that served as the stylistic text base for the project.
 - A *Status* class. Tells the user how he is doing. 
 - A *Level* class. This class contains a level, but because of its complexity, requires smaller classes:
	 - *Block*: represents one block in the level.
	 - *PowerUp*: represents one power-up in the level.
	 - *Bunny*: represents one bunny in the level.
	 - *TopHat*: represents one top hat in the level.

Finally, to control the entire project, I used a *Settings* class with a number of constant values. I felt it made sense to determine settings from one centralized location. That's what we do when we change our mobile phone or computer settings, so I believe it's logical to do the same thing when we design programs. 

**Adding new features:**
Minor design choices can be changed using the constants in the *Settings* class. Here is a general workflow, with optional steps, for adding larger new features:

 - If your feature necessitates a new class, create it.
 - If your feature directly concerns elements like the *TopHat*, *PowerUp*'s or *Block*'s, modify the corresponding classes directly. 
 - If your feature pertains to a level or the general game play, modify the *Level* class. 
	 - Depending on your feature, initialize it from the constructor.
	 - If your feature requires a verification at every time step, modify the *pointsGainedIn* or *bunnyCollisionCheck* methods. 
	 - Add any required function to the class.
 - If your feature is more general and involves presenting new views, modify the *BunnyBreakout* class. 
	 - Create the scene you wish to present.
	 - Switch to this scene by using the *attachScene* function designed for this purpose.
	 - Use the key code detection methods to control user input and determine navigation within the application.

**Major design choices, including trade-offs (i.e., pros and cons):**

 - The most major design choice is giving the *Level* class control over the *TopHat* and the *Bunny*. One might argue these are level-independent and as such should be initialized in *BunnyBreakout* instead, but there are several reasons why I chose to create them from *Level*:
	 - Code becomes far shorter and simpler when these elements belong to *Level*.
	 - *BunnyBreakout* becomes shorter and simpler in this scenario. The class can be used exclusively for "administrative" purposes: settings views, detecting key codes, creating the user, starting the application. I believe this provides better flexibility and modularity.
	 - There is one down-side, however: *Level* as a class becomes longer and larger in scope. 
 -  I chose to intercept key codes from *BunnyBreakout*, the main class. Ideally, I would have preferred to extract that code into a new class, to free up *BunnyBreakout*. Doing so, however, caused problems: *BunnyBreakout* makes the decisions and orders the other classes. Moving the key code detection to a new class would not solve this, and much of the logic would have had to remain in *BunnyBreakout* regardless. 
 - I chose to use *BunnyBreakout* as both a launcher for the application and the view presenter. I could have split this class into two, with one class being used solely for launching the application. In a larger project, I would certainly have done so. In this project, I felt this was overkill: in our scenario, the logic for presenting views and configuring the user was short enough to be combined with the launching code. This is definitely something I'll keep in mind however for following projects.

**Assumptions or decisions made to simplify or resolve ambiguities in functionality:**

 - Sometimes, the bunny is too fast and seems to fly through blocks quickly. It is acting as it should, but the user might be confused by this quick destruction of blocks. I assumed that despite this "correct" behavior on the bunny's part, I should fix this behavior so that it appears more normal to the user. In order to do so, I added a timeout to each bunny: after destroying a block, a bunny has a very short time window during which it cannot destroy more blocks. This adds complexity and is almost counter-intuitive, but I feel necessary for the game to function as the user would expect it to.
 - I assumed that the splash screen should be shown once per game, on startup.
 - I assumed that the cheat codes should be given to the user during the splash screen: this might not be the case in a real, commercially-sold game, but I felt for our purposes it was a useful addition.
 - I assumed that a user's lives should not reset after passing a level. I thought this would make the user manage his lives better, and keep the user engaged in each level - even the easier ones. If s/he lost on level 3, s/he would then be more careful on the easier levels 1 and 2 to retain their original total of lives.