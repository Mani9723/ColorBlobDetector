<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
  <script>
    var servletURL = window.location.origin;
  </script>
<title>Blob Detector</title>
</head>
<%@ page import="java.util.Date" %>
<body>
<form name = "myForm" enctype="multipart/form-data" action="colorDetector" method = "POST">

  <h2>STEP 1:</h2>
  <label for="img">Select image:</label>
  <input type="file" id="img" name="img" accept="image/*" required><br>


  <h2>STEP 2:</h2>
  <h4>Option 1:</h4>
  <label>Input RGB values</label><br>
  <label>r:</label>
  <input name = "red" type="number" min = "0" max = "255" size = "3">
  <label>g:</label>
  <input name = "green" type="number" min = "0" max = "255" size = "3">
  <label>b:</label>
  <input name = "blue" type="number" min = "0" max = "255" size="3"><br>

  <h4>Option 2:</h4>
  <label for="colorpicker">Color Picker:</label>
  <input name = "colorPickerValue" type="color" id="colorpicker" value = "#aeaea6"><br>


  <h2>STEP 3:</h2>
  <h4>Please choose a margin between 0-10. This number indicates the distance from a particular color where you
  still would consider it to be that color</h4>
  <input type="range" id="distance" name="dist" min="0" max="10" value="5"
         oninput="amount.value=dist.value" required>

  <output id="amount" name="amount" for="rangeInput">5</output>

 <br><input type="submit">
</form>
</body>
</html>
