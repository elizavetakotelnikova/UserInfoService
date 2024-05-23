package com.example.ownermicroservice.rabbitMQ;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    public static final String WEB_EXCHANGE = "web_exchange";
    public static final String FIND_OWNER_QUEUE = "finding_owner_queue";
    public static final String FIND_OWNER_BY_CRITERIA_OWNER_QUEUE = "find_owner_by_criteria_queue";
    public static final String CREATING_OWNER_QUEUE = "creating_owner_queue";
    public static final String UPDATING_OWNER_QUEUE = "updating_owner_queue";
    public static final String DELETING_OWNER_QUEUE = "deleting_owner_queue";
    public static final String WEB_TO_CATS = "web_to_cats_queue";
    public static final String ROUTING_KEY_CREATING_OWNER = "owner_creating";
    public static final String ROUTING_KEY_UPDATING_OWNER = "owner_updating";

    public static final String ROUTING_KEY_FINDING_OWNER = "owner_finding";
    public static final String ROUTING_KEY_FINDING_OWNER_BY_CRITERIA = "owner_criteria_finding";
    public static final String ROUTING_KEY_DELETING_OWNER = "owner_deleting";
    /*@Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(WEB_EXCHANGE);
    }*/
    /*@Bean
    public Queue webFindOwnerQueue() {
        return new Queue(FIND_OWNER_QUEUE);
    }
    @Bean
    public Queue webFindOwnersQueue() {
        return new Queue(FIND_OWNER_BY_CRITERIA_OWNER_QUEUE);
    }
    @Bean
    public Queue webCreatingOwnerQueue() {
        return new Queue(CREATING_OWNER_QUEUE);
    }
    @Bean
    public Queue webUpdatingOwnerQueue() {
        return new Queue(UPDATING_OWNER_QUEUE);
    }
    @Bean
    public Queue webDeletingOwnerQueue() {
        return new Queue(DELETING_OWNER_QUEUE);
    }
    @Bean
    public Queue webCatsQueue() {
        return new Queue(WEB_TO_CATS);
    }

    @Bean
    Binding bindingOwnersFinding() {
        return BindingBuilder.bind(webFindOwnerQueue()).to(topicExchange()).with(ROUTING_KEY_FINDING_OWNER);
    }
    @Bean
    Binding bindingFindingCriteriaOwnerCreating() {
        return BindingBuilder.bind(webFindOwnersQueue()).to(topicExchange()).with(ROUTING_KEY_FINDING_OWNER_BY_CRITERIA);
    }
    @Bean
    Binding bindingOwnersCreating() {
        return BindingBuilder.bind(webCreatingOwnerQueue()).to(topicExchange()).with(ROUTING_KEY_UPDATING_OWNER);
    }
    @Bean
    Binding bindingOwnersUpdating() {
        return BindingBuilder.bind(webUpdatingOwnerQueue()).to(topicExchange()).with(ROUTING_KEY_UPDATING_OWNER);
    }
    @Bean
    Binding bindingOwnersDeleting() {
        return BindingBuilder.bind(webDeletingOwnerQueue()).to(topicExchange()).with(ROUTING_KEY_DELETING_OWNER);
    }*/
    /*@Bean
    Binding bindingCats() {
        return BindingBuilder.bind(webCatsQueue()).to(topicExchange()).with(ROUTING_KEY_CATS);
    }
    @Bean
    Binding bindingOwnersCreate() {
        return BindingBuilder.bind(webCatsQueue()).to(topicExchange()).with(ROUTING_KEY_CATS);
    }*/
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    /*@Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }*/
    /*@Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(SEARCHING_QUEUE);
        container.setMessageListener(listenerAdapter);
        return container;
    }*/

}
