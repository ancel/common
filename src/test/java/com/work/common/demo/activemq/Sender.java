package com.work.common.demo.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 1、ActiveMQ5.8下载地址：http://activemq.apache.org/activemq-580-release.html. 根据平台选择在download link中选择相应的包下载。
 * 2、windows环境 
 * （1）下载
 * （2）解压，双击双击其bin目录下的activemq.bat
 * （3）通过http://IP:8161访问，通过http://IP:8161/admin可管理broker
 * 账号密码为（admin，admin），访问端口号的配置参见conf
 * /jetty.xml的server，账号密码参见jetty-realm.xml。linux环境下也是一样 
 * 3、linux环境 
 * （1）下载
 * （2）通过secureFX或者xftp将其复制到linux目录下 
 * （3）使用tar zxvf
 * apache-activemq-5.8.0-bin.tar.gz命令将其解压到当前目录 
 * （4）在bin目录下执行activemq
 * start即可启动ActiveMQ，其他命令： 停止：activemq stop 重启：activemq restart
 * （5）通过http://IP:8161访问，通过http://IP:8161/admin可管理broker
 * 
 * @author admin
 * 
 */
public class Sender {
	private static final int SEND_NUMBER = 5;

	public static void main(String[] args) {
		// ConnectionFactory ：连接工厂，JMS 用它创建连接
		ConnectionFactory connectionFactory;
		// Connection ：JMS 客户端到JMS Provider 的连接
		Connection connection = null;
		// Session： 一个发送或接收消息的线程
		Session session;
		// Destination ：消息的目的地;消息发送给谁.
		Destination destination;
		// MessageProducer：消息发送者
		MessageProducer producer;
		// TextMessage message;
		// 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
		try {
			// 构造从工厂得到连接对象
			connection = connectionFactory.createConnection();
			// 启动
			connection.start();
			// 获取操作连接
			session = connection.createSession(Boolean.TRUE,
					Session.AUTO_ACKNOWLEDGE);
			//获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
			destination = session.createQueue("FirstQueue");
			// 得到消息生成者【发送者】
			producer = session.createProducer(destination);
			// 设置不持久化，此处学习，实际根据项目决定
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			// 构造消息，此处写死，项目就是参数，或者方法获取
			sendMessage(session, producer);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != connection)
					connection.close();
			} catch (Throwable ignore) {
			}
		}
	}

	public static void sendMessage(Session session, MessageProducer producer)
			throws Exception {
		TextMessage message;
		for (int i = 1; i <= SEND_NUMBER; i++) {
			message = session.createTextMessage("ActiveMq 发送的消息" + i);
			// 发送消息到目的地方
			System.out.println("发送消息------" + "ActiveMq 发送的消息" + i);
			producer.send(message);
		}
		Thread.sleep(2000);
		message = session.createTextMessage("end");
		producer.send(message);
	}
}
