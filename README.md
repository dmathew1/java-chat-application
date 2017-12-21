Day 1: I am going with the client server design first to try and understand how it all works there, then migrate
over to a p2p design and then finally a p2p where the client first connects to the server and then the server dispatches
the connection directly to both clients

Server:
requires a serverSocket for the server to run on
requires to always be on, always listening for requests
so when a connection is made on the serverSocket,
a new socket is born with a direct connection to the client

Client:
requires a socket for the client to run on


Testing:
Created an ExecutorService with a fixed threadpool of 15 and ran the one server
and issues each thread to go 15 times.
Going to try 50 requests on 15 threads and see how it goes ---> went well
Going to try 100 requests on 15 threads and see how it goes ---> request got dispatched more than once on different threads
race conditions....


SideNotes:
A bufferedReader wraps a Reader object and allows for proper buffering of bytes
An inputStreamReader allows for bytes to come in the chars

Questions:
How to spawn threads from the server to handle requests from multiple clients

Issues:
Having a problem trying to spawn many threads on the server; i guess this is why the c10k problem exists


================================================

Day 2: Be able to serve up a response back to a browser with the server running; can get a GET request

Server: upon each client connecting, pass off task to worker thread
where the worker thread will make the connection directly to the client (workerthread --> client)


so ServerSocket --> Socket means that we can get the input and outstream from each Socket and if we aggregate
all the output streams then we can effectively talk to all clients at once mimicking the "chatroom" experience

Issues: blocking from the bufferedreader from client and server and the bufferedreader turns null

Notes: If youre sending information over and its not being read as a line (br.readline()) then it will block which it did
for both client and server

*** apparently even with the readline and "\n" it still doesnt work, had to use
read() != -1 to do my checks

Issues: Had to remove the blocking conditions on server threads and client threads because they both just deadlock on bufferedreader


================================================
Day 3: Be able to send a GET request to python server and output response back

Issues: Following HTTP protocol on the java server at this low level

Notes: pw.write("GET / HTTP/1.1\r\n\r\n"); --> I did not realize HTTP specs required twice carriage returns and new lines

c10k problem is for 10k concurrent requests and not 10k sequential requests

So Java client can now send 10k requests to a flask server

I was only reading the first line of the response and when adding more readLines i can get the whole response (header and body)
however I run into an issue where it tries to make multiple requests in one run

BufferedReader terminates upon either EOF or socket closing which if you do System.out.println() on each line
youll lose the window of time for the socket to stay open
so resolution is append to a variable
and upon socket termination print the response variable
