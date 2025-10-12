package org.alexstk.safka.orchestrator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storozhuk.decoder.AuthService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.IoEventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.SingleThreadIoEventLoop;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.alexstk.safka.orchestrator.entity.Topic;
import org.alexstk.safka.orchestrator.file.FileProcessor;
import org.alexstk.safka.orchestrator.io.handler.MessageInboundHandlerAdapter;
import org.alexstk.safka.orchestrator.io.handler.impl.message.GetTopicsMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.message.ReadTopicMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.sink.CreateTopicMessageHandler;
import org.alexstk.safka.orchestrator.io.handler.impl.sink.PublishMessageHandler;
import org.alexstk.safka.orchestrator.io.initializer.HandlerChannelInitializer;

public class MessageOrchestrator {

  private final Map<String, Topic> topics = new ConcurrentHashMap<>(); // Thread-safe map
  private final ExecutorService executorService = Executors.newFixedThreadPool(
      1); // Adjust thread pool size as needed
  private final int tcpPort;
  private final HandlerChannelInitializer handlerChannelInitializer;
  IoEventLoopGroup bossGroup;
  EventLoopGroup workerGroup;
  private ChannelGroup channelGroup;

  public MessageOrchestrator(int tcpPort, GetTopicsMessageHandler getTopicsMessageHandler,
      ReadTopicMessageHandler readTopicMessageHandler, PublishMessageHandler publishMessageHandler,
      CreateTopicMessageHandler createTopicMessageHandler, AuthService authService) {
    this.tcpPort = tcpPort;
    handlerChannelInitializer = new HandlerChannelInitializer(
        new MessageInboundHandlerAdapter(getTopicsMessageHandler, readTopicMessageHandler,
            publishMessageHandler, createTopicMessageHandler, authService));
  }

  public boolean createTopic(String topicName) {
    if (topics.get(topicName) == null) {
      topics.put(topicName, new Topic(topicName));
      return true;
    }
    return false;
  }

  public void startListening() {
    bossGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
    workerGroup = new SingleThreadIoEventLoop(bossGroup, new DefaultEventExecutor(),
        NioIoHandler.newFactory());
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup)
          .channel(NioServerSocketChannel.class)
          .childHandler(handlerChannelInitializer)
          .option(ChannelOption.SO_BACKLOG, 1024)
          .option(ChannelOption.AUTO_CLOSE, true)
          .option(ChannelOption.SO_REUSEADDR, true)
          .childOption(ChannelOption.SO_KEEPALIVE, true)
          .childOption(ChannelOption.TCP_NODELAY, true);

      // Bind and start to accept incoming connections.
      ChannelFuture f = b.bind(tcpPort).sync();
      System.out.println("Safka server started");

      ChannelFuture channelFuture = f.channel().closeFuture().sync();
      channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
      channelGroup.add(channelFuture.channel());
    } catch (InterruptedException e) {
      stop();
      throw new RuntimeException(e);
    }
  }

  public final void stop() {
    channelGroup.close();
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
  }
}
