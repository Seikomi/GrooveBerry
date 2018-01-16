package com.seikomi.grooveberry.bo;

public enum Genre {
	BLUES(0, "Blues"),
	CLASSIC_ROCK(1, "Classic rock"),
	COUNTRY(2, "Country"),
	DANCE(3, "Dance"),
	DISCO(4, "Disco"),
	FUNK(5, "Funk"),
	GRUNGE(6, "Grunge"),
	HIP_HOP(7, "Hip-hop"),
	JAZZ(8, "Jazz"),
	METAL(9,"Metal"),
	NEW_AGE(10, "New age"),
	OLDIES(11, "Oldies"),
	AUTRE(12, "Autre"),
	POP(13, "Pop"),
	RNB(14,	"RnB"),
	RAP(15, "Rap"),
	REGGAE(16, "Reggae"),
	ROCK(17, "Rock"),
	TECHNO(18,"Techno"),
	INDUSTRIAL(19, "Musique industrielle (industrial)"),
	ALTERNATIVE(20, "Rock alternatif (alternative)"),
	SKA(21,	"Ska"),
	DEATH_METAL(22, "Death metal");
	
	private int index;
	private String label;
	
	private Genre(int index, String label) {
		this.index = index;
		this.label = label;
	}

	public int getIndex() {
		return index;
	}

	public String getLabel() {
		return label;
	}

	public static Genre getGenre(int genreId) {
		for (Genre genre : values()) {
			if (genre.getIndex() == genreId) {
				return genre;
			}
		}
		return null;
	}

}
