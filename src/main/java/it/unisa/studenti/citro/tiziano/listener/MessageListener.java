package it.unisa.studenti.citro.tiziano.listener;

/**
 * Models a listener for message sent on the network.
 */
public interface MessageListener {

	/**
	 * Parse a received message.
	 * @param obj is the message.
	 * @return the exit status.
	 */
	Object parseMessage(Object obj);

	/**
	 * Exit status when receiving a message.
	 */
	String SUCCESS = "success";
}
