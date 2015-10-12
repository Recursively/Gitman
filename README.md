# Gitman

# An EXCEPTIONal Adventure

## Created by REDMF

There is an epic bug that is ruining everything! The goal of the game is to go through the office, find laptops and clone the code on them into into your own repository. Once you have collected all the bits of code, "compile" the code and then "run" it. Hopefully it helps you fix the problem! 

(Explaination of basic controls & goal)

(Information on multiplayer)

### How to run the game

1. Clone repository into your favourite Java IDE
2. Make sure that the repository contains lib and res
3. Make sure that the res folder is a resource (**Should include link to how to do this here**)
4. Make sure that lwjgl.jar, lwjgl_util.jar, slick-util.jar, PNGDecoder.jar, jogg-0.0.7.jar and jorbis-0.0.15.jar are set as dependencies for the project in your IDE
   - *These files are in the repo under lib > jars*
5. This is the hard part:
  * OpenGL is operating system specifc, and so you need to change which natives it will try and use
  * To do this you need to edit your games run configuration
  * The configuration is as follows:
  
  -Djava.library.path=lib/native/OS_TYPE
  
  *So for example if you were on a mac you would put:*
  -Djava.library.path=lib/native/macosx
  
  **List of operating systems**
  - freebsd
  - linux
  - macosx
  - openbsd
  - solaris
  - windows
  
   **Alternately, if you are using eclipse**
   - You can simply add the corresponding native as a native for the lwjgl.jar in your libraries build path
  
  
*Game created by:*
- *Ellie Coyle*
- *Divya Patel*
- *Finn Kinnear*
- *Reuben Puketapu*
- *Marcel van Workum*
