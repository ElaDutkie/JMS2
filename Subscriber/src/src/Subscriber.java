package src;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Topic;

public class Subscriber {
	private JMSContext jmsContext;
	private JMSConsumer jmsConsumer;
	private Topic topic;
//	Aby publikowane wiadomo�ci mog�y by� odebrane z op�nieniem
//	konieczne jest budowanie trwa�ych subskrybent�w.
//	� Ka�dy trwa�y subskrybent ma przypisany unikalny identyfikator ClientId.
//	� Dla ka�dego trwa�ego subskrybenta trzeba zdefiniowa� dedykowany obiekt
//	ConnectionFactory i Topic, oba z w�asno�ci� ClientId.
//	� Gdy trwa�y subskrybent staje si� nieaktywny, przychodz�ce wiadomo�ci do
//	tematu s� zachowywane i dostarczane, gdy subskrybent ponownie staje si�
//	aktywny. Subskrybent jest rozpoznawany przy u�yciu identyfikatora
//	ClientId.
	Subscriber(String url, String topicName) throws JMSException {
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
		jmsContext = connectionFactory.createContext();
		((com.sun.messaging.ConnectionFactory) connectionFactory)
				.setProperty(com.sun.messaging.ConnectionConfiguration.imqAddressList, url);
		topic = new com.sun.messaging.Topic(topicName); // "ATJTopic"
		jmsConsumer = jmsContext.createConsumer(topic);
	}

	public void receiveQueueMessageAsync() throws InterruptedException {
		jmsConsumer.setMessageListener(new AsynchSubscriber());
		for (int i = 0; i < 30; ++i) {
			System.out.println("Subskrybent wykonuje zadanie");
			Thread.sleep(1000);
		}
	}
	// sprawdzi� czy poni�szy kod na pewno ma by� tutaj
	
	public void getDurableMessages() {
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
		JMSContext jmsContext = connectionFactory.createContext();
		jmsContext.setClientID("ATJExeciseId");
		try {
		Topic topic = new com.sun.messaging.Topic("ATJDurableTopic");
		JMSConsumer jmsConsumer = jmsContext.createDurableConsumer(topic, "DurableSub");
		for (int i = 0; i < 60; ++i) {
		System.out.println("Trwa�y subskrybent wykonuje zadanie");
		try {
		String message = jmsConsumer.receiveBody(String.class);
		System.out.println(message);
		Thread.sleep(1000);
		}
		catch (InterruptedException e) { e.printStackTrace(); }
		}
		jmsConsumer.close();
		}
		catch (JMSException e) { e.printStackTrace(); }
		jmsContext.close();
		}

	protected void finalize() { 
 }
}
