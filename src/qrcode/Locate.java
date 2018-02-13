package qrcode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;



public class Locate {
	 Set<Point> pointset;
     Rect rect;
     double px;
     double py;
     List<Point> pointlist;
	Point point1 = null;//point1和point2标记为斜边的两个顶点
	Point point2 = null;
	Point point3 = null;//直角顶点
	 List<Point> NPointList=new ArrayList<Point>();//得到的四个顶点坐标数组
	
	
	
	public Set<Point> getPointset() {
		return pointset;
	}

	public void setPointset(Set<Point> pointset) {
		this.pointset = pointset;
	}
	
	/*
	 * 计算出第四个点的坐标
	 */
	public void computeRect() {
		pointlist=new ArrayList<Point>(pointset);
		
		//List lengthlist=new ArrayList<>();
		double maxlength=0;

		for(int i=0;i<pointlist.size();i++) {//计算边长
         if(i>0) {
        	 double length=Math.sqrt((pointlist.get(i).x-pointlist.get(i-1).x)*(pointlist.get(i).x-pointlist.get(i-1).x)+(pointlist.get(i).y-pointlist.get(i-1).y)*(pointlist.get(i).y-pointlist.get(i-1).y));
        	 if(length>maxlength) {
            	 maxlength=length;
            	 point1=pointlist.get(i);
            	 point2=pointlist.get(i-1);
             }
         }
         if(i==2) {//第三个边和第一个边的距离
        	 double length=Math.sqrt((pointlist.get(i).x-pointlist.get(0).x)*(pointlist.get(i).x-pointlist.get(0).x)+(pointlist.get(i).y-pointlist.get(0).y)*(pointlist.get(i).y-pointlist.get(0).y));
        	 if(length>maxlength) {
            	 maxlength=length;
            	 point1=pointlist.get(i);
            	 point2=pointlist.get(0);
             }
         }
         
        }
		/*取出直角顶点*/
		for(Point list:pointset) {
			if(list.x!=point1.x||list.y!=point1.y) 
				if(list.x!=point2.x||list.y!=point2.y)
					point3=list;       
	    }
		/*计算出对角线中点*/
        double x=(point2.x+point1.x)/2;
        double y=(point2.y+point1.y)/2;
        
        px= 2*x-point3.x;
        py= 2*y-point3.y;
        System.out.println("横坐标:"+px+"纵坐标"+py);
        getNPointList();
        transform();
  }
	/*
	 * 用来返回具有左上，右上，左下，右下顺序的四个顶点的数组
	 */
	public void getNPointList(){
		Point rightTop=null;
		Point leftDown=null;
		if(point1.x>point2.x) {//判断右上与左下的坐标
			rightTop=point1;
			leftDown=point2;
		}else {
			rightTop=point2;
			leftDown=point1;
		}
		NPointList.add(point3);
		NPointList.add(rightTop);
		NPointList.add(leftDown);
		Point rightDown=new Point(px,py);
		NPointList.add(rightDown);

	}
	
	/*
	 * 此函数为做仿射变换所用，传入的点的坐标转化为整型
	 */
	public void transform() {
		double newWidth = 0;
		double newHeight = 0;
		double rightTopX=NPointList.get(1).x;
		double rightTopY=NPointList.get(1).y;
		double leftTopX=NPointList.get(0).x;
		double leftTopY=NPointList.get(0).x;
		
		newWidth = Math.sqrt((leftTopX - rightTopX) * (leftTopX - rightTopX) + (leftTopY - rightTopY) * (leftTopY - rightTopY));
		//newHeight = Math.sqrt((leftTopX - leftDownX) * (leftTopX - leftDownX) + (leftTopY - leftDownY) * (leftTopY - leftDownY));	
		newHeight=newWidth*3.1;	//长宽比例来算出长度
		List<Point> distpoint=new ArrayList<Point>();
		distpoint.add(new Point(0,0));
		distpoint.add(new Point(newWidth,0));	
		distpoint.add(new Point(0,newHeight));
		distpoint.add(new Point(newWidth,newHeight));
		
		
		//Homography变换（3*3矩阵）
		MatOfPoint2f mp1 = new MatOfPoint2f(); 
		mp1.fromList(NPointList);
		MatOfPoint2f mp2 = new MatOfPoint2f(); 
		mp2.fromList(distpoint);
		MatOfPoint2f m1=mp1;
		MatOfPoint2f m2=mp2;
		
	
	    Mat h=Calib3d.findHomography(mp1, mp2,0, 3);
	    
	   
	   
	    Core.perspectiveTransform(m1, m2, h);
	    Mat outImg = new Mat((int)newHeight,(int)newWidth, Main.SrcIMG.type());
		Imgproc.warpPerspective(Main.SrcIMG, outImg,h, outImg.size());
		ImageViewer imws=new ImageViewer(outImg ,"矫正位置图片");
	    imws.imshow();
	
	}
}
