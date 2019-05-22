package com.how2java.bitcoin;

import java.util.Random;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet(name="BitCoinDataCenter",urlPatterns = "/BitCoinDataCenter",loadOnStartup=1) //标记为Servlet不是为了其被访问，而是为了便于伴随Tomcat一起启动，否则数据不会变化
public class BitCoinDataCenter extends HttpServlet implements Runnable{

	public void init(ServletConfig config){
		System.out.println("servlet启动了");
		startup();
	}
	
	public  void startup(){
		new Thread(this).start();
	}
	@Override
	public void run() {
		int bitPrice = 100000;
		while(true){
			
			//每隔1-3秒就产生一个新价格
			int duration = 1000+new Random().nextInt(2000);
			try {
				Thread.sleep(duration);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//新价格围绕100000左右50%波动
			float random = 1+(float) (Math.random()-0.5);
			int newPrice = (int) (bitPrice*random);
			
			//查看的人越多，价格越高
			int total = ServerManager.getTotal();//不停的更新在线人数
			newPrice = newPrice*total;
			
			String messageFormat = "{\"price\":\"%d\",\"total\":%d}";
			String message = String.format(messageFormat, newPrice,total);
			//每有一个新连接加入，或者sleep时间到，广播出去
			ServerManager.broadCast(message);
		}
	}
}
