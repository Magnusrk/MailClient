package org.example;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Open an SMTP connection to a mailserver and send one mail.
 *
 */
public class SMTPConnection {
    /* The socket to the server */
    private Socket connection;

    /* Streams for reading and writing the socket */
    private BufferedReader fromServer;
    private DataOutputStream toServer;

    private static final int SMTP_PORT = 25;
    private static final String CRLF = "\r\n";

    /* Are we connected? Used in close() to determine what to do. */
    private boolean isConnected = false;

    /* Create an SMTPConnection object. Create the socket and the
       associated streams. Initialize SMTP connection. */
    public SMTPConnection(Envelope envelope) throws IOException {
        connection = new Socket(envelope.DestHost, SMTP_PORT);
        //connection = new Socket("datacomm.bhsi.xyz", SMTP_PORT);
        fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        toServer = new DataOutputStream(connection.getOutputStream());

        /* Fill in */
	/* Read a line from server and check that the reply code is 220.
	   If not, throw an IOException. */
        int serverResponse = this.parseReply(fromServer.readLine());
        if (serverResponse != 220){
            throw new IOException("Unexpected server response");
        }
        /* Fill in */

	/* SMTP handshake. We need the name of the local machine.
	   Send the appropriate SMTP handshake command. */
        String localhost = InetAddress.getLocalHost().getHostName();
        sendCommand("HELO " + localhost, 250);

        isConnected = true;
    }

    /* Send the message. Write the correct SMTP-commands in the
       correct order. No checking for errors, just throw them to the
       caller. */
    public void send(Envelope envelope) throws IOException {
        /* Fill in */
	/* Send all the necessary commands to send a message. Call
	   sendCommand() to do the dirty work. Do _not_ catch the
	   exception thrown from sendCommand(). */
        sendCommand("MAIL FROM: <" + envelope.Sender + ">", 250);
        sendCommand("RCPT TO: <" + envelope.Recipient + ">", 250);
        sendCommand("DATA", 354);
        System.out.println(envelope.Message.Headers + envelope.Message.Body + "\r\n.");
        sendCommand(envelope.Message.Headers + envelope.Message.Body + "\r\n.", 250);
        /* Fill in */
    }

    /* Close the connection. First, terminate on SMTP level, then
       close the socket. */
    public void close() {
        isConnected = false;
        try {
            sendCommand("QUIT", 221);
            // connection.close();
        } catch (IOException e) {
            System.out.println("Unable to close connection: " + e);
            isConnected = true;
        }
    }

    /* Send an SMTP command to the server. Check that the reply code is
       what is is supposed to be according to RFC 821. */
    private void sendCommand(String command, int rc) throws IOException {
        /* Fill in */
        /* Write command to server and read reply from server. */
        toServer.writeBytes(command + CRLF);
        /* Fill in */

        /* Fill in */
	/* Check that the server's reply code is the same as the parameter
	   rc. If not, throw an IOException. */
        String response = fromServer.readLine();
        System.out.println(response);
        int reply = parseReply(response);
        if (reply != rc){
            throw new IOException("Reply did not match expected reply");
        }
        /* Fill in */
    }

    /* Parse the reply line from the server. Returns the reply code. */
    private int parseReply(String reply) {
        /* Fill in */
        StringTokenizer st = new StringTokenizer(reply, " ");
        int retVal;
        retVal = Integer.parseInt(st.nextToken());
        return retVal;
    }

    /* Destructor. Closes the connection if something bad happens. */
    protected void finalize() throws Throwable {
        if(isConnected) {
            close();
        }
        super.finalize();
    }
}
