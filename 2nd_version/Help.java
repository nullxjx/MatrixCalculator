package xjx;

import java.awt.*;
import javax.swing.*;

public class Help extends JDialog {
    private static final long serialVersionUID = 4693799019369193520L;
    private JPanel contentPane;
    private Font f = new Font("微软雅黑",Font.PLAIN,15);
    private JScrollPane scroll;
	
    public Help() {
        setTitle("帮助");//设置窗体标题
        Image img=Toolkit.getDefaultToolkit().getImage("title.png");//窗口图标
        setIconImage(img);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModal(true);//设置为模态窗口
        setSize(410,380);
        setResizable(false);
        setLocationRelativeTo(null);
        contentPane = new JPanel();// 创建内容面板
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        ShadePanel shadePanel = new ShadePanel();// 创建渐变背景面板
        contentPane.add(shadePanel, BorderLayout.CENTER);// 添加面板到窗体内容面板
        shadePanel.setLayout(null);
        
        JTextArea J1 = new JTextArea("本程序是一个用Java语言实现的矩阵计算器程序\n\n"
        		+ "本程序能够自定义矩阵，并且能对矩阵进行基本的四则运算以及复杂的混合运算，"
        		+ "可以为你省去无聊、繁琐的计算过程，非常适合正在学习线性代数的同学"
        		+ "（想老子大一那年就非常讨厌计算矩阵，这也是我想写这个程序的原因）。"
        		+ "本程序参考matlab实现，一些交互逻辑是和matlab是一样的，比如命令行模式，"
        		+ "个人认为在这里这种输入模式的效率是最高同时也是最简单的，定义矩阵的方式完全参考matlab。"
        		+ "本程序同样能够识别一些其他基本命令，能够对输入错误进行一些提示。\n\n"
        		+ "整个界面分成了三部份，左边的输入和输出区，也就是所谓的command区，"
        		+ "右上角记录你当前定义的矩阵，右下角记录你运算的历史记录，你在左边输入定义矩阵的命令，"
        		+ "若这个矩阵没有被定义过，则会在右上角加入这个矩阵，若之前被定义过，则会更新它的值。"
        		+ "你在左边输入表达式，运算结果会紧接着在左边显示，同时会添加到右下角的历史记录区。"
        		+ "菜单栏设置里的重置是在左边区域出问题时能帮你回到最开始的状态用的。自动换行是设置"
        		+ "左边的command区域用的，当命令过长时，勾选这个看起来比较美观。\n\n"
        		+ "有几个注意点:\n"
        		+ "-- 目前，最多只支持定义10个矩阵，再多了就会出问题，当然你也可以修改程序来达到更多的目的"
        		+ "不过我认为10个就够用了。\n"
        		+ "-- 目前把矩阵内的数据统一当作double型数据处理，并且运算结果统一保留两位，为的是打印出来比较整齐。\n"
        		+ "--目前支持的命令有：\n"
        		+ "cls(清屏) \n"
        		+ "help(帮助) \n"
        		+ "about(关于) \n"
        		+ "del all(删除所有已经定义过的矩阵) \n"
        		+ "inv(name)(求某个矩阵的逆矩阵)\n"
        		+ "定义矩阵  \n"
        		+ "计算表达式   \n"
        		+ "(注意前面的命令都不区分大小写)\n"
        		+ "-- 这里给一下输入矩阵的格式\n"
        		+ "A = [2 2;2 2]\n"
        		+ "A = [2,2;2,2]\n"
        		+ "A = [2 2;2,2]\n"
        		+ "和matlab输入一样的格式，任意位置都允许任意多个空格\n"
        		+ "--矩阵的名字必须以字母开头，任意多个数字或者字母结束\n\n\n"
        		+ "                           Copyright @XJX2018.\n   	   All rights reserved.");
        J1.setFocusable(false);
    	J1.setFont(f);
    	J1.setEditable(false);
    	J1.setOpaque(false);//背景透明
    	J1.setLineWrap(true);
    	
    	scroll = new JScrollPane(J1,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	scroll.setBorder(BorderFactory.createTitledBorder("Written By XJX"));
    	scroll.setOpaque(false);
    	scroll.getViewport().setOpaque(false);//JScrollPane设置成透明需加上这一行
    	shadePanel.add(scroll);
    	scroll.setBounds(10, 10, 385, 330);
    	
    	setVisible(true);
    }
    
    public static void main(String[] args) {
		new Help();
	}
}
