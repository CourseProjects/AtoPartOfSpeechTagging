/**
 * ��������ѧ
 * 1120122018
 * ��һ��
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
	private JRadioButton jrbFMM = new JRadioButton("����ע",true);
	private JRadioButton jrbBMM = new JRadioButton("��ע��鿴׼ȷ��",true);
	static private ButtonGroup radioGroup = new ButtonGroup() ;
	
	//getter and setter ����
	
	public JTextField getJtfCorpus() {
		return jtfCorpus;
	}
	public JTextField getJtfTestR() {
		return jtfTestR;
	}
	public JTextField getJtfResult() {
		return jtfResult;
	}
	
	public GUI(){	//���췽������GUI����
		setupFrame();
		setComponent();
	}
	
	private void setupFrame() {	//������ܣ����������й�������null��ʽ���֣�X-Y��ʽ��
    	JFrame.setDefaultLookAndFeelDecorated(true);
        frame.setSize(500,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("���Ĵ����Զ���ע����");
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
        //----------------------���빹��---------------
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
	private void setLabel(){	//���ñ�ǩλ�ú���������
		jlCorpus.setBounds(20,20,200,40);
		jlCorpus.setText("���Ͽ�");
		jlTestR.setBounds(20,110,200,40);
		jlTestR.setText("Դ�ļ�");
		jlResult.setBounds(20,200,200,40);
		jlResult.setText("�����ļ������ļ���");
		jlOption.setBounds(20,290,200,40);
		jlOption.setText("ѡ��ִ�����");
	}
	private void setButton(){	//���ð�ťλ�ú��������ԣ�����Ӽ�����
		jbCorpus.setBounds(380, 70, 100, 30);
		jbCorpus.setText("ѡ�����Ͽ�");
		jbTestR.setBounds(380, 160, 100, 30);
		jbTestR.setText("ѡ��Դ�ļ�");
		jbResult.setBounds(380, 250, 100, 30);
		jbResult.setText("ѡ���ļ���");
		jbSeg.setBounds(350, 320, 100, 30);
		jbSeg.setText("��ʼ�ִ�");
		
		addActionListener();
	}
	private void addActionListener(){	//�����еİ�ťButton��Ӽ���
		jbCorpus.addActionListener( new ActionListener() {	//��Ӽ���
	    	   
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				GUI_File gf = new GUI_File();
				String s = null;
				s = gf.getFile();
				jtfCorpus.setText(s);
			}
		});
		jbTestR.addActionListener( new ActionListener() {	//��Ӽ���
	    	   
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				GUI_File gf = new GUI_File();
				String s = null;
				s = gf.getFile();
				jtfTestR.setText(s);
			}
		});
		jbResult.addActionListener( new ActionListener() {	//��Ӽ���
	    	   
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				GUI_File gf = new GUI_File();
				String s = null;
				s = gf.getDirectory();
				jtfResult.setText(s);
			}
		});
		
		jbSeg.addActionListener( new ActionListener() {	//��Ӽ���
	    	   
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//�ж����Ͽ⡢Դ�ļ���Ŀ���ļ��ж���ѡ��
				if(jtfCorpus.getText().isEmpty()){
					JOptionPane.showMessageDialog( null,  
							"��ѡ�����Ͽ�",
		                    "��ѡ���ļ�/�ļ���",
		                    JOptionPane.INFORMATION_MESSAGE );
					return ;
				}else if(jtfTestR.getText().isEmpty()){
					JOptionPane.showMessageDialog( null,  
							"��ѡ��Դ�ļ�",
		                    "��ѡ���ļ�/�ļ���",
		                    JOptionPane.INFORMATION_MESSAGE );
					return ;
				}else if(jtfResult.getText().isEmpty()){
					JOptionPane.showMessageDialog( null,  
							"��ѡ�������ļ����ڵ�Ŀ¼",
		                    "��ѡ���ļ�/�ļ���",
		                    JOptionPane.INFORMATION_MESSAGE );
					return ;
				}
				//���б�ע�㷨
				StateAndSymbol sAS = new StateAndSymbol();
				frame.setTitle("���ڱ�ע...");
				if(jrbFMM.isSelected()){
					JOptionPane.showMessageDialog( null,  
							"��עʱ�������Ͽ�Ĵ�С����أ����Ͽ�Խ�󣬱�עʱ��Խ���������ĵȺ�",
		                    "���ڱ�ע...",
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
							"��ע���\n��ע����ļ�������Ŀ��·����",
		                    "��ע���",
		                    JOptionPane.INFORMATION_MESSAGE );
				}
				else{
					JOptionPane.showMessageDialog( null,  
							"��עʱ�������Ͽ�Ĵ�С����أ����Ͽ�Խ�󣬱�עʱ��Խ���������ĵȺ�",
		                    "���ڱ�ע...",
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
							"��ע���\n��ע����ļ�������Ŀ��·����",
		                    "��ע���",
		                    JOptionPane.INFORMATION_MESSAGE );
				}
				frame.setTitle("���Ա�ע���");
			}
		});
	}
	private void setRadioButton(){	//���õ�ѡ��ť������ѡ��ť���뵽��ť����
		radioGroup.add(jrbFMM);
		radioGroup.add(jrbBMM);
		jrbFMM.setBounds(20, 320, 80, 40);
		jrbBMM.setBounds(120, 320, 150, 40);
	}
	private void setTextField(){	//�����ı���
		jtfCorpus.setBounds(20, 70, 350, 30);
		jtfCorpus.setEditable(false);
		jtfTestR.setBounds(20, 160, 350, 30);
		jtfTestR.setEditable(false);
		jtfResult.setBounds(20, 250, 350, 30);
		jtfResult.setEditable(false);
	}
	private void setComponent(){	//����GUI�ؼ�
		setLabel();
		setButton();
		setTextField();
		setRadioButton();
	}
}
