package servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@WebServlet(name = "colorDetector", urlPatterns = {"/colorDetector"})
@MultipartConfig
public class OnlineBlobDetector extends HttpServlet
{
	private final static String TMP_PATH = "src/main/java/servlet/tmp";
	private int r,g,b, d;
	private String filePath;
	private String colorPickerValue;
	@Override
	protected void doPost  (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		boolean single = false, both = false;
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		try {
			r = Integer.parseInt(request.getParameter("red"));
			g = Integer.parseInt(request.getParameter("green"));
			b = Integer.parseInt(request.getParameter("blue"));
			System.out.printf("RGB Values: r: %d, g: %d, b: %d\n",r,g,b);
			single = true;
		}catch (NumberFormatException e){
			single = false;
		}

		colorPickerValue = request.getParameter("colorPickerValue");
		System.out.println(colorPickerValue);

		if(!single){

			System.out.println("We are here for some reason");
			r = Integer.valueOf(colorPickerValue.substring(1, 3), 16);
			g = Integer.valueOf(colorPickerValue.substring(3, 5), 16);
			b = Integer.valueOf(colorPickerValue.substring(5, 7), 16);
			System.out.println("Color Picker: " + colorPickerValue);
//			System.out.printf("RGB Values: r: %d, g: %d, b: %d\n",r1,r2,r3);
		}

		try{
			d = Integer.parseInt(request.getParameter("dist"));
			System.out.println("d value: "+ d);
		}catch (NumberFormatException e){
			e.printStackTrace();
		}

		Part filePart = request.getPart("img");
		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
		InputStream fileContent = filePart.getInputStream();

		byte[] buffer = new byte[fileContent.available()];
		fileContent.read(buffer);

		File targetFile = new File(TMP_PATH+"/"+fileName);
		OutputStream outStream = null;
		try {
			outStream = new FileOutputStream(targetFile);
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		assert outStream != null;
		outStream.write(buffer);
		outStream.close();
		fileContent.close();
		System.out.println(fileName);
		System.out.println(filePart.getSubmittedFileName());

		System.out.printf("RGB Values: r: %d, g: %d, b: %d d: %d\n",r,g,b,d);
		Detector detector = new Detector(new File(targetFile.getAbsolutePath()).toString(), new Color(r, g, b), d);
		detector.detect();

		detector.outputResults("output1.png",1);

		String body = "<!doctype html>\n" +
				"\n" +
				"<html lang=\"en\">\n" +
				"<head>\n" +
				"  <meta charset=\"utf-8\">\n" +
				"\n" +
				"  <title>The HTML5 Herald</title>\n" +
				"\n" +
				"</head>\n" +
				"\n" +
				"<body>\n" +
				"\t<img src=\""+fileName+"\">\n" +
				"</body>\n" +
				"</html>";

		//out.println(body);

		printHead(out);

		printTableBody(out,"output1.png");


		printTail(out);


	}

	private void printHead(PrintWriter out)
	{
		out.println("<html>");
		out.println("");
		out.println("<head>");
		out.println("<title>Image Color Finder</title>");
		out.println("</head>");
		out.println("");
	}
	private void printTableBody(PrintWriter out, String fileName)
	{
		out.println("<body style=\"background-color:#c0c0c0;\">");
		out.println("<h1>");
		out.println("Your uploaded image");
		out.println("</h1>");
		out.println("");
		String image = "<img src= \""+fileName+"\" alt = \"Broken Link\">";
		System.out.println(image);
//		out.println(image);
		out.println("<label>Saved to: " +fileName+ "</label>");
		out.println("");
		out.println("</body>");
	}

	private void printTail(PrintWriter out)
	{
		out.println("");
		out.println("</html>");
	}

	private void showErrorMessage(PrintWriter out)
	{

		String someMessage = "Please enter RGB values or choose a color using color picker !";
		out.println("<html><head>");

		out.println("<script type='text/javascript'>");
		out.println("alert(" + "'" + someMessage + "'" + ");</script>");
		out.println("</head><body></body></html>");
	}


	public void destroy()
	{
		File dir = new File(TMP_PATH);

		if(dir.isDirectory() == false) {
			System.out.println("Not a directory. Do nothing");
			return;
		}
		File[] listFiles = dir.listFiles();
		for(File file : listFiles){
			System.out.println("Deleting "+file.getName());
			file.delete();
		}
	}
}
