<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
  <title>Welcome to ShareThings</title>
  <link rel="stylesheet" href="CSS/loginprofile.css">
</head>
<body>
<%Object o = request.getAttribute("response");
	String display = null;
	if (o == null){
	}
	else{
		display = o.toString();
	}
	
  Object failures = request.getAttribute("failures");
  String failMessage = "";
  if(failures == null){
	  
  }else{
	  failMessage = failures.toString();
  }
%>
  <div id="wrap">
  <div id="regbar">
    <div id="navthing">
    <div id="home-container">
      <h1>  ShareThings </h1>        <p>Welcome User: <%= display %> </p> &nbsp  <p><a href="logout">LogOut</a> </p> <br/>
      <p> <%= failMessage %></p>
       <h2 style = "align:center; vertical-align:middle;"><a href="ViewProfile">Profile</a> | <a href="Order">order</a> | <a href="OrderHistory">Order History & Management</a></h2>
      </div>
      </div>
    </div>
    </div>
</body>
</html>