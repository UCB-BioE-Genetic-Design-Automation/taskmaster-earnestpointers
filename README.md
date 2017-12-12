PA System
================
Bryant Luong
12/12/2017

PA System
---------

PA System is a vizualization tool for the suite of automation tools we've been building in BioE 134. The name, PA System, has two meanings. The PA stands for PipetteAid because it is supposed to simulate a protocol and teach the user what to do next. The PA also stands for Public Announcement because my goal was to build a system that's a live dashboard for the user. This means PA System not only tells but also shows the user what to do.

### Features

Currently, PA System only supports the AddContainer and Transfer tasks of the Semiprotocol class.

For AddContainer, only 96-well PCR plates are supported. This means the grid sizes are to scale. When you add a plate to the bench, you can place your plate on top of the grid and make sure the corners are against the display's corner. This minimizes plate movement when a user actually uses the system.

When you start the application, you will need to choose **Step-by-Step** or **Play**. In **Step-by-Step** mode, you can walk through your protocol at your pace. To go to the next step, just click on the "next step" button. In **Play** mode, click on and hold the **Play** button until the UI changes and begins playing the protocol.

In both modes, you will see the remaining number of steps in the protocol. The task of the current step in the protocol is always shown at the bottom of the interface.

Lastly, the application does not detect display yet. It has only been tested on a 13-inch Macbook.

Setup
-----

1.  Download this repo into your machine.
2.  Open it in an IDE.
3.  Run the application by running the Main class in PA.System/controller.
4.  By default, **test.txt** is the loaded protocol and it is in semiprotocol/data.
5.  You can load your own semiprotocol by changing the **pathToSemiprotocol** variable in the Main class. It is the first variable in the class.

Closing Thoughts
----------------

There are many things I did not get to. Some are:

1.  A **previous step** button.
2.  Write appropriate classes for each container, which are just smaller GridPanes of the Plate class.
3.  Write UI code to handle RemoveContainer, Dispense, and Multichannel tasks.
4.  Different UIs for **Step-by-Step** and **Play**. For example, in **Play** I would leave the wells with liquid highlighted. In **Step-by-Step**, there would be a dashboard to the side of the next button that shows the well, the reagent in the well, and the location of the well.
5.  To fit 3 PCR plates or 2 PCR plates and tubes, I can rotate the plates 90 degrees.
6.  On startup, open the OS system file explorer so users tell the application the path to the semiprotocol.

Ideas beget ideas! *sighs* Alrighty. Take her for a spin!
