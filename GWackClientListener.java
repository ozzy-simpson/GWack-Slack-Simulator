import java.net.*;
import javax.swing.*;
import java.io.*;

public class GWackClientListener extends Thread {
    private Socket socket;
    private JTextArea textArea;
    private JList<String> uList;
    
    public GWackClientListener(Socket socket, JTextArea textArea, JList<String> uList) {
        this.socket = socket;
        this.textArea = textArea;
        this.uList = uList;
    }

    public void run() {
        try {
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

            String line;
            boolean message = false;
            boolean userList = false;
            String users = "";

            while ((line = in.readLine()) != null) {
                // Check for message
                if (line.equals("START MSG")) {
                    // Start message, continue to next line
                    message = true;
                    continue;
                }
                else if (line.equals("END MSG")) {
                    // End message, continue to next line
                    textArea.setCaretPosition(textArea.getDocument().getLength()); // Scroll to bottom
                    message = false;
                    continue;
                }
                else if (message) {
                    // Add message to text area
                    textArea.append(line + "\n");
                    continue;
                }

                // Check for update of user list
                if (line.equals("START_CLIENT_LIST")) {
                    // Start user list, continue to next line
                    userList = true;
                    continue;
                }
                else if (line.equals("END_CLIENT_LIST")) {
                    // End update of users
                    userList = false;
                    users = users.replaceAll("^,|,$", ""); // Remove first and last comma
                    // Update user list
                    uList.setListData(users.split(","));
                    users = "";
                    continue;
                }
                else if (userList) {
                    // Add to user list
                    users += "," + line;
                    continue;
                }
            }

            pw.close(); //close the stream
            in.close();//close the input stream
        }
        catch(Exception e) {}
    }
}
