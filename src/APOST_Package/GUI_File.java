/**
 * 北京理工大学
 * 1120122018
 * 吴一凡
 */
package APOST_Package;

import javax.swing.JFileChooser;

public class GUI_File {	//文件操作相关的类
	public String getDirectory(){	//显示选择文件夹对话框，获取文件夹路径
		JFileChooser pathChooser = new JFileChooser();
	    pathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );
//	    //设置“打开文件夹对话框”的默认路径---------没有增加设置默认路径的选项
//	    pathChooser.setCurrentDirectory(
//	    		Paths.get("F:\\").toFile()); 
	    //设置对话框标题
	    pathChooser.setDialogTitle("选择文件夹");
	    //设置确定按钮名
	    pathChooser.setApproveButtonText("确定");  
	    //显示打开文件对话框
	    int result = pathChooser.showOpenDialog( null );
	    //按下“取消”按钮，返回空
	    if ( result == JFileChooser.CANCEL_OPTION )
	         return null;

	    String pathName = pathChooser.getSelectedFile().getAbsolutePath();
	    return pathName;
	}
	public String getFile(){	//显示选择文件对话框，获取文件路径
		JFileChooser pathChooser = new JFileChooser();
	    pathChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//	    //设置“打开文件夹对话框”的默认路径---------没有增加设置默认路径的选项
//	    pathChooser.setCurrentDirectory(
//	    		Paths.get("F:\\").toFile()); 
	    //设置对话框标题
	    pathChooser.setDialogTitle("选择文件");
	    //设置确定按钮名
	    pathChooser.setApproveButtonText("确定");  
	    //显示打开文件对话框
	    int result = pathChooser.showOpenDialog( null );
	    //按下“取消”按钮，返回空
	    if ( result == JFileChooser.CANCEL_OPTION )
	         return null;

	    String pathName = pathChooser.getSelectedFile().getAbsolutePath();
	    return pathName;
	}
}
