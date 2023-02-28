package org.example;

import java.util.*;
import java.text.*;

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
    public Message(String from, String to, String subject, String text) {
        /* Remove whitespace */

        String image = """
                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
                """;
        From = from.trim();
        To = to.trim();
        String boundary ="--KkK170891tpbkKk__FV_KKKkkkjjwq--";
        Headers = "From: " + From + CRLF;
        Headers += "To: " + To + CRLF;
        Headers += "Subject: " + subject.trim() + CRLF;
        Headers += "MIME-Version: 1.0" +CRLF + "Content-Type:multipart/mixed;boundary=KkK170891tpbkKk__FV_KKKkkkjjwq"+ CRLF+ "--KkK170891tpbkKk__FV_KKKkkkjjwq"+ CRLF +
                "Content-Type:application/octet-stream;name=picture.jpg" + CRLF +
                    "Content-Transfer-Encoding:base64" + CRLF +
                    "Content-Disposition:attachment;filename=picture.jpg" + CRLF + CRLF;
        Headers += image + CRLF ;
        Headers += boundary + CRLF;
	/* A close approximation of the required format. Unfortunately
	   only GMT. */
        SimpleDateFormat format =
                new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
        String dateString = format.format(new Date());
        Headers += "Date: " + dateString + CRLF;
        Body = text;
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