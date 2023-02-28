package org.example;

import java.io.*;
import java.security.cert.CRL;
import java.util.*;
import java.text.*;
import java.util.Base64;

/* $Id: Message.java,v 1.5 1999/07/22 12:10:57 kangasha Exp $ */

/**
 * Mail message.
 *
 * @author Jussi Kangasharju
 */
public class Message {
    /* The headers and the body of the message. */
    public String Headers;
    public String Body;

    /* Sender and recipient. With these, we don't need to extract them
       from the headers. */
    private String From;
    private String To;

    /* To make it look nicer */
    private static final String CRLF = "\r\n";

    /* Create the message object by inserting the required headers from
       RFC 822 (From, To, Date). */
    public Message(String from, String to, String subject, String attach, String text) {
        /* Remove whitespace */
        String image = "";
        try {
            image = readFromFile(attach);
        } catch (IOException e){
            System.out.println(e);
        }


        From = from.trim();
        To = to.trim();
        String boundary ="--KkK170891tpbkKk__FV_KKKkkkjjwq--";
        Headers = "From: " + From + CRLF;
        Headers += "To: " + To + CRLF;
        Headers += "Subject: " + subject.trim() + CRLF;
        /* A close approximation of the required format. Unfortunately
	   only GMT. */
        SimpleDateFormat format =
                new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
        String dateString = format.format(new Date());
        Headers += "Date: " + dateString + CRLF;

        Headers += "MIME-Version: 1.0" +CRLF;
        Headers +="Content-Type:multipart/mixed;boundary=KkK170891tpbkKk__FV_KKKkkkjjwq"+ CRLF;
        Headers += "--KkK170891tpbkKk__FV_KKKkkkjjwq"+ CRLF;
        if(!Objects.equals(image, "")){

            Headers += "Content-Type:application/octet-stream;name=picture.jpg" + CRLF +
                    "Content-Transfer-Encoding:base64" + CRLF +
                    "Content-Disposition:attachment;filename=picture.jpg" + CRLF + CRLF;
            Headers += image + CRLF ;
            Headers += boundary + CRLF;
        }
        Headers += "Content-Type: text/plain; charset=\"iso-8859-1\"" + CRLF +
        "Content-Transfer-Encoding: quoted-printable" + CRLF + CRLF;
        Headers += text + CRLF;
        Body = "";
        Body += CRLF + boundary + CRLF;



    }

    private String readFromFile(String path) throws IOException {
        File f = new File(path);
        FileInputStream fin = new FileInputStream(f);
        byte[] imagebytearray = new byte[(int)f.length()];
        fin.read(imagebytearray);
        String imagetobase64 = Base64.getEncoder().encodeToString(imagebytearray);
        fin.close();
        return imagetobase64;
    }


    /* Two functions to access the sender and recipient. */
    public String getFrom() {
        return From;
    }

    public String getTo() {
        return To;
    }

    /* Check whether the message is valid. In other words, check that
       both sender and recipient contain only one @-sign. */
    public boolean isValid() {
        int fromat = From.indexOf('@');
        int toat = To.indexOf('@');

        if(fromat < 1 || (From.length() - fromat) <= 1) {
            System.out.println("Sender address is invalid");
            return false;
        }
        if(toat < 1 || (To.length() - toat) <= 1) {
            System.out.println("Recipient address is invalid");
            return false;
        }
        if(fromat != From.lastIndexOf('@')) {
            System.out.println("Sender address is invalid");
            return false;
        }
        if(toat != To.lastIndexOf('@')) {
            System.out.println("Recipient address is invalid");
            return false;
        }
        return true;
    }

    /* For printing the message. */
    public String toString() {
        String res;

        res = Headers + CRLF;
        res += Body;
        return res;
    }
}