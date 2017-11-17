# Smart Rail

SmartRail simulation created for CS 351 at the University of New Mexico.

## Getting Started

**NOTE:** IF the images do not load then right click on the resources directory (within intelliJ) and go to "mark directory as" and choose "resources root". This should add all the resources to the class path. 

To get a playable version of the game all that is needed is to download the .jar file and
double click to launch the game. The source code is included in the jar and seperately 
in this repository if needed. The doc folder contains a powerpoint of the class hierarchy and it
also includes the major methods that are called from instantiated objects. 

## Simulation

* The simulation is simple. Launch the program to get it started.
* From the main menu select the configuration file that you would like to use. 
* Now you should be on the simulation screen.
* To begin choose a departure staion by clicking on your first station.
* Next choose a destination station by clicking on it. Once both stations are chosen the train will be created, it will find the path and then it will automatically begin traversing the path that it found.

**NOTES:** 
* You can only click on blue stations and cannot click on dead ends.
* This is a one train simulation therefore, a train must fully exit the display before you create another train.
* A train cannot have the same destination as its departure (destination != departure).

## Making A Configuration File

* Configuration files are .txt files.

**Lanes**
* Lanes in the .txt file are represented in a line. Each line corresponds to a new lane.
* There can be no empty lanes.
* A lane must start with a type of station and end with a type of station. Refer to the next segment as to the functionality of the two types of stations.

**Stations**
* Stations are represented in the .txt file as the 'S' character, which is a default station.
* A second type of station is also implemented to allow for different number of stations on the left and the right. This station type is known as a dead end, represented by the 'D' character in the .txt file. A dead end is effectively "no station" at all. No train can go there.
* A station can only be connected on one side. A station can only be connected to a track.

**Tracks**
* Tracks are represented by the '=' character.
* Tracks do not have any restrictions except they must go in between two stations of any type.
* Example lane: 
  ```
  S=========S
       or
  D=====S
       or
  S===========D
  ```
  
**Lights**
* Lights are represented by the 'l' (lower case L) character.
* Lights have one restriction. A light has to be between two tracks.
* The only configuration for a light is as follows:
  ```
  S======l=====S
  ```
    
**Switches**
* There are two types of switches. Right switches are represented by the 'R' character and left switches are represented by the 'L' character.
* Switches ALWAYS have to have a track on both sides. 
* Right switches connect one lane down and one character to the right.
* Left swithces connect one lane down and one character to the left.
* Right switches can only connect to Left switces and vice versa. (No R to R or L to L connections)
* Switches can only connect the one switche at a time.
* **Example 1:**
  S=R==D
  ```
  S==L====S    (Note that when R connects to L, L in turn connects to R. So L can no longer be connected to anything else below it.)
      or
  S===L==S
  S==R==D      (Note that when L connects to R, R in turn connects to L. So R can no longer be connected to anything else below it.)
  ```
  
  **This following scenario is not possible:**
  ```
  S===L==S
  S==R===S (Note that the top L has connected to R already. R has connected to the top L in return. So the bottom L cannot connect to R)
  S===L===S
  ```
  
* **Example 2 (Multiple Switches in a lane):**
  ```
  S===R==L==S
  S=R==L==R==S
  S==L======D
  ```
  
**NOTE:** There is a config file named "config_TA" in the resources directory for the TAs to change and well as "config3" if needed. Refer to "config1" and "config2" in the resources directory for further examples of working configurations or email me at akash1196@unm.edu for further explanation.

## Prerequisites
All dependencies are included in the .jar file. All that's needed is:

```
jdk 1.8
```

## Known Issues

* Multiple train scenarios are not supported.

## Authors

* **Akash Patel**
* **Gabriel Urbaitis**

## Acknowledgments

* No code was used as is in this project.
