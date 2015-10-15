# Gitman

# An EXCEPTIONal Adventure

## Created by REDMF

There is an epic bug that is ruining everything! The goal of the game is to go through the office, find access cards to open laptops and clone the code on them into into your own repository. Once you have collected all the bits of code, the code will be "compiled". Hopefully this helps you fix the problem! 

### How to run the game

1. Clone repository into your favourite Java IDE
2. Make sure that the repository contains lib and res
3. Make sure that the res folder is a resource (Don't think this is required in some IDE's)
4. Make sure that lwjgl.jar, lwjgl_util.jar, slick-util.jar, PNGDecoder.jar, jogg-0.0.7.jar and jorbis-0.0.15.jar are set as dependencies for the project in your IDE
   - *These files are in the repo under lib > jars*

      *OpenGL is operating system specifc, and so you need to change which natives it will try and use*

      **If you are using eclipse:**
      
         Right click project > Build Path > Configure Build Path<br>
         Under libraries make sure all the jars are added. Add jars > redmf > lib > jars > JAR <br>
         Once the lwjgl.jar is added > press drop down arrow to the left of its name <br>
         Selecte Native library location > Edit > Workspace > redmf > lib > native > **YOUR OS** <br>
         
   
      **List of operating systems**
      
     - freebsd
     - linux
     - macosx
     - openbsd
     - solaris
     - windows
   
   *Otherwise add the following as a VM argument to your run configuration*

  -Djava.library.path=lib/native/OS_TYPE
  
  *So for example if you were on a mac you would put:*
  -Djava.library.path=lib/native/macosx
  
  

### How to run the game in multiplayer

--ECLIPSE--
  
1. Run Main on the server computer with the following arguments.

        Y   - for a Server running fullscreen
        N   - for a Server running small screen

        Take note of the ipaddress output in the console.

2. Run Main on a Client computer with the arguments:

        Y xxx.xxx.xxx.xxx   - for a Client running fullscreen
        N xxx.xxx.xxx.xxx   - for a Client running small screen

        where xxx.xxx.xxx.xxx is the ip address from the server 

3. A player cannot connect to a host when commits have been collected.

### Basic Controls
   - Use ARROW KEYS and ENTER to select options
   - W A S D for Movement
   - H for the Help Screen during the game
   - E to interact with Items
   - I to open Inventory
   - X to delete Items from the Inventory
   - F Save the game at any point


  
*Game created by:*
- *Reuben Puketapu*
- *Ellie Coyle*
- *Divya Patel*
- *Marcel van Workum*
- *Finn Kinnear*

Github Repo : https://github.com/Recursively/redmf<br>
Hosted Javadoc : http://www.marceldev.tk/redmf/doc
