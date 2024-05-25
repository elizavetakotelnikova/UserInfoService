package com.example.jpa;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
public class RabbitMQConfig {
    public static final String WEB_EXCHANGE = "web_exchange";
    public static final String FIND_OWNER_QUEUE = "finding_owner_queue";
    public static final String FIND_OWNER_BY_CRITERIA_OWNER_QUEUE = "find_owner_by_criteria_queue";
    public static final String CREATING_OWNER_QUEUE = "creating_owner_queue";
    public static final String UPDATING_OWNER_QUEUE = "updating_owner_queue";
    public static final String DELETING_OWNER_QUEUE = "deleting_owner_queue";
    public static final String ROUTING_KEY_CREATING_OWNER = "owner_creating";
    public static final String ROUTING_KEY_UPDATING_OWNER = "owner_updating";
    public static final String ROUTING_KEY_FINDING_OWNER = "owner_finding";
    public static final String ROUTING_KEY_FINDING_OWNER_BY_CRITERIA = "owner_criteria_finding";
    public static final String ROUTING_KEY_DELETING_OWNER = "owner_deleting";

    public static final String FIND_CAT_QUEUE = "finding_cat_queue";
    public static final String FIND_CAT_BY_CRITERIA_QUEUE = "find_cat_by_criteria_queue";
    public static final String CREATING_CAT_QUEUE = "creating_cat_queue";
    public static final String UPDATING_CAT_QUEUE = "updating_cat_queue";
    public static final String DELETING_CAT_QUEUE = "deleting_cat_queue";
    public static final String ROUTING_KEY_CREATING_CAT = "cat_creating";
    public static final String ROUTING_KEY_UPDATING_CAT = "cat_updating";
    public static final String ROUTING_KEY_FINDING_CAT = "cat_finding";
    public static final String ROUTING_KEY_FINDING_CAT_BY_CRITERIA = "cat_criteria_finding";
    public static final String ROUTING_KEY_DELETING_CAT = "cat_deleting";
    @Bean
    public ConnectionFactory connectionFactory(){
        return new CachingConnectionFactory("localhost");
    }
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(WEB_EXCHANGE);
    }
    @Bean
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
    public Queue webFindCatQueue() {
        return new Queue(FIND_CAT_QUEUE);
    }
    @Bean
    public Queue webFindCatsQueue() {
        return new Queue(FIND_CAT_BY_CRITERIA_QUEUE);
    }
    @Bean
    public Queue webCreatingCatQueue() {
        return new Queue(CREATING_CAT_QUEUE);
    }
    @Bean
    public Queue webUpdatingCatQueue() {
        return new Queue(UPDATING_CAT_QUEUE);
    }
    @Bean
    public Queue webDeletingCatQueue() {
        return new Queue(DELETING_CAT_QUEUE);
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
        return BindingBuilder.bind(webCreatingOwnerQueue()).to(topicExchange()).with(ROUTING_KEY_CREATING_OWNER);
    }
    @Bean
    Binding bindingOwnersUpdating() {
        return BindingBuilder.bind(webUpdatingOwnerQueue()).to(topicExchange()).with(ROUTING_KEY_UPDATING_OWNER);
    }
    @Bean
    Binding bindingOwnersDeleting() {
        return BindingBuilder.bind(webDeletingOwnerQueue()).to(topicExchange()).with(ROUTING_KEY_DELETING_OWNER);
    }

    @Bean
    Binding bindingCatsFinding() {
        return BindingBuilder.bind(webFindCatQueue()).to(topicExchange()).with(ROUTING_KEY_FINDING_CAT);
    }
    @Bean
    Binding bindingFindingCriteriaCatCreating() {
        return BindingBuilder.bind(webFindCatQueue()).to(topicExchange()).with(ROUTING_KEY_FINDING_CAT_BY_CRITERIA);
    }
    @Bean
    Binding bindingCatsCreating() {
        return BindingBuilder.bind(webCreatingCatQueue()).to(topicExchange()).with(ROUTING_KEY_CREATING_CAT);
    }
    @Bean
    Binding bindingCatsUpdating() {
        return BindingBuilder.bind(webUpdatingCatQueue()).to(topicExchange()).with(ROUTING_KEY_UPDATING_CAT);
    }
    @Bean
    Binding bindingCatsDeleting() {
        return BindingBuilder.bind(webDeletingCatQueue()).to(topicExchange()).with(ROUTING_KEY_DELETING_CAT);
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    public void postProcessEnvironment(final ConfigurableEnvironment environment, final SpringApplication application) {
        application.setAllowBeanDefinitionOverriding(true);
    }
    @Bean
    public AmqpTemplate rabbitTemplate() {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        //rabbitTemplate.setExchange(WEB_EXCHANGE);
        return rabbitTemplate;
    }
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }
    @Bean
    public RabbitListenerErrorHandler rabErrorHandler(){
        return new RabbitListenerErrorHandler() {
            @Override
            public Object handleError(Message message, org.springframework.messaging.Message<?> message1, ListenerExecutionFailedException e) throws Exception {
                System.out.println("meow");
                return null;
            }
        };
    }

}
