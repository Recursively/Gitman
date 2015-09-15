# REDMF

## A 3D adventure game

(Need a quick intro to the story/plot here)

(Explaination of basic controls & goal)

(Information on multiplayer)

### How to run the game

1. Clone repository into your favourite Java IDE
2. Make sure that the repository contains lib, lwjgl-2.9.3 and res
3. Make sure that the res folder is a resource (**Should include link to how to do this here**)
4. Make sure that lwjgl.jar, lwjgl_util.jar, slick-util.jar and PNGDecoder.jar are set as dependencies for the project in your IDE
   - *These files are in the repo under lwjgl-2.9.3 > jar and lib > jars*
5. This is the hard part:
  * OpenGL is operating system specifc, and so you need to change which natives it will try and use
  * To do this you need to edit your games run configuration
  * The configuration is as follows:
  
  -Djava.library.path=lwjgl-2.9.3/native/OS_TYPE
  
  *So for example if you were on a mac you would put:*
  -Djava.library.path=lwjgl-2.9.3/native/macosx
  
  **List of operating systems**
  - freebsd
  - linux
  - macosx
  - openbsd
  - solaris
  - windows
  
  
*Game created by:*
- *Ellie Coyle*
- *Divya Patel*
- *Finn Kinnear*
- *Reuben Puketapu*
- *Marcel van Workum*
