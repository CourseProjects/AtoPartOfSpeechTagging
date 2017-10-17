/**
 * 北京理工大学
 * 1120122018
 * 吴一凡
 */
package APOST_Package;

public class Symbol {
	String symbol;           //汉语词  如 ：迈向
	int num = 1;               //该词作为该词性时出现次数  如: 迈向/v 3  迈向作为动词出现了3次
	String end;              //汉语词类 如 ：/v
	double prop = 0.0;            //该词在该词性下出现的概率
	
	Symbol(String symbol){
		
		this.symbol = symbol;
		this.num = 1;
	}
	Symbol(){
		
	}
}
