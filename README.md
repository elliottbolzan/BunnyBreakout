BunnyBreakout
=============
An individual project for Duke's Computer Science 308 (Design and Implementation). For this first project, I individually built a rabbit-themed breakout game.

![](./images/1)

![](./images/2)

**File used to start / test the project:** BunnyBreakout.java.
**Data or resource files required by the project:**

 - Level files (text files): 1, 2, and 3. Files are configurations for levels. In these files, the 1 character represents a carrot block, the 2 character represents a soil block, and the 3 character represents a grass block. Whitespace represents space in the level.
 - Images: 1.gif, 2.gif, 3.gif, 2_broken.gif, 3_broken.gif, 3_broken_twice.gif, bunny.gif, power_1.gif, power_2.gif, power_3.gif.
 - Music: song.mp3.

**Key inputs that may be used during the program:**

 - Left and right arrows, to move top hat in the corresponding directions.
 - Space bar: extend top hat the width of the screen.
 - 1, 2, or 3: go to corresponding level.
 - C: remove carrots.
 - S: remove soil.
 - G: remove grass.

**Known bugs or problems with functionality:**

 - It occasionally seems as if the bunny flies through some blocks at high speeds. While the bunny is acting exactly as it should, bouncing quickly from block to block, for the user, it may seem like something is amiss. To try to resolve this, I have imposed a short time-out (depending on the frame rate, but around 40ms in general) between block destructions.

**Impressions of the assignment:**

I found the assignment interesting and relatively fun to design. I probably should have worked a bit less on it, hour-wise. I'm not certain much could be made to improve the assignment itself: many of the responsibilities and decisions are given to the coder, and as such, the improvements can be made by the programmer. I appreciated how our work in the first recitation was seminal to our project. This made the task less daunting, while leaving us to do most of the work, and I felt that was a good combination.
One thing I was unable to find for the assignment on the website were the Formatting Rules, the CheckStyle rules, and the CleanUp rules. Maybe these should be added in the future.
