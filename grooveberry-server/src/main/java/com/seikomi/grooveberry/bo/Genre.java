package com.seikomi.grooveberry.bo;

/**
 * Enumeration of the principal music genre available in the ID3 metadata
 * container.
 * 
 * @author Nicolas SYMPHORIEN (nicolas.symphorien@gmail.com)
 */
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
	
	/**
	 * Construct a genre enumerate.
	 * 
	 * @param index 
	 *           the index referenced in the genre ID3 
	 * @param label
	 *           the associated label
	 */
	private Genre(int index, String label) {
		this.index = index;
		this.label = label;
	}
	
	/**
	 * Gets the ID3 genre index of this enumerate.
	 * 
	 * @return the index 
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Gets the label of this enumerate.
	 * 
	 * @return the label 
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Gets the genre enumerate identifies by his id.
	 * 
	 * @param genreId
	 *            the genre enumerate id
	 * @return the genre enumerate
	 */
	public static Genre getGenre(int genreId) {
		for (Genre genre : values()) {
			if (genre.getIndex() == genreId) {
				return genre;
			}
		}
		return null;
	}

}
