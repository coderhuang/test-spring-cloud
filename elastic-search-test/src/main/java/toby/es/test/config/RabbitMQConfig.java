package toby.es.test.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import toby.es.test.constants.RabbitMqConstant;

@Configuration
public class RabbitMQConfig {

    public static final String X_MESSAGE_TTL = "x-message-ttl";
    public static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";
    public static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";

    @Bean
    @Primary
    @Qualifier("rabbitTemplate")
    public RabbitTemplate sendRabbitTemplate(final ConnectionFactory connectionFactory,
            final Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        final RabbitTemplate sendRabbitTemplate = new RabbitTemplate(connectionFactory);
        sendRabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return sendRabbitTemplate;
    }
    
    @Bean
    @Primary
    public DirectExchange defaultExchange() {
        return new DirectExchange(RabbitMqConstant.DEFAULT_EXCHANGE, true, false, null);
    }

    @Bean
    public DirectExchange dlExchange() {
        return new DirectExchange(RabbitMqConstant.DEAD_LETTER_EXCHANGE, true, false, null);
    }

    @Bean
    public Queue commonCouponSettingsSyncQueue() {

        Map<String, Object> args = new HashMap<>(4);
        // x-dead-letter-exchange 这里声明当前队列绑定的死信交换机
        args.put(X_DEAD_LETTER_EXCHANGE, RabbitMqConstant.DEAD_LETTER_EXCHANGE);
        // x-dead-letter-routing-key 这里声明当前队列的死信路由key
        args.put(X_DEAD_LETTER_ROUTING_KEY, RabbitMqConstant.COMMON_COUPON_SETTINGS_SYNC_DLQ);
        // x-message-ttl 声明队列的TTL
        args.put(X_MESSAGE_TTL, 6000);

        return QueueBuilder.durable(RabbitMqConstant.COMMON_COUPON_SETTINGS_SYNC_QUEUE).withArguments(args).build();
    }

    @Bean
    public Queue commonCouponSettingsSyncDlq() {

        Map<String, Object> args = new HashMap<>(4);
        // x-dead-letter-exchange 这里声明当前队列绑定的死信交换机
        args.put(X_DEAD_LETTER_EXCHANGE, RabbitMqConstant.DEFAULT_EXCHANGE);
        // x-dead-letter-routing-key 这里声明当前队列的死信路由key
        args.put(X_DEAD_LETTER_ROUTING_KEY, RabbitMqConstant.COMMON_COUPON_SETTINGS_SYNC_QUEUE);
        // x-message-ttl 声明队列的TTL
        args.put(X_MESSAGE_TTL, 6000);

        return QueueBuilder.durable(RabbitMqConstant.COMMON_COUPON_SETTINGS_SYNC_DLQ).withArguments(args).build();
    }

    @Bean
    public Binding commonCouponSettingsSyncBindingDefaultExchange(
            @Qualifier("defaultExchange") DirectExchange defaultExchange,
            @Qualifier("commonCouponSettingsSyncQueue") Queue queue) {

        return BindingBuilder.bind(queue).to(defaultExchange).with(RabbitMqConstant.COMMON_COUPON_SETTINGS_SYNC_QUEUE);
    }

    @Bean
    public Binding commonCouponSettingsSyncDlqBindingDefaultExchange(@Qualifier("dlExchange") DirectExchange dlExchange,
            @Qualifier("commonCouponSettingsSyncDlq") Queue queue) {

        return BindingBuilder.bind(queue).to(dlExchange).with(RabbitMqConstant.COMMON_COUPON_SETTINGS_SYNC_DLQ);
    }

    @Bean
    public Queue privateCouponSettingsSyncQueue() {

        Map<String, Object> args = new HashMap<>(4);
        // x-dead-letter-exchange 这里声明当前队列绑定的死信交换机
        args.put(X_DEAD_LETTER_EXCHANGE, RabbitMqConstant.DEAD_LETTER_EXCHANGE);
        // x-dead-letter-routing-key 这里声明当前队列的死信路由key
        args.put(X_DEAD_LETTER_ROUTING_KEY, RabbitMqConstant.PRIVATE_COUPON_SETTINGS_SYNC_DLQ);
        // x-message-ttl 声明队列的TTL
        args.put(X_MESSAGE_TTL, 6000);

        return QueueBuilder.durable(RabbitMqConstant.PRIVATE_COUPON_SETTINGS_SYNC_QUEUE).withArguments(args).build();
    }

    @Bean
    public Queue privateCouponSettingsSyncDlq() {

        Map<String, Object> args = new HashMap<>(4);
        // x-dead-letter-exchange 这里声明当前队列绑定的死信交换机
        args.put(X_DEAD_LETTER_EXCHANGE, RabbitMqConstant.DEFAULT_EXCHANGE);
        // x-dead-letter-routing-key 这里声明当前队列的死信路由key
        args.put(X_DEAD_LETTER_ROUTING_KEY, RabbitMqConstant.PRIVATE_COUPON_SETTINGS_SYNC_QUEUE);
        // x-message-ttl 声明队列的TTL
        args.put(X_MESSAGE_TTL, 6000);

        return QueueBuilder.durable(RabbitMqConstant.PRIVATE_COUPON_SETTINGS_SYNC_DLQ).withArguments(args).build();
    }

    @Bean
    public Binding privateCouponSettingsSyncBindingDefaultExchange(
            @Qualifier("defaultExchange") DirectExchange defaultExchange,
            @Qualifier("privateCouponSettingsSyncQueue") Queue queue) {

        return BindingBuilder.bind(queue).to(defaultExchange).with(RabbitMqConstant.PRIVATE_COUPON_SETTINGS_SYNC_QUEUE);
    }

    @Bean
    public Binding privateCouponSettingsSyncDlqBindingDefaultExchange(
            @Qualifier("dlExchange") DirectExchange dlExchange,
            @Qualifier("privateCouponSettingsSyncDlq") Queue queue) {

        return BindingBuilder.bind(queue).to(dlExchange).with(RabbitMqConstant.PRIVATE_COUPON_SETTINGS_SYNC_DLQ);
    }

}
