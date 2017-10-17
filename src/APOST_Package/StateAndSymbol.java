/**
 * 北京理工大学
 * 1120122018
 * 吴一凡
 * 主程序
 * stateAndSymbol()  构造字符集与状态集
 * sTMandSHT(double delta)  构造状态转移矩阵(state transfer matrix)与
 * 						         字符哈希表(symbol hash table)
 * viterbi()   基于隐马尔可夫模型利用viterbi算法实现词类标注
 * test()    标注效果测评
 */
package APOST_Package;


import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;




public class StateAndSymbol {
	StateSet staSet = new StateSet();        //状态集
	SymbolSet sybSet=new SymbolSet();        //字符集
	List<String> stateTable=new ArrayList<String>();  //储存所有词性，用于计算状态转换表的坐标
	int totalNum=0;            //共有词数
	int stateNum=0;            //共有词性数
	int stateTransNum[][];     //状态转换次数表
	double stateTransProp[][];         //状态转换概率表
	double unRegProp[];          //未登陆词特定状态的出现概率
	@SuppressWarnings("rawtypes")
	Map wordMap =new HashMap();  //词表
	
	int totalWords=0;      //共标注词数
	int correctWords=0;    //标注正确数
	double correctRate=0.0; //正确率
//	int running = 0;		//测试用，看程序是不是在跑
	
	GUI gui = Start.getGUI();
	String dic = gui.getJtfResult().getText();	//生成文件所在路径
	File infile1 = new File (gui.getJtfCorpus().getText());     //语料文件，北大语料库
	File infile2 = new File (gui.getJtfTestR().getText());      // 真正用于测试标注效果的测试文档
	File outfile1 = new File (dic + "\\StateSet.txt");       //状态集文件StateSet.txt
	File outfile2 = new File (dic + "\\SymbolSet.txt");      //字符集文件SymbolSet.txt
	File outfile3 = new File (dic + "\\SymbolProp.txt");  //字符＋概率文件SymbolProp.txt
	File outfile4 = new File (dic + "\\TestW.txt");    //  加工好的可以用于测试的文档TestW.txt
	File outfile5 = new File (dic + "\\Result.txt");       // 标注结果Result.txt
	File outfile6 =new File (dic + "\\StateTransMatrics.txt");  //  状态转移矩阵文件StateTransMatrics.txt
	
	//------------------------------------------------------------------------------
	//构造字符集与状态集，语料的前期加工
	public void stateAndSymbol()throws IOException{
		
		FileReader in = new FileReader(infile1);//语料文件
		FileWriter out1 =new FileWriter(outfile1);//状态集文件
		FileWriter out2 = new FileWriter(outfile2);//字符集文件
		FileWriter out4 = new FileWriter(outfile4);//  加工好的可以用于测试的文档
		State temp1 = new State("/v");          // 预先插入一定存在的词性，减少判断次数。
		temp1.num = 0;
		staSet.stateSet.add(temp1);
		stateTable.add("/v");
		
		int ch;
		String element = "";    //词+词性 串
		String end = "";       // 词性
		String symbol = "";     // 词
		int flag = 1;           //词性是否已经存在与表中的标志
		while ((ch = in.read()) != -1){                     //读文件
			if ((char)(ch) == '\r' || (char)(ch) == '\n'){
				out2.write(ch);
				out4.write(ch);
				continue;
			}
			if ((char)ch == ' ' || (char)(ch) == '[' || 
					(char)(ch)==']'){ 					//忽略 上述符号
				continue;
			}
			
			element += (char)ch;
			while((char) (ch = in.read()) != ' ' && (char)(ch) != ']'
					&& (char)(ch) != '\n'){ 			//构造一个词素
				element += (char)ch;
				}
				if ((element.length() == 21 && element.endsWith("/m"))
						|| element.length() == 2 || element.length()==1){
					element="";               			//词素是日期的话忽略
					continue;
				}
				else{
					int index = element.lastIndexOf("/");   //分离词素的词和词性部分
					if (index < element.length() && index != 0){
						end = element.substring(element.lastIndexOf("/"), element.length()); 
						symbol = element.substring(0, element.lastIndexOf("/"));
					}
					
					flag = 1;
					
						for (int i = 0; i < staSet.stateSet.size(); i++){  // 判断词性是否已经存在于表中
							if(staSet.stateSet.get(i).state.equals(end)){  //存在
								staSet.stateSet.get(i).num++;			//该词性出现次数++					
								out2.write(element+" ");
								out4.write(symbol+" ");
								element = "";
								symbol = "";
								end = "";
								flag  =0;
								break;
							}
							
						}
						if (flag == 1){       //不存在  相词性表（状态表中）插入新元素
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
		for (int i = 0; i < staSet.stateSet.size(); i++){//计算预料中词的总数
			out1.write(stateTable.get(i) + "  ");
			totalNum = totalNum+staSet.stateSet.get(i).num; 
		}
		
		stateNum = staSet.stateSet.size();  //状态总数（词性总数）
//		System.out.println(staSet.stateSet.size());
//		System.out.println(totalNum);
		in.close();
		out1.close();
		out2.close();
		out4.close();
		
	}
	
	//--------------------------------------------------------------------------------------------------
	// 构造状态转移矩阵 构造字符哈希表  求隐马尔可夫模型的参数  
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void sTMandSHT(double delta)throws IOException{
		stateTransNum = new int [stateNum][stateNum];  //状态转化表 （出现次数）
		stateTransProp = new double [stateNum][stateNum]; // 状态转化表 (频率)
		FileWriter out = new FileWriter(outfile3);  
		FileWriter out1 = new FileWriter (outfile6);
		for (int i = 0; i < stateNum; i++){             //初始化
			for (int j = 0; j < stateNum; j++){
				stateTransNum[i][j] = 0;
			}
		}
			
			
		String stateFront = "";  //前词串
		String stateBack = "";  //后词串
		String endFront = "";    //前词词性
		String endBack = "";    //后词词性
		int indexFront = 0;      //前词词性在词性表中的坐标
		int indexBack = 0;      //后词词性在词性表中的坐标
		FileReader in = new FileReader(outfile2);
		int ch = 0;	
		while ((char)(ch = in.read()) != ' '){ //取得第一个要插入哈希表的词串
			stateFront = stateFront + (char)ch; 
		}
		endFront = stateFront.substring(stateFront.indexOf("/"), stateFront.length()); //将词串插入哈希表
		Symbol temp = new Symbol(stateFront);
		temp.end = endFront;
		temp.prop = (double)temp.num/(double)totalNum;
		wordMap.put(stateFront, temp);		
		while ((ch = in.read()) != -1){
			if ((char)(ch) == ' ' || (char)ch == '\r' || ch == '\n'){//空格回车或换行
				continue;
			}
//			System.out.println("ch: " + ch);
//			System.out.println("I'm structAB" + running++);
			stateBack += (char)ch;
			while ((char) (ch = in.read()) != ' ' && ch != -1 ){
				stateBack += (char) ch;              //取得一个新词串
			}
//			System.out.println("I'm structABread" + running++);
//			System.out.println("stateB: " + stateB);
				endBack = stateBack.substring(stateBack.indexOf("/"), stateBack.length());
//				System.out.println("endB: " + endB);
				indexFront = stateTable.indexOf(endFront);        
				indexBack = stateTable.indexOf(endBack);
				stateTransNum[indexFront][indexBack]++;           //更新状态转移矩阵(转移次数)
				if (wordMap.containsKey(stateBack)){     
					temp = (Symbol) wordMap.get(stateBack);  //更新哈希表   如果词串已经存在于哈希表
					temp.num++;           //词串出现次数更新
					temp.prop = (double)temp.num/(double)totalNum;//词串出现频率更新。
					wordMap.put(stateBack, temp);
				}
				else{                         //如果词串不存在于哈希表中
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
		Collection setv = wordMap.values();  //遍历哈希表，将值打印到文件中
		Iterator iterator = setv.iterator();
		while(iterator.hasNext()){
			Symbol temp3 = (Symbol)iterator.next();
			out.write(temp3.symbol + " " + temp3.num + " " + temp3.end + 
					"    " + temp3.prop + '\r' + '\n');
		}
		out.close();
		in.close();
		unRegProp = new double [stateNum];   //处理未登陆词。
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
// -----------------viterbi算法 实现标注	----------------------------
	public void viterbi()throws IOException{
		FileReader in = new FileReader(infile2);
		FileWriter out = new FileWriter(outfile5);
		int ch;
		int n = 0;//句子中词素的个数
		String sentence = "";         // 一个待标注句子
		while ((ch=in.read()) != -1){
			if ((char)ch == ' ' || (char)ch == '\r' || (char)ch == '\n'){  
				continue;
			}
			n = 0;
			sentence += (char)ch; 
			while ((char)(ch = in.read()) != '。' && (char)(ch) !='？' 
					&&(char)(ch) != '！' && (char)(ch) != '：' && 
					(char)(ch) != '；' && (char)(ch) != '\n' && 
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
			if (sentence.endsWith("。") || sentence.endsWith("？") || 
					sentence.endsWith("！") || sentence.endsWith("：") || 
					sentence.endsWith("；")){
				n++;
				sentence += " ";
			}
			String [] words = sentence.split(" ");	//数组项中存放句子的词
			//------------ viterbi算法开始
			String wordState="";   //汉语词＋词性 串
			Symbol temp = new Symbol();
			VitVar vitArray[][] = new VitVar[n][stateNum]; //维特比变量数组
			VitPath pathArray[][] = new VitPath[n][stateNum]; //路径数组 pathArray[i][j]表示第i+1个词是j词性的时候第i个词的词性。
			for (int i = 0; i < stateNum; i++)  //初始化维特比算法，处理开始词
			{
				vitArray[0][i] = new VitVar();
				vitArray[0][i].index = 0;
				vitArray[0][i].prop = ((double)staSet.stateSet.get(i).num)/((double)totalNum);
				
				vitArray[0][i].state = staSet.stateSet.get(i).state;
				wordState = words[0] + vitArray[0][i].state;  //构成词串
				
				if((temp = (Symbol)wordMap.get(wordState)) != null) //词串在哈希表中
				{
					vitArray[0][i].prop = vitArray[0][i].prop*temp.prop;
				}
				else         //词串是未登陆词
				{
					vitArray[0][i].prop = vitArray[0][i].prop*unRegProp[i];
				}
			}
			//--------------------------------------------
			double propMax = 0.0;  //最大概率
			String symbolNow = "";   //当前概率下的词性
			String symbolMax = "";	//最大概率下的词性
			double propNow = 0.0;	//当前概率
			for (int i = 1; i < n; i++) //针对每个词的循环
			{
				for (int j = 0; j < stateNum; j++) //针对每种词性的循环
				{
					vitArray[i][j] = new VitVar();
					wordState = words[i] + staSet.stateSet.get(j).state;
					propMax = 0.0;
					for (int k = 0; k < stateNum; k++)    //寻找概率最大的选择
					{
						propNow = vitArray[i-1][k].prop*stateTransProp[k][j];
						symbolNow = vitArray[i-1][k].state;
						if ((temp = (Symbol)wordMap.get(wordState)) != null) //哈希表中的词串
						{
							propNow = propNow*temp.prop;
						} 
						else   //未登陆词串
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
					vitArray[i][j].prop = propMax;  //处理一个vitArray和pathArray项
					vitArray[i][j].index = i;
					vitArray[i][j].state = staSet.stateSet.get(j).state;
					pathArray[i-1][j] = new VitPath();
					pathArray[i-1][j].index = i-1;
					pathArray[i-1][j].stateFront = symbolMax;
				}
			}
			//  ----构建词性向量--------------------------------
			String []stateResult = new String[n]; //存储词性标注的结果
			for(int i = 0; i < n; i++)
			{
				stateResult[i] = "";
			}
			propMax = 0.0;
			int index = 0;
			for (int i = 0; i < stateNum; i++)         //寻找概率最大的结果
			{
				if (vitArray[n-1][i].prop > propMax)
				{
					propMax = vitArray[n-1][i].prop;
					index = i;
				}
			}
			stateResult[n-1] = staSet.stateSet.get(index).state;
			for (int i = n - 2; i >= 0; i--)  //开始循环，倒序构造词性标注结果数组
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
			//-------------------- viterbi算法结束 -----------------------
			for (int i = 0; i < n; i++){ //向文件中输出结果
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
	//  测试算法效果
	@SuppressWarnings("resource")
	public void test()throws IOException{
		FileReader inSource = new FileReader(outfile2); //源文件
		FileReader inResult = new FileReader(outfile5); //结果文件
		String source = "";//源中的一个词串
		String result = "";//标注结果的一个词串
		
		int chSource;			//源中读取的一个字符
		int chResult = 0;		//标注结果中的字符
		
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
							"准确率为：" + String.format("%.2f", correctRate*100) + "%\n正确标注词数：" + 
							correctWords + "   " + "共计标注词数：" + totalWords,
		                    "标注效果",
		                    JOptionPane.INFORMATION_MESSAGE );
//					System.out.print("准确率为：");
//					System.out.println(correctRate);
//					System.out.println("正确标注词数：" + correctWords + 
//							"   " + "共计标注词数：" + totalWords);
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
//	//主程序
//	public static void main (String[] args)throws IOException {
//		StateAndSymbol sAS = new StateAndSymbol();
//		sAS.stateAndSymbol();
//		sAS.sTMandSHT(0.0001);
//		sAS.viterbi();
//		sAS.test();
//		System.out.println("完成");
//	}
}
