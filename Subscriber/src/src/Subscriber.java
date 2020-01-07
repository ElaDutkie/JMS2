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
//	Aby publikowane wiadomoœci mog³y byæ odebrane z opóŸnieniem
//	konieczne jest budowanie trwa³ych subskrybentów.
//	• Ka¿dy trwa³y subskrybent ma przypisany unikalny identyfikator ClientId.
//	• Dla ka¿dego trwa³ego subskrybenta trzeba zdefiniowaæ dedykowany obiekt
//	ConnectionFactory i Topic, oba z w³asnoœci¹ ClientId.
//	• Gdy trwa³y subskrybent staje siê nieaktywny, przychodz¹ce wiadomoœci do
//	tematu s¹ zachowywane i dostarczane, gdy subskrybent ponownie staje siê
//	aktywny. Subskrybent jest rozpoznawany przy u¿yciu identyfikatora
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
	// sprawdziæ czy poni¿szy kod na pewno ma byæ tutaj
	
	public void getDurableMessages() {
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
		JMSContext jmsContext = connectionFactory.createContext();
		jmsContext.setClientID("ATJExeciseId");
		try {
		Topic topic = new com.sun.messaging.Topic("ATJDurableTopic");
		JMSConsumer jmsConsumer = jmsContext.createDurableConsumer(topic, "DurableSub");
		for (int i = 0; i < 60; ++i) {
		System.out.println("Trwa³y subskrybent wykonuje zadanie");
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
