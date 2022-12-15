import java.util.*;
import java.net.*;
import java.io.*;

public class GWackChannel {

    ServerSocket serverSock;
    LinkedList<String> users = new LinkedList<String>();
    LinkedList<Socket> sockets = new LinkedList<Socket>();

    public GWackChannel(int port) {

        try {
            serverSock = new ServerSocket(port);
        }
        catch(Exception e) {
            System.err.println("Cannot establish server socket");
            System.exit(1);
        }
        
    }

    public void serve(){
        while(true){
            try {
                //accept incoming connection
                sockets.add(serverSock.accept());
                
                //start the thread
                (new ClientHandler(sockets.getLast())).start();
                
                //continue looping
            }
            catch (Exception e) {} //exit serve if exception
        }
    }

    public void sendUserList() {
        // Send user list to all clients
        for (Socket s : sockets) {
            try {
                PrintWriter pw = new PrintWriter(s.getOutputStream());
                pw.println("START_CLIENT_LIST");
                for (String user : users) {
                    pw.println(user);
                }
                pw.println("END_CLIENT_LIST");
                pw.flush();
            }
            catch (Exception e) {
                System.err.println("Error sending user list");
            }
        }
    }

    private class ClientHandler extends Thread{

        Socket sock;
        public ClientHandler(Socket sock) {
            this.sock = sock;
        }

        public void run() {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

                String line;
                String currentName = "";
                String currentMessage = "";
                boolean secret = false;
                boolean name = false;
                boolean message = false;
                boolean loggedIn = false;

                while ((line = in.readLine()) != null) {
                    // Check for secret
                    if (line.equals("SECRET")) {
                        // Start secret, continue to next line
                        secret = true;
                        continue;
                    }
                    else if (secret) {
                        // Verify secret
                        if (line.equals("3c3c4ac618656ae32b7f3431e75f7b26b1a14a87")) {
                            // Secret is correct, we can continue
                            secret = false;
                            loggedIn = true;
                            continue;
                        }
                        else {
                            // Secret is incorrect, close connection
                            break;
                        }
                    }
                    
                    // Disconnect if not logged in
                    if (!loggedIn) {
                        break;
                    }

                    // Check for name
                    if (line.equals("NAME")) {
                        // Start name, continue to next line
                        name = true;
                        continue;
                    }
                    else if (name) {
                        // Add name to user list
                        users.add(line);
                        sendUserList();
                        currentName = line;
                        name = false;
                        continue;
                    }

                    // Check for message
                    if (line.equals("START MSG")) {
                        // Start message, continue to next line
                        message = true;
                        continue;
                    }
                    else if (line.equals("END MSG")) {
                        // End message, send message to all users and continue
                        for (Socket s : sockets) {
                            PrintWriter pw = new PrintWriter(s.getOutputStream());
                            pw.println("START MSG");
                            pw.println(currentMessage);
                            pw.println("END MSG");
                            pw.flush();
                        }
                        message = false;
                        currentMessage = "";
                        continue;
                    }
                    else if (message) {
                        // Add this line of message
                        currentMessage += line;
                        continue;
                    }
                }

                //close the connections
                in.close();
                sock.close();

                // Remove socket from list
                if (sockets.contains(sock)) {
                    sockets.remove(sock);
                }

                // Remove user from list
                if (currentName != "" && users.contains(currentName)) {
                    users.remove(currentName);
                    sendUserList();
                }
                
            }
            catch (Exception e) {
                //close the connections
                try {
                    in.close();
                    sock.close();
                }
                catch (Exception e2) {
                    System.err.println("Cannot close socket");
                }
            }
        }
    }

    public static void main(String args[]){
        GWackChannel server = new GWackChannel(2021);
        server.serve();
    }
       
    
}