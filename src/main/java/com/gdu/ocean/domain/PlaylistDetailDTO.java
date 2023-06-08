package com.gdu.ocean.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDetailDTO {
	private int PlaylistDetailNo;
	private PlaylistDTO playlistNo;
	private SongDTO songNo;

}
