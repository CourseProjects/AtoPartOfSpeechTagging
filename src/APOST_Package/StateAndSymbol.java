/**
 * ��������ѧ
 * 1120122018
 * ��һ��
 * ������
 * stateAndSymbol()  �����ַ�����״̬��
 * sTMandSHT(double delta)  ����״̬ת�ƾ���(state transfer matrix)��
 * 						         �ַ���ϣ��(symbol hash table)
 * viterbi()   ����������ɷ�ģ������viterbi�㷨ʵ�ִ����ע
 * test()    ��עЧ������
 */
package APOST_Package;


import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;




public class StateAndSymbol {
	StateSet staSet = new StateSet();        //״̬��
	SymbolSet sybSet=new SymbolSet();        //�ַ���
	List<String> stateTable=new ArrayList<String>();  //�������д��ԣ����ڼ���״̬ת���������
	int totalNum=0;            //���д���
	int stateNum=0;            //���д�����
	int stateTransNum[][];     //״̬ת��������
	double stateTransProp[][];         //״̬ת�����ʱ�
	double unRegProp[];          //δ��½���ض�״̬�ĳ��ָ���
	@SuppressWarnings("rawtypes")
	Map wordMap =new HashMap();  //�ʱ�
	
	int totalWords=0;      //����ע����
	int correctWords=0;    //��ע��ȷ��
	double correctRate=0.0; //��ȷ��
//	int running = 0;		//�����ã��������ǲ�������
	
	GUI gui = Start.getGUI();
	String dic = gui.getJtfResult().getText();	//�����ļ�����·��
	File infile1 = new File (gui.getJtfCorpus().getText());     //�����ļ����������Ͽ�
	File infile2 = new File (gui.getJtfTestR().getText());      // �������ڲ��Ա�עЧ���Ĳ����ĵ�
	File outfile1 = new File (dic + "\\StateSet.txt");       //״̬���ļ�StateSet.txt
	File outfile2 = new File (dic + "\\SymbolSet.txt");      //�ַ����ļ�SymbolSet.txt
	File outfile3 = new File (dic + "\\SymbolProp.txt");  //�ַ��������ļ�SymbolProp.txt
	File outfile4 = new File (dic + "\\TestW.txt");    //  �ӹ��õĿ������ڲ��Ե��ĵ�TestW.txt
	File outfile5 = new File (dic + "\\Result.txt");       // ��ע���Result.txt
	File outfile6 =new File (dic + "\\StateTransMatrics.txt");  //  ״̬ת�ƾ����ļ�StateTransMatrics.txt
	
	//------------------------------------------------------------------------------
	//�����ַ�����״̬�������ϵ�ǰ�ڼӹ�
	public void stateAndSymbol()throws IOException{
		
		FileReader in = new FileReader(infile1);//�����ļ�
		FileWriter out1 =new FileWriter(outfile1);//״̬���ļ�
		FileWriter out2 = new FileWriter(outfile2);//�ַ����ļ�
		FileWriter out4 = new FileWriter(outfile4);//  �ӹ��õĿ������ڲ��Ե��ĵ�
		State temp1 = new State("/v");          // Ԥ�Ȳ���һ�����ڵĴ��ԣ������жϴ�����
		temp1.num = 0;
		staSet.stateSet.add(temp1);
		stateTable.add("/v");
		
		int ch;
		String element = "";    //��+���� ��
		String end = "";       // ����
		String symbol = "";     // ��
		int flag = 1;           //�����Ƿ��Ѿ���������еı�־
		while ((ch = in.read()) != -1){                     //���ļ�
			if ((char)(ch) == '\r' || (char)(ch) == '\n'){
				out2.write(ch);
				out4.write(ch);
				continue;
			}
			if ((char)ch == ' ' || (char)(ch) == '[' || 
					(char)(ch)==']'){ 					//���� ��������
				continue;
			}
			
			element += (char)ch;
			while((char) (ch = in.read()) != ' ' && (char)(ch) != ']'
					&& (char)(ch) != '\n'){ 			//����һ������
				element += (char)ch;
				}
				if ((element.length() == 21 && element.endsWith("/m"))
						|| element.length() == 2 || element.length()==1){
					element="";               			//���������ڵĻ�����
					continue;
				}
				else{
					int index = element.lastIndexOf("/");   //������صĴʺʹ��Բ���
					if (index < element.length() && index != 0){
						end = element.substring(element.lastIndexOf("/"), element.length()); 
						symbol = element.substring(0, element.lastIndexOf("/"));
					}
					
					flag = 1;
					
						for (int i = 0; i < staSet.stateSet.size(); i++){  // �жϴ����Ƿ��Ѿ������ڱ���
							if(staSet.stateSet.get(i).state.equals(end)){  //����
								staSet.stateSet.get(i).num++;			//�ô��Գ��ִ���++					
								out2.write(element+" ");
								out4.write(symbol+" ");
								element = "";
								symbol = "";
								end = "";
								flag  =0;
								break;
							}
							
						}
						if (flag == 1){       //������  ����Ա�״̬���У�������Ԫ��
							State temp = new State(end);
							stateTable.add(end);
							staSet.stateSet.add(temp);
							out2.write(element+"  ");						
							out4.write(symbol+" ");
							element = "";
							symbol = "";
							end = "";
						}
						continue;
					}
				}
		for (int i = 0; i < staSet.stateSet.size(); i++){//����Ԥ���дʵ�����
			out1.write(stateTable.get(i) + "  ");
			totalNum = totalNum+staSet.stateSet.get(i).num; 
		}
		
		stateNum = staSet.stateSet.size();  //״̬����������������
//		System.out.println(staSet.stateSet.size());
//		System.out.println(totalNum);
		in.close();
		out1.close();
		out2.close();
		out4.close();
		
	}
	
	//--------------------------------------------------------------------------------------------------
	// ����״̬ת�ƾ��� �����ַ���ϣ��  ��������ɷ�ģ�͵Ĳ���  
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sTMandSHT(double delta)throws IOException{
		stateTransNum = new int [stateNum][stateNum];  //״̬ת���� �����ִ�����
		stateTransProp = new double [stateNum][stateNum]; // ״̬ת���� (Ƶ��)
		FileWriter out = new FileWriter(outfile3);  
		FileWriter out1 = new FileWriter (outfile6);
		for (int i = 0; i < stateNum; i++){             //��ʼ��
			for (int j = 0; j < stateNum; j++){
				stateTransNum[i][j] = 0;
			}
		}
			
			
		String stateFront = "";  //ǰ�ʴ�
		String stateBack = "";  //��ʴ�
		String endFront = "";    //ǰ�ʴ���
		String endBack = "";    //��ʴ���
		int indexFront = 0;      //ǰ�ʴ����ڴ��Ա��е�����
		int indexBack = 0;      //��ʴ����ڴ��Ա��е�����
		FileReader in = new FileReader(outfile2);
		int ch = 0;	
		while ((char)(ch = in.read()) != ' '){ //ȡ�õ�һ��Ҫ�����ϣ��Ĵʴ�
			stateFront = stateFront + (char)ch; 
		}
		endFront = stateFront.substring(stateFront.indexOf("/"), stateFront.length()); //���ʴ������ϣ��
		Symbol temp = new Symbol(stateFront);
		temp.end = endFront;
		temp.prop = (double)temp.num/(double)totalNum;
		wordMap.put(stateFront, temp);		
		while ((ch = in.read()) != -1){
			if ((char)(ch) == ' ' || (char)ch == '\r' || ch == '\n'){//�ո�س�����
				continue;
			}
//			System.out.println("ch: " + ch);
//			System.out.println("I'm structAB" + running++);
			stateBack += (char)ch;
			while ((char) (ch = in.read()) != ' ' && ch != -1 ){
				stateBack += (char) ch;              //ȡ��һ���´ʴ�
			}
//			System.out.println("I'm structABread" + running++);
//			System.out.println("stateB: " + stateB);
				endBack = stateBack.substring(stateBack.indexOf("/"), stateBack.length());
//				System.out.println("endB: " + endB);
				indexFront = stateTable.indexOf(endFront);        
				indexBack = stateTable.indexOf(endBack);
				stateTransNum[indexFront][indexBack]++;           //����״̬ת�ƾ���(ת�ƴ���)
				if (wordMap.containsKey(stateBack)){     
					temp = (Symbol) wordMap.get(stateBack);  //���¹�ϣ��   ����ʴ��Ѿ������ڹ�ϣ��
					temp.num++;           //�ʴ����ִ�������
					temp.prop = (double)temp.num/(double)totalNum;//�ʴ�����Ƶ�ʸ��¡�
					wordMap.put(stateBack, temp);
				}
				else{                         //����ʴ��������ڹ�ϣ����
					temp = new Symbol(stateBack);
					temp.end = endBack;
					temp.prop = (double)temp.num/(double)totalNum;
					wordMap.put(stateBack, temp);
				}
				stateFront = stateBack;
				endFront = endBack;
				stateBack = "";
				endBack = "";
	}
		Collection setv = wordMap.values();  //������ϣ����ֵ��ӡ���ļ���
		Iterator iterator = setv.iterator();
		while(iterator.hasNext()){
			Symbol temp3 = (Symbol)iterator.next();
			out.write(temp3.symbol + " " + temp3.num + " " + temp3.end + 
					"    " + temp3.prop + '\r' + '\n');
		}
		out.close();
		in.close();
		unRegProp = new double [stateNum];   //����δ��½�ʡ�
		for (int i = 0; i < stateNum; i++){
			unRegProp[i] = delta/(double) totalNum;
			out1.write(unRegProp[i] + "       ---");
			for (int j = 0;j < stateNum;j++){
				stateTransProp[i][j] = (double) stateTransNum[i][j]/(double) totalNum;
				out1.write("  " + stateTransProp[i][j]);
			}
			out1.write("" + '\r' + '\n');
		}
		
		out1.close();
//		System.out.println("sTMandSHT ends");
	}
//------------------------------------------------------------------------------------------------------------
// -----------------viterbi�㷨 ʵ�ֱ�ע	----------------------------
	public void viterbi()throws IOException{
		FileReader in = new FileReader(infile2);
		FileWriter out = new FileWriter(outfile5);
		int ch;
		int n = 0;//�����д��صĸ���
		String sentence = "";         // һ������ע����
		while ((ch=in.read()) != -1){
			if ((char)ch == ' ' || (char)ch == '\r' || (char)ch == '\n'){  
				continue;
			}
			n = 0;
			sentence += (char)ch; 
			while ((char)(ch = in.read()) != '��' && (char)(ch) !='��' 
					&&(char)(ch) != '��' && (char)(ch) != '��' && 
					(char)(ch) != '��' && (char)(ch) != '\n' && 
					(char)(ch) != '\r' && ch != -1){
				sentence += (char) ch;
				if ((char)ch == ' '){
					n++;
				}
			}
			//
		    if (ch != -1){
		    	sentence += (char) ch;
		    }
			if (sentence.endsWith("��") || sentence.endsWith("��") || 
					sentence.endsWith("��") || sentence.endsWith("��") || 
					sentence.endsWith("��")){
				n++;
				sentence += " ";
			}
			String [] words = sentence.split(" ");	//�������д�ž��ӵĴ�
			//------------ viterbi�㷨��ʼ
			String wordState="";   //����ʣ����� ��
			Symbol temp = new Symbol();
			VitVar vitArray[][] = new VitVar[n][stateNum]; //ά�رȱ�������
			VitPath pathArray[][] = new VitPath[n][stateNum]; //·������ pathArray[i][j]��ʾ��i+1������j���Ե�ʱ���i���ʵĴ��ԡ�
			for (int i = 0; i < stateNum; i++)  //��ʼ��ά�ر��㷨������ʼ��
			{
				vitArray[0][i] = new VitVar();
				vitArray[0][i].index = 0;
				vitArray[0][i].prop = ((double)staSet.stateSet.get(i).num)/((double)totalNum);
				
				vitArray[0][i].state = staSet.stateSet.get(i).state;
				wordState = words[0] + vitArray[0][i].state;  //���ɴʴ�
				
				if((temp = (Symbol)wordMap.get(wordState)) != null) //�ʴ��ڹ�ϣ����
				{
					vitArray[0][i].prop = vitArray[0][i].prop*temp.prop;
				}
				else         //�ʴ���δ��½��
				{
					vitArray[0][i].prop = vitArray[0][i].prop*unRegProp[i];
				}
			}
			//--------------------------------------------
			double propMax = 0.0;  //������
			String symbolNow = "";   //��ǰ�����µĴ���
			String symbolMax = "";	//�������µĴ���
			double propNow = 0.0;	//��ǰ����
			for (int i = 1; i < n; i++) //���ÿ���ʵ�ѭ��
			{
				for (int j = 0; j < stateNum; j++) //���ÿ�ִ��Ե�ѭ��
				{
					vitArray[i][j] = new VitVar();
					wordState = words[i] + staSet.stateSet.get(j).state;
					propMax = 0.0;
					for (int k = 0; k < stateNum; k++)    //Ѱ�Ҹ�������ѡ��
					{
						propNow = vitArray[i-1][k].prop*stateTransProp[k][j];
						symbolNow = vitArray[i-1][k].state;
						if ((temp = (Symbol)wordMap.get(wordState)) != null) //��ϣ���еĴʴ�
						{
							propNow = propNow*temp.prop;
						} 
						else   //δ��½�ʴ�
						{
							propNow = propNow*unRegProp[j];
						}
						if (propMax < propNow)
						{
							propMax = propNow;
							symbolMax = symbolNow;
						}
	//----------------------------------------------------------------------
					}
					vitArray[i][j].prop = propMax;  //����һ��vitArray��pathArray��
					vitArray[i][j].index = i;
					vitArray[i][j].state = staSet.stateSet.get(j).state;
					pathArray[i-1][j] = new VitPath();
					pathArray[i-1][j].index = i-1;
					pathArray[i-1][j].stateFront = symbolMax;
				}
			}
			//  ----������������--------------------------------
			String []stateResult = new String[n]; //�洢���Ա�ע�Ľ��
			for(int i = 0; i < n; i++)
			{
				stateResult[i] = "";
			}
			propMax = 0.0;
			int index = 0;
			for (int i = 0; i < stateNum; i++)         //Ѱ�Ҹ������Ľ��
			{
				if (vitArray[n-1][i].prop > propMax)
				{
					propMax = vitArray[n-1][i].prop;
					index = i;
				}
			}
			stateResult[n-1] = staSet.stateSet.get(index).state;
			for (int i = n - 2; i >= 0; i--)  //��ʼѭ������������Ա�ע�������
			{
				stateResult[i] = pathArray[i][index].stateFront;
				for (int j = 0; j < staSet.stateSet.size(); j++)
				{
					if (staSet.stateSet.get(j).state == pathArray[i][index].stateFront)
					{
						index = j;
						break;
					}
				}
			}
			//-------------------- viterbi�㷨���� -----------------------
			for (int i = 0; i < n; i++){ //���ļ���������
				out.write(words[i] + stateResult[i] + " ");
			}
			out.write("" + '\r' + '\n');
			sentence = "";
//			System.out.println("I'm Viterbi" + running++);
		}
		out.close();
		in.close();
	}
	
	//----------------------------------------------------------------------------------
	//  �����㷨Ч��
	@SuppressWarnings("resource")
	public void test()throws IOException{
		FileReader inSource = new FileReader(outfile2); //Դ�ļ�
		FileReader inResult = new FileReader(outfile5); //����ļ�
		String source = "";//Դ�е�һ���ʴ�
		String result = "";//��ע�����һ���ʴ�
		
		int chSource;			//Դ�ж�ȡ��һ���ַ�
		int chResult = 0;		//��ע����е��ַ�
		
		while (chResult != -1){
			while ((char)(chResult = inResult.read()) == ' ' || 
					(char)chResult == '\r' || (char)chResult == '\n'){
				continue;
			}
			if(chResult == -1){
				 correctRate = (double)((double)correctWords)/((double)totalWords);
					inSource.close();
					inResult.close();
					JOptionPane.showMessageDialog( null,  
							"׼ȷ��Ϊ��" + String.format("%.2f", correctRate*100) + "%\n��ȷ��ע������" + 
							correctWords + "   " + "���Ʊ�ע������" + totalWords,
		                    "��עЧ��",
		                    JOptionPane.INFORMATION_MESSAGE );
//					System.out.print("׼ȷ��Ϊ��");
//					System.out.println(correctRate);
//					System.out.println("��ȷ��ע������" + correctWords + 
//							"   " + "���Ʊ�ע������" + totalWords);
				return;
			}
			result = result + (char) chResult;
			while ((char)(chSource = inSource.read()) == ' ' || 
					(char)chSource == '\r' || (char)chSource == '\n'){
				continue;
			}
			source += (char)chSource;
			
			while((char)(chResult = inResult.read()) != ' ' && chResult != -1){
//				System.out.println("ch_r = " + chResult);
				result += (char)chResult;
			}
			while ((char)(chSource = inSource.read()) != ' ' && chSource != -1){
//				System.out.println("ch_s = " + chSource);
				source += (char)chSource;
			}
			
			
			if (source.equals(result)){
				correctWords++;
			}
			totalWords++;
			result = "";
			source = "";		
//			System.out.println("I'm score" + running++);
		}		
	}
	
	
	//----------------------------------------------------------------------------------
//	//������
//	public static void main (String[] args)throws IOException {
//		StateAndSymbol sAS = new StateAndSymbol();
//		sAS.stateAndSymbol();
//		sAS.sTMandSHT(0.0001);
//		sAS.viterbi();
//		sAS.test();
//		System.out.println("���");
//	}
}
