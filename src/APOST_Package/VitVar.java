/**
 * 北京理工大学
 * 1120122018
 * 吴一凡
 */
package APOST_Package;

public class VitVar {
	double prop;                  //概率 在前当位置出现该词性的概率
	String state;              //状态（词性）
	int index=0;
	public VitVar(){
		
	}
	public VitVar(double p,String state){
		this.prop = p;
		this.state = state;
	}

}
