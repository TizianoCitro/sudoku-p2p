package it.unisa.studenti.citro.tiziano.listener;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

/**
 * Models a listener for message sent on the network.
 */
public class MessageListenerImpl implements MessageListener {

	/**
	 * Builds a listener for a peer.
	 * @param peerId the peer id.
	 */
	public MessageListenerImpl(int peerId) {
		this.peerId = peerId;
	}

	/**
	 * Parse a received message.
	 * @param obj is the message.
	 * @return the exit status.
	 */
	@Override
	public Object parseMessage(Object obj) {
		TextIO textIO = TextIoFactory.getTextIO();
		TextTerminal<?> terminal = textIO.getTextTerminal();
		terminal.printf(String.format("\n[%s]: %s\n", peerId, obj));
		return SUCCESS;
	}

	/**
	 * The peer id.
	 */
	private int peerId;
}
