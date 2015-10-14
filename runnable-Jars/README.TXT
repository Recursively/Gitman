How to run the jar

Navigate to your operating systems respective directory.

The native files need to be in the same directory as the jar, this should be done for you.

    run the following command to run a windowed server by default:
    (note the servers ip address will be printed to the console)

    java -jar redmf.jar

    The jar takes optional arguments ->

    [N/Y] -> Fullscreen enabled [IPAddress] -> To connect to a server

    So to run a full screen client:

    java -jar redmf.jar Y XXX.XXX.XXX.XXX.XXX (Where XXX.XXX.XXX.XXX is the ip address of the server)

    To run a windowed server:

    java -jar redmf.jar N