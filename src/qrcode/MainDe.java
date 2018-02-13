package qrcode;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class MainDe {
	static {  
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);  
        //注意程序运行的时候需要在VM option添加该行 指明opencv的dll文件所在路径  
        //-Djava.library.path=$PROJECT_DIR$\opencv\x64  
    }  
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Detector dector=new Detector();
		 Mat srcImg=Imgcodecs.imread("G:/ImgaeTest/qr9.png");
		Mat distimg=dector.testAction(srcImg);
		ImageViewer ima=new ImageViewer(distimg ,"输出图片");
	     ima.imshow();
		 

	}

}
