/**
 * ��������ѧ
 * 1120122018
 * ��һ��
 */
package APOST_Package;

public class Symbol {
	String symbol;           //�����  �� ������
	int num = 1;               //�ô���Ϊ�ô���ʱ���ִ���  ��: ����/v 3  ������Ϊ���ʳ�����3��
	String end;              //������� �� ��/v
	double prop = 0.0;            //�ô��ڸô����³��ֵĸ���
	
	Symbol(String symbol){
		
		this.symbol = symbol;
		this.num = 1;
	}
	Symbol(){
		
	}
}
