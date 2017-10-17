/**
 * 北京理工大学
 * 1120122018
 * 吴一凡
 */
package APOST_Package;

public class State {
      String state;        // 状态 （词性）  如： /n  /v
      int num;             // 该状态词数  如： 动词 354个
      State(String state){
    	  this.state = state;
    	  this.num = 1;
      }
}
