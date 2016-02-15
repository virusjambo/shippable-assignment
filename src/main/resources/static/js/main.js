

$(document).ready(function() {
	
	$("#submit").on("click", function(e) {
		$("#error_div").addClass('hide');
		$("#total").text("0");
    	$("#past24").text("0");
    	$("#past7").text("0");
    	$("#all_issues").text("0");
    	$("#progress-bar").removeClass("hide");

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
			    	$("#progress-bar").addClass("hide");

			    	$("#total").text(res.total_issues);
			    	$("#past24").text(res.total_issues_past_day);
			    	$("#past7").text(res.total_issues_past_week);
			    	$("#all_issues").text(res.total_issues_more_than_week);

			    	
			    }else{
			    	$("#error_div").removeClass('hide');
			    	$("#error").text(res.error_code + "  "+res.error_message );
			    	$("#progress-bar").addClass("hide");

			    	
			    }
			    
			  },
			  failure:function(res){
			    	$("#progress-bar").addClass("hide");
			  }
			});
	});
});