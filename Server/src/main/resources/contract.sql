Connect 'jdbc:derby:CONTRACT;create=true';

CREATE TABLE fileinfo (
    fileid INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)primary key,
    fileUuid VARCHAR(50),
    fileName VARCHAR(50),
    randomKey VARCHAR(1500)
);
