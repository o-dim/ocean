package com.gdu.ocean.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SongDTO {
	
	private int songNo;
	private String title;
	private String singer;
	private String album;
	private int songTotal;
	private PlaylistDTO playlistNo;
	private CdDTO cdNo;
}
