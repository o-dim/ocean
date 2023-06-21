 function fnSportifySearch() {
        $('#searchBtn').click(function() {	    	
            $.ajax({
                url: '/song/spotifysearch.do',
                type: 'GET',
                data: 'q=' + $('#q').val(),
                dataType: 'json',
                success: function(resData) {
                    $('#result').empty();
                    // $('#result').append('검색결과 : ' + resData.total);
                    $.each(resData.songList, function(i, song) {
                    	var str = '<tr class="song-line">'
                        str += '<td><img src="'  + song.imgUrl + '" class="imgUrl">';
                        str += '<td class="title-singer"><div class="song-title">' + song.title + '</div><div class="singer">' + song.singer + '</div>';
                        str += '<td>' + song.music2 + '</td>';
                        // str += '<td><audio src="' + song.music2 + '" controls></audio>';
	                    $('#result').append(str);
                    });
                }
            });
        });
    }
    
    
    function fnRecommendsong() {
		$('#recommendBtn').click(function(){
	        $.ajax({
	            url: '/song/recommendsong.do',
	            type: 'GET',
	            data: 'mood=' + $('#mood').val(),
	            dataType: 'json',
	            success: function(resData) {
	                $('#recommendResult').empty();
	                var table = $('<table>');
	                var tableHeader = '<tr><th>title</th><th>preview</th></tr>';
	                table.append(tableHeader);
	                $.each(resData, function(i, song) {
	                    var row = $('<tr>');
	                    var title_td = $('<td>').append('<div class="song-title">' + song.title + '</div><div class="singer">' + song.singer + '</div>');
	                    var preview_td = $('<td>').append('<audio src="' + song.preview + '" controls></audio>');
	                    row.append(title_td, preview_td);
	                    table.append(row);
	                });
	                $('#recommendResult').append(table);
	            }
	        });
		})
    }
    
 
    $(function() {
        fnSportifySearch();
        fnRecommendsong();
        $.ajax({
            url: '/song/recommendsong.do',
            type: 'GET',
            dataType: 'json',
            data: 'mood=chill',
            success: function(resData) {
                $('#recommendResult').empty();
                var table = $('<table>');
                var tableHeader = '<tr><th>title</th><th>preview</th></tr>';
                table.append(tableHeader);
                $.each(resData, function(i, song) {
                    var row = $('<tr>');
                    var title_td = $('<td>').append('<div class="title">' + song.title + '</div><div class="singer">' + song.singer + '</div>');
                    var preview_td = $('<td>').append('<audio src="' + song.preview + '" controls></audio>');
                    row.append(title_td, preview_td);
                    table.append(row);
                });
                $('#recommendResult').append(table);
            }
    	});
    });