/**
 * 北京理工大学
 * 1120122018
 * 吴一凡
 */
package APOST_Package;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class GUI {
	private JFrame frame = new JFrame();
	private JButton jbCorpus = new JButton();
	private JButton jbTestR = new JButton();
	private JButton jbResult = new JButton();
	private JButton jbSeg = new JButton();
	private JLabel jlCorpus = new JLabel();		
	private JLabel jlTestR = new JLabel();
	private JLabel jlResult = new JLabel();
	private JLabel jlOption = new JLabel();
	private JTextField jtfCorpus = new JTextField();
	private JTextField jtfTestR = new JTextField();
	private JTextField jtfResult = new JTextField();
	private JRadioButton jrbFMM = new JRadioButton("仅标注",true);
	private JRadioButton jrbBMM = new JRadioButton("标注后查看准确率",true);
	static private ButtonGroup radioGroup = new ButtonGroup() ;
	
	//getter and setter 方法
	
	public JTextField getJtfCorpus() {
		return jtfCorpus;
	}
	public JTextField getJtfTestR() {
		return jtfTestR;
	}
	public JTextField getJtfResult() {
		return jtfResult;
	}
	
	public GUI(){	//构造方法建立GUI窗口
		setupFrame();
		setComponent();
	}
	
	private void setupFrame() {	//建立框架，并加入所有构件，以null方式布局（X-Y方式）
    	JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setSize(500,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("中文词性自动标注程序");
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
        //----------------------加入构件---------------
        frame.add(jbCorpus);
        frame.add(jbTestR);
        frame.add(jbResult);
        frame.add(jbSeg);
        frame.add(jlCorpus);
        frame.add(jlTestR);
        frame.add(jlResult);
        frame.add(jlOption);
        frame.add(jtfCorpus);
        frame.add(jtfTestR);
        frame.add(jtfResult);
        frame.add(jrbFMM);
        frame.add(jrbBMM);
    }
	private void setLabel(){	//设置标签位置和文字属性
		jlCorpus.setBounds(20,20,200,40);
		jlCorpus.setText("语料库");
		jlTestR.setBounds(20,110,200,40);
		jlTestR.setText("源文件");
		jlResult.setBounds(20,200,200,40);
		jlResult.setText("生成文件所在文件夹");
		jlOption.setBounds(20,290,200,40);
		jlOption.setText("选择分词类型");
	}
	private void setButton(){	//设置按钮位置和文字属性，并添加监听器
		jbCorpus.setBounds(380, 70, 100, 30);
		jbCorpus.setText("选择语料库");
		jbTestR.setBounds(380, 160, 100, 30);
		jbTestR.setText("选择源文件");
		jbResult.setBounds(380, 250, 100, 30);
		jbResult.setText("选择文件夹");
		jbSeg.setBounds(350, 320, 100, 30);
		jbSeg.setText("开始分词");
		
		addActionListener();
	}
	private void addActionListener(){	//对所有的按钮Button添加监听
		jbCorpus.addActionListener( new ActionListener() {	//添加监听
	    	   
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				GUI_File gf = new GUI_File();
				String s = null;
				s = gf.getFile();
				jtfCorpus.setText(s);
			}
		});
		jbTestR.addActionListener( new ActionListener() {	//添加监听
	    	   
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				GUI_File gf = new GUI_File();
				String s = null;
				s = gf.getFile();
				jtfTestR.setText(s);
			}
		});
		jbResult.addActionListener( new ActionListener() {	//添加监听
	    	   
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				GUI_File gf = new GUI_File();
				String s = null;
				s = gf.getDirectory();
				jtfResult.setText(s);
			}
		});
		
		jbSeg.addActionListener( new ActionListener() {	//添加监听
	    	   
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//判断语料库、源文件和目标文件夹都已选择
				if(jtfCorpus.getText().isEmpty()){
					JOptionPane.showMessageDialog( null,  
							"请选择语料库",
		                    "请选择文件/文件夹",
		                    JOptionPane.INFORMATION_MESSAGE );
					return ;
				}else if(jtfTestR.getText().isEmpty()){
					JOptionPane.showMessageDialog( null,  
							"请选择源文件",
		                    "请选择文件/文件夹",
		                    JOptionPane.INFORMATION_MESSAGE );
					return ;
				}else if(jtfResult.getText().isEmpty()){
					JOptionPane.showMessageDialog( null,  
							"请选择生成文件所在的目录",
		                    "请选择文件/文件夹",
		                    JOptionPane.INFORMATION_MESSAGE );
					return ;
				}
				//运行标注算法
				StateAndSymbol sAS = new StateAndSymbol();
				frame.setTitle("正在标注...");
				if(jrbFMM.isSelected()){
					JOptionPane.showMessageDialog( null,  
							"标注时间由语料库的大小正相关，语料库越大，标注时间越长，请耐心等候",
		                    "正在标注...",
		                    JOptionPane.INFORMATION_MESSAGE );
					try {
						sAS.stateAndSymbol();
						sAS.sTMandSHT(0.0001);
						sAS.viterbi();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog( null,  
							"标注完成\n标注后的文件保存于目标路径中",
		                    "标注完成",
		                    JOptionPane.INFORMATION_MESSAGE );
				}
				else{
					JOptionPane.showMessageDialog( null,  
							"标注时间由语料库的大小正相关，语料库越大，标注时间越长，请耐心等候",
		                    "正在标注...",
		                    JOptionPane.INFORMATION_MESSAGE );
					try {
						sAS.stateAndSymbol();
						sAS.sTMandSHT(0.0001);
						sAS.viterbi();
						sAS.test();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog( null,  
							"标注完成\n标注后的文件保存于目标路径中",
		                    "标注完成",
		                    JOptionPane.INFORMATION_MESSAGE );
				}
				frame.setTitle("词性标注完毕");
			}
		});
	}
	private void setRadioButton(){	//设置单选按钮，将单选按钮加入到按钮组中
		radioGroup.add(jrbFMM);
		radioGroup.add(jrbBMM);
		jrbFMM.setBounds(20, 320, 80, 40);
		jrbBMM.setBounds(120, 320, 150, 40);
	}
	private void setTextField(){	//设置文本框
		jtfCorpus.setBounds(20, 70, 350, 30);
		jtfCorpus.setEditable(false);
		jtfTestR.setBounds(20, 160, 350, 30);
		jtfTestR.setEditable(false);
		jtfResult.setBounds(20, 250, 350, 30);
		jtfResult.setEditable(false);
	}
	private void setComponent(){	//设置GUI控件
		setLabel();
		setButton();
		setTextField();
		setRadioButton();
	}
}
