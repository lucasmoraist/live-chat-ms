# Live Chat com WebSocket e STOMP
## Dúvidas
- Quando uma pessoa se conecta no live chat após o envio de uma mensagem essa pessoa não consegue ver a mensagem já 
  enviada antes, como resolver isso e como por exemplo o WhatsApp, messenger, telegram fazem para salvar as mensagens 
  enviadas e recebidas?

## Tecnologias Utilizadas

- **Spring Boot** - Framework principal para a aplicação.
- **Spring WebSocket** - Suporte para comunicação WebSocket.
- **STOMP (Simple Text Oriented Messaging Protocol)** - Protocolo de mensagens sobre WebSocket.
- **HTMLUtils** - Utilizado para evitar ataques XSS ao escapar conteúdos HTML.

---

## Configuração do WebSocket

A configuração do WebSocket é feita pela classe `WebSocketConfig`:

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.setApplicationDestinationPrefixes("/app");
    registry.enableSimpleBroker("/topic", "/queue");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/chat-websocket")
            .setAllowedOrigins("*")
            .withSockJS();
  }
}

```

### Explicação das anotações e métodos

- `@EnableWebSocketMessageBroker` Habilita o suporte a WebSockets e STOMP no Spring Boot.
- `configureMessageBroker(MessageBrokerRegistry registry)` Define como as mensagens são roteadas dentro da aplicação.
  - `enableSimpleBroker("/topic", "/queue")`:
    - `/topic`: Usado para mensagens públicas, enviadas a todos os clientes inscritos. 
    - `/queue`: Usado para mensagens privadas, enviadas diretamente a um cliente específico.
  - `setApplicationDestinationPrefixes("/app")`: Define o prefixo usado pelos clientes ao enviar mensagens para o servidor.
- `registerStompEndpoints(StompEndpointRegistry registry)` Define os endpoints WebSocket para que os clientes possam se conectar.
  - `addEndpoint("/buildrun-livechat-websocket")`: Define o caminho do WebSocket onde os clientes podem se conectar.
  - `withSockJS()`: Habilita suporte ao SockJS, que permite fallback para navegadores que não suportam WebSockets nativamente, garantindo maior compatibilidade.

---

## Controlador de Mensagens

A classe `LiveChatController` gerencia o recebimento e envio de mensagens no chat:

```java
@Controller
public class LiveChatController {

    @MessageMapping("/new-message")
    @SendTo("/topics/livechat")
    public ChatResponse newMessage(ChatRequest request) {
        return new ChatResponse(HtmlUtils.htmlEscape(request.user() + ": " + request.message()));
    }
}
```

### Explicação das anotações e métodos

- `@MessageMapping("/new-message")` Define um endpoint STOMP que recebe mensagens enviadas pelos clientes. 
  Quando um cliente envia uma mensagem para `/app/new-message`, esse método é acionado automaticamente.

- `@SendTo("/topics/livechat")` Define para onde a resposta do método será enviada. Todos os clientes que estiverem 
  inscritos no tópico `/topics/livechat` receberão a mensagem processada.

- `HtmlUtils.htmlEscape(...)` Protege contra ataques XSS ao escapar caracteres HTML. Transforma caracteres especiais 
  (`<`, `>`, `&`, etc.) em entidades seguras para exibição.

---

## Fluxo de Comunicação

1. Um cliente envia uma mensagem para `/app/new-message`.
2. O método `newMessage()` do controlador é chamado e processa a mensagem.
3. A resposta formatada é enviada para o tópico `/topics/livechat`, e todos os clientes inscritos recebem a mensagem.

---

