//P2P
package src;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class AsynchSubscriber implements MessageListener {
	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage)
			try {
				System.out.printf("Odebrano wiadomoœæ:'%s'%n", ((TextMessage) message).getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
	}
}