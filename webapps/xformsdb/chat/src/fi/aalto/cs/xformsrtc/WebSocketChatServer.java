package fi.aalto.cs.xformsrtc;


import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * A simple XFormsRTC/WebSocket chat server for XFormsRTC chat clients.
 *
 *
 * @author Markku Laine
 * @version 1.0	 Created on May 29, 2016
 */
@ServerEndpoint( value = "/websocket/chat" )
public class WebSocketChatServer {


	// PRIVATE STATIC FINAL VARIABLES
	private static final Logger logger							= Logger.getLogger( WebSocketChatServer.class );
    private static final Set<WebSocketChatServer> connections	= new CopyOnWriteArraySet<>();


    // PRIVATE VARIABLES
    private String nickname;
    private Session session;


    // PUBLIC CONSTRUCTORS
    public WebSocketChatServer() {
        logger.log( Level.DEBUG, "The ChatServer() constructor has been called." );
    	this.nickname	= null;
    	this.session	= null;
    }


    // PRIVATE STATIC METHODS
    private static void broadcast( String message ) {
    	for ( WebSocketChatServer client : WebSocketChatServer.connections ) {
            try {
                synchronized ( client ) {
                    client.session.getBasicRemote().sendText( message );
                }
            } catch ( IOException ex ) {
                logger.log( Level.INFO, "Chat error: Failed to send the message (" + message + ") to the client (" + client.nickname + ").", ex );
                WebSocketChatServer.connections.remove( client );

                try {
                    client.session.close();
                } catch ( IOException ex1 ) {
                    // Ignore
                }

                WebSocketChatServer.broadcast( WebSocketChatServer.generateXmlMessage( "system", client.nickname, client.nickname + " has been disconnected." ) );
            }
        }
    }

    private static String generateXmlMessage( String type, String nickname, String value ) {
    	OffsetDateTime now = OffsetDateTime.now();
    	String message     = "<message><type>" + type + "</type><nickname>" + nickname + "</nickname><value>" + value + "</value><datetime>" + now.toString() + "</datetime></message>";  // TODO: Validate parameters for malicious code
        logger.log( Level.DEBUG, "Message: " + message );
    	return message;
    }


    // PUBLIC METHODS
    @OnOpen
    public void start( Session session ) {
        logger.log( Level.DEBUG, "The start(...) method has been called." );
        this.session	= session;
        WebSocketChatServer.connections.add( this );
    }

    @OnClose
    public void end() {
        logger.log( Level.DEBUG, "The end() method has been called." );
    	WebSocketChatServer.connections.remove( this );
        WebSocketChatServer.broadcast( WebSocketChatServer.generateXmlMessage( "system", this.nickname, this.nickname + " has disconnected." ) );
    }

    @OnMessage
    public void incoming( String message ) {
        logger.log( Level.DEBUG, "The incoming(...) method has been called." );
    	// The first message from the client is for setting the nickname
    	if ( this.nickname == null ) {
    		this.nickname	= message;
            WebSocketChatServer.broadcast( WebSocketChatServer.generateXmlMessage( "system", this.nickname, this.nickname + " has joined." ) );
    	}
    	// Subsequent (normal) messages
    	else {
            WebSocketChatServer.broadcast( WebSocketChatServer.generateXmlMessage( "client", this.nickname, message ) );
    	}
    }

    @OnError
    public void onError( Throwable t ) throws Throwable {
        logger.log( Level.ERROR, "Chat error: " + t.toString(), t );
    }
}