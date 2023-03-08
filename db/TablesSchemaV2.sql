CREATE TABLE ANALYZER(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
 	name TEXT NOT NULL UNIQUE
);

CREATE TABLE GODCLASS(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	classname TEXT NOT NULL,
	wmc INTEGER NOT NULL,
	atfd INTEGER NOT NULL,
	tcc REAL NOT NULL,
	analyzerid INT NOT NULL,
	projectname TEXT NOT NULL, 
	path TEXT NOT NULL,
	insertdate datetime default current_timestamp,
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)
);

CREATE TABLE FEATUREENVY(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	classname TEXT NOT NULL,
	brainname TEXT NOT NULL,
	atfd INTEGER NOT NULL,
	laa REAL NOT NULL,
    fdp INTEGER NOT NULL,
	analyzerid INT NOT NULL,
	projectname TEXT NOT NULL, 
	path TEXT NOT NULL,
	insertdate datetime default current_timestamp,
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)
);


CREATE TABLE DATACLASS(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	classname TEXT NOT NULL,
	woc REAL NOT NULL,
	nopa INTEGER NOT NULL,
	noam INTEGER NOT NULL,
	wmc INTEGER NOT NULL,
	analyzerid INT NOT NULL,
	projectname TEXT NOT NULL, 
	path TEXT NOT NULL,
	insertdate datetime default current_timestamp,
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)
);


CREATE TABLE BRAINCLASS(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	classname TEXT NOT NULL,
	loc INTEGER NOT NULL,
	wmc INTEGER NOT NULL,
	tcc REAL NOT NULL,
	numberBrainMethods INTEGER NOT NULL,
	analyzerid INT NOT NULL,
	projectname TEXT NOT NULL, 
	path TEXT NOT NULL,
	insertdate datetime default current_timestamp,
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)
);

CREATE TABLE BRAINMETHOD(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	classname TEXT NOT NULL,
    methodname TEXT NOT NULL,
	loc INTEGER NOT NULL,
	cyclo INTEGER NOT NULL,
	maxnesting INTEGER NOT NULL,
    noav INTEGER NOT NULL,
	analyzerid INT NOT NULL,
	projectname TEXT NOT NULL, 
	path TEXT NOT NULL,
	insertdate datetime default current_timestamp,
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)
);



INSERT INTO  "ANALYZER" ("name") VALUES ('Analyzer');
INSERT INTO  "ANALYZER" ("name") VALUES ('PMD');