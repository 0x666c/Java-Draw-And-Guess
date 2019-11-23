package io.x666c.pictionary.web.standalone;

public class ServerLogic {
	
	private Thread workerThread;
	
	public ServerLogic() {
		workerThread = new Thread(this::routine);
	}
	
	public void start() {
		workerThread.start();
	}
	
	
	
	private void routine() {
		
	}
	
}