package servlet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Detector represents the image using disjoint sets
 * and find the parts of the image that is the same color as
 * requested.
 */
public class Detector extends JPanel
{
	/**
	 * Calculates the distance between two color to see
	 * if they can be considered the "same" color.
	 *
	 * @param c1 Color 1
	 * @param c2 Color 2
	 * @return  Distance between two colors
	 */
	public static int getDifference(Color c1, Color c2) {
		int r1,g1,b1,r2,g2,b2;
		double unscaled;

		r1 = c1.getRed();
		g1 = c1.getGreen();
		b1 = c1.getBlue();

		r2 = c2.getRed();
		g2 = c2.getGreen();
		b2 = c2.getBlue();

		unscaled = Math.floor(Math.pow((r1-r2),2) + Math.pow((g1-g2),2)
				+ Math.pow((b1-b2),2));
		return (int)((unscaled/195075)*100);    //195075 magic number
	}

	/**
	 * Thresholds the image and creates an image with only two different
	 * color: Black and white.
	 * Black refers to the pixels matched with the color.
	 * White refers to the pixels not required
	 * @param image Image to be processed
	 * @param c     Desired color
	 * @param okDist Acceptable distance where two color can still be considered the same
	 */
	public static void thresh(BufferedImage image, Color c, int okDist) {
		int width = image.getWidth();
		int height = image.getHeight();

		Color currPixelColor;
		int newRgb;

		for(int row = 0; row < height; row++){
			for (int col = 0; col < width; col++){
				currPixelColor = new Color(image.getRGB(col,row));
				newRgb = getDifference(currPixelColor,c) <= okDist
						? Color.BLACK.getRGB() : Color.WHITE.getRGB();
				image.setRGB(col,row,newRgb);   //set the new color
			}
		}
	}

	/**
	 * Finds the root of the pixel that is to the north of the current pixel.
	 * Finds the root of the pixel that is to the west of the current pixel.
	 * @param image Buffered Image
	 * @param ds    Disjoint set that holds the pixels
	 * @param pixelId   Id of the current pixel
	 * @return  {@code Pair<Root1, Root2>}, Roots of north and west
	 */
	public static Pair<Integer,Integer> getNeighborSets(BufferedImage image, DisjointSets<Pixel> ds, int pixelId)
	{
		Pixel top, left;
		Pixel curr = getPixel(image,pixelId);
		int rootTop, rootLeft;

		if(curr.b - 1 < 0) top = curr;      // edge case
		else top = getPixel(image, getId(image, curr.a, (curr.b - 1)));
		if(curr.a - 1 < 0) left = curr;     //edge case
		else left = getPixel(image,getId(image,(curr.a-1),curr.b));

		rootLeft = ds.find(getId(image,left));  //compress path
		rootTop = ds.find(getId(image,top));    //compress path

		return new Pair<>(rootTop,rootLeft);
	}

	/**
	 * Most important method.
	 * Walks the the image and processes each individual pixels.
	 * Unions the pixel to the north and its west if those pixels
	 * matches the criteria.
	 *
	 */
	public void detect() {
		int height = this.img.getHeight();
		int width = this.img.getWidth();
		int arrayLen = width*height, id = 0;
		System.out.println("Pixels: " + arrayLen);

		Pair<Integer,Integer> position;
		ArrayList<Pixel> list = new ArrayList<>(arrayLen);

		initArrayList(list,arrayLen);

		thresh(this.img,this.blobColor,this.okDist);

		this.ds = new DisjointSets<>(list);

		for(int i = 0; i < height; i++){
			//System.out.print(". ");
			for(int j = 0; j < width;j++){
				id = getId(this.img,j,i);
				position = getNeighborSets(this.img,this.ds,id);
				unionPixels(this.img,position,id);
			}
		}
		//System.out.println();
	}

	/**
	 * Unions the pixels to the current pixel.
	 *
	 * @param img Buffered Image
	 * @param position  {@code Pair< Integer,Integer>} roots of north and west pixels
	 * @param id    ID of the currPixel
	 */
	private void unionPixels(BufferedImage img, Pair<Integer,Integer> position, int id)
	{
		if(position.a == id)    //Edge case
			unionLeftPixel(img,position.b,id);
		else if(position.b == id)   //Edge Case
			unionTopPixel(img,position.a,id);
		else{
			unionTopPixel(img, position.a, id);
			unionLeftPixel(img, position.b, id);
		}
	}

	/**
	 * Unions the left pixel to the current Pixel.
	 * @param img   Buffered Image
	 * @param left  Root of the left pixel
	 * @param id    Root of current Pixel
	 */
	private void unionLeftPixel(BufferedImage img, int left, int id)
	{
		if(isLeftPixelSame(img,left,id))
			ds.union(ds.find(left),ds.find(id));
	}

	/**
	 * Unions the top pixel to the current pixel.
	 *
	 * @param img Buffered Image
	 * @param top Root of the top pixel
	 * @param id  Root of the current pixel
	 */
	private void unionTopPixel(BufferedImage img, int top, int id)
	{
		if(isTopPixelSame(img,top,id))
			ds.union(ds.find(top),ds.find(id));
	}

	/**
	 * Initializes the array list that holds the pixel IDs
	 *
	 * @param list ArrayList
	 * @param arrayLen  Length of the list
	 */
	private void initArrayList(ArrayList<Pixel> list, int arrayLen)
	{
		for(int i = 0; i < arrayLen; i++)
			list.add(getPixel(img,i));
	}

	/**
	 * Checks if the left pixel is the same color as the current Pixel
	 * @param img   Buffered Image
	 * @param root2 ID of left pixel
	 * @param pixId ID of current pixel
	 * @return  True if same color
	 */
	private boolean isLeftPixelSame(BufferedImage img, int root2, int pixId)
	{
		Color org = getColor(img,getPixel(img,pixId));
		Color left = getColor(img,getPixel(img,root2));
		return org.equals(left);
	}

	/**
	 * Checks if the top pixel is the same color as the current Pixel
	 * @param img   Buffered Image
	 * @param root1 ID of top pixel
	 * @param pixId ID of current pixel
	 * @return  True if same color
	 */
	private boolean isTopPixelSame(BufferedImage img, int root1, int pixId)
	{
		Color org = getColor(img,getPixel(img,pixId));
		Color top = getColor(img,getPixel(img,root1));
		return org.equals(top);
	}

	/**
	 * Goes through the image and recolors the image using the appropriate shades
	 * of the calculated color based on size of the disjoint set blobs
	 * @param roots - Set of roots of blobs
	 * @param pixels - Disjoint set of pixels
	 * @param data - row and col of the image
	 */
	private void recolor(ArrayList<Integer> roots, ArrayList<Set<Pixel>> pixels, int...data)
	{
		int k = data[0],color,row = data[1], col = data[2];
		for(int i = 0; i < k; i++){
			System.out.printf("Blob %d: %d\n",i+1,pixels.get(i).size());
			color = getSeqColor(i,k).getRGB();
			for(int m = 0; m < row; m++) {
				for (int n = 0; n < col; n++) {
					if (ds.find(getId(this.img, n, m)) == roots.get(i))
						this.img.setRGB(n, m, color);       //recolor
				}
			}
		}
	}

	/**
	 * Sorts both the roots and the pixels of the image
	 * @param roots - Integer list of roots
	 * @param pixels - Set of pixels
	 */
	private void sortTrees(ArrayList<Integer> roots, ArrayList<Set<Pixel>> pixels)
	{
		roots.sort((r1, r2) -> Integer.compare(ds.get(r2).size(), ds.get(r1).size()));
		pixels.sort((p1, p2) -> Integer.compare(p2.size(), p1.size()));
	}

	/**
	 * Prepares the image by gathering the roots and the pixels of the image
	 * @param k - Number of blobs to find
	 */
	private void prepImageOutput(int k)
	{
		int row = this.img.getHeight(), col = this.img.getWidth();

		int arrLen = this.img.getWidth() * this.img.getHeight();
		ArrayList<Integer> roots = new ArrayList<>(arrLen);   // Holds the roots of desired blobs
		ArrayList<Set<Pixel>> pixels = new ArrayList<>(arrLen);   //Holds the pixels of the roots

		for (int i = 0; i < arrLen; i++) {
			if (ds.get(i).size() > 0 && getColor(this.img, getPixel(this.img, i)).equals(Color.BLACK)) {
				roots.add(i); pixels.add(this.ds.get(i));
			}
		}
		sortTrees(roots,pixels);
		if(k > roots.size()) k = roots.size();
		k = roots.size();
		System.out.printf("%d/%d\n",k,roots.size());
		recolor(roots,pixels,k,row,col);
	}

	/**
	 * Outputs the thresholded image.
	 *
	 * Recolors the image with the desired color k times.
	 * Recolors the blobs in descending order with the largest blob
	 * getting the original color and the remaining gets a shade darker
	 * each time.
	 *
	 * @param outputFileName  Output file name
	 * @param k     Number of blobs to color
	 */
	public void outputResults(String outputFileName, int k)
	{
		if (k < 1) {
			throw new IllegalArgumentException(new String("! Error: k should be greater than 0, current k=" + k));
		}

		prepImageOutput(k);

		//Output file
		try {
			File ouptut = new File(outputFileName);
			ImageIO.write(this.img, "png", ouptut);
			System.err.println("- Saved result to "+outputFileName);
		}
		catch (Exception e) {
			System.err.println("! Error: Failed to save image to "+outputFileName);
		}

		/*
		//if you're doing the EC and your output image is still this.img,
		//you can uncomment this to save this.img to the specified outputECFileName
		try {
			File ouptut = new File(outputECFileName);
			ImageIO.write(this.img, "png", ouptut);
			System.err.println("- Saved result to "+outputECFileName);
		}
		catch (Exception e) {
			System.err.println("! Error: Failed to save image to "+outputECFileName);
		}
		*/
	}

	//Main
	public static void main(String[] args) {

		System.out.println("Main method of Detector class");

	}

	//-----------------------------------------------------------------------
	//
	// Read and provide comments, but do not change the following code
	//
	//-----------------------------------------------------------------------

	/**
	 * this is the 2D array of RGB pixels
	 */
	public BufferedImage img;

	/**
	 * the color of the blob we are detecting
	 */
	private Color blobColor;
	/**
	 * input image file name
	 */
	private String imgFileName;
	/**
	 * the disjoint set
	 */
	private DisjointSets<Pixel> ds;
	/**
	 * the distance between blobColor and the pixel which "still counts" as the color
	 */
	private int okDist;

	/**
	 * Constructor that stores the image and other data
	 * @param imgfile   Buffered Image
	 * @param blobColor Desired color
	 * @param okDist    Distance between two colors
	 */
	public Detector(String imgfile, Color blobColor, int okDist) {
		this.imgFileName=imgfile;
		this.blobColor = blobColor;
		this.okDist = okDist;

		System.out.println("Recieved this: " + this.imgFileName);

		reloadImage();
	}

	/**
	 * Reads the original image from the file.
	 */
	public void reloadImage() {
		File imageFile = new File(this.imgFileName);

		try {
			this.img = ImageIO.read(imageFile);
		}
		catch(IOException e) {
			System.err.println("! Error: Failed to read "+this.imgFileName+", error msg: "+e);
			return;
		}
	}

	/**
	 * JPanel function
	 * @param g Graphics
	 */
	public void paint(Graphics g) {
		g.drawImage(this.img, 0, 0,this);
	}

	//private classes below

	/**
	 * Convenient helper class representing a pair of things
	 */
	private static class Pair<A,B> {
		/**
		 * Holds a value
		 */
		A a;
		/**
		 * Holds another value
		 */
		B b;

		/**
		 * Stores the two values as pair
		 * @param a A
		 * @param b B
		 */
		public Pair(A a, B b) {
			this.a=a;
			this.b=b;
		}
	}

	/**
	 * A pixel is a set of locations a (aka. x, distance from the left) and b (aka. y, distance from the top)
	 */
	private static class Pixel extends Pair<Integer, Integer> {
		public Pixel(int x, int y) {
			super(x,y);
		}
	}

	/**
	 * Convert a pixel in an image to its ID
	 * @param image Buffered Image
	 * @param p Pixel
	 * @return ID of pixel
	 */
	private static int getId(BufferedImage image, Pixel p) {
		return getId(image, p.a, p.b);
	}

	/**
	 * Convex ID for an image back to a pixel
	 * @param image Buffered Image
	 * @param id  Pixel ID
	 * @return Pixel
	 */
	private static Pixel getPixel(BufferedImage image, int id) {
		int y = id/image.getWidth();
		int x = id-(image.getWidth()*y);

		if(y<0 || y>=image.getHeight() || x<0 || x>=image.getWidth())
			throw new ArrayIndexOutOfBoundsException();

		return new Pixel(x,y);
	}

	/**
	 * Converts a location in an image into an id
	 * @param image Buffered Image
	 * @param x position
	 * @param y position
	 * @return ID of pixel
	 */
	private static int getId(BufferedImage image, int x, int y) {
		return (image.getWidth()*y)+x;
	}

	/**
	 * Returns the color of a given pixel in a given image
	 * @param image Buffered Image
	 * @param p Pixel
	 * @return Color of pixel
	 */
	private static Color getColor(BufferedImage image, Pixel p) {
		return new Color(image.getRGB(p.a, p.b));
	}

	/**
	 * {@code Pass 0 -> k-1 as i to get the color for the blobs 0 -> k-1}
	 * @param i Initial value
	 * @param max Max value
	 * @return Next color in sequence
	 */
	private Color getSeqColor(int i, int max) {
		if(i < 0) i = 0;
		if(i >= max) i = max-1;

		int r = (int)(((max-i+1)/(double)(max+1)) * blobColor.getRed());
		int g = (int)(((max-i+1)/(double)(max+1)) * blobColor.getGreen());
		int b = (int)(((max-i+1)/(double)(max+1)) * blobColor.getBlue());

		if(r == 0 && g == 0 && b == 0) {
			r = g = b = 10;
		}
		else if(r == 255 && g == 255 && b == 255) {
			r = g = b = 245;
		}

		return new Color(r, g, b);
	}
}