package io.smallrye.reactive.messaging.rabbitmq.fault;

import static io.smallrye.reactive.messaging.rabbitmq.i18n.RabbitMQLogging.log;

import java.util.concurrent.CompletionStage;

import io.smallrye.reactive.messaging.rabbitmq.ConnectionHolder;
import io.smallrye.reactive.messaging.rabbitmq.IncomingRabbitMQMessage;
import io.vertx.mutiny.core.Context;

/**
 * A {@link RabbitMQFailureHandler} that in effect treats the nack as an ack.
 */
public class RabbitMQAccept implements RabbitMQFailureHandler {
    private final String channel;

    /**
     * Constructor.
     * 
     * @param channel the channel
     */
    public RabbitMQAccept(String channel) {
        this.channel = channel;
    }

    @Override
    public <V> CompletionStage<Void> handle(IncomingRabbitMQMessage<V> msg, Context context, Throwable reason) {
        // We mark the message as rejected and fail.
        log.nackedAcceptMessage(channel);
        log.fullIgnoredFailure(reason);
        return ConnectionHolder.runOnContext(context, msg::acknowledgeMessage);
    }
}
