DROP TABLE IF EXISTS ARTISTS;

CREATE TABLE ARTISTS (
    USERNAME VARCHAR(255) NOT NULL PRIMARY KEY,
    ARTIST_NAME VARCHAR(255) NOT NULL,
    ARTIST_GENRE VARCHAR(255) NOT NULL,
    ALBUMS_RECORDED INT NOT NULL
);
