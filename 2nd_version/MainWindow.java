package xjx;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;  
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.regex.*;

import javax.swing.event.CaretEvent;  
import javax.swing.event.CaretListener;


/**
 * 基于LinkedList实现栈
 * 在LinkedList实力中只选择部分基于栈实现的接口
 */
class MatrixStack<E> {
    private LinkedList<E> S = new LinkedList<E>();
    //入栈
    public void Push(E e){
        S.addFirst(e);
    }
    //查看栈顶元素但不移除
    public E GetTop(){
        return S.getFirst();
    }
    //出栈
    public E Pop(){
        return S.removeFirst();
    }
    //判空
    public boolean EmptyStack(){
        return S.isEmpty();
    }
}

@SuppressWarnings("serial")
class ExpressionInputException extends Exception{
	public ExpressionInputException(){}
	public ExpressionInputException(String smg){
		super(smg);
	}
}

public class MainWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1877974685325498861L;
	private static JTextArea J212;
	private static JTextArea J222;
	private CmdTextArea workspace;
	private static int ptr = 0;	//ptr记录工作空间一共有多少个数组
	
	private static StringBuffer textBuffer = new StringBuffer();  
	private static int currentDot = -1;  
	private static boolean isAllowedInputArea = false;  
	private static int currentKeyCode = 0;  
	private static boolean isConsume = false;
	private static int current_pos;
	
	public MainWindow(){
		super("Matrix Caculator By XJX"); 
		Image img = Toolkit.getDefaultToolkit().getImage("title.png");//窗口图标
		setIconImage(img); 
	    setSize(900, 650);
	    setResizable(false);
	    setLocationRelativeTo(null);
	    setLayout(null);  
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	    
	    String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
	    try {
	    	UIManager.setLookAndFeel(lookAndFeel);
	    } catch (ClassNotFoundException e1) {
	    	e1.printStackTrace();
	    } catch (InstantiationException e1) {
	    	e1.printStackTrace();
	    } catch (IllegalAccessException e1) {
	    	e1.printStackTrace();
	    } catch (UnsupportedLookAndFeelException e1) {
	    	e1.printStackTrace();
	    }
	    
	    //菜单栏部分
	    JMenuBar bar = new JMenuBar();
	    bar.setBackground(Color.white);
	    setJMenuBar(bar);
	    JMenu setMenu = new JMenu("设置");
	    JMenu helpMenu = new JMenu("帮助");
	    JMenuItem about = new JMenuItem("关于");
	    JMenuItem help = new JMenuItem("查看帮助");
	    JMenuItem reset = new JMenuItem("重置");
	    helpMenu.add(about);
	    helpMenu.add(help);
	    bar.add(setMenu);
	    bar.add(helpMenu);
	    JCheckBoxMenuItem linewrap = new JCheckBoxMenuItem("自动换行");
  		linewrap.setSelected(false);
  		setMenu.add(linewrap);
  		setMenu.add(reset);
  		
  		linewrap.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		if(linewrap.getState())//激活自动换行
        		{
        			workspace.setLineWrap(true);
        			workspace.setWrapStyleWord(true) ;
        		}
        		else
        		{
        			workspace.setLineWrap(false);
        			workspace.setWrapStyleWord(false) ;
        		}
        	}
  		});
	    
  		about.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		new About();
        	}
  		});
  		
  		help.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		new Help();
        	}
  		});
  		
  		reset.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		//重置
        		textBuffer = new StringBuffer();  
        		currentDot = -1;  
        		isAllowedInputArea = false;  
        		currentKeyCode = 0;  
        		isConsume = false;
        		workspace.setText("");
        		workspace.append(">>");  
        	    workspace.requestFocus();  
        	    workspace.setCaretPosition(workspace.getText().length());
        	}
  		});
  		
  		
	    workspace = new CmdTextArea();
	    workspace.addKeyListener(workspace);  
	    workspace.addCaretListener(workspace);  
//	    workspace.setFont(new Font("微软雅黑", Font.PLAIN, 15));
	    workspace.setFont(new Font("宋体",Font.BOLD,15));
	    JScrollPane scroll = new JScrollPane(workspace, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
	    		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);	        
	    add(scroll);
	    scroll.setBounds(0, 0, 500, 600);
	   
	    JPanel J2 = new JPanel();
		J2.setLayout(new GridLayout(2,1,0,2));
		J2.setBackground(Color.white);
		J2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black,2),
				"",TitledBorder.CENTER,TitledBorder.TOP,new Font("微软雅黑",Font.BOLD,0)));
		add(J2);
		J2.setBounds(500, 0, 393, 600);
		
		JPanel J21 = new JPanel();
		J21.setBackground(Color.white);
		J21.setLayout(new BorderLayout());
		J21.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray,2),
				"workspace",TitledBorder.LEFT,TitledBorder.TOP,new Font("微软雅黑",Font.BOLD,15)));
		JPanel J22 = new JPanel();
		J22.setBackground(Color.white);
		J22.setLayout(new BorderLayout());
		J22.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray,2),
				"command history",TitledBorder.LEFT,TitledBorder.TOP,new Font("微软雅黑",Font.BOLD,15)));
		J2.add(J21);
		J2.add(J22);
		
		J212 = new JTextArea();
		JScrollPane scroll212 = new JScrollPane(J212, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
	    		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		J212.setFont(new Font("微软雅黑",Font.BOLD,15));
		J212.setFont(new Font("宋体",Font.BOLD,15));
		J212.setEditable(false);
		J212.setVisible(true);
		J212.setLineWrap(true);//自动换行
		JButton J213 = new JButton("Clear workspace");
		J213.setFont(new Font("微软雅黑",Font.BOLD,20));
		
		J213.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						J212.setText("");
						ptr = 0;//把Buff数组清空
					}
				}
		);
		
		J21.add(scroll212,BorderLayout.CENTER);
		J21.add(J213,BorderLayout.SOUTH);
		J222 = new JTextArea();
		JScrollPane scroll222 = new JScrollPane(J222, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
	    		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		J222.setFont(new Font("微软雅黑",Font.BOLD,15));
		J222.setFont(new Font("宋体",Font.BOLD,15));
		J222.setEditable(false);
		J222.setLineWrap(true);//自动换行
		JButton J223 = new JButton("Clear history");
		J223.setFont(new Font("微软雅黑",Font.BOLD,20));
		
		J223.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						J222.setText("");
					}
				}
		);
		
		J22.add(scroll222,BorderLayout.CENTER);
		J22.add(J223,BorderLayout.SOUTH);
	      
	    workspace.append(">>");  
	    workspace.requestFocus();  
	    workspace.setCaretPosition(workspace.getText().length());
	    
	    setVisible(true);
	}
	
	
	/*
	 * CmdTextArea
	 * @author 
	 * http://blog.csdn.net/chen_nan/article/details/8762752
	 */
	
	public static class CmdTextArea extends JTextArea implements KeyListener,CaretListener {
		
		private static final long serialVersionUID = 1L;  
//		private StringBuffer textBuffer = new StringBuffer();  
//		private int currentDot = -1;  
//		private boolean isAllowedInputArea = false;  
//		private int currentKeyCode = 0;  
//		private boolean isConsume = false;
//		int current_pos;

		private static Matrix[] Buff = new Matrix[10];//J212区域最多保留10个矩阵

		/*
		 * @author XJX
		 * 识别矩阵输入的正则表达式
		 * 模仿matlab的输入格式
		 */
		String pattern = "^\\s*[A-Za-z][A-Za-z\\d]*\\s*=\\s*\\["
				+ "(((\\s*(\\+|-)?\\d+(\\.\\d+)?\\s*,)|(\\s*(\\+|-)?\\d+(\\.\\d+)?\\s+(,)?))*\\s*(\\+|-)?\\d+(\\.\\d+)?\\s*;)*"
				+ "((\\s*(\\+|-)?\\d+(\\.\\d+)?\\s*,)|(\\s*(\\+|-)?\\d+(\\.\\d+)?\\s+(,)?))*\\s*(\\+|-)?\\d+(\\.\\d+)?\\s*"
				+ "\\]\\s*$";
		/*for example
		 * A = [1.2 23,-124 ; +12.0,2.3,4] 
		 *  a0fo =[12 22 22;22 22 22 ;22  22    22]
		 *a0asf   =[12,23;23,23;    45,34,34] 
		 *
		 * A = [12.2 23.22,124.1,777.0 1;12,23,4] 
		 * a0fo =[12 22.22 22;22.22 22 22.22 ;221.2  22    22]
		 *a0asf   =[12,2.3;2.3,23;    3.4,3.4]
		 */
		
		String pattern_inv = "^\\s*(inv|INV|inverse|INVERSE)\\s*\\(\\s*[A-Za-z][A-Za-z\\d]*\\s*\\)\\s*$";
		
		public CmdTextArea() {  
			super();  
		}
		
		//确认输入的矩阵表达式是否合法,即每行的列数是否相等，若是，返回一个识别的矩阵，若不是，返回一个null矩阵
		public Matrix IsMatrixRight(String input) {
			String name;
			int row = 0,col = 0;//计算有多少行和多少列
			Matrix final_result = null;
			double[][] result;
			double[] result_temp = new double[100];//注意输入的矩阵最多只有100个数
			int result_temp_ptr = 0;
			for(int i = 0;i < input.length();i++)
			{
				if(input.charAt(i) == ';')
					row++;
			}
			row++;
			int[] column = new int[row];//column数组记录每行有多少列
	
			int start = input.indexOf("[");
			String temp = input.substring(start, input.length());
			String temp2 = input.substring(0, start);
			temp2 = temp2.replaceAll(" ", "");//过滤空格
			name = temp2.substring(0, temp2.length()-1);//去掉最后的=,得到名字
			double e = 0;
			int col_temp = 0;
			int ptr = 0,ptrtemp = 0;//ptr指向下一个将要读入的字符
			String Numbuff = "";//缓存读到的数字字符和小数点，用以把读到的数字字符转换成相应的数字
			char c;
    
			c=temp.charAt(ptr++);
			for(int i = 0;i < row;i++)//统计每一行的列数，判断每行的列数是否相等，若相等，则把输入的矩阵表达式转换为二维数组
			{
				while(c != ';' && c != ']')//证明没有读到一行的行末
				{
					if( c>=48&&c<=57 || c == '+' || c == '-') //读到数字或者正负号
					{
						ptrtemp = ptr;
						Numbuff += c;
						//判断数字字符后面一个字符是否为数字字符或者小数点
						while( ( temp.charAt(ptrtemp) >= 48 && temp.charAt(ptrtemp ) <= 57) || temp.charAt(ptrtemp) == '.')
						{
							Numbuff += temp.charAt(ptrtemp);
							ptrtemp++;
						}
						e = Double.valueOf(Numbuff).doubleValue();
						//注意每次判断处理完数字,以下这些变量要重置为初始值
						Numbuff = "";
						ptr = ptrtemp;//处理完一串数字字符，记得把ptr移到数字字符串的下一个位置
						//result[i][col_temp] = e;//第i行第col_temp列的元素记录到数组中
						result_temp[result_temp_ptr++] = e;
						col_temp++;
					}
					if(c == ' ' || c == ',' || c == '['){}
					c = temp.charAt(ptr++);
				}
				column[i] = col_temp;
				col_temp = 0;
				if(i < row-1)
				{
					c = temp.charAt(ptr++);
				}
			}
	
			col = column[0];
			for(int i=1;i<row;i++)
			{
				if(column[i] != col)
				{
					System.out.println("Error!");//出现列数不相等
					final_result = null;
					return final_result;
				}
				else{}
			}
	
			result = new double[row][col];//说明输入放入矩阵合法，为row行，col列的矩阵，新建一个数组保存下来
			result_temp_ptr = 0;
			for(int i = 0;i < row;i++)
				for(int j = 0;j < col;j++)
				{
					result[i][j] = result_temp[result_temp_ptr++];
				}
			final_result = new Matrix(result,name);
			return final_result;
		}  
		
		boolean Match(String s)
		{
			MatrixStack<String> S = new MatrixStack<>();
			String temp = "";
			int ptr = 0;
			while (ptr != s.length())
			{
				if (s.charAt(ptr) == '(')
				{
					temp += s.charAt(ptr);
					S.Push(temp);
					ptr++;
					temp = "";
				}
				else if (s.charAt(ptr) == ')')
				{
					if (S.EmptyStack())
					{
						return false;
					}
					else
					{
						S.Pop();
						ptr++;
					}
				}
				else
				{
					ptr++;
				}
			}
			if (S.EmptyStack())  
				return true; 
			else
				return false;
		}
		
		//下面这个函数能识别表达式的一些非法错误，本来想去掉，但是由于后面的算符优先分析法存在不足，所以还是加上了
		//如果后续找到了更好的方法解决这个问题则可以去掉这个很愚蠢的分析
		boolean IsExpressionLegal(String expression)
		{
			boolean flag = true,tag = true;
			String J12temp = expression,temp = "";

			for(int i = 0;i < J12temp.length();i++)//对表达式里的字符逐个分析
			{
				if(( J12temp.charAt(i) >= 65 && J12temp.charAt(i) <= 90 ) || 
				   ( J12temp.charAt(i) >= 97 && J12temp.charAt(i) <= 122) ||
				   ( J12temp.charAt(i) >= 48 && J12temp.charAt(i) <= 57 ) ||
					 J12temp.charAt(i) == '_' || J12temp.charAt(i) == '+' ||
					 J12temp.charAt(i) == '-' || J12temp.charAt(i) == '*' ||
					 J12temp.charAt(i) == '/' || J12temp.charAt(i) == '(' ||
					 J12temp.charAt(i) == ')') {} //合法出现的符号
				else
				{
					if(J12temp.charAt(i) == ' ')//遇到空格，直接过滤
					{
						temp = J12temp.substring(0, i) + J12temp.substring(i+1, J12temp.length());
						J12temp = temp;
						i--;
					}
					else
					{
//						JOptionPane.showMessageDialog(null, "表达式中出现非法字符！", "Error",JOptionPane.WARNING_MESSAGE);
						flag = false;
						break;
					}
				}
			}
			if(flag)
			{
				for(int i = 0;i < J12temp.length();i++)
				{
					if(i == 0)//对表达式第一个字符检验
					{
						if( J12temp.charAt(i) == ')' ||
							J12temp.charAt(i) == '+' ||
							J12temp.charAt(i) == '-' ||
							J12temp.charAt(i) == '*' ||
							J12temp.charAt(i) == '/' )
						{
//							JOptionPane.showMessageDialog(null, "表达式存在逻辑错误！", "Error",JOptionPane.WARNING_MESSAGE);
							tag = false;
							break;
						}
					}
					else if(i == J12temp.length()-1)//对表达式最后一个字符检验
					{
						if( J12temp.charAt(i) == '(' ||
							J12temp.charAt(i) == '+' ||
							J12temp.charAt(i) == '-' ||
							J12temp.charAt(i) == '*' ||
							J12temp.charAt(i) == '/' )
						{
//							JOptionPane.showMessageDialog(null, "表达式存在逻辑错误！", "Error",JOptionPane.WARNING_MESSAGE);
							tag = false;
							break;
						}
					}
					else//对表达式中间的所有字符进行语法分析
					{
						if( J12temp.charAt(i) == '+' ||
							J12temp.charAt(i) == '-' ||
							J12temp.charAt(i) == '*' ||
							J12temp.charAt(i) == '/' )//读到运算符
						{
							if( J12temp.charAt(i-1)== '+' ||
								J12temp.charAt(i-1)== '-' ||
								J12temp.charAt(i-1)== '*' ||
								J12temp.charAt(i-1)== '/' ||
								J12temp.charAt(i-1)== '(' || //前面不允许出现的字符
							
								J12temp.charAt(i+1)== '+' ||
								J12temp.charAt(i+1)== '-' ||
								J12temp.charAt(i+1)== '*' ||
								J12temp.charAt(i+1)== '/' ||
								J12temp.charAt(i+1)== ')' )  //后面不允许出现的字符
							{
//								JOptionPane.showMessageDialog(null, "表达式存在逻辑错误！", "Error",JOptionPane.WARNING_MESSAGE);
								tag = false;
								break;
							}
						}
						else if(J12temp.charAt(i)== '(')
						{
							if( J12temp.charAt(i-1) == '(' ||
								J12temp.charAt(i-1) == '+' ||
								J12temp.charAt(i-1) == '-' ||
								J12temp.charAt(i-1) == '*' ||
								J12temp.charAt(i-1) == '/' ) {}
							else
							{
//								JOptionPane.showMessageDialog(null, "表达式存在逻辑错误！", "Error",JOptionPane.WARNING_MESSAGE);
								tag = false;
								break;
							}
						}
						else if(J12temp.charAt(i)== ')')
						{
							if( J12temp.charAt(i-1)== '(' ||
								J12temp.charAt(i-1)== '+' ||
								J12temp.charAt(i-1)== '-' ||
								J12temp.charAt(i-1)== '*' ||
								J12temp.charAt(i-1)== '/' )
							{
//								JOptionPane.showMessageDialog(null, "表达式存在逻辑错误！", "Error",JOptionPane.WARNING_MESSAGE);
								tag = false;
								break;	
							}
						}
						else	//其他的为代表矩阵的标识符
						{
							if( J12temp.charAt(i-1)== ')' ||
								J12temp.charAt(i+1)== '(' )
							{
//								JOptionPane.showMessageDialog(null, "表达式存在逻辑错误！", "Error",JOptionPane.WARNING_MESSAGE);
								tag = false;
								break;	
							}
						}
					}
				}
				if( !Match(J12temp) )//判断表达式括号是否匹配
				{
					tag = false;
				}
				return tag;
			}
			return false;
		}
		
		//返回一个能够被IsExpressionCorrect函数识别的String
		public String TransToStandardEx(String s) throws ExpressionInputException {
			String result = s;
			for(int i = 0;i < result.length();i++)//实际上就是词法分析，判断表达式中是否有无法识别的字符，有的话抛出异常
			{
				if(( result.charAt(i) >= 65 && result.charAt(i) <= 90 ) || 
				   ( result.charAt(i) >= 97 && result.charAt(i) <= 122) ||
				   ( result.charAt(i) >= 48 && result.charAt(i) <= 57 ) ||
				     result.charAt(i) == '+' ||result.charAt(i) == '-'  || 
				     result.charAt(i) == '*' ||result.charAt(i) == '/'  || 
				     result.charAt(i) == '(' ||result.charAt(i) == ')') {} //合法出现的符号
				else
				{
					ExpressionInputException e = new ExpressionInputException("表达式出现无法识别的符号！");
					throw e;
				}
			}
		
			result = result.replaceAll(" ", "");//过滤空格
			String REGEX = "[A-Za-z][A-Za-z\\d]*";//矩阵名字
			Pattern p = Pattern.compile(REGEX);
		    // get a matcher object
		    Matcher m = p.matcher(result);
		    result = m.replaceAll("i");//所有的矩阵名字都用i代替
			return result;
		}	
		
		
		//识别一个表达式是否合法，接收一个String类型的参数作为输入表达式
		/*
		 * 参见编译原理的自底向上的算符优先分析相关知识
		 * 注意，算符优先文法存在错误的句子也能得到正确的规约的情况
		 * 所以，必须再额外加上一些简单的判断才能避免一些错误的识别
		 * 目前本人没有找到很好的能准确识别表达式的算法，正则表达式貌似也有点复杂
		 * 我看很多人都是在中缀表达式那一部分做识别的，但是由于本人直接调用了之前写的那部分代码，
		 * 故不想多做修改，想在这里能对表达式进行准确识别，希望有思路的人能告诉我怎么做
		 * 
		 */
		public boolean IsExpressionCorrect(String expression){
                                     //  +  -  *  /  (  )  i  #			
			int[][] OPRelationTable = {	{2, 2, 1, 1, 1, 2, 1, 2},    // +   
										{2, 2, 1, 1, 1, 2, 1, 2},    // -
										{2, 2, 2, 2, 1, 2, 1, 2},    // *
										{2, 2, 2, 2, 1, 2, 1, 2},    // /
										{1, 1, 1, 1, 1, 0, 1,-1},    // (
										{2, 2, 2, 2,-1, 2,-1, 2},    // )
										{2, 2, 2, 2,-1, 2,-1, 2},    // i
										{1, 1, 1, 1, 1,-1, 1, 0}};   // #
			/*
			 *0:=  1:<  2:>  -1:error 
			 */
			
			String trans = "+-*/()i#";
			
			//-----------------------------额外的识别------------------------------------
			
			String ex_temp = expression;
			try{
				ex_temp = TransToStandardEx(expression);
			}catch(ExpressionInputException e){
				System.out.println(e.toString());//输出错误信息
				return false;
			}
			
			for(int i = 0;i < ex_temp.length();i++)//实际上就是词法分析，判断表达式中是否有无法识别的字符
			{
				if( ex_temp.charAt(i) == '+'||
					ex_temp.charAt(i) == '-'||
					ex_temp.charAt(i) == '*'||
					ex_temp.charAt(i) == '/'||
					ex_temp.charAt(i) == '('||
					ex_temp.charAt(i) == ')'||
					ex_temp.charAt(i) == 'i'||
					ex_temp.charAt(i) == '#') {} //合法出现的符号
				else
				{
					return false;
				}
			}
			
			String pattern = ".*\\(\\).*";//表达式中不能存在直接相连的(),下面的算法无法识别这个，得在这里单独判断
			if(Pattern.matches(pattern, ex_temp))
			{
				return false;
			}
			
			String pattern2 = ".*[+\\-*/][+\\-*/].*";//表达式中不能存在直接相连的任意两个运算符
			if(Pattern.matches(pattern2, ex_temp))
			{
				return false;
			}
			
			if ( ! IsExpressionLegal(ex_temp) )
			{
				return false;
			}
			
			//------------------------------------------------------------------------------------
			
			String s = ex_temp + "#";//待分析串必须以#结束
			boolean end = false;
			MatrixStack<String> stack = new MatrixStack<>();
			stack.Push("#");//分析栈初始化为一个#
			int left_index,right_index;//分别用来表示栈顶元素和待分析的符号串串头的元素
			String Left_temp = "",Right_temp = "";
			char temp;
			int flag;
			while(!end)
			{
				Left_temp = stack.GetTop();
				temp = s.charAt(0);
				Right_temp = "" + temp;//char -> String
				left_index = trans.indexOf(Left_temp);
				right_index = trans.indexOf(Right_temp);
				flag = OPRelationTable[left_index][right_index];
				if(flag == 0 || flag == 1)//移进
				{
					stack.Push(Right_temp);//符号串串头移进分析栈
					s = s.substring(1, s.length());//待分析符号串去掉串头
				}
				else if(flag == 2)//归约
				{
					Right_temp = stack.Pop();
					Left_temp = stack.Pop();
					left_index = trans.indexOf(Left_temp);
					right_index = trans.indexOf(Right_temp);
					flag = OPRelationTable[left_index][right_index];
					while(flag == 0)
					{
						Right_temp = Left_temp;
						Left_temp = stack.Pop();
						left_index = trans.indexOf(Left_temp);
						right_index = trans.indexOf(Right_temp);
						flag = OPRelationTable[left_index][right_index];
					}
					stack.Push(Left_temp);
				}
				else if(flag == -1)//出错
				{
					System.out.println("表达式非法 ");
					end = true;
					return false;
				}
				
				if(s.length() == 0)
				{
					System.out.println("表达式合法 ");
					return true;
				}
			}
			System.out.println("表达式非法 ");
			return false;
		}
		
		
		//------------------------------By XJX----------------------------------

		/*
		 * @author XJX
		 * 中缀表达式求值
		 */
		
		public boolean Precede(char a,char b)//a为符号栈栈顶元素,b为待插入的元素
		{
			boolean i = true;//i=true入栈,i=false弹出操作符以及操作数进行计算
			if((a=='+'||a=='-')&&(b=='*'||b=='/')) i=true;
			if((a=='+'||a=='-')&&(b=='+'||b=='-')) i=false;
			if((a=='*'||a=='/')&&(b=='*'||b=='/')) i=false;
			if((a=='*'||a=='/')&&(b=='+'||b=='-')) i=false;
			if(a=='(') i=true;
			return i;
		}
		
		public Matrix EvaluateExpression(String expression) throws MatrixArithException  //核心函数
		{
			String s = expression;
			char c,d,e = '\0';
			Matrix a,b;
			int ptr2 = 0,ptrtemp = 0;
			boolean tag;
			Matrix j = null;
			Matrix result;
			String temp = s,char_temp = "";
			String Numbuff = "";//用以记录矩阵名
			MatrixStack<String> S1 = new MatrixStack<>();//S1为操作符栈,S2为操作数栈
			MatrixStack<Matrix> S2 = new MatrixStack<>();
			
			if(ptr == 0)//如果Buff数组为空，则无法进行计算
			{
//				JOptionPane.showMessageDialog(null, "表达式中的矩阵全部未定义！", "Error",JOptionPane.WARNING_MESSAGE);
				result = new Matrix(null,"temp");
//				MatrixArithException err = new MatrixArithException("表达式中存在未定义的矩阵！");
				MatrixArithException err = new MatrixArithException("矩阵 \"" + s.substring(0, s.length()-1) + "\" 未定义！");
				throw err;
			}
					
			c = temp.charAt(ptr2++);
			while(c != '=')
			{
				if(c >= 65 && c <= 90 || c >= 97 && c <= 122 || c >= 48 && c <= 57 || c == '_')//大小写字母，数字，下划线
				{
					ptrtemp = ptr2;
					Numbuff += c;
					//判断字符后面一个字符是否为矩阵名允许的字符
					while( ( temp.charAt(ptrtemp) >= 65 && temp.charAt(ptrtemp ) <= 90 ) ||
						   ( temp.charAt(ptrtemp) >= 97 && temp.charAt(ptrtemp ) <= 122 )||
						   ( temp.charAt(ptrtemp) >= 48 && temp.charAt(ptrtemp ) <= 57 ) ||
						     temp.charAt(ptrtemp) == '_' )
					{
						Numbuff += temp.charAt(ptrtemp);
						ptrtemp++;
					}
					for(int i = 0;i < ptr;i++)
					{
						if(Buff[i].name.equals(Numbuff))
						{
							S2.Push(Buff[i]);//把矩阵压入操作数栈
							break;
						}
						else
						{
							if( i == ptr-1)
							{
//								JOptionPane.showMessageDialog(null, "表达式中的矩阵 " + Numbuff + " 未定义！", "Error",JOptionPane.WARNING_MESSAGE);
								result = new Matrix(null,"temp");
//								MatrixArithException err = new MatrixArithException("表达式中存在未定义的矩阵！");
								MatrixArithException err = 
										new MatrixArithException("矩阵 \"" + Numbuff + "\" 未定义！");
								throw err;
							}
						}
					}
					//注意每次判断处理完数字,以下这些变量要重置为初始值
					Numbuff = "";
					ptr2 = ptrtemp;//处理完一串数字字符，记得把ptr移到数字字符串的下一个位置
				}
				if(c=='(') //输入为左括号
				{
					char_temp += c;//把char变成String,以下以此类推
					S1.Push(char_temp);
					char_temp = "";//每次用完记得重置为零
				}
				if(c==')')//输入为右括号
				{
					if(!S1.EmptyStack()) 
					{
						char_temp = S1.GetTop();
						e = char_temp.charAt(0);//把String变成char,以下以此类推
						char_temp = "";
					}
					while(e != '(')
					{
						a = S2.Pop();
						b = S2.Pop();
						char_temp = S1.Pop();
						d = char_temp.charAt(0);
						char_temp = "";
						
						try{
							if(d=='+') {j = MatrixArith.add(b, a);}
							if(d=='-') {j = MatrixArith.sub(b, a);}
							if(d=='*') {j = MatrixArith.mul(b, a);}
							if(d=='/') {j = MatrixArith.div(b, a);}
						}catch(MatrixArithException err){
							System.out.println(err.toString());
							result = new Matrix(null,"temp");
							MatrixArithException err2 = new MatrixArithException(err.toString());
							throw err2;
						}
						S2.Push(j);
						if(!S1.EmptyStack())
						{
							char_temp = S1.GetTop();
							e = char_temp.charAt(0);//把String变成char,以下以此类推
							char_temp = "";
						}
						if(e=='(') S1.Pop();
					}
				}
				if(c=='+'||c=='-'||c=='*'||c=='/')
				{
					if(S1.EmptyStack())  
					{
						char_temp += c;
						S1.Push(char_temp);
						char_temp = "";
					}
					else
					{
						char_temp = S1.GetTop();
						e = char_temp.charAt(0);
						char_temp = "";
						tag=Precede(e,c);
						if(!tag)
						{
							a = S2.Pop();
							b = S2.Pop();
							char_temp = S1.Pop();
							d = char_temp.charAt(0);
							char_temp = "";
							
							try{
								if(d=='+') {j = MatrixArith.add(b, a);}
								if(d=='-') {j = MatrixArith.sub(b, a);}
								if(d=='*') {j = MatrixArith.mul(b, a);}
								if(d=='/') {j = MatrixArith.div(b, a);}
							}catch(MatrixArithException err){
								System.out.println(err.toString());
								result = new Matrix(null,"temp");
								MatrixArithException err2 = new MatrixArithException(err.toString());
								throw err2;
							}
							
							char_temp += c;
							S1.Push(char_temp);
							char_temp = "";
							S2.Push(j);
						}
						else 
						{
							char_temp += c;
							S1.Push(char_temp);
							char_temp = "";
						}
					}
				}
				c = temp.charAt(ptr2++);
			}
			if(!S1.EmptyStack())
			{
				while(!S1.EmptyStack())
				{
					a = S2.Pop();
					b = S2.Pop();
					char_temp = S1.Pop();
					d = char_temp.charAt(0);
					char_temp = "";
								
					try{
						if(d=='+') {j = MatrixArith.add(b, a);}
						if(d=='-') {j = MatrixArith.sub(b, a);}
						if(d=='*') {j = MatrixArith.mul(b, a);}
						if(d=='/') {j = MatrixArith.div(b, a);}
					}catch(MatrixArithException err){
						System.out.println(err.toString());
						result = new Matrix(null,"temp");
						MatrixArithException err22 = new MatrixArithException(err.toString());
						throw err22;
					}
					
					S2.Push(j);
				}
			}
			//最后输出结果
			result = S2.Pop();
			return result;
		}
	
		
		//-----------------------------------------------------------------------
		
		//识别所有命令
		@Override  
		public void keyTyped(KeyEvent e) {  
			if (isConsume) {  
				e.consume();
				current_pos = this.getText().length();
				this.setCaretPosition(current_pos);
				System.out.println("Consumed!!!");
				return;  
			}  

			// 以'enter'键结束命令输入  
			if (currentKeyCode == KeyEvent.VK_ENTER) 
			{
				String input = this.getText().substring(textBuffer.length(),  
							this.getText().length() - 1);//输入的回车属于确认命令，不属于输入命令的内容，但是也会读到输入框中去
				System.out.println("输入了命令：" + input);
				textBuffer.append(input);  
				textBuffer.append("\n");//注意输入的回车也要加入到 textBuffer
				if (input.equals("exit")) 
				{
					this.append("Bye.");  
						System.exit(0);  
				}
				else if(input.equals("cls") || input.equals("CLS"))
				{
					this.setText("");
					textBuffer.delete(0, textBuffer.length());//清空textBuffer
					this.append(">>");
				}
				else if(input.equals("del all") || input.equals("delete all") 
						||input.equals("DEL ALL") || input.equals("DELETE ALL"))
				{
					J212.setText("");
					ptr = 0;//把Buff数组清空
				}
				else if(input.equals("about") || input.equals("ABOUT"))
				{
					this.append(input);  
					this.append("\n\n");  
					this.append(">>");
					new About();
				}
				else if(input.equals("help") || input.equals("HELP"))
				{
					this.append(input);  
					this.append("\n\n");  
					this.append(">>");
					new Help();
				}
				else if(input.equals("QQ") || input.equals("qq"))
				{
					this.append("加我QQ：3052247572，聊骚或聊技术都行");  
					this.append("\n\n");  
					this.append(">>");
				}
				else if(Pattern.matches(pattern, input))
				{
					System.out.println("和模式串匹配!");
//					this.append(input);  
//					this.append("\n");  
//					this.append(">>");
            
					if( IsMatrixRight(input) == null)
					{
						System.out.println("输入串有问题");
						this.append("定义矩阵出现各行列数不相等！");  
						this.append("\n\n");  
						this.append(">>");
					}
					else
					{
						System.out.println("输入串无问题");
						Matrix res = IsMatrixRight(input);
						int row = res.array.length;  
						int col = res.array[0].length;
						System.out.println(res.name + ":");
						for(int i =0;i<row;i++)
						{
							for(int j = 0;j < col;j++)
								System.out.print(res.array[i][j] + "   ");
							System.out.println();
						}
    	        
						boolean tag = true;
						String name = res.name;
						String J212temp = "";
						NumberFormat nFormat=NumberFormat.getNumberInstance(); 
						nFormat.setMaximumFractionDigits(2);
						nFormat.setMinimumFractionDigits(2);
    			
						for(int i = 0;i < ptr; i++)
						{
							if(Buff[i].name.equals(name))//如果此处随机生成的矩阵在Buff数组中已存在，那么更新之
							{
								Buff[i] = res;
								tag = false;
								break;
							}
						}
						if(tag)//证明Buff中不存在这个矩阵
						{
							Buff[ptr++] = res;
							System.out.println(ptr);
						}
						if( ptr == 0) Buff[ptr++] = res;
						J212.setText("");
						for(int i = 0;i < ptr;i++)//在J212区域打印Buff数组
						{
							J212temp += Buff[i].name + " = ";
							for(int j = 0;j < Buff[i].array.length;j++)
							{
								for(int k = 0;k < Buff[i].array[0].length;k++)
								{
									J212temp += nFormat.format(Buff[i].array[j][k]) + " ";
								}
								J212temp += "\n";
								if(j != Buff[i].array.length-1)//不是最后一行
								{
									for(int m = 0;m < Buff[i].name.length()+3;m++)
									{
										J212temp += " ";
									}
								}
							}
							J212temp += "\n";
						}
						J212.setText(J212temp);
						
						String command_temp = "";
						command_temp += res.name + " = ";
						for(int j = 0;j < res.array.length;j++)
						{
							for(int k = 0;k < res.array[0].length;k++)
							{
								command_temp += nFormat.format(res.array[j][k]) + " ";
							}
							command_temp += "\n";
							if(j != res.array.length-1)//不是最后一行
							{
								for(int m = 0;m < res.name.length()+3;m++)
								{
									command_temp += " ";
								}
							}
						}
						this.append(command_temp);  
						this.append("\n\n");  
						this.append(">>");
					}
				}
				else if(Pattern.matches(pattern_inv, input))//求逆矩阵
				{
					String expression = input;
					NumberFormat nFormat=NumberFormat.getNumberInstance(); 
					nFormat.setMaximumFractionDigits(2);
					nFormat.setMinimumFractionDigits(2);
					String err_temp = "";
					Matrix result = null;
					try{
						int start_index = expression.indexOf("(");
						int end_index = expression.indexOf(")");
						String matrix_name = expression.substring(start_index+1, end_index);
						matrix_name = matrix_name.replaceAll(" ", "");//过滤空格，获取矩阵名字
						
						
						for(int i = 0;i < ptr;i++)
						{
							if(Buff[i].name.equals(matrix_name))
							{
								result = MatrixArith.inverse_matrix(Buff[i]);
								break;
							}
							else
							{
								if( i == ptr-1)
								{
//									JOptionPane.showMessageDialog(null, "表达式中的矩阵 " + Numbuff + " 未定义！", "Error",JOptionPane.WARNING_MESSAGE);
									result = new Matrix(null,"temp");
//									MatrixArithException err = new MatrixArithException("表达式中存在未定义的矩阵！");
									MatrixArithException err = 
											new MatrixArithException("矩阵 \"" + matrix_name + "\" 未定义！");
									throw err;
								}
							}
						}
						
						String res_temp = "";
						for(int i = 0;i < result.array.length;i++)
						{
							for(int j = 0;j < result.array[0].length;j++)
							{
								res_temp += nFormat.format(result.array[i][j]) + " ";
							}
							res_temp += "\n";
						}
						this.append(res_temp);  
						this.append("\n\n");  
						this.append(">>");
						String his_temp = J222.getText();
						his_temp += expression.replaceAll(" ", "") + "= \n" + res_temp+ "\n";
						J222.setText(his_temp);
						
					}catch(MatrixArithException err){
						System.out.println(err.toString());
						//过滤掉err中无关的报错信息
						err_temp = err.toString();
						int start = err_temp.lastIndexOf(":");
						err_temp = err_temp.substring(start+2, err_temp.length());
						
						this.append(err_temp);  
						this.append("\n\n");  
						this.append(">>");
						String his_temp = J222.getText();
						his_temp += expression + " = \n" + "ERROR!" + "\n\n";
						J222.setText(his_temp);
					}
				}
				else if(IsExpressionCorrect(input))//识别表达式成功，可以开始计算
				{
					String expression = input;
					NumberFormat nFormat=NumberFormat.getNumberInstance(); 
					nFormat.setMaximumFractionDigits(2);
					nFormat.setMinimumFractionDigits(2);
					String err_temp = "";
					Matrix result = null;
					
					try{
						result = EvaluateExpression(expression + "=");//调用中缀表达式计算函数EvaluateExpression
						
						String res_temp = "";
						for(int i = 0;i < result.array.length;i++)
						{
							for(int j = 0;j < result.array[0].length;j++)
							{
								res_temp += nFormat.format(result.array[i][j]) + " ";
							}
							res_temp += "\n";
						}
						this.append(res_temp);  
						this.append("\n\n");  
						this.append(">>");
						String his_temp = J222.getText();
						his_temp += expression + "= \n" + res_temp+ "\n";
						J222.setText(his_temp);
						
					}catch(MatrixArithException err){
						System.out.println(err.toString());
						//过滤掉err中无关的报错信息
						err_temp = err.toString();
						int start = err_temp.lastIndexOf(":");
						err_temp = err_temp.substring(start+2, err_temp.length());
						
						this.append(err_temp);  
						this.append("\n\n");  
						this.append(">>");
						String his_temp = J222.getText();
						his_temp += expression + " = \n" + "ERROR!" + "\n\n";
						J222.setText(his_temp);
					}
				}
				else {  
					this.append("Unrecognized command \"" + input + "\" ");  
					this.append("\n\n");  
					this.append(">>");  
				}  
			}  
		}  

		@Override  
		public void keyPressed(KeyEvent e) {  
			currentKeyCode = e.getKeyCode();  
			isConsume = checkConsume(e) ? true : false;  
		}  

		@Override  
		public void keyReleased(KeyEvent e) {  
			if (isConsume) {  
				e.consume();  
				return;  
			}  
		}  

		/** 
		 * 检查是否要使用输入事件 
		 *  
		 * @param e 
		 * @return 
		 */  
		
		private boolean checkConsume(KeyEvent e) {  
			if (!isAllowedInputArea) {  
				e.consume();  
				return true;  
			}  

			if ((currentKeyCode == KeyEvent.VK_BACK_SPACE  
					|| currentKeyCode == KeyEvent.VK_ENTER  
					|| currentKeyCode == KeyEvent.VK_UP || currentKeyCode == KeyEvent.VK_LEFT)  
					&& currentDot == textBuffer.length()) {  
				e.consume();  
				return true;  
			}  
			return false;  
		}  

		@Override  
		public void append(String message) {  
			super.append(message);  
			textBuffer.append(message);  
		}  

		@Override  
		public void caretUpdate(CaretEvent e) {  
			currentDot = e.getDot();
			System.out.println("current pos : " + currentDot);
			//    String temp = this.getText();
			//    textBuffer = new StringBuffer(temp);
			isAllowedInputArea = currentDot < textBuffer.length() ? false : true;
			System.out.println("isAllowedInputArea : " + isAllowedInputArea);
			System.out.println("isConsume : " + isConsume);
		}
		
	}
	
	public static void main(String args[]){
		new MainWindow();
	}
}

