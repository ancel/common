package com.work.common.demo.socket;

import java.net.Socket;


/**
 * 
 * 创建人：wanghaibo 
 * 创建时间：2015-10-14 下午3:16:58 
 * 功能描述： 
 * socket处理接口，可以通过实现该接口自定义对socket的处理
 */  

public interface SocketProcessor extends Processor<Socket, Boolean>{
}
