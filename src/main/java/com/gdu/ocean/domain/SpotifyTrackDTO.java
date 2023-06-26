package com.gdu.ocean.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpotifyTrackDTO {
	private String artistName;
	private String title;
	private String albumName;
	private Object imageUrl;
}
