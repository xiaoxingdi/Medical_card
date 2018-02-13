package qrcode;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class findBycontours {
	Mat src;
	Mat grayImage;
	Mat edges;
	Mat hierarchy;
	public findBycontours(Mat src) {
		this.src=src;
	}
    void predo() {
    	Imgproc.cvtColor(this.src, grayImage, Imgproc.COLOR_BGR2GRAY);
    	Imgproc.GaussianBlur(grayImage, grayImage, new Size(5,5), 0);
    	Imgproc.Canny(grayImage, edges, 100, 200);
    	List<MatOfPoint> pointlist=new ArrayList<MatOfPoint>();
    	Imgproc.findContours(edges, pointlist, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
    	System.out.println(hierarchy.dump());
    }
}
