package xjx;

/*
 * @author XJX
 * https://github.com/JiaxinTse/MatrixCalculator
 */


class Matrix {
	String name;
	double[][] array;
	Matrix(double[][] a,String s){
		name = s;
		if(a == null)
		{
			array = null;
		}
		else
		{
			int row = a.length;  
	        int col = a[0].length;
			array = new double[row][col];
			for(int i = 0;i < row;i++)
				for(int j = 0;j < col;j++)
				{
					array[i][j] = a[i][j];
				}
		}	
	}
}

@SuppressWarnings("serial")
class MatrixArithException extends Exception{
	public MatrixArithException(){}
	public MatrixArithException(String smg){
		super(smg);
	}
}

public class MatrixArith {
	
	public static Matrix add(Matrix matrix_a, Matrix matrix_b) throws MatrixArithException {	//加法
		int row = matrix_a.array.length;  
        int col = matrix_a.array[0].length;
        Matrix result = null;
        double[][] res = new double[row][col];
        
        
        if(row != matrix_b.array.length || col != matrix_b.array[0].length)
        {  
//     	 	JOptionPane.showMessageDialog(null, "矩阵" + matrix_a.name + "和矩阵" + matrix_b.name + "无法相加！", "Error",JOptionPane.WARNING_MESSAGE);
        	res = null;
        	result = new Matrix(res,"temp");//出错时返回一个名字为temp，值为空的Matrix对象，并抛出异常
        	MatrixArithException e = new MatrixArithException("计算过程出现矩阵无法相加的情况！");
        	throw e;
        }  
        else
        {  
        	for(int i=0;i<row;i++)
        	{  
        		for(int j=0;j<col;j++)
        		{  
        			res[i][j] = matrix_a.array[i][j] + matrix_b.array[i][j];  
        		}  
        	}
        	result = new Matrix(res,"temp");//返回名字为temp的正确结果
        } 
        return result;
	}
	
	public static Matrix sub(Matrix matrix_a, Matrix matrix_b) throws MatrixArithException {	//减法  
	        int row = matrix_a.array.length;  
	        int col = matrix_a.array[0].length;  
	        Matrix result = null;
	        double[][] res = new double[row][col];  
	        
	        
	        if(row != matrix_b.array.length || col != matrix_b.array[0].length)
	        {  
//		    	JOptionPane.showMessageDialog(null, "矩阵" + matrix_a.name + "和矩阵" + matrix_b.name + "无法相减！", "Error",JOptionPane.WARNING_MESSAGE);
	        	res = null;
	        	result = new Matrix(res,"temp");
	        	MatrixArithException e = new MatrixArithException("计算过程出现矩阵无法相减的情况！");
	        	throw e;
	        }  
	        else
	        {  
	        	for(int i=0;i<row;i++)
	        	{  
	        		for(int j=0;j<col;j++)
	        		{  
	        			res[i][j] = matrix_a.array[i][j] - matrix_b.array[i][j];  
	        		}  
	        	}
	        	result = new Matrix(res,"temp");
	        }
	        return result;  
	    }
	 
	public static Matrix mul(Matrix matrix_a, Matrix matrix_b) throws MatrixArithException { //乘法 
	        int row = matrix_b.array.length;  
	        int col = matrix_a.array[0].length;
	        Matrix result = null;
	        double[][] res = new double[matrix_a.array.length][matrix_b.array[0].length];  
	        
	        
	        if(col != row)
	        {  
//		    	JOptionPane.showMessageDialog(null, "矩阵" + matrix_a.name + "和矩阵" + matrix_b.name + "无法相乘！", "Error",JOptionPane.WARNING_MESSAGE);
	        	res = null;
	        	result = new Matrix(res,"temp");
	        	MatrixArithException e = new MatrixArithException("计算过程出现矩阵无法相乘的情况！");
	        	throw e;
	        }  
	        else
	        {  
	        	for(int i=0;i<matrix_a.array.length;i++)
	        	{  
	        		for(int j=0;j<matrix_b.array[0].length;j++)
	        			for(int k=0;k<row;k++)
	        			{
	        				res[i][j] += matrix_a.array[i][k] * matrix_b.array[k][j];
	        			}
	        	}
	        	result = new Matrix(res,"temp");
	        }
	        return result;
	 }
	 
	public static Matrix inverse_matrix(Matrix matrix) throws MatrixArithException {	//求matrix的逆矩阵并返回结果
		 int row = matrix.array.length,k;
		 double[][] expand_matrix = new double[row][row * 2];	//拓展矩阵
		 double[][] res = new double[row][row];
		 Matrix result = null;
		 
		 if(row != matrix.array[0].length)
		 {  
//			 JOptionPane.showMessageDialog(null, "被除数矩阵" + matrix.name + "不可逆！" , "Error",JOptionPane.WARNING_MESSAGE);//可逆矩阵必须为方阵
			 res = null;
			 result = new Matrix(res,"temp");
			 MatrixArithException e = new MatrixArithException("计算过程出现矩阵不可逆的情况！");
			 throw e;
		 }
		 else
		 {
			 for(int i=0;i<row;i++)
			 {
				 for(int j=0;j<row;j++)
				 {
					 expand_matrix[i][j]=matrix.array[i][j];
				 }
			 }
			 for(int i=0;i<row;i++)	//构造拓展矩阵	
			 {
				 for(int j=row;j<(row * 2);j++)
				 {
					 if(i==(j-row))
					 {
						 expand_matrix[i][j]=1;
					 }
					 else
					 {
						 expand_matrix[i][j]=0;
					 }
				 }
			 }
			 /********************求逆模块***********************/
			 for(int i=0;i<row;i++)	
			 {
				 if(expand_matrix[i][i]==0)
				 {
					 for(k=i;k<row;k++)
					 {
						 if(expand_matrix[k][i]!=0)
						 {
							 for(int j=0;j<row*2;j++)
							 {
								 double temp;
								 temp=expand_matrix[i][j];
								 expand_matrix[i][j]=expand_matrix[k][j];
								 expand_matrix[k][j]=temp;
							 }
							 break;
						 }
					 }
					 if(k==row)
					 {
//				         JOptionPane.showMessageDialog(null, "被除数矩阵" + matrix + "不可逆！" , "Error",JOptionPane.WARNING_MESSAGE);
						 res = null;
						 result = new Matrix(res,"temp");
						 MatrixArithException e = new MatrixArithException("计算过程出现矩阵不可逆的情况！");
						 throw e;
					 }
				 }
				 for(int j=row*2-1;j>=i;j--)
				 {
					 expand_matrix[i][j]/=expand_matrix[i][i];
				 }
				 for(k=0;k<row;k++)
				 {
					 if(k!=i)
					 {
						 double temp=expand_matrix[k][i];
						 for(int j=0;j<row*2;j++)
						 {
							 expand_matrix[k][j]-=temp*expand_matrix[i][j];
						 }
					 }
				 }
			 }
			 /********************求逆模块***********************/
				 
			 for(int i=0;i<row;i++)	//导出结果
			 {
				 for(int j=row;j<row*2;j++)
				 {
					 res[i][j-row]=expand_matrix[i][j];
				 }
			 }
			 result = new Matrix(res,"temp");
		 }
		 return result;
	}
	 
	public static Matrix div(Matrix matrix_a, Matrix matrix_b) throws MatrixArithException { //除法(matrix_a除以matrix_b)
	     Matrix result = null;
		 Matrix temp ;
		 try{
			 temp = inverse_matrix(matrix_b);
			 result = mul(matrix_a,temp);	//调用乘法
		 }catch(MatrixArithException e){
			 System.out.println(e.toString());
			 MatrixArithException e2 = new MatrixArithException(e.toString());
			 throw e2;
		 } 
	     return result;
	 }
}
