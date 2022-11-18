{\rtf1\ansi\ansicpg1252\cocoartf2706
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;\f1\fnil\fcharset0 Menlo-Regular;}
{\colortbl;\red255\green255\blue255;\red70\green70\blue70;\red254\green254\blue251;\red0\green0\blue0;
}
{\*\expandedcolortbl;;\cssrgb\c34510\c34510\c34510;\cssrgb\c99608\c99608\c98824;\csgray\c0;
}
\margl1440\margr1440\vieww11520\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 CREATE TABLE ANALYZER(\
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\
 	name TEXT NOT NULL UNIQUE\
);\
\
CREATE TABLE GODCLASS(\
	id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\
	classname TEXT NOT NULL,\
	wmc INTEGER NOT NULL,\
	atfd INTEGER NOT NULL,\
	tcc REAL NOT NULL,\
	analyzerid INT NOT NULL,\
	projectname TEXT NOT NULL, \
	path TEXT NOT NULL,\
	insertdate datetime default current_timestamp
\f1\fs28 \cf2 \cb3 \expnd0\expndtw0\kerning0
,
\f0\fs24 \cf0 \cb1 \kerning1\expnd0\expndtw0 \
	FOREIGN KEY(analyzerid) REFERENCES ANALYZER(id)\
);\
\
\pard\tx560\tx1120\tx1680\tx2240\tx2800\tx3360\tx3920\tx4480\tx5040\tx5600\tx6160\tx6720\pardirnatural\partightenfactor0

\f1\fs22 \cf4 \CocoaLigature0 INSERT INTO  "ANALYZER" ("name") VALUES ('Analyzer');\
INSERT INTO  "ANALYZER" ("name") VALUES ('PMD');}