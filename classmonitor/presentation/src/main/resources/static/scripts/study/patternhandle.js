$("#plus").click(function(){
	var plus_div = $("<div>"+
					"<select id='student' style='display:inline-block;margin:10px 10px 0px 0px'>"+
					" 	<option value='' style='display:none;'>请选择学生</option>"+
					"	<option value='1'>张小明</option>"+
					"	<option value='2'>李小红</option>"+
					"	<option value='3'>蔡小丁</option>"+
					"	<option value='4'>叶小秀</option>"+
					"</select>"+
					"<select id='behavior' style='display:inline-block;margin:10px 10px 0px 0px'>"+
					" 	<option value='' style='display:none;'>请选择行为</option>"+
					"	<option value='1'>多次迟到</option>"+
					"	<option value='2'>多次缺席</option>"+
					"	<option value='3'>多次结对离开</option>"+
					"	<option value='4'>多次离席</option>"+
					"	<option value='5'>多次睡觉</option>"+
					"</select>"+
					"</div>");
	$("#select_team").append(plus_div);
})