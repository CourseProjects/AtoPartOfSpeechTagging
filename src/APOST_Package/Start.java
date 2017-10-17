package APOST_Package;

public class Start {
private static GUI guiInstance = new GUI();
	
	public static GUI getGUI() {
		return guiInstance;
	}

	public static void main(String args[]){	//启动程序，由GUI界面开始
		new Start();	
		/**创建一个匿名对象，利用构造方法创建GUI的对象，全程序只用这一个对象，
		 * 以免对象不一致导致界面内的路径无法正确传递到内核中
		 */
	}
}
