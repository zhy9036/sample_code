"use strict";
/**
 * Yang Zhang
 * This is the js file for Skill Matcher using jQuery	
 */

$(document).ready(function(){	
	var haha = "python";
	var URL = "https://jobs.github.com/positions.json?description=";


	//function for seraching with ajax
	$.fn.search = function(){
		$.ajax({
			type: "GET",
			url: "https://jobs.github.com/positions.json",
			dataType: "jsonp",
			data: {description:$("#skill").val()},
			cache:false,
			success: function(html){
				if(!jQuery.isEmptyObject(html)){
					$.each(html, function(key, value){
						var tr = document.createElement('tr');
						$.each(value, function(key1,value1){
							// filter data
							if(key1 == "title" || key1 == "location" || key1 == "company"){
								var td = document.createElement('td');
								td.innerHTML = value1
								tr.appendChild(td)
							}
						});
						$("#jobtable").append(tr);
					});
					$("#jobtable").show();
				}else{
					document.getElementById("ifempty").innerHTML = "No Result Found!";
				}
				$("#loadingJobs").hide();
				
			},
			error: function(a, b, c) {
				alert("werewr");
			}
		});
	};


	$("#search").click(function(e){
		e.preventDefault();
		//reset result area
		document.getElementById("ifempty").innerHTML = "";
		document.getElementById("jobtable").innerHTML = "";
		var tr = document.createElement('tr');
		tr.innerHTML = "<th>JOB TITLE</th><th>LOCATION</th><th>COMPANY</th>";
		$("#jobtable").append(tr);
		$(this).search();
		$("#loadingJobs").show();
		$("#jobtable").hide();
		return false;	
	});
});