package com.ui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.border.BevelBorder;


@SuppressWarnings("serial")
public class RenrenHomeUI extends JFrame {
	private JMenuBar mBar;;
	private JMenu menu;
	private JMenuItem[] items = {new JMenuItem("renren"),new JMenuItem("bbs")};
	
	public void init(){
		mBar = new JMenuBar();
		menu = new JMenu("WebSite");

		for(int i=0;i<items.length;i++){
			menu.add(items[i]);
		}
		
		mBar.add(menu);
		mBar.setBorder(new BevelBorder(BevelBorder.RAISED));
		setJMenuBar(mBar);
		
		setSize(400, 400);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setVisible(true);
		
	}
	
	public RenrenHomeUI(){
		init();
	}
	
  public static void main(String[] args){
	  new RenrenHomeUI();
  }
}
