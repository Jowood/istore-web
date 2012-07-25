<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en"  lang="en">
<head>
    <title>项目资料管理</title> 

	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<meta http-equiv="Cache-Control" content="no-cache" />
    <!--[if IE 9]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />   
    <![endif]-->
    <link rel="stylesheet" href="ui/global.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="ui/loginStyle.css" type="text/css" media="screen" />
	<link href="ui/login.css" type="text/css" rel="stylesheet" media="screen" />
    <script type="text/javascript" src="ui/jquery.js"></script> 

	<script type="text/javascript">
	function login() {
		$('#Form1').submit();
	}
	
	$(function() {
		document.onkeydown = function(e){
		    var ev = document.all ? window.event : e;
		    if(ev.keyCode==13) {

		    	login();

		     }
		}
        var status = '${requestScope.status}';
        
        switch (status) {
	        case '-1':
	            alert("请输入用户名");
	            break;
	        case '-2':
	            alert("请输入密码");
	            break;
	        case '-3':
	            alert("不存在该用户名");
	            break;
	        case '-4':
	            alert("密码错误");
	            break; 
	    }
	});
	</script>
	
</head>
<body class="login"> 
    <!--[if lt IE 7]>
    <link rel="stylesheet" href="ui/ie6.css" type="text/css" media="screen" />    
    <![endif]-->	
    <form name="Form1" method="post" action="checkUserLogin.login" id="Form1">
  
  

    <div style="margin: 30px auto -55px; padding: 18px; width: 520px;">
    
    </div>

    <div id="panelErrorMsg">

	
        
    
</div>
 
    <div id="login">
	    <div id="cap-top"></div>
	    <div id="cap-body">
	    <div id="branding" class="clearfix">
            <img id="imgLogo" src="images/login/title.png" style="border-width:0px;height:132px;width:466px;float:left;" />
            
        </div>		
            <div id="panelLogin">
	
			    <div>

				    <label>用户名:</label>
                        <input type="text" class="textbox340" name="name" id="name" value="" />
			    </div>
			    <div>
				    <label>密码:</label>
                        <input type="password" class="textbox340" name="password" id="password" value="" />

			    </div>
                					
                <div class="submit clearfix" style="text-align:right;">                    
                    <a id="lbtnLogin" class="buttonLoginV2" href="javascript:login()">登录</a>                    
                </div> 
		    
</div>	

            

            			           
            						
		    			
		    
	    </div>
	    <div id="cap-bottom"><img src="images/login/cap-bottom.png" alt="" /></div>
    </div><!-- END #login -->

    </form> 

</body>
</html>
