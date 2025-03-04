package com.example.live_chat_ms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Este metodo tem como função configurar o prefixo que será utilizado para os endpoints
    // O prefixo é o caminho que o servidor irá utilizar para enviar mensagens para o cliente
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topics");
        registry.setApplicationDestinationPrefixes("/app");
    }

    // Este metodo tem como função registrar os endpoints que serão utilizados
    // Esse endpoint é o caminho que o cliente irá acessar para se conectar ao servidor
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws");
    }

}
