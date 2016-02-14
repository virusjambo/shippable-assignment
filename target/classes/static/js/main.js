

$(document).ready(function() {
	
	$("#submit").on("click", function(e) {
		$("#error_div").addClass('hide');
		var path=$("#repo_name").val();
		if(path && path.trim()=="" ){
			$("#error_div").removeClass('hide');
	    	$("#error").text('Please enter URL' );

		}
		$.ajax({
			  url:'/info?value='+path,
			  type:"GET",
			  contentType:"application/json",
			  dataType:"json",
			  success: function(res){
			    if(res && res.success){
			    	$("#total").text(res.total_issues);
			    	$("#past24").text(res.total_issues_past_day);
			    	$("#past7").text(res.total_issues_past_week);
			    }else{
			    	$("#error_div").removeClass('hide');
			    	$("#error").text(res.error_code + "  "+res.error_message );

			    	
			    }
			    
			  },
			  failure:function(res){
			    alert("failure");
			  }
			});
	});
});