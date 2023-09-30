DROP TABLE GODCLASS;
DROP TABLE FEATUREENVY;
DROP TABLE DATACLASS;
DROP TABLE BRAINCLASS;
DROP TABLE BRAINMETHOD;
DROP TABLE PROMISCUOUSCONTROLLER;
DROP TABLE BRAINCONTROLLER;
DROP TABLE MEDDLINGSERVICE;
DROP TABLE LABORIOUSREPOSITORYMETHOD;
DROP TABLE BRAINREPOSITORY;
DROP TABLE FATREPOSITORY;
DROP TABLE ANALYZER;

CREATE TABLE ANALYZER(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
 	name TEXT NOT NULL UNIQUE
);

CREATE TABLE GODCLASS(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	classname TEXT NOT NULL,
	mainclassname TEXT NOT NULL,
	wmc INTEGER NOT NULL,
	atfd INTEGER NOT NULL,
	tcc REAL NOT NULL,
	analyzerid INT NOT NULL,
	projectname TEXT NOT NULL, 
	path TEXT NOT NULL,
	language TEXT NOT NULL,
	insertdate datetime default current_timestamp,
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)
);

CREATE TABLE FEATUREENVY(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	classname TEXT NOT NULL,
    mainclassname TEXT NOT NULL,
	methodname TEXT NOT NULL,
	atfd INTEGER NOT NULL,
	laa REAL NOT NULL,
    fdp INTEGER NOT NULL,
	analyzerid INT NOT NULL,
	projectname TEXT NOT NULL, 
	path TEXT NOT NULL,
	language TEXT NOT NULL,
	insertdate datetime default current_timestamp,
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)
);


CREATE TABLE DATACLASS(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	classname TEXT NOT NULL,
    mainclassname TEXT NOT NULL,
	woc REAL NOT NULL,
	nopa INTEGER NOT NULL,
	noam INTEGER NOT NULL,
	wmc INTEGER NOT NULL,
	analyzerid INT NOT NULL,
	projectname TEXT NOT NULL, 
	path TEXT NOT NULL,
	language TEXT NOT NULL,
	insertdate datetime default current_timestamp,
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)
);


CREATE TABLE BRAINCLASS(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	classname TEXT NOT NULL,
    mainclassname TEXT NOT NULL,
	loc INTEGER NOT NULL,
	wmc INTEGER NOT NULL,
	tcc REAL NOT NULL,
	numberBrainMethods INTEGER NOT NULL,
	analyzerid INT NOT NULL,
	projectname TEXT NOT NULL, 
	path TEXT NOT NULL,
	language TEXT NOT NULL,
	insertdate datetime default current_timestamp,
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)
);

CREATE TABLE BRAINMETHOD(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	classname TEXT NOT NULL,
    mainclassname TEXT NOT NULL,
    methodname TEXT NOT NULL,
	loc INTEGER NOT NULL,
	cyclo INTEGER NOT NULL,
	maxnesting INTEGER NOT NULL,
    noav INTEGER NOT NULL,
	analyzerid INT NOT NULL,
	projectname TEXT NOT NULL, 
	path TEXT NOT NULL,
	language TEXT NOT NULL,
	insertdate datetime default current_timestamp,
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)
);

CREATE TABLE PROMISCUOUSCONTROLLER(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	classname TEXT NOT NULL,
    mainclassname TEXT NOT NULL,
	NOR INTEGER NOT NULL,
	NSD INTEGER NOT NULL,	
	analyzerid INT NOT NULL,
	projectname TEXT NOT NULL, 
	path TEXT NOT NULL,
	language TEXT NOT NULL,
	insertdate datetime default current_timestamp,
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)
);

CREATE TABLE BRAINCONTROLLER(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	classname TEXT NOT NULL,
    mainclassname TEXT NOT NULL,
	NFRC INTEGER NOT NULL,
	analyzerid INT NOT NULL,
	projectname TEXT NOT NULL, 
	path TEXT NOT NULL,
	language TEXT NOT NULL,
	insertdate datetime default current_timestamp,
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)
);

CREATE TABLE MEDDLINGSERVICE(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	classname TEXT NOT NULL,
    mainclassname TEXT NOT NULL,
	persistenceDependencies INTEGER NOT NULL,
	analyzerid INT NOT NULL,
	projectname TEXT NOT NULL, 
	path TEXT NOT NULL,
	language TEXT NOT NULL,
	insertdate datetime default current_timestamp,
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)
);

CREATE TABLE LABORIOUSREPOSITORYMETHOD(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	classname TEXT NOT NULL,
    mainclassname TEXT NOT NULL,
    methodname TEXT NOT NULL,
	quantityPersistenceActions INTEGER NOT NULL,
	analyzerid INT NOT NULL,
	projectname TEXT NOT NULL, 
	path TEXT NOT NULL,
	language TEXT NOT NULL,
	insertdate datetime default current_timestamp,
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)
);

CREATE TABLE BRAINREPOSITORY(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	classname TEXT NOT NULL,
    mainclassname TEXT NOT NULL,
	cyclo INTEGER NOT NULL,
	sqlcomplexity INTEGER NOT NULL,
	analyzerid INT NOT NULL,
	projectname TEXT NOT NULL, 
	path TEXT NOT NULL,
	language TEXT NOT NULL,
	insertdate datetime default current_timestamp,
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)
);

CREATE TABLE FATREPOSITORY(
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
	classname TEXT NOT NULL,
    mainclassname TEXT NOT NULL,
	entities INTEGER NOT NULL,
	analyzerid INT NOT NULL,
	projectname TEXT NOT NULL, 
	path TEXT NOT NULL,
	language TEXT NOT NULL,
	insertdate datetime default current_timestamp,
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)
);

INSERT INTO  "ANALYZER" ("name") VALUES ('Analyzer');
INSERT INTO  "ANALYZER" ("name") VALUES ('PMD');
INSERT INTO  "ANALYZER" ("name") VALUES ('SpringLint');
INSERT INTO  "ANALYZER" ("name") VALUES ('JSpIRIT');