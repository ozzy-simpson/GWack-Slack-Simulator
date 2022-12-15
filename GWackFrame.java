import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.net.*;
import java.io.*;

public class GWackFrame extends JFrame {

	// Connection
	private Socket sock = null;  
	private PrintWriter pw = null;

	// Frame components
    private JPanel GWack;
	private JPanel headerPanel;
	private JLabel appTitle;
	private JTabbedPane appPages;
	private JPanel messagesPanel;
	private JLabel messagesTitle;
	private JScrollPane messagesScroll;
	private JTextArea messages;
	private JPanel composePanel;
	private JLabel composeLabel;
	private JScrollPane composeScroll;
	private JTextArea composeInput;
	private JPanel sendPanel;
	private JButton sendButton;
	private JPanel usersPanel;
	private JLabel usersTitle;
	private JScrollPane usersScroll;
	private JList<String> usersList;
	private JPanel settingsPanel;
	private JLabel settingsTitle;
	private JScrollPane settingsScroll;
	private JPanel settingsInner;
	private JPanel settingsInner2;
	private JPanel namePanel;
	private JLabel nameLabel;
	private JTextField nameInput;
	private JPanel hostPanel;
	private JLabel ipLabel;
	private JLabel portLabel;
	private JTextField ipInput;
	private JTextField portInput;
	private JPanel connectPanel;
	private JPanel connectSpacer;
	private JButton connectButton;

    public GWackFrame(){
        super();
        this.setTitle("GWack");

        GWack = new JPanel();
		headerPanel = new JPanel();
		appTitle = new JLabel();
		appPages = new JTabbedPane();
		messagesPanel = new JPanel();
		messagesTitle = new JLabel();
		messagesScroll = new JScrollPane();
		messages = new JTextArea();
		composePanel = new JPanel();
		composeLabel = new JLabel();
		composeScroll = new JScrollPane();
		composeInput = new JTextArea();
		sendPanel = new JPanel();
		sendButton = new JButton();
		usersPanel = new JPanel();
		usersTitle = new JLabel();
		usersScroll = new JScrollPane();
		usersList = new JList<>();
		settingsPanel = new JPanel();
		settingsTitle = new JLabel();
		settingsScroll = new JScrollPane();
		settingsInner = new JPanel();
		settingsInner2 = new JPanel();
		namePanel = new JPanel();
		nameLabel = new JLabel();
		nameInput = new JTextField();
		hostPanel = new JPanel();
		ipLabel = new JLabel();
		portLabel = new JLabel();
		ipInput = new JTextField();
		portInput = new JTextField();
		connectPanel = new JPanel();
		connectSpacer = new JPanel(null);
		connectButton = new JButton();

		GWack.setLayout(new BorderLayout());

		headerPanel.setBackground(new Color(0x2c3e50));
		headerPanel.setForeground(new Color(0xecf0f1));
		headerPanel.setLayout(new GridLayout());

		appTitle.setText("GWack");
		appTitle.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		appTitle.setBorder(new EmptyBorder(5, 15, 5, 15));
		appTitle.setForeground(new Color(0xecf0f1));
		headerPanel.add(appTitle);

		GWack.add(headerPanel, BorderLayout.NORTH);

		appPages.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		appPages.setBorder(null);
		appPages.setTabPlacement(SwingConstants.LEFT);
		appPages.setForeground(Color.black);

		messagesPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
		messagesPanel.setBackground(new Color(0xecf0f1));
		messagesPanel.setLayout(new BorderLayout());

		messagesTitle.setText("Messages");
		messagesTitle.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
		messagesTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
		messagesPanel.add(messagesTitle, BorderLayout.NORTH);

		messages.setEditable(false);
		messages.setBackground(Color.white);
		messages.setRows(10);
		messages.setTabSize(4);
		messagesScroll.setViewportView(messages);

		messagesPanel.add(messagesScroll, BorderLayout.CENTER);

		composePanel.setBackground(new Color(0xecf0f1));
		composePanel.setLayout(new GridLayout(3, 1, 0, 5));

		composeLabel.setText("Compose");
		composeLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		composePanel.add(composeLabel);

		composeScroll.setBackground(new Color(0xecf0f1));

		composeInput.setRows(2);
		// if enter pressed in composeInput, send message
		composeInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendButton.doClick();
				}
			}
		});
		composeScroll.setViewportView(composeInput);

		composePanel.add(composeScroll);

		sendPanel.setBackground(new Color(0xecf0f1));
		sendPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		sendButton.setText("Send");
		sendButton.addActionListener((e) -> {
			sendMessage(this);
		});
		sendPanel.add(sendButton);
		
		composePanel.add(sendPanel);
		messagesPanel.add(composePanel, BorderLayout.SOUTH);
				
		appPages.addTab("Messages", messagesPanel);

		usersPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
		usersPanel.setBackground(new Color(0xecf0f1));
		usersPanel.setLayout(new BorderLayout());

		usersTitle.setText("Members Online");
		usersTitle.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
		usersTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
		usersPanel.add(usersTitle, BorderLayout.NORTH);

		usersScroll.setViewportView(usersList);

		usersPanel.add(usersScroll, BorderLayout.CENTER);

		appPages.addTab("Active Users", usersPanel);
		appPages.setEnabledAt(1, false);

		settingsPanel.setBackground(new Color(0xecf0f1));
		settingsPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
		settingsPanel.setLayout(new BorderLayout(5, 5));

		settingsTitle.setText("Settings");
		settingsTitle.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
		settingsTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
		settingsPanel.add(settingsTitle, BorderLayout.NORTH);

		settingsScroll.setBorder(null);
		settingsScroll.setBackground(new Color(0xecf0f1));

		settingsInner.setBorder(null);
		settingsInner.setBackground(new Color(0xecf0f1));
		settingsInner.setLayout(new FlowLayout());

		settingsInner2.setBackground(new Color(0xecf0f1));
		settingsInner2.setLayout(new GridLayout(3, 1, 0, 10));

		namePanel.setBackground(new Color(0xecf0f1));
		namePanel.setLayout(new GridLayout(2, 1, 5, 5));

		nameLabel.setText("Name");
		nameLabel.setLabelFor(nameInput);
		nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		namePanel.add(nameLabel);

		nameInput.setColumns(20);
		nameInput.setHorizontalAlignment(SwingConstants.LEFT);
		namePanel.add(nameInput);

		settingsInner2.add(namePanel);

		hostPanel.setBackground(new Color(0xecf0f1));
		hostPanel.setLayout(new GridLayout(2, 2, 5, 5));

		ipLabel.setText("IP Address");
		ipLabel.setLabelFor(ipInput);
		hostPanel.add(ipLabel);

		portLabel.setText("Port");
		portLabel.setLabelFor(portInput);
		hostPanel.add(portLabel);

		ipInput.setColumns(20);
		ipInput.setHorizontalAlignment(SwingConstants.LEFT);
		hostPanel.add(ipInput);

		portInput.setColumns(20);
		portInput.setHorizontalAlignment(SwingConstants.LEFT);
		hostPanel.add(portInput);

		settingsInner2.add(hostPanel);

		connectPanel.setBackground(new Color(0xecf0f1));
		connectPanel.setLayout(new GridLayout(2, 1, 5, 0));

		connectSpacer.setBackground(new Color(0xecf0f1));
		connectSpacer.setDoubleBuffered(false);
		connectPanel.add(connectSpacer);

		connectButton.setText("Connect");
		connectButton.addActionListener((e) -> {
			if (connectButton.getText().equals("Connect")) {
				// Change button text
				connectButton.setText("Connecting...");

				// Disable inputs
				nameInput.setEnabled(false);
				ipInput.setEnabled(false);
				portInput.setEnabled(false);

				// Connect to server
				if(connectToServer(this)) {
					// Change button text
					connectButton.setText("Disconnect");
					appPages.setSelectedIndex(0); // Set page to "Messages"

					// Enable users panel
					appPages.setEnabledAt(1, true);
				}
				else {
					// Re-enable inputs
					nameInput.setEnabled(true);
					ipInput.setEnabled(true);
					portInput.setEnabled(true);

					// Change button text
					connectButton.setText("Connect");
				}
			}
			else {
				// Change button text
				connectButton.setText("Disconnecting...");

				if (disconnectFromServer(this)) {
					// Change button text
					connectButton.setText("Connect");

					// Re-enable inputs
					nameInput.setEnabled(true);
					ipInput.setEnabled(true);
					portInput.setEnabled(true);

					// Disable users panel
					appPages.setEnabledAt(1, false);
				}
				else {
					// Change button text
					connectButton.setText("Disconnect");
				}
			}
		});
		connectPanel.add(connectButton);

		settingsInner2.add(connectPanel);
		settingsInner.add(settingsInner2);

		settingsScroll.setViewportView(settingsInner);

		settingsPanel.add(settingsScroll, BorderLayout.CENTER);

		appPages.addTab("Settings", settingsPanel);

		GWack.add(appPages, BorderLayout.CENTER);
		appPages.setSelectedIndex(2); // Set default page to "Settings"

        this.add(GWack);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

    }

	public static void connectionErrorFrame(JFrame frame, String error) {
		JOptionPane.showMessageDialog(frame, error, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private boolean connectToServer(JFrame frame) {
		// Ensure name, host, and port are given
		if (nameInput.getText().equals("") || ipInput.getText().equals("") || portInput.getText().equals("")) {
			connectionErrorFrame(frame, "Please enter a username, host, and port.");
			return false;
		}
      
        try {
           sock = new Socket(ipInput.getText(),Integer.parseInt(portInput.getText()));
        }
		catch (Exception e) {
			connectionErrorFrame(frame, "Cannot connect to server");
			return false;
        }

        try{
            pw = new PrintWriter(sock.getOutputStream());
			// Login to server
            pw.println("SECRET");
			pw.println("3c3c4ac618656ae32b7f3431e75f7b26b1a14a87");
			pw.println("NAME");
			pw.println(nameInput.getText());
			pw.flush();

			Thread t = new GWackClientListener(sock, messages, usersList);
    		t.start();
        }
		catch(Exception e){
            connectionErrorFrame(frame, "Error logging in to server");
            return false;
        }
		return true;
	}

	private boolean disconnectFromServer(JFrame frame) {
        try {
			if (pw != null) {
				pw.close(); //close the stream
			}
            sock.close();//close the socket
			messages.setText("");
			composeInput.setText("");
			return true;
        }
		catch (Exception e) {
			connectionErrorFrame(frame, "Cannot disconnect");
			return false;
        }
	}

	private void sendMessage(JFrame frame) {
		// check that server is connected
		if (sock == null || sock.isClosed()) {
			connectionErrorFrame(frame, "You need to connect to a server before sending a message");
			composeInput.setText("");
			appPages.setSelectedIndex(2); // Switch to "Settings"
			return;
		}
		composeInput.setText(composeInput.getText().replace("\n", "")); // Remove newlines

		if (composeInput.getText().equals("")) {
			return;
		}
		try {
			pw.println("START MSG");
			pw.println("[" + nameInput.getText() + "] " + composeInput.getText()); // prepend username and remove newlines
			pw.println("END MSG");
			pw.flush();
			composeInput.setText("");
		}
		catch (Exception e) {
			connectionErrorFrame(frame, "Cannot send message");
		}
	}
}
